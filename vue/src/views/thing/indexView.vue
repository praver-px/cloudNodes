<script setup>
import {
  ArrowCircleDownRound,
  ArrowCircleUpRound,
  DeleteOutlineRound,
  EditNoteRound,
  ArrowUpwardRound,
  ContentPasteOffRound
} from '@vicons/material'
import {computed, ref} from 'vue'
import {useThemeStore} from "@/stores/themeStore";
import {storeToRefs} from "pinia";
import {getUserToken, loginInvalid} from '@/utils/userLoginUtil'
import {useLoadingBar, useMessage} from 'naive-ui'
import {noteBaseRequest} from "@/request/noteRequest";

const message = useMessage()
const loadingBar = useLoadingBar()

const things = ref([])

const thingCardTopIconText = top => {
  if (top) {
    return {
      icon: ArrowCircleDownRound,
      text: '取消置顶'
    }
  } else {
    return {
      icon: ArrowCircleUpRound,
      text: '置顶'
    }
  }
}

const themeStore = useThemeStore()
const {isDarkTheme} = storeToRefs(themeStore)

const thingFinishShadowColor = computed(() => {
  return isDarkTheme.value ? "#363433" : "#bdaead"
})
const loading = ref(true)
const topThing = async (isTop, thingId, index) => {
  const userToken = await getUserToken();
  loadingBar.start()
  const thing = things.value[index]
  thing.toTop = true
  const {data: responseData} = await noteBaseRequest.get(
      "/thing/top",
      {
        params: {isTop, thingId},
        headers: {userToken}
      }
  ).catch(() => {
    loadingBar.error()
    thing.toTop = false
    throw message.error(isTop ? "置顶失败！" : "取消置顶失败！")
  })

  if (responseData.success) {
    loadingBar.finish()
    message.success(responseData.message)
    getThingList(false)
  } else {
    loadingBar.error();
    thing.toTop = false
    message.error(responseData.message)
    if (responseData.code === "L_008") {
      loginInvalid(true)
    }
  }
}
const getThingList = async (isUpdateLoading) => {
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
    if (isUpdateLoading) loading.value = false
  } else {
    loadingBar.error();
    message.error(responseData.message)
    if (responseData.code === "L_008") {
      loginInvalid(true)
    }
  }
}

getThingList(true)
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
      <n-space v-else-if="!loading && things.length >0">
        <n-card
            :class="{'thing-card-finished': t.finished}"
            size="small"
            v-for="t in things"
            :key="t.id"
            embedded
            :bordered="isDarkTheme"
            :segmented="{'content':'soft'}"
            :title="t.title"
            style="min-width: 230px;">
          <template #header-extra>
            <n-popover>
              <template #trigger>
                <n-button type="error" text style="margin-left: 16px">
                  <n-icon :size="18" :component="DeleteOutlineRound"/>
                </n-button>
              </template>
              删除
            </n-popover>


            <n-popover>
              <template #trigger>
                <!-- !! 数值类型 -> boolean 类型 -->
                <n-button :disabled="t.toTop" text style="margin-left: 6px" @click="topThing(!!!t.top,t.id,index)">
                  <n-icon :size="18" :component="thingCardTopIconText(t.top).icon"/>
                </n-button>
              </template>
              {{ thingCardTopIconText(t.top).text }}
            </n-popover>

            <n-popover>
              <template #trigger>
                <n-button text style="margin-left: 6px">
                  <n-icon :size="18" :component="EditNoteRound"/>
                </n-button>
              </template>
              编辑
            </n-popover>

          </template>
          <template #default>
            <n-space>
              <n-tag v-for="tag in t.tags.split(',')" :key="tag" size="small" :bordered="false">{{ tag }}</n-tag>

            </n-space>
          </template>
          <template #footer>
            <n-space align="center" size="small">
              <n-tag
                  :color="{color:'rgba(0 0 0 0)',textColor: '#18A058'}"
                  v-if="t.top"
                  round
                  size="small"
                  :bordered="false">
                <n-icon size="18" :component="ArrowUpwardRound"/>
              </n-tag>
              <n-divider v-if="t.top" vertical/>
              <n-text depth="3">{{ t.updateTime }}</n-text>
            </n-space>
          </template>
        </n-card>
      </n-space>
      <!--      暂无内容-->
      <n-empty v-else style="margin: 20px auto;" description="暂无小记数据，灵光一现，小记一下？" :size="'huge'">
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
.n-card.thing-card-finished:after {
  position: absolute;
  content: '';
  width: 100px;
  height: 100px;
  left: 40px;
  transform: translateY(50px);
  background-image: url("@/assets/finish.png");
  background-size: 100px 100px;
  background-repeat: no-repeat;
  filter: drop-shadow(5px 3px 2px v-bind(thingFinishShadowColor));
}
</style>