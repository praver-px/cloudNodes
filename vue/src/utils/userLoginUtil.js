import {useLoginModalStore} from "@/stores/loginModalStore";

/**
 * 获取本地登录用户的token信息，没有则弹出登录窗口
 * @returns {Promise<void>}
 */
export const getUserToken = async () => {
    const token = localStorage.getItem("userToken");
    if (token === null) {
        // 弹出登录窗口
        const {changeLoginModalShowStatus} = useLoginModalStore()
        await changeLoginModalShowStatus(true)
        throw "未登录"
    } else {
        return token
    }
}