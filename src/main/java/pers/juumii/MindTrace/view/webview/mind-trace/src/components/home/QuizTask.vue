<script setup>
import QuizCard from '../atomic/QuizCard.vue'
import RadioGroup from '../atomic/RadioGroup.vue'
import SubpageHeader from '../atomic/SubpageHeader.vue'
import {general, getProtoType, synchronizeKTreeConfigs, getQuizGeneratorPrototype, getQuizCard, getKNode, updateKNodeById} from '@/js/mirror/general'
import { loadQuizFinished, loadQuiz } from '@/js/mirror/home/QuizTask'
import {ref, onMounted} from 'vue'
import { HomePage } from '@/js/mirror/home/HomePage'
import { QuizTask, init } from '@/js/mirror/home/QuizTask'
import { linkToKnowledge } from '@/js/mirror/repository/RepositoryPage'


const onCompletionSelected = async (selection, cardCopy)=>{
    let card = getQuizCard(cardCopy.id)  
    console.log("Selecetd Card:",card)  

    let completion = selection == 'difficult' ? 30: selection == 'good' ? 70: selection == 'perfect' ? 100: 0
    await getProtoType('quizRecord').then(record=>{
        record.cardId = card.id
        record.completion = completion
        record.description = selection
        card.quizRecords.push(record)
        console.log("quiz recorded:", card, getKNode(card.knowledgeId))
    })
    await updateKNodeById(card.knowledgeId)
    for(let i in QuizTask.quizCards)
        if(QuizTask.quizCards[i].id == card.id)
            QuizTask.quizCards.splice(i,1)
    await loadQuizFinished()
}



onMounted(()=>{init()})

</script>

<template>
    <el-container direction="vertical">
        <SubpageHeader
        @pageLeft="()=>HomePage.pageSelected= 'QuizTrace'"
        @pageRight="()=>HomePage.pageSelected= 'LearningTask'">
            <div class="header-info" v-if="!QuizTask.quizIsDue">
                Today is not Quiz Day.
            </div>
            <div class="header-info" v-if="(QuizTask.quizIsDue  && QuizTask.quizCards.length != 0)">
                Quiz Task !
            </div>
            <div class="header-info" v-if="QuizTask.quizIsDue && QuizTask.quizCards.length == 0">
                Quiz Task Finished !
            </div>
        </SubpageHeader>
        <div class="options-row space-evenly" style="margin: auto 42vw">
            <el-button @click="()=>QuizTask.pageSelected = 'Settings'">
                <el-icon><Setting /></el-icon>
            </el-button>
            <el-button @click="()=>{loadQuiz(); QuizTask.pageSelected = 'QuizTask'}">
                <el-icon><EditPen /></el-icon>
            </el-button>
        </div>
        <el-scrollbar style="height:70vh">
            <div class="settings" v-if="QuizTask.pageSelected == 'Settings'">
                <br/>
                <el-form v-model="general.kTreeConfigs" :inline="true" style="width:80vw; margin: 0 auto">
                    <el-form-item label="Period(days)">
                        <el-input-number 
                        v-model="general.kTreeConfigs.quizSchedule" 
                        :min="1" :max="30" 
                        @change="()=>synchronizeKTreeConfigs()" 
                        size="small"/>
                    </el-form-item>
                    <el-form-item label="Scale(mins)">
                        <el-input-number 
                        v-model="general.kTreeConfigs.quizScale" 
                        :min="1" :max="500" 
                        @change="()=>synchronizeKTreeConfigs()" 
                        size="small"/>
                    </el-form-item>
                    <el-form-item>
                        <el-tooltip
                        v-if="!QuizTask.quizIsDue"
                        content="reset time anchor" effect="light">
                            <el-icon 
                            class="icon-button" size="150%" style="color:#79bbff"
                            @click="()=>{
                                let now = new Date(Date.now())
                                //µ÷ÕûÊ±²î
                                now.setHours(now.getHours() + 8)
                                general.kTreeConfigs.timeAnchor = now.toISOString().split('.')[0]
                                synchronizeKTreeConfigs()
                            }"><RefreshRight /></el-icon>
                        </el-tooltip>
                    </el-form-item>
                    <el-divider/>
                    <el-form v-model="general.kTreeConfigs" :inline="true" style="width:80vw; margin: 0 auto" label-position="top">
                        <el-form-item label="Quiz Generator" >
                            <el-radio-group 
                            v-model="general.kTreeConfigs.quizGenerator.type"
                            @change="async ()=>{
                                await getQuizGeneratorPrototype(general.kTreeConfigs.quizGenerator.type).then(e =>general.kTreeConfigs.quizGenerator.configs=e)
                                await synchronizeKTreeConfigs()}"
                            style="width:20vw">
                                <el-radio label="RandomQuizGenerator">Random Quiz</el-radio>
                                <el-radio label="HorizontalQuizGenerator" >Horizontal Quiz</el-radio>
                                <el-radio label="VerticalQuizGenerator" >Vertical Quiz</el-radio>
                                <el-radio label="MultiBasedQuizGenerator" >Multi Based</el-radio>
                                <el-radio label="CompositionalQuizGenerator" >Compositional</el-radio>
                            </el-radio-group>
                        </el-form-item>
                        <el-form-item label="Configs" style="width:40vw; margin-left:3vw">
                            <div 
                            class="compositional-quiz-wrapper" style="width:100%; margin-left:2vw"
                            v-if="general.kTreeConfigs.quizGenerator.type=='CompositionalQuizGenerator'">
                                <div class="priority-header el-text-color">Priority</div>
                                <div class="priority-options">
                                    <el-button 
                                    style="margin-left: 0; display: block;" text
                                    v-for="(factor, index) in general.kTreeConfigs.quizGenerator.configs.priority" :key="factor"
                                    @click="()=>{
                                        let temp = general.kTreeConfigs.quizGenerator.configs.priority[0]
                                        general.kTreeConfigs.quizGenerator.configs.priority[0] = general.kTreeConfigs.quizGenerator.configs.priority[index]
                                        general.kTreeConfigs.quizGenerator.configs.priority[index] = temp
                                        synchronizeKTreeConfigs()
                                    }">
                                        ({{(index+1)}})&nbsp;{{factor == 'recordCompletionComparator' ? 'completion' : 
                                        factor == 'recordTimeComparator' ? 'time gap' :
                                        factor == 'rateComparator' ? 'importance' : 'null'}}
                                    </el-button>
                                </div>
                            </div>
                            <div class="weighted-quiz-wrapper" style="width:100%; margin-left:2vw"
                            v-if="(general.kTreeConfigs.quizGenerator.type=='MultiBasedQuizGenerator' && general.kTreeConfigs.quizGenerator.configs.weightMap != undefined)">
                                <div class="weight-header" style="color:#606266">Weights</div>
                                <div class="importance-weight">
                                    <span class="margin-right el-text-color" style="display:inline-block;width:13vw">importance:</span>
                                    <el-input-number size="small" v-model="general.kTreeConfigs.quizGenerator.configs.weightMap.rateEvaluator"/>
                                </div>
                                <div class="completion-weight">
                                    <span class="margin-right el-text-color" style="display:inline-block;width:13vw">completion:</span>
                                    <el-input-number size="small" v-model="general.kTreeConfigs.quizGenerator.configs.weightMap.recordCompletionEvaluator"/>
                                </div>
                                <div class="time-gap-weight">
                                    <span class="margin-right el-text-color" style="display:inline-block;width:13vw">time gap(days):</span>
                                    <el-input-number size="small" v-model="general.kTreeConfigs.quizGenerator.configs.weightMap.recordTimeEvaluator"/>
                                </div>
                            </div>
                        </el-form-item>
                    </el-form>
                </el-form>
            </div>
            <div class="quizzes" v-if="QuizTask.pageSelected == 'QuizTask'">
                <div class="quiz-task-wrapper" v-if="(QuizTask.quizIsDue  && QuizTask.quizCards.length != 0)">
                    <QuizCard 
                    v-for="card in QuizTask.quizCards" :key="card" 
                    :id="card.id" 
                    class="card-margin">
                        <template v-slot:general-plugin>
                            <RadioGroup @selected="(selection)=>{onCompletionSelected(selection,card)}">
                                <el-radio-button label="difficult" />
                                <el-radio-button label="good" />
                                <el-radio-button label="perfect" />
                            </RadioGroup>
                            <el-icon 
                            class="vertical-center icon-button" 
                            style="color:#79bbff; margin-left: 14vw;" 
                            @click="() => linkToKnowledge(card.knowledgeId)"
                            size="120%"><Promotion /></el-icon>
                        </template>
                    </QuizCard>
                </div>
                <div class="quiz-finished-wrapper card-margin" v-if="QuizTask.quizIsDue && QuizTask.quizCards.length == 0" >
                    <QuizCard 
                    v-for="card in QuizTask.finished" :key="card" 
                    :id="card.id"  
                    class="card-margin">
                        <template v-slot:general-plugin>
                            <el-icon 
                            class="vertical-center icon-button" 
                            :style="{
                                marginLeft:'30vw',
                                color: 
                                    card.quizRecords[card.quizRecords.length - 1].completion > 90 ? '#79bbff':
                                    card.quizRecords[card.quizRecords.length - 1].completion > 60 ? '#eebe77': '#f89898'}" 
                            @click="() => linkToKnowledge(card.knowledgeId)"
                            size="120%"><Promotion /></el-icon>
                        </template>
                    </QuizCard>
                </div>
            </div>
        </el-scrollbar>
    </el-container>
</template>
