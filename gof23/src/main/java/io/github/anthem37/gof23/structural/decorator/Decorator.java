package io.github.anthem37.gof23.structural.decorator;

/**
 * 装饰器接口
 * 定义装饰器的标准契约
 *
 * @param <T> 被装饰对象类型
 * @author anthem37
 * @version 1.0.0
 */
public interface Decorator<T> {

    /**
     * 装饰对象
     *
     * @param target 被装饰的目标对象
     * @return 装饰后的对象
     */
    T decorate(T target);

    /**
     * 获取装饰器名称
     *
     * @return 装饰器名称
     */
    String getName();

    /**
     * 获取装饰器优先级
     * 数值越小优先级越高
     *
     * @return 优先级
     */
    default int getPriority() {
        return Integer.MAX_VALUE;
    }

    /**
     * 检查是否支持装饰指定类型的对象
     *
     * @param targetClass 目标对象类型
     * @return 是否支持
     */
    default boolean supports(Class<?> targetClass) {
        return true;
    }
}