package io.github.anthem37.gof23.structural.adapter;

/**
 * 适配器接口
 * 定义适配器的标准契约
 *
 * @param <S> 源类型
 * @param <T> 目标类型
 * @author anthem37
 * @version 1.0.0
 */
public interface Adapter<S, T> {

    /**
     * 适配操作
     * 将源对象转换为目标对象
     *
     * @param source 源对象
     * @return 目标对象
     */
    T adapt(S source);

    /**
     * 检查是否支持适配指定的源类型
     *
     * @param sourceClass 源类型
     * @return 是否支持
     */
    boolean supports(Class<?> sourceClass);

    /**
     * 获取源类型
     *
     * @return 源类型
     */
    Class<S> getSourceType();

    /**
     * 获取目标类型
     *
     * @return 目标类型
     */
    Class<T> getTargetType();

    /**
     * 获取适配器名称
     *
     * @return 适配器名称
     */
    String getName();
}