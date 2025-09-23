package io.github.anthem37.gof23.creational.builder;

/**
 * 建造者接口
 * 定义建造者的基本行为规范
 *
 * @param <T> 要构建的对象类型
 * @author anthem37
 * @version 1.0.0
 */
public interface Builder<T> {

    /**
     * 构建对象
     *
     * @return 构建完成的对象
     */
    T build();

    /**
     * 重置建造者状态
     * 清空所有已设置的属性，准备构建新对象
     *
     * @return 建造者实例，支持链式调用
     */
    Builder<T> reset();
}