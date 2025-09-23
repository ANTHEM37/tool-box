package io.github.anthem37.gof23.behavioral.template;

import io.github.anthem37.gof23.common.FrameworkConstants;
import io.github.anthem37.gof23.common.FrameworkException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 抽象模板类
 * 定义算法的骨架，具体步骤由子类实现
 * 提供通用的执行流程和异常处理
 *
 * @param <T> 输入参数类型
 * @param <R> 返回结果类型
 * @author anthem37
 * @version 1.0.0
 */
public abstract class AbstractTemplate<T, R> {

    private static final Logger logger = LoggerFactory.getLogger(AbstractTemplate.class);

    /**
     * 模板方法，定义算法骨架
     * 这是一个final方法，子类不能重写
     *
     * @param input 输入参数
     * @return 执行结果
     * @throws FrameworkException 当执行过程中发生异常时抛出
     */
    public final R execute(T input) {
        if (input == null) {
            throw new FrameworkException(FrameworkConstants.ErrorMessages.NULL_PARAMETER + ": input");
        }

        String templateName = this.getClass().getSimpleName();
        logger.debug("开始执行模板: {}", templateName);

        try {
            // 前置处理
            preProcess(input);

            // 参数验证
            validate(input);

            // 核心处理
            R result = doExecute(input);

            // 后置处理
            postProcess(input, result);

            logger.debug("模板执行完成: {}", templateName);
            return result;

        } catch (Exception e) {
            // 异常处理
            handleException(input, e);

            if (e instanceof FrameworkException) {
                throw e;
            }
            throw new FrameworkException(FrameworkConstants.ErrorMessages.EXECUTION_FAILED + ": " + templateName, e);
        }
    }

    /**
     * 前置处理
     * 在核心处理之前执行，可用于初始化、日志记录等
     * 子类可以重写此方法
     *
     * @param input 输入参数
     */
    protected void preProcess(T input) {
        // 默认不做任何处理
    }

    /**
     * 参数验证
     * 验证输入参数的有效性
     * 子类可以重写此方法来实现自定义验证逻辑
     *
     * @param input 输入参数
     * @throws FrameworkException 当验证失败时抛出
     */
    protected void validate(T input) {
        // 默认不做验证
    }

    /**
     * 核心处理逻辑
     * 子类必须实现此方法
     *
     * @param input 输入参数
     * @return 处理结果
     */
    protected abstract R doExecute(T input);

    /**
     * 后置处理
     * 在核心处理之后执行，可用于清理资源、日志记录等
     * 子类可以重写此方法
     *
     * @param input 输入参数
     * @param result 处理结果
     */
    protected void postProcess(T input, R result) {
        // 默认不做任何处理
    }

    /**
     * 异常处理
     * 当执行过程中发生异常时调用
     * 子类可以重写此方法来实现自定义异常处理逻辑
     *
     * @param input 输入参数
     * @param exception 发生的异常
     */
    protected void handleException(T input, Exception exception) {
        logger.error("模板执行异常: {}", this.getClass().getSimpleName(), exception);
    }

    /**
     * 获取模板名称
     * 用于日志记录和异常信息
     *
     * @return 模板名称
     */
    protected String getTemplateName() {
        return this.getClass().getSimpleName();
    }
}