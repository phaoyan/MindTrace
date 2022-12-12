import { reactive } from "vue"
import { getKNode, loadKRoot, updateKNode } from "../general"
import {general} from "../general"
import {AppVue} from "../AppVue"

export const RepositoryPage = reactive({
    selectedIndexes: []
})

export const updateSelectedIndexes = async (selectIndexes) => {
    RepositoryPage.selectedIndexes = selectIndexes
    console.log("update selected:")
    console.log(RepositoryPage.selectedIndexes)
    console.log(getSelectedKNode())
}

export const updateSelectedIndexesByKnowledgeId = async(id)=>{
    let res = []
    let curKNode = getKNode(id)
    while(curKNode.data.id != 0){
        res.push(curKNode.data.id)
        curKNode = getKNode(curKNode.data.superKnowledgeId)
    }
    res.push(0)
    updateSelectedIndexes(res.reverse())
}

export const linkToKnowledge = async (id)=>{
    updateSelectedIndexesByKnowledgeId(id)
    loadKRoot()
    AppVue.pageSelected = 'repository'
}

export const getSelectedKNode = ()=>{
    let res = general.kRoot
    RepositoryPage.selectedIndexes.forEach(index =>{
        res.subKNodes.forEach(kNode =>{
            if(kNode.data.id == index)
                res = kNode
        })
    })
    return res
}

export const updateSelectedKNode = async ()=>{
    await updateKNode(getSelectedKNode())
    await loadKRoot()
}