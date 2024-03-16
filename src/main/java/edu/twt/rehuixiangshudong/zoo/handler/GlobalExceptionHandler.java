package edu.twt.rehuixiangshudong.zoo.handler;

import edu.twt.rehuixiangshudong.zoo.constant.MessageConstant;
import edu.twt.rehuixiangshudong.zoo.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.connector.ClientAbortException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler
    public void clientAbortExceptionHandler(ClientAbortException clientAbortException) {
        log.error("客户端中断了连接！");
    }
    /**
     *捕获业务逻辑异常
     * @param ex 异常
     *
     */
    @ExceptionHandler
    public Result<Object> exceptionHandler(Exception ex) {
        log.error("异常为：{}",ex.getClass());
        log.error("异常信息为：{}",ex.getMessage());
        if (ex.getMessage() == null){//异常信息为空 避免空指针异常
            return Result.fail(MessageConstant.NOT_KNOWN_ERROR);
        }
        //上传文件过大提示
        if (ex.getMessage().contains("size exceeded")) {
            return Result.fail(MessageConstant.FILE_TOO_BIG);
        }
        //未发现资源提示
        if (ex.getMessage().contains("static resource")) {
            return Result.fail(MessageConstant.RESOURCE_NOT_FOUND);
        }
        //其他提示
        if(ex.getMessage().length() > 20){
            return Result.fail(MessageConstant.NOT_KNOWN_ERROR);
        }
        return Result.fail(ex.getMessage());
    }
}
