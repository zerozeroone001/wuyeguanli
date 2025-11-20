package com.ruoyi.user.controller;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysDept;
import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.SmsUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.SysOwnerProfile;
import com.ruoyi.system.service.*;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * 用户信息
 * 
 * @author ruoyi
 */
@RestController
@RequestMapping("/user/info")
public class UserController extends BaseController
{
    @Autowired
    private ISysUserService userService;

    @Autowired
    private ISysRoleService roleService;

    @Autowired
    private ISysDeptService deptService;

    @Autowired
    private ISysPostService postService;

    @Autowired
    private SmsService smsService;

    @Autowired
    private ISysOwnerProfileService ownerProfileService;


    @Autowired
    private RedisTemplate<String, String> redisTemplate;


    @PostMapping("/importTemplate")
    public void importTemplate(HttpServletResponse response)
    {
        ExcelUtil<SysUser> util = new ExcelUtil<SysUser>(SysUser.class);
        util.importTemplateExcel(response, "用户数据");
    }

    /**
     * 根据用户编号获取详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:user:query')")
    @GetMapping(value = { "/", "/{userId}" })
    public AjaxResult getInfo(@PathVariable(value = "userId", required = false) Long userId)
    {
        AjaxResult ajax = AjaxResult.success();
        if (StringUtils.isNotNull(userId))
        {
            userService.checkUserDataScope(userId);
            SysUser sysUser = userService.selectUserById(userId);
            ajax.put(AjaxResult.DATA_TAG, sysUser);
            ajax.put("postIds", postService.selectPostListByUserId(userId));
            ajax.put("roleIds", sysUser.getRoles().stream().map(SysRole::getRoleId).collect(Collectors.toList()));
        }
        List<SysRole> roles = roleService.selectRoleAll();
        ajax.put("roles", SysUser.isAdmin(userId) ? roles : roles.stream().filter(r -> !r.isAdmin()).collect(Collectors.toList()));
        ajax.put("posts", postService.selectPostAll());
        return ajax;
    }



    /**
     * 修改用户
     */
    @PreAuthorize("@ss.hasPermi('system:user:edit')")
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@Validated @RequestBody SysUser user)
    {
        userService.checkUserAllowed(user);
        userService.checkUserDataScope(user.getUserId());
        deptService.checkDeptDataScope(user.getDeptId());
        roleService.checkRoleDataScope(user.getRoleIds());
        if (!userService.checkUserNameUnique(user))
        {
            return error("修改用户'" + user.getUserName() + "'失败，登录账号已存在");
        }
        else if (StringUtils.isNotEmpty(user.getPhonenumber()) && !userService.checkPhoneUnique(user))
        {
            return error("修改用户'" + user.getUserName() + "'失败，手机号码已存在");
        }
        else if (StringUtils.isNotEmpty(user.getEmail()) && !userService.checkEmailUnique(user))
        {
            return error("修改用户'" + user.getUserName() + "'失败，邮箱账号已存在");
        }
        user.setUpdateBy(getUsername());
        return toAjax(userService.updateUser(user));
    }


    /**
     * 重置密码
     */
    @PreAuthorize("@ss.hasPermi('system:user:resetPwd')")
    @Log(title = "用户管理", businessType = BusinessType.UPDATE)
    @PutMapping("/resetPwd")
    public AjaxResult resetPwd(@RequestBody SysUser user)
    {
        userService.checkUserAllowed(user);
        userService.checkUserDataScope(user.getUserId());
        user.setPassword(SecurityUtils.encryptPassword(user.getPassword()));
        user.setUpdateBy(getUsername());
        return toAjax(userService.resetPwd(user));
    }



    /**
     * 发送短信验证码
     *
     * @param user 手机号码
     * @return 操作结果
     */
//    @PostMapping("/sendCode")
//    public AjaxResult sendSmsCode(@RequestBody SysUser user) {
//        // 1. 生成6位随机验证码
//        String code = String.valueOf((int) ((Math.random() * 9 + 1) * 100000));
//        System.out.println("11111111111111");
//        System.out.println(user.getPhonenumber());
//        // 2. 将验证码存入Redis，有效期5分钟
//        redisCache.setCacheObject(SMS_CODE_PREFIX + user.getPhonenumber(), code, 5, TimeUnit.MINUTES);
//        wechatService.getPageLink(11L);
//        // 3. 发送短信
//        boolean isSuccess = smsService.sendSms(user.getPhonenumber(), code);
//
//        if (isSuccess) {
//            return AjaxResult.success("短信发送成功");
//        } else {
//            return AjaxResult.error("短信发送失败");
//        }
//    }

    /**
     * 发送验证码
     */
    @PostMapping("/sendCode")
    public AjaxResult sendCode(@RequestBody Map<String, String> params)
    {
        String phone = params.get("phoneNumber");

        // 验证手机号格式
        if (!SmsUtils.isValidPhone(phone))
        {
            return error("手机号格式不正确");
        }

        // 检查发送频率(60秒内不能重复发送)
        String sendKey = "sms:send:" + phone;
        String sendTime = redisTemplate.opsForValue().get(sendKey);
        if (StringUtils.isNotEmpty(sendTime))
        {
            return error("验证码发送过于频繁,请稍后再试");
        }

        // 生成6位验证码
        String code = SmsUtils.generateCode();

        // 保存验证码到Redis,有效期10分钟
        String codeKey = "sms:code:" + phone;
        redisTemplate.opsForValue().set(codeKey, code, 10, TimeUnit.MINUTES);

        // 记录发送时间,60秒内不能重复发送
        redisTemplate.opsForValue().set(sendKey, String.valueOf(System.currentTimeMillis()), 60, TimeUnit.SECONDS);

        // 发送短信(这里是模拟发送,实际项目需要对接短信平台)
        boolean isSuccess = smsService.sendSms(phone, code);

        if (isSuccess)
        {
            return success("验证码发送成功");
        }
        else
        {
            return error("验证码发送失败,请稍后重试");
        }
    }


    /**
     * 绑定手机号
     */
    @Log(title = "绑定手机号", businessType = BusinessType.UPDATE)
    @PostMapping("/bindPhone")
    public AjaxResult bindPhone(@RequestBody Map<String, String> params)
    {
        String phoneNumber = params.get("phoneNumber");
        String code = params.get("code");

        // 验证手机号格式
        if (!SmsUtils.isValidPhone(phoneNumber))
        {
            return error("手机号格式不正确");
        }

        // 验证验证码格式
        if (StringUtils.isEmpty(code) || code.length() != 6)
        {
            return error("验证码格式不正确");
        }

        // 从Redis获取验证码
        String codeKey = "sms:code:" + phoneNumber;
        String savedCode = redisTemplate.opsForValue().get(codeKey);

        if (StringUtils.isEmpty(savedCode))
        {
            return error("验证码已过期,请重新获取");
        }

        if (!savedCode.equals(code))
        {
            return error("验证码错误");
        }

        // 检查手机号是否已被其他用户绑定
        SysUser checkUser = new SysUser();
        checkUser.setPhonenumber(phoneNumber);
        List<SysUser> users = userService.selectUserList(checkUser);

        Long currentUserId = getUserId();
        for (SysUser user : users)
        {
            // 如果手机号已被其他用户绑定,则不允许绑定
            if (!user.getUserId().equals(currentUserId))
            {
                return error("该手机号已被其他用户绑定");
            }
        }

        // 更新用户手机号
        SysUser user = new SysUser();
        user.setUserId(currentUserId);
        user.setPhonenumber(phoneNumber);
        user.setUpdateBy(getUsername());

        SysOwnerProfile ownerProfile = ownerProfileService.selectSysOwnerProfileByUserId(user.getUserId());
        ownerProfile.setPhonenumber(phoneNumber);
        ownerProfileService.updateSysOwnerProfile(ownerProfile);

        int rows = userService.updateUser(user);
        Map<String, Object> result = new HashMap<>();
        if (rows > 0)
        {
            // 绑定成功,删除验证码
            redisTemplate.delete(codeKey);

            // 返回更新后的用户信息
            SysUser updatedUser = userService.selectUserById(currentUserId);

            result.put("userId", updatedUser.getUserId());
            result.put("phonenumber", updatedUser.getPhonenumber());

        }
        else
        {
            return error("绑定失败,请稍后重试");
        }
        return success(result);
    }
}
