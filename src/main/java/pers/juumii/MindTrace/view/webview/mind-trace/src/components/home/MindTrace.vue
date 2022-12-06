<script setup>
import SubpageHeader from '../atomic/SubpageHeader.vue'
import QuizCard from '../atomic/QuizCard.vue' 
import data from '@/js/data'
import request from '@/js/request'
import operation from '@/js/operation'
import { onMounted, ref } from 'vue'
import { Calendar, Histogram } from '@element-plus/icons-vue'

// quiz trace
const kNodeCount = ref([])
const quizCardCount = ref([])
const quizCardTrace = ref([])
const quizCardsThisPage = ref()
const currentPage = ref(1)

// general info
const overviewMarkdown = ref("# ")

onMounted(async ()=>{
    request.getStatistics('kNodeCount').then(e=>kNodeCount.value=e)
    request.getStatistics('quizCardCount').then(e=>quizCardCount.value=e)
    await request.getStatistics('quizCardsSortedByEstablishment').then(e=>{quizCardTrace.value=e})
    quizCardsThisPage.value = quizCardTrace.value.slice((currentPage.value - 1) * 10, currentPage.value * 10)
    request.getStatistics('overviewMarkdown').then(e=>{overviewMarkdown.value = e; console.log(overviewMarkdown.value)})
})

const pageSelected = ref('GeneralInfo')

</script>

<template>
    <el-container direction="vertical">
        <SubpageHeader
        @pageLeft="()=>data.Home.pageSelected = 'QuizTask'"
        @pageRight="()=>data.Home.pageSelected = 'LearningTask'">
            <div class="header-info" v-if="pageSelected == 'QuizCardTrace'">Quiz Trace</div>
            <div class="header-info" v-if="pageSelected == 'GeneralInfo'">Statistics</div>
        </SubpageHeader>
        <div class="options-row space-evenly" style="margin: auto 42vw">
            <el-button @click="()=>pageSelected = 'GeneralInfo'">
                <el-icon><Histogram /></el-icon>
            </el-button>
            <el-button @click="()=>{request.loadQuiz(); pageSelected = 'QuizCardTrace'}">
                <el-icon><Calendar /></el-icon>
            </el-button>
        </div>
        <el-scrollbar class="scroll" style="height:70vh; overflow: hidden;">
            <div class="trace-info-wrapper">
                <div class="select-pages">
                    <div class="quiz-card-trace" v-if="pageSelected == 'QuizCardTrace'">
                        <el-timeline>
                            <el-timeline-item
                            v-for="quizCard in quizCardsThisPage" :key="quizCard.id"
                            :timestamp="quizCard.establishTime.replace('T',' ')"
                            placement="top" class="card-margin">
                                <QuizCard :id="quizCard.id">
                                    <template v-slot:general-plugin>
                                            <el-icon 
                                            class="vertical-center icon-button" 
                                            style="color:#79bbff;margin-left:35vw" 
                                            @click="() => operation.linkToKnowledge(quizCard.knowledgeId)"
                                            size="130%"><Promotion /></el-icon>
                                    </template>
                                </QuizCard>
                            </el-timeline-item>
                        </el-timeline>
                        <el-pagination
                        class="card-margin"
                        v-model:currentPage="currentPage"
                        :total="quizCardTrace.length"
                        @current-change="quizCardsThisPage = quizCardTrace.slice((currentPage - 1) * 10, currentPage * 10)"/>
                    </div>
                    <div class="general-info" v-if="pageSelected == 'GeneralInfo'">
                        <v-md-preview :text="overviewMarkdown"/>
                    </div>
                </div>
            </div>

        </el-scrollbar>
    </el-container>
</template>