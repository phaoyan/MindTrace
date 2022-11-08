<script setup>
import {inject} from 'vue'

const data = inject('data')
const operation = inject('operation')
const getProtoType = inject('getProtoType')

</script>

<template>
    <el-container direction="vertical">
        <el-card 
        class="learning-card"
        v-for="card in data.selectedKNode.value.data.learningCards" :key="card">
            <template #header>
                <div class="header-box space-between">
                    <input 
                    class="clear-input input-80" 
                    v-model="card.description"
                    @change="()=>operation.updateKNode(data.selectedKNode.value)"/>
                    <el-button 
                    @click="()=>{
                        //使用splice删除这张卡片
                        data.selectedKNode.value.data.learningCards.splice(data.selectedKNode.value.data.learningCards.indexOf(card),1)
                        operation.updateKNode(data.selectedKNode.value)
                    }">
                    <el-icon><Delete /></el-icon>
                    </el-button>
                </div>
            </template>

            <div class="content-box space-between">
                <input 
                class="clear-input input-80"
                v-model="card.resource"
                @change="()=>operation.updateKNode(data.selectedKNode.value)"/>
            </div>
        </el-card>
        <el-card class="add-card">
            <el-icon 
            class="icon-button" 
            size="150%"
            @click="async()=>{
                await getProtoType('learningCard').then(e=>{
                    e.knowledgeId = data.selectedKNode.value.data.id
                    data.selectedKNode.value.data.learningCards.push(e)
                })
                operation.updateKNode(data.selectedKNode.value)
            }">
            <Plus />
            </el-icon>
        </el-card>
    </el-container>
</template>

<style lang="less">
.learning-card{
    margin-bottom: 2vh;
}
.link{
    margin-left: 5vw;
}
</style>