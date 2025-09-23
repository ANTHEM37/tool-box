package io.github.anthem37.gof23.behavioral.chain;

import io.github.anthem37.gof23.common.FrameworkConstants;
import io.github.anthem37.gof23.common.FrameworkException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 责任链管理器
 * 管理处理器的注册、排序和执行
 * 线程安全的责任链实现
 *
 * @param <T> 请求类型
 * @param <R> 响应类型
 * @author anthem37
 * @version 1.0.0
 */
public class HandlerChain<T, R> {

    /**
     * 处理器列表，使用CopyOnWriteArrayList保证线程安全
     */
    private final List<Handler<T, R>> handlers = new CopyOnWriteArrayList<>();

    /**
     * 是否已排序
     */
    private volatile boolean sorted = true;

    /**
     * 添加处理器
     *
     * @param handler 处理器实例
     * @throws FrameworkException 当处理器为空时抛出
     */
    public void addHandler(Handler<T, R> handler) {
        if (handler == null) {
            throw new FrameworkException(FrameworkConstants.ErrorMessages.nullParameter("handler"));
        }

        handlers.add(handler);
        sorted = false;
    }

    /**
     * 移除处理器
     *
     * @param handler 要移除的处理器
     * @return 是否成功移除
     */
    public boolean removeHandler(Handler<T, R> handler) {
        return handlers.remove(handler);
    }

    /**
     * 处理请求
     * 按优先级顺序遍历处理器，找到第一个能处理的处理器执行
     *
     * @param request 请求对象
     * @return 处理结果，如果没有处理器能处理则返回null
     * @throws FrameworkException 当请求为空时抛出
     */
    public R handle(T request) {
        if (request == null) {
            throw new FrameworkException(FrameworkConstants.ErrorMessages.nullParameter("request"));
        }

        ensureSorted();

        for (Handler<T, R> handler : handlers) {
            if (handler.canHandle(request)) {
                return handler.handle(request);
            }
        }

        return null;
    }

    /**
     * 处理请求（所有能处理的处理器都会执行）
     * 返回所有处理结果的列表
     *
     * @param request 请求对象
     * @return 所有处理结果的列表
     * @throws FrameworkException 当请求为空时抛出
     */
    public List<R> handleAll(T request) {
        if (request == null) {
            throw new FrameworkException(FrameworkConstants.ErrorMessages.nullParameter("request"));
        }

        ensureSorted();

        // 延迟初始化，只有在有结果时才创建列表
        List<R> results = null;
        for (Handler<T, R> handler : handlers) {
            if (handler.canHandle(request)) {
                R result = handler.handle(request);
                if (result != null) {
                    if (results == null) {
                        results = new ArrayList<>();
                    }
                    results.add(result);
                }
            }
        }

        return results == null ? Collections.emptyList() : results;
    }

    /**
     * 获取处理器数量
     *
     * @return 处理器数量
     */
    public int getHandlerCount() {
        return handlers.size();
    }

    /**
     * 清空所有处理器
     */
    public void clear() {
        handlers.clear();
        sorted = true;
    }

    /**
     * 确保处理器列表已按优先级排序
     */
    private void ensureSorted() {
        if (!sorted) {
            synchronized (this) {
                if (!sorted) {
                    // 直接对handlers进行排序，避免创建新的ArrayList
                    handlers.sort(Comparator.comparingInt(Handler::getPriority));
                    sorted = true;
                }
            }
        }
    }

    /**
     * 获取所有处理器的只读副本
     *
     * @return 处理器列表的只读副本
     */
    public List<Handler<T, R>> getHandlers() {
        ensureSorted();
        return Collections.unmodifiableList(new ArrayList<>(handlers));
    }
}