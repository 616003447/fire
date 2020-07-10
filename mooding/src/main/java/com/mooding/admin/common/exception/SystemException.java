package com.mooding.admin.common.exception;

/**
 * 自定义异常
 * @Author BlueFire
 * @Date 2020/7/9 -20:07
 */
public class SystemException extends RuntimeException{
    private static final long serialVersionUID = 1L;

    public SystemException(String message){
        super(message);
    }

    public SystemException(Throwable cause)
    {
        super(cause);
    }

    public SystemException(String message,Throwable cause)
    {
        super(message,cause);
    }
}
