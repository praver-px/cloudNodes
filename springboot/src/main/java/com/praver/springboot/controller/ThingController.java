package com.praver.springboot.controller;


import cn.hutool.core.lang.Validator;
import cn.hutool.json.JSONUtil;
import com.praver.springboot.entity.Thing;
import com.praver.springboot.entity.User;
import com.praver.springboot.exception.ServiceException;
import com.praver.springboot.service.IThingService;
import com.praver.springboot.util.code.EventCode;
import com.praver.springboot.util.response.ResponseData;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/thing")
public class ThingController {

    @Resource
    private IThingService thingService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;


    @GetMapping("/list")
    public ResponseData getUserTingList(@RequestHeader String userToken) {
        if (Validator.isEmpty(userToken))
            return new ResponseData(false, "登录状态有误！", EventCode.PARAM_USER_TOKEN_WRONG);
        String userTokenRedisValue = null;
        try {
            userTokenRedisValue = stringRedisTemplate.opsForValue().get(userToken);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseData(false, "小记服务错误！", EventCode.REDIS_SERVE_ERROR);
        }
        if (Validator.isEmpty(userTokenRedisValue))
            return new ResponseData(false, "登录失效！", EventCode.LOGIN_INVALID);

        User user = JSONUtil.toBean(userTokenRedisValue, User.class);

        try {
            List<Thing> things = thingService.getUserNormalTing(user.getId());
            return new ResponseData(true, "获取成功！", EventCode.SELECT_SUCCESS, things);
        } catch (ServiceException e) {
            e.printStackTrace();
            return new ResponseData(false, e.getMessage(), e.getCode());
        }
    }
}
