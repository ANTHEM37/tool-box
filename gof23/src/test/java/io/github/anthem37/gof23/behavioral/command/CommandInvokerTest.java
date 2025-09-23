package io.github.anthem37.gof23.behavioral.command;

import io.github.anthem37.gof23.common.FrameworkException;
import org.junit.Before;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.*;

/**
 * CommandInvoker泛型化实现测试
 */
public class CommandInvokerTest {

    private CommandInvoker<String> stringCommandInvoker;
    private CommandInvoker<Integer> integerCommandInvoker;

    @Before
    public void setUp() {
        stringCommandInvoker = new CommandInvoker<>();
        integerCommandInvoker = new CommandInvoker<>();
    }

    @Test
    public void testStringCommandExecution() {
        StringCommand command = new StringCommand("Hello World");
        String result = stringCommandInvoker.execute(command);
        
        assertEquals("执行: Hello World", result);
        assertTrue(command.isExecuted());
    }

    @Test
    public void testIntegerCommandExecution() {
        IntegerCommand command = new IntegerCommand(10);
        Integer result = integerCommandInvoker.execute(command);
        
        assertEquals(Integer.valueOf(20), result);
        assertTrue(command.isExecuted());
    }

    @Test
    public void testStringCommandUndo() {
        StringCommand command = new StringCommand("Hello World");
        stringCommandInvoker.execute(command);
        
        String undoResult = stringCommandInvoker.undo();
        
        assertEquals("撤销: Hello World", undoResult);
        assertFalse(command.isExecuted());
    }

    @Test
    public void testIntegerCommandUndo() {
        IntegerCommand command = new IntegerCommand(10);
        integerCommandInvoker.execute(command);
        
        Integer undoResult = integerCommandInvoker.undo();
        
        assertEquals(Integer.valueOf(5), undoResult);
        assertFalse(command.isExecuted());
    }

    @Test
    public void testAsyncCommandExecution() throws ExecutionException, InterruptedException {
        StringCommand command = new StringCommand("Async Hello");
        CompletableFuture<String> future = stringCommandInvoker.executeAsync(command);
        
        String result = future.get();
        
        assertEquals("执行: Async Hello", result);
        assertTrue(command.isExecuted());
    }

    @Test
    public void testCommandHistory() {
        StringCommand command1 = new StringCommand("Command 1");
        StringCommand command2 = new StringCommand("Command 2");
        
        stringCommandInvoker.execute(command1);
        stringCommandInvoker.execute(command2);
        
        assertEquals(2, stringCommandInvoker.getHistorySize());
        
        stringCommandInvoker.undo();
        assertEquals(1, stringCommandInvoker.getHistorySize());
        
        stringCommandInvoker.undo();
        assertEquals(0, stringCommandInvoker.getHistorySize());
    }

    @Test
    public void testClearHistory() {
        StringCommand command1 = new StringCommand("Command 1");
        StringCommand command2 = new StringCommand("Command 2");
        
        stringCommandInvoker.execute(command1);
        stringCommandInvoker.execute(command2);
        
        assertEquals(2, stringCommandInvoker.getHistorySize());
        
        stringCommandInvoker.clearHistory();
        assertEquals(0, stringCommandInvoker.getHistorySize());
    }

    @Test
    public void testCanUndo() {
        StringCommand command = new StringCommand("Test Command");
        
        assertFalse(stringCommandInvoker.canUndo());
        
        stringCommandInvoker.execute(command);
        assertTrue(stringCommandInvoker.canUndo());
        
        stringCommandInvoker.undo();
        assertFalse(stringCommandInvoker.canUndo());
    }

    @Test(expected = FrameworkException.class)
    public void testExecuteNullCommand() {
        stringCommandInvoker.execute(null);
    }

    @Test
    public void testDefaultConstructor() {
        CommandInvoker<String> invoker = new CommandInvoker<>();
        assertEquals(0, invoker.getHistorySize());
    }

    @Test
    public void testCustomMaxHistorySize() {
        CommandInvoker<String> invoker = new CommandInvoker<>(5);
        assertEquals(0, invoker.getHistorySize());
        
        // 添加超过5个命令
        for (int i = 0; i < 7; i++) {
            StringCommand command = new StringCommand("Command " + i);
            invoker.execute(command);
        }
        
        // 历史记录应该保持在5个
        assertEquals(5, invoker.getHistorySize());
    }
}