import {defineStore} from 'pinia'
import {computed, ref} from "vue";
import {darkTheme} from "naive-ui";
import {DarkModeRound, LightbulbOutlined} from '@vicons/material'

//操作主题的全局状态

export const useUserStore = defineStore(
    "user",
    () => {
        const id = ref(null)
        const nickName = ref('')
        const headPic = ref('')
        const level = ref(0)
        const email = ref('')
        const time = ref('')

        const setUserInfo = (u_id, u_nickname, u_headPic, u_level, u_email, u_time) => {
            id.value = u_id
            nickName.value = u_nickname
            headPic.value = u_headPic
            level.value = u_level
            email.value = u_email
            time.value = u_time
        }

        return {id, email, nickName, headPic, level, time, setUserInfo}
    },
    {
        persist: {
            storage: localStorage,//本地存储
        }
    })
