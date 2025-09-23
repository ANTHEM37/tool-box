package io.github.anthem37.gof23.creational.singleton;

import io.github.anthem37.gof23.common.FrameworkException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.locks.ReentrantLock;
import java.util.function.Supplier;

/**
 * 线程安全的单例实现
 * 使用双重检查锁定模式确保线程安全
 *
 * @param <T> 单例类型
 * @author anthem37
 * @version 1.0.0
 */
public final class ThreadSafeSingleton<T extends Singleton> {

    private static final Logger logger = LoggerFactory.getLogger(ThreadSafeSingleton.class);
    private final ReentrantLock lock = new ReentrantLock();
    private final Supplier<T> instanceSupplier;
    private final String instanceName;
    private volatile T instance;

    /**
     * 构造函数
     *
     * @param instanceSupplier 实例创建供应商
     * @param instanceName 实例名称
     */
    public ThreadSafeSingleton(Supplier<T> instanceSupplier, String instanceName) {
        if (instanceSupplier == null) {
            throw new FrameworkException("实例供应商不能为空");
        }
        if (instanceName == null || instanceName.trim().isEmpty()) {
            throw new FrameworkException("实例名称不能为空");
        }

        this.instanceSupplier = instanceSupplier;
        this.instanceName = instanceName;
    }

    /**
     * 获取单例实例
     * 使用双重检查锁定模式
     *
     * @return 单例实例
     */
    public T getInstance() {
        if (instance == null) {
            lock.lock();
            try {
                if (instance == null) {
                    instance = createInstance();
                }
            } finally {
                lock.unlock();
            }
        }
        return instance;
    }

    /**
     * 创建实例
     *
     * @return 新实例
     */
    private T createInstance() {
        try {
            T newInstance = instanceSupplier.get();
            if (newInstance == null) {
                throw new FrameworkException("实例供应商返回null");
            }

            newInstance.initialize();
            logger.debug("创建单例实例: {}", instanceName);
            return newInstance;
        } catch (Exception e) {
            logger.error("创建单例实例失败: {}", instanceName, e);
            throw new FrameworkException("创建单例实例失败: " + instanceName, e);
        }
    }

    /**
     * 销毁单例实例
     */
    public void destroyInstance() {
        if (instance != null) {
            lock.lock();
            try {
                if (instance != null) {
                    instance.destroy();
                    instance = null;
                    logger.debug("销毁单例实例: {}", instanceName);
                }
            } finally {
                lock.unlock();
            }
        }
    }

    /**
     * 检查实例是否存在
     *
     * @return 是否存在实例
     */
    public boolean hasInstance() {
        return instance != null;
    }

    /**
     * 获取实例名称
     *
     * @return 实例名称
     */
    public String getInstanceName() {
        return instanceName;
    }
}