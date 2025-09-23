package io.github.anthem37.gof23.behavioral.command;

/**
 * 命令接口
 * 定义命令的标准契约
 *
 * @param <T> 命令执行结果的类型
 * @author anthem37
 * @version 1.0.0
 */
public interface Command<T> {

    /**
     * 执行命令
     *
     * @return 执行结果
     */
    T execute();

    /**
     * 撤销命令
     *
     * @return 撤销结果
     */
    default T undo() {
        throw new UnsupportedOperationException("该命令不支持撤销操作");
    }

    /**
     * 检查命令是否可撤销
     *
     * @return 是否可撤销
     */
    default boolean isUndoable() {
        return false;
    }

    /**
     * 获取命令名称
     *
     * @return 命令名称
     */
    String getName();

    /**
     * 获取命令描述
     *
     * @return 命令描述
     */
    default String getDescription() {
        return getName();
    }

    /**
     * 验证命令是否可执行
     *
     * @return 是否可执行
     */
    default boolean validate() {
        return true;
    }
}