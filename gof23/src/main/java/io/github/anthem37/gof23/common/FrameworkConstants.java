package io.github.anthem37.gof23.common;

/**
 * 框架常量定义
 * 统一管理框架中使用的常量，避免魔法数字和字符串
 *
 * @author anthem37
 * @version 1.0.0
 */
public final class FrameworkConstants {

    /**
     * 私有构造函数，防止实例化
     */
    private FrameworkConstants() {
        throw new UnsupportedOperationException("常量类不允许实例化");
    }

    /**
     * 默认配置常量
     */
    public static final class Default {
        /** 默认缓存大小 */
        public static final int CACHE_SIZE = 100;

        /** 默认超时时间(毫秒) */
        public static final long TIMEOUT_MS = 5000L;

        /** 默认重试次数 */
        public static final int RETRY_COUNT = 3;

        /** 默认最大缓存大小 */
        public static final int MAX_CACHE_SIZE = 1000;
    }

    /**
     * 错误消息常量
     */
    public static final class ErrorMessages {
        /** 参数无效 */
        public static final String INVALID_PARAMETER = "参数无效";

        /** 空参数错误 */
        public static final String NULL_PARAMETER = "参数不能为空";

        /** 初始化失败 */
        public static final String INIT_FAILED = "初始化失败";

        /** 执行失败 */
        public static final String EXECUTION_FAILED = "执行失败";

        /** 不支持的操作 */
        public static final String UNSUPPORTED_OPERATION = "不支持的操作";

        /** 操作失败 */
        public static final String OPERATION_FAILED = "操作失败";

        /** 资源未找到 */
        public static final String RESOURCE_NOT_FOUND = "资源未找到";

        /** 验证失败 */
        public static final String VALIDATION_FAILED = "验证失败";
    }
}