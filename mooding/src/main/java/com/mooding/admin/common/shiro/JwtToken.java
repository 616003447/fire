package com.mooding.admin.common.shiro;

import org.apache.shiro.authc.AuthenticationToken;

/**
 * @Author BlueFire
 * @Date 2020/7/11 -8:45
 */

public class JwtToken implements AuthenticationToken {

    private static final long serialVersionUID = 1L;
    private String token;

    public JwtToken(String token) {
        this.token = token;
    }

    @Override
    public Object getPrincipal() {
        return token;
    }

    @Override
    public Object getCredentials() {
        return token;
    }
}

