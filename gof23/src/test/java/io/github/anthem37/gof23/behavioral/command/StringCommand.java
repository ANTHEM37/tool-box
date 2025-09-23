package io.github.anthem37.gof23.behavioral.command;

/**
 * 字符串命令示例实现
 */
public class StringCommand implements Command<String> {
    private final String message;
    private boolean executed = false;
    
    public StringCommand(String message) {
        this.message = message;
    }
    
    @Override
    public String execute() {
        executed = true;
        return "执行: " + message;
    }
    
    @Override
    public String undo() {
        executed = false;
        return "撤销: " + message;
    }
    
    @Override
    public boolean isUndoable() {
        return true;
    }
    
    @Override
    public String getName() {
        return "StringCommand";
    }
    
    public boolean isExecuted() {
        return executed;
    }
}