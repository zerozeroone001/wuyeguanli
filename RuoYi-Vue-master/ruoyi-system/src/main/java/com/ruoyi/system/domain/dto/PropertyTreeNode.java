package com.ruoyi.system.domain.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * 房产树形节点DTO
 * 用于前端级联选择器展示楼栋→单元→房号的树形结构
 * 
 * @author ruoyi
 */
public class PropertyTreeNode
{
    /** 节点值(楼栋名/单元名/房号) */
    private String value;

    /** 节点显示文本 */
    private String label;

    /** 子节点列表 */
    private List<PropertyTreeNode> children;

    /** 是否禁用(已绑定的房产) */
    private Boolean disabled;

    /** 层级(1=楼栋, 2=单元, 3=房号) */
    private Integer level;

    /** 房产ID(仅叶子节点有效) */
    private Long propertyId;

    public PropertyTreeNode()
    {
        this.children = new ArrayList<>();
        this.disabled = false;
    }

    public PropertyTreeNode(String value, String label, Integer level)
    {
        this.value = value;
        this.label = label;
        this.level = level;
        this.children = new ArrayList<>();
        this.disabled = false;
    }

    public String getValue()
    {
        return value;
    }

    public void setValue(String value)
    {
        this.value = value;
    }

    public String getLabel()
    {
        return label;
    }

    public void setLabel(String label)
    {
        this.label = label;
    }

    public List<PropertyTreeNode> getChildren()
    {
        return children;
    }

    public void setChildren(List<PropertyTreeNode> children)
    {
        this.children = children;
    }

    public Boolean getDisabled()
    {
        return disabled;
    }

    public void setDisabled(Boolean disabled)
    {
        this.disabled = disabled;
    }

    public Integer getLevel()
    {
        return level;
    }

    public void setLevel(Integer level)
    {
        this.level = level;
    }

    public Long getPropertyId()
    {
        return propertyId;
    }

    public void setPropertyId(Long propertyId)
    {
        this.propertyId = propertyId;
    }

    /**
     * 添加子节点
     */
    public void addChild(PropertyTreeNode child)
    {
        if (this.children == null)
        {
            this.children = new ArrayList<>();
        }
        this.children.add(child);
    }

    /**
     * 判断是否有子节点
     */
    public boolean hasChildren()
    {
        return this.children != null && !this.children.isEmpty();
    }
}
