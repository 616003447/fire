package com.mooding.admin.system.controller;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONObject;
import com.aliyuncs.exceptions.ClientException;
import com.mooding.admin.common.model.AppHttpCodeEnum;
import com.mooding.admin.common.model.ResponseResult;
import com.mooding.admin.common.contants.SystemContans;
import com.mooding.admin.system.entity.SysUser;
import com.mooding.admin.system.model.SysLoginModel;
import com.mooding.admin.system.service.ISysUserService;
import com.mooding.admin.utils.ObjectConvertUtils;
import com.mooding.admin.utils.cache.RedisUtil;
import com.mooding.admin.utils.security.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author BlueFire
 * @Date 2020/7/9 -7:51
 */
@Slf4j
@RestController
@RequestMapping("/sys")
public class LoginController {

    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private ISysUserService sysUserService;


    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public ResponseResult login(@RequestBody SysLoginModel sysLoginModel) {
        String username = sysLoginModel.getUsername();
        String password = sysLoginModel.getPassword();
        //1.验证图片验证码不能为空，前端进行判断
        String captcha = sysLoginModel.getCaptcha();
        if (captcha == null) {
            return ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_CAPTCHA_ERROR);
        }
        //2.验证验证码是否正确
        String lowerCaseCaptcha = captcha.toLowerCase();
        String realKey = MD5Util.MD5Encode(lowerCaseCaptcha + sysLoginModel.getCheckKey(), "utf-8");
        //从redis缓存中读取验证码和密码，暂时不做
        //Object checkCode = "123456";
        Object checkCode = redisUtil.get(realKey);
        if (checkCode == null || !checkCode.equals(lowerCaseCaptcha)) {
            return ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_CAPTCHA_ERROR);
        }
        //3. 校验用户是否存在且有效
        SysUser sysUser = sysUserService.getOneUserByUser(new SysUser().setUserName(username));
        ResponseResult result = sysUserService.checkUserIsEffective(sysUser);
        if (result != null) {
            return result;
        }

        //4. 校验用户名或密码是否正确
        String userpassword = PasswordUtil.encrypt(username, password, "12345678"/*sysUser.getSalt()*/);
        String syspassword = sysUser.getPassword();
        if (!syspassword.equals(userpassword)) {
            return ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_PASSWORD_ERROR);
        }

        // 生成token
        String token = JwtUtil.sign(username, syspassword);
        // 设置token缓存有效时间
        redisUtil.set(SystemContans.PREFIX_USER_TOKEN + token, token);
        redisUtil.expire(SystemContans.PREFIX_USER_TOKEN + token, JwtUtil.EXPIRE_TIME * 2 / 1000);

        Map<String, Object> map = new HashMap<>();
        map.put("token", token);
        map.put("user", sysUser);
        return ResponseResult.okResult(map);
    }

    /**
     * 后台生成图形验证码 ：有效
     *
     * @param response
     * @param key
     */
    //@ApiOperation("获取验证码")
    @GetMapping(value = "/randomImage/{key}")
    public ResponseResult randomImage(HttpServletResponse response, @PathVariable String key) {
        String base64 = null;
        try {
            String code = RandomUtil.randomString(SystemContans.RANDOM_IMAGE_BASE_CHECK_CODES, 4);
            code = "1234";//20200510 cfm add: 为方便测试，验证码设为定值
            String lowerCaseCode = code.toLowerCase();
            String realKey = MD5Util.MD5Encode(lowerCaseCode + key, "utf-8");
            redisUtil.set(realKey, lowerCaseCode, 60);
            base64 = RandImageUtil.generate(code);

        } catch (Exception e) {
            return ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_RANDOM_IMAGE_ERROR);
        }
        return ResponseResult.okResult(base64);
    }

    /**
     * 短信登录接口
     *
     * @param jsonObject
     * @return
     */
    @PostMapping(value = "/sms")
    public ResponseResult sms(@RequestBody JSONObject jsonObject) {
        String mobile = jsonObject.get("mobile").toString();
        //手机号模式 登录模式: "2"  注册模式: "1"
        String smsmode = jsonObject.get("smsmode").toString();
        log.info(mobile);
        if (ObjectConvertUtils.isEmpty(mobile)) {
            return ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_MOBILE_NOT_EMPTY_ERROR);
        }
        Object object = redisUtil.get(mobile);
        if (object != null) {
            return ResponseResult.okResult("验证码10分钟内，仍然有效！");
        }

        //随机数
        String captcha = RandomUtil.randomNumbers(6);
        JSONObject obj = new JSONObject();
        obj.put("code", captcha);
        try {
            boolean b = false;
            //注册模板
            if (SystemContans.SMS_TPL_TYPE_1.equals(smsmode)) {
                SysUser sysUser = sysUserService.getOneUserByUser(new SysUser().setPhonenumber(mobile));
                if (sysUser != null) {
                    //sysBaseAPI.addLog("手机号已经注册，请直接登录！", CommonConstant.LOG_TYPE_1, null);
                    return ResponseResult.okResult(" 手机号已经注册，请直接登录！");

                }
                b = DySmsHelper.sendSms(mobile, obj, DySmsEnum.REGISTER_TEMPLATE_CODE);
            } else {
                //登录模式，校验用户有效性
                SysUser sysUser = sysUserService.getOneUserByUser(new SysUser().setPhonenumber(mobile));
                ResponseResult result = sysUserService.checkUserIsEffective(sysUser);
                if (result != null) {
                    return result;
                }

                /**
                 * smsmode 短信模板方式  0 .登录模板、1.注册模板、2.忘记密码模板
                 */
                if (SystemContans.SMS_TPL_TYPE_0.equals(smsmode)) {
                    //登录模板
                    b = DySmsHelper.sendSms(mobile, obj, DySmsEnum.LOGIN_TEMPLATE_CODE);
                } else if (SystemContans.SMS_TPL_TYPE_2.equals(smsmode)) {
                    //忘记密码模板
                    b = DySmsHelper.sendSms(mobile, obj, DySmsEnum.FORGET_PASSWORD_TEMPLATE_CODE);
                }
            }

            if (b == false) {
                return ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_MOBILE_SMS_FAIL_ERROR);

            }
            //验证码10分钟内有效
            redisUtil.set(mobile, captcha, 600);
            //update-begin--Author:scott  Date:20190812 for：issues#391
            //result.setResult(captcha);
            //update-end--Author:scott  Date:20190812 for：issues#391
            ResponseResult.okResult("短信发送成功！十分钟内有效");

        } catch (ClientException e) {
            e.printStackTrace();
            //result.error500(" 短信接口未配置，请联系管理员！");
            return ResponseResult.errorResult(AppHttpCodeEnum.SERVER_ERROR);
        }
        return ResponseResult.okResult("短信发送成功！十分钟内有效");
    }


    /**
     * 手机号登录接口
     *
     * @param jsonObject
     * @return
     */
   // @ApiOperation("手机号登录接口")
    @PostMapping("/phoneLogin")
    public ResponseResult phoneLogin(@RequestBody JSONObject jsonObject) {

        String mobile = jsonObject.get("mobile").toString();

        //校验用户有效性
        SysUser sysUser =  sysUserService.getOneUserByUser(new SysUser().setPhonenumber(mobile));
        ResponseResult result = sysUserService.checkUserIsEffective(sysUser);
        if (result != null) {
            return result;
        }

        String smscode = jsonObject.get("captcha").toString();
        Object code = redisUtil.get(mobile);
        if (!smscode.equals(code)) {
            return ResponseResult.errorResult(AppHttpCodeEnum.LOGIN_CAPTCHA_ERROR);
        }
        //用户信息
       // userInfo(sysUser, result);
        //添加日志
        //sysBaseAPI.addLog("用户名: " + sysUser.getUsername() + ",登录成功！", CommonConstant.LOG_TYPE_1, null);

        return result;
    }

    /**
     * 获取用户信息
     *
     * @return 用户信息
     */
    @GetMapping("getInfo")
    public ResponseResult getInfo()
    {
        /*LoginUser loginUser = tokenService.getLoginUser(ServletUtils.getRequest());
        SysUser user = loginUser.getUser();
        // 角色集合
        Set<String> roles = permissionService.getRolePermission(user);
        // 权限集合
        Set<String> permissions = permissionService.getMenuPermission(user);
        AjaxResult ajax = AjaxResult.success();
        ajax.put("user", user);
        ajax.put("roles", roles);
        ajax.put("permissions", permissions);*/
        return ResponseResult.okResult("111");
    }

}
