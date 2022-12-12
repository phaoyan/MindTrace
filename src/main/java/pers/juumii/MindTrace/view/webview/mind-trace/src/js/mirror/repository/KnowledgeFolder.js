import { reactive } from "vue"
import {general} from "../general"
import { getSelectedKNode, updateSelectedIndexes, updateSelectedKNode } from "./RepositoryPage"
import { updateKNode, getProtoType } from "../general"
import {RepositoryPage} from "./RepositoryPage"

export const KnowledgeFolder = reactive({
    options:[]
})

export const renderKTreeOptions = (kroots=[general.kRoot])=>{
    // console.log("render options ... ")
    let options = []
    for(let index in kroots){
        let knode = kroots[index]
        if(knode != undefined && knode.data != null ){
            options.push({
                value: knode.data.id,
                label: knode.data.description,
                children: renderKTreeOptions(knode.subKNodes)
            })
        }
    }
    return options
}

export const addKNode = async ()=>{
    let kNode
    await getProtoType('kNode').then(e=>kNode = e)
    kNode.data.superKnowledgeId = getSelectedKNode().data.id
    getSelectedKNode().subKNodes.push(kNode)
    await updateSelectedKNode()
    console.log("addKNode: ",general.kRoot)
    KnowledgeFolder.options = renderKTreeOptions([general.kRoot])
}

export const removeKNode = async ()=>{
    let tempId = getSelectedKNode().data.id
    RepositoryPage.selectedIndexes.pop()
    updateSelectedIndexes(RepositoryPage.selectedIndexes)
    for(let i = 0; i < getSelectedKNode().subKNodes.length; i ++){
        if(tempId == getSelectedKNode().subKNodes[i].data.id){
            getSelectedKNode().subKNodes.splice(i,1)
        }
    }
    updateKNode(getSelectedKNode())
    KnowledgeFolder.options = renderKTreeOptions([general.kRoot])
}
export const init = async ()=>{
    KnowledgeFolder.options = renderKTreeOptions()
}
