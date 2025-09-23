package io.github.anthem37.gof23.behavioral.visitor;

import io.github.anthem37.gof23.common.FrameworkException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 访问者注册表
 * 管理访问者和执行访问操作
 *
 * @author anthem37
 * @version 1.0.0
 */
public class VisitorRegistry {

    private static final Logger logger = LoggerFactory.getLogger(VisitorRegistry.class);

    /**
     * 访问者映射表：元素类型 -> 访问者列表
     */
    private final Map<Class<?>, List<Visitor>> visitors = new ConcurrentHashMap<>();

    /**
     * 全局访问者列表（支持所有元素类型）
     */
    private final List<Visitor> globalVisitors = new ArrayList<>();

    /**
     * 注册访问者
     *
     * @param visitor 访问者实例
     * @param elementTypes 支持的元素类型，如果为空则表示支持所有类型
     */
    public void registerVisitor(Visitor visitor, Class<?>... elementTypes) {
        if (visitor == null) {
            throw new FrameworkException("访问者不能为空");
        }

        if (elementTypes == null || elementTypes.length == 0) {
            // 注册为全局访问者
            globalVisitors.add(visitor);
            logger.debug("注册全局访问者: {}", visitor.getName());
        } else {
            // 注册为特定类型的访问者
            for (Class<?> elementType : elementTypes) {
                visitors.computeIfAbsent(elementType, k -> new ArrayList<>())
                        .add(visitor);
                logger.debug("注册访问者: {} -> {}",
                        elementType.getSimpleName(), visitor.getName());
            }
        }
    }

    /**
     * 移除访问者
     *
     * @param visitorName 访问者名称
     * @return 是否移除成功
     */
    public boolean removeVisitor(String visitorName) {
        if (visitorName == null || visitorName.trim().isEmpty()) {
            return false;
        }

        boolean removed = false;

        // 从全局访问者中移除
        removed |= globalVisitors.removeIf(visitor ->
                visitorName.equals(visitor.getName()));

        // 从特定类型访问者中移除
        for (List<Visitor> visitorList : visitors.values()) {
            removed |= visitorList.removeIf(visitor ->
                    visitorName.equals(visitor.getName()));
        }

        if (removed) {
            logger.debug("移除访问者: {}", visitorName);
        }

        return removed;
    }

    /**
     * 访问元素
     *
     * @param element 被访问的元素
     * @return 访问结果列表
     */
    public List<Object> visit(Visitable element) {
        if (element == null) {
            throw new FrameworkException("被访问元素不能为空");
        }

        List<Object> results = new ArrayList<>();
        Class<?> elementClass = element.getClass();

        logger.debug("开始访问元素: {} ({})",
                element.getElementType(), element.getElementId());

        // 执行全局访问者
        for (Visitor visitor : globalVisitors) {
            if (visitor.supports(elementClass)) {
                Object result = executeVisit(visitor, element);
                results.add(result);
            }
        }

        // 执行特定类型的访问者
        List<Visitor> typeVisitors = visitors.get(elementClass);
        if (typeVisitors != null) {
            for (Visitor visitor : typeVisitors) {
                if (visitor.supports(elementClass)) {
                    Object result = executeVisit(visitor, element);
                    results.add(result);
                }
            }
        }

        logger.debug("访问元素完成: {} (访问者数量: {})",
                element.getElementType(), results.size());

        return results;
    }

    /**
     * 执行访问操作
     *
     * @param visitor 访问者
     * @param element 被访问元素
     * @return 访问结果
     */
    private Object executeVisit(Visitor visitor, Visitable element) {
        try {
            visitor.beforeVisit(element);

            Object result = visitor.visit(element);

            visitor.afterVisit(element, result);

            logger.debug("访问成功: {} -> {}",
                    visitor.getName(), element.getElementType());

            return result;

        } catch (Exception e) {
            logger.error("访问失败: {} -> {}",
                    visitor.getName(), element.getElementType(), e);
            throw new FrameworkException("访问失败: " + visitor.getName(), e);
        }
    }

    /**
     * 获取指定元素类型的访问者数量
     *
     * @param elementClass 元素类型
     * @return 访问者数量
     */
    public int getVisitorCount(Class<?> elementClass) {
        int count = globalVisitors.size();

        List<Visitor> typeVisitors = visitors.get(elementClass);
        if (typeVisitors != null) {
            count += typeVisitors.size();
        }

        return count;
    }

    /**
     * 获取总访问者数量
     *
     * @return 总访问者数量
     */
    public int getTotalVisitorCount() {
        int count = globalVisitors.size();

        for (List<Visitor> visitorList : visitors.values()) {
            count += visitorList.size();
        }

        return count;
    }

    /**
     * 清空所有访问者
     */
    public void clear() {
        int count = getTotalVisitorCount();
        globalVisitors.clear();
        visitors.clear();
        logger.debug("清空访问者注册表，移除 {} 个访问者", count);
    }
}