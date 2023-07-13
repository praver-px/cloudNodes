import {defineStore} from 'pinia'
import {ref} from "vue";

//弹出登录模态框的全局状态

export const useLoginModalStore = defineStore(
    "login-modal",
    () => {
        const showLoginModal = ref(false)

        const changeLoginModalShowStatus = (show) => {
            showLoginModal.value = show
        }

        return {showLoginModal, changeLoginModalShowStatus}
    })