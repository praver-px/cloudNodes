package com.praver.springboot.service.impl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.json.JSONUtil;
import com.mybatisflex.core.query.QueryWrapper;
import com.praver.springboot.entity.User;
import com.praver.springboot.entity.UserLog;
import com.praver.springboot.exception.ServiceException;
import com.praver.springboot.exception.ServiceRollbackException;
import com.praver.springboot.mapper.IUserLogMapper;
import com.praver.springboot.mapper.IUserMapper;
import com.praver.springboot.service.IUserService;
import com.praver.springboot.util.code.EventCode;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.praver.springboot.entity.table.Tables.USER;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
@Transactional(rollbackFor = ServiceRollbackException.class)
public class UserServiceImpl implements IUserService {

    @Resource
    private IUserMapper userMapper;

    @Resource
    private IUserLogMapper userLogMapper;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    /**
     *  邮箱号密码登录实现
     * @param email 邮箱号
     * @param password 密码
     * @return 用户的对象，redis中的userTokenKey
     * @throws ServiceException
     */
    @Override
    public Map loginByEmailAndPassword(String email, String password) throws ServiceException {
        //根据邮箱和密码查询用户记录
        QueryWrapper queryWrapper = QueryWrapper.create()
                .select(USER.ID, USER.EMAIL, USER.NICKNAME, USER.HEAD_PIC, USER.LEVEL, USER.STATUS, USER.TIME)
                .where(USER.EMAIL.eq(email))
                .and(USER.PASSWORD.eq(password));
        User user = null;
        try {
            user = userMapper.selectOneByQuery(queryWrapper);
        } catch (Exception e) {
            throw new ServiceException("登录服务器错误！", EventCode.SELECT_EXCEPTION);
        }
        if (user == null) {
            throw new ServiceException("账号或密码错误！", EventCode.SELECT_NONE);
        }
        if (user.getStatus() == 0) {
            throw new ServiceException("该账号已禁用！", EventCode.ACCOUNT_CLOCK);
        }

        //新增用户登录日志
        UserLog userLog = UserLog.builder()
                .event(EventCode.LOGIN_EMAIL_PASSWORD_SUCCESS)
                .desc("邮箱号密码登录")
                .time(new Date())
                .userId(user.getId())
                .build();

        int count = 0;
        try {
            count = userLogMapper.insert(userLog);
        } catch (Exception e) {
            throw new ServiceException("登录服务错误！",EventCode.LOGIN_LOG_CREATE_EXCEPTION);
        }
        if (count != 1){
            throw new ServiceRollbackException("登录服务错误！",EventCode.LOGIN_LOG_CREATE_FAIL);
        }
        //将登录信息存入redis中: 14天-将查询登录用户的关键词返回给客户端
        String userTokenKey ="userToken:" + IdUtil.randomUUID();
        try {
            stringRedisTemplate.opsForValue().set(
                    userTokenKey,
                    JSONUtil.toJsonStr(user),
                    14,
                    TimeUnit.DAYS
            );
        } catch (Exception e) {
            throw new ServiceRollbackException("登录服务错误！",EventCode.LOGIN_SAVE_USER_TOKEN_REDIS_EXCEPTION);
        }
        //将登录的用户信息和查询redis用户信息的关键词封装
        Map<String, Object> map = new HashMap<>();
        map.put("user",user);
        map.put("userToken",userTokenKey);
        return map;
    }
}
