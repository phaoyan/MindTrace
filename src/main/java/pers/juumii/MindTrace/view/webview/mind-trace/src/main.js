import { createApp } from 'vue'
import App from './App.vue'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import './assets/reset.css'
import router from './router/index.js'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import {init} from '@/js/mirror/general'
import data from '@/js/data'
import request from'@/js/request'

import VueMarkdownEditor from '@kangc/v-md-editor'
import '@kangc/v-md-editor/lib/style/base-editor.css'
import vuepressTheme from '@kangc/v-md-editor/lib/theme/vuepress.js'
import '@kangc/v-md-editor/lib/theme/style/vuepress.css'
import Prism from 'prismjs'
import createKatexPlugin from '@kangc/v-md-editor/lib/plugins/katex/cdn'

import VMdPreview from '@kangc/v-md-editor/lib/preview'
import '@kangc/v-md-editor/lib/style/preview.css'
import githubTheme from '@kangc/v-md-editor/lib/theme/github.js'
import '@kangc/v-md-editor/lib/theme/style/github.css'
import hljs from 'highlight.js'


const app = createApp(App)

//监听文字选中，将其储存在selectedText中。用于实现超链接
document.addEventListener("selectionchange", () => data.selectedText = window.getSelection().toString())
document.addEventListener("keyup", (e)=>{if(e.key == '?' && e.ctrlKey) request.openLink()})

//其他初始化项
init()

//集成markdown插件
VueMarkdownEditor.use(vuepressTheme, {Prism}).use(createKatexPlugin())
VMdPreview.use(githubTheme, {Hljs: hljs}).use(createKatexPlugin())


// element-plus icon
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component)
}

app.use(ElementPlus).use(router).use(VueMarkdownEditor).use(VMdPreview).mount('#app')
