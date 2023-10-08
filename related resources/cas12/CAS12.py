from Bio import SeqIO
import os
import random
import sys
import numpy as np
from scipy.stats import spearmanr
from tensorflow.keras.models import Model
from tensorflow.keras.layers import Input
from tensorflow.keras.layers import Multiply
from tensorflow.keras.layers import Dense, Dropout, Activation, Flatten
from tensorflow.keras.layers import Convolution1D, AveragePooling1D
from tensorflow.keras.optimizers import Adam
from tensorflow.keras.callbacks import TensorBoard
from tensorflow.keras.models import load_model

def main():
    # print("Usage: python CAS12.py input.txt output.txt")
    # print("input.txt must include 4 columns with single header row")
    # print("\t1st column: sequence index")
    # print("\t2nd column: 34bp target sequence")
    # print("\t3nd column: PAM start position")
    # print("\t4rd column: binary chromain information of the target sequence\n")        

    # print("CAS12 currently requires python=3.8, tensorflow=2.5.0, keras=2.5.0")
    # print("CAS12 available requires pre-obtained binary chromatin information (DNase-seq narraow peak data from ENCODE)")
    # print("Viral nucleic acid is exposed, the default chromatin accessibility in DeepCpf1 is 1\n")

    # print("Building models")
    DeepCpf1_Input_SEQ = Input(shape=(34,4))
    DeepCpf1_C1 = Convolution1D(80, 7, activation='relu')(DeepCpf1_Input_SEQ)
    DeepCpf1_P1 = AveragePooling1D(2)(DeepCpf1_C1)
    DeepCpf1_F = Flatten()(DeepCpf1_P1)
    DeepCpf1_DO1= Dropout(0.3)(DeepCpf1_F)
    DeepCpf1_D1 = Dense(80, activation='relu')(DeepCpf1_DO1)
    DeepCpf1_DO2= Dropout(0.3)(DeepCpf1_D1)
    DeepCpf1_D2 = Dense(40, activation='relu')(DeepCpf1_DO2)
    DeepCpf1_DO3= Dropout(0.3)(DeepCpf1_D2)
    DeepCpf1_D3_SEQ = Dense(40, activation='relu')(DeepCpf1_DO3)
    
    DeepCpf1_Input_CA = Input(shape=(1,))
    DeepCpf1_D3_CA = Dense(40, activation='relu')(DeepCpf1_Input_CA)
    DeepCpf1_M = Multiply()([DeepCpf1_D3_SEQ, DeepCpf1_D3_CA])
    
    DeepCpf1_DO4= Dropout(0.3)(DeepCpf1_M)
    DeepCpf1_Output = Dense(1, activation='linear')(DeepCpf1_DO4)
    DeepCpf1 = Model(inputs=[DeepCpf1_Input_SEQ, DeepCpf1_Input_CA], outputs=[DeepCpf1_Output])

    # print("Loading weights for the models")
    DeepCpf1.load_weights('weights/CAS12_weights.h5')

    # print("Loading test data")#输入fasta文件
    
    FILE = open(sys.argv[2], "r")
    data = FILE.readlines()
    SEQ, CA = PREPROCESS(data)
    FILE.close()

    # print("Predicting on test data")
    DeepCpf1_SCORE = DeepCpf1.predict([SEQ, CA], batch_size=50, verbose=0) *3

    # print("Saving to " + sys.argv[3])
    OUTPUT = open(sys.argv[3], "w")
    for l in range(len(data)):
        if l == 0:
            OUTPUT.write(data[l].strip())
            OUTPUT.write("\tDeepCpf1 Score\n")
        else:
            OUTPUT.write(data[l].strip())
            OUTPUT.write("\t%f\n" % (DeepCpf1_SCORE[l-1]))
    OUTPUT.close()
    
    #print("Top 10 gRNAs:\n ")
    result=grna_sort(sys.argv[3])
    for grna, start_position,score in result:
        print(f'cas:{grna}\t{start_position}\t{score}')

#处理输入文件，提取序列与染色质可及性列，即input文件的第2列和第4列
def PREPROCESS(lines):
    data_n = len(lines) - 1
    SEQ = np.zeros((data_n, 34, 4), dtype=int)
    CA = np.zeros((data_n, 1), dtype=int)
    
    for l in range(1, data_n+1):
        data = lines[l].split()
        seq = data[1]
        seq = seq
        for i in range(34):
            if seq[i] in "Aa":
                SEQ[l-1, i, 0] = 1
            elif seq[i] in "Cc":
                SEQ[l-1, i, 1] = 1
            elif seq[i] in "Gg":
                SEQ[l-1, i, 2] = 1
            elif seq[i] in "Tt":
                SEQ[l-1, i, 3] = 1
        CA[l-1,0] = int(data[3])

    return SEQ, CA

#根据grna得分排序，输出前十名
def grna_sort(file):
    fo=open(file,'r')
    grnas=[]
    for line in fo:
        if line[0]!='T':
            field=line.split()
            grna=field[1][4:31]
            start_position=field[2]
            score=float(field[4])
            grnas.append((grna,start_position,score))
    from operator import itemgetter
    sorted_grnas = sorted(grnas[1:], key=itemgetter(2), reverse=True)
    top_10_grnas = sorted_grnas[:10]
    return top_10_grnas

# 读取fasta文件
def read_fasta(filename):
    seq_dict = {}
    for record in SeqIO.parse(filename, "fasta"):
        complement_sequence = get_complement(record.seq)
        seq_dict[record.id] = str(complement_sequence)
    return seq_dict

#转换互补链（逆转录）
def get_complement(sequence):
    complement_dict = {'A': 'T', 'T': 'A', 'C': 'G', 'G': 'C', 'N': 'N', 'S': 'S', 'R': 'R', 'K': 'K', 'Y': 'Y'}
    complement_sequence = ''.join([complement_dict[base] for base in sequence])
    return complement_sequence

#搜索pam序列
def find_pam_sequence(seq):
    pam_seq = ['TTTA','TTTT','TTTC','TTTG']#cas12的PAM序列‘TTTN’，是cas12识别的基础
    for pam in pam_seq:
        indexes = [i for i in range(len(seq)) if seq.startswith(pam, i)]
        target_sequences = []
        for index in indexes:
            if len(seq) - (index + 34) >= 0: # 判断剩余序列的长度是否足够
                target_sequences.append(seq[index-4:index+len(pam)+26])#提取PAM序列周围的34bp序列为target_sequence
    return indexes,target_sequences

if __name__ == '__main__':
    #将fasta文件写成模型输入的标准格式
    inputfile=open(sys.argv[2],'w')
    inputfile.write('Target number'+'\t'+'34 bp target sequence (4 bp + PAM + 23 bp protospacer + 3 bp)'+'\t'+'start position'+'\t'+"Chromatin accessibility (1= DNase I hypersensitive sites, 0 = Dnase I non-sensitive sites)"+'\n')#列名
    seq_dict=read_fasta(sys.argv[1])#输入病毒序列fasta文件
    for seq_id, seq in seq_dict.items():
        indexes,SEQ = find_pam_sequence(seq)
    for i, sequence in enumerate(SEQ):
        inputfile.write(f'{i+1}\t{sequence}\t{indexes[i]}\t{1}\n')#病毒染色质裸露，可及性都取1
    inputfile.close()
    main()