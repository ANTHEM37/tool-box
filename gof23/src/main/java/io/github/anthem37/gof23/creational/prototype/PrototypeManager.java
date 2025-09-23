package io.github.anthem37.gof23.creational.prototype;

import io.github.anthem37.gof23.common.FrameworkConstants;
import io.github.anthem37.gof23.common.FrameworkException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 原型管理器
 * 管理和创建原型对象的注册中心
 *
 * @param <T> 原型对象类型
 * @author anthem37
 * @version 1.0.0
 */
public class PrototypeManager<T> {

    private static final Logger logger = LoggerFactory.getLogger(PrototypeManager.class);

    /**
     * 原型对象注册表
     */
    private final Map<String, Prototype<T>> prototypes = new ConcurrentHashMap<>();

    /**
     * 管理器名称
     */
    private final String managerName;

    /**
     * 构造函数
     *
     * @param managerName 管理器名称
     */
    public PrototypeManager(String managerName) {
        if (managerName == null || managerName.trim().isEmpty()) {
            throw new FrameworkException(FrameworkConstants.ErrorMessages.INVALID_PARAMETER);
        }
        this.managerName = managerName;
        logger.debug("创建原型管理器: {}", managerName);
    }

    /**
     * 注册原型
     *
     * @param name 原型名称
     * @param prototype 原型对象
     * @return 当前管理器实例
     */
    public PrototypeManager<T> registerPrototype(String name, Prototype<T> prototype) {
        if (name == null || name.trim().isEmpty()) {
            throw new FrameworkException(FrameworkConstants.ErrorMessages.INVALID_PARAMETER);
        }
        if (prototype == null) {
            throw new FrameworkException("原型对象不能为空");
        }

        prototypes.put(name, prototype);
        logger.debug("注册原型: {} -> {}", name, prototype.getPrototypeName());
        return this;
    }

    /**
     * 移除原型
     *
     * @param name 原型名称
     * @return 是否移除成功
     */
    public boolean removePrototype(String name) {
        if (name == null || name.trim().isEmpty()) {
            return false;
        }

        Prototype<T> removed = prototypes.remove(name);
        if (removed != null) {
            logger.debug("移除原型: {}", name);
            return true;
        }
        return false;
    }

    /**
     * 创建原型克隆
     *
     * @param name 原型名称
     * @return 克隆对象
     */
    public T createClone(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new FrameworkException(FrameworkConstants.ErrorMessages.INVALID_PARAMETER);
        }

        Prototype<T> prototype = prototypes.get(name);
        if (prototype == null) {
            throw new FrameworkException("未找到原型: " + name);
        }

        logger.debug("创建原型克隆: {}", name);
        T cloned = prototype.clone();

        // 验证克隆对象
        if (!prototype.validateClone(cloned)) {
            logger.error("原型克隆验证失败: {}", name);
            throw new FrameworkException("原型克隆验证失败");
        }

        return cloned;
    }

    /**
     * 创建深度克隆
     *
     * @param name 原型名称
     * @return 深度克隆对象
     */
    public T createDeepClone(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new FrameworkException(FrameworkConstants.ErrorMessages.INVALID_PARAMETER);
        }

        Prototype<T> prototype = prototypes.get(name);
        if (prototype == null) {
            throw new FrameworkException("未找到原型: " + name);
        }

        logger.debug("创建深度克隆: {}", name);
        T cloned = prototype.deepClone();

        // 验证克隆对象
        if (!prototype.validateClone(cloned)) {
            logger.error("深度克隆验证失败: {}", name);
            throw new FrameworkException("深度克隆验证失败");
        }

        return cloned;
    }

    /**
     * 检查原型是否存在
     *
     * @param name 原型名称
     * @return 是否存在
     */
    public boolean hasPrototype(String name) {
        return name != null && prototypes.containsKey(name);
    }

    /**
     * 获取所有原型名称
     *
     * @return 原型名称集合
     */
    public Set<String> getPrototypeNames() {
        return Collections.unmodifiableSet(prototypes.keySet());
    }

    /**
     * 获取原型数量
     *
     * @return 原型数量
     */
    public int getPrototypeCount() {
        return prototypes.size();
    }

    /**
     * 清空所有原型
     */
    public void clear() {
        int count = prototypes.size();
        prototypes.clear();
        logger.debug("清空原型管理器: {}, 移除了{}个原型", managerName, count);
    }

    /**
     * 获取管理器名称
     *
     * @return 管理器名称
     */
    public String getManagerName() {
        return managerName;
    }
}