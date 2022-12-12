<script setup>
import QuizCard from '../atomic/QuizCard.vue'
import { getSelectedKNode } from '@/js/mirror/repository/RepositoryPage'
import { updateKNodeById } from '@/js/mirror/general'
import { removeCard, addCard, shearSelectedQuizCard, shiftSelectedQuizCard, QuizCardPanel } from '@/js/mirror/repository/QuizCardPanel'

</script>

<template>
    <el-container direction="vertical">
        <QuizCard 
        v-for="card in getSelectedKNode().data.quizCards" :key="card" 
        :id="card.id" 
        style="margin-bottom:2vh">
            <template v-slot:general-plugin>
                <el-icon class="icon-button vertical-center" style="margin-right:5vw" @click="()=>shearSelectedQuizCard(card.id)"><Scissor /></el-icon>
                <el-popover
                    placement="bottom"
                    trigger="hover"
                    :width="350">
                    <template #reference>
                        <el-icon class="icon-button vertical-center" style="margin-right: 5vw"><Calendar /></el-icon>
                    </template>
                    <el-table :data="card.quizRecords">
                        <el-table-column width="200" property="time" label="date" />
                        <el-table-column width="150" property="completion" label="completion" />
                    </el-table>
                </el-popover>
                <el-popover
                    placement="bottom"
                    trigger="hover"
                    :width="300">
                    <template #reference>
                        <el-icon class="icon-button vertical-center"><PieChart /></el-icon>
                    </template>
                    <el-form-item label="scale:">
                        <el-slider 
                        v-model="card.scale" 
                        :step="1" :min="1" :max="15"
                         @change="()=>updateKNodeById(card.knowledgeId)"/>
                    </el-form-item>
                </el-popover>
                <el-rate style="margin-left:5vw" v-model="card.rate" allow-half  @change="()=>updateKNodeById(card.knowledgeId)"/>

                <el-button @click="()=>removeCard(card)" style="margin-left:5vw">
                    <el-icon><Delete /></el-icon>
                </el-button>
            </template>
        </QuizCard>
        

        <el-card class="space-between">
            <el-icon 
                class="icon-button" size="150%" style="margin-right:5vw"
                @click="()=>addCard()">
                <Plus />
            </el-icon>
            <el-icon
                v-if="QuizCardPanel.quizCardToBeShifted != undefined"
                class="icon-button" size="150%"
                @click="()=>shiftSelectedQuizCard()">
                <Scissor/>
            </el-icon>
        </el-card>
    </el-container>
</template>
