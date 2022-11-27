<script setup>
import QuizCard from '../atomic/QuizCard.vue'
import RadioGroup from '../atomic/RadioGroup.vue'
import data from '@/js/data'
import request from '@/js/request'
import operation from '@/js/operation'
import {onMounted, reactive, ref} from 'vue'


const quizIsDue = ref(true)
let quizCards = reactive([])
const generateQuiz = ()=>{
    request.quizIsDue().then((isDue)=>{
        quizCards.splice(0,quizCards.length)
        quizIsDue.value = isDue
        if(isDue)
            request.generateQuiz().then(e=>{
                for(let i in e)
                    quizCards.push(e[i])})
        console.log(quizIsDue.value)
    })
}



const onCompletionSelected = async (selection, cardCopy)=>{
    let card = operation.getQuizCard(cardCopy.id)  
    console.log("Selecetd Card:",card)  

    let completion = selection == 'difficult' ? 30: selection == 'good' ? 70: selection == 'perfect' ? 100: 0
    await request.getProtoType('quizRecord').then(e=>{
        e.cardId = card.id
        e.completion = completion
        e.description = selection
        card.quizRecords.push(e)
        console.log("quiz recorded:", card, operation.getKNode(card.knowledgeId))
    })
    await operation.updateKNodeById(card.knowledgeId)
    for(let i in quizCards)
        if(quizCards[i].id == card.id)
            quizCards.splice(i,1)
}


const pageSelected = ref(1)

onMounted(()=>generateQuiz())

</script>

<template>
    <el-container direction="vertical">
        <div class="header basic-info">
            <div class="options" style="width: 50vw; margin:0 auto; display: flex; justify-content: space-evenly;">
                <el-button @click="()=>pageSelected = 0">
                    <el-icon><Setting /></el-icon>
                </el-button>
                <el-button @click="()=>{generateQuiz(); pageSelected = 1}">
                    <el-icon><EditPen /></el-icon>
                </el-button>
                <el-button @click="()=>{}">
                    <el-icon><Histogram /></el-icon>
                </el-button>
            </div>
        </div>
        <el-scrollbar style="height:75vh">
            <div class="settings" v-if="pageSelected == 0">
                <br/>
                <el-form v-model="data.settings" :inline="true" style="width:80vw; margin: 0 auto">
                    <el-form-item label="Period(days)">
                        <el-input-number 
                        v-model="data.settings.quizSchedule" 
                        :min="0" :max="30" 
                        @change="()=>request.synchronizeSettings()" 
                        size="small"/>
                    </el-form-item>
                    <el-form-item label="Scale(mins)">
                        <el-input-number 
                        v-model="data.settings.quizScale" 
                        :min="1" :max="500" 
                        @change="()=>request.synchronizeSettings()" 
                        size="small"/>
                    </el-form-item>
                    <el-form-item label="Quiz Generator">
                        <el-radio-group 
                        v-model="data.settings.quizGenerator"
                        @change="()=>request.synchronizeSettings()">
                            <el-radio label="RandomQuizGenerator" style="width:50vw">Random Quiz</el-radio>
                            <el-radio label="KnowledgeBasedQuizGenerator" style="width:50vw">Knowledge Based</el-radio>
                            <el-radio label="RecordBasedQuizGenerator" style="width:50vw">Record Based</el-radio>
                        </el-radio-group>
                    </el-form-item>
                </el-form>
            </div>
            <div class="quizzes" v-if="pageSelected == 1">
                <div class="idle-info" v-if="!quizIsDue">
                    Today is not Quiz Day.
                </div>
                <div class="quiz-task-wrapper" v-if="quizIsDue">
                    <QuizCard v-for="card in quizCards" :key="card" :card="card" 
                    style="margin: 2vh 4vw">
                        <template v-slot:general-plugin>
                            <RadioGroup @selected="(selection)=>{onCompletionSelected(selection,card)}">
                                <el-radio-button label="difficult" />
                                <el-radio-button label="good" />
                                <el-radio-button label="perfect" />
                            </RadioGroup>
                            <el-button 
                            class="vertical-center" style="margin-left:2vw; margin-right: 2vw;" 
                            @click="() => request.openLink()">
                                <el-icon>
                                    <Link />
                                </el-icon>
                            </el-button>
                        </template>
                    </QuizCard>
                </div>
            </div>
        </el-scrollbar>
    </el-container>
</template>