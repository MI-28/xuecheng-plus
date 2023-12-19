package com.xuecheng.base.exception;

/**
 * 自定义异常类
 * @Author Zihao Qin
 * @Date 2023/12/19 11:20
 */
public class XueChengPlusException extends RuntimeException {

    // 异常信息
    private String errMessage;

    public XueChengPlusException() {
    }

    public XueChengPlusException(String errMessage) {
//        super(errMessage);
        this.errMessage = errMessage;
    }

    // 异常抛出方法（String）
    public static void cast(String errMessage){
        throw new XueChengPlusException(errMessage);
    }

    // 异常抛出方法（枚举）
    public static void cast(CommonError error){
        throw new XueChengPlusException(error.getErrMessage());
    }

    public String getErrMessage() {
        return errMessage;
    }

    public void setErrMessage(String errMessage) {
        this.errMessage = errMessage;
    }
}
