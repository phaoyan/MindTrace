<script setup>
import data from '@/js/data'
import request from '@/js/request';
import operation from '@/js/operation'


</script>

<template>
    <el-container direction="vertical">
        <el-card 
        class="learning-card"
        v-for="card in data.selectedKNode.data.learningCards" :key="card">
            <template #header>
                <div class="header-box space-between">
                    <input 
                    class="clear-input input-80" 
                    v-model="card.description"
                    @change="()=>operation.updateSelectedKNode()"/>
                    <el-button 
                    @click="()=>{
                        //使用splice删除这张卡片
                        data.selectedKNode.data.learningCards.splice(data.selectedKNode.data.learningCards.indexOf(card),1)
                        operation.updateSelectedKNode()
                    }">
                    <el-icon><Delete /></el-icon>
                    </el-button>
                </div>
            </template>

            <div class="content-box space-between">
                <el-input 
                type="textarea"
                autosize
                class="clear-input input-80"
                v-model="card.resource"
                @change="()=>operation.updateSelectedKNode()"/>
            </div>
        </el-card>
        <el-card class="add-card">
            <el-icon 
            class="icon-button" 
            size="150%"
            @click="async()=>{
                await request.getProtoType('learningCard').then(e=>{
                    e.knowledgeId = data.selectedKNode.data.id
                    data.selectedKNode.data.learningCards.push(e)
                })
                operation.updateSelectedKNode()
            }">
            <Plus />
            </el-icon>
        </el-card>
    </el-container>
</template>

<style scoped>
.learning-card{
    margin-bottom: 2vh;
}
.link{
    margin-left: 5vw;
}
</style>