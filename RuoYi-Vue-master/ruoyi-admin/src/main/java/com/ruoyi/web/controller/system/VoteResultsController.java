package com.ruoyi.web.controller.system;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.vo.VoteResultVO;
import com.ruoyi.system.mapper.SysMeetingVoteMapper;
import com.ruoyi.system.service.ISysMeetingVoteService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 表决结果Controller
 *
 * @author ruoyi
 */
@RestController
@RequestMapping("/system/voteResults")
public class VoteResultsController extends BaseController
{
    private static final Logger log = LoggerFactory.getLogger(VoteResultsController.class);

    @Autowired
    private SysMeetingVoteMapper meetingVoteMapper;
    
    @Autowired
    private ISysMeetingVoteService meetingVoteService;

    /**
     * 查询会议表决结果统计
     */
    @GetMapping("/list")
    public AjaxResult list(
            @RequestParam Long meetingId,
            @RequestParam Long communityId)
    {
        List<VoteResultVO> list = meetingVoteMapper.selectVoteResultsByMeeting(meetingId, communityId);
        log.info("selectVoteResultsByMeeting returned {} results for meetingId: {} and communityId: {}", list.size(), meetingId, communityId);
        return AjaxResult.success(list);
    }

    /**
     * 导出投票列表
     */
    @GetMapping("/export")
    public void export(HttpServletResponse response, @RequestParam Long meetingId, @RequestParam Long communityId)
    {
        List<com.ruoyi.system.domain.vo.VoteListExportVO> list = meetingVoteService.selectVoteListForExport(meetingId, communityId);
        ExcelUtil<com.ruoyi.system.domain.vo.VoteListExportVO> util = new ExcelUtil<com.ruoyi.system.domain.vo.VoteListExportVO>(com.ruoyi.system.domain.vo.VoteListExportVO.class);
        util.exportExcel(response, list, "投票结果列表");
    }
}
