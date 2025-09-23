package io.github.anthem37.gof23.behavioral.strategy;

import io.github.anthem37.gof23.common.FrameworkConstants;
import io.github.anthem37.gof23.common.FrameworkException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 策略上下文管理器
 * 负责策略的注册、查找和执行
 * 线程安全的策略管理容器
 *
 * @param <T> 输入参数类型
 * @param <R> 返回结果类型
 * @author anthem37
 * @version 1.0.0
 */
public class StrategyContext<T, R> {

    /**
     * 策略存储容器，使用ConcurrentHashMap保证线程安全
     */
    private final Map<String, Strategy<T, R>> strategies = new ConcurrentHashMap<>();

    /**
     * 注册策略
     *
     * @param strategy 策略实例
     * @throws FrameworkException 当策略为空或标识符为空时抛出
     */
    public void registerStrategy(Strategy<T, R> strategy) {
        if (strategy == null) {
            throw new FrameworkException(FrameworkConstants.ErrorMessages.nullParameter("strategy"));
        }

        String identifier = strategy.getIdentifier();
        if (identifier == null || identifier.trim().isEmpty()) {
            throw new FrameworkException(FrameworkConstants.ErrorMessages.nullParameter("strategy identifier"));
        }

        strategies.put(identifier, strategy);
    }

    /**
     * 执行指定策略
     *
     * @param identifier 策略标识
     * @param input 输入参数
     * @return 执行结果
     * @throws FrameworkException 当策略不存在时抛出
     */
    public R execute(String identifier, T input) {
        if (identifier == null || identifier.trim().isEmpty()) {
            throw new FrameworkException(FrameworkConstants.ErrorMessages.nullParameter("identifier"));
        }

        Strategy<T, R> strategy = strategies.get(identifier);
        if (strategy == null) {
            throw new FrameworkException("策略不存在: " + identifier);
        }

        return strategy.execute(input);
    }

    /**
     * 检查策略是否存在
     *
     * @param identifier 策略标识
     * @return 是否存在
     */
    public boolean hasStrategy(String identifier) {
        return identifier != null && strategies.containsKey(identifier);
    }

    /**
     * 移除策略
     *
     * @param identifier 策略标识
     * @return 被移除的策略，如果不存在则返回null
     */
    public Strategy<T, R> removeStrategy(String identifier) {
        if (identifier == null) {
            return null;
        }
        return strategies.remove(identifier);
    }

    /**
     * 获取已注册策略数量
     *
     * @return 策略数量
     */
    public int getStrategyCount() {
        return strategies.size();
    }

    /**
     * 清空所有策略
     */
    public void clear() {
        strategies.clear();
    }
}