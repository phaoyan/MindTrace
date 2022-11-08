<script setup>
import {ref, inject} from 'vue'
import SwitchButton from './SwitchButton.vue'
import SwitchCard from './SwitchCard.vue';

const data = inject('data')
const operation = inject('operation')
const getProtoType = inject('getProtoType')

//控制卡片正背面的数组: true for front, false for back
const switches = ref(data.selectedKNode.value.data.quizCards.map(card=>true))



</script>

<template>
    <container direction="vertical">
        <SwitchCard
        class="quiz-card"
        v-for="card in data.selectedKNode.value.data.quizCards" :key="card">
            <template v-slot:header>
                <input 
                class="clear-input" 
                v-model="card.description"
                @change="()=>operation.updateKNode(data.selectedKNode.value)"/> 
            </template>
            <template v-slot:front>
                <div class="front space-between"> 
                    <input 
                    v-if="!card.front.startsWith('$EXTERNAL$')"
                    class="clear-input input-100" 
                    v-model="card.front"
                    @change="()=>operation.updateKNode(data.selectedKNode.value)"/>
                    <SwitchButton
                    v-if="card.front.startsWith('$EXTERNAL$')"
                    @frontAction="()=>operation.openMarkdown(card.front)"
                    @backAction="()=>operation.closeMarkdown().then(e=>{card.front=e; operation.updateKNode(data.selectedKNode.value)})">
                        <template v-slot:front>
                            <el-icon><Document /></el-icon>   
                        </template>
                        <template v-slot:back>
                            <el-icon><Check /></el-icon>
                        </template>
                    </SwitchButton>
                    <el-button 
                    @click="()=>{
                        //使用splice删除这张卡片
                        data.selectedKNode.value.data.quizCards.splice(data.selectedKNode.value.data.quizCards.indexOf(card),1)
                        operation.updateKNode(data.selectedKNode.value)
                    }">
                    <el-icon><Delete /></el-icon>
                    </el-button>
                </div>
            </template>
            <template v-slot:back>
                <div class="back space-between">
                    <input 
                    v-if="!card.back.startsWith('$EXTERNAL$')"
                    class="clear-input input-100" 
                    v-model="card.back"
                    @change="()=>operation.updateKNode(data.selectedKNode.value)"/> 
                    <SwitchButton
                    v-if="card.back.startsWith('$EXTERNAL$')"
                    @frontAction="()=>operation.openMarkdown(card.back)"
                    @backAction="()=>operation.closeMarkdown().then(e=>{card.back=e; operation.updateKNode(data.selectedKNode.value)})">
                        <template v-slot:front>
                            <el-icon><Document /></el-icon>   
                        </template>
                        <template v-slot:back>
                            <el-icon><Check /></el-icon>
                        </template>
                    </SwitchButton>
                    <el-button 
                    @click="()=>{
                        //使用splice删除这张卡片
                        data.selectedKNode.value.data.quizCards.splice(data.selectedKNode.value.data.quizCards.indexOf(card),1)
                        operation.updateKNode(data.selectedKNode.value)
                    }">
                    <el-icon><Delete /></el-icon>
                    </el-button>
                </div>
            </template>
        </SwitchCard>
        <el-card class="add-card">
            <el-icon 
            class="icon-button" 
            size="150%"
            @click="async()=>{
                await getProtoType('quizCard').then(e=>{
                    e.knowledgeId = data.selectedKNode.value.data.id
                    data.selectedKNode.value.data.quizCards.push(e)
                })
                switches.push(true)
                operation.updateKNode(data.selectedKNode.value)
            }">
            <Plus />
            </el-icon>
        </el-card>
    </container>
</template>

<style lang="less" scoped>
.quiz-card{
    margin-bottom: 2vh;

}
.add-card{
    display: flex;
    justify-content: space-between;
}

</style>