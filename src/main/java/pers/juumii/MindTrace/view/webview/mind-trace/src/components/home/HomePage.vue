<script setup>
import {ref} from 'vue'
import QuizTask from './QuizTask.vue'
import LearningTask from './LearningTask.vue'
import MindTrace from './MindTrace.vue'
import data from '@/js/data'
import request from '@/js/request'

const emits = defineEmits(['changePage'])

// 用于让el-select在选中某项后显示
const value = ref(data.selectedKTree)


</script>

<template>
    <el-container direction="vertical">
        <div class="header" style="margin: 0 auto; height:10vh; line-height:10vh">
            <div class="row-wrapper">
                <el-select
                class="kTree-select"
                style="margin-right:1vw"
                v-model="value"
                default-first-option
                @change="(option)=>{
                    request.useKTree(option); 
                    data.Home.pageSelected = ''; data.Home.pageSelected = 'QuizTask'}">
                    <el-option
                    v-for="option in data.kTreeList" :key="option"
                    :label="option"
                    :value="option"/>
                </el-select>
                <el-button
                @click="emits('changePage')">
                    <el-icon><Menu /></el-icon>
                </el-button>
                <el-button
                @click="()=>{request.createKTree()}">
                    <el-icon><Plus /></el-icon>
                </el-button>
                <el-button
                @click="()=>{request.removeKTree(); value = ''}">
                    <el-icon><Delete/></el-icon>
                </el-button>
            </div>
        </div>
        <el-divider/>
        <div class="wrapper">
            <QuizTask v-if="data.Home.pageSelected=='QuizTask'"/>
            <LearningTask v-if="data.Home.pageSelected=='LearningTask'"/>
            <MindTrace v-if="data.Home.pageSelected=='MindTrace'"/>
        </div>
    </el-container>
</template>