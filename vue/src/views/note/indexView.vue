<script setup>
import {ref} from "vue";
import {PlusRound, SubtitlesOffOutlined} from '@vicons/material'
import NoteCard from "@/components/note/NoteCard.vue";
import {getUserToken, loginInvalid} from "@/utils/userLoginUtil";
import {useLoadingBar, useMessage} from 'naive-ui'
import {noteBaseRequest} from "@/request/noteRequest";

const message = useMessage()
const loadingBar = useLoadingBar()

const loading = ref(true)

const noteList = ref([])
const getNoteList = async () => {
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
    // noteList.value = responseData.data
  } else {
    loadingBar.error()
    message.error(responseData.message)
    if (responseData.code === 'L_008') {
      loginInvalid(true)
    }
  }

}
getNoteList()
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

        <n-list v-else-if="noteList.length >0" hoverable clickable style="margin: 12px">
          <n-list-item v-for="note in noteList">
            <note-card :key="note.id" :desc="note.body" :id="note.id" :time="note.updateTime"
                       :title="note.title" :top="!!note.top"/>
          </n-list-item>
        </n-list>
        <n-empty v-else
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
</template>

<style>
.n-layout-sider.n-layout-sider--collapsed.note-list .n-layout-toggle-button {
  right: -15px;
}

.n-layout-sider.note-list .n-layout-toggle-button {
  transition: right 1s;
}
</style>