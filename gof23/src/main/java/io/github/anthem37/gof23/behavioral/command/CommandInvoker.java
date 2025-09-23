package io.github.anthem37.gof23.behavioral.command;

import io.github.anthem37.gof23.common.FrameworkException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Stack;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 命令调用器
 * 负责执行命令和管理命令历史
 *
 * @author anthem37
 * @version 1.0.0
 */
public class CommandInvoker {

    private static final Logger logger = LoggerFactory.getLogger(CommandInvoker.class);

    /**
     * 命令历史栈
     */
    private final Stack<Command> commandHistory = new Stack<>();

    /**
     * 异步执行器
     */
    private final ExecutorService executor;

    /**
     * 最大历史记录数
     */
    private final int maxHistorySize;

    /**
     * 构造函数
     *
     * @param maxHistorySize 最大历史记录数
     */
    public CommandInvoker(int maxHistorySize) {
        if (maxHistorySize <= 0) {
            throw new FrameworkException("最大历史记录数必须大于0");
        }

        this.maxHistorySize = maxHistorySize;
        this.executor = Executors.newCachedThreadPool(r -> {
            Thread thread = new Thread(r, "CommandInvoker-" + System.currentTimeMillis());
            thread.setDaemon(true);
            return thread;
        });
    }

    /**
     * 默认构造函数
     * 使用默认的最大历史记录数100
     */
    public CommandInvoker() {
        this(100);
    }

    /**
     * 同步执行命令
     *
     * @param command 命令
     * @return 执行结果
     */
    public Object execute(Command command) {
        if (command == null) {
            throw new FrameworkException("命令不能为空");
        }

        if (!command.validate()) {
            throw new FrameworkException("命令验证失败: " + command.getName());
        }

        try {
            logger.debug("执行命令: {} - {}", command.getName(), command.getDescription());

            Object result = command.execute();

            // 如果命令可撤销，添加到历史记录
            if (command.isUndoable()) {
                addToHistory(command);
            }

            logger.debug("命令执行成功: {}", command.getName());
            return result;

        } catch (Exception e) {
            logger.error("命令执行失败: {}", command.getName(), e);
            throw new FrameworkException("命令执行失败: " + command.getName(), e);
        }
    }

    /**
     * 异步执行命令
     *
     * @param command 命令
     * @return 异步执行结果
     */
    public CompletableFuture<Object> executeAsync(Command command) {
        return CompletableFuture.supplyAsync(() -> execute(command), executor);
    }

    /**
     * 撤销上一个命令
     *
     * @return 撤销结果
     */
    public Object undo() {
        if (commandHistory.isEmpty()) {
            throw new FrameworkException("没有可撤销的命令");
        }

        Command command = commandHistory.pop();

        try {
            logger.debug("撤销命令: {}", command.getName());

            Object result = command.undo();

            logger.debug("命令撤销成功: {}", command.getName());
            return result;

        } catch (Exception e) {
            logger.error("命令撤销失败: {}", command.getName(), e);
            // 撤销失败时，将命令重新放回历史记录
            commandHistory.push(command);
            throw new FrameworkException("命令撤销失败: " + command.getName(), e);
        }
    }

    /**
     * 添加命令到历史记录
     *
     * @param command 命令
     */
    private void addToHistory(Command command) {
        // 如果历史记录已满，移除最旧的记录
        if (commandHistory.size() >= maxHistorySize) {
            commandHistory.remove(0);
        }

        commandHistory.push(command);
        logger.debug("添加命令到历史记录: {} (历史记录数: {})",
                command.getName(), commandHistory.size());
    }

    /**
     * 清空命令历史
     */
    public void clearHistory() {
        int count = commandHistory.size();
        commandHistory.clear();
        logger.debug("清空命令历史，移除 {} 个命令", count);
    }

    /**
     * 获取历史记录数量
     *
     * @return 历史记录数量
     */
    public int getHistorySize() {
        return commandHistory.size();
    }

    /**
     * 检查是否有可撤销的命令
     *
     * @return 是否有可撤销的命令
     */
    public boolean canUndo() {
        return !commandHistory.isEmpty();
    }

    /**
     * 关闭调用器
     * 释放资源
     */
    public void shutdown() {
        executor.shutdown();
        clearHistory();
        logger.debug("命令调用器已关闭");
    }
}