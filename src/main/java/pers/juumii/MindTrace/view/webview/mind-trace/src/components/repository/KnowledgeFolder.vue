<script setup>
import {ref, watch} from "vue"
import data from '@/js/data'
import request from '@/js/request'
import operation from '@/js/operation'

const emits = defineEmits(['changePage'])

let options = ref()
const cascaderProps = {
    checkStrictly: true
}
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
    kNode.data.superKnowledgeId = data.selectedKNode.data.id
    data.selectedKNode.subKNodes.push(kNode)
    await operation.updateSelectedKNode()
    console.log("addKNode: ",data.kRoot)
    options.value = renderOptions([data.kRoot])
}

const removeKNode = async ()=>{
    let tempId = data.selectedKNode.data.id
    data.selectedIndexes.pop()
    operation.updateSelectedIndexes(data.selectedIndexes)
    for(let i = 0; i < data.selectedKNode.subKNodes.length; i ++){
        if(tempId == data.selectedKNode.subKNodes[i].data.id){
            data.selectedKNode.subKNodes.splice(i,1)
        }
    }
    operation.updateKNode(data.selectedKNode)
    options.value = renderOptions([data.kRoot])
}

const saveData = async ()=>{
    request.saveData()
}


</script>

<template>
    <div class="header space-between">
        <div class="left">
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
        <div class="right">
            <el-button
            @click="()=>saveData()">
                <el-icon><FolderChecked /></el-icon>
            </el-button>
            <el-button
            @click="emits('changePage')">
            <el-icon><HomeFilled /></el-icon>
          </el-button>
        </div>
    </div>
    <el-scrollbar id="kpath-scroll">
        <el-cascader-panel 
        id="kpath" 
        v-model="data.selectedIndexes"
        :options="options"
        :props="cascaderProps"
        @change="selected=>{operation.updateSelectedIndexes(selected)}"/>
    </el-scrollbar>
</template>

<style lang="less" scoped>
.header{
    margin-top: 2vh;
}
.left{
    display: flex;
}
.right{
    margin-right: 2vw;
}
.add-sub-knowledge-button{
    margin-bottom: 2vw;
}
#kpath-header{
    height: 45vh;
}
#kpath-scroll{
    width: 90vw;
    vertical-align: middle;
}
#kpath{
    width:500vw;
    height: 30vh;
}
</style>