package io.github.anthem37.gof23.creational.factory;

import io.github.anthem37.gof23.common.FrameworkException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

/**
 * 抽象工厂实现
 * 提供工厂的基础实现和产品创建器管理
 *
 * @param <T> 产品类型
 * @author anthem37
 * @version 1.0.0
 */
public abstract class AbstractFactory<T> implements Factory<T> {

    private static final Logger logger = LoggerFactory.getLogger(AbstractFactory.class);

    /**
     * 产品创建器映射表
     */
    private final Map<String, Function<Object[], T>> creators = new ConcurrentHashMap<>();

    /**
     * 注册产品创建器
     *
     * @param type 产品类型
     * @param creator 创建器函数
     */
    protected void registerCreator(String type, Function<Object[], T> creator) {
        if (type == null || type.trim().isEmpty()) {
            throw new FrameworkException("产品类型不能为空");
        }
        if (creator == null) {
            throw new FrameworkException("创建器不能为空");
        }

        creators.put(type, creator);
        logger.debug("注册产品创建器: {}", type);
    }

    @Override
    public T create(String type, Object... params) {
        if (!supports(type)) {
            throw new IllegalArgumentException("不支持的产品类型: " + type);
        }

        try {
            Function<Object[], T> creator = creators.get(type);
            T product = creator.apply(params);
            logger.debug("创建产品实例: {} -> {}", type, product.getClass().getSimpleName());
            return product;
        } catch (Exception e) {
            logger.error("创建产品失败: {}", type, e);
            throw new FrameworkException("创建产品失败: " + type, e);
        }
    }

    @Override
    public boolean supports(String type) {
        return type != null && creators.containsKey(type);
    }

    @Override
    public Set<String> getSupportedTypes() {
        return new java.util.HashSet<>(creators.keySet());
    }

    /**
     * 获取已注册的创建器数量
     *
     * @return 创建器数量
     */
    public int getCreatorCount() {
        return creators.size();
    }
}