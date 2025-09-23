package io.github.anthem37.gof23.behavioral.memento;

import io.github.anthem37.gof23.common.FrameworkException;

/**
 * 发起人接口
 * 定义创建和恢复备忘录的操作
 *
 * @param <T> 状态类型
 * @param <M> 备忘录类型
 * @author anthem37
 * @version 1.0.0
 */
public interface Originator<T, M extends Memento<T>> {

    /**
     * 创建备忘录
     *
     * @return 备忘录
     * @throws FrameworkException 如果创建失败
     */
    M createMemento() throws FrameworkException;

    /**
     * 恢复状态
     *
     * @param memento 备忘录
     * @throws FrameworkException 如果恢复失败
     */
    void restoreFromMemento(M memento) throws FrameworkException;

    /**
     * 获取当前状态
     *
     * @return 当前状态
     */
    T getCurrentState();

    /**
     * 设置状态
     *
     * @param state 状态
     * @throws FrameworkException 如果设置失败
     */
    void setState(T state) throws FrameworkException;

    /**
     * 检查是否支持指定的备忘录类型
     *
     * @param mementoType 备忘录类型
     * @return 是否支持
     */
    boolean supports(Class<?> mementoType);
}