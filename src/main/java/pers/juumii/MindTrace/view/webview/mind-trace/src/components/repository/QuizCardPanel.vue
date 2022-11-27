<script setup>
import QuizCard from '../atomic/QuizCard.vue'
import data from '@/js/data'
import operation from '@/js/operation'
import request from '@/js/request'


const removeCard = (card)=>{
    //使用splice删除这张卡片
    data.selectedKNode.data.quizCards.splice(data.selectedKNode.data.quizCards.indexOf(card),1)
    operation.updateSelectedKNode()
}

const addCard = async()=>{
    await request.getProtoType('quizCard').then(e=>{
        e.knowledgeId = data.selectedKNode.data.id
        data.selectedKNode.data.quizCards.push(e)
    })
    operation.updateSelectedKNode()
}

</script>

<template>
    <el-container direction="vertical">
        <QuizCard v-for="card in data.selectedKNode.data.quizCards" :key="card" :card="card" style="margin-bottom:2vh">
            <template v-slot:general-plugin>
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
                        :step="1" :min="1" :max="5"
                         @change="()=>operation.updateKNodeById(card.knowledgeId)"/>
                    </el-form-item>
                </el-popover>
            </template>
            <template v-slot:front-plugin>
                <el-button @click="()=>removeCard(card)">
                    <el-icon><Delete /></el-icon>
                </el-button>
            </template>
            <template v-slot:back-plugin>
                <el-button @click="()=>removeCard(card)">
                    <el-icon><Delete /></el-icon>
                </el-button>
            </template>
        </QuizCard>
        

        <el-card class="space-between">
            <el-icon 
            class="icon-button" 
            size="150%"
            @click="()=>addCard()">
            <Plus />
            </el-icon>
        </el-card>
    </el-container>
</template>
