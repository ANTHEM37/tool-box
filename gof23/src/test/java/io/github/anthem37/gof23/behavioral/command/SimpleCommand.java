package io.github.anthem37.gof23.behavioral.command;

/**
 * 简单命令示例实现
 */
public class SimpleCommand implements Command<String> {
    private final String action;
    
    public SimpleCommand(String action) {
        this.action = action;
    }
    
    @Override
    public String execute() {
        return "执行动作: " + action;
    }
    
    @Override
    public String getName() {
        return "SimpleCommand";
    }
}