package com.praver.springboot.controller;

import cn.hutool.core.lang.Validator;
import cn.hutool.core.text.CharSequenceUtil;
import cn.hutool.crypto.SecureUtil;
import com.praver.springboot.exception.ServiceException;
import com.praver.springboot.service.IUserService;
import com.praver.springboot.util.code.EventCode;
import com.praver.springboot.util.response.ResponseData;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;


@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private IUserService userService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     * 登录接口 Email 密码
     *
     * @param email
     * @param password
     * @return 响应数据{user，userToken}
     */
    @PostMapping("/login/email/password")
    public ResponseData loginByEmailAndPassword(String email, String password) {
        if (Validator.isEmpty(email))
            return new ResponseData(false, "邮箱有误！", EventCode.ACCOUNT_EMAIL_WRONG);
        if (Validator.isEmpty(password))
            return new ResponseData(false, "密码有误！", EventCode.ACCOUNT_PASSWORD_WRONG);
        password = SecureUtil.md5(password);
        try {
            Map map = userService.loginByEmailAndPassword(email, password);
            return new ResponseData(true, "登录成功！", EventCode.LOGIN_EMAIL_PASSWORD_SUCCESS, map);
        } catch (ServiceException e) {
            return new ResponseData(false, e.getMessage(), e.getCode());
        }
    }

    /**
     * 邮箱验证码注册
     *
     * @param email 邮箱地址
     * @param vc    输入的验证码
     * @param vcKey 验证码查询关键词
     * @return 响应数据
     */
    @PostMapping("/register/email")
    public ResponseData registerAccountEmail(String email, String vc, String vcKey) {
        if (Validator.isEmpty(email))
            return new ResponseData(false, "邮箱有误！", EventCode.ACCOUNT_EMAIL_WRONG);
        if (Validator.isEmpty(vc))
            return new ResponseData(false, "验证码有误！", EventCode.PARAM_VC_WRONG);
        if (Validator.isEmpty(vcKey))
            return new ResponseData(false, "验证码关键词有误！", EventCode.PARAM_VC_KEY_WRONG);
        //判断验证码对应的邮箱是否对应
        String vc_email = vcKey.split(":")[1];
        if (!CharSequenceUtil.equals(email, vc_email)) {
            return new ResponseData(false, "注册邮箱与验证码不匹配！", EventCode.PARAM_VC_KEY_EMAIL_WRONG);
        }
        //获取实际的验证码
        String vcTokenValue = stringRedisTemplate.opsForValue().get(vcKey);
        if (Validator.isEmpty(vcTokenValue))
            return new ResponseData(false, "验证码失效！", EventCode.VC_INVALID);
        //判断验证码是否准确
        if (!CharSequenceUtil.equals(vcTokenValue, vc)) {
            return new ResponseData(false, "验证码有误！", EventCode.VC_MATCH_ERROR);
        }
        try {
            userService.registerAccountByEmail(email);
            return new ResponseData(true, "注册成功！", EventCode.ACCOUNT_EMAIL_REGISTER_SUCCESS);
        } catch (ServiceException e) {
            e.printStackTrace();
            return new ResponseData(false, e.getMessage(), e.getCode());
        }
    }
}
