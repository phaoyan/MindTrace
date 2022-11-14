<script setup>
import KnowledgeFolder from './components/repository/KnowledgeFolder.vue'
import KnoledgeContent from './components/repository/KnoledgeContent.vue'
import HomePage from './components/home/HomePage.vue'
import {ref, provide, onMounted} from 'vue'
import axios from 'axios'

const pageSelected = ref('repository')

// 选中kTree的名称
const selectedKTree = ref()
// 所有kTree的名称，用于向后台发送切换kTree的请求
const kTreeList = ref([])
// knowledge folder 中选中的元素的索引链
const selectedIndexes = ref([])
const selectedKNode = ref({})
// kRoot
const kRoot = ref({})
const folderUpdate = ref(true)
const selectedText = ref("")

const data = {
  kTreeList: kTreeList,
  selectedKTree: selectedKTree,
  selectedIndexes: selectedIndexes,
  selectedKNode: selectedKNode,
  kRoot: kRoot,
  folderUpdate: folderUpdate,
  selectedText: selectedText
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
const openLink = async ()=>{
  await axios({
    method:"POST",
    url:"http://localhost:9090/utils/openLink",
    data:selectedText.value,
    headers:{
      'Content-Type':'text/plain'
    }
  }).then(()=>{
    console.log("open link success.")
  })
}
const openMarkdown = async (text)=>{
  await axios({
    method:"POST",
    url:"http://localhost:9090/utils/markdown/open",
    data:text,
    headers:{
      'Content-Type':'text/plain'
    }
  }).then(()=>{
    console.log("open markdown success.")
  }) 
}
const closeMarkdown = async ()=>{
  let res
  await axios.get("http://localhost:9090/utils/markdown/close").then((e)=>{
    res = e.data
    console.log("close markdown success.")
    console.log(res)
  })
  return res
}
const loadKTreeNames = async ()=>{
  await axios.get("http://localhost:9090/utils/data/check/all").then(e=>{
    data.kTreeList.value = e.data
    console.log("load kTree names success.")
    console.log(data.kTreeList.value)
  })
}
const loadCurrentKTreeName = async ()=>{
  await axios("http://localhost:9090/utils/data/check/current").then(e=>{
    data.selectedKTree.value = e.data
    console.log("load current kTree name success: ")
    console.log(data.selectedKTree.value)
  })
}
const useKTree = async (name)=>{
  await axios({
    method:"POST",
    url:"http://localhost:9090/utils/data/use",
    data:name,
    headers:{
      'Content-Type':'text/plain'
    }
  })
  init()
  console.log("use kTree:" + name)
}
const createKTree = async ()=>{
  await axios({
    method:"GET",
    url:"http://localhost:9090/utils/data/create",
    headers:{
      'Content-Type':'text/plain'
    }
  })
  await init()
}
const removeKTree = async ()=>{
  await axios({
    method:"POST",
    url:"http://localhost:9090/utils/data/delete",
    data:data.selectedKTree.value,
    headers:{
      'Content-Type':'text/plain'
    }
  })
  console.log("remove kTree success.")
  await init()
}
const saveData = async ()=>{
  await axios.get("http://localhost:9090/utils/data/save").then(e=>{console.log("data synchronized.")})
}
const updateSelectedKTreeName = async ()=>{
  await axios.get("http://localhost:9090/utils/data/alterName?newName="+selectedKTree.value).then(e=>{
    console.log("kTee name changed.")
  })
  await loadKTreeNames()
}
const request = {
  loadKNode: loadKNode,
  synchronizeKNode: synchronizeKNode,
  getProtoType: getProtoType,
  openLink: openLink,
  openMarkdown: openMarkdown,
  closeMarkdown: closeMarkdown,
  useKTree: useKTree,
  saveData: saveData
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
  updateSelectedIndexes: updateSelectedIndexes,
  openLink: openLink,
  openMarkdown: openMarkdown,
  closeMarkdown: closeMarkdown,
  saveData: saveData,
  updateSelectedKTreeName: updateSelectedKTreeName,
  useKTree: useKTree,
  createKTree: createKTree,
  removeKTree: removeKTree
}
provide('data',data)
provide('operation',operation)
provide('getProtoType',request.getProtoType)

const init = async ()=>{
  await operation.getRoot()
  await operation.updateSelectedIndexes([0])
  await loadKTreeNames()
  await loadCurrentKTreeName()
}

// 挂载后初始化data
onMounted(async ()=>{
  init()
})

//监听文字选中，将其储存在selectedText中。用于实现超链接
document.addEventListener("selectionchange", () => {
  data.selectedText.value = window.getSelection().toString()
});

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
            init()}"/>
      </div>
  </div>
</template>

<style lang="less">

.container{
  overflow: hidden !important;
}
</style>
