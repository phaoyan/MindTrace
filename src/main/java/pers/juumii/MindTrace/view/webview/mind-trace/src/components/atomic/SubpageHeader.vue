<script setup>
const emits = defineEmits(['pageLeft','pageRight'])
</script>

<template>
    <div class="wrapper space-between">
        <div class="page-left" style="margin-left:4vw;">
            <el-icon class="icon-button" style="margin-top:3vh" size="150%"  @click="()=>emits('pageLeft')"><ArrowLeft /></el-icon>
        </div>
        <div class="plugins">
            <slot></slot>
        </div>
        <div class="page-right" style="margin-right:4vw;">
            <el-icon class="icon-button" style="margin-top:3vh" size="150%" @click="()=>emits('pageRight')"><ArrowRight /></el-icon>
        </div>
    </div>
</template>

