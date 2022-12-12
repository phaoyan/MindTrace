import axios from 'axios'
import data from './data'
import constants from './constants'
import statistics from './statistics'
import {init} from './init'


const exit = async ()=>{
    console.log("test")
    await axios.get(constants.backHost + "exit")
}

//基本数据相关
const loadKNode = async (id) => {
    let kNode
    await axios.get(constants.backHost + "KNode/load?id=" + id).then((e) => {
        kNode = e.data
        console.log("getKNode success: ")
        console.log(kNode)
    })
    return kNode
}
const synchronizeKNode = async (newState) => {
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
const getProtoType = async (type) => {
    let res
    await axios.get(constants.backHost + "KNode/protoType?type=" + type).then(e => res = e.data)
    console.log("protoType: ")
    console.log(res)
    return res
}

//settings相关
const loadSettings = async()=>{
    await axios.get(constants.backHost + "settings/load").then(e=>{
        data.settings = e.data
        console.log("load settings: ",data.settings)})
}
const synchronizeSettings = async()=>{
    console.log("settings:",data.settings)
    await axios({
        method:"POST",
        url:constants.backHost + "settings/synchronize",
        data:data.settings
    })
}


//杂项功能
const openLink = async () => {
    await axios({
        method: "POST",
        url: constants.backHost + "utils/openLink",
        data: data.selectedText,
        headers: {
            'Content-Type': 'text/plain'
        }
    }).then(() => {
        console.log("open link success.")
    })
}
//markdown上传图片
const uploadImage = async (event, insertImage, files) => {
    for(let i in files){
        let file = files[i]
        let formData = new FormData()
        formData.append('file', file)
        await axios({
            method:"POST",
            url:constants.backHost + "file/image/upload",
            data:formData
        }).then(e=>{
            insertImage({
                url:e.data.url,
                desc:e.data.desc
            })
        })
    }
}

//KTree相关
const loadKTreeConfigs = async ()=>{
    await axios.get(constants.backHost + "utils/data/config/load").then(e => {data.kTreeConfigs = e.data})
    console.log("ktree configs: ")
    console.log(data.kTreeConfigs)
}
const synchronizeKTreeConfigs = async()=>{
    await axios({
        method:"POST",
        url:constants.backHost + "utils/data/config/synchronize",
        data:data.kTreeConfigs
    }).then((e)=>{console.log("ktree configs synchronized:", e.data)})
}
const getQuizGeneratorPrototype = async (type)=>{
    let res
    await axios.get(constants.backHost + "utils/data/config/quizGenerator/load?type="+type).then(e => {
        res = e.data
        console.log("quiz generator protytype:", res)
    })
    return res
}
const loadKTreeNames = async () => {
    await axios.get(constants.backHost + "utils/data/check/all").then(e => {
        data.kTreeList = e.data
        console.log("load kTree names success.")
        console.log(data.kTreeList)
    })
}
const loadCurrentKTreeName = async () => {
    await axios(constants.backHost + "utils/data/check/current").then(e => {
        data.selectedKTree = e.data
        console.log("load current kTree name success: ")
        console.log(data.selectedKTree)
    })
}
const useKTree = async (name) => {
    await axios({
        method: "POST",
        url: constants.backHost + "utils/data/use",
        data: name,
        headers: {
            'Content-Type': 'text/plain'
        }
    })
    data.selectedKTree = name
    init()
    console.log("use kTree:" + name)
}
const createKTree = async () => {
    await axios({
        method: "GET",
        url: constants.backHost + "utils/data/create",
        headers: {
            'Content-Type': 'text/plain'
        }
    })
    await init()
}
const removeKTree = async () => {
    await axios({
        method: "POST",
        url: constants.backHost + "utils/data/delete",
        data: data.selectedKTree,
        headers: {
            'Content-Type': 'text/plain'
        }
    })
    init()
    console.log("remove kTree success.")
}
const saveData = async () => {
    await axios.get(constants.backHost + "utils/data/save").then(e => { console.log("data synchronized.") })
}
const updateSelectedKTreeName = async () => {
    await axios.get(constants.backHost + "utils/data/alterName?newName=" + data.selectedKTree).then(e => {
        console.log("kTee name changed.")
    })
    await loadKTreeNames()
}

//QuizTask相关
const loadQuizIsDue = async()=>{
    await axios.get(constants.backHost + "utils/quiz/isDue").then(e => {
        data.Home.QuizTask.quizIsDue = e.data
    })
    console.log('quiz is due ? ', data.Home.QuizTask.quizIsDue)
}
const loadQuiz = async ()=>{
    await loadQuizIsDue()
    data.Home.QuizTask.quizCards.splice(0, data.Home.QuizTask.quizCards.length)
    await axios.get(constants.backHost + "utils/quiz/generate").then(e=>{
        for(let i in e.data)
            data.Home.QuizTask.quizCards.push(e.data[i])})
    console.log('quiz generated: ', data.Home.QuizTask.quizCards)
}
const loadQuizFinished = async()=>{
    await loadQuizIsDue()
    data.Home.QuizTask.finished.splice(0, data.Home.QuizTask.finished.length)
    await axios.get(constants.backHost + "utils/quiz/finished").then(e=>{
        for(let i in e.data)
            data.Home.QuizTask.finished.push(e.data[i])})
    console.log('quiz finished: ', data.Home.QuizTask.finished)
}

export default {
    exit,
    loadKNode,
    synchronizeKNode,
    getProtoType,
    loadSettings,
    synchronizeSettings,
    openLink,
    uploadImage,
    loadKTreeConfigs,
    synchronizeKTreeConfigs,
    getQuizGeneratorPrototype,
    useKTree,
    saveData,
    updateSelectedKTreeName,
    removeKTree,
    createKTree,
    loadCurrentKTreeName,
    loadKTreeNames,
    loadQuizIsDue,
    loadQuiz,
    loadQuizFinished,
    ...statistics
}