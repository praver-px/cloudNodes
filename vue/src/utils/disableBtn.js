/**
 *
 * @param btnDisabled 按钮 disable 属性绑定的数据源
 * @param isDisabled 是否是禁用按钮
 * @param delay 取消禁用按钮是否需要延迟
 * @param time 延迟的时间（s）
 */
export const disableBtn = (btnDisabled, isDisabled, isDelay = false, time = 1000) => {
    if (isDisabled) {
        btnDisabled.value = isDisabled //禁用按钮
    } else {
        if (isDelay) {
            //需要有延迟取消禁用按钮
            setTimeout(() => {
                btnDisabled.value = isDisabled//取消禁用按钮
            }, time * 1000)
        } else {
            btnDisabled.value = isDisabled //取消禁用按钮
        }
    }
}