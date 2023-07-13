package com.praver.springboot;

import cn.hutool.crypto.SecureUtil;
import com.praver.springboot.entity.UserLog;
import com.praver.springboot.mapper.IUserLogMapper;
import com.praver.springboot.util.code.EventCode;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.Date;

@SpringBootTest
class SpringbootApplicationTests {

    @Resource
    private IUserLogMapper userLogMapper;

    @Test
    void contextLoads() {
        System.out.println(SecureUtil.md5("123456"));

    }

}
