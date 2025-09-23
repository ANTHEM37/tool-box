package io.github.anthem37.gof23.behavioral.strategy;

/**
 * 策略接口
 * 定义策略的统一行为规范
 *
 * @param <T> 输入参数类型
 * @param <R> 返回结果类型
 * @author anthem37
 * @version 1.0.0
 */
public interface Strategy<T, R> {

    /**
     * 执行策略
     *
     * @param input 输入参数
     * @return 执行结果
     */
    R execute(T input);

    /**
     * 获取策略标识
     * 用于策略注册和查找
     *
     * @return 策略标识
     */
    String getIdentifier();
}