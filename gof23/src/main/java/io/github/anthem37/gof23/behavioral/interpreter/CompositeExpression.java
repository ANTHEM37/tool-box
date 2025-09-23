package io.github.anthem37.gof23.behavioral.interpreter;

import io.github.anthem37.gof23.common.FrameworkConstants;
import io.github.anthem37.gof23.common.FrameworkException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * 复合表达式
 * 包含多个子表达式的复合表达式
 *
 * @param <T> 上下文类型
 * @param <R> 解释结果类型
 * @author anthem37
 * @version 1.0.0
 */
public abstract class CompositeExpression<T, R> extends AbstractExpression<T, R> {

    private static final Logger logger = LoggerFactory.getLogger(CompositeExpression.class);

    /**
     * 子表达式列表
     */
    protected final List<Expression<T, R>> subExpressions;

    /**
     * 构造函数
     *
     * @param expressionType 表达式类型
     */
    protected CompositeExpression(String expressionType) {
        super(expressionType);
        this.subExpressions = new ArrayList<>();
    }

    /**
     * 添加子表达式
     *
     * @param expression 子表达式
     * @return 当前表达式（支持链式调用）
     */
    public CompositeExpression<T, R> addExpression(Expression<T, R> expression) {
        if (expression != null) {
            subExpressions.add(expression);
            logger.debug("添加子表达式: {}", expression.getExpressionType());
        }
        return this;
    }

    /**
     * 移除子表达式
     *
     * @param expression 子表达式
     * @return 是否移除成功
     */
    public boolean removeExpression(Expression<T, R> expression) {
        boolean removed = subExpressions.remove(expression);
        if (removed) {
            logger.debug("移除子表达式: {}", expression.getExpressionType());
        }
        return removed;
    }

    /**
     * 获取子表达式列表
     *
     * @return 子表达式列表（只读）
     */
    public List<Expression<T, R>> getSubExpressions() {
        return Collections.unmodifiableList(subExpressions);
    }

    /**
     * 获取子表达式数量
     *
     * @return 子表达式数量
     */
    public int getSubExpressionCount() {
        return subExpressions.size();
    }

    /**
     * 清空所有子表达式
     */
    public void clearExpressions() {
        int count = subExpressions.size();
        subExpressions.clear();
        logger.debug("清空 {} 个子表达式", count);
    }

    @Override
    public boolean isValid() {
        if (!super.isValid()) {
            return false;
        }

        // 检查所有子表达式是否有效
        for (Expression<T, R> expression : subExpressions) {
            if (expression == null || !expression.isValid()) {
                return false;
            }
        }

        return true;
    }

    /**
     * 解释所有子表达式
     *
     * @param context 上下文
     * @return 子表达式结果列表
     * @throws FrameworkException 如果解释失败
     */
    protected List<R> interpretSubExpressions(T context) throws FrameworkException {
        List<R> results = new ArrayList<>();

        for (Expression<T, R> expression : subExpressions) {
            try {
                R result = expression.interpret(context);
                results.add(result);
            } catch (Exception e) {
                logger.error("解释子表达式失败: {}", expression.getExpressionType(), e);
                throw new FrameworkException(FrameworkConstants.ErrorMessages.OPERATION_FAILED, e);
            }
        }

        return results;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!super.equals(obj)) {
            return false;
        }
        CompositeExpression<?, ?> that = (CompositeExpression<?, ?>) obj;
        return Objects.equals(subExpressions, that.subExpressions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subExpressions);
    }
}