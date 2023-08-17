<script setup>
import MainLeftToolbar from "@/components/toolbar/MainLeftToolbar.vue";
import LoginModal from "@/components/login/LoginModal.vue";
import MainTopToolbar from "@/components/toolbar/MainTopToolbar.vue";

import {useDialog} from 'naive-ui'
import {ref, inject, watch} from "vue";

const dialog = useDialog()

const needReload = inject('needReload')

const loginStatusDialog = ref(null)

watch(
    () => needReload.value,
    newData => {
      if (newData) {
        changeLoginStatusDialog()
      }
    })
const changeLoginStatusDialog = () => {
  if (loginStatusDialog === null) {
    loginStatusDialog.value = dialog.warning({
      showIcon: false,
      title: '警告',
      content: '登录状态发生改变，请刷新页面！',
      positiveText: '刷新',
      closable: false,
      closeOnEsc: false,
      maskClosable: false,
      positiveButtonProps: {
        tertiary: true,
      },
      onPositiveClick: () => {
        window.location.reload()
        return false
      }
    })
  }

}


// onMounted(() => {
//   window.addEventListener('storage', event => {
//     if (event.key === 'user') {
//       changeLoginStatusDialog()
//     }
//   })
// })
</script>

<template>
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
</template>

<style scoped>

</style>