<script setup>
import { getSelectedKNode } from '@/js/mirror/repository/RepositoryPage'
import { KnowledgeContent } from '@/js/mirror/repository/KnowledgeContent'
import { KnowledgeReview } from '@/js/mirror/repository/KnowledgeReview'

</script>

<template>

    <el-row>
        <el-col :span="0.5">
            <el-container direction="vertical">
                <el-icon 
                class="icon-button el-red" size="130%" style="position: relative; top:65vh; left:3vw"
                @click="()=>{
                    KnowledgeContent.pageSelected = 'Review'
                    KnowledgeReview.pageSelected = 'Poor Completion'
                }"
                ><Promotion/></el-icon>
            </el-container>
        </el-col>
        <el-col :span="23.5">
            <v-md-preview :text="getSelectedKNode().overviewMarkdown" />
        </el-col>
    </el-row>
</template>