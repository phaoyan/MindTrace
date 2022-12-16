import { reactive } from "vue"
import { QuizTask, loadQuizIsDue } from "./QuizTask"

export const HomePage = reactive({
    pageSelected:''
})

export const init = async()=>{
    await loadQuizIsDue()
    if(QuizTask.quizIsDue == true)
        HomePage.pageSelected = 'QuizTask'
    else HomePage.pageSelected = 'LearningTask'
}
