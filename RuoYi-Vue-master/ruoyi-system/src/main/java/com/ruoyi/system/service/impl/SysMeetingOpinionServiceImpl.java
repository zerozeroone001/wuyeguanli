package com.ruoyi.system.service.impl;

import com.ruoyi.system.domain.SysMeetingOpinion;
import com.ruoyi.system.mapper.SysMeetingOpinionMapper;
import com.ruoyi.system.service.ISysMeetingOpinionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysMeetingOpinionServiceImpl implements ISysMeetingOpinionService {

    @Autowired
    private SysMeetingOpinionMapper opinionMapper;

    @Override
    public int submitOpinion(SysMeetingOpinion opinion) {
        return opinionMapper.insertOpinion(opinion);
    }
}
