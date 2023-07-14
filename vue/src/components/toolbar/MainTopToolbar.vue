<script setup>
import {AccountBoxFilled, LogOutRound, ManageAccountsFilled, NotificationsNoneOutlined} from '@vicons/material'
import {NIcon, useLoadingBar, useMessage} from "naive-ui";
import {useThemeStore} from "@/stores/themeStore";
import {storeToRefs} from "pinia";
import {useLoginModalStore} from "@/stores/loginModalStore";
import {useUserStore} from "@/stores/userStore";
import {h, ref} from "vue";
import {noteBaseRequest} from "@/request/noteRequest";
import {loginInvalid} from "@/utils/userLoginUtil";

const message = useMessage()
const loadingBar = useLoadingBar()

const loginModalStore = useLoginModalStore()
const {changeLoginModalShowStatus} = loginModalStore

const themeStore = useThemeStore()
const {theme, isDarkTheme} = storeToRefs(themeStore)
const {changeTheme} = themeStore

//用户的共享数据
const userStore = useUserStore()
const {id: user_id, head_image, nickName, levelInfo} = storeToRefs(userStore)
const {resetUserInfo} = userStore
const userMenuShow = ref(false)

const renderIcon = icon => {
  return () => h(NIcon, null, {default: () => h(icon)});
}

//用户菜单
const userMenu = [
  {
    key: 'user-center',
    label: '个人中心',
    icon: renderIcon(AccountBoxFilled)
  },
  {
    key: 'account-setting',
    label: '账号设置',
    icon: renderIcon(ManageAccountsFilled)

  },
  {
    key: 'sign-out',
    label: '退出登录',
    icon: renderIcon(LogOutRound)

  }
]
//用户菜单选中回调函数
const clickUserMenu = (key) => {
  userMenuShow.value = false
  switch (key) {
    case 'user-center':
      break;
    case 'account-setting':
      break;
    case 'sign-out':
      signOutLogin()
      break;
  }
}

const signOutLogin = async () => {
  const userToken = localStorage.getItem("userToken");
  if (userToken === null) {
    throw  message.error("登录失效")
  }
  //删除redis中对应的key
  const {data: responseData} = await noteBaseRequest.get(
      "/user/login/out",
      {
        headers: {userToken}
      }
  ).catch(() => {
    throw  message.error("退出登录错误")
  })

  if (responseData.success) {
    loginInvalid(true)
    localStorage.removeItem('userToken')
    resetUserInfo()
  } else {
    message.error(responseData.message)
  }
}

</script>

<template>
  <n-space align="center" justify="space-between" style="height: 100%;">
    <n-text type="info">云笔记</n-text>
    <n-space>
      <n-popover trigger="hover" :warp-item="false" v-model:show="userMenuShow" :width="260" v-if="user_id !==null">
        <template #trigger>
          <n-button :bordered="false">
            <n-avatar
                round
                :src="head_image"/>
          </n-button>
        </template>
        <n-thing :title="nickName" width="260" content-style="padding:10px">
          <template #avatar>
            <n-avatar round size="large" :src="head_image" style="position: relative;top: 3px;"/>
          </template>
          <template #description>
            <n-space>
              <n-tag :bordered="false" size="small" type="success">
                {{ levelInfo }}
              </n-tag>
              <n-text>
                2099-12-31 过期
              </n-text>
            </n-space>
          </template>
          <template #default>
            <n-divider style="margin: 5px auto"/>
            <n-menu id="user-header-menu" :options="userMenu" :on-update-value="clickUserMenu"/>
          </template>
        </n-thing>
      </n-popover>

      <n-divider v-if="user_id !==null" vertical style="position: relative;top: 5px;"/>
      <!--      消息按钮-->
      <n-badge dot processing color="#a7535a" :offset="[-8,4]">
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
      <n-button v-if="user_id === null" tertiary @click="changeLoginModalShowStatus(true)">登录</n-button>
    </n-space>
  </n-space>

  <!--    登录注册-->
</template>

<style scoped>
.n-menu#user-header-menu:deep(.n-menu-item-content) {
  padding-left: 18px !important;
}
</style>

