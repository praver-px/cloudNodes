<script setup>
import {NavigationRound} from "@vicons/material";
import {htmlToText} from "html-to-text";

defineProps({
  id: {type: Number, required: true},
  title: {type: String, required: true},
  desc: {type: String, required: true},
  top: {type: Boolean, default: false},
  time: {type: String, required: true},
})
</script>

<template>
  <n-thing>
    <template #header>
      <n-ellipsis line-clamp="1">{{ title }}</n-ellipsis>
    </template>
    <template #description>
      <n-ellipsis line-clamp="2" :tooltip="false">
        <n-text depth="3">{{ htmlToText(desc) }}</n-text>
      </n-ellipsis>
    </template>
    <template #default>
      <n-space align="center" size="small">
        <n-tag :color="{color:'rgba(0 0 0 0)',textColor: '#18A058'}" v-if="top" round size="small" :bordered="false">
          <n-icon size="18" :component="NavigationRound"/>
        </n-tag>
        <n-divider v-if="top" vertical/>
        <n-text depth="3">{{ time }}</n-text>
      </n-space>
    </template>
  </n-thing>
</template>

<style scoped>

</style>