<script setup>
import { ref } from 'vue'

const emits = defineEmits(['selected'])

const selection = ref()
</script>

<template>
    <el-radio-group v-model="selection" @change="(selection)=>emits('selected',selection)">
        <slot></slot>
    </el-radio-group>
</template>