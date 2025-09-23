package io.github.anthem37.gof23.behavioral.memento;

/**
 * 备忘录接口
 * 定义备忘录的基本操作
 *
 * @param <T> 状态类型
 * @author anthem37
 * @version 1.0.0
 */
public interface Memento<T> {

    /**
     * 获取状态
     *
     * @return 状态
     */
    T getState();

    /**
     * 获取创建时间戳
     *
     * @return 创建时间戳
     */
    long getTimestamp();

    /**
     * 获取备忘录标识
     *
     * @return 备忘录标识
     */
    String getMementoId();

    /**
     * 检查备忘录是否有效
     *
     * @return 是否有效
     */
    boolean isValid();
}