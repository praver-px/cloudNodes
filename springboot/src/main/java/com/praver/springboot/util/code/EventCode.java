package com.praver.springboot.util.code;

public class EventCode {

    // ===== 登录状态码=====
    /**
     * 已经登陆
     */
    public static final String LOGIN_EMAIL_PASSWORD_SUCCESS = "L_001";
    /**
     * 登录失败
     */
    public static final String login_fail = "L_002";
    /**
     * 登录日志创建异常
     */
    public static final String LOGIN_LOG_CREATE_EXCEPTION = "L_003";
    /**
     * 登录日志创建失败
     */
    public static final String LOGIN_LOG_CREATE_FAIL = "L_004";
    /**
     * 登录成功后存入用户信息至redis失败
     */
    public static final String LOGIN_SAVE_USER_TOKEN_REDIS_EXCEPTION = "L_005";

    // ===== 账号状态码=====
    /**
     * 账号已被锁定
     */
    public static final String ACCOUNT_CLOCK = "A_001";

    /**
     * 邮箱账号有误
     */
    public static final String ACCOUNT_EMAIL_WRONG = "A_002";
    /**
     * 密码有误
     */
    public static final String ACCOUNT_PASSWORD_WRONG = "A_003";

    // ===== SQL 业务状态码=====
    /**
     * 查询成功
     */
    public static final String SELECT_SUCCESS = "S_001";
    /**
     * 查询异常
     */
    public static final String SELECT_EXCEPTION = "S_001";
    /**
     * 查询失败
     */
    public static final String SELECT_ERROR = "S_001";
    /**
     * 查询为空
     */
    public static final String SELECT_NONE = "S_001";

}
