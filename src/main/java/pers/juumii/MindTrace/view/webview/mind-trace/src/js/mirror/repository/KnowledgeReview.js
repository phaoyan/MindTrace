import { reactive } from "vue"
import { getSelectedKNode } from "./RepositoryPage"

export const KnowledgeReview = reactive({
    pageSelected:'Poor Completion',
    poorlyCompletedQuizCards:[]
})

export const init = ()=>{
    KnowledgeReview.poorlyCompletedQuizCards = getSelectedKNode().poorRecordQuizCards
    console.log("poor record quiz cards", KnowledgeReview.poorlyCompletedQuizCards)
}
