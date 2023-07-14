<script setup>
import {computed, ref} from "vue";
import Login from "@/components/login/Login.vue";
import Register from "@/components/login/Register.vue";
import RegisterSuccess from "@/components/login/RegisterSuccess.vue";
import {useLoginModalStore} from "@/stores/loginModalStore";
import {storeToRefs} from "pinia";

const loginModalStore = useLoginModalStore()
const {showLoginModal} = storeToRefs(loginModalStore)

//登录模态框显示内容（1:登录 2:注册 3：注册成功）
const loginModalStep = ref(1)
//判断显示那个卡片
const showLoginModalCard = computed(() => {
  switch (loginModalStep.value) {
    case 1:
      return Login;
    case 2:
      return Register;
    default:
      return RegisterSuccess;
  }
})
//切换卡片
const changeModalStep = step => {
  loginModalStep.value = step
}
</script>

<template>
  <n-modal v-model:show="showLoginModal" :mask-closable="false" transform-origin="center">
    <div style="width: 400px;">
      <Transition name="bounce" mode="out-in">
        <component :is="showLoginModalCard" @changeStep="changeModalStep"/>
      </Transition>
    </div>
  </n-modal>
</template>
<style>
.bounce-enter-active {
  animation: bounce-in 0.5s;
}

.bounce-leave-active {
  animation: bounce-in 0.5s reverse;
}

@keyframes bounce-in {
  0% {
    transform: scale(0);
  }
  50% {
    transform: scale(1.25);
  }
  100% {
    transform: scale(1);
  }
}
</style>