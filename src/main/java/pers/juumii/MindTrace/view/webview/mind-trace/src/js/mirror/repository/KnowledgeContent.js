import { reactive } from "vue"
import { getSelectedKNode, updateSelectedKNode } from "./RepositoryPage"
import { getKNode, updateKNode, getQuizCard, general } from "../general"
import constants from "@/js/constants"

export const KnowledgeContent = reactive({
    toBeShifted: {},
    toBePasted: {},
    quizCardToBeShifted: undefined,
    pageSelected:"Quiz"
})


export const shearSelectedKNode = ()=>{
    KnowledgeContent.toBeShifted = getSelectedKNode()
}

export const shiftToSelectedKNode = async ()=>{
    //若没有要转移的节点，则直接返回；
    //若转移的节点和选中的节点一样，则直接返回
    if(KnowledgeContent.toBeShifted == {} || KnowledgeContent.toBeShifted.data.id == getSelectedKNode().data.id)
        return
    let superKNode = getKNode(KnowledgeContent.toBeShifted.data.superKnowledgeId)
    //将原来的节点挂载到当前选中节点的子节点中
    KnowledgeContent.toBeShifted.data.superKnowledgeId = getSelectedKNode().data.id
    getSelectedKNode().subKNodes.push(KnowledgeContent.toBeShifted)
    //将原来的节点和原来节点的父节点解绑
    superKNode.subKNodes.splice(superKNode.subKNodes.indexOf(KnowledgeContent.toBeShifted),1)
    console.log("superKNode",superKNode)
    //更新
    await updateKNode(general.kRoot)
}
export const copySelectedKNode = ()=>{
    //深拷贝
    KnowledgeContent.toBePasted = JSON.parse(JSON.stringify(getSelectedKNode()))
    KnowledgeContent.toBePasted.data.id = Date.parse(new Date())
    console.log("data copied:",KnowledgeContent.toBePasted, getSelectedKNode())
}
export const pasteToSelectedKNode = async ()=>{
    if(KnowledgeContent.toBePasted.data == undefined || KnowledgeContent.toBePasted.data.id == getSelectedKNode().data.id)
        return
        KnowledgeContent.toBePasted.data.superKnowledgeId = getSelectedKNode().data.id
    getSelectedKNode().subKNodes.push(KnowledgeContent.toBePasted)
    await updateKNode(KnowledgeContent.kRoot)
    KnowledgeContent.toBePasted = {}
}

export const shearSelectedQuizCard = (id)=>{
    KnowledgeContent.quizCardToBeShifted = getQuizCard(id)
}

export const shiftSelectedQuizCard = async ()=>{
    constants.arrayRemove(getKNode(KnowledgeContent.quizCardToBeShifted.knowledgeId).data.quizCards, KnowledgeContent.quizCardToBeShifted)
    await updateKNode(getKNode(KnowledgeContent.quizCardToBeShifted.knowledgeId))
    KnowledgeContent.quizCardToBeShifted.knowledgeId = getSelectedKNode().data.id
    getSelectedKNode().data.quizCards.push(KnowledgeContent.quizCardToBeShifted)
    await updateSelectedKNode()
    KnowledgeContent.quizCardToBeShifted = undefined
}
