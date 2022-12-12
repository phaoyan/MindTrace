<script setup>
import { getSelectedKNode } from '@/js/mirror/repository/RepositoryPage';

</script>

<template>
    <v-md-preview :text="getSelectedKNode().overviewMarkdown"/>
</template>