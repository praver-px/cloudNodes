<script setup>
import {NotificationsNoneOutlined} from '@vicons/material'
import {useThemeStore} from "@/stores/themeStore";
import {storeToRefs} from "pinia";
import {useLoginModalStore} from "@/stores/loginModalStore";

const loginModalStore = useLoginModalStore()
const {changeLoginModalShowStatus} = loginModalStore

const themeStore = useThemeStore()
const {theme, isDarkTheme} = storeToRefs(themeStore)
const {changeTheme} = themeStore

</script>

<template>
  <n-space align="center" justify="space-between" style="height: 100%;">
    <n-text type="info">云笔记</n-text>
    <n-space>
      <n-avatar round src="https://gw.alipayobjects.com/zos/antfincdn/aPkFc8Sj7n/method-draw-image.svg"/>

      <n-divider vertical style="position: relative;top: 5px;"/>
      <!--      消息按钮-->
      <n-badge dot processing color="grey" :offset="[-8,4]">
        <n-button circle tertiary>
          <template #icon>
            <n-icon :component="NotificationsNoneOutlined"/>
          </template>
        </n-button>
      </n-badge>
      <!--      主题按钮-->
      <n-button circle tertiary @click="changeTheme(!isDarkTheme)">
        <template #icon>
          <n-icon :component="theme.icon"/>
        </template>
      </n-button>
      <!--      登录按钮-->
      <n-button tertiary @click="changeLoginModalShowStatus(true)">登录</n-button>
    </n-space>

  </n-space>

  <!--    登录注册-->

</template>

