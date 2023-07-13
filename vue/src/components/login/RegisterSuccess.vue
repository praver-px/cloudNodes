<script setup>
import {CheckCircleRound,} from '@vicons/material'

const emits = defineEmits(['changeStep']);

</script>

<template>
  <n-card align="center">
    <n-icon size="120" :component="CheckCircleRound" color="#67C23A"/>
    <h4 style="margin: 0 0 5px 0">注册成功</h4>
    <n-text>
      初始密码已发送至注册邮箱
      <n-button text typ e="info" @click="emits('changeStep',2)"> 点击此处前往登录</n-button>
    </n-text>
  </n-card>
</template>
