<script setup>
import {ref} from "vue";
import {EmailOutlined, LockOutlined} from "@vicons/material";
import {noteBaseRequest} from "@/request/noteRequest";
import {useMessage, useLoadingBar} from 'naive-ui'
import {useLoginModalStore} from "@/stores/loginModalStore";
import {useUserStore} from "@/stores/userStore";

//用户共享的资源对象
const userStore = useUserStore();
const {setUserInfo} = userStore;

//登录模态框共享对象
const loginModalStore = useLoginModalStore()
//改变登录模态框的显示状态
const {changeLoginModalShowStatus} = loginModalStore;
//自定义事件
const emits = defineEmits(['changeStep']);

//消息对象
const message = useMessage();
//加载条对象
const loadingBar = useLoadingBar();

const loginFormValue = ref({
  email: '',
  password: '',
  trim: true,
})
const loginFormRef = ref(null)
//禁用登录按钮
const loginBtnDisable = ref(false)
//登录
const login = (e) => {
  e.preventDefault();
  loginFormRef.value?.validate(async (errors) => {
    if (!errors) {
      loadingBar.start() // 加载条开始
      loginBtnDisable.value = true
      //发送登录请求
      const {data: responseData} = await noteBaseRequest.post("/user/login/email/password", {
        email: loginFormValue.value.email,
        password: loginFormValue.value.password
      }).catch(() => {
        loadingBar.error();
        //发送请求失败
        message.error("发送请求失败");
        setTimeout(() => {
          loginBtnDisable.value = false
        }, 1500)
        throw "发送请求失败！";
      })
      //等到服务器数据除了
      //console.log(responseData)
      if (responseData.success) {
        loadingBar.finish();
        message.success(responseData.message);
        changeLoginModalShowStatus(false)
        localStorage.setItem("userToken", responseData.data.userToken)
        const user = responseData.data.user;
        setUserInfo(user.id, user.nickname, user.headPic, user.level, user.email, user.time)
      } else {
        loadingBar.error();
        message.error(responseData.message);
      }
      setTimeout(() => {
        loginBtnDisable.value = false
      }, 1500)
    }
  })
}
const loginFormRules = {
  email: [
    {
      message: '请输入邮箱',
      trigger: ["input", "blur"]
    },
    {
      message: '请输入正确的邮箱',
      trigger: ["input", "blur"],
      validator: (rule, value) => {
        return /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(.[a-zA-Z0-9_-])+/.test(value)

      }
    }
  ],
  password: [{
    required: true,
    message: '请输入密码',
    trigger: ["input", "blur"]
  }],
  trim: [{
    message: '接受《条款与协议》才能登录',
    trigger: "change",
    validator: (rule, value) => {
      console.log(value)
      return value
    }
  }],
}
</script>

<template>
  <n-card>
    <n-space justify="space-between" align="center">
      <h2>登录</h2>
      <n-text depth="3">
        没有账号？
        <n-button text type="info" @click="emits('changeStep',2)">前往注册</n-button>
      </n-text>
    </n-space>
    <n-form ref="loginFormRef" :model="loginFormValue" :rules="loginFormRules">
      <n-form-item path="email" label="邮箱" first>
        <n-input clearable placeholder="请输入邮箱地址" v-model:value="loginFormValue.email">
          <template #prefix>
            <n-icon :component="EmailOutlined"/>
          </template>
        </n-input>
      </n-form-item>
      <n-form-item path="password" label="密码">
        <n-input type="password" show-password-on="click" clearable placeholder="请输入密码"
                 v-model:value="loginFormValue.password">
          <template #prefix>
            <n-icon :component="LockOutlined"/>
          </template>
        </n-input>
      </n-form-item>
      <n-form-item :show-label="false" path="trim">
        <n-checkbox v-model:checkd="loginFormValue.trim">同意本公司的</n-checkbox>
        <n-button text type="info">《条款与协议》</n-button>
      </n-form-item>
      <n-form-item :show-label="false">
        <n-button block type="info" :disabled="loginBtnDisable" @click="login">登录</n-button>
      </n-form-item>
    </n-form>
    <n-space justify="center">
      <n-button text color="#909399">
        忘记密码？
      </n-button>
    </n-space>
  </n-card>
</template>
