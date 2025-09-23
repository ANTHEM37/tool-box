package io.github.anthem37.gof23.structural.proxy;

import io.github.anthem37.gof23.common.FrameworkException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * 代理工厂
 * 使用JDK动态代理创建代理对象
 *
 * @author anthem37
 * @version 1.0.0
 */
public class ProxyFactory {

    private static final Logger logger = LoggerFactory.getLogger(ProxyFactory.class);

    /**
     * 创建代理对象
     *
     * @param target 目标对象
     * @param proxy 代理处理器
     * @param <T> 目标对象类型
     * @return 代理对象
     */
    @SuppressWarnings("unchecked")
    public static <T> T createProxy(T target, io.github.anthem37.gof23.structural.proxy.Proxy<T> proxy) {
        if (target == null) {
            throw new FrameworkException("目标对象不能为空");
        }
        if (proxy == null) {
            throw new FrameworkException("代理处理器不能为空");
        }

        Class<?> targetClass = target.getClass();
        Class<?>[] interfaces = targetClass.getInterfaces();

        if (interfaces.length == 0) {
            throw new FrameworkException("目标对象必须实现至少一个接口");
        }

        proxy.setTarget(target);

        InvocationHandler handler = new ProxyInvocationHandler<>(proxy);

        T proxyInstance = (T) Proxy.newProxyInstance(
                targetClass.getClassLoader(),
                interfaces,
                handler
        );

        logger.debug("创建代理对象: {} ({})", targetClass.getSimpleName(), proxy.getName());
        return proxyInstance;
    }

    /**
     * 代理调用处理器
     *
     * @param <T> 目标对象类型
     */
    private static class ProxyInvocationHandler<T> implements InvocationHandler {

        private final io.github.anthem37.gof23.structural.proxy.Proxy<T> proxy;

        public ProxyInvocationHandler(io.github.anthem37.gof23.structural.proxy.Proxy<T> proxy) {
            this.proxy = proxy;
        }

        @Override
        public Object invoke(Object proxyObj, Method method, Object[] args) throws Throwable {
            String methodName = method.getName();
            Object[] safeArgs = args != null ? args : new Object[0];

            logger.debug("代理调用: {}.{} ({})",
                    proxy.getTarget().getClass().getSimpleName(), methodName, proxy.getName());

            // 前置处理
            if (!proxy.beforeInvoke(methodName, safeArgs)) {
                logger.debug("前置处理阻止方法执行: {}", methodName);
                return null;
            }

            try {
                // 调用目标方法
                Object result = method.invoke(proxy.getTarget(), args);

                // 后置处理
                result = proxy.afterInvoke(methodName, safeArgs, result);

                logger.debug("代理调用成功: {}.{}",
                        proxy.getTarget().getClass().getSimpleName(), methodName);
                return result;

            } catch (Exception e) {
                logger.error("代理调用异常: {}.{}",
                        proxy.getTarget().getClass().getSimpleName(), methodName, e);

                // 异常处理
                proxy.onException(methodName, safeArgs, e);
                throw e;
            }
        }
    }
}