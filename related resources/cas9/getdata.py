import numpy as np
import pandas as pd
from functools import reduce
from operator import add

#序列及表观遗传信息编码
ntcode={'A':(1,0,0,0),
        'T':(0,1,0,0),
        'C':(0,0,1,0),
        'G':(0,0,0,1)}

epicode={'A':1,'N':0}

def get_seqcode(seq):
    return np.array(reduce(add,map(lambda x: ntcode[x], seq.upper()))).reshape(1,len(seq),-1)

def get_epicode(seq):
    return np.array([[epicode[i]] for i in seq]).reshape(1,len(seq),1)

class grna:
    def __init__(self,filepath,with_y=True):
        self.path=filepath
        self.ori_data=pd.read_csv(filepath,header=None)
        self.with_y=with_y
        self.col_num=2 if with_y==True else 1
        self.col=list(self.ori_data.columns)[-self.col_num:]
        self.data=self.ori_data[self.col]

    def get_dataset(self,x_type=np.float32,y_type=np.float32):
        #返回的x为大小为(n,4,23)的三维矩阵
        seq_set=np.concatenate(list(map(get_seqcode,self.data[self.col[0]])))
        x=seq_set.astype(x_type)
        x = x.transpose(0, 2, 1)
        if self.with_y:
            y=np.array(self.data[self.col[-1]]).astype(y_type)
            return x,y
        else:
             return x
        
class epigrna:
    def __init__(self,filepath,epi_feature=4,with_y=True):
        self.path=filepath
        self.ori_data=pd.read_csv(filepath,header=None)
        self.with_y=with_y
        self.epi_fea=epi_feature
        self.col_num=2+epi_feature if with_y==True else 1+epi_feature
        self.col=list(self.ori_data.columns)[-self.col_num:]
        self.data=self.ori_data[self.col]

    def get_dataset(self,x_type=np.float32,y_type=np.float32):
        episeq_set=np.concatenate(list(map(get_seqcode,self.data[self.col[0]])))
        #迭代表观遗传特征序列拼接产生矩阵
        for c in self.col[1:self.col_num+1]:
            epi_seq=np.concatenate(list(map(get_epicode,self.data[c])))
            episeq_set=np.concatenate((episeq_set,epi_seq),axis=2)
        x=episeq_set.astype(x_type)
        x = x.transpose(0, 2, 1)
        if self.with_y:
            y=np.array(self.data[self.col[-1]]).astype(y_type)
            return x,y
        else:
            return x



