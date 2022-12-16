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
    //��û��Ҫת�ƵĽڵ㣬��ֱ�ӷ��أ�
    //��ת�ƵĽڵ��ѡ�еĽڵ�һ������ֱ�ӷ���
    if(KnowledgeContent.toBeShifted == {} || KnowledgeContent.toBeShifted.data.id == getSelectedKNode().data.id)
        return
    let superKNode = getKNode(KnowledgeContent.toBeShifted.data.superKnowledgeId)
    //��ԭ���Ľڵ���ص���ǰѡ�нڵ���ӽڵ���
    KnowledgeContent.toBeShifted.data.superKnowledgeId = getSelectedKNode().data.id
    getSelectedKNode().subKNodes.push(KnowledgeContent.toBeShifted)
    //��ԭ���Ľڵ��ԭ���ڵ�ĸ��ڵ���
    superKNode.subKNodes.splice(superKNode.subKNodes.indexOf(KnowledgeContent.toBeShifted),1)
    console.log("superKNode",superKNode)
    //����
    await updateKNode(general.kRoot)
}
export const copySelectedKNode = ()=>{
    //���
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
