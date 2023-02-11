package com.xuecheng.base.exception;

/**
 * @author Mr.M
 * @version 1.0
 * @description 学成在线项目异常类
 * @date 2022/9/6 11:29
 */
public class XueChengPlusException extends RuntimeException {

    private static final long serialVersionUID = 5565760508056698922L;

    private String errMessage;

    public XueChengPlusException() {
        super();
    }

    public XueChengPlusException(String errMessage) {
        super(errMessage);
        this.errMessage = errMessage;
    }

    public String getErrMessage() {
        return errMessage;
    }

    public static void cast(CommonError commonError) {
        throw new XueChengPlusException(commonError.getErrMessage());
    }

    public static void cast(String errMessage) {
        throw new XueChengPlusException(errMessage);
    }

}
