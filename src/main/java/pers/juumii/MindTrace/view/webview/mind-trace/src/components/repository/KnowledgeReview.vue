<script setup>
import QuizCardVue from '../atomic/QuizCard.vue'
import { KnowledgeReview, init } from '@/js/mirror/repository/KnowledgeReview'
import { linkToKnowledge, RepositoryPage } from '@/js/mirror/repository/RepositoryPage'
import { onMounted, watch } from 'vue'

onMounted(()=>{
    setTimeout(() => {
        init()
    }, 500);
})
watch(RepositoryPage, ()=>init())

</script>

<template>
    <el-container direction="vertical">
        <div class="poor-completion" v-if="KnowledgeReview.pageSelected == 'Poor Completion'">
            <div class="header-info">Poor Completion</div>
            <QuizCardVue 
            v-for="card in KnowledgeReview.poorlyCompletedQuizCards" :key="card.id" :id="card.id"
            class="card-margin">
                <template #general-plugin>
                    <el-icon 
                    class="vertical-center icon-button" 
                    style="color:#79bbff; position: relative; left:35vw" 
                    @click="() => {linkToKnowledge(card.knowledgeId)}"
                    size="120%"><Promotion /></el-icon>
                </template>
            </QuizCardVue>
        </div>
    </el-container>
</template>