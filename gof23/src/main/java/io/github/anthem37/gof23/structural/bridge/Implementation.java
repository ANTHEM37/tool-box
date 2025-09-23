package io.github.anthem37.gof23.structural.bridge;

/**
 * 桥接模式实现接口
 * 定义实现部分的接口，提供基本操作方法
 *
 * @param <T> 操作参数类型
 * @param <R> 操作结果类型
 * @author anthem37
 * @version 1.0.0
 */
public interface Implementation<T, R> {

    /**
     * 具体实现操作
     *
     * @param input 输入参数
     * @return 操作结果
     */
    R operationImpl(T input);

    /**
     * 获取实现名称
     *
     * @return 实现名称
     */
    String getImplementationName();

    /**
     * 获取实现类型
     *
     * @return 实现类型
     */
    String getImplementationType();

    /**
     * 检查是否支持指定操作
     *
     * @param operationType 操作类型
     * @return 是否支持
     */
    boolean supports(String operationType);

    /**
     * 初始化实现
     */
    void initialize();

    /**
     * 销毁实现
     */
    void destroy();
}