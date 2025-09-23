package io.github.anthem37.gof23.behavioral.command;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * 标准命令模式行为验证测试
 */
public class StandardCommandPatternTest {

    @Test
    public void testStandardCommandPattern() {
        // 创建命令
        SimpleCommand command = new SimpleCommand("打开文件");
        
        // 创建调用者
        CommandInvoker<String> invoker = new CommandInvoker<>();
        
        // 执行命令
        String result = invoker.execute(command);
        
        // 验证结果
        assertEquals("执行动作: 打开文件", result);
    }
    
    @Test
    public void testInvokerKnowsOnlyCommandInterface() {
        // 验证调用者只认识命令接口，不认识具体实现
        Command<String> command = new SimpleCommand("保存文件");
        CommandInvoker<String> invoker = new CommandInvoker<>();
        
        String result = invoker.execute(command);
        
        assertEquals("执行动作: 保存文件", result);
    }
}