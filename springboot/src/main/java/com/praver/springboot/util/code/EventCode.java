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
    public static final String LOGIN_FAIL = "L_002";
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
    /**
     * 退出登录异常
     */
    public static final String LOGIN_OUT_EXCEPTION = "L_006";
    /**
     * 退出登录成功
     */
    public static final String LOGIN_OUT_SUCCESS = "L_007";
    /**
     * 登录失效
     */
    public static final String LOGIN_INVALID = "L_008";


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
    /**
     * 邮箱号已被注册
     */
    public static final String ACCOUNT_EMAIL_REGISTERED = "A_004";
    /**
     * 邮箱注册成功
     */
    public static final String ACCOUNT_EMAIL_REGISTER_SUCCESS = "A_005";
    /**
     * 邮箱注册日志异常
     */
    public static final String ACCOUNT_EMAIL_REGISTER_LOG_EXCEPTION = "A_006";
    /**
     * 邮箱注册日志失败
     */
    public static final String ACCOUNT_EMAIL_REGISTER_LOG_ERROR = "A_007";


    // ===== SQL 业务状态码=====
    /**
     * 查询成功
     */
    public static final String SELECT_SUCCESS = "S_001";
    /**
     * 查询异常
     */
    public static final String SELECT_EXCEPTION = "S_002";
    /**
     * 查询失败
     */
    public static final String SELECT_ERROR = "S_003";
    /**
     * 查询为空
     */
    public static final String SELECT_NONE = "S_004";
    /**
     * 新增异常
     */
    public static final String INSERT_EXCEPTION = "S_005";
    /**
     * 新增失败
     */
    public static final String INSERT_ERROR = "S_006";
    /**
     * 修改异常
     */
    public static final String UPDATE_EXCEPTION = "S_007";
    /**
     * 修改失败
     */
    public static final String UPDATE_ERROR = "S_008";
    /**
     * 修改成功
     */
    public static final String UPDATE_SUCCESS = "S_009";


    // ===== 邮箱服务状态码=====
    /**
     * 邮箱验证码发送成功
     */
    public static final String EMAIL_SAND_SUCCESS = "E_001";
    /**
     * 邮箱验证码发送失败
     */
    public static final String EMAIL_SAND_ERROR = "E_002";
    /**
     * 邮箱验证码存储失败
     */
    public static final String EMAIL_SEND_VC_SAVE_REDIS_ERROR = "E_003";


    /**
     * 发送初始密码异常
     */
    public static String Email_SAnd_INIT_PASSWORD_EXCEPTION = "E_004";

    //     ===== 验证码验证参数状态码=====
    /**
     * 验证码失效
     */
    public static final String VC_INVALID = "V_001";
    /**
     * 验证码匹配失败
     */
    public static final String VC_MATCH_ERROR = "V_002";

    //     ===== 验证码参数状态码=====
    /**
     * 验证码有误
     */
    public static final String PARAM_VC_WRONG = "P_001";
    /**
     * 验证码关键词有误
     */
    public static final String PARAM_VC_KEY_WRONG = "P_002";
    /**
     * 验证码与邮箱不匹配
     */
    public static final String PARAM_VC_KEY_EMAIL_WRONG = "P_003";
    /**
     * 登录状态有误
     */
    public static final String PARAM_USER_TOKEN_WRONG = "P_004";
    /**
     * 置顶参数有误
     */
    public static final String PARAM_TOP_WRONG = "P_005";
    /**
     * 小记编号参数有误
     */
    public static final String PARAM_THING_ID_WRONG = "P_006";
    /**
     * 删除参数有误 - 是否为彻底删除参数
     */
    public static final String PARAM_DELETE_COMPLETE_WRONG = "P_007";
    /**
     * 删除操作有误 - 是否为回收站操作
     */
    public static final String PARAM_DELETE_ReCYCLE_WRONG = "P_006";

    //     ===== Redis 服务状态码=====

    /**
     * redis 服务出错
     */
    public static final String REDIS_SERVE_ERROR = "R_001";

    //     ===== 小记 服务状态码=====
    /**
     * 小记置顶状态修改成功
     */
    public static final String THING_TOP_SUCCESS = "T_001";
    /**
     * 小记置顶状态修改失败
     */
    public static final String THING_TOP_FAILED = "T_002";
    /**
     * 删除小记成功
     */
    public static final String THING_DELETE_SUCCESS = "T_003";
    /**
     * 彻底删除小记成功
     */
    public static final String THING_COMPLETE_DELETE_SUCCESS = "T_003";


}
