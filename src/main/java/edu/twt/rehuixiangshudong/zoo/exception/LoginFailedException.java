package edu.twt.rehuixiangshudong.zoo.exception;

public class LoginFailedException extends RuntimeException {
    /**
     *
     * @param message 异常提示
     */
    public LoginFailedException(String message) {
        super(message);
    }
}
