package com.ruoyi.common.utils;

import com.ruoyi.common.core.context.CommunityContextHolder;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.exception.ServiceException;

/**
 * 小区相关的通用工具方法封装。
 *
 * <p>目前主要负责：</p>
 * <ul>
 *   <li>提供便捷的“当前小区”获取方法；</li>
 *   <li>对必须绑定小区的场景进行统一的判空校验；</li>
 *   <li>辅助识别当前登录用户是否为超级管理员。</li>
 * </ul>
 *
 * @author codex
 */
public class CommunityUtils
{
    private CommunityUtils()
    {
    }

    /**
     * 读取当前线程上下文中的小区 ID。
     *
     * @return 当前小区 ID，可能为 null（超级管理员查看全部数据时）
     */
    public static Long getCurrentCommunityId()
    {
        return CommunityContextHolder.getCommunityId();
    }

    /**
     * 读取并校验当前小区 ID，若不存在则抛出业务异常。
     * 适用于“非超级管理员必须绑定小区”的场景，避免各处重复判空。
     *
     * @param errorMessage 提示语
     * @return 已校验非空的小区 ID
     */
    public static Long requireCurrentCommunityId(String errorMessage)
    {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        Long communityId = getCurrentCommunityId();
        if (communityId == null && "00".equals(loginUser.getUser().getUserType()))
        {
            throw new ServiceException(StringUtils.isNotBlank(errorMessage) ? errorMessage : "当前账号未绑定任何小区");
        }
        return communityId;
    }

    /**
     * 判断当前登录用户是否为超级管理员。
     *
     * @return true 表示当前用户为超级管理员
     */
    public static boolean isCurrentUserAdmin()
    {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        return loginUser != null && SecurityUtils.isAdmin(loginUser.getUserId());
    }

    /**
     * 校验目标数据所属小区是否与当前账号匹配。
     *
     * @param targetCommunityId 目标数据绑定的小区ID
     */
    public static void checkCommunityPermission(Long targetCommunityId)
    {
        if (isCurrentUserAdmin())
        {
            return;
        }
        LoginUser loginUser = SecurityUtils.getLoginUser();
        Long current = requireCurrentCommunityId("当前账号未绑定任何小区");
        if("00".equals(loginUser.getUser().getUserType())){
            if (targetCommunityId == null || !current.equals(targetCommunityId))
            {
                System.out.println(loginUser.getUser().getUserType());
                throw new ServiceException("无权访问其他小区的数据");
            }
        }

    }
}
