<script setup>
import {ref} from "vue"
const emits = defineEmits(['frontAction','backAction'])

const state = ref(true)

</script>

<template>
    <div class="container">
        <el-button
        v-if="state == true"
        @click="$emit('frontAction'); state = ! state">
            <slot name="front"></slot>
        </el-button>
        <el-button
        v-if="state == false"
        @click="$emit('backAction'); state = ! state">
            <slot name="back"></slot>
        </el-button>
    </div>
</template>