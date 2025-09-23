package io.github.anthem37.gof23.behavioral.mediator;

/**
 * 中介者接口
 * 定义同事对象之间的通信协议
 *
 * @author anthem37
 * @version 1.0.0
 */
public interface Mediator {

    /**
     * 注册同事对象
     *
     * @param colleague 同事对象
     */
    void registerColleague(Colleague colleague);

    /**
     * 移除同事对象
     *
     * @param colleague 同事对象
     */
    void removeColleague(Colleague colleague);

    /**
     * 同事对象间的通信
     *
     * @param sender 发送者
     * @param message 消息
     * @param targetId 目标同事ID，null表示广播
     */
    void communicate(Colleague sender, Object message, String targetId);

    /**
     * 广播消息
     *
     * @param sender 发送者
     * @param message 消息
     */
    void broadcast(Colleague sender, Object message);

    /**
     * 获取中介者名称
     *
     * @return 中介者名称
     */
    String getMediatorName();

    /**
     * 获取同事对象数量
     *
     * @return 同事对象数量
     */
    int getColleagueCount();

    /**
     * 检查同事对象是否存在
     *
     * @param colleagueId 同事对象ID
     * @return 是否存在
     */
    boolean hasColleague(String colleagueId);
}