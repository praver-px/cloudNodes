<script setup>
import {DeleteOutlineFilled} from '@vicons/material'

defineProps({
  show: {type: Boolean, default: false},
  describe: {type: String, required: true},
  deleteBtn: {type: Boolean, default: true},
  completeDeleteBtn: {type: Boolean, default: true},
})

//自定义事件 彻底删除 删除 取消
const emit = defineEmits(['delete', 'cancel']);

</script>

<template>
  <n-modal
      :show="show"
      preset="dialog"
      type="error"
      :closable="false"
      transform-origin="center"
      title="删除提醒">
    <template #icon>
      <n-icon style="position: relative;top: -2px" :component="DeleteOutlineFilled"/>
    </template>
    <template #default>{{ describe }}</template>
    <template #action>
      <n-button v-if="completeDeleteBtn" size="small" tertiary type="error" @click="emit('delete',true)">
        彻底删除
      </n-button>
      <n-button v-if="deleteBtn" size="small" secondary type="error" @click="emit('delete',false)">删除</n-button>
      <n-button size="small" tertiary @click="emit('cancel')">取消</n-button>
    </template>
  </n-modal>
</template>
