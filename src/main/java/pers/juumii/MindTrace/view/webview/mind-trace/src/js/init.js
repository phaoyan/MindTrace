import data from "./data"
import operation from "./operation"
import request from "./request"

export const init = async () => {
    await operation.getRoot()
    await request.loadKTreeNames()
    await request.loadCurrentKTreeName()
    await request.loadSettings()
    await request.loadKTreeConfigs()
    await request.loadQuizIsDue()
    await request.loadQuiz()
    await request.loadQuizFinished()
    await request.getStatistics('quizCardsSortedByEstablishment').then(e=>{data.Home.MindTrace.quizCardTrace=e})
    await request.getStatistics('overviewMarkdown').then(e=>{data.Home.MindTrace.overviewMarkdown = e; console.log(data.Home.MindTrace.overviewMarkdown)})
    
    data.Home.LearningTask.kTreeOptions = operation.renderKTreeOptions()
}
