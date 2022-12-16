<script setup>
import { ref } from 'vue'
import SwitchButton from './SwitchButton.vue'
import { uploadImage } from '@/js/mirror/atomic/EditableContent'

const props = defineProps({
    text: {
        type: String,
        default: ''
    },
    external: {
        type: Boolean,
        default: true
    },
    edit: {
        type: Boolean,
        default: false
    }
})

const emits = defineEmits(['change'])


const text = ref(props.text)
const markdownEditMode = ref(props.edit)

</script>

<template >
    <div class="space-between" style="width:100%" v-if="text != undefined">
        <div class="space-between" style="width:100%" v-if="external==true">
            <div class="left-buttons" style="margin-right:2vw">
                <SwitchButton
                style="width:10%"
                :isFront="!edit"
                @frontAction="()=>markdownEditMode = true"
                @backAction="()=>markdownEditMode = false">
                    <template v-slot:front><el-icon><Edit /></el-icon></template>
                    <template v-slot:back><el-icon><Finished /></el-icon></template>
                </SwitchButton>
            </div>
            <div class="text" style="width:90%">
                <el-scrollbar>
                    <v-md-editor 
                    v-if="markdownEditMode == true" 
                    v-model="text" 
                    @change="()=>{emits('change', text)}"
                    left-toolbar="undo redo | image"
                    :disabled-menus="[]"
                    @upload-image="(event, insertImage, files)=>uploadImage(event, insertImage, files)"/>
                    <v-md-preview v-if="markdownEditMode == false" :text="text"/>
                </el-scrollbar>
            </div>
        </div>
        <el-input 
        style="width: 100%"
        v-if="external==false"
        v-model="text"
        type="textarea"
        autosize
        @change="()=>{emits('change', text)}"/>
    </div>
</template>