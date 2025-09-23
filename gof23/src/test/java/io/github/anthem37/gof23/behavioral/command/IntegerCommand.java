package io.github.anthem37.gof23.behavioral.command;

/**
 * 整数命令示例实现
 */
public class IntegerCommand implements Command<Integer> {
    private final int value;
    private boolean executed = false;
    
    public IntegerCommand(int value) {
        this.value = value;
    }
    
    @Override
    public Integer execute() {
        executed = true;
        return value * 2;
    }
    
    @Override
    public Integer undo() {
        executed = false;
        return value / 2;
    }
    
    @Override
    public boolean isUndoable() {
        return true;
    }
    
    @Override
    public String getName() {
        return "IntegerCommand";
    }
    
    public boolean isExecuted() {
        return executed;
    }
}