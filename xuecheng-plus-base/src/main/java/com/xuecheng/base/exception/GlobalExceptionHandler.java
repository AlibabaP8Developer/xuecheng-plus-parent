package com.xuecheng.base.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import java.util.List;

/**
 * @author Mr.M
 * @version 1.0
 * @description 全局异常处理器
 * @date 2022/9/6 11:29
 */
@Slf4j
@ControllerAdvice // 控制器增强
public class GlobalExceptionHandler {

    // 处理xuechengplusexception异常，此类异常是手动抛出的异常，可预知的异常
    @ResponseBody
    @ExceptionHandler(XueChengPlusException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR) // 状态码500
    public RestErrorResponse doXueChengPlusException(XueChengPlusException e) {
        String errMessage = e.getErrMessage();
        log.error("【捕获系统异常】{}", errMessage);
        return new RestErrorResponse(errMessage);
    }

    // 捕获不可预知的异常
    @ResponseBody
    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public RestErrorResponse doValidException(MethodArgumentNotValidException argumentNotValidException) {
        BindingResult bindingResult = argumentNotValidException.getBindingResult();
        // 收集错误
        StringBuffer errMsg = new StringBuffer();

        List<FieldError> fieldErrors = bindingResult.getFieldErrors();
        fieldErrors.forEach(error -> {
            errMsg.append(error.getDefaultMessage()).append(",");
        });
        log.error(errMsg.toString());
        return new RestErrorResponse(errMsg.toString());
    }

    @ExceptionHandler(value = BindException.class)
    public RestErrorResponse exceptionHandle(BindException exception) {

        BindingResult result = exception.getBindingResult();
        StringBuilder errorMsg = new StringBuilder();

        List<FieldError> fieldErrors = result.getFieldErrors();
        fieldErrors.forEach(error -> {
            log.error("field: " + error.getField() + ", msg:" + error.getDefaultMessage());
            errorMsg.append(error.getDefaultMessage()).append("!");
        });
        return new RestErrorResponse(errorMsg.toString());
    }

    @ResponseBody
    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public RestErrorResponse exception(Exception e) {

        log.error("【系统异常】{}", e.getMessage(), e);
        e.printStackTrace();
        if (e.getMessage().equals("不允许访问")) {
            return new RestErrorResponse("没有操作此功能的权限");
        }
        return new RestErrorResponse(CommonError.UNKOWN_ERROR.getErrMessage());
    }
}
