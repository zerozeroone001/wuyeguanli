# 认证状态判断使用指南

## 概述

本系统通过用户表的 `is_owner` 字段来判断用户的认证状态，实现统一的认证状态管理。

## 字段定义

### is_owner 字段值
- **0**: 未认证业主
- **1**: 已认证业主
- **2**: 业委会成员

## 后端实现

### 1. 实体类定义
```java
// SysUser.java
/** 是否业委会成员（0否 1是） */
@Excel(name = "是否业委会成员", readConverterExp = "0=否,1=业主，2=业委会")
private int isOwner;

public int getIsOwner() {
    return isOwner;
}

public void setIsOwner(int isOwner) {
    this.isOwner = isOwner;
}
```

### 2. 认证状态设置逻辑

#### 业主认证通过时
```java
// SysOwnerProfileServiceImpl.java
if (sysOwnerProfile.getAuthStatus() == 2) { // 审核通过
    SysUser user = userService.selectUserById(sysOwnerProfile.getUserId());
    if (user != null) {
        user.setIsOwner(1); // 设置为已认证业主
        userService.updateUser(user);
    }
}
```

#### 管理员设置业委会成员
```java
// SysUserController.java
// 设置为业委会成员
@PutMapping("/setCommitteeMember/{userId}")
public AjaxResult setCommitteeMember(@PathVariable("userId") Long userId) {
    SysUser user = new SysUser();
    user.setUserId(userId);
    user.setIsOwner(2); // 设置为业委会成员
    user.setUpdateBy(getUsername());
    return toAjax(userService.updateUser(user));
}

// 取消业委会成员身份
@PutMapping("/removeCommitteeMember/{userId}")
public AjaxResult removeCommitteeMember(@PathVariable("userId") Long userId) {
    SysUser user = new SysUser();
    user.setUserId(userId);
    user.setIsOwner(0); // 取消认证
    user.setUpdateBy(getUsername());
    return toAjax(userService.updateUser(user));
}
```

## 前端实现

### 1. Vuex 状态管理
```javascript
// store/modules/user.js
state: {
    isOwner: storage.get(constant.isOwner) || 0 // 0-未认证，1-业主，2-业委会
},

mutations: {
    SET_IS_OWNER: (state, isOwner) => {
        state.isOwner = isOwner
        storage.set(constant.isOwner, isOwner)
    }
},

actions: {
    GetInfo({ commit, state }) {
        return new Promise((resolve, reject) => {
            getInfo().then(res => {
                // 更新认证状态
                commit('SET_AUTH_STATUS', res.is_owner > 0)
                commit('SET_IS_OWNER', res.is_owner)
                resolve(res)
            })
        })
    }
}
```

### 2. Getters 定义
```javascript
// store/getters.js
const getters = {
    isOwner: state => state.user.isOwner,
    authStatus: state => state.user.authStatus
}
```

### 3. 工具函数使用
```javascript
// utils/authHelper.js
import { 
    isAuthenticated, 
    getAuthStatusText, 
    getAuthStatusColor,
    hasAuthPermission 
} from '@/utils/authHelper'

// 在组件中使用
export default {
    computed: {
        ...mapGetters(['isOwner']),
        
        // 判断是否已认证
        authStatus() {
            return isAuthenticated(this.isOwner)
        },
        
        // 获取认证状态文本
        ownerStatusText() {
            return getAuthStatusText(this.isOwner)
        }
    }
}
```

### 4. 页面中的使用示例
```vue
<template>
  <view class="user-info">
    <!-- 认证状态显示 -->
    <view class="auth-status" :class="{ verified: authStatus }">
      <uni-icons 
        :type="getAuthStatusIcon(isOwner)" 
        :color="getAuthStatusColor(isOwner)"
      />
      <text>{{ ownerStatusText }}</text>
    </view>
    
    <!-- 条件渲染 -->
    <view v-if="hasAuthPermission(isOwner, 'owner')">
      业主专享功能
    </view>
    
    <view v-if="hasAuthPermission(isOwner, 'committee')">
      业委会专享功能
    </view>
  </view>
</template>

<script>
import { mapGetters } from 'vuex'
import { 
    isAuthenticated, 
    getAuthStatusText, 
    getAuthStatusColor,
    getAuthStatusIcon,
    hasAuthPermission 
} from '@/utils/authHelper'

export default {
    computed: {
        ...mapGetters(['isOwner']),
        authStatus() {
            return isAuthenticated(this.isOwner)
        },
        ownerStatusText() {
            return getAuthStatusText(this.isOwner)
        }
    },
    methods: {
        getAuthStatusIcon,
        getAuthStatusColor,
        hasAuthPermission
    }
}
</script>
```

## 工具函数 API

### isAuthenticated(isOwner)
判断用户是否已认证
- **参数**: `isOwner` - is_owner字段值
- **返回**: `boolean` - 是否已认证

### getAuthStatusText(isOwner)
获取认证状态文本
- **参数**: `isOwner` - is_owner字段值
- **返回**: `string` - 认证状态文本

### getAuthStatusColor(isOwner)
获取认证状态颜色
- **参数**: `isOwner` - is_owner字段值
- **返回**: `string` - 颜色值

### hasAuthPermission(isOwner, requiredAuth)
检查用户权限
- **参数**: 
  - `isOwner` - is_owner字段值
  - `requiredAuth` - 需要的认证级别 ('any' | 'owner' | 'committee')
- **返回**: `boolean` - 是否有权限

## 最佳实践

1. **统一使用工具函数**: 避免在组件中直接判断 `isOwner` 值
2. **权限控制**: 在需要权限的功能前使用 `hasAuthPermission` 进行判断
3. **状态同步**: 确保后端更新 `is_owner` 字段后，前端能及时同步状态
4. **用户体验**: 根据认证状态显示不同的UI和功能

## 注意事项

1. `is_owner` 字段的更新需要相应的权限验证
2. 认证状态变更应该记录操作日志
3. 前端状态更新需要处理网络异常情况
4. 缓存策略需要考虑认证状态的实时性