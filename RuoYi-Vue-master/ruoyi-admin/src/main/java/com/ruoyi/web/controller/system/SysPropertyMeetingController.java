package com.ruoyi.web.controller.system;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.SysPropertyMeeting;
import com.ruoyi.system.service.ISysPropertyMeetingService;
import com.ruoyi.system.service.ISysUserService;
import com.ruoyi.system.service.WechatService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;
import java.text.SimpleDateFormat;

/**
 * 会议管理Controller
 * 
 * @author ruoyi
 * @date 2025-08-21
 */
@RestController
@RequestMapping("/system/meeting")
public class SysPropertyMeetingController extends BaseController
{
    @Autowired
    private ISysPropertyMeetingService sysPropertyMeetingService;
    
    @Autowired
    private ISysUserService sysUserService;
    
    @Autowired
    private WechatService wechatService;

    /**
     * 查询会议管理列表
     */
    @PreAuthorize("@ss.hasPermi('system:meeting:list')")
    @GetMapping("/list")
    public TableDataInfo list(SysPropertyMeeting sysPropertyMeeting)
    {
        startPage();
        List<SysPropertyMeeting> list = sysPropertyMeetingService.selectSysPropertyMeetingList(sysPropertyMeeting);
        return getDataTable(list);
    }

    /**
     * 导出会议管理列表
     */
    @PreAuthorize("@ss.hasPermi('system:meeting:export')")
    @Log(title = "会议管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, SysPropertyMeeting sysPropertyMeeting)
    {
        List<SysPropertyMeeting> list = sysPropertyMeetingService.selectSysPropertyMeetingList(sysPropertyMeeting);
        ExcelUtil<SysPropertyMeeting> util = new ExcelUtil<SysPropertyMeeting>(SysPropertyMeeting.class);
        util.exportExcel(response, list, "会议管理数据");
    }

    /**
     * 获取会议管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:meeting:query')")
    @GetMapping(value = "/{meetingId}")
    public AjaxResult getInfo(@PathVariable("meetingId") Long meetingId)
    {
        return success(sysPropertyMeetingService.selectSysPropertyMeetingByMeetingId(meetingId));
    }

    /**
     * 新增会议管理
     */
    @PreAuthorize("@ss.hasPermi('system:meeting:add')")
    @Log(title = "会议管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody SysPropertyMeeting sysPropertyMeeting)
    {
        return toAjax(sysPropertyMeetingService.insertSysPropertyMeeting(sysPropertyMeeting));
    }

    /**
     * 修改会议管理
     */
    @PreAuthorize("@ss.hasPermi('system:meeting:edit')")
    @Log(title = "会议管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody SysPropertyMeeting sysPropertyMeeting)
    {
        return toAjax(sysPropertyMeetingService.updateSysPropertyMeeting(sysPropertyMeeting));
    }

    /**
     * 删除会议管理
     */
    @PreAuthorize("@ss.hasPermi('system:meeting:remove')")
    @Log(title = "会议管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{meetingIds}")
    public AjaxResult remove(@PathVariable Long[] meetingIds)
    {
        return toAjax(sysPropertyMeetingService.deleteSysPropertyMeetingByMeetingIds(meetingIds));
    }

    /**
     * 发送会议通知
     * 
     * @param meetingId 会议ID
     * @return 发送结果
     */
    @PreAuthorize("@ss.hasPermi('system:meeting:notify')")
    @Log(title = "会议通知", businessType = BusinessType.UPDATE)
    @PostMapping("/notify/{meetingId}")
    public AjaxResult sendMeetingNotification(@PathVariable("meetingId") Long meetingId) {
        try {
            // 1. 获取会议信息
            SysPropertyMeeting meeting = sysPropertyMeetingService.selectSysPropertyMeetingByMeetingId(meetingId);
            if (meeting == null) {
                return AjaxResult.error("会议不存在");
            }
            
            // 2. 获取所有有openId的用户
            List<SysUser> usersWithOpenId = sysUserService.selectUserListWithOpenId();
            if (usersWithOpenId == null || usersWithOpenId.isEmpty()) {
                return AjaxResult.error("没有找到有效的用户");
            }
            
            // 3. 格式化会议时间
            String meetingTime = "";
            if (meeting.getMeetingTime() != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
                meetingTime = sdf.format(meeting.getMeetingTime());
            }
            
            // 4. 发送订阅消息
            int successCount = 0;
            int failCount = 0;
            StringBuilder errorMsg = new StringBuilder();
            
            for (SysUser user : usersWithOpenId) {
                try {
                    boolean result = wechatService.sendMeetingNotification(
                        user.getOpenid(),
                        meeting.getMeetingTitle(),
                        meetingTime,
                        meeting.getMeetingLocation()
                    );
                    
                    if (result) {
                        successCount++;
                    } else {
                        failCount++;
                        errorMsg.append("用户").append(user.getNickName()).append("发送失败; ");
                    }
                } catch (Exception e) {
                    failCount++;
                    errorMsg.append("用户").append(user.getNickName()).append("发送异常: ").append(e.getMessage()).append("; ");
                }
            }
            
            // 5. 返回发送结果
            String message = String.format("会议通知发送完成！成功: %d人，失败: %d人", successCount, failCount);
            if (failCount > 0 && errorMsg.length() > 0) {
                message += "。失败详情: " + errorMsg.toString();
            }
            
            return AjaxResult.success(message);
            
        } catch (Exception e) {
            logger.error("发送会议通知失败", e);
            return AjaxResult.error("发送会议通知失败: " + e.getMessage());
        }
    }
}
