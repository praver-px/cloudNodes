<script setup>
import {useThemeStore} from "@/stores/themeStore";
import {storeToRefs} from "pinia";
import LoginModal from "@/components/login/LoginModal.vue";
import MainTopToolbar from "@/components/toolbar/MainTopToolbar.vue";
import {onMounted} from "vue";
import MainLeftToolbar from "@/components/toolbar/MainLeftToolbar.vue";

//主题共享资源
const themeStore = useThemeStore()
//主题
const {theme} = storeToRefs(themeStore)
const {changeTheme} = themeStore
onMounted(() => {
  window.addEventListener('storage', event => {
    if (event.key === 'theme') {
      const newTheme = JSON.parse(event.newValue)
      changeTheme(newTheme.isDarkTheme)
    }
  })
})
</script>

<template>
  <n-config-provider :theme="theme.name">
    <n-loading-bar-provider>
      <n-notification-provider>
        <n-message-provider>
          <n-layout position="absolute">
            <n-layout-header style="height: 64px; padding:0 20px" bordered>
              <MainTopToolbar/>
            </n-layout-header>
            <n-layout has-sider position="absolute" style="top: 64px;">
              <n-layout-sider width="64px" bordered content-style="padding: 24px 0;text-align:center;">
                <main-left-toolbar/>
              </n-layout-sider>
              <router-view/>
            </n-layout>
          </n-layout>
          <LoginModal/>
        </n-message-provider>
      </n-notification-provider>
    </n-loading-bar-provider>
  </n-config-provider>
</template>

