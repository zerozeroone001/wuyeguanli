package com.ruoyi.system.service.impl;

import com.aliyun.dyvmsapi20170525.Client;
import com.aliyun.dyvmsapi20170525.models.SmartCallRequest;
import com.aliyun.dyvmsapi20170525.models.SmartCallResponse;
import com.aliyun.tea.TeaException;
import com.aliyun.teaopenapi.models.Config;
import com.aliyun.teautil.models.RuntimeOptions;
import com.ruoyi.system.service.ISysConfigService;
import com.ruoyi.system.service.PhoneService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
 * 智能外呼服务实现类
 * 基于阿里云 SmartCall API 实现会议通知的智能语音交互外呼功能
 * 
 * @author ruoyi
 */
@Service
public class PhoneServiceImpl implements PhoneService {
    private static final Logger log = LoggerFactory.getLogger(PhoneServiceImpl.class);

    @Autowired
    private ISysConfigService configService;

    /**
     * 发送单条会议通知智能外呼
     * 使用 SmartCall API 实现智能语音交互
     * 
     * @param phoneNumber 接收电话的手机号或固话号码
     * @param meetingId 会议ID（用于日志和外部ID）
     * @return 是否发送成功
     */
    @Override
    public boolean sendSingleMeetingPhone(String phoneNumber, Long meetingId) {
        try {
            // 1. 获取配置信息
            String accessKeyId = configService.selectConfigByKey("sys_ali_accessKeyId");
            String accessKeySecret = configService.selectConfigByKey("sys_ali_accessKeySecret");
            String endpoint = configService.selectConfigByKey("sys_ali_endpoint");
            String calledShowNumber = configService.selectConfigByKey("sys_ali_smart_calledShowNumber");
            String voiceCode = configService.selectConfigByKey("sys_ali_smart_voiceCode");
            String asrModelId = configService.selectConfigByKey("sys_ali_smart_asrModelId");
            String actionCodeBreak = configService.selectConfigByKey("sys_ali_smart_actionCodeBreak");
            String muteTime = configService.selectConfigByKey("sys_ali_smart_muteTime");
            String pauseTime = configService.selectConfigByKey("sys_ali_smart_pauseTime");

            // 2. 验证必要配置
            if (!StringUtils.hasText(accessKeyId) || !StringUtils.hasText(accessKeySecret)) {
                log.error("阿里云 AccessKey 配置不完整，请检查系统配置");
                return false;
            }
            
            if (!StringUtils.hasText(calledShowNumber)) {
                log.error("被叫显号未配置，请在系统配置中设置 sys_ali_smart_calledShowNumber");
                return false;
            }
            
            if (!StringUtils.hasText(voiceCode)) {
                log.error("开场语未配置，请在系统配置中设置 sys_ali_smart_voiceCode");
                return false;
            }

            // 3. 创建客户端配置
            Config config = new Config()
                    .setAccessKeyId(accessKeyId)
                    .setAccessKeySecret(accessKeySecret);
            
            // 设置端点，默认为 dyvmsapi.aliyuncs.com
            config.endpoint = StringUtils.hasText(endpoint) ? endpoint : "dyvmsapi.aliyuncs.com";

            // 4. 创建语音服务客户端
            Client client = new Client(config);

            // 5. 构建智能外呼请求
            SmartCallRequest request = new SmartCallRequest();
            
            // 设置被叫号码（必填）
            request.setCalledNumber(phoneNumber);
            
            // 设置被叫显号（必填）
            request.setCalledShowNumber(calledShowNumber);
            
            // 设置开场放音内容（必填）
            // 支持三种格式：
            // 1. TTS 文本：直接输入文本，系统会自动转为语音
            // 2. 语音文件 URL：http://xxx.wav
            // 3. 控制台上传的语音文件 ID
            request.setVoiceCode(voiceCode);
            
            // 设置外部ID，用于业务关联（可选）
            request.setOutId("meeting_" + meetingId);
            
            // 设置语音识别模型ID（可选）
            if (StringUtils.hasText(asrModelId)) {
                request.setAsrModelId(asrModelId);
            }
            
            // 设置开场放音是否可打断（可选，默认 true）
            if (StringUtils.hasText(actionCodeBreak)) {
                request.setActionCodeBreak(Boolean.parseBoolean(actionCodeBreak));
            }
            
            // 设置静音时长（可选，单位：毫秒，范围：1000-20000）
            if (StringUtils.hasText(muteTime)) {
                try {
                    request.setMuteTime(Integer.parseInt(muteTime));
                } catch (NumberFormatException e) {
                    log.warn("静音时长配置格式错误，使用默认值: {}", muteTime);
                }
            }
            
            // 设置停顿时长（可选，单位：毫秒，范围：300-1200）
            if (StringUtils.hasText(pauseTime)) {
                try {
                    request.setPauseTime(Integer.parseInt(pauseTime));
                } catch (NumberFormatException e) {
                    log.warn("停顿时长配置格式错误，使用默认值: {}", pauseTime);
                }
            }

            // 6. 发送请求
            RuntimeOptions runtime = new RuntimeOptions();
            SmartCallResponse response = client.smartCallWithOptions(request, runtime);

            // 7. 处理响应
            if (response != null && response.getBody() != null) {
                String code = response.getBody().getCode();
                String message = response.getBody().getMessage();
                String callId = response.getBody().getCallId();

                if ("OK".equals(code)) {
                    log.info("智能外呼创建成功 - 手机号: {}, 会议ID: {}, CallId: {}", 
                            phoneNumber, meetingId, callId);
                    return true;
                } else {
                    log.error("智能外呼创建失败 - 手机号: {}, 错误码: {}, 错误信息: {}", 
                            phoneNumber, code, message);
                    return false;
                }
            } else {
                log.error("智能外呼响应为空 - 手机号: {}", phoneNumber);
                return false;
            }

        } catch (TeaException error) {
            // 处理阿里云SDK异常
            log.error("智能外呼创建异常 - 手机号: {}, 会议ID: {}, 错误信息: {}", 
                    phoneNumber, meetingId, error.getMessage());
            if (error.getData() != null && error.getData().get("Recommend") != null) {
                log.error("诊断建议: {}", error.getData().get("Recommend"));
            }
            return false;
        } catch (Exception e) {
            // 处理其他异常
            log.error("智能外呼创建异常 - 手机号: {}, 会议ID: {}, 异常: ", phoneNumber, meetingId, e);
            return false;
        }
    }
}
