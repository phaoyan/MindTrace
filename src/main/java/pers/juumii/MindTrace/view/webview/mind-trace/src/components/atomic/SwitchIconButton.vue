<script setup>
import {ref} from "vue"
const props = defineProps({
    //true for front, false for back
    isFront:{
        type:Boolean,
        default:true
    },
    size:{
        type:Number,
        default:130
    },
    getState:{
        type:Function,
        default:undefined
    }
})

const emits = defineEmits(['frontAction','backAction'])

const state = ref(props.isFront)

</script>

<template>
    <div class="container" style="margin: auto 0;">
        <el-icon
        class="icon-button vertical-center"
        :size="(size)+'%'"
        v-if="getState == undefined ? state == true : getState()"
        @click="()=>{$emit('frontAction'); state = ! state}">
            <slot name="front"></slot>
        </el-icon>
        <el-icon
        class="icon-button vertical-center"
        :size="(size)+'%'"
        v-if="getState == undefined ? state == false : !getState()"
        @click="()=>{$emit('backAction'); state = ! state}">
            <slot name="back"></slot>
        </el-icon>
    </div>
</template>