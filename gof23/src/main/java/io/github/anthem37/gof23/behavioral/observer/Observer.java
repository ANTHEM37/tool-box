package io.github.anthem37.gof23.behavioral.observer;

/**
 * 观察者接口
 * 定义观察者接收通知的行为规范
 *
 * @param <T> 事件数据类型
 * @author anthem37
 * @version 1.0.0
 */
public interface Observer<T> {

    /**
     * 接收事件通知
     *
     * @param event 事件数据
     */
    void onEvent(T event);

    /**
     * 获取观察者标识
     * 用于观察者的注册和管理
     *
     * @return 观察者标识
     */
    default String getIdentifier() {
        return this.getClass().getSimpleName() + "@" + Integer.toHexString(hashCode());
    }
}