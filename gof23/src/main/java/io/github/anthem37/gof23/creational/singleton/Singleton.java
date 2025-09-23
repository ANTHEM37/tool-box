package io.github.anthem37.gof23.creational.singleton;

/**
 * 单例接口
 * 定义单例对象的标准契约
 *
 * @author anthem37
 * @version 1.0.0
 */
public interface Singleton {

    /**
     * 初始化单例实例
     * 在实例创建后调用，用于执行初始化逻辑
     */
    default void initialize() {
        // 默认空实现
    }

    /**
     * 销毁单例实例
     * 在实例销毁前调用，用于执行清理逻辑
     */
    default void destroy() {
        // 默认空实现
    }

    /**
     * 检查实例是否已初始化
     *
     * @return 是否已初始化
     */
    default boolean isInitialized() {
        return true;
    }
}