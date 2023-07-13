package com.praver.springboot.mapper;

import com.mybatisflex.core.BaseMapper;
import com.praver.springboot.entity.UserLog;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;


@Mapper
@Repository
public interface IUserLogMapper extends BaseMapper<UserLog> {
}
