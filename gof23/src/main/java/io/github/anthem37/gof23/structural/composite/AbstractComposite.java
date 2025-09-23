package io.github.anthem37.gof23.structural.composite;

import io.github.anthem37.gof23.common.FrameworkConstants;
import io.github.anthem37.gof23.common.FrameworkException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * 抽象组合组件
 * 提供组合模式的基础实现
 *
 * @param <T> 组件数据类型
 * @author anthem37
 * @version 1.0.0
 */
public abstract class AbstractComposite<T> implements Component<T> {

    private static final Logger logger = LoggerFactory.getLogger(AbstractComposite.class);

    /**
     * 组件名称
     */
    protected final String name;

    /**
     * 子组件列表
     */
    protected final List<Component<T>> children = new CopyOnWriteArrayList<>();

    /**
     * 父组件
     */
    protected Component<T> parent;

    /**
     * 构造函数
     *
     * @param name 组件名称
     */
    protected AbstractComposite(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new FrameworkException(FrameworkConstants.ErrorMessages.nullParameter("组件名称"));
        }
        this.name = name;
        logger.debug("创建组合组件: {}", name);
    }

    @Override
    public void add(Component<T> component) {
        if (component == null) {
            throw new FrameworkException(FrameworkConstants.ErrorMessages.nullParameter("子组件"));
        }
        if (component == this) {
            throw new FrameworkException("不能添加自己作为子组件");
        }

        children.add(component);
        component.setParent(this);
        logger.debug("添加子组件: {} -> {}", name, component.getName());
    }

    @Override
    public void remove(Component<T> component) {
        if (component == null) {
            return;
        }

        if (children.remove(component)) {
            component.setParent(null);
            logger.debug("移除子组件: {} -> {}", name, component.getName());
        }
    }

    @Override
    public Component<T> getChild(int index) {
        if (index < 0 || index >= children.size()) {
            throw new FrameworkException("子组件索引越界: " + index);
        }
        return children.get(index);
    }

    @Override
    public List<Component<T>> getChildren() {
        return Collections.unmodifiableList(children);
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean isLeaf() {
        return children.isEmpty();
    }

    @Override
    public Component<T> getParent() {
        return parent;
    }

    @Override
    public void setParent(Component<T> parent) {
        this.parent = parent;
    }

    @Override
    public int getDepth() {
        if (parent == null) {
            return 0;
        }
        return parent.getDepth() + 1;
    }

    @Override
    public void accept(ComponentVisitor<T> visitor) {
        if (visitor == null) {
            throw new FrameworkException("访问者不能为空");
        }

        if (isLeaf()) {
            visitor.visitLeaf(this);
        } else {
            visitor.visitComposite(this);
            // 遍历子组件
            for (Component<T> child : children) {
                child.accept(visitor);
            }
        }
    }

    /**
     * 获取子组件数量
     *
     * @return 子组件数量
     */
    public int getChildCount() {
        return children.size();
    }

    /**
     * 查找子组件
     *
     * @param name 组件名称
     * @return 找到的组件，未找到返回null
     */
    public Component<T> findChild(String name) {
        if (name == null || name.trim().isEmpty()) {
            return null;
        }

        for (Component<T> child : children) {
            if (name.equals(child.getName())) {
                return child;
            }
        }
        return null;
    }

    /**
     * 递归查找组件
     *
     * @param name 组件名称
     * @return 找到的组件，未找到返回null
     */
    public Component<T> findDescendant(String name) {
        if (name == null || name.trim().isEmpty()) {
            return null;
        }

        // 先查找直接子组件
        Component<T> found = findChild(name);
        if (found != null) {
            return found;
        }

        // 递归查找子组件的后代
        for (Component<T> child : children) {
            if (child instanceof AbstractComposite) {
                found = ((AbstractComposite<T>) child).findDescendant(name);
                if (found != null) {
                    return found;
                }
            }
        }

        return null;
    }
}