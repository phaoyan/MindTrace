<script setup>
import {ref, inject, watch} from 'vue'
import QuizTask from './QuizTask.vue'

const emits = defineEmits(['changePage'])

const data = inject('data')
const operation = inject('operation')

// 用于让el-select在选中某项后显示
const value = ref('')

</script>


<template>
    <el-container direction="vertical">
        <div class="header">
            <el-select
            class="kTree-select"
            v-model="value"
            default-first-option
            @change="(option)=>operation.useKTree(option)">
                <el-option
                v-for="option in data.kTreeList.value" :key="option"
                :label="option"
                :value="option"/>
            </el-select>
            <el-button
            @click="emits('changePage')">
                <el-icon><Menu /></el-icon>
            </el-button>
            <el-button
            @click="()=>{operation.createKTree(); emits('changePage')}">
                <el-icon><Plus /></el-icon>
            </el-button>
            <el-button
            @click="()=>{operation.removeKTree()}">
                <el-icon><Delete/></el-icon>
            </el-button>
        </div>
        <el-divider/>
        <QuizTask />
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