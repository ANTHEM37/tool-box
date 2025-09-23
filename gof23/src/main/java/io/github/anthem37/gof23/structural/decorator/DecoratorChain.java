package io.github.anthem37.gof23.structural.decorator;

import io.github.anthem37.gof23.common.FrameworkException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 装饰器链
 * 管理多个装饰器的执行顺序和组合
 *
 * @param <T> 被装饰对象类型
 * @author anthem37
 * @version 1.0.0
 */
public class DecoratorChain<T> {

    private static final Logger logger = LoggerFactory.getLogger(DecoratorChain.class);

    /**
     * 装饰器列表，使用线程安全的集合
     */
    private final List<Decorator<T>> decorators = new CopyOnWriteArrayList<>();

    /**
     * 添加装饰器
     *
     * @param decorator 装饰器
     * @return 当前链实例，支持链式调用
     */
    public DecoratorChain<T> addDecorator(Decorator<T> decorator) {
        if (decorator == null) {
            throw new FrameworkException("装饰器不能为空");
        }

        decorators.add(decorator);
        // 按优先级排序
        decorators.sort(Comparator.comparingInt(Decorator::getPriority));

        logger.debug("添加装饰器: {} (优先级: {})", decorator.getName(), decorator.getPriority());
        return this;
    }

    /**
     * 移除装饰器
     *
     * @param decoratorName 装饰器名称
     * @return 是否移除成功
     */
    public boolean removeDecorator(String decoratorName) {
        if (decoratorName == null || decoratorName.trim().isEmpty()) {
            return false;
        }

        boolean removed = decorators.removeIf(decorator ->
                decoratorName.equals(decorator.getName()));

        if (removed) {
            logger.debug("移除装饰器: {}", decoratorName);
        }

        return removed;
    }

    /**
     * 执行装饰器链
     * 按优先级顺序依次装饰目标对象
     *
     * @param target 目标对象
     * @return 装饰后的对象
     */
    public T decorate(T target) {
        if (target == null) {
            throw new FrameworkException("目标对象不能为空");
        }

        T result = target;
        Class<?> targetClass = target.getClass();

        for (Decorator<T> decorator : decorators) {
            if (decorator.supports(targetClass)) {
                try {
                    result = decorator.decorate(result);
                    logger.debug("应用装饰器: {} -> {}",
                            decorator.getName(), result.getClass().getSimpleName());
                } catch (Exception e) {
                    logger.error("装饰器执行失败: {}", decorator.getName(), e);
                    throw new FrameworkException("装饰器执行失败: " + decorator.getName(), e);
                }
            } else {
                logger.debug("跳过不支持的装饰器: {} (目标类型: {})",
                        decorator.getName(), targetClass.getSimpleName());
            }
        }

        return result;
    }

    /**
     * 获取装饰器数量
     *
     * @return 装饰器数量
     */
    public int getDecoratorCount() {
        return decorators.size();
    }

    /**
     * 获取所有装饰器名称
     *
     * @return 装饰器名称列表
     */
    public List<String> getDecoratorNames() {
        List<String> names = new ArrayList<>();
        for (Decorator<T> decorator : decorators) {
            names.add(decorator.getName());
        }
        return names;
    }

    /**
     * 清空所有装饰器
     */
    public void clear() {
        int count = decorators.size();
        decorators.clear();
        logger.debug("清空装饰器链，移除 {} 个装饰器", count);
    }
}