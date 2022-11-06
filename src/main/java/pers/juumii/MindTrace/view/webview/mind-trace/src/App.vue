<script setup>
import KnowledgeFolder from './components/KnowledgeFolder.vue'
import KnoledgeContent from './components/KnoledgeContent.vue'
import {ref, provide, onMounted} from 'vue'
import axios from 'axios'

// knowledge folder 中选中的元素的索引链
const selectedIndexes = ref([])
const selectedKNode = ref({})
// kRoot
const kRoot = ref({})
const folderUpdate = ref(true)
const data = {
  selectedIndexes: selectedIndexes,
  selectedKNode: selectedKNode,
  kRoot: kRoot,
  folderUpdate: folderUpdate
}

const loadKNode = async (id)=>{
  let kNode
  await axios.get("http://localhost:9090/KNode/load?id="+id).then((e)=>{
    kNode = e.data
    console.log("getKNode success: ")
    console.log(kNode)
  })
  return kNode
}
const synchronizeKNode = async (newState)=>{
  console.log("synchronize: ")
  console.log(newState)
  await axios({
    method:"POST",
    url:"http://localhost:9090/KNode/synchronize",
    data:newState
  }).then(()=>{
    console.log("update data success.")
  })
}
const getProtoType = async (type)=>{
  let res
  await axios.get("http://localhost:9090/KNode/protoType?type="+type).then(e=>res=e.data)
  console.log("protoType: ")
  console.log(res)
  return res
}
const request = {
  loadKNode: loadKNode,
  synchronizeKNode: synchronizeKNode,
  getProtoType: getProtoType
}


const getRoot = async ()=>{
  await request.loadKNode(0).then(e=>kRoot.value=e)
  folderUpdate.value = !folderUpdate.value
}
const updateKNode = async (kNode)=>{
  await request.synchronizeKNode(kNode)
}
const updateSelectedKNode = ()=>{
  data.selectedKNode.value = data.kRoot.value
  for(let i = 1; i < data.selectedIndexes.value.length; i ++){
    for(let j = 0; j < data.selectedKNode.value.subKNodes.length; j ++){
      if(data.selectedKNode.value.subKNodes[j].data.id == data.selectedIndexes.value[i]){
        data.selectedKNode.value = data.selectedKNode.value.subKNodes[j]
        // console.log("kRoot afterChange:")
        // console.log(kRoot)
      }
    }
  }
}
const updateSelectedIndexes = async (selectIndexes)=>{
  data.selectedIndexes.value = selectIndexes
  updateSelectedKNode()
  // console.log("update selected:")
  // console.log(selectedIndexes.value)
  // console.log(selectedKNode.value)
}
const operation = {
  getRoot: getRoot,
  updateKNode: updateKNode,
  updateSelectedIndexes: updateSelectedIndexes
}
provide('data',data)
provide('operation',operation)
provide('getProtoType',request.getProtoType)

// 挂载后初始化data
onMounted(async ()=>{
  await operation.getRoot()
  await operation.updateSelectedIndexes([0])
})

</script>

<template>
  <div class="container">
    <!-- <knowledge-folder/>
    <knoledge-content/> -->
  </div>
</template>

<style lang="less">
.container{
  overflow: hidden !important;
}
</style>
