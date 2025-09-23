package io.github.anthem37.gof23.behavioral.interpreter;

import java.util.Objects;

/**
 * 抽象表达式实现
 * 提供表达式的基础实现
 *
 * @param <T> 上下文类型
 * @param <R> 解释结果类型
 * @author anthem37
 * @version 1.0.0
 */
public abstract class AbstractExpression<T, R> implements Expression<T, R> {

    /**
     * 表达式类型
     */
    protected final String expressionType;

    /**
     * 构造函数
     *
     * @param expressionType 表达式类型
     */
    protected AbstractExpression(String expressionType) {
        this.expressionType = Objects.requireNonNull(expressionType, "表达式类型不能为空");
    }

    @Override
    public String getExpressionType() {
        return expressionType;
    }

    @Override
    public boolean supports(Class<?> contextType) {
        return contextType != null;
    }

    @Override
    public boolean isValid() {
        return expressionType != null && !expressionType.trim().isEmpty();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        AbstractExpression<?, ?> that = (AbstractExpression<?, ?>) obj;
        return Objects.equals(expressionType, that.expressionType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(expressionType);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{expressionType='" + expressionType + "'}";
    }
}