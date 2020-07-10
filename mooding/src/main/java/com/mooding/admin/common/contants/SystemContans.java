package com.mooding.admin.common.contants;

/**
 * @Author BlueFire
 * @Date 2020/7/9 -8:48
 */
public class SystemContans {
    //用户相关常量
    /**
     * 是否用户已被删除 0正常  2删除
     */
    public static final Integer USER_UNDEL_FLAG = 0;
    public static final Integer USER_DEL_FLAG = 2;

    /**
     * 是否用户已被冻结 0正常(解冻) 2冻结
     */
    public static final Integer USER_UNFREEZE = 1;
    public static final Integer USER_FREEZE = 2;


    /** 登录用户Token令牌缓存KEY前缀 */
    public static final String PREFIX_USER_TOKEN  = "prefix_user_token_";

    /**图片验证码基础字符*/
    public static final String RANDOM_IMAGE_BASE_CHECK_CODES = "qwertyuiplkjhgfdsazxcvbnmQWERTYUPLKJHGFDSAZXCVBNM1234567890";

    /**
     * 短信模板方式  0 .登录模板、1.注册模板、2.忘记密码模板
     */
    public static final String SMS_TPL_TYPE_0  = "0";
    public static final String SMS_TPL_TYPE_1  = "1";
    public static final String SMS_TPL_TYPE_2  = "2";
}
