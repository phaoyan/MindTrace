<script setup>
import KnowledgeFolderVue from './KnowledgeFolder.vue'
import KnowledgeContentVue from './KnowledgeContent.vue'
import data from '@/js/data'

</script>

<template>
    <KnowledgeFolderVue @changePage="data.AppVue.pageSelected = 'home'"/>
    <KnowledgeContentVue/>
</template>