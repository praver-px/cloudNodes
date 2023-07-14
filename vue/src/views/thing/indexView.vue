<script setup>
import {ContentPasteOffRound} from '@vicons/material'
import {ref} from 'vue'
import {useThemeStore} from "@/stores/themeStore";
import {storeToRefs} from "pinia";
import {getUserToken, loginInvalid} from '@/utils/userLoginUtil'
import {useLoadingBar, useMessage} from 'naive-ui'
import {noteBaseRequest} from "@/request/noteRequest";
import ThingCard from '@/components/thing/tingCard.vue'
import gsap from "gsap";

const message = useMessage()
const loadingBar = useLoadingBar()

const things = ref([])


const themeStore = useThemeStore()
const {isDarkTheme} = storeToRefs(themeStore)


const loading = ref(true)

const getThingList = async () => {
  //判断登录状态
  const userToken = await getUserToken();
  //发送获取小记列表请求
  loadingBar.start()

  const {data: responseData} = await noteBaseRequest.get(
      "/thing/list",
      {
        headers: {userToken}
      }
  ).catch(() => {
    loadingBar.error()
    throw message.error("获取小记请求失败！")
  })

  if (responseData.success) {
    loadingBar.finish()
    things.value = responseData.data
    loading.value = false
  } else {
    loadingBar.error();
    message.error(responseData.message)
    if (responseData.code === "L_008") {
      loginInvalid(true)
    }
  }
}

getThingList()
// 执行动画前的位置
const beforeEnter = (el) => {
  gsap.set(el, {
    y: 50,
    opacity: 0
  })
}
// 执行动画
const enterEvent = (el, done) => {
  gsap.to(el, {
    y: 0,//偏移量 x,y
    opacity: 1,//透明度
    duration: 0.3,//动画时间 单位 s
    delay: el.dataset.index * 0.12,//延迟动画
    onComplete: done //动画完成后需要调用此函数
  })
}
</script>

<template>
  <n-layout embedded content-style="padding:24px">
    <n-card size="small" :bordered="false">
      <template #header>
        <h3>小记界面</h3>
      </template>
      <template #header-extra>
        <n-button dashed>新增小记</n-button>
      </template>
    </n-card>

    <!--    小记列表容器-->
    <n-card size="small" :bordered="false" style="margin-top: 20px; ">
      <!--      加载骨架屏-->
      <n-space v-if="loading">
        <n-card
            style="min-width: 230px;"
            embedded
            :bordered="isDarkTheme"
            :segmented="{'content':'soft'}"
            size="small"
            v-for="n in 8">
          <template #header>
            <n-skeleton :width="180" size="small"/>
          </template>
          <template #header-extra>
            <n-skeleton circle :width="20" repeat="3" style="margin-left: 6px;"/>
          </template>
          <template #default>
            <n-space>
              <n-skeleton :width="50" :height="22"/>
              <n-skeleton :width="80" :height="22"/>
              <n-skeleton :width="50" :height="22"/>
            </n-space>
          </template>
          <template #footer>
            <n-skeleton text :width="140"/>
          </template>
        </n-card>
      </n-space>
      <!--      具体小记信息-->
      <n-space :wrap-item="false">
        <TransitionGroup @before-enter="beforeEnter" @enter="enterEvent">
          <template v-if="!loading && things.length >0">
            <thing-card
                class="thing-cards"
                v-for="(thing,index) in things"
                :key="thing.id"
                :id="thing.id"
                :data-index="index"
                :title="thing.title"
                :finished="!!thing.finished"
                :top="!!thing.top"
                :tags="thing.tags.split(',')"
                :time="thing.updateTime"
                @change-status="getThingList(false)"/>
          </template>
        </TransitionGroup>

      </n-space>
      <!--      暂无内容-->
      <n-empty v-if="!loading && things.length ===0" style="margin: 20px auto;"
               description="暂无小记数据，灵光一现，小记一下？" :size="'huge'">
        <template #icon>
          <n-icon :component="ContentPasteOffRound"/>
        </template>
        <template #extra>
          <n-button dashed>小记一笔</n-button>
        </template>
      </n-empty>
    </n-card>
  </n-layout>
</template>
<style scoped>

.n-card.thing-cards {
  transition: all 0.5s;
}
</style>