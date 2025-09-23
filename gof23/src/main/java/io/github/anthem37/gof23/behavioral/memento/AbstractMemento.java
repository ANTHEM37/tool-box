package io.github.anthem37.gof23.behavioral.memento;

import java.util.Objects;
import java.util.UUID;

/**
 * 抽象备忘录实现
 * 提供备忘录的基础实现
 *
 * @param <T> 状态类型
 * @author anthem37
 * @version 1.0.0
 */
public abstract class AbstractMemento<T> implements Memento<T> {

    /**
     * 状态
     */
    protected final T state;

    /**
     * 创建时间戳
     */
    protected final long timestamp;

    /**
     * 备忘录标识
     */
    protected final String mementoId;

    /**
     * 构造函数
     *
     * @param state 状态
     */
    protected AbstractMemento(T state) {
        this.state = state;
        this.timestamp = System.currentTimeMillis();
        this.mementoId = UUID.randomUUID().toString();
    }

    /**
     * 构造函数
     *
     * @param state 状态
     * @param mementoId 备忘录标识
     */
    protected AbstractMemento(T state, String mementoId) {
        this.state = state;
        this.timestamp = System.currentTimeMillis();
        this.mementoId = Objects.requireNonNull(mementoId, "备忘录标识不能为空");
    }

    @Override
    public T getState() {
        return state;
    }

    @Override
    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public String getMementoId() {
        return mementoId;
    }

    @Override
    public boolean isValid() {
        return mementoId != null && !mementoId.trim().isEmpty();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        AbstractMemento<?> that = (AbstractMemento<?>) obj;
        return Objects.equals(mementoId, that.mementoId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(mementoId);
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{mementoId='" + mementoId +
                "', timestamp=" + timestamp + "}";
    }
}