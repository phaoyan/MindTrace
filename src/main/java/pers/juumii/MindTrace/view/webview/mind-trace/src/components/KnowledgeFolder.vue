<script setup>
import {ref, inject, watch, onMounted} from "vue"

const data = inject('data')
const operation = inject('operation')
const getProtoType = inject('getProtoType')

const options = ref([])
const cascaderProps = {
    checkStrictly: true
}

watch(data.folderUpdate, ()=>{
    options.value = renderOptions([data.kRoot.value])
})

const renderOptions = (kroots)=>{
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
    await getProtoType('kNode').then(e=>kNode = e)
    kNode.data.superKnowledgeId = data.selectedKNode.value.data.id
    data.selectedKNode.value.subKNodes.push(kNode)
    operation.updateKNode(data.selectedKNode.value)
    options.value = renderOptions([data.kRoot.value])
}

const removeKNode = async ()=>{
    let tempId = data.selectedKNode.value.data.id
    data.selectedIndexes.value.pop()
    operation.updateSelectedIndexes(data.selectedIndexes.value)
    for(let i = 0; i < data.selectedKNode.value.subKNodes.length; i ++){
        if(tempId == data.selectedKNode.value.subKNodes[i].data.id){
            data.selectedKNode.value.subKNodes.splice(i,1)
        }
    }
    operation.updateKNode(data.selectedKNode.value)
    options.value = renderOptions([data.kRoot.value])
}


</script>

<template>
    <div class="header">
        <div id="knowledge-title">Knowledges</div>
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
    <el-scrollbar id="kpath-scroll">
        <el-cascader-panel 
        id="kpath" 
        :options="options"
        :props="cascaderProps"
        @change="selected=>{operation.updateSelectedIndexes(selected)}"/>
    </el-scrollbar>
    
</template>

<style lang="less" scoped>
.header{
    display: flex;
    margin-top: 2vh;
}
.add-sub-knowledge-button{
    margin-bottom: 2vw;
}
#knowledge-title{
    margin: 0 3vw;
    font-size: 130%;
    font-weight: 300;
    font-style: italic;
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