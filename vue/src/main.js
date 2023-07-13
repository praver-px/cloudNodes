import './assets/main.css'

import {createApp} from 'vue'
// 全局状态管理机制
import {createPinia} from 'pinia'

import App from './App.vue'
import router from './router'

// 通用字体
import 'vfonts/Lato.css'
// 等宽字体
import 'vfonts/FiraCode.css'
import naive from "naive-ui";

import piniaPluginPersistedstate from 'pinia-plugin-persistedstate'

const pinia = createPinia()
pinia.use(piniaPluginPersistedstate)

const app = createApp(App)
app.use(router).use(naive).use(pinia)

app.mount('#app')
