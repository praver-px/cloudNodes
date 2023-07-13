package com.praver.springboot.controller;


import cn.hutool.core.lang.Validator;
import com.praver.springboot.exception.ServiceException;
import com.praver.springboot.service.IMailService;
import com.praver.springboot.util.code.EventCode;
import com.praver.springboot.util.response.ResponseData;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/email")
public class MailController {

    @Resource
    private IMailService mailService;

    /**
     * 获取邮箱验证码接口
     *
     * @param email 邮箱号
     * @return 验证码的关键词
     */
    @GetMapping("/register/vc")
    public ResponseData getEmailRegisterAccountVC(String email) {
        if (Validator.isEmpty(email))
            return new ResponseData(false, "邮箱有误！", EventCode.ACCOUNT_EMAIL_WRONG);
        try {
            String registerVC = mailService.getEmailRegisterVC(email);
            return new ResponseData(true, "发送成功", EventCode.EMAIL_SAND_SUCCESS, registerVC);
        } catch (ServiceException e) {
            return new ResponseData(false, e.getMessage(), e.getCode());
        }

    }


}
