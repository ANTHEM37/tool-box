package io.github.anthem37.gof23.behavioral.memento;

import io.github.anthem37.gof23.common.FrameworkConstants;
import io.github.anthem37.gof23.common.FrameworkException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * 管理者
 * 负责管理备忘录的存储和检索
 *
 * @param <T> 状态类型
 * @param <M> 备忘录类型
 * @author anthem37
 * @version 1.0.0
 */
public class Caretaker<T, M extends Memento<T>> {

    private static final Logger logger = LoggerFactory.getLogger(Caretaker.class);

    /**
     * 备忘录存储
     */
    private final ConcurrentMap<String, M> mementos;

    /**
     * 备忘录历史记录（按时间顺序）
     */
    private final List<String> history;

    /**
     * 最大存储数量
     */
    private final int maxSize;

    /**
     * 构造函数
     */
    public Caretaker() {
        this(FrameworkConstants.Default.MAX_CACHE_SIZE);
    }

    /**
     * 构造函数
     *
     * @param maxSize 最大存储数量
     */
    public Caretaker(int maxSize) {
        this.maxSize = Math.max(1, maxSize);
        this.mementos = new ConcurrentHashMap<>();
        this.history = Collections.synchronizedList(new ArrayList<>());

        logger.debug("管理者已创建，最大存储数量: {}", this.maxSize);
    }

    /**
     * 保存备忘录
     *
     * @param memento 备忘录
     * @throws FrameworkException 如果保存失败
     */
    public void saveMemento(M memento) throws FrameworkException {
        if (memento == null || !memento.isValid()) {
            throw new FrameworkException(FrameworkConstants.ErrorMessages.INVALID_PARAMETER);
        }

        String mementoId = memento.getMementoId();

        // 检查存储限制
        if (mementos.size() >= maxSize && !mementos.containsKey(mementoId)) {
            // 移除最旧的备忘录
            String oldestId = history.get(0);
            mementos.remove(oldestId);
            history.remove(0);
            logger.debug("移除最旧的备忘录: {}", oldestId);
        }

        mementos.put(mementoId, memento);
        if (!history.contains(mementoId)) {
            history.add(mementoId);
        }

        logger.debug("保存备忘录: {}", mementoId);
    }

    /**
     * 获取备忘录
     *
     * @param mementoId 备忘录标识
     * @return 备忘录
     * @throws FrameworkException 如果获取失败
     */
    public M getMemento(String mementoId) throws FrameworkException {
        if (mementoId == null) {
            throw new FrameworkException(FrameworkConstants.ErrorMessages.INVALID_PARAMETER);
        }

        M memento = mementos.get(mementoId);
        if (memento == null) {
            throw new FrameworkException("备忘录不存在: " + mementoId);
        }

        return memento;
    }

    /**
     * 获取最新的备忘录
     *
     * @return 最新的备忘录
     * @throws FrameworkException 如果没有备忘录
     */
    public M getLatestMemento() throws FrameworkException {
        if (history.isEmpty()) {
            throw new FrameworkException("没有可用的备忘录");
        }

        String latestId = history.get(history.size() - 1);
        return getMemento(latestId);
    }

    /**
     * 移除备忘录
     *
     * @param mementoId 备忘录标识
     * @return 被移除的备忘录
     */
    public M removeMemento(String mementoId) {
        if (mementoId == null) {
            return null;
        }

        M removed = mementos.remove(mementoId);
        history.remove(mementoId);

        if (removed != null) {
            logger.debug("移除备忘录: {}", mementoId);
        }

        return removed;
    }

    /**
     * 获取所有备忘录标识
     *
     * @return 备忘录标识集合
     */
    public Set<String> getMementoIds() {
        return Collections.unmodifiableSet(new HashSet<>(mementos.keySet()));
    }

    /**
     * 获取历史记录
     *
     * @return 历史记录（按时间顺序）
     */
    public List<String> getHistory() {
        synchronized (history) {
            return new ArrayList<>(history);
        }
    }

    /**
     * 检查是否包含指定的备忘录
     *
     * @param mementoId 备忘录标识
     * @return 是否包含
     */
    public boolean containsMemento(String mementoId) {
        return mementos.containsKey(mementoId);
    }

    /**
     * 获取备忘录数量
     *
     * @return 备忘录数量
     */
    public int size() {
        return mementos.size();
    }

    /**
     * 检查是否为空
     *
     * @return 是否为空
     */
    public boolean isEmpty() {
        return mementos.isEmpty();
    }

    /**
     * 清空所有备忘录
     */
    public void clear() {
        int size = mementos.size();
        mementos.clear();
        history.clear();
        logger.debug("清空所有备忘录，移除 {} 个备忘录", size);
    }
}