import {defineStore} from 'pinia'
import {computed, ref} from "vue";
import {darkTheme} from "naive-ui";
import {DarkModeRound, LightbulbOutlined} from '@vicons/material'

//操作主题的全局状态

export const useThemeStore = defineStore(
    "theme",
    () => {
        const isDarkTheme = ref(false)
        const theme = computed(() => {
            if (isDarkTheme.value) {
                //暗系主题
                return {
                    name: darkTheme,
                    icon: LightbulbOutlined
                }
            } else {
                //亮系主题
                return {
                    name: null,
                    icon: DarkModeRound

                }
            }
        })

        const changeTheme = dark => {
            isDarkTheme.value = dark
        }

        return {isDarkTheme, theme, changeTheme}
    },
    {
        persist: {
            storage: localStorage,//本地存储
            paths: ["isDarkTheme"] //将 isDarkTheme 持久化
        }
    })
