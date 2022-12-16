<script setup>
import {KnowledgeFolder, addKNode, removeKNode , init} from "@/js/mirror/repository/KnowledgeFolder"
import { general, updateSelectedKTreeName, saveData, init as initGeneral } from "@/js/mirror/general"
import { RepositoryPage, updateSelectedIndexes } from "@/js/mirror/repository/RepositoryPage"
import { onMounted, watch } from "vue"
import { AppVue } from "@/js/mirror/AppVue"

const emits = defineEmits(['changePage'])

onMounted(()=>{init()})
watch(general, ()=>{init()})

</script>

<template>
    <div class="space-between" style="margin-top:2vh">
        <div class="left" style="display:flex">
            <input 
            class="clear-input title length-50" 
            v-model="general.selectedKTree"
            @change="()=>updateSelectedKTreeName()"/>
            <el-button 
            class="add-sub-knowledge-button"
            @click="()=>addKNode()">
                <el-icon><ArrowRightBold /></el-icon>
            </el-button>
            <el-button 
            class="remove-knowledge-button"
            @click="()=>removeKNode()">
                <el-icon><Delete /></el-icon>
            </el-button>
        </div>
        <div class="right" style="margin-right:2vw">
            <el-button
            @click="()=>saveData()">
                <el-icon><FolderChecked /></el-icon>
            </el-button>
            <el-button
            @click="async()=>{await initGeneral() ; AppVue.pageSelected = 'home'}">
            <el-icon><HomeFilled /></el-icon>
          </el-button>
        </div>
    </div>
    <el-scrollbar class="kpath-scroll" style="width: 100vw;">
        <el-cascader-panel 
        class="kpath" 
        style="width:500vw; height: 20vh;"
        v-model="RepositoryPage.selectedIndexes"
        :options="KnowledgeFolder.options"
        :props="{checkStrictly: true}"
        @change="selected=>{updateSelectedIndexes(selected)}"/>
    </el-scrollbar>
</template>