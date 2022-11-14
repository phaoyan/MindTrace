<script setup>
import {ref, inject, watch} from 'vue'

const emits = defineEmits(['changePage'])

const data = inject('data')
const operation = inject('operation')

// 用于让el-select在选中某项后显示
const value = ref('')

</script>


<template>
    <el-container>
        <div class="header">
            <el-select
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
    </el-container>
</template>