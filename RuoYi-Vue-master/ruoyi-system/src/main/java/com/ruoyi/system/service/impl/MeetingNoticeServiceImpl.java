package com.ruoyi.system.service.impl;

import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.domain.SysMeetingNotice;
import com.ruoyi.system.domain.SysMeetingVote;
import com.ruoyi.system.domain.SysOwnerProfile;
import com.ruoyi.system.domain.SysPropertyMeeting;
import com.ruoyi.system.domain.vo.VoteRecordVO;
import com.ruoyi.system.mapper.SysMeetingNoticeMapper;
import com.ruoyi.system.mapper.SysMeetingVoteMapper;
import com.ruoyi.system.mapper.SysOwnerProfileMapper;
import com.ruoyi.system.mapper.SysPropertyMeetingMapper;
import com.ruoyi.system.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class MeetingNoticeServiceImpl implements MeetingNoticeService {
    private static final Logger log = LoggerFactory.getLogger(MeetingNoticeServiceImpl.class);
    @Autowired
    private ISysPropertyMeetingService sysPropertyMeetingService;
    @Autowired
    private ISysUserService sysUserService;
    @Autowired
    private WechatService wechatService;
    @Autowired
    private SmsService smsService;
    @Autowired
    private SysMeetingNoticeMapper sysMeetingNoticeMapper;
    @Autowired
    private SysMeetingVoteMapper sysMeetingVoteMapper;
    @Autowired
    private SysOwnerProfileMapper sysOwnerProfileMapper;
    @Autowired
    private PhoneService phoneService;

    // 通知类型常量 (根据数据库定义映射)
    private static final int TYPE_SMS = 0;
    private static final int TYPE_PHONE = 1;
    private static final int TYPE_VISIT = 2;
    private static final int TYPE_WECHAT = 3; // 自定义：微信通知

    @Override
    public boolean notice(Long meetingId) {
        try {
            // 1. 获取会议信息
            SysPropertyMeeting meeting = sysPropertyMeetingService.selectSysPropertyMeetingByMeetingId(meetingId);
            if (meeting == null) {
                return false;
            }
            Long communityId = meeting.getCommunityId();

            // 2. 获取本小区所有业主信息
            SysOwnerProfile queryOwner = new SysOwnerProfile();
            queryOwner.setCommunityId(communityId);
            List<SysOwnerProfile> owners = sysOwnerProfileMapper.selectSysOwnerProfileList(queryOwner);
            if (owners == null || owners.isEmpty()) {
                return true;
            }
            log.info("业主数：" + (owners.size()));

            // 3. 查询已投票用户ID集合
            // 从 sys_meeting_vote 表中查询 is_vote = 1 的用户（已完成投票的用户）
            SysMeetingVote voteQuery = new SysMeetingVote();
            voteQuery.setMeetingId(meetingId);
            List<SysMeetingVote> votes = sysMeetingVoteMapper.selectSysMeetingUnVoteUserList(voteQuery);
            Set<Long> votedUserIds = votes != null ? votes.stream().map(SysMeetingVote::getUserId).collect(Collectors.toSet()) : new HashSet<>();
            log.info("已投票业主数：" + votedUserIds.size());
            log.info("需要通知未投票业主数：" + (owners.size() - votedUserIds.size()));
            // 4. 获取已通知记录 (按用户ID分组)
            SysMeetingNotice queryNotice = new SysMeetingNotice();
            queryNotice.setMeetingId(meetingId);
            List<SysMeetingNotice> notices = sysMeetingNoticeMapper.selectSysMeetingNoticeList(queryNotice);
            Map<Long, Set<Integer>> notifiedMap = new HashMap<>();
            if (notices != null) {
                for (SysMeetingNotice n : notices) {
                    notifiedMap.computeIfAbsent(n.getUserId(), k -> new HashSet<>()).add(n.getVoteType());
                }
            }

            // 5. 格式化会议时间
            String meetingTimeStr = "";
            if (meeting.getMeetingTime() != null) {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
                meetingTimeStr = sdf.format(meeting.getMeetingTime());
            }

            // 6. 遍历业主发送通知
            for (SysOwnerProfile owner : owners) {
                Long userId = owner.getUserId();
                if (userId == null) continue;

                // 如果已投票，跳过
                if (votedUserIds.contains(userId)) continue;

                Set<Integer> userNotifiedTypes = notifiedMap.getOrDefault(userId, new HashSet<>());

                // 获取用户详细信息 (为了openid)
                SysUser user = sysUserService.selectUserById(userId);
                String openId = user != null ? user.getOpenid() : null;
                String phone = owner.getPhonenumber();
                if (StringUtils.isEmpty(phone) && user != null) {
                    phone = user.getPhonenumber();
                }

                // --- 1. 微信订阅消息通知 ---
                if (!userNotifiedTypes.contains(TYPE_WECHAT) && StringUtils.isNotEmpty(openId)) {
                    try {
                        boolean success = wechatService.sendMeetingDetailNotification(
                                openId,
                                meeting.getMeetingTitle(),
                                meetingTimeStr,
                                meeting.getMeetingLocation()
                        );
                        if (success) {
                            recordNotice(meetingId, userId, owner.getUserName(), TYPE_WECHAT, "微信通知成功");
                            log.info("微信通知：" + user.getUserName());
                        }
                    } catch (Exception e) {
                        // ignore
                    }
                }

                // --- 2. 短信通知 ---
                if (!userNotifiedTypes.contains(TYPE_SMS) && StringUtils.isNotEmpty(phone)) {
                    try {
                        boolean success = smsService.sendSingleMeetingSms(phone, meetingId);
                        if (success) {
                            recordNotice(meetingId, userId, owner.getUserName(), TYPE_SMS, "短信通知成功");
                            log.info("短信通知：" + user.getUserName());
                        }
                    } catch (Exception e) {
                        // ignore
                    }
                }

                // --- 3. 电话通知 ---
                if (!userNotifiedTypes.contains(TYPE_PHONE) && StringUtils.isNotEmpty(phone)) {
                    try {
                        boolean success = phoneService.sendSingleMeetingPhone(phone, meetingId);
                        if (success) {
                            recordNotice(meetingId, userId, owner.getUserName(), TYPE_PHONE, "电话通知成功");
                            log.info("电话通知：" + user.getUserName());
                        }
                    } catch (Exception e) {
                        // ignore
                    }
                }
            }

            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    private void recordNotice(Long meetingId, Long userId, String userName, int type, String remark) {
        SysMeetingNotice notice = new SysMeetingNotice();
        notice.setMeetingId(meetingId);
        notice.setUserId(userId);
        notice.setUserName(userName);
        notice.setVoteType(type);
        notice.setVoteOption(type); // 冗余字段同步
        notice.setFlieUrl(remark); // 借用字段存储备注
        notice.setCreateTime(DateUtils.getNowDate());
        notice.setUpdateTime(DateUtils.getNowDate());
        sysMeetingNoticeMapper.insertSysMeetingNotice(notice);
    }
}
