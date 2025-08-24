
package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.SysHandover;

/**
 * 承接查验Mapper接口
 * 
 * @author ruoyi
 * @date 2025-08-22
 */
public interface SysHandoverMapper 
{
    /**
     * 查询承接查验列表
     * 
     * @param sysHandover 承接查验
     * @return 承接查验集合
     */
    public List<SysHandover> selectHandoverList(SysHandover sysHandover);
}
