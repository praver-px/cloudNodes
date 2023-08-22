<script setup>
import {ContentPasteOffRound, SearchRound} from '@vicons/material'
import {ref, watch} from 'vue'
import {useThemeStore} from "@/stores/themeStore";
import {storeToRefs} from "pinia";
import {getUserToken, loginInvalid} from '@/utils/userLoginUtil'
import {useLoadingBar, useMessage} from 'naive-ui'
import {noteBaseRequest} from "@/request/noteRequest";
import ThingCard from '@/components/thing/tingCard.vue'
import EditThingModal from "@/components/thing/EditThingModal.vue";
import gsap from "gsap";
import {useUserStore} from "@/stores/userStore"

const message = useMessage()
const loadingBar = useLoadingBar()

const things = ref([])

const userStore = useUserStore()
const {token, id: user_id} = userStore

watch(
    () => token.value,
    newData => {
      if (newData !== null) {
        loading.value = true
        getThingList(true, false)
        const editThingValue = editThingModalRef.value;
        if (editThingValue.thingId !== null && editThingValue.userId !== user_id) {
          editThingValue.show = false
        }
      }
    }
)

const themeStore = useThemeStore()
const {isDarkTheme} = storeToRefs(themeStore)


const loading = ref(true)


//编辑小记模态框的引用
const editThingModalRef = ref(null)

const search = ref(null)

const filter = ref(null)

let enterDelay = true
let hiddenAnimation = true

const filterOptions = [
  {label: '默认', value: null},
  {label: '已完成', value: 1},
  {label: '未完成', value: 0},
]

const getThingList = async (ed, ha) => {
  enterDelay = ed
  hiddenAnimation = ha
  //判断登录状态
  const userToken = await getUserToken();
  //发送获取小记列表请求
  loadingBar.start()

  const {data: responseData} = await noteBaseRequest.get(
      "/thing/list",
      {
        params: {
          search: search.value,
          filter: filter.value
        },
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
getThingList(true, false)

// 执行动画前的位置
const beforeEnter = (el) => {
  gsap.set(el, {
    y: 30,
    opacity: 0
  })

}

// 执行动画
const enterEvent = (el, done) => {
  gsap.to(el, {
    y: 0,//偏移量 x,y
    opacity: 1,//透明度
    duration: 0.3,//动画时间 单位 s
    delay: () => (enterDelay ? el.dataset.index * 0.12 : 0),//延迟动画
    onComplete: done //动画完成后需要调用此函数
  })
}

const beforeLeave = (el) => {
  if (hiddenAnimation) {
    const left = el.offsetLeft
    const top = el.offsetTop
    gsap.set(el, {
      position: 'absolute',
      boxShadow: '0 0 5px black',
      zIndex: 1,
      top,
      left
    })
  }
}
const leaveEvent = (el, done) => {
  if (hiddenAnimation) {
    let tl = gsap.timeline() //创建动画时间线
    tl.to(el, {
      scale: 1.3,
      duration: 0.25,
    }).to(el, {
      scale: 0,
      duration: 0.25,
      onComplete: done
    })
  } else {
    gsap.to(el, {
      duration: 0,//动画时间 s
      onComplete: done
    })
  }

}


//删除提醒框对象
const deleteRemind = ref({
  show: false,//显示情况
  id: null,//小记id
  title: null,//提醒框内容
})
const showDeleteRemindDialog = (id, title) => {
  deleteRemind.value.id = id
  deleteRemind.value.show = true
  deleteRemind.value.title = title
}

const toDeleteTing = async (complete) => {
  deleteRemind.value.show = false
  const userToken = await getUserToken();
  loadingBar.start()
  const {data: responseData} = await noteBaseRequest.get(
      "/thing/delete",
      {
        params: {complete, thingId: deleteRemind.value.id, isRecycle: false},
        headers: {userToken}
      }).catch(() => {
    loadingBar.error()
    throw message.error(complete ? '彻底删除小记请求失败！' : '删除小记请求失败！')
  })

  if (responseData.success) {
    loadingBar.finish()
    message.success(responseData.message)
    getThingList(false, true)
  } else {
    message.error(responseData.message)
    if (responseData.code === "L_008") {
      loginInvalid(true)
    }
  }
}
</script>

<template>
  <n-layout embedded content-style="padding:24px">
    <n-card size="small" :bordered="false">
      <template #header>
        <h3>小记界面</h3>
      </template>
      <template #header-extra>
        <n-space>
          <!--          搜索-->
          <n-input-group>
            <n-input v-model:value="search" placeholder="搜索"/>
            <n-button @click="getThingList(false)">
              <n-icon size="20" :component="SearchRound"/>
            </n-button>
          </n-input-group>
          <n-select @update:value="getThingList(false)" :options="filterOptions" v-model:value="filter"
                    placeholder="筛选" style="width: 120px;"/>
          <n-button dashed @click="editThingModalRef.showEditModal(null)">新增小记</n-button>
        </n-space>
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
        <TransitionGroup @before-enter="beforeEnter" @enter="enterEvent" @before-leave="beforeLeave"
                         @leave="leaveEvent" move-class="move-transition">
          <template v-if="!loading && things.length >0">
            <thing-card
                class="thing-cards"
                @delete="showDeleteRemindDialog(thing.id,thing.title)"
                v-for="(thing,index) in things"
                :key="thing.id"
                :id="thing.id"
                :data-index="index"
                :title="thing.title"
                :finished="!!thing.finished"
                :top="!!thing.top"
                :tags="thing.tags.split(',')"
                :time="thing.updateTime"
                @edit="editThingModalRef.showEditModal(thing.id)"
                @change-status="getThingList(false,false)"/>
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
          <n-button dashed @click="editThingModalRef.showEditModal(null)">小记一笔</n-button>
        </template>
      </n-empty>
    </n-card>
  </n-layout>
  <!--删除提醒-->
  <DeleteRemind
      @completeDelete=""
      :show="deleteRemind.show"
      @delete="toDeleteTing"
      @cancel="deleteRemind.show = false"
      :title="deleteRemind.title"/>

  <!--  编辑-->
  <EditThingModal ref="editThingModalRef" @save="getThingList"/>
</template>
<style scoped>
.move-transition {
  transition: all 0.5s;
}
</style>