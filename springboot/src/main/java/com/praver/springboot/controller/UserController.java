package com.praver.springboot.controller;

import cn.hutool.core.lang.Validator;
import cn.hutool.crypto.SecureUtil;
import com.praver.springboot.exception.ServiceException;
import com.praver.springboot.service.IUserService;
import com.praver.springboot.util.code.EventCode;
import com.praver.springboot.util.response.ResponseData;
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

    /**
     * 登录接口 Email 密码
     *
     * @param email
     * @param password
     * @return 响应数据{user，userToken}
     */
    @PostMapping("/login/email/password")
    public ResponseData loginByEmailAndPassword(String email, String password) {
        if (Validator.isEmpty(email)) return new ResponseData(false, "邮箱有误！", EventCode.ACCOUNT_EMAIL_WRONG);
        if (Validator.isEmpty(password)) return new ResponseData(false, "密码有误！", EventCode.ACCOUNT_PASSWORD_WRONG);
        password = SecureUtil.md5(password);
        try {
            Map map = userService.loginByEmailAndPassword(email, password);
            return new ResponseData(true, "登录成功！", EventCode.LOGIN_EMAIL_PASSWORD_SUCCESS, map);
        } catch (ServiceException e) {
            return new ResponseData(false, e.getMessage(), e.getCode());
        }
    }

}
