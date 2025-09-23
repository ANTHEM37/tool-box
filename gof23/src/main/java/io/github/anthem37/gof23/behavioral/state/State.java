package io.github.anthem37.gof23.behavioral.state;

/**
 * 状态接口
 * 定义状态的标准契约
 *
 * @param <T> 上下文类型
 * @author anthem37
 * @version 1.0.0
 */
public interface State<T> {

    /**
     * 进入状态时的处理
     *
     * @param context 上下文对象
     */
    default void onEnter(T context) {
        // 默认空实现
    }

    /**
     * 退出状态时的处理
     *
     * @param context 上下文对象
     */
    default void onExit(T context) {
        // 默认空实现
    }

    /**
     * 处理事件
     *
     * @param context 上下文对象
     * @param event 事件
     * @return 处理结果
     */
    Object handle(T context, String event);

    /**
     * 获取状态名称
     *
     * @return 状态名称
     */
    String getName();

    /**
     * 检查是否支持指定事件
     *
     * @param event 事件名称
     * @return 是否支持
     */
    default boolean supports(String event) {
        return true;
    }

    /**
     * 获取支持的事件列表
     *
     * @return 事件名称集合
     */
    default java.util.Set<String> getSupportedEvents() {
        return java.util.Collections.emptySet();
    }
}