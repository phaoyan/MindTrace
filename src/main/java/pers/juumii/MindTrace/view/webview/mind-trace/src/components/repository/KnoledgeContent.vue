<script setup>
import {ref, inject} from 'vue'
import LearningCardPanel from './LearningCardPanel.vue'
import QuizCardPanel from './QuizCardPanel.vue'

const data = inject('data')
const operation = inject('operation')

const pageSelected = ref("quizCards")

const pageOptions = [
    {
        label:"info",
        value:"info"
    },
    {
        label:"quizCards",
        value:"quizCards"
    },
    {
        label:"learningCards",
        value:"learningCards"
    }
]

</script>

<template>
    <el-container v-if="data.selectedKNode.value.data != null">
        <el-header class="header space-between">
            <input 
            class="knowledge-title" 
            v-model="data.selectedKNode.value.data.description"
            @change="()=>{
                operation.updateKNode(data.selectedKNode.value)
                data.folderUpdate.value = !data.folderUpdate.value
            }"/>
            <el-button
            class="icon-button vertical-center"
            @click="()=>operation.openLink()">
            <el-icon><Link /></el-icon>
            </el-button>
            <div class="selection-wrapper vertical-center">
                <el-cascader 
                class="select" 
                :options="pageOptions" 
                v-model="pageSelected"/>
            </div>
        </el-header>
        <el-main class="main">
            <el-scrollbar class="main-scroll">
                <el-container v-if="pageSelected == 'info'">
                    <el-button/>
                </el-container>
                <LearningCardPanel v-if="pageSelected == 'learningCards'"/>
                <QuizCardPanel v-if="pageSelected == 'quizCards'"/>
            </el-scrollbar>
        </el-main>
    </el-container>
</template>


<style lang="less" scoped>
.clear-input{
    background: none;
    outline: none;
    border: none;
    width: 80%;
}
.middle-font{
    font-size: 120%;
    font-weight: lighter;
    font-style: italic; 
}
.large-font{
    font-size: 150%;
    font-weight: lighter;
    font-style: italic; 
}
.knowledge-title{
    width: 50vw !important;
    .clear-input();
    .large-font();
    margin: 2vw;
}
.selection-wrapper{
    margin: auto 2vw;
}
.main-scroll{
    height: 45vh !important;
}
.add-task-card{
    margin: 0 auto;
}
.add-task-card:hover{
    background-color: beige;
}


</style>