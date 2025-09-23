package io.github.anthem37.gof23.structural.composite;

import java.util.List;

/**
 * 组合模式组件接口
 * 定义组合中对象的通用接口，可以是叶子节点或容器节点
 *
 * @param <T> 组件数据类型
 * @author anthem37
 * @version 1.0.0
 */
public interface Component<T> {

    /**
     * 执行操作
     *
     * @param context 上下文参数
     * @return 操作结果
     */
    T operation(Object context);

    /**
     * 添加子组件
     *
     * @param component 子组件
     */
    void add(Component<T> component);

    /**
     * 移除子组件
     *
     * @param component 子组件
     */
    void remove(Component<T> component);

    /**
     * 获取子组件
     *
     * @param index 索引
     * @return 子组件
     */
    Component<T> getChild(int index);

    /**
     * 获取所有子组件
     *
     * @return 子组件列表
     */
    List<Component<T>> getChildren();

    /**
     * 获取组件名称
     *
     * @return 组件名称
     */
    String getName();

    /**
     * 获取组件类型
     *
     * @return 组件类型
     */
    String getType();

    /**
     * 是否为叶子节点
     *
     * @return 是否为叶子节点
     */
    boolean isLeaf();

    /**
     * 获取父组件
     *
     * @return 父组件
     */
    Component<T> getParent();

    /**
     * 设置父组件
     *
     * @param parent 父组件
     */
    void setParent(Component<T> parent);

    /**
     * 获取组件深度
     *
     * @return 组件深度
     */
    int getDepth();

    /**
     * 接受访问者
     *
     * @param visitor 访问者
     */
    void accept(ComponentVisitor<T> visitor);
}