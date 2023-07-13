<script setup>
import {ref} from "vue";
import {EmailOutlined} from "@vicons/material";

const emits = defineEmits(['changeStep']);

const registerFormValue = ref({
  email: '',
  vc: '',
  trim: true,
})
const registerFormRef = ref(null)
const register = (e) => {
  e.preventDefault();
  registerFormRef.value?.validate((errors) => {
    if (!errors) {
      alert("注册成功")
    }
  })
}
const registerFormRules = {
  email: [
    {
      message: '请输入邮箱',
      trigger: ["input", "blur"]
    },
    {
      key: "email",
      message: '请输入正确的邮箱',
      trigger: ["input", "blur"],
      validator: (rule, value) => {
        return /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(.[a-zA-Z0-9_-])+/.test(value)

      }
    }
  ],
  vc: [{
    required: true,
    message: '请输入验证码',
    trigger: ["input", "blur"]
  }],
  trim: [{
    message: '接受《条款与协议》才能登录',
    trigger: "change",
    validator: (rule, value) => {
      return value
    }
  }],
}

//获取验证码
//按钮状态
const btnCountDown = ref({
  text: '获取验证码',// 显示文本
  time: 60,
  disabled: false,
  clock: null
})

//按钮倒计时
const buttonCountDown = () => {
  btnCountDown.value.clock = setInterval(() => {
    if (btnCountDown.value.time === 1) {
      resetButtonCountDownStatus()
    } else {
      btnCountDown.value.disabled = true
      btnCountDown.value.time--
      btnCountDown.value.text = btnCountDown.value.time + "S后重新获取"
    }
  }, 1000)
}
//重置按钮
const resetButtonCountDownStatus = () => {
  clearInterval(btnCountDown.value.clock)
  btnCountDown.value.text = "获取验证码"
  btnCountDown.value.time = 60
  btnCountDown.value.disabled = false
}

const getEmailVC = () => {
  registerFormRef.value?.validate(
      (errors) => {
        if (!errors) {
          buttonCountDown()
        }
      },
      (rule) => {
        return rule?.key === 'email'
      }
  )
}
</script>

<template>
  <n-card>
    <n-space justify="space-between" align="center">
      <h2>注册</h2>
      <n-text depth="3">
        已有账号？
        <n-button text type="info" @click="emits('changeStep',1)">前往登录</n-button>
      </n-text>
    </n-space>
    <n-form ref="registerFormRef" :model="registerFormValue" :rules="registerFormRules">
      <n-form-item path="email" label="邮箱" first>
        <n-input clearable placeholder="请输入邮箱地址" v-model:value="registerFormValue.email">
          <template #prefix>
            <n-icon :component="EmailOutlined"/>
          </template>
        </n-input>
      </n-form-item>

      <n-grid :cols="2" :x-gap="24">
        <n-form-item-gi path="vc" label="验证码">
          <n-input placeholder="请输入验证码" v-model:value="registerFormValue.vc"/>
        </n-form-item-gi>
        <n-form-item-gi path="vc">
          <n-button block secondary type="primary" :disabled="btnCountDown.disabled" @click="getEmailVC">
            {{ btnCountDown.text }}
          </n-button>
        </n-form-item-gi>
      </n-grid>

      <n-form-item :show-label="false" path="trim">
        <n-checkbox v-model:checkd="registerFormValue.trim">同意本公司的</n-checkbox>
        <n-button text type="info">《条款与协议》</n-button>
      </n-form-item>
      <n-form-item :show-label="false">
        <n-button block type="info" @click="register">注册</n-button>
      </n-form-item>
    </n-form>
  </n-card>
</template>
