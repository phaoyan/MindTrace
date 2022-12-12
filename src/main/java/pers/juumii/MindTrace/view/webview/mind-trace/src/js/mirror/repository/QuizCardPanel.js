import { reactive } from "vue"
import { getSelectedKNode, updateSelectedKNode } from "./RepositoryPage"
import { getQuizCard, getProtoType, getKNode, updateKNode } from "../general"
import constants from "@/js/constants"

export const QuizCardPanel = reactive({
    quizCardToBeShifted:undefined
})

export const removeCard = (card)=>{
    //使用splice删除这张卡片
    getSelectedKNode().data.quizCards.splice(getSelectedKNode().data.quizCards.indexOf(card),1)
    updateSelectedKNode()
}

export const addCard = async()=>{
    await getProtoType('quizCard').then(e=>{
        e.knowledgeId = getSelectedKNode().data.id
        getSelectedKNode().data.quizCards.push(e)
        console.log("addCard", getSelectedKNode().data.quizCards)
    })
    updateSelectedKNode()
}

export const shearSelectedQuizCard = (id)=>{
    QuizCardPanel.quizCardToBeShifted = getQuizCard(id)
}

export const shiftSelectedQuizCard = async ()=>{
    constants.arrayRemove(getKNode(QuizCardPanel.quizCardToBeShifted.knowledgeId).data.quizCards, QuizCardPanel.quizCardToBeShifted)
    await updateKNode(getKNode(QuizCardPanel.quizCardToBeShifted.knowledgeId))
    QuizCardPanel.quizCardToBeShifted.knowledgeId = getSelectedKNode().data.id
    getSelectedKNode().data.quizCards.push(QuizCardPanel.quizCardToBeShifted)
    await updateSelectedKNode()
    QuizCardPanel.quizCardToBeShifted = undefined
}

