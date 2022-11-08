import { createApp } from 'vue'
import App from './App.vue'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import './assets/reset.css'
import router from './router/index.js'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'

const app = createApp(App)

// element-plus icon
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component)
}

app.use(ElementPlus).use(router).mount('#app')
