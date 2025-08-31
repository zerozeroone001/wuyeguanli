package com.ruoyi.web.controller.system;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.domain.SysFileInfo;
import com.ruoyi.system.service.ISysFileInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * 通用文件上传接口
 * 
 * @author ruoyi
 */
@RestController
@RequestMapping("/system/file")
public class SysFileInfoController {

    @Autowired
    private ISysFileInfoService sysFileInfoService;

    /**
     * 通用上传请求
     */
    @PostMapping("/upload")
    public AjaxResult uploadFile(MultipartFile file)
    {
        try
        {
            // 上传文件并保存文件信息
            SysFileInfo fileInfo = sysFileInfoService.uploadFile(file);
            return AjaxResult.success(fileInfo);
        }
        catch (Exception e)
        {
            return AjaxResult.error(e.getMessage());
        }
    }
}
