<script setup>
import RepositoryPage from './components/repository/RepositoryPage.vue'
import HomePage from '@/components/home/HomePage.vue'
import { AppVue, init } from './js/mirror/AppVue'
import { onMounted } from 'vue'

onMounted(()=>init())

</script>

<template>
  <div class="container">
      <div class="repository" v-if="AppVue.pageSelected=='repository'">
        <RepositoryPage/>
      </div>
      <div class="home" v-if="AppVue.pageSelected=='home'">
        <HomePage/>
      </div>
  </div>
</template>