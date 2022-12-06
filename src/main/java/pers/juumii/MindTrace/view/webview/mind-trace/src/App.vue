<script setup>
import KnowledgeFolder from '@/components/repository/KnowledgeFolder.vue'
import KnoledgeContent from '@/components/repository/KnoledgeContent.vue'
import HomePage from '@/components/home/HomePage.vue'
import data from '@/js/data'
import operation from '@/js/operation'
import {init} from '@/js/init'

</script>

<template>
  <div class="container">
      <div class="repository" v-if="data.AppVue.pageSelected=='repository'">
        <knowledge-folder @changePage="data.AppVue.pageSelected = 'home'"/>
        <knoledge-content/>
      </div>
      <div class="home" v-if="data.AppVue.pageSelected=='home'">
        <HomePage @changePage="()=>{
            data.AppVue.pageSelected='repository'
            operation.updateSelectedIndexes([0])
            init()}"/>
      </div>
  </div>
</template>