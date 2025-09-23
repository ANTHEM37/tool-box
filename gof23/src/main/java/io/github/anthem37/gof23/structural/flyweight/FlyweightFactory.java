package io.github.anthem37.gof23.structural.flyweight;

import io.github.anthem37.gof23.common.FrameworkConstants;
import io.github.anthem37.gof23.common.FrameworkException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.function.Function;

/**
 * 享元工厂
 * 管理享元对象的创建和缓存
 *
 * @param <T> 外部状态类型
 * @param <R> 操作结果类型
 * @author anthem37
 * @version 1.0.0
 */
public class FlyweightFactory<T, R> {

    private static final Logger logger = LoggerFactory.getLogger(FlyweightFactory.class);

    /**
     * 享元缓存（使用LinkedHashMap保持插入顺序）
     */
    private final Map<String, Flyweight<T, R>> flyweights;

    /**
     * 享元创建函数
     */
    private final Function<String, Flyweight<T, R>> flyweightCreator;

    /**
     * 最大缓存大小
     */
    private final int maxCacheSize;

    /**
     * 构造函数
     *
     * @param flyweightCreator 享元创建函数
     */
    public FlyweightFactory(Function<String, Flyweight<T, R>> flyweightCreator) {
        this(flyweightCreator, FrameworkConstants.Default.MAX_CACHE_SIZE);
    }

    /**
     * 构造函数
     *
     * @param flyweightCreator 享元创建函数
     * @param maxCacheSize 最大缓存大小
     */
    public FlyweightFactory(Function<String, Flyweight<T, R>> flyweightCreator, int maxCacheSize) {
        this.flyweightCreator = Objects.requireNonNull(flyweightCreator, "享元创建函数不能为空");
        this.maxCacheSize = Math.max(1, maxCacheSize);
        // 使用同步的LinkedHashMap保持插入顺序，支持LRU
        this.flyweights = Collections.synchronizedMap(new LinkedHashMap<String, Flyweight<T, R>>());

        logger.debug("享元工厂已创建，最大缓存大小: {}", this.maxCacheSize);
    }

    /**
     * 获取享元对象
     *
     * @param intrinsicState 内部状态
     * @return 享元对象
     * @throws FrameworkException 如果创建失败
     */
    public Flyweight<T, R> getFlyweight(String intrinsicState) throws FrameworkException {
        if (intrinsicState == null) {
            throw new FrameworkException(FrameworkConstants.ErrorMessages.INVALID_PARAMETER);
        }

        try {
            synchronized (flyweights) {
                Flyweight<T, R> existing = flyweights.get(intrinsicState);
                if (existing != null) {
                    return existing;
                }

                // 检查缓存大小限制
                if (flyweights.size() >= maxCacheSize) {
                    logger.warn("享元缓存已达到最大大小: {}", maxCacheSize);
                    // LRU策略：移除最旧的元素（LinkedHashMap的第一个元素）
                    String firstKey = flyweights.keySet().iterator().next();
                    flyweights.remove(firstKey);
                    logger.debug("移除享元: {}", firstKey);
                }

                Flyweight<T, R> flyweight = flyweightCreator.apply(intrinsicState);
                flyweights.put(intrinsicState, flyweight);
                logger.debug("创建新享元: {}", intrinsicState);
                return flyweight;
            }
        } catch (Exception e) {
            logger.error("获取享元失败: {}", intrinsicState, e);
            throw new FrameworkException(FrameworkConstants.ErrorMessages.OPERATION_FAILED, e);
        }
    }

    /**
     * 获取缓存的享元数量
     *
     * @return 缓存数量
     */
    public int getCacheSize() {
        return flyweights.size();
    }

    /**
     * 获取所有内部状态
     *
     * @return 内部状态集合
     */
    public Set<String> getIntrinsicStates() {
        return Collections.unmodifiableSet(new HashSet<>(flyweights.keySet()));
    }

    /**
     * 清空缓存
     */
    public void clearCache() {
        int size = flyweights.size();
        flyweights.clear();
        logger.debug("清空享元缓存，移除 {} 个享元", size);
    }

    /**
     * 检查是否包含指定的内部状态
     *
     * @param intrinsicState 内部状态
     * @return 是否包含
     */
    public boolean containsFlyweight(String intrinsicState) {
        return flyweights.containsKey(intrinsicState);
    }
}