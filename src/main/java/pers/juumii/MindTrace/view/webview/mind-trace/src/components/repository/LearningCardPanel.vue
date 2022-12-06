<script setup>
import LearningCard from '../atomic/LearningCard.vue'
import request from '@/js/request'
import operation from '@/js/operation'
import data from '@/js/data'

</script>

<template>
    <el-container direction="vertical" >
        <div class="learning-repository-wrapper" style="margin-bottom:1vh">
            <el-tag 
            v-for="card in data.kTreeConfigs.learningRepository" :key="card"
            class="icon-button  margin-horizontal" effect="plain" size="large" closable
            @click="async ()=>{
                await request.getProtoType('learningCard').then(e=>{
                    let id = e.id
                    let knowledgeId = operation.getSelectedKNode().data.id
                    e = card
                    e.id = id
                    e.knowledgeId = knowledgeId
                    operation.getSelectedKNode().data.learningCards.push(e)
                })
                await operation.updateSelectedKNode()
            }"
            @close="()=>{
                data.kTreeConfigs.learningRepository.splice(data.kTreeConfigs.learningRepository.indexOf(card),1)
                request.synchronizeKTreeConfigs()
            }">{{card.description}}</el-tag>
        </div>
        
        <LearningCard
        v-for="card in operation.getSelectedKNode().data.learningCards" :key="card" :id="card.id"
        style="margin-bottom:2vh">
            <template v-slot:general-plugin>
                <el-icon 
                class="icon-button vertical-center margin-horizontal"
                @click="()=>{
                    let cardCopy = data.Repository.toBePasted = JSON.parse(JSON.stringify(card))
                    data.kTreeConfigs.learningRepository.push(cardCopy)
                    request.synchronizeKTreeConfigs()
                    operation.getSelectedKNode().data.learningCards.splice(operation.getSelectedKNode().data.learningCards.indexOf(card),1)
                    operation.updateSelectedKNode()
                }"><Upload /></el-icon>

                <el-popover
                    placement="bottom"
                    trigger="hover"
                    :width="200">
                    <template #reference>
                        <el-icon class="icon-button vertical-center margin-horizontal"><Calendar /></el-icon>
                    </template>
                    <el-table :data="card.learningRecords">
                        <el-table-column width="200" property="time" label="date" />
                    </el-table>
                </el-popover>
                <el-popover
                    placement="bottom"
                    trigger="hover"
                    :width="300">
                    <template #reference>
                        <el-icon class="icon-button vertical-center  margin-horizontal"><PieChart /></el-icon>
                    </template>
                    <el-form-item label="scale:">
                        <el-slider 
                        v-model="card.scale" 
                        :step="5" :min="5" :max="120"
                        @change="()=>operation.updateKNodeById(card.knowledgeId)"/>
                    </el-form-item>
                </el-popover>

                <el-icon 
                class="icon-button vertical-center  margin-horizontal"
                @click="async ()=>{
                    await request.getProtoType('learningRecord').then(record =>{
                        record.cardId = card.id
                        record.completion = 1
                        card.learningRecords.push(record)
                    })
                    await operation.updateKNodeById(card.knowledgeId)
                }"><Finished /></el-icon>

                <el-button 
                style="margin-left:16vw"
                @click="()=>{
                    //使用splice删除这张卡片
                    operation.getSelectedKNode().data.learningCards.splice(operation.getSelectedKNode().data.learningCards.indexOf(card),1)
                    operation.updateSelectedKNode()
                }">
                <el-icon><Delete /></el-icon>
                </el-button>
            </template>
        </LearningCard>
        <el-card class="add-card">
            <el-icon 
            class="icon-button" 
            size="150%"
            @click="async()=>{
                await request.getProtoType('learningCard').then(e=>{
                    e.knowledgeId = operation.getSelectedKNode().data.id
                    operation.getSelectedKNode().data.learningCards.push(e)
                })
                await operation.updateSelectedKNode()
            }">
            <Plus />
            </el-icon>
        </el-card>
    </el-container>
</template>