# -*- coding: utf-8 -*-
"""
Created on Fri Jul 21 21:40:56 2023

@author: 潇筱
"""

import numpy as np
import sys
import os
import pickle

import fnn

import sklearn
import sklearn.metrics
import tensorflow as tf

def sliding(fasta):
    target_before = 10
    target = 28
    target_end = 10

    result = []
    gud_loc = []

    for index in range(len(fasta) - target_before - target - target_end + 1):
        target_index = index + target_before

        ta_be = one_hot(fasta[index : target_index], False)
        ta = one_hot(fasta[target_index : target_index + target], True)
        ta_end = one_hot(fasta[target_index + target : target_index + target + target_end], False)
        one_hot_result = np.vstack((ta_be, ta, ta_end))

        result.append(one_hot_result)
        gud_loc.append([fasta[target_index : target_index + target], target_index])

    return np.array(result), gud_loc

        
        
def one_hot(seq,flag):
    '''input:seq; 
        flag(whether it is connected)'''
    FASTA_CODES = {'A': set(('A')),
               'T': set(('T')),
               'C': set(('C')),
               'G': set(('G')),
               'K': set(('G', 'T')),
               'M': set(('A', 'C')),
               'R': set(('A', 'G')),
               'Y': set(('C', 'T')),
               'S': set(('C', 'G')),
               'W': set(('A', 'T')),
               'B': set(('C', 'G', 'T')),
               'V': set(('A', 'C', 'G')),
               'H': set(('A', 'C', 'T')),
               'D': set(('A', 'G', 'T')),
               'N': set(('A', 'T', 'C', 'G'))}
    onehot_idx = {'A': 0, 'C': 1, 'G': 2, 'T': 3}
    
    seq = seq.upper()
    ma = np.zeros((len(seq),8))
    
    for b in range(len(seq)):
        real_bases = FASTA_CODES[seq[b]]
        for b_real in real_bases:
            ma[b][onehot_idx[b_real]] = 1.0
        if flag:
            ma[b][4+onehot_idx[b_real]] = 1.0
    
    return ma
    
def read_data(loc):
    '''read data from fasta'''
    with open(loc,'r') as f:
       f = f.readlines()
       fasta = ''
       for i in f:
           if '>' in i:
               continue
           else:
               fasta += i.strip()
    return fasta

def load_model(load_path, params, x_train):
    """Construct model and load weights according to hyperparameter search.

    Args:
        load_path: path containing model weights
        params: dict of parameters
        x_train, y_train: train data (only needed for data shape and class
            weights)

    Returns:..
        fnn.CasCNNWithParallelFilters object
    """




    # First construct the model
    model = construct_model(params, x_train.shape,
            regression=True,
            compile_for_keras=True)

    
    

    # Note: Previoulsly, this would have to train the model on one
    # data point (reason below); however, this is no longer needed with Keras
    # See https://www.tensorflow.org/beta/guide/keras/saving_and_serializing
    # for details on loading a serialized subclassed model
    # To initialize variables used by the optimizers and any stateful metric
    # variables, we need to train it on some data before calling `load_weights`;
    # note that it appears this is necessary (otherwise, there are no variables
    # in the model, and nothing gets loaded)
    # Only train the models on one data point, and for 1 epoch

    def copy_weights(model):
        # Copy weights, so we can verify that they changed after loading
        return [tf.Variable(w) for w in model.weights]

    def weights_are_eq(weights1, weights2):
        # Determine whether weights1 == weights2
        for w1, w2 in zip(weights1, weights2):
            # 'w1' and 'w2' are each collections of weights (e.g., the kernel
            # for some layer); they are tf.Variable objects (effectively,
            # tensors)
            # Make a tensor containing element-wise boolean comparisons (it
            # is a 1D tensor with True/False)
            elwise_eq = tf.equal(w1, w2)
            # Check if all elements in 'elwise_eq' are True (this will make a
            # Tensor with one element, True or False)
            all_are_eq_tensor = tf.reduce_all(elwise_eq)
            # Convert the tensor 'all_are_eq_tensor' to a boolean
            all_are_eq = all_are_eq_tensor.numpy()
            if not all_are_eq:
                return False
        return True

    def load_weights(model, fn):
        # Load weights
        # There are some concerns about whether weights are actually being
        # loaded (e.g., https://github.com/tensorflow/tensorflow/issues/27937),
        # so check that they have changed after calling `load_weights`
        # Use expect_partial() to silence warnings because this will not
        # load optimizer parameters, which are loaded in construct_model()
        w_before = copy_weights(model)
        w_before2 = copy_weights(model)
        model.load_weights(os.path.join(load_path, fn)).expect_partial()
        w_after = copy_weights(model)
        w_after2 = copy_weights(model)

        assert (weights_are_eq(w_before, w_before2) is True)
        assert (weights_are_eq(w_before, w_after) is False)
        assert (weights_are_eq(w_after, w_after2) is True)

    load_weights(model, 'model.weights')

    return model
    


def construct_model(params, shape, regression=False, compile_for_keras=True,
        y_train=None, parallelize_over_gpus=False):
    """Construct model.

    This uses the fnn module.

    This can also compile the model for Keras, to use multiple GPUs if
    available.

    Args:
        params: dict of hyperparameters
        shape: shape of input data; only used for printing model summary
        regression: if True, perform regression; if False, classification
        compile_for_keras: if set, compile for keras
        y_train: training data to use for computing class weights; only needed
            if compile_for_keras is True and regression is False
        parallelize_over_gpus: if True, parallelize over all available GPUs

    Returns:
        fnn.CasCNNWithParallelFilters object
    """
    if not compile_for_keras:
        # Just return a model
        return fnn.construct_model(params, shape, regression=regression)

    def make():
        model = fnn.construct_model(params, shape, regression=regression)

        # Define an optimizer, loss, metrics, etc.
        if model.regression:
            # When doing regression, sometimes the output would always be the
            # same value regardless of input; decreasing the learning rate fixed this
            optimizer = tf.keras.optimizers.Adam(learning_rate=model.learning_rate)
            loss = 'mse'

            # Note that using other custom metrics like R^2, Pearson, etc. (as
            # implemented above) seems to raise errors; they are really only
            # needed during testing
            metrics = ['mse', 'mae']

            model.class_weight = None

        # Compile the model
        model.compile(optimizer=optimizer, loss=loss, metrics=metrics)
        return model

    if parallelize_over_gpus:
        # Use a MirroredStrategy to take advantage of multiple GPUs, if there are
        # multiple
        strategy = tf.distribute.MirroredStrategy()
        with strategy.scope():
            model = make()
    else:
        model = make()

    return model

def rank(gud_loc, y_pred,output_file):
    for i in range(len(gud_loc)):
        gud_loc[i].append(y_pred[i][0])
    guide_recommand = gud_loc
    sorted_list = sorted(guide_recommand, key=lambda x: x[2], reverse=True)  # 按照y_pred列排序
    result = sorted_list[:10]
    with open(output_file, 'w') as f:
        for i in result:
            i[2] = (i[2]+ 4) / 4.38465762437957  # 将 i[2] 转换为浮点数
            for j in range(1, 3):
                i[j] = str(i[j])
            i[2] = str(i[2])  # 将浮点数转换为字符串
            f.write(' '.join(i) + '\n')  # 将结果写入文件，每行一个结果
    for i in result:
        for j in range(1,3):
            i[j] = str(i[j])
    for i in result:
        print("cas:"+' '.join(i))


if __name__ == "__main__":
    loc = sys.argv[1]
    output_file=sys.argv[2]
    data = read_data(loc)

    one_hot_result,gud_loc = sliding(data)
    # Read saved parameters and load them into the args namespace

    load_path_params = os.path.join('model-49642642',
            'model.params.pkl')
    with open(load_path_params, 'rb') as f:
        saved_params = pickle.load(f)
    params = {}
    for k, v in saved_params.items():
        print("Setting argument '{}'={}".format(k, v))
        params[k] = v

    model = load_model('model-49642642', params, one_hot_result )
    y_pred = model.predict(one_hot_result)
    
    rank(gud_loc, y_pred,output_file)
