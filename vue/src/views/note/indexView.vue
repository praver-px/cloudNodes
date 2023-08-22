<script setup>
import {computed, h, nextTick, ref} from "vue";
import NoteCard from "@/components/note/NoteCard.vue";
import {getUserToken, loginInvalid} from "@/utils/userLoginUtil";
import {NIcon, useLoadingBar, useMessage} from 'naive-ui'
import {noteBaseRequest} from "@/request/noteRequest";
import gsap from "gsap";
import {
  DeleteOutlineOutlined,
  DriveFileRenameOutlineOutlined,
  NavigationRound,
  NextPlanRound,
  PlusRound,
  SubtitlesOffOutlined
} from '@vicons/material'

const message = useMessage()
const loadingBar = useLoadingBar()

const loading = ref(true)

const renderIcon = icon => {
  return () => h(NIcon, null, {default: () => h(icon)})
}

const noteList = ref([])

let enterDelay = true
let hiddenAnimation = true

// 执行动画前的位置
const beforeEnter = (el) => {
  gsap.set(el, {
    x: 30,
    opacity: 0
  })

}

// 执行动画
const enterEvent = (el, done) => {
  gsap.to(el, {
    x: 0,//偏移量 x,y
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
      width: 'calc(100% -24px)',
      zIndex: 1,
      top,
      left,
      backdropFilter: 'blur(5px)'
    })
  }
}

const leaveEvent = (el, done) => {
  if (hiddenAnimation) {
    gsap.to(el, {
      scale: 0,
      duration: 0.5,
      onComplete: done
    })
  } else {
    gsap.to(el, {
      duration: 0,//动画时间 s
      onComplete: done
    })
  }

}

const getNoteList = async (ed, ha) => {
  enterDelay = ed
  hiddenAnimation = ha
  const userToken = await getUserToken()
  loadingBar.start()

  const {data: responseData} = await noteBaseRequest.get(
      "/note/list",
      {
        headers: {userToken}
      }
  ).catch(() => {
    loadingBar.error()
    throw message.error('获取笔记列表失败')
  })

  if (responseData.success) {
    loadingBar.finish()
    loading.value = false
    noteList.value = responseData.data
  } else {
    loadingBar.error()
    message.error(responseData.message)
    if (responseData.code === 'L_008') {
      loginInvalid(true)
    }
  }

}
getNoteList(true, true)

const contextMenu = ref({
  show: false,
  id: null,
  title: '',
  top: false,
  x: 0,
  y: 0,
  options: computed(() => {
    return [
      {icon: renderIcon(DriveFileRenameOutlineOutlined), label: '重命名', key: 'rename'},
      {icon: renderIcon(DeleteOutlineOutlined), label: '删除', key: 'delete'},
      {icon: renderIcon(NavigationRound), label: '置顶', key: 'top', show: !contextMenu.value.top},
      {icon: renderIcon(NextPlanRound), label: '取消置顶', key: 'cancel-top', show: contextMenu.value.top},
    ]
  })
})

const showContextMenu = (e, id, top, title) => {
  e.preventDefault()
  contextMenu.value.show = false
  nextTick().then(() => {
    contextMenu.value.show = true
    contextMenu.value.id = id
    contextMenu.value.top = top
    contextMenu.value.title = title
    contextMenu.value.x = e.clientX
    contextMenu.value.y = e.clientY

  })
}
const clickContextMenuOutside = () => {
  contextMenu.value.show = false
}

const selectContextMenu = (key) => {
  contextMenu.value.show = false
  if (key === 'cancel-top') {
    topNote(false)
  } else if (key === 'top') {
    topNote(true)
  } else if (key === 'delete') {
    displayDeleteRemind.value = true
  }
}

const topNote = async (isTop) => {
  const userToken = await getUserToken();
  loadingBar.start()

  const {data: responseData} = await noteBaseRequest.get(
      "/note/top",
      {
        params: {isTop, noteId: contextMenu.value.id},
        headers: {userToken}
      }
  ).catch(() => {
    loadingBar.error()
    throw message.error(isTop ? "置顶失败！" : "取消置顶失败！")
  })

  if (responseData.success) {
    loadingBar.finish()
    message.success(responseData.message)
    getNoteList(false, false)
  } else {
    loadingBar.error();
    message.error(responseData.message)
    if (responseData.code === "L_008") {
      loginInvalid(true)
    }
  }
}

const displayDeleteRemind = ref(false)
const toDeleteNote = async (complete) => {
  displayDeleteRemind.value = false
  const userToken = await getUserToken();
  loadingBar.start()
  const {data: responseData} = await noteBaseRequest.get(
      "/note/delete",
      {
        params: {complete, noteId: contextMenu.value.id, isRecycle: false},
        headers: {userToken}
      }).catch(() => {
    loadingBar.error()
    throw message.error(complete ? '彻底删除笔记请求失败！' : '删除笔记请求失败！')
  })

  if (responseData.success) {
    loadingBar.finish()
    message.success(responseData.message)
    getNoteList(false, true)
  } else {
    message.error(responseData.message)
    if (responseData.code === "L_008") {
      loginInvalid(true)
    }
  }
}

</script>

<template>
  <n-layout has-sider>
    <n-layout-sider bordered :collapsed-width="0" :width="300"
                    show-trigger class="note-list">
      <n-scrollbar>
        <n-card :bordered="false" style="position: sticky; top: 0; z-index: 1;">
          <template #action>
            <n-space align="center" justify="space-between">
              <h3 style="margin: 0">笔记列表</h3>
              <n-button circle type="primary">
                <n-icon :size="24" :component="PlusRound"/>
              </n-button>
            </n-space>
          </template>
        </n-card>

        <n-space v-if="loading" vertical style="margin: 12px">
          <n-card size="small" v-for=" n in 5" :key="n">
            <n-space vertical>
              <n-skeleton :height="26" :width="120"/>
              <n-skeleton text :repeat="2"/>
              <n-skeleton :height="23" :width="200"/>
            </n-space>
          </n-card>
        </n-space>

        <n-list hoverable clickable style="margin: 12px">
          <TransitionGroup @before-enter="beforeEnter" @enter="enterEvent" @before-leave="beforeLeave"
                           @leave="leaveEvent" move-class="move-transition">
            <template v-if="!loading && noteList.length >0">
              <n-list-item v-for="(note,index) in noteList" :data-index="index" :key="note.id"
                           @contextmenu="showContextMenu($event,note.id,!!note.top,note.title)"
                           :class="{'contexting': (contextMenu.id === note.id && contextMenu.show)}">
                <note-card :key="note.id" :desc="note.body" :id="note.id" :time="note.updateTime"
                           :title="note.title" :top="!!note.top"/>
              </n-list-item>
            </template>
          </TransitionGroup>
        </n-list>
        <n-empty v-if="!loading && noteList.length ===0"
                 style="width: max-content; position: absolute;top: 50%;left: 50%; transform: translate(-65px,-58px);"
                 size="huge"
                 description="暂无笔记数据">
          <template #icon>
            <n-icon :component="SubtitlesOffOutlined"/>
          </template>
          <template #extra>
            <n-button dashed>创建笔记</n-button>
          </template>
        </n-empty>
      </n-scrollbar>
    </n-layout-sider>
  </n-layout>
  <DeleteRemind
      :show="displayDeleteRemind"
      @cancel="displayDeleteRemind = false"
      @delete="toDeleteNote"
      :title="contextMenu.title"/>
  <n-dropdown
      placement="bottom-start"
      trigger="manual"
      :options="contextMenu.options"
      :x="contextMenu.x"
      :y="contextMenu.y"
      :show="contextMenu.show"
      @clickoutside="clickContextMenuOutside"
      @select="selectContextMenu"/>
</template>

<style>
.n-layout-sider.n-layout-sider--collapsed.note-list .n-layout-toggle-button {
  right: -15px;
}

.n-layout-sider.note-list .n-layout-toggle-button {
  transition: right 1s;
}

.n-list.n-list-item.move-transition {
  transition: all 0.5s;
}

.contexting {
  box-shadow: 0 0 5px #18A058;
}
</style>