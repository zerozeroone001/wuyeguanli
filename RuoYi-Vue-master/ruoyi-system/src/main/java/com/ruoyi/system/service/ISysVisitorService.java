package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.SysVisitor;

/**
 * 访客登记Service接口
 *
 * @author ruoyi
 * @date 2025-08-25
 */
public interface ISysVisitorService {
    /**
     * 查询访客登记
     *
     * @param visitorId 访客登记ID
     * @return 访客登记
     */
    public SysVisitor selectSysVisitorById(Long visitorId);

    /**
     * 查询访客登记列表
     *
     * @param sysVisitor 访客登记
     * @return 访客登记集合
     */
    public List<SysVisitor> selectSysVisitorList(SysVisitor sysVisitor);

    /**
     * 新增访客登记
     *
     * @param sysVisitor 访客登记
     * @return 结果
     */
    public int insertSysVisitor(SysVisitor sysVisitor);

    /**
     * 修改访客登记
     *
     * @param sysVisitor 访客登记
     * @return 结果
     */
    public int updateSysVisitor(SysVisitor sysVisitor);

    /**
     * 批量删除访客登记
     *
     * @param ids 需要删除的数据ID
     * @return 结果
     */
    public int deleteSysVisitorByIds(String ids);

    /**
     * 删除访客登记信息
     *
     * @param visitorId 访客登记ID
     * @return 结果
     */
    public int deleteSysVisitorById(Long visitorId);
}
