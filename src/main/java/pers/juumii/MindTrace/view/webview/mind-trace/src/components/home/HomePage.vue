<script setup>
import {ref} from 'vue'
import QuizTask from './QuizTask.vue'
import data from '@/js/data'
import request from '@/js/request'

const emits = defineEmits(['changePage'])

// 用于让el-select在选中某项后显示
const value = ref('')


const mainPageSwitch = ref(0)
</script>

<template>
    <el-container direction="vertical">
        <div class="header">
            <el-select
            class="kTree-select"
            v-model="value"
            default-first-option
            @change="(option)=>request.useKTree(option)">
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
        <el-divider/>
        <QuizTask/>
    </el-container>
</template>

<style scoped>
.kTree-select{
    margin-right: 1vw;
}
.header{
    margin: 0 auto;
    height: 10vh;
    line-height: 10vh;
}
</style>