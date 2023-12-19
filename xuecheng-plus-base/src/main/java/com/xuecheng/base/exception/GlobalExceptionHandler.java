package com.xuecheng.base.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 异常处理器
 * @Author Zihao Qin
 * @Date 2023/12/19 11:31
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    // 自定义业务异常
    @ExceptionHandler(XueChengPlusException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public RestErrorResponse customException(XueChengPlusException e){
        // 日志打印
        log.error("业务异常：{}", e.getErrMessage());

        // 解析异常
        RestErrorResponse restErrorResponse = new RestErrorResponse(e.getErrMessage());

        // 返回异常信息
        return restErrorResponse;
    }

    // 系统异常
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public RestErrorResponse exception(Exception e){
        log.error("系统异常：{}", e.getMessage());

        RestErrorResponse restErrorResponse = new RestErrorResponse(CommonError.UNKOWN_ERROR.getErrMessage());

        return restErrorResponse;
    }
}
