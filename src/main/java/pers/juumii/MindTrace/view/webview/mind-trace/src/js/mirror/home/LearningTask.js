import { reactive } from "vue"
import constants from "@/js/constants"
import { general, getProtoType, synchronizeKTreeConfigs } from "../general"
import { renderKTreeOptions } from "../repository/KnowledgeFolder"

export const LearningTask = reactive({
    startTime: undefined,
    description:'',
    kTreeOptions:[],
    selectedKNodes:[],
    nowTime: constants.nowTime()
})

export const addRecord = async ()=>{
    await getProtoType('learningRecord').then(record=>{
        console.log("record",record)
        record.startTime = constants.nowDate()+'T'+LearningTask.startTime
        record.finishTime = constants.now()
        record.description = LearningTask.description
        LearningTask.selectedKNodes.forEach(item=>record.concernedKnowledgeIds.push(item.value))
        general.kTreeConfigs.learningRecords.push(record)
        synchronizeKTreeConfigs()
    })
    LearningTask.startTime = undefined
    LearningTask.selectedKNodes = []
}

export const init = async ()=>{
    LearningTask.kTreeOptions = renderKTreeOptions()
    console.log("learing task options", LearningTask.kTreeOptions)
}