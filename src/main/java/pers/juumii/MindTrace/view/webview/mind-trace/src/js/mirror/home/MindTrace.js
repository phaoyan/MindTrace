import { reactive } from "vue"
import { getKTreeInfo } from "../general"
import  moment from 'moment'
import constants from "@/js/constants"

export const MindTrace = reactive({
    overviewMarkdown: '',
    learningRecordsGroupedByDate:{},
    learningOverviews:{},
    pageSelected:'GeneralInfo'
})

export const getLearningRecordsOfDay = (date)=>{
    if(MindTrace.learningRecordsGroupedByDate[moment(date).toISOString().split('T')[0]] != undefined)
        return MindTrace.learningRecordsGroupedByDate[moment(date).toISOString().split('T')[0]]
    return []
}

export const getLearningTimeSpentOfDay = (date)=>{
    let total = 0
    getLearningRecordsOfDay(date).forEach(record => {
        total += record.duration
    });
    return constants.durationTransferToString(total)
}

export const getLearningOverviewOfDay = (date)=>{
    return MindTrace.learningOverviews[moment(date).toISOString().split('T')[0]]
}

export const init = async ()=>{
    await getKTreeInfo().then(kTree=>{
        MindTrace.overviewMarkdown = kTree.overviewMarkdown
        MindTrace.learningRecordsGroupedByDate = kTree.learningRecordsGroupedByDate
        MindTrace.learningOverviews = kTree.learningOverviews
        console.log("MindTrace.js init ...",MindTrace.overviewMarkdown, MindTrace.learningRecordsGroupedByDate)
    })
}