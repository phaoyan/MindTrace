<script setup>
import {ref} from "vue"
const props = defineProps({
    //true for front, false for back
    isFront:{
        type:Boolean,
        default:true
    }
})

const emits = defineEmits(['frontAction','backAction'])

const state = ref(props.isFront)

</script>

<template>
    <div class="container" style="margin: auto 0;">
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