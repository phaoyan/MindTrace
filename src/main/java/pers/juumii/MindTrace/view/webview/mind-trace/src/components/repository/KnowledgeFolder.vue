<script setup>
import {ref, watch} from "vue"
import data from '@/js/data'
import request from '@/js/request'
import operation from '@/js/operation'

const emits = defineEmits(['changePage'])

let options = ref()
watch(data, ()=>{
    options.value = renderOptions([data.kRoot])
})

const renderOptions = (kroots)=>{
    // console.log("render options ... ")
    let options = []
    for(let index in kroots){
        let knode = kroots[index]
        if(knode.data != null){
            options.push({
                value: knode.data.id,
                label: knode.data.description,
                children: renderOptions(knode.subKNodes)
            })
        }
    }
    return options
}

const addKNode = async ()=>{
    let kNode
    await request.getProtoType('kNode').then(e=>kNode = e)
    kNode.data.superKnowledgeId = operation.getSelectedKNode().data.id
    operation.getSelectedKNode().subKNodes.push(kNode)
    await operation.updateSelectedKNode()
    console.log("addKNode: ",data.kRoot)
    options.value = renderOptions([data.kRoot])
}

const removeKNode = async ()=>{
    let tempId = operation.getSelectedKNode().data.id
    data.Repository.selectedIndexes.pop()
    operation.updateSelectedIndexes(data.Repository.selectedIndexes)
    for(let i = 0; i < operation.getSelectedKNode().subKNodes.length; i ++){
        if(tempId == operation.getSelectedKNode().subKNodes[i].data.id){
            operation.getSelectedKNode().subKNodes.splice(i,1)
        }
    }
    operation.updateKNode(operation.getSelectedKNode())
    options.value = renderOptions([data.kRoot])
}

</script>

<template>
    <div class="space-between" style="margin-top:2vh">
        <div class="left" style="display:flex">
            <input 
            class="clear-input title length-50" 
            v-model="data.selectedKTree"
            @change="()=>request.updateSelectedKTreeName()"/>
            <el-button 
            class="add-sub-knowledge-button"
            @click="()=>addKNode()">
                <el-icon><ArrowRightBold /></el-icon>
            </el-button>
            <el-button 
            class="remove-knowledge-button"
            @click="()=>removeKNode()">
                <el-icon><Delete /></el-icon>
            </el-button>
        </div>
        <div class="right" style="margin-right:2vw">
            <el-button
            @click="()=>request.saveData()">
                <el-icon><FolderChecked /></el-icon>
            </el-button>
            <el-button
            @click="emits('changePage')">
            <el-icon><HomeFilled /></el-icon>
          </el-button>
        </div>
    </div>
    <el-scrollbar class="kpath-scroll" style="width: 100vw;">
        <el-cascader-panel 
        class="kpath" 
        style="width:500vw; height: 20vh;"
        v-model="data.Repository.selectedIndexes"
        :options="options"
        :props="{checkStrictly: true}"
        @change="selected=>{operation.updateSelectedIndexes(selected)}"/>
    </el-scrollbar>
</template>