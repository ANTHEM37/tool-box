package io.github.anthem37.gof23.structural.composite;

/**
 * 组件访问者接口
 * 用于遍历组合结构中的组件
 *
 * @param <T> 组件数据类型
 * @author anthem37
 * @version 1.0.0
 */
public interface ComponentVisitor<T> {

    /**
     * 访问叶子组件
     *
     * @param leaf 叶子组件
     */
    void visitLeaf(Component<T> leaf);

    /**
     * 访问容器组件
     *
     * @param composite 容器组件
     */
    void visitComposite(Component<T> composite);

    /**
     * 获取访问者名称
     *
     * @return 访问者名称
     */
    String getVisitorName();
}