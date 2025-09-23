package io.github.anthem37.gof23.structural.flyweight;

/**
 * 享元接口
 * 定义享元对象的基本操作
 *
 * @param <T> 外部状态类型
 * @param <R> 操作结果类型
 * @author anthem37
 * @version 1.0.0
 */
public interface Flyweight<T, R> {

    /**
     * 执行操作
     *
     * @param extrinsicState 外部状态
     * @return 操作结果
     */
    R operation(T extrinsicState);

    /**
     * 获取内部状态标识
     *
     * @return 内部状态标识
     */
    String getIntrinsicState();

    /**
     * 检查是否支持指定的外部状态类型
     *
     * @param stateType 外部状态类型
     * @return 是否支持
     */
    boolean supports(Class<?> stateType);
}