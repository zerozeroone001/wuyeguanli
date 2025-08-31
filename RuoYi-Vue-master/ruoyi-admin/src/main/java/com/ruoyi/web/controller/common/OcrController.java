package com.ruoyi.web.controller.common;

import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.file.FileUploadUtils;
import com.ruoyi.common.utils.file.FileUtils;
import com.ruoyi.framework.config.ServerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

/**
 * OCR识别处理
 * 
 * @author ruoyi
 */
@RestController
@RequestMapping("/common/ocr")
public class OcrController
{

    @Autowired
    private ServerConfig serverConfig;

    /**
     * 身份证识别
     */
    @PostMapping("/idcard")
    public AjaxResult recognizeIdCard(@RequestParam("file") MultipartFile file, @RequestParam("side") String side)
    {
        try
        {
            // 上传文件到服务器
            String filePath = RuoYiConfig.getUploadPath();
            String fileName = FileUploadUtils.upload(filePath, file);
            String url = serverConfig.getUrl() + fileName;

            // 调用阿里云OCR
//            Client client = aliyunOcrConfig.createClient();
//            RecognizeIdcardRequest request = new RecognizeIdcardRequest();
//            request.setUrl(url); // 使用公网URL
//            // 如果是私有网络或需要更高安全性，可以使用 request.setBody(file.getInputStream());
//
//            RuntimeOptions runtime = new RuntimeOptions();
//            RecognizeIdcardResponse response = client.recognizeIdcardWithOptions(request, runtime);
//
//            // 构造返回结果
            AjaxResult ajax = AjaxResult.success("OCR识别成功");
//            ajax.put("fileUrl", url);
//            ajax.put("ocrData", response.getBody().getData());
            return ajax;
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return AjaxResult.error("OCR识别失败: " + e.getMessage());
        }
    }
}
