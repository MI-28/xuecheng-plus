package com.xuecheng.base.exception;
import java.io.Serializable;

/**
 * 异常信息封装类
 * @Author Zihao Qin
 * @Date 2023/12/19 11:20
 */
public class RestErrorResponse implements Serializable {

    private String errMessage;

    public RestErrorResponse(String errMessage){
        this.errMessage= errMessage;
    }

    public String getErrMessage() {
        return errMessage;
    }

    public void setErrMessage(String errMessage) {
        this.errMessage = errMessage;
    }
}