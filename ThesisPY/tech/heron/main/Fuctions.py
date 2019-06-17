import copy


def getSubjectStartIndex(elem):
    return elem[1][1][0]

def getSubjectStopIndex(elem):
    return elem[1][1][0]

def arrangeSubjects(list):
    list_len = len(list)
    list_local = copy.deepcopy(list)
    #先按照传入元素的子集的开始位置进行排序
    list_local.sort(key=getSubjectStartIndex)
    #去掉所有子集中被其它子集覆盖的子集
    i = 0
    while i < len(list_local):
        j = i +1
        while j < len(list_local):
            # dfsa
            if getSubjectStopIndex(list_local[j]) <= getSubjectStopIndex(list_local[i]):
                list_local.pop(j)
                continue
            if getSubjectStartIndex(list_local[j]) >= getSubjectStartIndex(list_local[i]) and getSubjectStopIndex(list_local[j]) > getSubjectStopIndex(list_local[i]):
                list_local.pop(i)
                i -= 1
                break
            j += 1
        i += 1

    #再将subject分层
    list_layers = []
    list_tmp = []
    l = 0
    while l <= len(list_local):
        if len(list_local) == 0:
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
            continue

        if getSubjectStartIndex(list_local[l]) < getSubjectStopIndex(list_tmp[-1]):
            list_tmp.append(list_local[l])
            list_local.pop(l)
            continue

        l += 1

    #合并各分层
    #1 添加一个逻辑子集
    list_sub_logic = []
    for elem in list_layers[0]:
        list_sub_logic.append((getSubjectStartIndex(elem) , getSubjectStopIndex(elem)))

    #2 循环合并
    







# list_1 = ['dfa','dfa','dfa','dfa']
# list_2 = list_1
#
# list_2.append('hfsda')
#
# print(list_1 )
# print(list_2 )

