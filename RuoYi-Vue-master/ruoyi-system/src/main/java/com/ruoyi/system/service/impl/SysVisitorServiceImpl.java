package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.SysVisitorMapper;
import com.ruoyi.system.domain.SysVisitor;
import com.ruoyi.system.service.ISysVisitorService;
import com.ruoyi.common.core.text.Convert;

/**
 * 访客登记Service业务层处理
 *
 * @author ruoyi
 * @date 2025-08-25
 */
@Service
public class SysVisitorServiceImpl implements ISysVisitorService {
    @Autowired
    private SysVisitorMapper sysVisitorMapper;

    /**
     * 查询访客登记
     *
     * @param visitorId 访客登记ID
     * @return 访客登记
     */
    @Override
    public SysVisitor selectSysVisitorById(Long visitorId) {
        return sysVisitorMapper.selectSysVisitorById(visitorId);
    }

    /**
     * 查询访客登记列表
     *
     * @param sysVisitor 访客登记
     * @return 访客登记
     */
    @Override
    public List<SysVisitor> selectSysVisitorList(SysVisitor sysVisitor) {
        return sysVisitorMapper.selectSysVisitorList(sysVisitor);
    }

    /**
     * 新增访客登记
     *
     * @param sysVisitor 访客登记
     * @return 结果
     */
    @Override
    public int insertSysVisitor(SysVisitor sysVisitor) {
        sysVisitor.setCreateTime(DateUtils.getNowDate());
        return sysVisitorMapper.insertSysVisitor(sysVisitor);
    }

    /**
     * 修改访客登记
     *
     * @param sysVisitor 访客登记
     * @return 结果
     */
    @Override
    public int updateSysVisitor(SysVisitor sysVisitor) {
        sysVisitor.setUpdateTime(DateUtils.getNowDate());
        return sysVisitorMapper.updateSysVisitor(sysVisitor);
    }

    /**
     * 删除访客登记对象
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    @Override
    public int deleteSysVisitorByIds(String ids) {
        return sysVisitorMapper.deleteSysVisitorByIds(Convert.toStrArray(ids));
    }

    /**
     * 删除访客登记信息
     *
     * @param visitorId 访客登记ID
     * @return 结果
     */
    @Override
    public int deleteSysVisitorById(Long visitorId) {
        return sysVisitorMapper.deleteSysVisitorById(visitorId);
    }
}
