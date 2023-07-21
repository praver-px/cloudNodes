<script setup>
import {
  ArrowCircleDownRound,
  ArrowCircleUpRound,
  ArrowUpwardRound,
  DeleteOutlineRound,
  EditNoteRound
} from "@vicons/material";
import {useThemeStore} from "@/stores/themeStore";
import {computed, ref} from "vue";
import {getUserToken, loginInvalid} from "@/utils/userLoginUtil";
import {noteBaseRequest} from "@/request/noteRequest";
import {useLoadingBar, useMessage} from 'naive-ui'

const message = useMessage()
const loadingBar = useLoadingBar()

//主题对象
const themeStore = useThemeStore()
const {isDarkTheme} = themeStore

const propsData = defineProps({
  id: {type: Number, required: true},// 编号
  finished: {type: Boolean, default: false},// 是否已完成，默认未完成
  title: {type: String, required: true},// 小记标题
  top: {type: Boolean, default: false}, //是否置顶
  tags: {type: Array, required: ['暂无标签']},// 小记标签
  time: {type: String, required: true},// 更新时间
})

//自定义事件
const emits = defineEmits(['changeStatus', 'delete', 'edit']);

const thingFinishShadowColor = computed(() => {
  return "#363433"
})
//置顶按钮是否被禁用
const topBtnDisabled = ref(false)
const thingCardTopIconText = computed(() => {
  if (propsData.top) {
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
})
const topThing = async (isTop) => {
  const userToken = await getUserToken();
  loadingBar.start()
  topBtnDisabled.value = true
  const {data: responseData} = await noteBaseRequest.get(
      "/thing/top",
      {
        params: {isTop, thingId: propsData.id},
        headers: {userToken}
      }
  ).catch(() => {
    loadingBar.error()
    topBtnDisabled.value = false
    throw message.error(isTop ? "置顶失败！" : "取消置顶失败！")
  })
  topBtnDisabled.value = false
  if (responseData.success) {
    loadingBar.finish()
    message.success(responseData.message)
    emits('changeStatus')
  } else {
    loadingBar.error();
    message.error(responseData.message)
    if (responseData.code === "L_008") {
      loginInvalid(true)
    }
  }
}
</script>

<template>
  <n-card
      :class="{'thing-card-finished': finished}"
      size="small"
      embedded
      :bordered="isDarkTheme"
      :segmented="{'content':'soft'}"
      :title="title"
      style="min-width: 250px;max-width: max-content">
    <template #header-extra>
      <n-popover>
        <template #trigger>
          <n-button type="error" text style="margin-left: 16px" @click="emits('delete',{id,title})">
            <n-icon :size="18" :component="DeleteOutlineRound"/>
          </n-button>
        </template>
        删除
      </n-popover>


      <n-popover>
        <template #trigger>
          <!-- !! 数值类型 -> boolean 类型 -->
          <n-button :disabled="topBtnDisabled" text style="margin-left: 6px" @click="topThing(!top)">
            <n-icon :size="18" :component="thingCardTopIconText.icon"/>
          </n-button>
        </template>
        {{ thingCardTopIconText.text }}
      </n-popover>

      <n-popover>
        <template #trigger>
          <n-button text style="margin-left: 6px" @click="emits('edit')">
            <n-icon :size="18" :component="EditNoteRound"/>
          </n-button>
        </template>
        编辑
      </n-popover>

    </template>
    <template #default>
      <n-space>
        <n-tag v-for="tag in tags" size="small" :bordered="false">{{ tag }}</n-tag>

      </n-space>
    </template>
    <template #footer>
      <n-space align="center" size="small">
        <n-tag
            :color="{color:'rgba(0 0 0 0)',textColor: '#18A058'}"
            v-if="top"
            round
            size="small"
            :bordered="false">
          <n-icon size="18" :component="ArrowUpwardRound"/>
        </n-tag>
        <n-divider v-if="top" vertical/>
        <n-text depth="3">{{ time }}</n-text>
      </n-space>
    </template>
  </n-card>
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