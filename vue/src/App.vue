<script setup>
import {useThemeStore} from "@/stores/themeStore";
import {storeToRefs} from "pinia";
import LoginModal from "@/components/login/LoginModal.vue";
import MainTopToolbar from "@/components/toolbar/MainTopToolbar.vue";


const themeStore = useThemeStore()
const {theme} = storeToRefs(themeStore)
</script>

<template>
  <n-config-provider :theme="theme.name">
    <n-loading-bar-provider>
      <n-message-provider>
        <n-layout position="absolute">
          <n-layout-header style="height: 64px; padding:0 20px" bordered>
            <MainTopToolbar/>
          </n-layout-header>
          <n-layout has-sider position="absolute" style="top: 64px;">
            <n-layout-sider width="64px" bordered content-style="padding: 24px 0;text-align:center;">
              海淀桥
            </n-layout-sider>
            <n-layout-content content-style="padding: 24px;">
              <n-h2>平山道</n-h2>
            </n-layout-content>
          </n-layout>
        </n-layout>
        <LoginModal/>
      </n-message-provider>
    </n-loading-bar-provider>
  </n-config-provider>
</template>

