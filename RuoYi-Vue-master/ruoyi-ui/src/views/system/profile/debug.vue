<template>
  <div class="app-container">
    <el-card class="box-card">
      <div slot="header" class="clearfix">
        <span>业主认证状态调试工具</span>
      </div>

      <el-form :model="queryParams" ref="queryForm" size="small" :inline="true" label-width="100px">
        <el-form-item label="用户ID" prop="userId">
          <el-input
            v-model="queryParams.userId"
            placeholder="请输入用户ID"
            clearable
          />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleQuery">查询</el-button>
        </el-form-item>
      </el-form>

      <el-divider></el-divider>

      <div v-if="userInfo" class="debug-info">
        <h4>用户基本信息</h4>
        <el-descriptions :column="2" border>
          <el-descriptions-item label="用户ID">{{ userInfo.userId }}</el-descriptions-item>
          <el-descriptions-item label="用户名">{{ userInfo.userName }}</el-descriptions-item>
          <el-descriptions-item label="真实姓名">{{ userInfo.nickName }}</el-descriptions-item>
          <el-descriptions-item label="手机号">{{ userInfo.phonenumber }}</el-descriptions-item>
          <el-descriptions-item label="is_owner字段">{{ userInfo.is_owner }}</el-descriptions-item>
        </el-descriptions>

        <h4 style="margin-top: 20px;">业主认证信息</h4>
        <el-descriptions :column="2" border>
          <el-descriptions-item label="认证状态">{{ profileInfo.authStatus }}</el-descriptions-item>
          <el-descriptions-item label="认证状态文本">
            <dict-tag :options="dict.type.sys_auth_status" :value="profileInfo.authStatus"/>
          </el-descriptions-item>
          <el-descriptions-item label="真实姓名">{{ profileInfo.realName }}</el-descriptions-item>
          <el-descriptions-item label="身份证号">{{ profileInfo.idCardNo }}</el-descriptions-item>
          <el-descriptions-item label="楼栋房号">{{ profileInfo.buildingNo }}{{ profileInfo.unitNo }}{{ profileInfo.roomNo }}</el-descriptions-item>
          <el-descriptions-item label="审核备注">{{ profileInfo.authRemark }}</el-descriptions-item>
          <el-descriptions-item label="申请时间">{{ parseTime(profileInfo.createTime) }}</el-descriptions-item>
          <el-descriptions-item label="审核时间">{{ parseTime(profileInfo.updateTime) }}</el-descriptions-item>
          <el-descriptions-item label="审核人">{{ profileInfo.updateBy }}</el-descriptions-item>
        </el-descriptions>

        <h4 style="margin-top: 20px;">状态计算逻辑</h4>
        <el-descriptions :column="1" border>
          <el-descriptions-item label="calculateOwnerStatus返回值">
            <el-tag :type="getOwnerStatusType(userInfo.is_owner)">
              {{ getOwnerStatusText(userInfo.is_owner) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="计算逻辑">
            <p v-if="!profileInfo || profileInfo.authStatus !== '2'">
              认证状态不是"2"（已认证），返回未认证状态
            </p>
            <p v-else-if="profileInfo.isCommitteeMember === 'Y'">
              已认证且是业委会成员，返回业委会状态(2)
            </p>
            <p v-else>
              已认证的普通业主，返回业主状态(1)
            </p>
          </el-descriptions-item>
        </el-descriptions>
      </div>

      <div v-else-if="queryParams.userId" class="no-data">
        <el-empty description="未找到用户信息"></el-empty>
      </div>
    </el-card>
  </div>
</template>

<script>
import { listOwnerProfile } from "@/api/system/ownerProfile";
import { getUser } from "@/api/system/user";

export default {
  name: "OwnerProfileDebug",
  dicts: ['sys_auth_status'],
  data() {
    return {
      queryParams: {
        userId: null
      },
      userInfo: null,
      profileInfo: null
    };
  },
  methods: {
    handleQuery() {
      if (!this.queryParams.userId) {
        this.$modal.msgError("请输入用户ID");
        return;
      }
      this.getUserInfo();
      this.getProfileInfo();
    },
    getUserInfo() {
      getUser(this.queryParams.userId).then(response => {
        this.userInfo = response.data;
      }).catch(() => {
        this.userInfo = null;
      });
    },
    getProfileInfo() {
      const queryParams = {
        userId: this.queryParams.userId
      };
      listOwnerProfile(queryParams).then(response => {
        if (response.rows && response.rows.length > 0) {
          this.profileInfo = response.rows[0];
        } else {
          this.profileInfo = null;
        }
      }).catch(() => {
        this.profileInfo = null;
      });
    },
    getOwnerStatusType(status) {
      switch(status) {
        case 0: return 'info';
        case 1: return 'success';
        case 2: return 'warning';
        default: return 'info';
      }
    },
    getOwnerStatusText(status) {
      switch(status) {
        case 0: return '未认证(0)';
        case 1: return '业主(1)';
        case 2: return '业委会(2)';
        default: return '未知';
      }
    }
  }
};
</script>

<style scoped>
.debug-info h4 {
  color: #303133;
  margin-bottom: 15px;
}

.no-data {
  text-align: center;
  padding: 40px 0;
}
</style>
