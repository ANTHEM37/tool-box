package io.github.anthem37.gof23.structural.proxy;

/**
 * 代理接口
 * 定义代理的标准契约
 *
 * @param <T> 被代理对象类型
 * @author anthem37
 * @version 1.0.0
 */
public interface Proxy<T> {

    /**
     * 获取被代理的目标对象
     *
     * @return 目标对象
     */
    T getTarget();

    /**
     * 设置被代理的目标对象
     *
     * @param target 目标对象
     */
    void setTarget(T target);

    /**
     * 执行前置处理
     * 在调用目标方法前执行
     *
     * @param methodName 方法名
     * @param args 方法参数
     * @return 是否继续执行目标方法
     */
    default boolean beforeInvoke(String methodName, Object[] args) {
        return true;
    }

    /**
     * 执行后置处理
     * 在调用目标方法后执行
     *
     * @param methodName 方法名
     * @param args 方法参数
     * @param result 方法返回值
     * @return 处理后的返回值
     */
    default Object afterInvoke(String methodName, Object[] args, Object result) {
        return result;
    }

    /**
     * 异常处理
     * 在目标方法抛出异常时执行
     *
     * @param methodName 方法名
     * @param args 方法参数
     * @param exception 异常
     * @throws Exception 处理后的异常
     */
    default void onException(String methodName, Object[] args, Exception exception) throws Exception {
        throw exception;
    }

    /**
     * 获取代理名称
     *
     * @return 代理名称
     */
    String getName();
}