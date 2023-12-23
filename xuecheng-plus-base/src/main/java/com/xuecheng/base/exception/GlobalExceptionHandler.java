package com.xuecheng.base.exception;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

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
        log.error("系统异常：{}", e.getMessage(),e);

        RestErrorResponse restErrorResponse = new RestErrorResponse(CommonError.UNKOWN_ERROR.getErrMessage());

        return restErrorResponse;
    }

    // 校验异常
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public RestErrorResponse methodArgumentNotValidException(MethodArgumentNotValidException e){

        // 单次验证可能有很多报错信息
        BindingResult bindingResult = e.getBindingResult();

        List<String> errorList = new ArrayList<>();

        // 获取所有错误信息
        bindingResult.getAllErrors().stream().forEach(item->{
            errorList.add(item.getDefaultMessage());
        });

        String error = StringUtils.join(errorList, ",");

        log.error("校验异常：{}", error);

        RestErrorResponse restErrorResponse = new RestErrorResponse(error);

        return restErrorResponse;
    }
}
