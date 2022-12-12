import request from './request'
import data from './data'
import constants from './constants'

const getRoot = async () => {
    await request.loadKNode(0).then(e => data.kRoot = e)
}


const getKNode = (id, kNode=data.kRoot)=>{
    if(id == kNode.data.id)
        return kNode
    else for(let i in kNode.subKNodes){
        let subKNode = getKNode(id, kNode.subKNodes[i])
        if(subKNode != undefined)
            return subKNode
    }
}
const getKNodes = (ids)=>{
    let res = []
    ids.forEach(id => {if(getKNode(id) != undefined) res.push(getKNode(id))})
    console.log('getKNodes', res, ids)
    return res
}
const getQuizCard = (id, kNode=data.kRoot)=>{
    for(let i in kNode.data.quizCards)
        if(kNode.data.quizCards[i].id == id)
            return kNode.data.quizCards[i]
    for(let i in kNode.subKNodes)
        if(getQuizCard(id, kNode.subKNodes[i]) != undefined)
            return getQuizCard(id, kNode.subKNodes[i])
}
const getLearningCard = (id, kNode=data.kRoot)=>{
    for(let i in kNode.data.learningCards)
        if(kNode.data.learningCards[i].id == id)
            return kNode.data.learningCards[i]
    for(let i in kNode.subKNodes)
        if(getLearningCard(id, kNode.subKNodes[i]) != undefined)
            return getLearningCard(id, kNode.subKNodes[i])
    for(let i in data.kTreeConfigs.learningRepository)
        if(data.kTreeConfigs.learningRepository[i].id == id)
            return data.kTreeConfigs.learningRepository[i]
}
const getLearningCards = (kNode=data.kRoot)=>{
    let res = []
    res = res.concat(kNode.data.learningCards)
    kNode.subKNodes.forEach(kNode => res = res.concat(getLearningCards(kNode)))
    if(kNode == data.kRoot)
        console.log('learningCards', res)
    return res
}
const updateKNode = async (kNode) => {
    await request.synchronizeKNode(kNode)
}
const updateKNodeById = async (id) =>{
    console.log("update knode by id:", id, getKNode(id))
    await updateKNode(getKNode(id))
}
const updateSelectedKNode = async ()=>{
    await request.synchronizeKNode(getSelectedKNode())
    await getRoot()
}
const getSelectedKNode = ()=>{
    let res = data.kRoot
    data.Repository.selectedIndexes.forEach(index =>{
        res.subKNodes.forEach(kNode =>{
            if(kNode.data.id == index)
                res = kNode
        })
    })
    return res
}
const updateSelectedIndexes = async (selectIndexes) => {
    data.Repository.selectedIndexes = selectIndexes
    console.log("update selected:")
    console.log(data.Repository.selectedIndexes)
    console.log(getSelectedKNode())
}
const updateSelectedIndexesByKnowledgeId = async(id)=>{
    let res = []
    let curKNode = getKNode(id)
    while(curKNode.data.id != 0){
        res.push(curKNode.data.id)
        curKNode = getKNode(curKNode.data.superKnowledgeId)
    }
    res.push(0)
    updateSelectedIndexes(res.reverse())
}
const shearSelectedKNode = ()=>{
    data.Repository.toBeShifted = getSelectedKNode()
}

const shiftToSelectedKNode = async ()=>{
    //若没有要转移的节点，则直接返回；
    //若转移的节点和选中的节点一样，则直接返回
    if(data.Repository.toBeShifted == {} || data.Repository.toBeShifted.data.id == getSelectedKNode().data.id)
        return
    let superKNode = getKNode(data.Repository.toBeShifted.data.superKnowledgeId)
    //将原来的节点挂载到当前选中节点的子节点中
    data.Repository.toBeShifted.data.superKnowledgeId = getSelectedKNode().data.id
    getSelectedKNode().subKNodes.push(data.Repository.toBeShifted)
    //将原来的节点和原来节点的父节点解绑
    superKNode.subKNodes.splice(superKNode.subKNodes.indexOf(data.Repository.toBeShifted),1)
    console.log("superKNode",superKNode)
    //更新
    await updateKNode(data.kRoot)
}
const copySelectedKNode = ()=>{
    //深拷贝
    data.Repository.toBePasted = JSON.parse(JSON.stringify(getSelectedKNode()))
    data.Repository.toBePasted.data.id = Date.parse(new Date())
    console.log("data copied:",data.Repository.toBePasted, getSelectedKNode())
}
const pasteToSelectedKNode = async ()=>{
    if(data.Repository.toBePasted.data == undefined || data.Repository.toBePasted.data.id == getSelectedKNode().data.id)
        return
    data.Repository.toBePasted.data.superKnowledgeId = getSelectedKNode().data.id
    getSelectedKNode().subKNodes.push(data.Repository.toBePasted)
    await updateKNode(data.kRoot)
    data.Repository.toBePasted = {}
}

const shearSelectedQuizCard = (id)=>{
    data.Repository.quizCardToBeShifted = getQuizCard(id)
}

const shiftSelectedQuizCard = async ()=>{
    constants.arrayRemove(getKNode(data.Repository.quizCardToBeShifted.knowledgeId).data.quizCards, data.Repository.quizCardToBeShifted)
    await updateKNode(getKNode(data.Repository.quizCardToBeShifted.knowledgeId))
    data.Repository.quizCardToBeShifted.knowledgeId = getSelectedKNode().data.id
    getSelectedKNode().data.quizCards.push(data.Repository.quizCardToBeShifted)
    await updateSelectedKNode()
    data.Repository.quizCardToBeShifted = undefined
}
const linkToKnowledge = async (id)=>{
    updateSelectedIndexesByKnowledgeId(id)
    getRoot()
    data.AppVue.pageSelected = 'repository'
}

const renderKTreeOptions = (kroots=[data.kRoot])=>{
    // console.log("render options ... ")
    let options = []
    for(let index in kroots){
        let knode = kroots[index]
        if(knode.data != null){
            options.push({
                value: knode.data.id,
                label: knode.data.description,
                children: renderKTreeOptions(knode.subKNodes)
            })
        }
    }
    return options
}

export default {
    getRoot,
    getKNode,
    getKNodes,
    getSelectedKNode,
    getQuizCard,
    getLearningCard,
    getLearningCards,
    updateKNode,
    updateKNodeById,
    updateSelectedIndexes,
    updateSelectedIndexesByKnowledgeId,
    updateSelectedKNode,
    shearSelectedKNode,
    shiftToSelectedKNode,
    copySelectedKNode,
    pasteToSelectedKNode,
    shearSelectedQuizCard,
    shiftSelectedQuizCard,
    linkToKnowledge,
    renderKTreeOptions
}