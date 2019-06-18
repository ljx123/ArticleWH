#!/usr/bin/env python3
# -*- coding: utf-8 -*-

' a function module '

__author__ = 'heron'


import copy


def getSubjectStartIndex(elem):
    return elem[1][1][0]

def getSubjectStopIndex(elem):
    return elem[1][1][1]

def shrinkToLeft(elem , offset):
    new_elem = []
    new_source_tuple = (elem[1][0][0] , elem[1][0][1] - offset)
    new_compared_tuple = (elem[1][1][0] , elem[1][1][0] - offset)
    new_elem.append(new_source_tuple)
    new_elem.append(new_compared_tuple)
    return (elem[0] , new_elem)

def shrinkToRight(elem , offset):
    new_elem = []
    new_source_tuple = (elem[1][0][0] + offset , elem[1][0][1])
    new_compared_tuple = (elem[1][1][0] + offset , elem[1][1][1])
    new_elem.append(new_source_tuple)
    new_elem.append(new_compared_tuple)
    return (elem[0] , new_elem)

def arrangeSubjects(list , duplicate_min_len):
    list_len = len(list)
    list_local = copy.deepcopy(list)
    #先按照传入元素的子集的开始位置进行排序
    list_local.sort(key=getSubjectStartIndex)
    # print('/////////////////////////////////////')
    #
    # print(list_local)
    # print('/////////////////////////////////////')
    #去掉所有子集中被其它子集覆盖的子集
    list_local_len = len(list_local)
    i = 0
    while i < list_local_len:
        j = i +1
        while j < list_local_len:
            # dfsa
            if getSubjectStopIndex(list_local[j]) <= getSubjectStopIndex(list_local[i]):
                list_local.pop(j)
                list_local_len = len(list_local)
                continue
            if getSubjectStartIndex(list_local[j]) <= getSubjectStartIndex(list_local[i]) and getSubjectStopIndex(list_local[j]) > getSubjectStopIndex(list_local[i]):
                list_local.pop(i)
                list_local_len = len(list_local)
                i -= 1
                break
            j += 1
        i += 1

    #再将subject分层
    list_layers = []
    list_tmp = []
    l = 0
    list_local_len = len(list_local)
    while l <= list_local_len:
        if len(list_local) == 0:
            if len(list_tmp) > 0:
                list_layers.append(list_tmp)
                list_tmp = []
            break
        if l == len(list_local):
            l = 0
            if len(list_tmp) > 0:
                list_layers.append(list_tmp)
                list_tmp = []
            continue
        if l == 0 and len(list_tmp) == 0:
            list_tmp.append(list_local[0])
            list_local.pop(0)
            list_local_len = len(list_local)
            continue

        if getSubjectStartIndex(list_local[l]) > getSubjectStopIndex(list_tmp[-1]):
            list_tmp.append(list_local[l])
            list_local.pop(l)
            list_local_len = len(list_local)
            continue

        l += 1

    #合并各分层
    #1 添加一个逻辑子集
    list_sub_logic = []
    # list_result = copy.deepcopy(list_layers[0])
    for elem in list_layers[0]:
        list_sub_logic.append((getSubjectStartIndex(elem) , getSubjectStopIndex(elem)))
    print('/////////////////////////////////////')
    for elem in list_layers:
        print(elem)

    print('/////////////////////////////////////')
    #2 循环合并
    for i in range(len(list_layers)):
        if i ==0 :
            continue
        for elem1 in list_layers[i]:
            for j in range(len(list_layers[0])):
                if getSubjectStartIndex(elem1) >= getSubjectStartIndex(list_layers[0][j]):
                    if j+1<len(list_layers[0]) and (getSubjectStartIndex(list_layers[0][j+1]) - getSubjectStopIndex(list_layers[0][j])) > duplicate_min_len:
                        if getSubjectStopIndex(elem1) >= getSubjectStartIndex(list_layers[0][j+1]):
                            left_offset = getSubjectStopIndex(list_layers[0][j]) - getSubjectStartIndex(elem1) + 1
                            right_offset = getSubjectStopIndex(elem1) - getSubjectStartIndex(list_layers[0][j+1]) + 1
                            list_layers[0].insert(j+1 , shrinkToRight(shrinkToLeft(elem1 , right_offset)) , left_offset)
                        elif getSubjectStopIndex(elem1) < getSubjectStartIndex(list_layers[0][j+1]):
                            if getSubjectStopIndex(elem1) - getSubjectStopIndex(list_layers[0][j]) > duplicate_min_len:
                                left_offset = getSubjectStopIndex(list_layers[0][j]) - getSubjectStartIndex(elem1) + 1
                                list_layers[0].insert(j+1 , shrinkToRight(elem1 , left_offset))
                    elif j + 1 == len(list_layers[0]):
                        left_offset = getSubjectStopIndex(list_layers[0][j]) - getSubjectStartIndex(elem1) + 1
                        list_layers[0].insert(j + 1, shrinkToRight(elem1, left_offset))

                    break
                else:
                    continue

    return list_layers[0]








# list_1 = ['dfa','dfa','dfa','dfa']
# list_2 = list_1
#
# list_2.append('hfsda')
#
# print(list_1 )
# print(list_2 )

