import {defineStore} from 'pinia'
import {computed, ref, watch} from "vue";


//操作主题的全局状态

export const useUserStore = defineStore(
    "user",
    () => {
        const token = ref(null)
        const id = ref(null)
        const nickname = ref(null)
        const headPic = ref(null)
        const level = ref(null)
        const email = ref(null)
        const time = ref(null)

        const setUserInfo = (u_token, u_id, u_nickname, u_headPic, u_level, u_email, u_time) => {
            token.value = u_token
            id.value = u_id
            nickname.value = u_nickname
            headPic.value = u_headPic
            level.value = u_level
            email.value = u_email
            time.value = u_time

        }
        const head_image = computed(() => {
            if (headPic.value === null || headPic.value === undefined) {
                return 'https://cdnimg103.lizhi.fm/user/2017/02/04/2583325032200238082_160x160.jpg'
            } else {
                return headPic.value
            }
        })
        const nickName = computed(() => {
            if (nickname.value === null || nickname.value === undefined) {
                return '暂未设置昵称'
            } else {
                return nickname.value
            }
        })
        const levelInfo = computed(() => {
            if (level.value === 0) {
                return "会员"
            } else {
                return "超级会员"
            }
        })
        const resetUserInfo = () => {
            token.value = null
        }

        watch(
            () => token.value,
            newData => {
                if (newData === null) {
                    id.value = null
                    nickname.value = null
                    headPic.value = null
                    level.value = null
                    email.value = null
                    time.value = null
                }
            }
        )

        return {token, id, email, nickname, level, time, head_image, nickName, levelInfo, resetUserInfo, setUserInfo}
    },
    {
        persist: {
            storage: localStorage,//本地存储
        }
    })
