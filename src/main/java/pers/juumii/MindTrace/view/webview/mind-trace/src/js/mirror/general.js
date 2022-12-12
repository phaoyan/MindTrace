import { reactive } from "vue"
import constants from "../constants"
import axios from "axios"
import { QuizTrace } from "./home/QuizTrace"
import { MindTrace } from "./home/MindTrace"

export const general = reactive({
    kRoot:{},
    kTreeList: [],
    selectedKTree: '',
    settings:{},
    kTreeConfigs:{}
})

export const init = async () => {
    await loadKRoot()
    await loadKTreeNames()
    await loadCurrentKTreeName()
    await loadSettings()
    await loadKTreeConfigs()
    await getKTreeInfo('quizCardsSortedByEstablishment').then(kTree=>{
        QuizTrace.quizCardTrace = kTree.quizCardsSortedByEstablishment
        MindTrace.overviewMarkdown = kTree.overviewMarkdown
        MindTrace.learningRecordsGroupedByDate = kTree.learningRecordsGroupedByDate
        MindTrace.learningOverviews = kTree.learningOverviews
    })
}

export const getKTreeInfo = async ()=>{
    let res
    await axios.get(constants.backHost + "kTree/info").then(e=>res=e.data)
    console.log("get kTree info",res)
    return res
}

export const loadKRoot = async ()=>{
    await axios.get(constants.backHost + "KNode/load?id=0").then((e) => {
        general.kRoot = e.data
        console.log("getKNode success: ")
        console.log(general.kRoot)
    })
}

export const getProtoType = async (type) => {
    let res
    await axios.get(constants.backHost + "KNode/protoType?type=" + type).then(e => res = e.data)
    console.log("protoType: ")
    console.log(res)
    return res
}

export const getQuizGeneratorPrototype = async (type)=>{
    let res
    await axios.get(constants.backHost + "kTree/config/quizGenerator/load?type="+type).then(e => {
        res = e.data
        console.log("quiz generator protytype:", res)
    })
    return res
}

export const getKNode = (id, kNode=general.kRoot)=>{
    if(id == kNode.data.id)
        return kNode
    else for(let i in kNode.subKNodes){
        let subKNode = getKNode(id, kNode.subKNodes[i])
        if(subKNode != undefined)
            return subKNode
    }
}

export const getKNodes = (ids)=>{
    let res = []
    ids.forEach(id => {if(getKNode(id) != undefined) res.push(getKNode(id))})
    console.log('getKNodes', res, ids)
    return res
}

export const getQuizCard = (id, kNode=general.kRoot)=>{
    for(let i in kNode.data.quizCards)
        if(kNode.data.quizCards[i].id == id)
            return kNode.data.quizCards[i]
    for(let i in kNode.subKNodes)
        if(getQuizCard(id, kNode.subKNodes[i]) != undefined)
            return getQuizCard(id, kNode.subKNodes[i])
}

export const getLearningCard = (id, kNode=general.kRoot)=>{
    for(let i in kNode.data.learningCards)
        if(kNode.data.learningCards[i].id == id)
            return kNode.data.learningCards[i]
    for(let i in kNode.subKNodes)
        if(getLearningCard(id, kNode.subKNodes[i]) != undefined)
            return getLearningCard(id, kNode.subKNodes[i])
    for(let i in general.kTreeConfigs.learningRepository)
        if(general.kTreeConfigs.learningRepository[i].id == id)
            return general.kTreeConfigs.learningRepository[i]
}
export const getLearningCards = (kNode=general.kRoot)=>{
    let res = []
    res = res.concat(kNode.data.learningCards)
    kNode.subKNodes.forEach(kNode => res = res.concat(getLearningCards(kNode)))
    if(kNode == general.kRoot)
        console.log('learningCards', res)
    return res
}

export const updateKNode = async (newState) => {
    console.log("synchronize: ")
    console.log(newState)
    await axios({
        method: "POST",
        url: constants.backHost + "KNode/synchronize",
        data: newState
    }).then(() => {
        console.log("update data success.")
    })
}

export const updateKNodeById = async (id) =>{
    console.log("update knode by id:", id, getKNode(id))
    await updateKNode(getKNode(id))
}


export const useKTree = async (name)=>{
    await axios({
        method: "POST",
        url: constants.backHost + "kTree/use",
        data: name,
        headers: {
            'Content-Type': 'text/plain'
        }
    })
    general.selectedKTree = name
    init()
    console.log("use kTree:" + name)
}

export const createKTree = async ()=>{
    await axios({
        method: "GET",
        url: constants.backHost + "kTree/create",
        headers: {
            'Content-Type': 'text/plain'
        }
    })
    await init()
}

export const removeKTree = async ()=>{
    await axios({
        method: "POST",
        url: constants.backHost + "kTree/delete",
        data: general.selectedKTree,
        headers: {
            'Content-Type': 'text/plain'
        }
    })
    init()
    console.log("remove kTree success.")
}

export const loadKTreeConfigs = async ()=>{
    await axios.get(constants.backHost + "kTree/config/load").then(e => {general.kTreeConfigs = e.data})
    console.log("ktree configs: ")
    console.log(general.kTreeConfigs)
}

export const synchronizeKTreeConfigs = async ()=>{
    await axios({
        method:"POST",
        url:constants.backHost + "kTree/config/synchronize",
        data:general.kTreeConfigs
    }).then((e)=>{console.log("ktree configs synchronized:", e.data)})
}

export const loadKTreeNames = async () => {
    await axios.get(constants.backHost + "kTree/check/all").then(e => {
        general.kTreeList = e.data
        console.log("load kTree names success.")
        console.log(general.kTreeList)
    })
}
export const loadCurrentKTreeName = async () => {
    await axios(constants.backHost + "kTree/check/current").then(e => {
        general.selectedKTree = e.data
        console.log("load current kTree name success: ")
        console.log(general.selectedKTree)
    })
}

export const updateSelectedKTreeName = async () => {
    await axios.get(constants.backHost + "kTree/alterName?newName=" + general.selectedKTree).then(e => {
        console.log("kTee name changed.")
    })
    await loadKTreeNames()
}

export const saveData = async () => {
    await axios.get(constants.backHost + "kTree/save").then(e => { console.log("data synchronized.") })
}

export const loadSettings = async()=>{
    await axios.get(constants.backHost + "settings/load").then(e=>{
        general.settings = e.data
        console.log("load settings: ",general.settings)})
}
export const synchronizeSettings = async()=>{
    console.log("settings:",general.settings)
    await axios({
        method:"POST",
        url:constants.backHost + "settings/synchronize",
        data:general.settings
    })
}
