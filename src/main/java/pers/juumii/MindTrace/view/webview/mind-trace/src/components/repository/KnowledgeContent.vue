<script setup>
import KnowledgeReviewVue from './KnowledgeReview.vue'
import KnowledgeInfoPanelVue from './KnowledgeInfoPanel.vue'
import LearningCardPanel from './LearningCardPanel.vue'
import QuizCardPanel from './QuizCardPanel.vue'
import SwitchButton from '../atomic/SwitchButton.vue'
import { KnowledgeContent, shearSelectedKNode, shiftToSelectedKNode, copySelectedKNode, pasteToSelectedKNode } from '@/js/mirror/repository/KnowledgeContent'
import { getSelectedKNode, updateSelectedKNode } from '@/js/mirror/repository/RepositoryPage'
</script>

<template>
    <el-container v-if="getSelectedKNode().data != null">
        <el-header class="header space-between">
            <input class="knowledge-title clear-input large-font" 
                v-model="getSelectedKNode().data.description" 
                @change="() => {
                updateSelectedKNode()}" />
            <div class="plugin-wrapper space-between">
                <SwitchButton
                @frontAction="()=>shearSelectedKNode()"
                @backAction="()=>shiftToSelectedKNode()"
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
                @click="()=>{if(KnowledgeContent.toBePasted.data == undefined) copySelectedKNode(); else pasteToSelectedKNode()}">
                    <el-icon v-if="KnowledgeContent.toBePasted.data == undefined"><DocumentCopy /></el-icon>
                    <el-icon v-if="KnowledgeContent.toBePasted.data != undefined"><CopyDocument /></el-icon>
                </el-button>
            </div>
        </el-header>
        <el-main class="main" style="padding-top:0">
            <el-tabs v-model="KnowledgeContent.pageSelected">
                <el-tab-pane label="Info" name="Info">
                    <el-scrollbar class="main-scroll" style="height:54vh">
                        <KnowledgeInfoPanelVue/>
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
                <el-tab-pane label="Review" name="Review">
                    <el-scrollbar class="main-scroll" style="height:54vh">
                        <KnowledgeReviewVue/>
                    </el-scrollbar>
                </el-tab-pane>
            </el-tabs>
        </el-main>
    </el-container>
</template>