<script setup>
import SwitchCard from './SwitchCard.vue'
import EditableContent from './EditableContent.vue'
import { getQuizCard, updateKNodeById } from '@/js/mirror/general';

const props = defineProps({id: Number})

</script>

<template>
    <SwitchCard v-if="getQuizCard(props.id) != undefined">
        <template v-slot:header>
            <div class="space-between">
                <EditableContent
                style="width:30vw;margin-right: 4vw;"
                :text="getQuizCard(props.id).description"
                :external="false"
                :edit="false"
                @change="async (text)=>{
                    getQuizCard(props.id).description = text;
                    await updateKNodeById(getQuizCard(props.id).knowledgeId)}"/>
                <slot name="general-plugin"></slot>
            </div>
        </template>
        <template v-slot:front>
            <div class="space-between">
                <EditableContent
                :text="getQuizCard(props.id).front"
                :edit="getQuizCard(props.id).front == ''"
                @change="async (text)=>{
                    getQuizCard(props.id).front = text
                    await updateKNodeById(getQuizCard(props.id).knowledgeId)}"/>
                <slot name="front-plugin"></slot>
            </div>
        </template>
        <template v-slot:back>
            <div class="space-between">
                <EditableContent
                :text="getQuizCard(props.id).back"
                :edit="getQuizCard(props.id).back == ''"
                @change="async (text)=>{
                    getQuizCard(props.id).back = text;
                    await updateKNodeById(getQuizCard(props.id).knowledgeId)}"/>
                <slot name="back-plugin"></slot>
            </div>
        </template>
    </SwitchCard>
</template>