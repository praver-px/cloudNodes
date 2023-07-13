package com.praver.springboot.mapper;

import com.mybatisflex.core.BaseMapper;
import com.praver.springboot.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;


@Mapper
@Repository
public interface IUserMapper extends BaseMapper<User> {
}
