package com.praver.springboot.util.validate;

import cn.hutool.core.lang.Validator;
import cn.hutool.json.JSONUtil;
import com.praver.springboot.entity.User;
import com.praver.springboot.exception.ValidateParamException;
import com.praver.springboot.util.code.EventCode;
import org.springframework.data.redis.core.StringRedisTemplate;

public class TokenValidateUtil {


    public static User validateUserToken(String userToken, StringRedisTemplate stringRedisTemplate) throws ValidateParamException {
        if (Validator.isEmpty(userToken))
            throw new ValidateParamException("登录状态有误！", EventCode.PARAM_USER_TOKEN_WRONG);
        String userTokenRedisValue = null;
        try {
            userTokenRedisValue = stringRedisTemplate.opsForValue().get(userToken);
        } catch (Exception e) {
            e.printStackTrace();
            throw new ValidateParamException("服务错误！", EventCode.REDIS_SERVE_ERROR);
        }
        if (Validator.isEmpty(userTokenRedisValue))
            throw new ValidateParamException("登录失效！", EventCode.LOGIN_INVALID);
        return JSONUtil.toBean(userTokenRedisValue, User.class);
    }
}
