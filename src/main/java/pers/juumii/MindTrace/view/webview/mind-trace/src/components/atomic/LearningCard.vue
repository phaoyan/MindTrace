<script setup>
import EditableContent from './EditableContent.vue'
import {getLearningCard, updateKNodeById} from '@/js/mirror/general'



const props = defineProps({id:Number})
</script>

<template>
    <el-card v-if="(getLearningCard(props.id) != undefined)">
        <template #header>
            <div class="space-between">
                <EditableContent
                style="width:40vw;margin-right: 4vw;"
                :text="getLearningCard(props.id).description"
                :external="false"
                @change="(text)=>{
                    getLearningCard(props.id).description = text;
                    updateKNodeById(getLearningCard(props.id).knowledgeId)}"/>
                <slot name="general-plugin"></slot>
            </div>
        </template>
        <EditableContent
        :text="getLearningCard(props.id).resource"
        @change="(text)=>{
            getLearningCard(props.id).resource = text;
            updateKNodeById(getLearningCard(props.id).knowledgeId)}"/>
        <slot name="resource-plugin"></slot>
    </el-card>
</template>