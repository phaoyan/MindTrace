<script setup>
import {ref, onMounted, inject, watch} from 'vue'

const data = inject('data')
const update = inject('update')

const pageSelected = ref("learningCards")

</script>

<template>
    <el-container>
        <el-header class="header">
            <input 
            class="knowledge-title" 
            v-model="data.selectedKnowledge.value.description"
            @change="()=>update.updateSelectedKnowledge()"/>
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

                <!-- Learning Cards -->
                <el-container v-if="pageSelected == 'learningCards'" direction="vertical">
                    <el-card 
                    class="learning-task-card"
                    v-for="card in data.selectedKnowledge.value.learningCards" :key="card">
                        <template #header>
                            <input 
                            class="clear-input" 
                            v-model="card.description"
                            @change="()=>update.updateSelectedLearningTask(card)"/>
                            <el-button 
                            @click="update.U_removeLearningTask(card)">
                            <el-icon><Delete /></el-icon>
                            </el-button>
                        </template>

                        <input 
                        class="clear-input" 
                        v-model="card.resource"
                        @change="()=>update.updateSelectedLearningTask(card)"/>
                    </el-card>
                    <el-card class="add-task-card">
                        <el-icon 
                        class="add-task-icon" 
                        size="150%"
                        @click="update.U_addLearningTask()">
                        <Plus />
                        </el-icon>
                    </el-card>
                </el-container>

                <!-- Quiz Cards -->
                <el-container v-if="pageSelected == 'quizCards'">
                    <el-card v-for="card in data.selectedKnowledge.value.quizCards" :key="card">
                        <el-switch v-model="value1" />
                        {{value1}}
                    </el-card>
                </el-container>
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
.icon-button{
    cursor: pointer;
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
.add-task-icon{
    .icon-button();
}
.learning-task-card{
    margin-bottom: 2vh;
}
</style>