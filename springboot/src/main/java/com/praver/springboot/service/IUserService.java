package com.praver.springboot.service;

import com.praver.springboot.exception.ServiceException;

import java.util.Map;

public interface IUserService {

    /**
     * 获取邮箱是否已被注册
     *
     * @param email 邮箱号
     * @throws ServiceException
     */
    void getEmailAccountIsExist(String email) throws ServiceException;

    /**
     * 登录（邮箱哈，密码）
     *
     * @param email    邮箱号
     * @param password 密码
     * @return {user： 登录成后的用户信息，userToken：存到redis中}
     */
    Map loginByEmailAndPassword(String email, String password) throws ServiceException;

    /**
     * 根据邮箱来注册账号
     * @param email - 邮箱号奥
     */
    void registerAccountByEmail(String email) throws ServiceException;
}
