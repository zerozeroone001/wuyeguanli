
package com.ruoyi.system.domain;

import com.ruoyi.common.core.domain.BaseEntity;

public class SysSupervisionExpert extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    private Long id;

    private String name;

    private String title;

    private String avatar;

    private String specialties;

    private Integer supervisionCount;

    private Double rating;

    private String experience;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }
    public void setTitle(String title)
    {
        this.title = title;
    }

    public String getTitle()
    {
        return title;
    }
    public void setAvatar(String avatar)
    {
        this.avatar = avatar;
    }

    public String getAvatar()
    {
        return avatar;
    }
    public void setSpecialties(String specialties)
    {
        this.specialties = specialties;
    }

    public String getSpecialties()
    {
        return specialties;
    }
    public void setSupervisionCount(Integer supervisionCount)
    {
        this.supervisionCount = supervisionCount;
    }

    public Integer getSupervisionCount()
    {
        return supervisionCount;
    }
    public void setRating(Double rating)
    {
        this.rating = rating;
    }

    public Double getRating()
    {
        return rating;
    }
    public void setExperience(String experience)
    {
        this.experience = experience;
    }

    public String getExperience()
    {
        return experience;
    }
}
