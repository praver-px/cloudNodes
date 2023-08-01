<script setup>
import {computed, ref, h} from "vue";

import {AddRound, MinusRound} from '@vicons/material'

import {NSpace, NText, useNotification, useLoadingBar, useMessage} from 'naive-ui'
import {getUserToken, loginInvalid} from "@/utils/userLoginUtil";
import {noteBaseRequest} from "@/request/noteRequest";


const notification = useNotification()
const loadingBar = useLoadingBar()
const message = useMessage()
const emits = defineEmits(['save'])
const onCreateTuDoThing = () => ({
  checked: false,
  thing: '',
})

const formValue = ref({
  id: null,
  title: '',
  top: false,
  tags: [],
  content: [],
  finished: computed(() => {
    const content = formValue.value.content
    if (content.length === 0) return false
    return content.every(item => item.checked)
  })
})
const formRef = ref(null)

const formRules = {
  title: {
    required: true,
    message: '请设置标题！'
  },
  tags: {
    required: true,
    message: '请设置标签！'
  },
  content: {
    required: true,
    message: '请设置内容！'
  }
}
const saveEditThing = () => {
  formRef.value?.validate(errors => {
    if (!errors) {
      save(formValue.value.id === null)
    } else {
      const errorsMessage = [].concat(...errors)
      notification.error({
        title: '编辑小记保存提醒！',
        content: () => h(
            NSpace,
            {vertical: true},
            {
              default: () => errorsMessage.map(
                  (item, index) => h(
                      NText,
                      {type: 'error'},
                      {
                        default: () => (index + 1) + '：' + item.message
                      }
                  )
              )
            }
        )
      })
    }
  })
}
//小记保存
const save = async (isNewCreate) => {
  const userToken = await getUserToken();
  loadingBar.start()
  const {data: responseData} = await noteBaseRequest(
      {
        method: isNewCreate ? 'PUT' : 'POST',
        url: isNewCreate ? '/thing/create' : '/thing/update',
        data: {
          thingId: formValue.value.id,
          title: formValue.value.title,
          top: formValue.value.top,
          tags: formValue.value.tags.join(),
          content: JSON.stringify(formValue.value.content),
          finished: formValue.value.finished
        },
        headers: {userToken}

      }).catch(() => {
    loadingBar.error()
    throw message.error('保存失败！')
  })

  if (responseData.success) {
    loadingBar.finish()
    message.success(responseData.message)
    show.value = false
    emits('save', false, false)
  } else {
    loadingBar.error()
    message.error(responseData.message)
    if (responseData.code === 'L_008') {
      loginInvalid(true)
    }
  }
}

const loading = ref(true)
const show = ref(false)
const showEditModal = id => {
  show.value = true
  loading.value = true
  if (id === null) {
    loading.value = false
  } else {
    formValue.value.id = id
    getEditThing(id)
  }
}
const resetEditThing = () => {
  formValue.value.id = null
  formValue.value.title = ''
  formValue.value.top = false
  formValue.value.tags = []
  formValue.value.content = []
}

defineExpose({showEditModal})


const getEditThing = async (thingId) => {
  const userToken = await getUserToken()
  loadingBar.start()
  const {data: responseData} = await noteBaseRequest.get(
      "/thing/getOne",
      {
        params: {thingId},
        headers: {userToken}
      }
  ).catch(() => {
    loadingBar.error()
    throw message.error('获取小记信息失败！')
  })

  if (responseData.success) {
    loadingBar.finish()
    const thingData = responseData.data
    formValue.value.title = thingData.title
    formValue.value.top = !!thingData.top
    formValue.value.tags = thingData.tags.split(',')
    formValue.value.content = JSON.parse(thingData.content)
    loading.value = false
  } else {
    loadingBar.error()
    message.error(responseData.message)
    if (responseData.code === 'L_008') {
      loginInvalid(true)
    }
  }
}

</script>
<template>
  <n-modal v-model:show="show" :auto-focus="false" :on-after-leave="resetEditThing">
    <div>
      <!--      骨架屏-->
      <n-card size="small" :bordered="false" style="width: 460px" v-show="loading">
        <template #default>
          <div style="padding: 0 14px">
            <n-skeleton :height="40" :sharp="false"/>
          </div>

          <div style="padding: 10px 14px 0">
            <n-space align="center">
              <n-skeleton text :width="42" :height="14"/>
              <n-skeleton text :width="40" :height="22" :sharp="false"/>
              <n-space align="center">
                <n-skeleton text :width="42" :height="14"/>
                <n-skeleton text :width="38" :height="28" :sharp="false"/>
              </n-space>
            </n-space>

            <n-divider style="margin-top: 14px"/>

            <n-skeleton :height="34" :sharp="false"/>
          </div>
        </template>

        <template #action>
          <n-grid cols="2" :x-gap="12">
            <n-gi>
              <n-skeleton :height="34" :sharp="false"/>
            </n-gi>
            <n-gi>
              <n-skeleton :height="34" :sharp="false"/>
            </n-gi>
          </n-grid>
        </template>
      </n-card>
      <!--      编辑卡片-->
      <n-card size="small" :bordered="false" style="width: 460px" v-show="!loading"
              :class="{'thing-card-finished':formValue.finished}">
        <template #default>
          <n-form ref="formRef" :model="formValue" :rules="formRules">
            <n-form-item :show-label="false" :show-feedback="false" path="title">
              <n-input size="large" placeholder="请输入标题" v-model:value="formValue.title"
                       style="--n-border:none;background-color: transparent"/>
            </n-form-item>


            <div style="padding: 10px 14px 0">

              <n-space align="center">
                <n-text depth="3">置顶：</n-text>
                <n-switch :round="false" v-model:value="formValue.top"/>
                <n-space align="center">
                  <n-form-item :show-label="false" :show-feedback="false" path="tags">
                    <n-text depth="3">标签：</n-text>
                    <n-dynamic-tags :max="5" v-model:value="formValue.tags" :color="{borderColor:'transparent'}"/>
                  </n-form-item>
                </n-space>
              </n-space>

              <n-divider style="margin-top: 14px"/>
              <n-form-item :show-label="false" :show-feedback="false" path="content">
                <n-dynamic-input :on-create="onCreateTuDoThing" v-model:value="formValue.content">
                  <template #create-button-default>添加一个代办事件</template>
                  <template #default="{value}">
                    <div style="display: flex;align-items: center; width: 100%">
                      <n-checkbox v-model:checked="value.checked"/>
                      <n-input v-model:value="value.thing" placeholder="请输入..."
                               style="margin-left: 12px;--n-border: none"/>
                    </div>
                  </template>
                  <template #action="{index,create,remove,move}">
                    <div style="display: flex;align-items: center;margin-left: 12px">
                      <n-button circle tertiary type="tertiary" @click="()=>create(index)" style="margin-right: 6px">
                        <n-icon :component="AddRound"/>
                      </n-button>
                      <n-button circle tertiary type="tertiary" @click="()=>remove(index)">
                        <n-icon :component="MinusRound"/>
                      </n-button>
                    </div>
                  </template>
                </n-dynamic-input>
              </n-form-item>
            </div>
          </n-form>
        </template>

        <template #action>
          <n-grid cols="2" :x-gap="12">
            <n-gi>
              <n-button block tertiary @click="show = false">取消</n-button>
            </n-gi>
            <n-gi>
              <n-button block ghost type="primary" @click="saveEditThing">保存</n-button>
            </n-gi>
          </n-grid>
        </template>
      </n-card>
    </div>
  </n-modal>
</template>
<style scoped>
.n-card.thing-card-finished {
  background-image: url("@/assets/finish.png");
  background-repeat: no-repeat;
  background-position: 360px 0;
  animation: finished 0.25s linear forwards;
}

@keyframes finished {
  from {
    background-size: 130px 130px;
  }
  to {
    background-size: 100px 100px;
  }
}
</style>