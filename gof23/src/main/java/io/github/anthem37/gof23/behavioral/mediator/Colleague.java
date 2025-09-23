package io.github.anthem37.gof23.behavioral.mediator;

/**
 * 同事类接口
 * 定义参与中介者模式的同事对象的基本行为
 *
 * @author anthem37
 * @version 1.0.0
 */
public interface Colleague {

    /**
     * 获取中介者
     *
     * @return 中介者
     */
    Mediator getMediator();

    /**
     * 设置中介者
     *
     * @param mediator 中介者
     */
    void setMediator(Mediator mediator);

    /**
     * 接收消息
     *
     * @param sender 发送者
     * @param message 消息
     */
    void receiveMessage(Colleague sender, Object message);

    /**
     * 发送消息
     *
     * @param message 消息
     * @param targetId 目标同事ID，null表示广播
     */
    void sendMessage(Object message, String targetId);

    /**
     * 获取同事对象ID
     *
     * @return 同事对象ID
     */
    String getColleagueId();

    /**
     * 获取同事对象名称
     *
     * @return 同事对象名称
     */
    String getColleagueName();

    /**
     * 获取同事对象类型
     *
     * @return 同事对象类型
     */
    String getColleagueType();

    /**
     * 检查是否可以处理消息
     *
     * @param message 消息
     * @return 是否可以处理
     */
    boolean canHandle(Object message);
}