package com.praver.springboot.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.extra.mail.MailUtil;
import com.praver.springboot.exception.ServiceException;
import com.praver.springboot.exception.ServiceRollbackException;
import com.praver.springboot.service.IMailService;
import com.praver.springboot.service.IUserService;
import com.praver.springboot.util.code.EventCode;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;


@Service
@Transactional(rollbackFor = ServiceRollbackException.class)
public class MailServiceImpl implements IMailService {
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Resource
    private IUserService userService;

    @Override
    public String getEmailRegisterVC(String email) throws ServiceException {
        //判断邮箱是否被注册过
        userService.getEmailAccountIsExist(email);
        //发送验证码给邮箱
        int time = 15;
        String code = RandomUtil.randomString(6);//生成6位验证码
        String content = "<p>尊敬的" + email + ":</p>" +
                "<p>你正在申请账号服务，请勿泄露该验证码！</p>" +
                "<p>验证码为：<b style=font-size:20px; color:blue;>" + code + "</b></p>" +
                "<p>该验证码的有效时间为" + time + "分钟!</p>" +
                "<p>" + DateUtil.now() + "</p>";
        try {
            MailUtil.send(
                    email,
                    "邮箱账号注册验证码",
                    content,
                    true
            );
        } catch (Exception e) {
            throw new ServiceException("验证码发送失败！", EventCode.EMAIL_SAND_ERROR);
        }

        //将验证码保存至redis中 15 分钟
        String ERAvcTokenKey = "ERAvcToken:" + email + ":" + IdUtil.randomUUID();
        try {
            stringRedisTemplate.opsForValue().set(ERAvcTokenKey, code, time, TimeUnit.MINUTES);
        } catch (Exception e) {
            throw new ServiceException("验证码存储失败！", EventCode.EMAIL_SEND_VC_SAVE_REDIS_ERROR);
        }
        return ERAvcTokenKey;
    }
}
