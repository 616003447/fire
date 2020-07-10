package com.mooding.admin.system.model;

import lombok.Data;

/**
 * 登录表单
 *
 * @Author BlueFire
 * @Date 2020/7/9 -8:15
 */
@Data
public class SysLoginModel {

   // @ApiModelProperty(value = "账号")
    private String username;
   // @ApiModelProperty(value = "密码")
    private String password;
    //@ApiModelProperty(value = "验证码")
    private String captcha;
   // @ApiModelProperty(value = "验证码key")
    private String checkKey;
}
