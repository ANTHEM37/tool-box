package io.github.anthem37.gof23.common;

/**
 * 框架统一异常类
 * 提供统一的异常处理机制
 *
 * @author anthem37
 * @version 1.0.0
 */
public class FrameworkException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    /**
     * 构造函数
     *
     * @param message 异常消息
     */
    public FrameworkException(String message) {
        super(message);
    }

    /**
     * 构造函数
     *
     * @param message 异常消息
     * @param cause 原因异常
     */
    public FrameworkException(String message, Throwable cause) {
        super(message, cause);
    }

    /**
     * 构造函数
     *
     * @param cause 原因异常
     */
    public FrameworkException(Throwable cause) {
        super(cause);
    }
}