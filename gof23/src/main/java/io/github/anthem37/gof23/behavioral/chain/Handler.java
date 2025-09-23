package io.github.anthem37.gof23.behavioral.chain;

/**
 * 责任链处理器接口
 * 定义责任链中每个处理器的基本行为
 *
 * @param <T> 请求类型
 * @param <R> 响应类型
 * @author anthem37
 * @version 1.0.0
 */
public interface Handler<T, R> {

    /**
     * 处理请求
     *
     * @param request 请求对象
     * @return 处理结果，如果无法处理则返回null
     */
    R handle(T request);

    /**
     * 判断是否能够处理该请求
     *
     * @param request 请求对象
     * @return 是否能处理
     */
    boolean canHandle(T request);

    /**
     * 获取处理器优先级
     * 数值越小优先级越高
     *
     * @return 优先级
     */
    default int getPriority() {
        return Integer.MAX_VALUE;
    }
}