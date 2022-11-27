<script setup>
import KnowledgeFolder from '@/components/repository/KnowledgeFolder.vue'
import KnoledgeContent from '@/components/repository/KnoledgeContent.vue'
import HomePage from '@/components/home/HomePage.vue'
import {ref} from 'vue'
import operation from '@/js/operation'
import {init} from '@/js/init'

let pageSelected = ref('home')



</script>

<template>
  <div class="container">
      <div class="repository" v-if="pageSelected=='repository'">
        <knowledge-folder @changePage="pageSelected = 'home'"/>
        <knoledge-content/>
      </div>
      <div class="home" v-if="pageSelected=='home'">
        <HomePage @changePage="()=>{
            pageSelected='repository'
            operation.updateSelectedIndexes([0])
            init()}"/>
      </div>
  </div>
</template>