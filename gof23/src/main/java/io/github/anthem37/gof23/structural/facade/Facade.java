package io.github.anthem37.gof23.structural.facade;

import java.util.Map;

/**
 * 外观接口
 * 定义外观模式的标准契约
 *
 * @author anthem37
 * @version 1.0.0
 */
public interface Facade {

    /**
     * 执行操作
     *
     * @param operation 操作名称
     * @param parameters 操作参数
     * @return 操作结果
     */
    Object execute(String operation, Map<String, Object> parameters);

    /**
     * 检查是否支持指定操作
     *
     * @param operation 操作名称
     * @return 是否支持
     */
    boolean supports(String operation);

    /**
     * 获取支持的所有操作
     *
     * @return 操作名称集合
     */
    java.util.Set<String> getSupportedOperations();

    /**
     * 获取外观名称
     *
     * @return 外观名称
     */
    String getName();

    /**
     * 初始化外观
     * 在外观创建后调用
     */
    default void initialize() {
        // 默认空实现
    }

    /**
     * 销毁外观
     * 在外观销毁前调用
     */
    default void destroy() {
        // 默认空实现
    }
}