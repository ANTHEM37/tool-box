package io.github.anthem37.gof23.behavioral.mediator;

import io.github.anthem37.gof23.common.FrameworkException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 抽象中介者
 * 提供中介者模式的基础实现
 *
 * @author anthem37
 * @version 1.0.0
 */
public abstract class AbstractMediator implements Mediator {

    private static final Logger logger = LoggerFactory.getLogger(AbstractMediator.class);

    /**
     * 同事对象注册表
     */
    protected final Map<String, Colleague> colleagues = new ConcurrentHashMap<>();

    /**
     * 中介者名称
     */
    protected final String mediatorName;

    /**
     * 构造函数
     *
     * @param mediatorName 中介者名称
     */
    protected AbstractMediator(String mediatorName) {
        if (mediatorName == null || mediatorName.trim().isEmpty()) {
            throw new FrameworkException("中介者名称不能为空");
        }
        this.mediatorName = mediatorName;
        logger.debug("创建中介者: {}", mediatorName);
    }

    @Override
    public void registerColleague(Colleague colleague) {
        if (colleague == null) {
            throw new FrameworkException("同事对象不能为空");
        }

        String colleagueId = colleague.getColleagueId();
        if (colleagueId == null || colleagueId.trim().isEmpty()) {
            throw new FrameworkException("同事对象ID不能为空");
        }

        colleagues.put(colleagueId, colleague);
        colleague.setMediator(this);

        logger.debug("注册同事对象: {} -> {}", mediatorName, colleagueId);

        // 通知其他同事对象有新成员加入
        onColleagueRegistered(colleague);
    }

    @Override
    public void removeColleague(Colleague colleague) {
        if (colleague == null) {
            return;
        }

        String colleagueId = colleague.getColleagueId();
        if (colleagues.remove(colleagueId) != null) {
            colleague.setMediator(null);
            logger.debug("移除同事对象: {} -> {}", mediatorName, colleagueId);

            // 通知其他同事对象有成员离开
            onColleagueRemoved(colleague);
        }
    }

    @Override
    public void communicate(Colleague sender, Object message, String targetId) {
        if (sender == null) {
            throw new FrameworkException("发送者不能为空");
        }
        if (message == null) {
            throw new FrameworkException("消息不能为空");
        }

        logger.debug("中介通信: {} -> {} (目标: {})",
                sender.getColleagueId(), message, targetId);

        if (targetId == null) {
            // 广播消息
            broadcast(sender, message);
        } else {
            // 点对点消息
            Colleague target = colleagues.get(targetId);
            if (target == null) {
                logger.warn("目标同事对象不存在: {}", targetId);
                onMessageDeliveryFailed(sender, message, targetId);
                return;
            }

            if (target.canHandle(message)) {
                target.receiveMessage(sender, message);
                onMessageDelivered(sender, target, message);
            } else {
                logger.warn("目标同事对象无法处理消息: {} -> {}", targetId, message);
                onMessageDeliveryFailed(sender, message, targetId);
            }
        }
    }

    @Override
    public void broadcast(Colleague sender, Object message) {
        if (sender == null) {
            throw new FrameworkException("发送者不能为空");
        }
        if (message == null) {
            throw new FrameworkException("消息不能为空");
        }

        logger.debug("广播消息: {} -> {}", sender.getColleagueId(), message);

        int deliveredCount = 0;
        for (Colleague colleague : colleagues.values()) {
            if (colleague != sender && colleague.canHandle(message)) {
                colleague.receiveMessage(sender, message);
                deliveredCount++;
            }
        }

        logger.debug("广播完成，成功投递: {} 个同事对象", deliveredCount);
        onBroadcastCompleted(sender, message, deliveredCount);
    }

    @Override
    public String getMediatorName() {
        return mediatorName;
    }

    @Override
    public int getColleagueCount() {
        return colleagues.size();
    }

    @Override
    public boolean hasColleague(String colleagueId) {
        return colleagueId != null && colleagues.containsKey(colleagueId);
    }

    /**
     * 获取所有同事对象ID
     *
     * @return 同事对象ID集合
     */
    public Set<String> getColleagueIds() {
        return Collections.unmodifiableSet(colleagues.keySet());
    }

    /**
     * 获取同事对象
     *
     * @param colleagueId 同事对象ID
     * @return 同事对象
     */
    public Colleague getColleague(String colleagueId) {
        return colleagues.get(colleagueId);
    }

    /**
     * 同事对象注册时的回调
     *
     * @param colleague 新注册的同事对象
     */
    protected abstract void onColleagueRegistered(Colleague colleague);

    /**
     * 同事对象移除时的回调
     *
     * @param colleague 被移除的同事对象
     */
    protected abstract void onColleagueRemoved(Colleague colleague);

    /**
     * 消息投递成功时的回调
     *
     * @param sender 发送者
     * @param receiver 接收者
     * @param message 消息
     */
    protected abstract void onMessageDelivered(Colleague sender, Colleague receiver, Object message);

    /**
     * 消息投递失败时的回调
     *
     * @param sender 发送者
     * @param message 消息
     * @param targetId 目标ID
     */
    protected abstract void onMessageDeliveryFailed(Colleague sender, Object message, String targetId);

    /**
     * 广播完成时的回调
     *
     * @param sender 发送者
     * @param message 消息
     * @param deliveredCount 成功投递数量
     */
    protected abstract void onBroadcastCompleted(Colleague sender, Object message, int deliveredCount);
}