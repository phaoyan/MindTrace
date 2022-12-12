import { reactive } from "vue"
import { getKTreeInfo } from "../general"

export const QuizTrace = reactive({
    quizCardTrace:[],
    currentPage:1
})

export const getQuizCardsThisPage = ()=>{
    return QuizTrace.quizCardTrace.slice((QuizTrace.currentPage - 1) * 10, QuizTrace.currentPage * 10)
}

export const init = async ()=>{
    await getKTreeInfo().then(kTree=>{QuizTrace.quizCardTrace=kTree.quizCardsSortedByEstablishment})
}