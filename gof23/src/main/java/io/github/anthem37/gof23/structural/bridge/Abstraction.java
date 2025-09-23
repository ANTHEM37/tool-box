package io.github.anthem37.gof23.structural.bridge;

/**
 * 桥接模式抽象类
 * 定义抽象部分的接口，维护一个指向实现部分的引用
 *
 * @param <T> 操作参数类型
 * @param <R> 操作结果类型
 * @author anthem37
 * @version 1.0.0
 */
public abstract class Abstraction<T, R> {

    /**
     * 实现部分的引用
     */
    protected Implementation<T, R> implementation;

    /**
     * 构造函数
     *
     * @param implementation 实现对象
     */
    protected Abstraction(Implementation<T, R> implementation) {
        this.implementation = implementation;
    }

    /**
     * 基本操作
     *
     * @param input 输入参数
     * @return 操作结果
     */
    public abstract R operation(T input);

    /**
     * 扩展操作
     *
     * @param input 输入参数
     * @return 操作结果
     */
    public abstract R extendedOperation(T input);

    /**
     * 获取抽象名称
     *
     * @return 抽象名称
     */
    public abstract String getAbstractionName();

    /**
     * 获取实现
     *
     * @return 实现对象
     */
    public Implementation<T, R> getImplementation() {
        return implementation;
    }

    /**
     * 设置实现
     *
     * @param implementation 实现对象
     */
    public void setImplementation(Implementation<T, R> implementation) {
        this.implementation = implementation;
    }

    /**
     * 验证实现是否兼容
     *
     * @param implementation 实现对象
     * @return 是否兼容
     */
    public abstract boolean isCompatible(Implementation<T, R> implementation);
}