package io.github.anthem37.gof23.structural.adapter;

import io.github.anthem37.gof23.common.FrameworkException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 适配器注册表
 * 管理和查找适配器
 *
 * @author anthem37
 * @version 1.0.0
 */
public class AdapterRegistry {

    private static final Logger logger = LoggerFactory.getLogger(AdapterRegistry.class);

    /**
     * 适配器映射表：源类型 -> 目标类型 -> 适配器
     */
    private final Map<String, Map<String, Adapter<?, ?>>> adapters = new ConcurrentHashMap<>();

    /**
     * 注册适配器
     *
     * @param adapter 适配器实例
     * @param <S> 源类型
     * @param <T> 目标类型
     */
    public <S, T> void registerAdapter(Adapter<S, T> adapter) {
        if (adapter == null) {
            throw new FrameworkException("适配器不能为空");
        }

        String sourceKey = adapter.getSourceType().getName();
        String targetKey = adapter.getTargetType().getName();

        adapters.computeIfAbsent(sourceKey, k -> new ConcurrentHashMap<>())
                .put(targetKey, adapter);

        logger.debug("注册适配器: {} -> {} ({})",
                sourceKey, targetKey, adapter.getName());
    }

    /**
     * 查找适配器
     *
     * @param sourceClass 源类型
     * @param targetClass 目标类型
     * @param <S> 源类型
     * @param <T> 目标类型
     * @return 适配器实例，如果未找到返回null
     */
    @SuppressWarnings("unchecked")
    public <S, T> Adapter<S, T> findAdapter(Class<S> sourceClass, Class<T> targetClass) {
        if (sourceClass == null || targetClass == null) {
            return null;
        }

        String sourceKey = sourceClass.getName();
        String targetKey = targetClass.getName();

        Map<String, Adapter<?, ?>> targetAdapters = adapters.get(sourceKey);
        if (targetAdapters != null) {
            Adapter<?, ?> adapter = targetAdapters.get(targetKey);
            if (adapter != null) {
                logger.debug("找到适配器: {} -> {} ({})",
                        sourceKey, targetKey, adapter.getName());
                return (Adapter<S, T>) adapter;
            }
        }

        logger.debug("未找到适配器: {} -> {}", sourceKey, targetKey);
        return null;
    }

    /**
     * 执行适配
     *
     * @param source 源对象
     * @param targetClass 目标类型
     * @param <S> 源类型
     * @param <T> 目标类型
     * @return 适配后的对象
     * @throws FrameworkException 当找不到适配器时
     */
    @SuppressWarnings("unchecked")
    public <S, T> T adapt(S source, Class<T> targetClass) {
        if (source == null) {
            throw new FrameworkException("源对象不能为空");
        }
        if (targetClass == null) {
            throw new FrameworkException("目标类型不能为空");
        }

        Class<S> sourceClass = (Class<S>) source.getClass();
        Adapter<S, T> adapter = findAdapter(sourceClass, targetClass);

        if (adapter == null) {
            throw new FrameworkException(String.format(
                    "未找到适配器: %s -> %s", sourceClass.getName(), targetClass.getName()));
        }

        try {
            T result = adapter.adapt(source);
            logger.debug("适配成功: {} -> {} ({})",
                    sourceClass.getSimpleName(), targetClass.getSimpleName(), adapter.getName());
            return result;
        } catch (Exception e) {
            logger.error("适配失败: {} -> {} ({})",
                    sourceClass.getSimpleName(), targetClass.getSimpleName(), adapter.getName(), e);
            throw new FrameworkException("适配失败", e);
        }
    }

    /**
     * 移除适配器
     *
     * @param sourceClass 源类型
     * @param targetClass 目标类型
     * @return 是否移除成功
     */
    public boolean removeAdapter(Class<?> sourceClass, Class<?> targetClass) {
        if (sourceClass == null || targetClass == null) {
            return false;
        }

        String sourceKey = sourceClass.getName();
        String targetKey = targetClass.getName();

        Map<String, Adapter<?, ?>> targetAdapters = adapters.get(sourceKey);
        if (targetAdapters != null) {
            Adapter<?, ?> removed = targetAdapters.remove(targetKey);
            if (removed != null) {
                logger.debug("移除适配器: {} -> {} ({})",
                        sourceKey, targetKey, removed.getName());

                // 如果该源类型下没有其他适配器，移除整个映射
                if (targetAdapters.isEmpty()) {
                    adapters.remove(sourceKey);
                }
                return true;
            }
        }

        return false;
    }

    /**
     * 获取已注册的适配器数量
     *
     * @return 适配器数量
     */
    public int getAdapterCount() {
        return adapters.values().stream()
                .mapToInt(Map::size)
                .sum();
    }

    /**
     * 清空所有适配器
     */
    public void clear() {
        int count = getAdapterCount();
        adapters.clear();
        logger.debug("清空适配器注册表，移除 {} 个适配器", count);
    }
}