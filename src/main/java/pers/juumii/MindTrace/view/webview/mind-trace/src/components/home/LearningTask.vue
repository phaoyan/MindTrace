<script setup>
import SwitchIconButton from '../atomic/SwitchIconButton.vue'
import SubpageHeader from '../atomic/SubpageHeader.vue'
import EditableContent from '../atomic/EditableContent.vue'
import constants from '@/js/constants'
import {ref, onMounted} from 'vue'
import { LearningTask, addRecord, init } from '@/js/mirror/home/LearningTask'
import { HomePage } from '@/js/mirror/home/HomePage'
import { general, getKNodes, synchronizeKTreeConfigs } from '@/js/mirror/general'
import { linkToKnowledge } from '@/js/mirror/repository/RepositoryPage'

setInterval(()=>{LearningTask.nowTime = constants.nowTime()}, 1000)
onMounted(()=>{init()})

</script>

<template>
    <el-container direction="vertical">
        <SubpageHeader
        @pageLeft="()=>HomePage.pageSelected = 'QuizTask'"
        @pageRight="()=>HomePage.pageSelected = 'MindTrace'">
            <div class="header-info">Learning Records</div>
        </SubpageHeader>
        <el-scrollbar style="height:75vh">
            <el-card class="add-record card-margin">
                <template #header>
                    <div class="header-wrapper space-between">
                        <div class="vertical-center">
                            <span class="el-text-color margin-right">{{LearningTask.nowTime}}</span>
                        </div>
                        <div class="space-between">
                            <div class="vertical-center margin-right">
                                <span class="el-text-color margin-right" v-if="(LearningTask.startTime != undefined)">
                                    start:{{LearningTask.startTime}}
                                </span>
                            </div>
                            <SwitchIconButton
                            :getState="()=>LearningTask.startTime == undefined"
                            @frontAction="()=>LearningTask.startTime = constants.nowTime()"
                            @backAction="()=>addRecord()">
                                <template #front>
                                    <EditPen/>
                                </template>
                                <template #back>
                                    <Finished/>
                                </template>
                            </SwitchIconButton>
                        </div>

                    </div>
                </template>
                <div class="wrapper" v-if="(LearningTask.kTreeOptions != undefined && LearningTask.startTime != undefined)">
                    <EditableContent
                    :text="LearningTask.description"
                    :edit="true" @change="(text)=>{LearningTask.description = text}"/>
                    <el-divider/>
                    <el-tree 
                    :data="LearningTask.kTreeOptions" 
                    :props="{children:'children', label:'label'}"
                    show-checkbox check-strictly default-expand-all
                    @check-change="(selected)=>{
                        if(LearningTask.selectedKNodes.indexOf(selected) == -1)
                            LearningTask.selectedKNodes.push(selected);
                        else LearningTask.selectedKNodes.splice(LearningTask.selectedKNodes.indexOf(selected), 1)}"/>
                </div>
            </el-card>
            <el-timeline>
                <el-timeline-item 
                class="card-margin"
                placement="top"
                v-for="record in general.kTreeConfigs.learningRecords" :key="record.id"
                :timestamp="constants.timestamp(record.startTime, record.finishTime)">
                    <el-card>
                        <div class="space-between">
                            <div class="description">{{record.description}}</div>
                            <div class="options space-between">
                                <el-popover
                                placement="top-start"
                                trigger="hover">
                                    <template #reference>
                                        <el-icon class="icon-button" size="120%" style="color:#79bbff"><Promotion /></el-icon>
                                    </template>
                                    <div class="buttons-wrapper"
                                    v-for="concerned in getKNodes(record.concernedKnowledgeIds)" :key="concerned">
                                        <el-button
                                        text
                                        @click="linkToKnowledge(concerned.data.id)">
                                            {{concerned.data.description}}
                                        </el-button>
                                    </div>
                                </el-popover>
                                <el-icon class="icon-button" size="120%" style="margin-left:5vw"
                                @click="()=>{
                                    general.kTreeConfigs.learningRecords.splice(general.kTreeConfigs.learningRecords.indexOf(record),1)
                                    synchronizeKTreeConfigs()
                                }"><Delete /></el-icon>
                            </div>
                        </div>
                    </el-card>
                </el-timeline-item>
            </el-timeline>
        </el-scrollbar>
    </el-container>
</template>