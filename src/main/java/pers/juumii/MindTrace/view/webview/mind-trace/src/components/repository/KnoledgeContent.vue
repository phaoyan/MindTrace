<script setup>
import { onMounted, ref } from 'vue'
import LearningCardPanel from './LearningCardPanel.vue'
import QuizCardPanel from './QuizCardPanel.vue'
import SwitchButton from '../atomic/SwitchButton.vue'
import data from '@/js/data'
import operation from '@/js/operation'
import request from '@/js/request'

const pageSelected = ref("quizCards")

const pageOptions = [
    {
        label: "info",
        value: "info"
    },
    {
        label: "quizCards",
        value: "quizCards"
    },
    {
        label: "learningCards",
        value: "learningCards"
    }
]


</script>

<template>
    <el-container v-if="data.selectedKNode.data != null">
        <el-header class="header space-between">
            <input class="knowledge-title" 
                v-model="data.selectedKNode.data.description" 
                @change="() => {
                operation.updateSelectedKNode()
                data.folderUpdate = !data.folderUpdate}" />
            <div class="plugin-wrapper space-between">
                <SwitchButton
                @frontAction="()=>operation.shearSelectedKNode()"
                @backAction="()=>operation.pasteToSelectedKNode()"
                style="margin-right: 2vw;">
                    <template v-slot:front>
                        <el-icon><Scissor /></el-icon>
                    </template>
                    <template v-slot:back>
                        <el-icon><Check /></el-icon>
                    </template>
                </SwitchButton>
                <el-button class="vertical-center" @click="() => request.openLink()">
                    <el-icon>
                        <Link />
                    </el-icon>
                </el-button>
                <div class="selection-wrapper vertical-center">
                    <el-cascader class="select" :options="pageOptions" v-model="pageSelected" />
                </div>
            </div>
        </el-header>
        <el-main class="main">
            <el-scrollbar class="main-scroll">
                <el-container v-if="pageSelected == 'info'">
                    <el-button />
                </el-container>
                <LearningCardPanel v-if="pageSelected == 'learningCards'" />
                <QuizCardPanel v-if="pageSelected == 'quizCards'" />
            </el-scrollbar>
        </el-main>
    </el-container>
</template>


<style lang="less" scoped>
.clear-input {
    background: none;
    outline: none;
    border: none;
    width: 80%;
}

.middle-font {
    font-size: 120%;
    font-weight: lighter;
    font-style: italic;
}

.large-font {
    font-size: 150%;
    font-weight: lighter;
    font-style: italic;
}

.knowledge-title {
    width: 50vw !important;
    .clear-input();
    .large-font();
    margin: 2vw;
}

.selection-wrapper {
    margin: auto 2vw;
}

.main-scroll {
    height: 45vh !important;
}
</style>