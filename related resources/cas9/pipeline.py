######################################
###############pipeline###############
######################################

import argparse
import os
import sys
from cnn import cnn

#接受fasta文件，读成一整串字符
def ReadFasta(filename):
    seq = []
    with open(filename,"r") as fa:
        while 1:
            line = fa.readline().strip()
            if line and line.startswith(">"):
                line = fa.readline().strip()
                seq.append(line)
            elif line:
                seq.append(line)
            else:
                break
    return "".join(seq).upper()

#寻找PAM位点并读取PAM和上游20nt
def FindPam(seq):
    pam = []
    for base in range(len(seq)):
        if seq[base] == "G" and seq[base+1] == "G":
            pam_seq = seq[base-21:base+2]
            if len(pam_seq) == 23:
                start = str(base-21)
                end = str(base+1)
                pam.append([start,end,pam_seq,'A'*23])

    return pam

#将PAM及其周边序列写成csv文件，一个序列一列
def WritePam(pam):
    with open("pam.csv","w") as fo:
        for i in pam:
            fo.write(','.join(i)+'\n')


if __name__ == "__main__":
    

    fastafile = sys.argv[1]
    outfile  = sys.argv[2]

    # if os.path.exists(fastafile) == False:
    #     print('fasta file do not exist')
    #     sys.exit()

    seq = ReadFasta(fastafile)
    pam = FindPam(seq)
    pam_coded = WritePam(pam)
    
    #将原间隔区输入模型
    from getdata import *
    import torch
    #无表观信息数据初始化
    filepath = 'pam.csv'
    input_data = epigrna(filepath,with_y=False,epi_feature=1)
    x = input_data.get_dataset()
    x = np.expand_dims(x,axis=2)   #shape=[n,5,1,23]
    x = torch.Tensor(x)

    model = cnn(5)
    model.load_state_dict(torch.load('model.pt', map_location=torch.device('cpu')))
    model.eval()
    output = model(x).squeeze()
    
    #排序显示输出结果
    output_list = output.tolist()
    pam_scored = list(zip(list(input_data.ori_data[list(input_data.ori_data.columns)[0]]), list(input_data.data[input_data.col[0]]), output_list))
    pam_scored_sorted = sorted(pam_scored, key=lambda x: x[2],reverse = True)
    fw = open(outfile,'w')
    print("Top 10 Results:")
    for i in range(10):
        print("cas:"+pam_scored_sorted[i][1]+' '+str(pam_scored_sorted[i][0])+' '+str(pam_scored_sorted[i][2]))
    for i in range(10):
        fw.write(str(pam_scored_sorted[i][0])+'\t'+pam_scored_sorted[i][1]+'\t'+str(pam_scored_sorted[i][2])+'\n')
    fw.close()
