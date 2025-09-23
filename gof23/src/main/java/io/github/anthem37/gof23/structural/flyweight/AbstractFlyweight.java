package io.github.anthem37.gof23.structural.flyweight;

import java.util.Objects;

/**
 * 抽象享元实现
 * 提供享元的基础实现
 *
 * @param <T> 外部状态类型
 * @param <R> 操作结果类型
 * @author anthem37
 * @version 1.0.0
 */
public abstract class AbstractFlyweight<T, R> implements Flyweight<T, R> {

    /**
     * 内部状态
     */
    protected final String intrinsicState;

    /**
     * 构造函数
     *
     * @param intrinsicState 内部状态
     */
    protected AbstractFlyweight(String intrinsicState) {
        this.intrinsicState = Objects.requireNonNull(intrinsicState, "内部状态不能为空");
    }

    @Override
    public String getIntrinsicState() {
        return intrinsicState;
    }

    @Override
    public boolean supports(Class<?> stateType) {
        return stateType != null;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        AbstractFlyweight<?, ?> that = (AbstractFlyweight<?, ?>) obj;
        return Objects.equals(intrinsicState, that.intrinsicState);
    }

    @Override
    public int hashCode() {
        return Objects.hash(intrinsicState);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{intrinsicState='" + intrinsicState + "'}";
    }
}