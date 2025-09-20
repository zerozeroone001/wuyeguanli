package com.ruoyi.system.service.impl;

import java.util.List;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.system.domain.dto.OwnerProfileImportDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.ruoyi.system.mapper.SysOwnerProfileMapper;
import com.ruoyi.system.domain.SysOwnerProfile;
import com.ruoyi.system.service.ISysOwnerProfileService;

/**
 * 业主信息扩展Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-09-15
 */
@Service
public class SysOwnerProfileServiceImpl implements ISysOwnerProfileService 
{
    private static final Logger log = LoggerFactory.getLogger(SysOwnerProfileServiceImpl.class);

    @Autowired
    private SysOwnerProfileMapper sysOwnerProfileMapper;

    @Override
    public SysOwnerProfile selectSysOwnerProfileByOwnerId(Long ownerId)
    {
        return sysOwnerProfileMapper.selectSysOwnerProfileByOwnerId(ownerId);
    }

    @Override
    public List<SysOwnerProfile> selectSysOwnerProfileList(SysOwnerProfile sysOwnerProfile)
    {
        return sysOwnerProfileMapper.selectSysOwnerProfileList(sysOwnerProfile);
    }

    @Override
    @Transactional
    public int insertSysOwnerProfile(SysOwnerProfile sysOwnerProfile)
    {
        sysOwnerProfile.setCreateTime(DateUtils.getNowDate());
        return sysOwnerProfileMapper.insertSysOwnerProfile(sysOwnerProfile);
    }

    @Override
    @Transactional
    public int updateSysOwnerProfile(SysOwnerProfile sysOwnerProfile)
    {
        sysOwnerProfile.setUpdateTime(DateUtils.getNowDate());
        return sysOwnerProfileMapper.updateSysOwnerProfile(sysOwnerProfile);
    }

    @Override
    @Transactional
    public int deleteSysOwnerProfileByOwnerIds(Long[] ownerIds)
    {
        return sysOwnerProfileMapper.deleteSysOwnerProfileByOwnerIds(ownerIds);
    }

    @Override
    @Transactional
    public int deleteSysOwnerProfileByOwnerId(Long ownerId)
    {
        return sysOwnerProfileMapper.deleteSysOwnerProfileByOwnerId(ownerId);
    }

    @Override
    public String importOwner(List<OwnerProfileImportDto> ownerList, boolean isUpdateSupport, String operName)
    {
        if (StringUtils.isNull(ownerList) || ownerList.size() == 0)
        {
            throw new ServiceException("导入业主数据不能为空！");
        }
        int successNum = 0;
        int failureNum = 0;
        StringBuilder successMsg = new StringBuilder();
        StringBuilder failureMsg = new StringBuilder();
        for (OwnerProfileImportDto dto : ownerList)
        {
            try
            {
                // 根据用户最新要求，不进行重复校验，直接插入
                SysOwnerProfile newProfile = new SysOwnerProfile();

                // 拼接备注字段
                StringBuilder remarkBuilder = new StringBuilder();
                if (StringUtils.isNotEmpty(dto.getCommunityName())) {
                    remarkBuilder.append("小区：").append(dto.getCommunityName()).append(" ");
                }
                if (StringUtils.isNotEmpty(dto.getHouseNumber())) {
                    remarkBuilder.append("门牌号：").append(dto.getHouseNumber()).append(" ");
                }
                if (StringUtils.isNotEmpty(dto.getRemark())) {
                    remarkBuilder.append("原备注：").append(dto.getRemark());
                }

                newProfile.setRealName(dto.getRealName());
                newProfile.setPhonenumber(dto.getPhonenumber());
                newProfile.setIdCardNo(dto.getIdCardNo());
                newProfile.setRemark(remarkBuilder.toString());
                newProfile.setCreateBy(operName);
                
                this.insertSysOwnerProfile(newProfile);
                successNum++;
                successMsg.append("<br/>" + successNum + "、身份证号 " + dto.getIdCardNo() + " 的数据导入成功");
            }
            catch (Exception e)
            {
                failureNum++;
                String msg = "<br/>" + failureNum + "、身份证号 " + dto.getIdCardNo() + " 的数据导入失败：";
                failureMsg.append(msg + e.getMessage());
                log.error(msg, e);
            }
        }
        if (failureNum > 0)
        {
            failureMsg.insert(0, "导入结果不完整！ " + successNum + " 条数据成功，" + failureNum + " 条数据失败，错误如下：");
            throw new ServiceException(failureMsg.toString());
        }
        else
        {
            successMsg.insert(0, "恭喜您，数据已全部导入成功！共 " + successNum + " 条。");
        }
        return successMsg.toString();
    }
}
