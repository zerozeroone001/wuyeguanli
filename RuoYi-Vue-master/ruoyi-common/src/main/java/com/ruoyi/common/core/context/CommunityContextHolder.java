package com.ruoyi.common.core.context;

/**
 * 小区上下文存储器，通过 ThreadLocal 保存当前请求对应的小区 ID。
 * 该类放置于 common 模块，便于系统内的任意层（控制器、服务、DAO）
 * 都能以一致方式获取“当前小区”信息。
 *
 * @author codex
 */
public class CommunityContextHolder
{
    /**
     * 使用 ThreadLocal 保存当前线程的小区 ID。
     * 线程结束或请求完成时务必调用 clear()，避免内存泄漏。
     */
    private static final ThreadLocal<Long> CONTEXT = new ThreadLocal<>();

    /**
     * 获取当前线程绑定的小区 ID。
     *
     * @return 当前小区 ID；若为超级管理员且未选择则可能返回 null
     */
    public static Long getCommunityId()
    {
        return CONTEXT.get();
    }

    /**
     * 绑定当前线程的小区 ID。
     *
     * @param communityId 小区 ID，可为 null（超级管理员查看全部时）
     */
    public static void setCommunityId(Long communityId)
    {
        CONTEXT.set(communityId);
    }

    /**
     * 清理当前线程的小区上下文，防止线程复用导致数据串扰。
     */
    public static void clear()
    {
        CONTEXT.remove();
    }
}
