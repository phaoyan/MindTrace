<script setup>
import { ref } from 'vue'
import LearningCardPanel from './LearningCardPanel.vue'
import QuizCardPanel from './QuizCardPanel.vue'
import SwitchButton from '../atomic/SwitchButton.vue'
import data from '@/js/data'
import operation from '@/js/operation'

const activeName = ref('Quiz')
</script>

<template>
    <el-container v-if="operation.getSelectedKNode().data != null">
        <el-header class="header space-between">
            <input class="knowledge-title clear-input large-font" 
                v-model="operation.getSelectedKNode().data.description" 
                @change="() => {
                operation.updateSelectedKNode()
                data.folderUpdate = !data.folderUpdate}" />
            <div class="plugin-wrapper space-between">
                <SwitchButton
                @frontAction="()=>operation.shearSelectedKNode()"
                @backAction="()=>operation.shiftToSelectedKNode()"
                style="margin-right: 2vw;">
                    <template v-slot:front>
                        <el-icon><Scissor /></el-icon>
                    </template>
                    <template v-slot:back>
                        <el-icon><Check /></el-icon>
                    </template>
                </SwitchButton>
                <el-button 
                class="vertical-center" 
                @click="()=>{if(data.Repository.toBePasted.data == undefined) operation.copySelectedKNode(); else operation.pasteToSelectedKNode()}">
                    <el-icon v-if="data.Repository.toBePasted.data == undefined"><DocumentCopy /></el-icon>
                    <el-icon v-if="data.Repository.toBePasted.data != undefined"><CopyDocument /></el-icon>
                </el-button>
            </div>
        </el-header>
        <el-main class="main" style="padding-top:0">
            <el-tabs v-model="activeName">
                <el-tab-pane label="Info" name="Info">
                    <el-scrollbar class="main-scroll" style="height:54vh">
                        <el-container>
                            <el-button />
                        </el-container>
                    </el-scrollbar>
                </el-tab-pane>
                <el-tab-pane label="Learn" name="Learning">
                    <el-scrollbar class="main-scroll" style="height:54vh">
                        <LearningCardPanel/>
                    </el-scrollbar>
                </el-tab-pane>
                <el-tab-pane label="Quiz" name="Quiz">
                    <el-scrollbar class="main-scroll" style="height:54vh">
                        <QuizCardPanel/>
                    </el-scrollbar>
                </el-tab-pane>
            </el-tabs>
        </el-main>
    </el-container>
</template>