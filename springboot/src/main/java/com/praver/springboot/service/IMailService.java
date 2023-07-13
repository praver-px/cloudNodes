package com.praver.springboot.service;

import com.praver.springboot.exception.ServiceException;

public interface IMailService {
    /**
     * 获取邮箱的验证码
     *
     * @param email 邮箱号
     * @return 查询验证码的关键词
     */
    String getEmailRegisterVC(String email) throws ServiceException;
}
