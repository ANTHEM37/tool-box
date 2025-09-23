package io.github.anthem37.gof23.behavioral.visitor;

/**
 * 可访问接口
 * 定义可被访问元素的标准契约
 *
 * @author anthem37
 * @version 1.0.0
 */
public interface Visitable {

    /**
     * 接受访问者访问
     *
     * @param visitor 访问者
     * @return 访问结果
     */
    Object accept(Visitor visitor);

    /**
     * 获取元素类型
     *
     * @return 元素类型
     */
    default String getElementType() {
        return this.getClass().getSimpleName();
    }

    /**
     * 获取元素标识
     *
     * @return 元素标识
     */
    default String getElementId() {
        return String.valueOf(hashCode());
    }
}