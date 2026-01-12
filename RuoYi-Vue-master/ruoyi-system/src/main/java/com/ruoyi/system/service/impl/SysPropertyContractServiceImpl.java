package com.ruoyi.system.service.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.utils.CommunityUtils;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.domain.SysContractFile;
import com.ruoyi.system.domain.SysFileInfo;
import com.ruoyi.system.mapper.SysContractFileMapper;
import com.ruoyi.system.service.ISysFileInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.SysPropertyContractMapper;
import com.ruoyi.system.domain.SysPropertyContract;
import com.ruoyi.system.service.ISysPropertyContractService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 物业服务合同Service业务层处理
 * 
 * @author ruoyi
 * @date 2025-08-21
 */
@Service
public class SysPropertyContractServiceImpl implements ISysPropertyContractService 
{
    @Autowired
    private SysPropertyContractMapper sysPropertyContractMapper;

    @Autowired
    private ISysFileInfoService sysFileInfoService;

    @Autowired
    private SysContractFileMapper sysContractFileMapper;

    /**
     * 查询物业服务合同
     * 
     * @param contractId 物业服务合同主键
     * @return 物业服务合同
     */
    @Override
    public SysPropertyContract selectSysPropertyContractByContractId(Long contractId)
    {
        SysPropertyContract contract = sysPropertyContractMapper.selectSysPropertyContractByContractId(contractId);
        if (contract != null)
        {
            // 数据归属校验，防止跨小区读取
            CommunityUtils.checkCommunityPermission(contract.getCommunityId());
            List<SysFileInfo> fileList = sysFileInfoService.selectSysFileInfoListByContractId(contractId);
            contract.setFileList(fileList);
        }
        return contract;
    }

    /**
     * 查询物业服务合同列表
     * 
     * @param sysPropertyContract 物业服务合同
     * @return 物业服务合同
     */
    @Override
    public List<SysPropertyContract> selectSysPropertyContractList(SysPropertyContract sysPropertyContract)
    {
        return sysPropertyContractMapper.selectSysPropertyContractList(sysPropertyContract);
    }

    /**
     * 新增物业服务合同
     * 
     * @param sysPropertyContract 物业服务合同
     * @return 结果
     */
    @Override
    @Transactional
    public int insertSysPropertyContract(SysPropertyContract sysPropertyContract)
    {
        validateCommunityScope(sysPropertyContract.getCommunityId());
        sysPropertyContract.setCreateTime(DateUtils.getNowDate());
        
        // 自动生成合同编号（如果未提供）
        if (sysPropertyContract.getContractNo() == null || sysPropertyContract.getContractNo().isEmpty())
        {
            sysPropertyContract.setContractNo(generateContractNo());
        }
        
        // 1. 插入主表
        int rows = sysPropertyContractMapper.insertSysPropertyContract(sysPropertyContract);
        // 2. 插入关联文件
        insertContractFiles(sysPropertyContract);
        return rows;
    }

    /**
     * 修改物业服务合同
     * 
     * @param sysPropertyContract 物业服务合同
     * @return 结果
     */
    @Override
    @Transactional
    public int updateSysPropertyContract(SysPropertyContract sysPropertyContract)
    {
        validateCommunityScope(sysPropertyContract.getCommunityId());
        sysPropertyContract.setUpdateTime(DateUtils.getNowDate());
        // 1. 删除旧的文件关联
        sysContractFileMapper.deleteByContractId(sysPropertyContract.getContractId());
        // 2. 插入新的文件关联
        insertContractFiles(sysPropertyContract);
        // 3. 更新主表
        return sysPropertyContractMapper.updateSysPropertyContract(sysPropertyContract);
    }

    /**
     * 批量删除物业服务合同
     * 
     * @param contractIds 需要删除的物业服务合同主键
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteSysPropertyContractByContractIds(Long[] contractIds)
    {
        for (Long contractId : contractIds)
        {
            SysPropertyContract contract = sysPropertyContractMapper.selectSysPropertyContractByContractId(contractId);
            if (contract != null)
            {
                CommunityUtils.checkCommunityPermission(contract.getCommunityId());
            }
            sysContractFileMapper.deleteByContractId(contractId);
        }
        return sysPropertyContractMapper.deleteSysPropertyContractByContractIds(contractIds);
    }

    /**
     * 删除物业服务合同信息
     * 
     * @param contractId 物业服务合同主键
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteSysPropertyContractByContractId(Long contractId)
    {
        SysPropertyContract contract = sysPropertyContractMapper.selectSysPropertyContractByContractId(contractId);
        if (contract != null)
        {
            CommunityUtils.checkCommunityPermission(contract.getCommunityId());
        }
        sysContractFileMapper.deleteByContractId(contractId);
        return sysPropertyContractMapper.deleteSysPropertyContractByContractId(contractId);
    }

    /**
     * 新增合同与文件关联信息
     *
     * @param contract 合同对象
     */
    public void insertContractFiles(SysPropertyContract contract)
    {
        List<Long> fileIds = contract.getFileIds();
        if (fileIds != null && !fileIds.isEmpty())
        {
            List<SysContractFile> list = new ArrayList<>();
            for (Long fileId : fileIds)
            {
                list.add(new SysContractFile(contract.getContractId(), fileId));
            }
            if (list.size() > 0)
            {
                sysContractFileMapper.batchInsert(list);
            }
        }
    }

    /**
     * 校验小区ID有效性并执行权限检查。
     *
     * @param communityId 目标小区ID
     */
    private void validateCommunityScope(Long communityId)
    {
        if (communityId == null)
        {
            throw new ServiceException("合同数据必须绑定所属小区");
        }
        CommunityUtils.checkCommunityPermission(communityId);
    }

    /**
     * 生成合同编号
     * 格式：HT + 年月日(8位) + 随机数(6位)
     * 例如：HT20250107123456
     *
     * @return 合同编号
     */
    private String generateContractNo()
    {
        // 获取当前日期，格式：yyyyMMdd
        String dateStr = DateUtils.parseDateToStr("yyyyMMdd", new Date());
        
        // 生成6位随机数
        int randomNum = (int) ((Math.random() * 900000) + 100000);
        
        // 拼接合同编号：HT + 日期 + 随机数
        return "HT" + dateStr + randomNum;
    }

    /**
     * 更新合同阶段
     * 
     * @param contractId 合同ID
     * @param newStage 新阶段
     * @return 结果
     */
    @Override
    public int updateContractStage(Long contractId, String newStage)
    {
        SysPropertyContract contract = sysPropertyContractMapper.selectSysPropertyContractByContractId(contractId);
        if (contract == null)
        {
            throw new ServiceException("合同不存在");
        }
        // 校验权限
        validateCommunityScope(contract.getCommunityId());
        
        // 更新阶段
        SysPropertyContract updateContract = new SysPropertyContract();
        updateContract.setContractId(contractId);
        updateContract.setContractStage(newStage);
        updateContract.setUpdateTime(DateUtils.getNowDate());
        
        return sysPropertyContractMapper.updateSysPropertyContract(updateContract);
    }

    /**
     * 审核合同（待审核 -> 待制作查验清单）
     */
    @Override
    public int reviewContract(Long contractId)
    {
        SysPropertyContract contract = sysPropertyContractMapper.selectSysPropertyContractByContractId(contractId);
        if (contract == null)
        {
            throw new ServiceException("合同不存在");
        }
        validateCommunityScope(contract.getCommunityId());
        
        // 校验当前状态
        if (!"pending_accept".equals(contract.getContractStage()))
        {
            throw new ServiceException("当前合同状态不是待审核，无法执行审核操作");
        }
        
        SysPropertyContract updateContract = new SysPropertyContract();
        updateContract.setContractId(contractId);
        updateContract.setContractStage("pending_entry_checklist");
        updateContract.setCompleteTime(DateUtils.getNowDate());
        updateContract.setUpdateTime(DateUtils.getNowDate());
        
        return sysPropertyContractMapper.updateSysPropertyContract(updateContract);
    }

    /**
     * 上传查验清单（待制作查验清单 -> 待上传物业查验清单）
     */
    @Override
    public int uploadEntryChecklist(Long contractId, String fileUrl)
    {
        SysPropertyContract contract = sysPropertyContractMapper.selectSysPropertyContractByContractId(contractId);
        if (contract == null)
        {
            throw new ServiceException("合同不存在");
        }
        validateCommunityScope(contract.getCommunityId());
        
        if (!"pending_entry_checklist".equals(contract.getContractStage()))
        {
            throw new ServiceException("当前合同状态不是待制作查验清单，无法上传查验清单");
        }
        
        SysPropertyContract updateContract = new SysPropertyContract();
        updateContract.setContractId(contractId);
        updateContract.setEntryChecklistUrl(fileUrl);
        updateContract.setContractStage("entry_checklist_uploaded");
        updateContract.setUpdateTime(DateUtils.getNowDate());
        
        return sysPropertyContractMapper.updateSysPropertyContract(updateContract);
    }

    /**
     * 上传物业查验清单（待上传物业查验清单 -> 待上传合同月履行清单）
     */
    @Override
    public int uploadPropertyEntryChecklist(Long contractId, String fileUrl)
    {
        SysPropertyContract contract = sysPropertyContractMapper.selectSysPropertyContractByContractId(contractId);
        if (contract == null)
        {
            throw new ServiceException("合同不存在");
        }
        validateCommunityScope(contract.getCommunityId());
        
        if (!"entry_checklist_uploaded".equals(contract.getContractStage()))
        {
            throw new ServiceException("当前合同状态不是待上传物业查验清单，无法上传物业查验清单");
        }
        
        SysPropertyContract updateContract = new SysPropertyContract();
        updateContract.setContractId(contractId);
        updateContract.setPropertyEntryChecklistUrl(fileUrl);
        updateContract.setContractStage("pending_monthly_checklist");
        updateContract.setUpdateTime(DateUtils.getNowDate());
        
        return sysPropertyContractMapper.updateSysPropertyContract(updateContract);
    }

    /**
     * 上传合同月履行清单（待上传合同月履行清单 -> 待上传物业月履行清单）
     */
    @Override
    public int uploadMonthlyChecklist(Long contractId, String fileUrl)
    {
        SysPropertyContract contract = sysPropertyContractMapper.selectSysPropertyContractByContractId(contractId);
        if (contract == null)
        {
            throw new ServiceException("合同不存在");
        }
        validateCommunityScope(contract.getCommunityId());
        
        if (!"pending_monthly_checklist".equals(contract.getContractStage()))
        {
            throw new ServiceException("当前合同状态不是待上传合同月履行清单，无法上传合同月履行清单");
        }
        
        SysPropertyContract updateContract = new SysPropertyContract();
        updateContract.setContractId(contractId);
        updateContract.setMonthlyChecklistUrl(fileUrl);
        updateContract.setContractStage("monthly_checklist_uploaded");
        updateContract.setUpdateTime(DateUtils.getNowDate());
        
        return sysPropertyContractMapper.updateSysPropertyContract(updateContract);
    }

    /**
     * 上传物业月履行清单（待上传物业月履行清单 -> 合同月履行清单待审核）
     */
    @Override
    public int uploadPropertyMonthlyChecklist(Long contractId, String fileUrl)
    {
        SysPropertyContract contract = sysPropertyContractMapper.selectSysPropertyContractByContractId(contractId);
        if (contract == null)
        {
            throw new ServiceException("合同不存在");
        }
        validateCommunityScope(contract.getCommunityId());
        
        if (!"monthly_checklist_uploaded".equals(contract.getContractStage()))
        {
            throw new ServiceException("当前合同状态不是待上传物业月履行清单，无法上传物业月履行清单");
        }
        
        SysPropertyContract updateContract = new SysPropertyContract();
        updateContract.setContractId(contractId);
        updateContract.setPropertyMonthlyChecklistUrl(fileUrl);
        updateContract.setContractStage("monthly_pending_review");
        updateContract.setUpdateTime(DateUtils.getNowDate());
        
        return sysPropertyContractMapper.updateSysPropertyContract(updateContract);
    }


    /**
     * 月履行清单审核
     * 完全履行 -> 待考核
     * 未履行/未完全履行 -> 待上传整改通知单
     */
    @Override
    public int reviewMonthlyChecklist(Long contractId, String result)
    {
        SysPropertyContract contract = sysPropertyContractMapper.selectSysPropertyContractByContractId(contractId);
        if (contract == null)
        {
            throw new ServiceException("合同不存在");
        }
        validateCommunityScope(contract.getCommunityId());
        
        if (!"monthly_pending_review".equals(contract.getContractStage()))
        {
            throw new ServiceException("当前合同状态不是合同月履行清单待审核，无法执行审核操作");
        }
        
        SysPropertyContract updateContract = new SysPropertyContract();
        updateContract.setContractId(contractId);
        updateContract.setPerformanceStatus(result);
        updateContract.setUpdateTime(DateUtils.getNowDate());
        
        if ("complete".equals(result))
        {
            // 完全履行 -> 待考核
            updateContract.setContractStage("pending_assessment");
        }
        else
        {
            // 未履行/未完全履行 -> 待上传整改通知单
            updateContract.setContractStage("pending_rectification_notice");
        }
        
        return sysPropertyContractMapper.updateSysPropertyContract(updateContract);
    }

    /**
     * 上传整改通知单（待上传整改通知单 -> 待上传整改结果告知单）
     */
    @Override
    public int uploadRectificationNotice(Long contractId, String fileUrl)
    {
        SysPropertyContract contract = sysPropertyContractMapper.selectSysPropertyContractByContractId(contractId);
        if (contract == null)
        {
            throw new ServiceException("合同不存在");
        }
        validateCommunityScope(contract.getCommunityId());
        
        if (!"pending_rectification_notice".equals(contract.getContractStage()))
        {
            throw new ServiceException("当前合同状态不是待上传整改通知单，无法上传整改通知单");
        }
        
        SysPropertyContract updateContract = new SysPropertyContract();
        updateContract.setContractId(contractId);
        updateContract.setRectificationNoticeUrl(fileUrl);
        updateContract.setContractStage("pending_rectification_result");
        updateContract.setUpdateTime(DateUtils.getNowDate());
        
        return sysPropertyContractMapper.updateSysPropertyContract(updateContract);
    }

    /**
     * 上传整改结果评定通知书（待上传整改结果告知单 -> 待上传年度履行报告）
     */
    @Override
    public int uploadRectificationResult(Long contractId, String fileUrl)
    {
        SysPropertyContract contract = sysPropertyContractMapper.selectSysPropertyContractByContractId(contractId);
        if (contract == null)
        {
            throw new ServiceException("合同不存在");
        }
        validateCommunityScope(contract.getCommunityId());
        
        if (!"pending_rectification_result".equals(contract.getContractStage()))
        {
            throw new ServiceException("当前合同状态不是待上传整改结果告知单，无法上传整改结果评定通知书");
        }
        
        SysPropertyContract updateContract = new SysPropertyContract();
        updateContract.setContractId(contractId);
        updateContract.setRectificationResultUrl(fileUrl);
        updateContract.setContractStage("pending_annual_report");
        updateContract.setUpdateTime(DateUtils.getNowDate());
        
        return sysPropertyContractMapper.updateSysPropertyContract(updateContract);
    }

    /**
     * 上传年度履行报告（待上传年度履行报告 -> 待考核）
     */
    @Override
    public int uploadAnnualReport(Long contractId, String fileUrl)
    {
        SysPropertyContract contract = sysPropertyContractMapper.selectSysPropertyContractByContractId(contractId);
        if (contract == null)
        {
            throw new ServiceException("合同不存在");
        }
        validateCommunityScope(contract.getCommunityId());
        
        if (!"pending_annual_report".equals(contract.getContractStage()))
        {
            throw new ServiceException("当前合同状态不是待上传年度履行报告，无法上传年度履行报告");
        }
        
        SysPropertyContract updateContract = new SysPropertyContract();
        updateContract.setContractId(contractId);
        updateContract.setAnnualReportUrl(fileUrl);
        updateContract.setContractStage("pending_assessment");
        updateContract.setUpdateTime(DateUtils.getNowDate());
        
        return sysPropertyContractMapper.updateSysPropertyContract(updateContract);
    }
}

