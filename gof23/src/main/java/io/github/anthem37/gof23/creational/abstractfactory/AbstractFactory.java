package io.github.anthem37.gof23.creational.abstractfactory;

import io.github.anthem37.gof23.common.FrameworkException;

/**
 * 抽象工厂接口
 * 定义创建一系列相关产品的接口
 *
 * @param <T> 产品族类型
 * @author anthem37
 * @version 1.0.0
 */
public interface AbstractFactory<T> {

    /**
     * 创建产品A
     *
     * @param type 产品类型
     * @param <A> 产品A类型
     * @return 产品A实例
     * @throws FrameworkException 如果创建失败
     */
    <A extends T> A createProductA(Class<A> type) throws FrameworkException;

    /**
     * 创建产品B
     *
     * @param type 产品类型
     * @param <B> 产品B类型
     * @return 产品B实例
     * @throws FrameworkException 如果创建失败
     */
    <B extends T> B createProductB(Class<B> type) throws FrameworkException;

    /**
     * 获取工厂类型
     *
     * @return 工厂类型
     */
    String getFactoryType();

    /**
     * 检查是否支持指定的产品类型
     *
     * @param productType 产品类型
     * @return 是否支持
     */
    boolean supports(Class<?> productType);

    /**
     * 获取支持的产品族
     *
     * @return 产品族类型
     */
    Class<T> getProductFamily();
}