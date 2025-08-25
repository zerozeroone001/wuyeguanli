package com.ruoyi.system.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import javax.validation.constraints.NotBlank;

/**
 * 设备保修对象 sys_warranty
 *
 * @author ruoyi
 * @date 2025-08-25
 */
public class SysWarranty extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 保修ID */
    private Long warrantyId;

    /** 设备名称 */
    @Excel(name = "设备名称")
    @NotBlank(message = "设备名称不能为空")
    private String equipmentName;

    /** 设备位置 */
    @Excel(name = "设备位置")
    @NotBlank(message = "设备位置不能为空")
    private String equipmentLocation;

    /** 问题描述 */
    @Excel(name = "问题描述")
    @NotBlank(message = "问题描述不能为空")
    private String description;

    /** 上报人姓名 */
    @Excel(name = "上报人姓名")
    @NotBlank(message = "上报人姓名不能为空")
    private String reporterName;

    /** 上报人电话 */
    @Excel(name = "上报人电话")
    @NotBlank(message = "上报人电话不能为空")
    private String reporterPhone;

    /** 状态（0待处理 1处理中 2已完成） */
    @Excel(name = "状态", readConverterExp = "0=待处理,1=处理中,2=已完成")
    private String status;

    public Long getWarrantyId() {
        return warrantyId;
    }

    public void setWarrantyId(Long warrantyId) {
        this.warrantyId = warrantyId;
    }

    public String getEquipmentName() {
        return equipmentName;
    }

    public void setEquipmentName(String equipmentName) {
        this.equipmentName = equipmentName;
    }

    public String getEquipmentLocation() {
        return equipmentLocation;
    }

    public void setEquipmentLocation(String equipmentLocation) {
        this.equipmentLocation = equipmentLocation;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getReporterName() {
        return reporterName;
    }

    public void setReporterName(String reporterName) {
        this.reporterName = reporterName;
    }

    public String getReporterPhone() {
        return reporterPhone;
    }

    public void setReporterPhone(String reporterPhone) {
        this.reporterPhone = reporterPhone;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("warrantyId", getWarrantyId())
                .append("equipmentName", getEquipmentName())
                .append("equipmentLocation", getEquipmentLocation())
                .append("description", getDescription())
                .append("reporterName", getReporterName())
                .append("reporterPhone", getReporterPhone())
                .append("status", getStatus())
                .append("createBy", getCreateBy())
                .append("createTime", getCreateTime())
                .append("updateBy", getUpdateBy())
                .append("updateTime", getUpdateTime())
                .append("remark", getRemark())
                .toString();
    }
}
