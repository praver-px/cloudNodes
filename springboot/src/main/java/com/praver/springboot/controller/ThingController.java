package com.praver.springboot.controller;


import cn.hutool.core.lang.Validator;
import com.praver.springboot.entity.Thing;
import com.praver.springboot.entity.User;
import com.praver.springboot.exception.ServiceException;
import com.praver.springboot.service.IThingService;
import com.praver.springboot.util.code.EventCode;
import com.praver.springboot.util.response.ResponseData;
import com.praver.springboot.util.validate.TokenValidateUtil;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/thing")
public class ThingController {

    @Resource
    private IThingService thingService;
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @GetMapping("/delete")
    public ResponseData deleteTing(boolean complete, int thingId, boolean isRecycleBin, @RequestHeader String userToken) {
        try {
            User user = TokenValidateUtil.validateUserToken(userToken, stringRedisTemplate);
            if (Validator.isEmpty(complete))
                return new ResponseData(false, "置顶状态参数为空！", EventCode.PARAM_DELETE_COMPLETE_WRONG);
            if (Validator.isEmpty(isRecycleBin))
                return new ResponseData(false, "置顶状态参数为空！", EventCode.PARAM_DELETE_ReCYCLE_WRONG);
            if (Validator.isEmpty(thingId))
                return new ResponseData(false, "小记编号参数为空！", EventCode.PARAM_THING_ID_WRONG);
            thingService.deleteTingById(complete, thingId, user.getId(), isRecycleBin);
            return new ResponseData(true, complete ? "彻底删除成功！" : "删除成功！", EventCode.UPDATE_SUCCESS);
        } catch (ServiceException e) {
            e.printStackTrace();
            return new ResponseData(false, e.getMessage(), e.getCode());
        }
    }

    @GetMapping("/list")
    public ResponseData getUserTingList(@RequestHeader String userToken) {

        try {
            User user = TokenValidateUtil.validateUserToken(userToken, stringRedisTemplate);
            List<Thing> things = thingService.getUserNormalTing(user.getId());
            return new ResponseData(true, "获取成功！", EventCode.SELECT_SUCCESS, things);
        } catch (ServiceException e) {
            e.printStackTrace();
            return new ResponseData(false, e.getMessage(), e.getCode());
        }
    }

    @GetMapping("/top")
    public ResponseData topThing(boolean isTop, int thingId, @RequestHeader String userToken) {
        try {
            User user = TokenValidateUtil.validateUserToken(userToken, stringRedisTemplate);
            if (Validator.isEmpty(isTop))
                return new ResponseData(false, "置顶状态参数为空！", EventCode.PARAM_TOP_WRONG);
            if (Validator.isEmpty(thingId))
                return new ResponseData(false, "小记编号参数为空！", EventCode.PARAM_THING_ID_WRONG);
            thingService.topThing(isTop, thingId, user.getId());
            return new ResponseData(true, isTop ? "置顶成功！" : "取消置顶成功！", EventCode.UPDATE_SUCCESS);
        } catch (ServiceException e) {
            e.printStackTrace();
            return new ResponseData(false, e.getMessage(), e.getCode());
        }
    }
}
