<script setup>
import SubpageHeader from "../atomic/SubpageHeader.vue"
import QuizCard from "../atomic/QuizCard.vue"
import { HomePage } from "@/js/mirror/home/HomePage"
import {QuizTrace,getQuizCardsThisPage, init} from "@/js/mirror/home/QuizTrace"
import { linkToKnowledge } from "@/js/mirror/repository/RepositoryPage"
import { Calendar } from "@element-plus/icons-vue"
import { onMounted } from "vue"

onMounted(async()=>{await init()})

</script>

<template>
    <div class="quiz-card-trace">
        <SubpageHeader
        @pageLeft="()=>HomePage.pageSelected = 'MindTrace'"
        @pageRight="()=>HomePage.pageSelected = 'QuizTask'">
            <div class="header-info">Quiz Trace</div>
        </SubpageHeader>
        <div class="options-row space-evenly" style="margin: auto 42vw">
            <el-button>
                <el-icon><Calendar /></el-icon>
            </el-button>
        </div>
        <el-scrollbar class="scroll" style="height:70vh; overflow: hidden;">
            <el-timeline>
                <el-timeline-item
                v-for="quizCard in getQuizCardsThisPage()" :key="quizCard.id"
                :timestamp="quizCard.establishTime.replace('T',' ')"
                placement="top" class="card-margin">
                    <QuizCard :id="quizCard.id">
                        <template v-slot:general-plugin>
                                <el-icon 
                                class="vertical-center icon-button" 
                                style="color:#79bbff;margin-left:35vw" 
                                @click="() => linkToKnowledge(quizCard.knowledgeId)"
                                size="130%"><Promotion /></el-icon>
                        </template>
                    </QuizCard>
                </el-timeline-item>
            </el-timeline>
            <el-pagination
            class="card-margin"
            v-model:currentPage="QuizTrace.currentPage"
            :total="QuizTrace.quizCardTrace.length"/>
        </el-scrollbar>

    </div>
</template>