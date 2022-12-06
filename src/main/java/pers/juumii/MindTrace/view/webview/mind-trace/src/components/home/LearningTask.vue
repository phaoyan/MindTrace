<script setup>
import LearningCard from '../atomic/LearningCard.vue'
import SubpageHeader from '../atomic/SubpageHeader.vue'
import data from '@/js/data'

</script>

<template>
    <el-container direction="vertical">
        <SubpageHeader
        @pageLeft="()=>data.Home.pageSelected = 'MindTrace'"
        @pageRight="()=>data.Home.pageSelected = 'QuizTask'">
            <div class="header-info">Learning Repository</div>
        </SubpageHeader>
        <el-scrollbar style="height:75vh">
            <div class="learning-cards-wrapper">
                
                <LearningCard v-for="card in data.kTreeConfigs.learningRepository" :key="card" :id="card.id" class="card-margin"></LearningCard>
            </div>
        </el-scrollbar>
    </el-container>
</template>