<script setup>
import KnowledgeFolder from './components/KnowledgeFolder.vue'
import KnoledgeContent from './components/KnoledgeContent.vue'
import {ref, provide, onMounted} from 'vue'
import axios from 'axios'

// knowledge folder 中选中的元素的索引链
const selectedIndexes = ref([])
const selectedKnowledge = ref({})
// kTree
const kTree = ref({})
const data = {
  selectedIndexes: selectedIndexes,
  selectedKnowledge: selectedKnowledge,
  kTree: kTree
}


const getKTree = async ()=>{
  let kroot
  await axios.get("http://localhost:9090/knowledge/getRoot").then(e=>{
      kroot = e.data
  })
  console.log("request:")
  console.log(kroot)
  return kroot
}
const getKnowledgeById = async (id)=>{
  let knowledge
  await axios.get("http://localhost:9090/knowledge/getKnowledgeById?id="+id).then(e=>knowledge = e.data)
  console.log("get knowledge by id:")
  console.log(knowledge)
  return knowledge
}
const addKnowledgeBySuperId = async (superId)=>{
  if(superId == undefined)
    superId = 0
  await axios.get("http://localhost:9090/knowledge/addKnowledgeBySuperId?superId="+superId).then(e=>{
    console.log("add knowledge success: ")
    console.log(e.data)
  })
}
const removeKnowledgeById = async (id)=>{
  if(id == undefined)
    id = 0
  await axios.get("http://localhost:9090/knowledge/remove?id="+id).then(e=>{
    console.log("remove knowledge success: ")
    console.log(e.data)
  })
}
const updateKnowledge = async (knowldege)=>{
  console.log("update knowledge")
  console.log(knowldege)
  await axios({
    method:"post",
    url:"http://localhost:9090/knowledge/update",
    data:knowldege
  })
}

const addLearningTask = async (knowledge)=>{
  if(knowledge.id == undefined)
    return

  await axios.get("http://localhost:9090/knowledge/addLearningCard?id="+knowledge.id).then(e=>{
    console.log("add learning card success: ")
    console.log(e.data)
  })
}
const updateLearningTask = async (task)=>{
  if(task.id == undefined)
    return

  await axios.get("http://localhost:9090/knowledge/updateLearningCard", {params:{json:JSON.stringify(task)}}).then(e=>{
    console.log("update learning card success: ")
    console.log(e.data)
  })
}
const removeLearningTask = async (task)=>{
  if(task.id == undefined)
    return

  await axios.get("http://localhost:9090/knowledge/removeLearningCard?id="+task.id).then(e=>{
    console.log("remove learning task success: ")
    console.log(e.data)
  })
}
const request = {
    getKTree: getKTree,
    getKnowledgeById: getKnowledgeById,
    addKnowledgeBySuperId: addKnowledgeBySuperId,
    removeKnowledgeById: removeKnowledgeById,
    updateKnowledge: updateKnowledge,
    addLearningTask: addLearningTask,
    updateLearningTask: updateLearningTask,
    removeLearningTask: removeLearningTask
}

const updateSelectedIndexes = async (newSelected)=>{
  selectedIndexes.value = newSelected
  await request.getKnowledgeById(selectedIndexes.value[selectedIndexes.value.length-1]).then(e => selectedKnowledge.value = e)
}
const updateSelectedKnowledge = async()=>{
  await request.updateKnowledge(selectedKnowledge.value)
  await request.getKTree().then(e=>kTree.value = e)
}
const addSubKnowledge = async ()=>{
  await request.addKnowledgeBySuperId(selectedKnowledge.value.id)
  await request.getKTree().then(e=>kTree.value = e)
}
const removeKnowledge = async ()=>{
  let tempIndexes = selectedIndexes.value.slice(0,-1)
  await request.removeKnowledgeById(selectedKnowledge.value.id)
  await request.getKTree().then(e=>kTree.value = e)
  updateSelectedIndexes(tempIndexes)
}
const updateSelectedLearningTask = async (task)=>{
  await request.updateLearningTask(task)
}
const U_addLearningTask = async ()=>{
  await request.addLearningTask(selectedKnowledge.value)
  await request.getKnowledgeById(selectedKnowledge.value.id).then(e => selectedKnowledge.value = e) 
}
const U_removeLearningTask = async (task)=>{
  await request.removeLearningTask(task)
  await request.getKnowledgeById(selectedKnowledge.value.id).then(e => selectedKnowledge.value = e) 
}
const update = {
  updateSelectedIndexes: updateSelectedIndexes,
  updateSelectedKnowledge: updateSelectedKnowledge,
  addSubKnowledge: addSubKnowledge,
  removeKnowledge: removeKnowledge,
  updateSelectedLearningTask: updateSelectedLearningTask,
  U_addLearningTask: U_addLearningTask,
  U_removeLearningTask: U_removeLearningTask
}
provide('data',data)
provide('update',update)

// 挂载后初始化data
onMounted(async ()=>{
  await request.getKTree().then(e=>kTree.value = e)
})

</script>

<template>
  <div class="container">
    <knowledge-folder/>
    <knoledge-content/>
  </div>
</template>

<style lang="less">
.container{
  overflow: hidden !important;
}
</style>
