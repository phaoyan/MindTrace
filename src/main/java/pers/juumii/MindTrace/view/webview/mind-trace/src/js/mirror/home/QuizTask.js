import { reactive } from "vue"
import axios from "axios"
import constants from "@/js/constants"
import { getKNode, getQuizCard, getProtoType, updateKNodeById } from "../general"

export const QuizTask = reactive({
    quizIsDue: false,
    quizCards: [],
    finished: [],
    pageSelected:'QuizTask'
})

export const loadQuizIsDue = async () => {
    await axios.get(constants.backHost + "utils/quiz/isDue").then(e => {
        QuizTask.quizIsDue = e.data
    })
    console.log('quiz is due ? ', QuizTask.quizIsDue)
}

export const loadQuiz = async () => {
    QuizTask.quizCards.splice(0, QuizTask.quizCards.length)
    await axios.get(constants.backHost + "utils/quiz/generate").then(e => {
        for (let i in e.data)
            QuizTask.quizCards.push(e.data[i])
    })
    console.log('quiz generated: ', QuizTask.quizCards)
}

export const loadQuizFinished = async () => {
    await loadQuizIsDue()
    QuizTask.finished.splice(0, QuizTask.finished.length)
    await axios.get(constants.backHost + "utils/quiz/finished").then(e => {
        for (let i in e.data)
            QuizTask.finished.push(e.data[i])
    })
    console.log('quiz finished: ', QuizTask.finished)
}

export const onCompletionSelected = async (selection, cardCopy) => {
    let card = getQuizCard(cardCopy.id)
    console.log("Selecetd Card:", card)

    let completion = selection == 'difficult' ? 30 : selection == 'good' ? 70 : selection == 'perfect' ? 100 : 0
    await getProtoType('quizRecord').then(record => {
        record.cardId = card.id
        record.completion = completion
        record.description = selection
        card.quizRecords.push(record)
        console.log("quiz recorded:", card, getKNode(card.knowledgeId))
    })
    await updateKNodeById(card.knowledgeId)
    for (let i in QuizTask.quizCards)
        if (QuizTask.quizCards[i].id == card.id)
            QuizTask.quizCards.splice(i, 1)
    await loadQuizFinished()
}

export const init = async () => {
    await loadQuizIsDue()
    await loadQuiz()
    await loadQuizFinished()
}
