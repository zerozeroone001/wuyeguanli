package com.ruoyi.web.controller.system;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.system.domain.vo.UserVoteDetailVO;
import com.ruoyi.system.domain.vo.VoteRecordVO;
import com.ruoyi.system.mapper.SysMeetingVoteMapper;

/**
 * 投票记录Controller
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/system/voteRecords")
public class VoteRecordsController extends BaseController
{
    @Autowired
    private SysMeetingVoteMapper meetingVoteMapper;

    /**
     * 查询投票记录列表
     */
    @GetMapping("/list")
    public TableDataInfo list(
            @RequestParam(required = false) Long meetingId,
            @RequestParam(required = false) Long communityId,
            @RequestParam(required = false) String userName,
            @RequestParam(required = false) String voteStatus)
    {
        startPage();
        List<VoteRecordVO> list = meetingVoteMapper.selectVoteRecordsList(meetingId, communityId, userName, voteStatus);
        return getDataTable(list);
    }

    /**
     * 查询用户投票详情
     */
    @GetMapping("/detail")
    public TableDataInfo detail(
            @RequestParam Long meetingId,
            @RequestParam Long userId)
    {
        List<UserVoteDetailVO> list = meetingVoteMapper.selectUserVoteDetails(meetingId, userId);
        return getDataTable(list);
    }
}
