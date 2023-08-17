<script setup>
import {
  AddBoxRound,
  ArticleOutlined,
  ListOutlined,
  SearchRound,
  AccessTimeRound,
  StarBorderRound,
  ShoppingCartOutlined,
  DeleteForeverOutlined
} from '@vicons/material'
import {h, ref, watch} from "vue";
import {NIcon} from 'naive-ui'
import {useRouter} from 'vue-router'
import bus from 'vue3-eventbus'

const router = useRouter()

//读图标
const renderIcon = (icon, size, color) => {
  return () => h(NIcon, {size, color}, {default: () => h(icon)})
}
//新增笔记小记菜单选项
const addBoxOption = [
  {
    label: '新增笔记',
    key: 'note',
    icon: renderIcon(ArticleOutlined, 22, '#409EFF'),
  },
  {
    label: '新增小记',
    key: 'thing',
    icon: renderIcon(ListOutlined, 22, '#67C23A'),
    props: {
      onclick: () => {
        //跳转地址
        router.push('/thing').then(() => {
          bus.emit('newCreateThing')
        })
      }
    }
  },
]

const mainMenus = [
  {label: '最近操作', icon: AccessTimeRound, size: 30, to: ''},
  {label: '笔记', icon: ArticleOutlined, size: 30, to: '/note'},
  {label: '小记', icon: ListOutlined, size: 30, to: '/thing'},
  {label: '收藏', icon: StarBorderRound, size: 32, to: ''},
  {label: '商城', icon: ShoppingCartOutlined, size: 30, to: ''},
  {label: '回收站', icon: DeleteForeverOutlined, size: 30, to: ''},
]

const routerPath = ref(router.currentRoute.value)

watch(() => router.currentRoute.value, newData => {
  routerPath.value = newData.path
})
</script>

<template>
  <n-space vertical>
    <!--    新增文件-->
    <n-dropdown :options="addBoxOption" placement="right-start">
      <n-button text type="primary">
        <n-icon :size="34" :component="AddBoxRound"/>
      </n-button>
    </n-dropdown>
    <!--    搜索文件-->
    <n-button text>
      <n-icon :size="34" :component="SearchRound"/>
    </n-button>
  </n-space>

  <n-divider style="width: 34px; margin: 15px auto"/>

  <n-space vertical :size="10">
    <n-popover v-for="menu in mainMenus" trigger="hover" placement="right" :show-arrow="false">
      <template #trigger>
        <n-button text @click="router.push(menu.to)" :quaternary="routerPath !== menu.to">
          <n-icon :size="menu.size" :color="routerPath===menu.to?'#63e2b7':null" :component="menu.icon"/>
        </n-button>
      </template>
      <span>{{ menu.label }}</span>
    </n-popover>
  </n-space>


</template>

<style scoped>

</style>