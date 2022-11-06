<script setup>
import {ref, inject, watch} from 'vue'
import LearningCardPanel from './LearningCardPanel.vue'
import QuizCardPanel from './QuizCardPanel.vue'

const data = inject('data')
const operation = inject('operation')

const pageSelected = ref("learningCards")


</script>

<template>
    <el-container v-if="data.selectedKNode.value.data != null">
        <el-header class="header">
            <input 
            class="knowledge-title" 
            v-model="data.selectedKNode.value.data.description"
            @change="()=>{
                operation.updateKNode(data.selectedKNode.value)
                data.folderUpdate.value = !data.folderUpdate.value
            }"/>
            <el-dropdown 
            class="select"
            @command="newPage=>pageSelected = newPage">
                <el-button>
                    <el-icon>
                        <arrow-down />
                    </el-icon>
                </el-button>
                <template #dropdown>
                    <el-dropdown-menu>
                        <el-dropdown-item command="info">Info</el-dropdown-item>
                        <el-dropdown-item command="learningCards">Learning Tasks</el-dropdown-item>
                        <el-dropdown-item command="quizCards">Quiz Tasks</el-dropdown-item>
                    </el-dropdown-menu>
                </template>
            </el-dropdown>
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


.header{
    display:flex;
    justify-content: space-between;
}
.knowledge-title{
    width: 50vw !important;
    .clear-input();
    .large-font();
    margin: 2vw;
}
.select{
    margin: auto 0;
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