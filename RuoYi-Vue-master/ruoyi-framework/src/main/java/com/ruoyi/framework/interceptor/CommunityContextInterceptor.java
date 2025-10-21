package com.ruoyi.framework.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import com.ruoyi.common.core.context.CommunityContextHolder;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;

/**
 * 小区上下文拦截器。
 *
 * <p>在请求进入 Controller 之前，根据当前登录用户与前端传递的“小区选择”信息确定本次请求对应的小区范围，
 * 并写入 ThreadLocal，方便后续业务链路统一读取与校验。</p>
 *
 * <p>处理规则：</p>
 * <ul>
 *   <li>未登录或没有带上小区信息：直接清空上下文并放行为匿名请求；</li>
 *   <li>超管账号：允许通过 Header/参数切换小区，缺省时使用自身绑定的小区（通常为 null，视为查看全部）；</li>
 *   <li>普通账号：必须使用账号绑定的小区，若未绑定则中断本次请求。</li>
 * </ul>
 */
@Component
public class CommunityContextInterceptor implements HandlerInterceptor
{
    private static final String HEADER_KEY = "X-Community-Id";
    private static final String PARAM_KEY = "communityId";
    private static final String PARAM_FALLBACK = "currentCommunityId";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
    {
        Authentication authentication = SecurityUtils.getAuthentication();
        if (authentication == null || !(authentication.getPrincipal() instanceof LoginUser))
        {
            // 未登录时直接清理上下文，确保 ThreadLocal 不残留历史数据
            CommunityContextHolder.clear();
            return true;
        }

        LoginUser loginUser = (LoginUser) authentication.getPrincipal();
        Long resolvedCommunityId;
        if (SecurityUtils.isAdmin(loginUser.getUserId()))
        {
            resolvedCommunityId = parseCommunityId(request);
            // 超管未显式传参时，回落到自己绑定的小区（通常为 null，表示查看全部小区）
            if (resolvedCommunityId == null)
            {
                SysUser operator = loginUser.getUser();
                resolvedCommunityId = operator != null ? operator.getCommunityId() : null;
            }
        }
        else
        {
            SysUser operator = loginUser.getUser();
            // 用户类型为 10（物业总部或其他跨小区账号）时允许跨社区切换
            boolean skipBindingCheck = operator != null && StringUtils.equals("10", operator.getUserType());
            if (operator == null || (!skipBindingCheck && operator.getCommunityId() == null))
            {
                throw new ServiceException("当前账号尚未绑定任何小区，请联系管理员处理");
            }
            if (skipBindingCheck)
            {
                resolvedCommunityId = parseCommunityId(request);
                if (resolvedCommunityId == null)
                {
                    resolvedCommunityId = operator.getCommunityId();
                }
            }
            else
            {
                resolvedCommunityId = operator.getCommunityId();
            }
        }

        CommunityContextHolder.setCommunityId(resolvedCommunityId);
        // 方便后续链路（如日志、前端回显等）直接从 request 中读取当前小区信息
        request.setAttribute(PARAM_KEY, resolvedCommunityId);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
    {
        // 请求完成后无论成功或失败都要清理 ThreadLocal，防止数据串扰
        CommunityContextHolder.clear();
    }

    /**
     * 从请求头或查询参数中解析小区 ID。
     *
     * @param request 当前 HTTP 请求
     * @return 解析到的小区 ID；如果未提供或解析失败则返回 null
     */
    private Long parseCommunityId(HttpServletRequest request)
    {
        String raw = request.getHeader(HEADER_KEY);
        if (StringUtils.isEmpty(raw))
        {
            raw = request.getParameter(PARAM_KEY);
        }
        if (StringUtils.isEmpty(raw))
        {
            raw = request.getParameter(PARAM_FALLBACK);
        }
        if (StringUtils.isEmpty(raw))
        {
            return null;
        }
        try
        {
            return Long.valueOf(raw.trim());
        }
        catch (NumberFormatException ex)
        {
            throw new ServiceException("无效的小区编号：" + raw);
        }
    }
}
