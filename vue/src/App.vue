<script setup>
import {useThemeStore} from "@/stores/themeStore";
import {storeToRefs} from "pinia";
import {onMounted, ref, provide} from "vue";
import {useUserStore} from '@/stores/userStore'
import RootView from "@/views/RootView.vue";

// 用户的共享资源
const userStore = useUserStore()
//主题共享资源
const themeStore = useThemeStore()

//主题
const {theme} = storeToRefs(themeStore)

const needReload = ref(false)

//为后代组件提供数据
provide('needReload', needReload)

//用户登录token
onMounted(() => {
  window.addEventListener('storage', event => {
    if (event.key === 'theme') {
      themeStore.$hydrate()
      // const newTheme = JSON.parse(event.newValue)
      // changeTheme(newTheme.isDarkTheme)
    } else if (event.key === 'user') {
      const newToken = JSON.parse(event.newValue).token;
      const oldToken = JSON.parse(event.oldValue).token;
      if (newToken === oldToken) {
        userStore.$hydrate()
      } else {
        needReload.value = true
        setTimeout(() => {

          needReload.value = false
        }, 1000)
      }
    }
  })
})
</script>

<template>
  <n-config-provider :theme="theme.name">
    <n-loading-bar-provider>
      <n-notification-provider>
        <n-dialog-provider>
          <n-message-provider>
            <root-view/>
          </n-message-provider>
        </n-dialog-provider>
      </n-notification-provider>
    </n-loading-bar-provider>
  </n-config-provider>
</template>

