package io.github.anthem37.gof23.behavioral.state;

import io.github.anthem37.gof23.common.FrameworkException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 状态机
 * 管理状态转换和事件处理
 *
 * @param <T> 上下文类型
 * @author anthem37
 * @version 1.0.0
 */
public class StateMachine<T> {

    private static final Logger logger = LoggerFactory.getLogger(StateMachine.class);

    /**
     * 状态映射表
     */
    private final Map<String, State<T>> states = new ConcurrentHashMap<>();

    /**
     * 状态转换映射表：当前状态 -> 事件 -> 目标状态
     */
    private final Map<String, Map<String, String>> transitions = new ConcurrentHashMap<>();
    /**
     * 上下文对象
     */
    private final T context;
    /**
     * 状态机名称
     */
    private final String name;
    /**
     * 当前状态
     */
    private volatile State<T> currentState;

    /**
     * 构造函数
     *
     * @param context 上下文对象
     * @param name 状态机名称
     */
    public StateMachine(T context, String name) {
        if (context == null) {
            throw new FrameworkException("上下文对象不能为空");
        }
        if (name == null || name.trim().isEmpty()) {
            throw new FrameworkException("状态机名称不能为空");
        }

        this.context = context;
        this.name = name;
    }

    /**
     * 添加状态
     *
     * @param state 状态实例
     * @return 当前状态机实例，支持链式调用
     */
    public StateMachine<T> addState(State<T> state) {
        if (state == null) {
            throw new FrameworkException("状态不能为空");
        }

        states.put(state.getName(), state);
        logger.debug("添加状态: {} (状态机: {})", state.getName(), name);
        return this;
    }

    /**
     * 添加状态转换
     *
     * @param fromState 源状态名称
     * @param event 触发事件
     * @param toState 目标状态名称
     * @return 当前状态机实例，支持链式调用
     */
    public StateMachine<T> addTransition(String fromState, String event, String toState) {
        if (fromState == null || fromState.trim().isEmpty()) {
            throw new FrameworkException("源状态名称不能为空");
        }
        if (event == null || event.trim().isEmpty()) {
            throw new FrameworkException("事件名称不能为空");
        }
        if (toState == null || toState.trim().isEmpty()) {
            throw new FrameworkException("目标状态名称不能为空");
        }

        transitions.computeIfAbsent(fromState, k -> new ConcurrentHashMap<>())
                .put(event, toState);

        logger.debug("添加状态转换: {} --[{}]--> {} (状态机: {})",
                fromState, event, toState, name);
        return this;
    }

    /**
     * 设置初始状态
     *
     * @param stateName 状态名称
     */
    public void setInitialState(String stateName) {
        State<T> state = states.get(stateName);
        if (state == null) {
            throw new FrameworkException("状态不存在: " + stateName);
        }

        this.currentState = state;
        state.onEnter(context);

        logger.debug("设置初始状态: {} (状态机: {})", stateName, name);
    }

    /**
     * 处理事件
     *
     * @param event 事件名称
     * @return 处理结果
     */
    public Object handleEvent(String event) {
        if (currentState == null) {
            throw new FrameworkException("状态机未初始化，请先设置初始状态");
        }
        if (event == null || event.trim().isEmpty()) {
            throw new FrameworkException("事件名称不能为空");
        }

        logger.debug("处理事件: {} (当前状态: {}, 状态机: {})",
                event, currentState.getName(), name);

        try {
            // 处理事件
            Object result = currentState.handle(context, event);

            // 检查是否需要状态转换
            String currentStateName = currentState.getName();
            Map<String, String> stateTransitions = transitions.get(currentStateName);

            if (stateTransitions != null && stateTransitions.containsKey(event)) {
                String nextStateName = stateTransitions.get(event);
                transitionTo(nextStateName);
            }

            logger.debug("事件处理完成: {} (状态机: {})", event, name);
            return result;

        } catch (Exception e) {
            logger.error("事件处理失败: {} (状态机: {})", event, name, e);
            throw new FrameworkException("事件处理失败: " + event, e);
        }
    }

    /**
     * 转换到指定状态
     *
     * @param stateName 目标状态名称
     */
    private void transitionTo(String stateName) {
        State<T> nextState = states.get(stateName);
        if (nextState == null) {
            throw new FrameworkException("目标状态不存在: " + stateName);
        }

        String previousStateName = currentState.getName();

        // 退出当前状态
        currentState.onExit(context);

        // 切换状态
        currentState = nextState;

        // 进入新状态
        currentState.onEnter(context);

        logger.debug("状态转换: {} -> {} (状态机: {})",
                previousStateName, stateName, name);
    }

    /**
     * 获取当前状态名称
     *
     * @return 当前状态名称
     */
    public String getCurrentStateName() {
        return currentState != null ? currentState.getName() : null;
    }

    /**
     * 获取所有状态名称
     *
     * @return 状态名称集合
     */
    public Set<String> getAllStateNames() {
        return new java.util.HashSet<>(states.keySet());
    }

    /**
     * 检查状态是否存在
     *
     * @param stateName 状态名称
     * @return 是否存在
     */
    public boolean hasState(String stateName) {
        return states.containsKey(stateName);
    }

    /**
     * 获取状态机名称
     *
     * @return 状态机名称
     */
    public String getName() {
        return name;
    }

    /**
     * 获取上下文对象
     *
     * @return 上下文对象
     */
    public T getContext() {
        return context;
    }
}