<script setup>
import SwitchIconButton from './SwitchIconButton.vue'
import constants from '@/js/constants'
import { ref } from 'vue'

const emits = defineEmits(['timerStopped'])

const totalSeconds = ref(0)

let timer
const startTimer = ()=>{
    timer = setInterval(()=>{totalSeconds.value += 1}, 1000)
}
const stopTimer = ()=>{
    clearInterval(timer)
    emits('timerStopped', totalSeconds.value)
    totalSeconds.value = 0
}
</script>

<template>
    <div class="wrapper-bar row-reverse" style="width:20vw">
        <SwitchIconButton
        class="margin-left"
        @frontAction="()=>startTimer()"
        @backAction="()=>stopTimer()">
            <template v-slot:front>
                <EditPen />
            </template>
            <template v-slot:back>
                <Finished />
            </template>
        </SwitchIconButton>
        <div class="vertical-center margin-right middle-font el-text-color" v-if="(totalSeconds != 0)">
            {{constants.durationTransfer(totalSeconds)[0]}}&nbsp;:
            {{constants.durationTransfer(totalSeconds)[1]}}&nbsp;:
            {{constants.durationTransfer(totalSeconds)[2]}}
        </div>
    </div>
</template>