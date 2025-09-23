package io.github.anthem37.gof23.behavioral.observer;

import io.github.anthem37.gof23.common.FrameworkConstants;
import io.github.anthem37.gof23.common.FrameworkException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.ForkJoinPool;

/**
 * 事件发布器
 * 管理观察者的注册和事件的发布
 * 支持同步和异步事件发布
 *
 * @param <T> 事件数据类型
 * @author anthem37
 * @version 1.0.0
 */
public class EventPublisher<T> {

    private static final Logger logger = LoggerFactory.getLogger(EventPublisher.class);

    /**
     * 观察者列表，使用CopyOnWriteArrayList保证线程安全
     */
    private final List<Observer<T>> observers = new CopyOnWriteArrayList<>();

    /**
     * 异步执行器
     */
    private final Executor executor;

    /**
     * 构造函数，使用默认的ForkJoinPool作为异步执行器
     */
    public EventPublisher() {
        this(ForkJoinPool.commonPool());
    }

    /**
     * 构造函数
     *
     * @param executor 异步执行器
     */
    public EventPublisher(Executor executor) {
        this.executor = executor != null ? executor : ForkJoinPool.commonPool();
    }

    /**
     * 注册观察者
     *
     * @param observer 观察者实例
     * @throws FrameworkException 当观察者为空时抛出
     */
    public void subscribe(Observer<T> observer) {
        if (observer == null) {
            throw new FrameworkException(FrameworkConstants.ErrorMessages.nullParameter("observer"));
        }

        if (!observers.contains(observer)) {
            observers.add(observer);
            logger.debug("观察者已注册: {}", observer.getIdentifier());
        }
    }

    /**
     * 取消注册观察者
     *
     * @param observer 要取消注册的观察者
     * @return 是否成功取消注册
     */
    public boolean unsubscribe(Observer<T> observer) {
        boolean removed = observers.remove(observer);
        if (removed && logger.isDebugEnabled()) {
            logger.debug("观察者已取消注册: {}", observer != null ? observer.getIdentifier() : "null");
        }
        return removed;
    }

    /**
     * 同步发布事件
     * 按注册顺序通知所有观察者
     *
     * @param event 事件数据
     * @throws FrameworkException 当事件为空时抛出
     */
    public void publish(T event) {
        if (event == null) {
            throw new FrameworkException(FrameworkConstants.ErrorMessages.nullParameter("event"));
        }

        logger.debug("开始同步发布事件: {}", event.getClass().getSimpleName());

        for (Observer<T> observer : observers) {
            try {
                observer.onEvent(event);
            } catch (Exception e) {
                logger.error("观察者处理事件时发生异常: {}", observer.getIdentifier(), e);
                // 继续通知其他观察者，不因为一个观察者的异常而中断
            }
        }

        logger.debug("同步事件发布完成，通知了 {} 个观察者", observers.size());
    }

    /**
     * 异步发布事件
     * 每个观察者在独立的线程中接收通知
     *
     * @param event 事件数据
     * @return CompletableFuture，可用于等待所有观察者处理完成
     * @throws FrameworkException 当事件为空时抛出
     */
    public CompletableFuture<Void> publishAsync(T event) {
        if (event == null) {
            throw new FrameworkException(FrameworkConstants.ErrorMessages.nullParameter("event"));
        }

        logger.debug("开始异步发布事件: {}", event.getClass().getSimpleName());

        CompletableFuture<?>[] futures = observers.stream()
                .map(observer -> CompletableFuture.runAsync(() -> {
                    try {
                        observer.onEvent(event);
                    } catch (Exception e) {
                        logger.error("观察者处理事件时发生异常: {}", observer.getIdentifier(), e);
                    }
                }, executor))
                .toArray(CompletableFuture[]::new);

        return CompletableFuture.allOf(futures)
                .whenComplete((result, throwable) -> {
                    if (throwable == null) {
                        logger.debug("异步事件发布完成，通知了 {} 个观察者", observers.size());
                    } else {
                        logger.error("异步事件发布过程中发生异常", throwable);
                    }
                });
    }

    /**
     * 获取观察者数量
     *
     * @return 观察者数量
     */
    public int getObserverCount() {
        return observers.size();
    }

    /**
     * 清空所有观察者
     */
    public void clear() {
        int count = observers.size();
        observers.clear();
        logger.debug("已清空所有观察者，共 {} 个", count);
    }

    /**
     * 检查是否有观察者
     *
     * @return 是否有观察者
     */
    public boolean hasObservers() {
        return !observers.isEmpty();
    }
}