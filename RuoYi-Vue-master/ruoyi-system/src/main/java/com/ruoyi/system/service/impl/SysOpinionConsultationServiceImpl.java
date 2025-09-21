package com.ruoyi.system.service.impl;

import com.ruoyi.system.domain.SysOpinionConsultation;
import com.ruoyi.system.mapper.SysOpinionConsultationMapper;
import com.ruoyi.system.service.ISysOpinionConsultationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysOpinionConsultationServiceImpl implements ISysOpinionConsultationService {

    @Autowired
    private SysOpinionConsultationMapper consultationMapper;

    @Override
    public int insertOpinionConsultation(SysOpinionConsultation consultation) {
        return consultationMapper.insertOpinionConsultation(consultation);
    }
}