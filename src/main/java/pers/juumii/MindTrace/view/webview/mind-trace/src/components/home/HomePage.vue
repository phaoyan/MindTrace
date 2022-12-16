<script setup>
import {ref, onMounted} from 'vue'
import QuizTask from './QuizTask.vue'
import LearningTask from './LearningTask.vue'
import MindTrace from './MindTrace.vue'
import QuizTrace from './QuizTrace.vue'
import { HomePage, init } from '@/js/mirror/home/HomePage'
import { general, useKTree, createKTree, removeKTree } from '@/js/mirror/general'
import { updateSelectedIndexes } from '@/js/mirror/repository/RepositoryPage'
import { AppVue } from '@/js/mirror/AppVue'


const emits = defineEmits(['changePage'])

// 用于让el-select在选中某项后显示
const value = ref(general.selectedKTree)

onMounted(()=>init())
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
                    useKTree(option); }">
                    <el-option
                    v-for="option in general.kTreeList" :key="option"
                    :label="option"
                    :value="option"/>
                </el-select>
                <el-button
                @click="()=>{
                    AppVue.pageSelected = 'repository'
                    updateSelectedIndexes([0])}">
                    <el-icon><Menu /></el-icon>
                </el-button>
                <el-button
                @click="()=>{createKTree()}">
                    <el-icon><Plus /></el-icon>
                </el-button>
                <el-button
                @click="()=>{removeKTree(); value = ''}">
                    <el-icon><Delete/></el-icon>
                </el-button>
            </div>
        </div>
        <el-divider/>
        <div class="wrapper">
            <QuizTask v-if="HomePage.pageSelected=='QuizTask'"/>
            <LearningTask v-if="HomePage.pageSelected=='LearningTask'"/>
            <MindTrace v-if="HomePage.pageSelected=='MindTrace'"/>
            <QuizTrace v-if="HomePage.pageSelected=='QuizTrace'"/>
        </div>
    </el-container>
</template>