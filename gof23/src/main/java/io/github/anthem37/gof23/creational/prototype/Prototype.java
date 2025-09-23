package io.github.anthem37.gof23.creational.prototype;

/**
 * 原型模式接口
 * 定义对象克隆的标准接口
 *
 * @param <T> 原型对象类型
 * @author anthem37
 * @version 1.0.0
 */
public interface Prototype<T> {

    /**
     * 克隆对象
     *
     * @return 克隆后的对象
     */
    T clone();

    /**
     * 深度克隆对象
     *
     * @return 深度克隆后的对象
     */
    T deepClone();

    /**
     * 获取原型名称
     *
     * @return 原型名称
     */
    String getPrototypeName();

    /**
     * 验证克隆对象的有效性
     *
     * @param cloned 克隆对象
     * @return 是否有效
     */
    boolean validateClone(T cloned);
}