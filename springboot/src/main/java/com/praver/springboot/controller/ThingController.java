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
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/thing")
public class ThingController {

    @Resource
    private IThingService thingService;
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @PostMapping("/update")
    public ResponseData updateThing(int thingId, String title, boolean top, String tags, String content, boolean finished, @RequestHeader String userToken) {
        try {
            User user = TokenValidateUtil.validateUserToken(userToken, stringRedisTemplate);
            if (Validator.isEmpty(thingId))
                return new ResponseData(false, "小记Id参数为空！", EventCode.PARAM_THING_ID_WRONG);
            if (Validator.isEmpty(title))
                return new ResponseData(false, "小记标题参数为空！", EventCode.PARAM_THING_TITLE_WRONG);
            if (Validator.isEmpty(top))
                return new ResponseData(false, "小记置顶参数为空！", EventCode.PARAM_THING_TOP_WRONG);
            if (Validator.isEmpty(tags))
                return new ResponseData(false, "小记标签参数为空！", EventCode.PARAM_THING_TAGS_WRONG);
            if (Validator.isEmpty(content))
                return new ResponseData(false, "小记内容参数为空！", EventCode.PARAM_THING_CONTENT_WRONG);
            if (Validator.isEmpty(finished))
                return new ResponseData(false, "小记完成参数为空！", EventCode.PARAM_THING_FINISHED_WRONG);
            Thing thing = Thing.builder()
                    .id(thingId)
                    .title(title).top(top ? 1 : 0).tags(tags)
                    .content(content).time(new Date())
                    .userId(user.getId()).finished(finished ? 1 : 0)
                    .updateTime(new Date()).build();
            thingService.updateThing(thing);
            return new ResponseData(true, "小记修改成功！", EventCode.THING_UPDATE_SUCCESS);
        } catch (ServiceException e) {
            e.printStackTrace();
            return new ResponseData(false, e.getMessage(), e.getCode());
        }
    }

    @GetMapping("/getOne")
    public ResponseData getThing(int thingId, @RequestHeader String userToken) {
        try {
            User user = TokenValidateUtil.validateUserToken(userToken, stringRedisTemplate);
            if (Validator.isEmpty(thingId))
                return new ResponseData(false, "小记编号参数为空！", EventCode.PARAM_THING_ID_WRONG);
            Thing thing = thingService.getThing(thingId, user.getId());
            return new ResponseData(true, "获取小记", EventCode.SELECT_SUCCESS, thing);
        } catch (ServiceException e) {
            e.printStackTrace();
            return new ResponseData(false, e.getMessage(), e.getCode());
        }
    }

    @PutMapping("/create")
    public ResponseData createThing(String title, boolean top, String tags, String content, boolean finished, @RequestHeader String userToken) {
        try {
            User user = TokenValidateUtil.validateUserToken(userToken, stringRedisTemplate);
            if (Validator.isEmpty(title))
                return new ResponseData(false, "小记标题参数为空！", EventCode.PARAM_THING_TITLE_WRONG);
            if (Validator.isEmpty(top))
                return new ResponseData(false, "小记置顶参数为空！", EventCode.PARAM_THING_TOP_WRONG);
            if (Validator.isEmpty(tags))
                return new ResponseData(false, "小记标签参数为空！", EventCode.PARAM_THING_TAGS_WRONG);
            if (Validator.isEmpty(content))
                return new ResponseData(false, "小记内容参数为空！", EventCode.PARAM_THING_CONTENT_WRONG);
            if (Validator.isEmpty(finished))
                return new ResponseData(false, "小记完成参数为空！", EventCode.PARAM_THING_FINISHED_WRONG);
            Thing thing = Thing.builder()
                    .time(new Date())
                    .title(title).top(top ? 1 : 0).tags(tags)
                    .content(content).time(new Date())
                    .userId(user.getId()).finished(finished ? 1 : 0)
                    .updateTime(new Date()).build();
            thingService.newCreateThing(thing);
            return new ResponseData(true, "小记新增成功！", EventCode.THING_CREATE_SUCCESS);
        } catch (ServiceException e) {
            e.printStackTrace();
            return new ResponseData(false, e.getMessage(), e.getCode());
        }
    }

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
    public ResponseData getUserTingList(String search, Integer filter, @RequestHeader String userToken) {
        try {
            User user = TokenValidateUtil.validateUserToken(userToken, stringRedisTemplate);
            List<Thing> things = thingService.getUserNormalTing(search, filter, user.getId());
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
