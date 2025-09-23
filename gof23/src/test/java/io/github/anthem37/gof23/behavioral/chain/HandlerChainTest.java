package io.github.anthem37.gof23.behavioral.chain;

import io.github.anthem37.gof23.common.FrameworkException;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

/**
 * HandlerChain单元测试
 */
public class HandlerChainTest {

    private HandlerChain<String, String> handlerChain;
    
    // 测试处理器实现
    static class TestHandler implements Handler<String, String> {
        private final String name;
        private final int priority;
        private final boolean canHandle;
        private boolean handled = false;
        
        public TestHandler(String name, int priority, boolean canHandle) {
            this.name = name;
            this.priority = priority;
            this.canHandle = canHandle;
        }
        
        @Override
        public String handle(String request) {
            handled = true;
            return name + ":handled:" + request;
        }
        
        @Override
        public boolean canHandle(String request) {
            return canHandle;
        }
        
        @Override
        public int getPriority() {
            return priority;
        }
        
        public boolean isHandled() {
            return handled;
        }
    }
    
    @Before
    public void setUp() {
        handlerChain = new HandlerChain<>();
    }
    
    @Test
    public void testAddHandler() {
        Handler<String, String> handler = new TestHandler("test", 1, true);
        handlerChain.addHandler(handler);
        
        assertEquals(1, handlerChain.getHandlerCount());
    }
    
    @Test
    public void testAddNullHandler() {
        try {
            handlerChain.addHandler(null);
            fail("Expected FrameworkException to be thrown");
        } catch (FrameworkException e) {
            // Expected
        }
    }
    
    @Test
    public void testRemoveHandler() {
        Handler<String, String> handler = new TestHandler("test", 1, true);
        handlerChain.addHandler(handler);
        
        assertTrue(handlerChain.removeHandler(handler));
        assertEquals(0, handlerChain.getHandlerCount());
    }
    
    @Test
    public void testHandleWithMatchingHandler() {
        Handler<String, String> handler1 = new TestHandler("handler1", 2, false);
        Handler<String, String> handler2 = new TestHandler("handler2", 1, true);
        
        handlerChain.addHandler(handler1);
        handlerChain.addHandler(handler2);
        
        String result = handlerChain.handle("test");
        
        assertEquals("handler2:handled:test", result);
        assertFalse(((TestHandler) handler1).isHandled());
        assertTrue(((TestHandler) handler2).isHandled());
    }
    
    @Test
    public void testHandleWithNoMatchingHandler() {
        Handler<String, String> handler1 = new TestHandler("handler1", 1, false);
        Handler<String, String> handler2 = new TestHandler("handler2", 2, false);
        
        handlerChain.addHandler(handler1);
        handlerChain.addHandler(handler2);
        
        String result = handlerChain.handle("test");
        
        assertNull(result);
        assertFalse(((TestHandler) handler1).isHandled());
        assertFalse(((TestHandler) handler2).isHandled());
    }
    
    @Test
    public void testHandleWithNullRequest() {
        try {
            handlerChain.handle(null);
            fail("Expected FrameworkException to be thrown");
        } catch (FrameworkException e) {
            // Expected
        }
    }
    
    @Test
    public void testHandleAllWithMatchingHandlers() {
        Handler<String, String> handler1 = new TestHandler("handler1", 2, true);
        Handler<String, String> handler2 = new TestHandler("handler2", 1, true);
        Handler<String, String> handler3 = new TestHandler("handler3", 3, false);
        
        handlerChain.addHandler(handler1);
        handlerChain.addHandler(handler2);
        handlerChain.addHandler(handler3);
        
        List<String> results = handlerChain.handleAll("test");
        
        assertEquals(2, results.size());
        // 验证按优先级排序执行
        assertEquals("handler2:handled:test", results.get(0));
        assertEquals("handler1:handled:test", results.get(1));
        
        assertTrue(((TestHandler) handler1).isHandled());
        assertTrue(((TestHandler) handler2).isHandled());
        assertFalse(((TestHandler) handler3).isHandled());
    }
    
    @Test
    public void testHandleAllWithNoMatchingHandlers() {
        Handler<String, String> handler1 = new TestHandler("handler1", 1, false);
        Handler<String, String> handler2 = new TestHandler("handler2", 2, false);
        
        handlerChain.addHandler(handler1);
        handlerChain.addHandler(handler2);
        
        List<String> results = handlerChain.handleAll("test");
        
        assertTrue(results.isEmpty());
        assertFalse(((TestHandler) handler1).isHandled());
        assertFalse(((TestHandler) handler2).isHandled());
    }
    
    @Test
    public void testHandleAllWithNullRequest() {
        try {
            handlerChain.handleAll(null);
            fail("Expected FrameworkException to be thrown");
        } catch (FrameworkException e) {
            // Expected
        }
    }
    
    @Test
    public void testClear() {
        Handler<String, String> handler1 = new TestHandler("handler1", 1, true);
        Handler<String, String> handler2 = new TestHandler("handler2", 2, true);
        
        handlerChain.addHandler(handler1);
        handlerChain.addHandler(handler2);
        
        assertEquals(2, handlerChain.getHandlerCount());
        
        handlerChain.clear();
        
        assertEquals(0, handlerChain.getHandlerCount());
    }
    
    @Test
    public void testGetHandlers() {
        Handler<String, String> handler1 = new TestHandler("handler1", 1, true);
        Handler<String, String> handler2 = new TestHandler("handler2", 2, true);
        
        handlerChain.addHandler(handler1);
        handlerChain.addHandler(handler2);
        
        List<Handler<String, String>> handlers = handlerChain.getHandlers();
        
        assertEquals(2, handlers.size());
        // 验证返回的是只读副本
        try {
            handlers.add(handler1);
            fail("Expected UnsupportedOperationException to be thrown");
        } catch (UnsupportedOperationException e) {
            // Expected
        }
    }
    
    @Test
    public void testHandlerPriorityOrdering() {
        // 添加乱序的处理器
        Handler<String, String> handler1 = new TestHandler("handler1", 10, true);
        Handler<String, String> handler2 = new TestHandler("handler2", 1, true);
        Handler<String, String> handler3 = new TestHandler("handler3", 5, true);
        
        handlerChain.addHandler(handler1);
        handlerChain.addHandler(handler2);
        handlerChain.addHandler(handler3);
        
        String result = handlerChain.handle("test");
        
        // 应该由优先级最高的handler2处理
        assertEquals("handler2:handled:test", result);
    }
}