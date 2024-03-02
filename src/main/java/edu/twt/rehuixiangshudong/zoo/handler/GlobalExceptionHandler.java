package edu.twt.rehuixiangshudong.zoo.handler;

import edu.twt.rehuixiangshudong.zoo.constant.MessageConstant;
import edu.twt.rehuixiangshudong.zoo.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    /**
     *捕获业务逻辑异常
     * @param ex 异常
     *
     */
    @ExceptionHandler
    public Result<Object> exceptionHandler(Exception ex) {
        log.error("异常为：{}",ex.getClass());
        log.error("异常信息为：{}",ex.getMessage());
        if(ex.getMessage().length() > 20){
            return Result.fail(MessageConstant.NOT_KNOWN_ERROR);
        }
        return Result.fail(ex.getMessage());
    }
}
