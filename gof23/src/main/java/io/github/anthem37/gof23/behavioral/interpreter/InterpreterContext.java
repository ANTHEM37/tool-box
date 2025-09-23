package io.github.anthem37.gof23.behavioral.interpreter;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 解释器上下文
 * 存储解释过程中的变量和状态
 *
 * @author anthem37
 * @version 1.0.0
 */
public class InterpreterContext {

    /**
     * 变量存储
     */
    private final ConcurrentMap<String, Object> variables;

    /**
     * 构造函数
     */
    public InterpreterContext() {
        this.variables = new ConcurrentHashMap<>();
    }

    /**
     * 设置变量
     *
     * @param name 变量名
     * @param value 变量值
     * @param <T> 变量类型
     * @return 当前上下文（支持链式调用）
     */
    public <T> InterpreterContext setVariable(String name, T value) {
        Objects.requireNonNull(name, "变量名不能为空");
        if (value != null) {
            variables.put(name, value);
        } else {
            variables.remove(name);
        }
        return this;
    }

    /**
     * 获取变量
     *
     * @param name 变量名
     * @param type 变量类型
     * @param <T> 变量类型
     * @return 变量值
     */
    @SuppressWarnings("unchecked")
    public <T> T getVariable(String name, Class<T> type) {
        Objects.requireNonNull(name, "变量名不能为空");
        Objects.requireNonNull(type, "变量类型不能为空");

        Object value = variables.get(name);
        if (value == null) {
            return null;
        }

        if (type.isInstance(value)) {
            return (T) value;
        }

        throw new ClassCastException("变量 " + name + " 的类型不匹配，期望: " + type.getName() +
                ", 实际: " + value.getClass().getName());
    }

    /**
     * 获取变量（不指定类型）
     *
     * @param name 变量名
     * @return 变量值
     */
    public Object getVariable(String name) {
        Objects.requireNonNull(name, "变量名不能为空");
        return variables.get(name);
    }

    /**
     * 检查是否包含变量
     *
     * @param name 变量名
     * @return 是否包含
     */
    public boolean containsVariable(String name) {
        return variables.containsKey(name);
    }

    /**
     * 移除变量
     *
     * @param name 变量名
     * @return 被移除的变量值
     */
    public Object removeVariable(String name) {
        Objects.requireNonNull(name, "变量名不能为空");
        return variables.remove(name);
    }

    /**
     * 获取所有变量名
     *
     * @return 变量名集合
     */
    public Set<String> getVariableNames() {
        return Collections.unmodifiableSet(new HashSet<>(variables.keySet()));
    }

    /**
     * 清空所有变量
     */
    public void clear() {
        variables.clear();
    }

    /**
     * 获取变量数量
     *
     * @return 变量数量
     */
    public int size() {
        return variables.size();
    }

    /**
     * 检查是否为空
     *
     * @return 是否为空
     */
    public boolean isEmpty() {
        return variables.isEmpty();
    }

    /**
     * 复制上下文
     *
     * @return 新的上下文副本
     */
    public InterpreterContext copy() {
        InterpreterContext copy = new InterpreterContext();
        copy.variables.putAll(this.variables);
        return copy;
    }

    @Override
    public String toString() {
        return "InterpreterContext{variables=" + variables + "}";
    }
}