<script setup>
import SubpageHeader from '../atomic/SubpageHeader.vue'
import {onMounted, ref} from 'vue'
import {Calendar, Histogram} from '@element-plus/icons-vue'
import { MindTrace, init, getLearningTimeSpentOfDay, getLearningOverviewOfDay } from '@/js/mirror/home/MindTrace'
import { HomePage } from '@/js/mirror/home/HomePage'
import moment from 'moment'

onMounted(async ()=>{
    await init()
})


const calendar = ref()
const selectDate = (val) => {
  calendar.value.selectDate(val)
}
</script>

<template>
    <el-container direction="vertical">
        <SubpageHeader
        @pageLeft="()=>HomePage.pageSelected = 'LearningTask'"
        @pageRight="()=>HomePage.pageSelected = 'QuizTrace'">
            <div class="header-info">Statistics</div>
        </SubpageHeader>
        <div class="options-row space-evenly" style="margin: auto 42vw">
            <el-button @click="()=>MindTrace.pageSelected = 'GeneralInfo'">
                <el-icon><Document /></el-icon>
            </el-button>
            <!-- <el-button @click="()=>MindTrace.pageSelected = 'ChartsInfo'">
                <el-icon><Histogram /></el-icon>
            </el-button> -->
            <el-button @click="()=>MindTrace.pageSelected = 'CalendarInfo'">
                <el-icon><Calendar /></el-icon>
            </el-button>
        </div>
        <el-scrollbar class="scroll" style="height:70vh; overflow: hidden;">
            <div class="trace-info-wrapper">
                <div class="select-pages">
                    <div class="general-info" v-if="MindTrace.pageSelected == 'GeneralInfo'">
                        <v-md-preview :text="MindTrace.overviewMarkdown"/>
                    </div>
                    <div class="charts-info" v-if="MindTrace.pageSelected == 'ChartsInfo'">
                        Charts Info
                    </div>
                    <div class="calendar-info card-margin" v-if="MindTrace.pageSelected == 'CalendarInfo'">
                        <el-calendar ref="calendar">
                            <template #header>
                                <el-icon class="icon-button" @click="selectDate('prev-month')"><Back /></el-icon>
                                <span class="el-text-color" style="font-size: 130%; font-style: oblique; font-weight: 200;">{{moment().toISOString().split('T')[0]}}</span>
                                <el-icon class="icon-button" @click="selectDate('next-month')"><Right /></el-icon>
                            </template>
                            <template #date-cell="{data}">
                                <el-popover trigger="click" placement="right-start" :width="getLearningOverviewOfDay(data.date) == undefined ? 200: 400">
                                    <template #reference>
                                        <div class="wrapper" style="width:100%; height:100%">
                                            <div class="header el-text-color">{{data.date.getDate()}}</div>
                                            <div class="info el-blue" style="text-align: center; transform: translateY(80%);">
                                                {{getLearningTimeSpentOfDay(data.date)}}
                                            </div>
                                        </div>
                                    </template>
                                    <v-md-preview :text="getLearningOverviewOfDay(data.date)" v-if="getLearningOverviewOfDay(data.date) != undefined"/>
                                    <div class="no-data el-text-color large-font" v-if="getLearningOverviewOfDay(data.date) == undefined">No Data</div>
                                </el-popover>
                            </template>
                        </el-calendar>
                    </div>
                </div>
            </div>
        </el-scrollbar>
    </el-container>
</template>