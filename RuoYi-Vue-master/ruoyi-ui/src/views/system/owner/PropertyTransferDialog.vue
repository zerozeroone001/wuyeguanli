<template>
  <el-dialog :title="title" :visible.sync="open" width="900px" append-to-body>
    <div class="transfer-container">
      <!-- Top: Transfer Shuttle -->
      <div class="transfer-shuttle">
        <el-transfer
          v-model="targetPropertyIds"
          :data="allProperties"
          :titles="['当前用户房产', '目标用户房产']"
          :button-texts="['', '']"
          :props="{
            key: 'propertyId',
            label: 'label'
          }"
          @change="handleChange"
        >
          <span slot-scope="{ option }">{{ option.label }}</span>
        </el-transfer>
      </div>

      <!-- Bottom: User Selection -->
      <el-row :gutter="20" class="user-selection">
        <el-col :span="12">
          <el-card shadow="never" class="user-card">
            <div slot="header">
              <span>当前用户 (左侧)</span>
            </div>
            <div v-if="sourceUser">
              <p><strong>姓名:</strong> {{ sourceUser.userName }}</p>
              <p><strong>电话:</strong> {{ sourceUser.phonenumber }}</p>
            </div>
            <div v-else class="empty-text">请在列表中选择一个用户</div>
          </el-card>
        </el-col>
        <el-col :span="12">
          <el-card shadow="never" class="user-card">
            <div slot="header">
              <span>目标用户 (右侧)</span>
              <el-button style="float: right; padding: 3px 0" type="text" @click="openUserSelect">选择用户</el-button>
            </div>
            <div v-if="targetUser">
              <p><strong>姓名:</strong> {{ targetUser.userName || targetUser.nickName }}</p>
              <p><strong>电话:</strong> {{ targetUser.phonenumber }}</p>
            </div>
            <div v-else class="empty-text">请点击右上角选择目标用户</div>
          </el-card>
        </el-col>
      </el-row>
    </div>

    <div slot="footer" class="dialog-footer">
      <el-button type="primary" @click="submitForm" :disabled="!canSubmit">确 定</el-button>
      <el-button @click="cancel">取 消</el-button>
    </div>

    <!-- User Select Dialog -->
    <user-select-dialog :visible.sync="userSelectVisible" @confirm="handleUserSelected" />
  </el-dialog>
</template>

<script>
import { listEstateUserProperty } from "@/api/system/estateUserProperty";
import { transferProperties } from "@/api/system/owner";
import UserSelectDialog from "@/components/UserSelectDialog";

export default {
  name: "PropertyTransferDialog",
  components: { UserSelectDialog },
  data() {
    return {
      title: "房产合并与拆分",
      open: false,
      sourceUser: null,
      targetUser: null,
      userSelectVisible: false,
      // All properties involved (from both source and target)
      allProperties: [],
      // IDs of properties that should end up with the target user
      targetPropertyIds: []
    };
  },
  computed: {
    canSubmit() {
      return this.sourceUser && this.targetUser && this.sourceUser.userId !== this.targetUser.userId;
    }
  },
  methods: {
    init(row) {
      this.reset();
      this.sourceUser = { ...row };
      this.open = true;
      this.loadProperties();
    },
    reset() {
      this.sourceUser = null;
      this.targetUser = null;
      this.allProperties = [];
      this.targetPropertyIds = [];
    },
    openUserSelect() {
      this.userSelectVisible = true;
    },
    handleUserSelected(user) {
      if (this.sourceUser && user.userId === this.sourceUser.userId) {
        this.$message.warning("目标用户不能与当前用户相同");
        return;
      }
      this.targetUser = user;
      this.loadProperties();
    },
    loadProperties() {
      if (!this.sourceUser) return;

      this.allProperties = [];
      this.targetPropertyIds = [];

      const promises = [];
      const queryParams = {
        pageNum: 1,
        pageSize: 10000
      };

      // Load source user properties
      console.log("Loading properties for source user:", this.sourceUser.userId);
      promises.push(listEstateUserProperty({ ...queryParams, userId: this.sourceUser.userId }).then(res => {
        console.log("Source user properties:", res.rows);
        return res.rows.map(item => ({
          propertyId: item.propertyId,
          label: `${item.communityName || ''} ${item.buildingName || ''}-${item.unitName || ''}-${item.roomNumber || ''}`,
          originalOwner: 'source'
        }));
      }));

      // Load target user properties if selected
      if (this.targetUser) {
        console.log("Loading properties for target user:", this.targetUser.userId);
        promises.push(listEstateUserProperty({ ...queryParams, userId: this.targetUser.userId }).then(res => {
          console.log("Target user properties:", res.rows);
          return res.rows.map(item => ({
            propertyId: item.propertyId,
            label: `${item.communityName || ''} ${item.buildingName || ''}-${item.unitName || ''}-${item.roomNumber || ''}`,
            originalOwner: 'target'
          }));
        }));
      }

      Promise.all(promises).then(results => {
        let all = [];
        if (results[0]) all = all.concat(results[0]);
        if (results[1]) {
          all = all.concat(results[1]);
          // Pre-select target user's properties in the transfer list
          this.targetPropertyIds = results[1].map(p => p.propertyId);
        }

        // Remove duplicates based on propertyId just in case
        const seen = new Set();
        this.allProperties = all.filter(item => {
          const duplicate = seen.has(item.propertyId);
          seen.add(item.propertyId);
          return !duplicate;
        });
      });
    },
    handleChange(value, direction, movedKeys) {
      // value contains the array of keys now on the right side
    },
    submitForm() {
      if (!this.canSubmit) return;

      const data = {
        sourceUserId: this.sourceUser.userId,
        targetUserId: this.targetUser.userId,
        targetPropertyIds: this.targetPropertyIds
      };

      console.log("提交房产转移请求:", data);

      transferProperties(data).then(response => {
        this.$modal.msgSuccess("房产转移成功");
        this.open = false;
        this.$emit("refresh");
      }).catch(error => {
        console.error("房产转移失败:", error);
        const errorMsg = error.msg || error.message || "未知错误";
        this.$modal.msgError("房产转移失败: " + errorMsg);
      });
    },
    cancel() {
      this.open = false;
    }
  }
};
</script>

<style scoped lang="scss">
.transfer-container {
  padding: 10px;
}
.transfer-shuttle {
  display: flex;
  justify-content: center;
  margin-bottom: 20px;
}
.user-selection {
  margin-top: 20px;
}
.user-card {
  height: 150px;
  .empty-text {
    color: #909399;
    text-align: center;
    margin-top: 30px;
  }
}
</style>
