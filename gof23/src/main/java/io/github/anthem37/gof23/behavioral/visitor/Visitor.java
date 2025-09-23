package io.github.anthem37.gof23.behavioral.visitor;

/**
 * 访问者接口
 * 定义访问者的标准契约
 *
 * @author anthem37
 * @version 1.0.0
 */
public interface Visitor {

    /**
     * 访问元素
     *
     * @param element 被访问的元素
     * @return 访问结果
     */
    Object visit(Visitable element);

    /**
     * 获取访问者名称
     *
     * @return 访问者名称
     */
    String getName();

    /**
     * 检查是否支持访问指定类型的元素
     *
     * @param elementClass 元素类型
     * @return 是否支持
     */
    default boolean supports(Class<?> elementClass) {
        return true;
    }

    /**
     * 访问前的准备工作
     *
     * @param element 被访问的元素
     */
    default void beforeVisit(Visitable element) {
        // 默认空实现
    }

    /**
     * 访问后的清理工作
     *
     * @param element 被访问的元素
     * @param result 访问结果
     */
    default void afterVisit(Visitable element, Object result) {
        // 默认空实现
    }
}