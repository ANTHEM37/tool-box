package io.github.anthem37.gof23.behavioral.interpreter;

import io.github.anthem37.gof23.common.FrameworkException;

/**
 * 表达式接口
 * 定义解释器的基本操作
 *
 * @param <T> 上下文类型
 * @param <R> 解释结果类型
 * @author anthem37
 * @version 1.0.0
 */
public interface Expression<T, R> {

    /**
     * 解释表达式
     *
     * @param context 上下文
     * @return 解释结果
     * @throws FrameworkException 如果解释失败
     */
    R interpret(T context) throws FrameworkException;

    /**
     * 获取表达式类型
     *
     * @return 表达式类型
     */
    String getExpressionType();

    /**
     * 检查是否支持指定的上下文类型
     *
     * @param contextType 上下文类型
     * @return 是否支持
     */
    boolean supports(Class<?> contextType);

    /**
     * 验证表达式的有效性
     *
     * @return 是否有效
     */
    boolean isValid();
}