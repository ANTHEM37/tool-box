package io.github.anthem37.gof23.creational.factory;

import java.util.Set;

/**
 * 工厂接口
 * 定义创建对象的标准契约
 *
 * @param <T> 产品类型
 * @author anthem37
 * @version 1.0.0
 */
public interface Factory<T> {

    /**
     * 创建产品实例
     *
     * @param type 产品类型标识
     * @param params 创建参数
     * @return 产品实例
     * @throws IllegalArgumentException 当类型不支持时
     */
    T create(String type, Object... params);

    /**
     * 检查是否支持指定类型
     *
     * @param type 产品类型标识
     * @return 是否支持
     */
    boolean supports(String type);

    /**
     * 获取支持的所有类型
     *
     * @return 支持的类型集合
     */
    Set<String> getSupportedTypes();
}