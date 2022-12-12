<script setup>
import LearningCard from '../atomic/LearningCard.vue'
import { general, getProtoType, synchronizeKTreeConfigs, updateKNodeById } from '@/js/mirror/general'
import { KnowledgeContent } from '@/js/mirror/repository/KnowledgeContent'
import { getSelectedKNode, updateSelectedKNode } from '@/js/mirror/repository/RepositoryPage'

</script>

<template>
    <el-container direction="vertical" >
        <div class="learning-repository-wrapper" style="margin-bottom:1vh">
            <el-tag 
            v-for="card in general.kTreeConfigs.learningRepository" :key="card"
            class="icon-button  margin-horizontal" effect="plain" size="large" closable
            @click="async ()=>{
                await getProtoType('learningCard').then(e=>{
                    let id = e.id
                    let knowledgeId = getSelectedKNode().data.id
                    e = card
                    e.id = id
                    e.knowledgeId = knowledgeId
                    getSelectedKNode().data.learningCards.push(e)
                })
                await updateSelectedKNode()
            }"
            @close="()=>{
                general.kTreeConfigs.learningRepository.splice(general.kTreeConfigs.learningRepository.indexOf(card),1)
                synchronizeKTreeConfigs()
            }">{{card.description}}</el-tag>
        </div>
        
        <LearningCard
        v-for="card in getSelectedKNode().data.learningCards" :key="card" :id="card.id"
        style="margin-bottom:2vh">
            <template v-slot:general-plugin>
                <el-icon 
                class="icon-button vertical-center margin-horizontal"
                @click="()=>{
                    let cardCopy = KnowledgeContent.toBePasted = JSON.parse(JSON.stringify(card))
                    general.kTreeConfigs.learningRepository.push(cardCopy)
                    synchronizeKTreeConfigs()
                    getSelectedKNode().data.learningCards.splice(getSelectedKNode().data.learningCards.indexOf(card),1)
                    updateSelectedKNode()
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
                        @change="()=>updateKNodeById(card.knowledgeId)"/>
                    </el-form-item>
                </el-popover>

                <el-icon 
                class="icon-button vertical-center  margin-horizontal"
                @click="async ()=>{
                    await getProtoType('learningRecord').then(record =>{
                        record.cardId = card.id
                        record.completion = 1
                        card.learningRecords.push(record)
                    })
                    await updateKNodeById(card.knowledgeId)
                }"><Finished /></el-icon>

                <el-button 
                style="margin-left:16vw"
                @click="()=>{
                    //使用splice删除这张卡片
                    getSelectedKNode().data.learningCards.splice(getSelectedKNode().data.learningCards.indexOf(card),1)
                    updateSelectedKNode()
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
                await getProtoType('learningCard').then(e=>{
                    e.knowledgeId = getSelectedKNode().data.id
                    getSelectedKNode().data.learningCards.push(e)
                })
                await updateSelectedKNode()
            }">
            <Plus />
            </el-icon>
        </el-card>
    </el-container>
</template>