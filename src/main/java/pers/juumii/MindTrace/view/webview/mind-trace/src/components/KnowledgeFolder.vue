<script setup>
import {onMounted, ref, inject, watch} from "vue"

const data = inject('data')
const update = inject('update')

const options = ref([])
const cascaderProps = {
    checkStrictly: true
}

watch(data.kTree, ()=>{
    options.value = _renderKTree([data.kTree.value])
})

// kroots: array of knodes;
// return: array of options
const _renderKTree = (kroots)=>{
    let options = []

    for(let index in kroots){
        let knode = kroots[index]
        // console.log("renderKTree--for:")
        // console.log(knode)
        if(knode.data != null){
            options.push({
                value: knode.data.id,
                label: knode.data.description,
                children: _renderKTree(knode.subKNodes)
            })
        }
    }
    // console.log("_renderKTree:")
    // console.log(options)
    return options
}

const renderKTree = (kroot)=>{
    return _renderKTree(kroot.subKNodes)
}

</script>

<template>
    <div class="header">
        <div id="knowledge-title">Knowledges</div>
        <el-button 
        class="add-sub-knowledge-button"
        @click="update.addSubKnowledge()">
            <el-icon><ArrowRightBold /></el-icon>
        </el-button>
        <el-button 
        class="remove-knowledge-button"
        @click="update.removeKnowledge()">
            <el-icon><Delete /></el-icon>
        </el-button>
    </div>
    <el-scrollbar id="kpath-scroll">
        <el-cascader-panel 
        id="kpath" 
        :options="options"
        :props="cascaderProps"
        @change="selected=>{update.updateSelectedIndexes(selected)}"/>
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