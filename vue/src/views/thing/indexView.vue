<script setup>
import {
  DeleteOutlineRound, ArrowCircleUpRound,
  ArrowCircleDownRound, EditNoteRound
} from '@vicons/material'
import {computed} from 'vue'
import {useThemeStore} from "@/stores/themeStore";
import {storeToRefs} from "pinia";

const themeStore = useThemeStore()
const {isDarkTheme} = storeToRefs(themeStore)

const thingFinishShadowColor = computed(() => {
  return isDarkTheme.value ? "#363433" : "#bdaead"
})
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

    <n-card size="small" :bordered="false" style="margin-top: 20px; ">
      <n-space>
        <n-card
            class="thing-card-finished"
            size="small"
            embedded
            :bordered="isDarkTheme"
            :segmented="{'content':'soft'}"
            title=" card list">
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
                <n-button text style="margin-left: 6px">
                  <n-icon :size="18" :component="ArrowCircleUpRound"/>
                </n-button>
              </template>
              置顶
            </n-popover>
            <n-popover>
              <template #trigger>
                <n-button text style="margin-left: 6px">
                  <n-icon :size="18" :component="ArrowCircleDownRound"/>
                </n-button>
              </template>
              取消置顶
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
              <n-tag size="small" :bordered="false">tag 1</n-tag>
              <n-tag size="small" :bordered="false">tag 2</n-tag>
            </n-space>
          </template>
          <template #footer>
            <n-space align="center" size="small">
              <n-tag size="small" :bordered="false" type="success">Top</n-tag>
              <n-divider vertical/>
              <n-text depth="3">dateTme</n-text>
            </n-space>
          </template>
        </n-card>
      </n-space>
    </n-card>
  </n-layout>
</template>
<style scoped>
.n-card.thing-card-finished:after {
  position: absolute;
  content: '';
  width: 100px;
  height: 100px;
//border: 1px solid white;  bottom: 50%; left: 40px; transform: translateY(50px); background-image: url("@/assets/finish.png");
  background-size: 100px 100px;
  background-repeat: no-repeat;
  filter: drop-shadow(5px 3px 2px v-bind(thingFinishShadowColor));
}
</style>