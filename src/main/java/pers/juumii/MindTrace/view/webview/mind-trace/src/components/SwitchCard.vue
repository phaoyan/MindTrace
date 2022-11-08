<script setup>
import {ref} from "vue"
const emits = defineEmits(['frontAction','backAction'])

const state = ref(true)

</script>

<template>
    <el-card>
        <template #header>
            <div class="header space-between">
                <slot name="header">
                    
                </slot>
                <el-switch v-model="state"></el-switch>
            </div>
        </template>
        <slot 
        v-if="state == true"
        name="front">

        </slot>
        <slot 
        v-if="state == false"
        name="back">

        </slot>
    </el-card>
</template>