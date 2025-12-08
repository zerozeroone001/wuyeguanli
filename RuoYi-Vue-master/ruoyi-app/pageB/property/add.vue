<template>
  <view class="property-auth-container">
    <!-- 页面标题 -->
    <view class="page-header">
      <text class="page-title">产权认证</text>
      <text class="page-subtitle">请填写以下信息提交产权认证申请</text>
    </view>

    <!-- 表单区域 -->
    <view class="form-section">
      <!-- 基本信息 -->
      <view class="form-group">
        <view class="group-title">
          <view class="title-icon"></view>
          <text>基本信息</text>
        </view>

        <view class="form-item">
          <text class="label required">姓名</text>
          <input
            class="input"
            v-model="formData.ownerName"
            placeholder="请输入姓名"
            placeholder-class="input-placeholder"
          />
        </view>

        <view class="form-item">
          <text class="label required">性别</text>
          <view class="radio-group">
            <view
              class="radio-item"
              v-for="item in genderList"
              :key="item.value"
              @click="formData.gender = item.value"
            >
              <view :class="['radio-icon', formData.gender === item.value ? 'active' : '']">
                <view v-if="formData.gender === item.value" class="radio-dot"></view>
              </view>
              <text class="radio-text">{{ item.text }}</text>
            </view>
          </view>
        </view>

        <view class="form-item">
          <text class="label required">联系电话</text>
          <input
            class="input"
            v-model="formData.contactPhone"
            placeholder="请输入联系电话"
            placeholder-class="input-placeholder"
            type="number"
            maxlength="11"
          />
        </view>
      </view>

      <!-- 房产信息 -->
      <view class="form-group">
        <view class="group-title">
          <view class="title-icon"></view>
          <text>房产信息</text>
        </view>

        <view class="form-item">
          <text class="label required">选择小区</text>
          <picker @change="handleCommunityChange" :value="communityIndex" :range="communityList" range-key="communityName">
            <view class="input-wrapper">
              <text :class="['input-text', formData.communityId ? '' : 'placeholder']">
                {{ selectedCommunityName || '请选择小区' }}
              </text>
              <uni-icons type="right" size="16" color="#BFBFBF" />
            </view>
          </picker>
        </view>

        <view class="form-item">
          <text class="label required">选择楼栋</text>
          <picker @change="handleBuildingChange" :value="buildingIndex" :range="buildingList" range-key="buildingName" :disabled="!formData.communityId">
            <view class="input-wrapper">
              <text :class="['input-text', formData.buildingName ? '' : 'placeholder']">
                {{ selectedBuildingName || '请先选择小区' }}
              </text>
              <uni-icons type="right" size="16" color="#BFBFBF" />
            </view>
          </picker>
        </view>

        <view class="form-item">
          <text class="label required">选择房号</text>
          <picker @change="handleRoomChange" :value="roomIndex" :range="roomList" range-key="roomNumber" :disabled="!formData.buildingName">
            <view class="input-wrapper">
              <text :class="['input-text', formData.propertyId ? '' : 'placeholder']">
                {{ selectedRoomNumber || '请先选择楼栋' }}
              </text>
              <uni-icons type="right" size="16" color="#BFBFBF" />
            </view>
          </picker>
        </view>
      </view>

      <!-- 房产证件 -->
      <view class="form-group">
        <!-- <view class="group-title">
          <view class="title-icon"></view>
          <text>房产证件</text>
        </view> -->

       <!-- <view class="form-item upload-item">
          <text class="label required">上传房产证</text>
          <view class="upload-area">
            <view v-if="!formData.propertyCert" class="upload-button" @click="choosePropertyCert">
              <uni-icons type="camera" size="40" color="#BFBFBF" />
              <text class="upload-text">图片大小不大于3MB</text>
            </view>
            <view v-else class="image-preview" @click="previewImage(formData.propertyCert)">
              <image :src="formData.propertyCert" mode="aspectFill" class="preview-image" />
              <view class="image-delete" @click.stop="deleteImage">
                <uni-icons type="close" size="16" color="#fff" />
              </view>
            </view>
          </view>
          <view class="help-text" @click="showExample">
            <text class="help-link">查看样例</text>
          </view>
        </view> -->

        <view class="form-item">
          <text class="label required">用户类型</text>
          <view class="radio-group">
            <view
              class="radio-item"
              v-for="item in userTypes"
              :key="item.value"
              @click="formData.userType = item.value"
            >
              <view :class="['radio-icon', formData.userType === item.value ? 'active' : '']">
                <view v-if="formData.userType === item.value" class="radio-dot"></view>
              </view>
              <text class="radio-text">{{ item.text }}</text>
            </view>
          </view>
        </view>
      </view>
    </view>

    <!-- 底部按钮 -->
    <view class="bottom-actions">
      <button class="submit-button primary" @click="submit">提交审核</button>
    </view>
  </view>
</template>

<script>
import { listAllProperty, addMyProperty, listCommunity } from "@/api/property.js";
import upload from '@/utils/upload';

export default {
  data() {
    return {
      formData: {
        ownerName: '',
        gender: '1',
        contactPhone: '',
        communityId: null,
        buildingName: null,
        propertyId: null,
        buildingArea: '',
        propertyCert: '',
        userType: '00'
      },
      communityList: [],
      communityIndex: -1,
      buildingList: [],
      buildingIndex: -1,
      roomList: [],
      roomIndex: -1,
      genderList: [
        { text: '男', value: '1' },
        { text: '女', value: '2' }
      ],
      userTypes: [
        { text: '业主', value: '00' },
        { text: '家属', value: '01' },
        { text: '租户', value: '02' }
      ],
      uploadingImage: false,
      submitting: false,
      hasDataChanged: false
    };
  },
  computed: {
    selectedCommunityName() {
      return this.communityList[this.communityIndex] ? this.communityList[this.communityIndex].communityName : ''
    },
    selectedBuildingName() {
      return this.buildingList[this.buildingIndex] ? this.buildingList[this.buildingIndex].buildingName : ''
    },
    selectedRoomNumber() {
      return this.roomList[this.roomIndex] ? this.roomList[this.roomIndex].roomNumber : ''
    }
  },
  onLoad() {
    this.getCommunities();
  },
  // 监听页面数据变化
  watch: {
    formData: {
      handler() {
        this.hasDataChanged = true;
      },
      deep: true
    }
  },
  // 页面返回前拦截
  onBackPress() {
    if (this.hasDataChanged && !this.submitting) {
      uni.showModal({
        title: '提示',
        content: '您填写的内容尚未提交,确定要离开吗?',
        success: (res) => {
          if (res.confirm) {
            uni.navigateBack();
          }
        }
      });
      return true; // 阻止默认返回
    }
    return false; // 允许返回
  },
  methods: {
    // 加载小区列表
    async getCommunities() {
      try {
        const response = await listCommunity({ pageNum: 1, pageSize: 1000 });
        this.communityList = response.rows || [];
      } catch (error) {
        console.error('加载小区列表失败', error);
        uni.showToast({
          title: '加载小区列表失败',
          icon: 'none'
        });
      }
    },

    // 小区选择改变
    handleCommunityChange(e) {
      this.communityIndex = e.detail.value;
      const community = this.communityList[this.communityIndex];
      if (community) {
        this.formData.communityId = community.communityId;
        // 重置后续选择
        this.resetBuilding();
        this.resetRoom();
        this.getBuildings();
      }
    },

    // 加载楼栋列表
    async getBuildings() {
      try {
        const response = await listAllProperty({ communityId: this.formData.communityId, pageNum: 1, pageSize: 10000 });
        const buildings = {};
        (response.rows || []).forEach(p => {
          if (!buildings[p.buildingName]) {
            buildings[p.buildingName] = { buildingName: p.buildingName };
          }
        });
        this.buildingList = Object.values(buildings);
      } catch (error) {
        console.error('加载楼栋列表失败', error);
      }
    },

    // 楼栋选择改变
    handleBuildingChange(e) {
      this.buildingIndex = e.detail.value;
      const building = this.buildingList[this.buildingIndex];
      if (building) {
        this.formData.buildingName = building.buildingName;
        // 重置房间选择
        this.resetRoom();
        this.getRooms();
      }
    },

    // 加载房间列表
    async getRooms() {
      try {
        const response = await listAllProperty({
          communityId: this.formData.communityId,
          buildingName: this.formData.buildingName,
          pageNum: 1,
          pageSize: 10000
        });
        this.roomList = response.rows || [];
      } catch (error) {
        console.error('加载房间列表失败', error);
      }
    },

    // 房号选择改变
    handleRoomChange(e) {
      this.roomIndex = e.detail.value;
      const room = this.roomList[this.roomIndex];
      if (room) {
        this.formData.propertyId = room.propertyId;
      }
    },

    // 重置楼栋
    resetBuilding() {
      this.buildingList = [];
      this.buildingIndex = -1;
      this.formData.buildingName = null;
    },

    // 重置房间
    resetRoom() {
      this.roomList = [];
      this.roomIndex = -1;
      this.formData.propertyId = null;
    },

    // 选择房产证图片
    choosePropertyCert() {
      if (this.uploadingImage) {
        uni.showToast({
          title: '图片正在上传中...',
          icon: 'none'
        });
        return;
      }

      uni.chooseImage({
        count: 1,
        sizeType: ['compressed'],
        sourceType: ['camera', 'album'],
        success: (res) => {
          const tempFilePath = res.tempFilePaths[0];
          // 检查文件大小
          uni.getFileInfo({
            filePath: tempFilePath,
            success: (info) => {
              if (info.size > 3 * 1024 * 1024) {
                uni.showToast({
                  title: '图片大小不能超过3MB',
                  icon: 'none'
                });
                return;
              }
              // 上传图片
              this.uploadImage(tempFilePath);
            },
            fail: (error) => {
              console.error('获取文件信息失败', error);
              uni.showToast({
                title: '获取文件信息失败',
                icon: 'none'
              });
            }
          });
        },
        fail: (error) => {
          console.error('选择图片失败', error);
        }
      });
    },

    // 上传图片
    async uploadImage(filePath) {
      this.uploadingImage = true;
      uni.showLoading({ title: '上传中...' });

      try {
        const result = await upload({
          url: '/common/upload',
          filePath: filePath,
          name: 'file'
        });

        if (result && result.fileName) {
          // 保存完整的URL路径
          this.formData.propertyCert = result.url;
          uni.hideLoading();
          uni.showToast({
            title: '上传成功',
            icon: 'success'
          });
        } else {
          throw new Error('上传返回数据格式错误');
        }
      } catch (error) {
        console.error('图片上传失败', error);
        uni.hideLoading();
        uni.showToast({
          title: error.message || '上传失败,请重试',
          icon: 'none'
        });
      } finally {
        this.uploadingImage = false;
      }
    },

    // 预览图片
    previewImage(url) {
      uni.previewImage({
        urls: [url],
        current: 0
      });
    },

    // 删除图片
    deleteImage() {
      uni.showModal({
        title: '提示',
        content: '确定要删除这张图片吗?',
        success: (res) => {
          if (res.confirm) {
            this.formData.propertyCert = '';
            uni.showToast({
              title: '已删除',
              icon: 'success',
              duration: 1500
            });
          }
        }
      });
    },

    // 查看样例
    showExample() {
      uni.showModal({
        title: '房产证样例',
        content: '请上传清晰的房产证照片,确保产权人姓名、房产地址、专有部分建筑面积等信息清晰可见。',
        showCancel: false
      });
    },

    // 表单验证
    validateForm() {
      // 验证姓名
      if (!this.formData.ownerName || !this.formData.ownerName.trim()) {
        uni.showToast({
          title: '请输入姓名',
          icon: 'none',
          duration: 2000
        });
        return false;
      }

      const trimmedName = this.formData.ownerName.trim();
      if (trimmedName.length < 2 || trimmedName.length > 10) {
        uni.showToast({
          title: '姓名长度在2-10个字符',
          icon: 'none',
          duration: 2000
        });
        return false;
      }

      // 验证性别
      if (!this.formData.gender) {
        uni.showToast({
          title: '请选择性别',
          icon: 'none',
          duration: 2000
        });
        return false;
      }

      // 验证联系电话
      if (!this.formData.contactPhone || !this.formData.contactPhone.trim()) {
        uni.showToast({
          title: '请输入联系电话',
          icon: 'none',
          duration: 2000
        });
        return false;
      }

      const phoneReg = /^1[3-9]\d{9}$/;
      if (!phoneReg.test(this.formData.contactPhone.trim())) {
        uni.showToast({
          title: '请输入正确的手机号码',
          icon: 'none',
          duration: 2000
        });
        return false;
      }

      // 验证小区
      if (!this.formData.communityId) {
        uni.showToast({
          title: '请选择小区',
          icon: 'none',
          duration: 2000
        });
        return false;
      }

      // 验证楼栋
      if (!this.formData.buildingName) {
        uni.showToast({
          title: '请选择楼栋',
          icon: 'none',
          duration: 2000
        });
        return false;
      }

      // 验证房号
      if (!this.formData.propertyId) {
        uni.showToast({
          title: '请选择房号',
          icon: 'none',
          duration: 2000
        });
        return false;
      }



      // 验证用户类型
      if (!this.formData.userType) {
        uni.showToast({
          title: '请选择用户类型',
          icon: 'none',
          duration: 2000
        });
        return false;
      }

      return true;
    },

    // 提交表单
    submit() {
      if (!this.validateForm()) {
        return;
      }

      uni.showModal({
        title: '确认提交',
        content: '确定提交产权认证申请吗?',
        success: (res) => {
          if (res.confirm) {
            this.doSubmit();
          }
        }
      });
    },

    // 执行提交
    async doSubmit() {
      // 防止重复提交
      if (this.uploadingImage) {
        uni.showToast({
          title: '图片正在上传中,请稍候',
          icon: 'none'
        });
        return;
      }

      if (this.submitting) {
        uni.showToast({
          title: '正在提交中,请勿重复操作',
          icon: 'none'
        });
        return;
      }

      this.submitting = true;

      uni.showLoading({
        title: '提交中...',
        mask: true
      });

      try {
        const submissionData = {
          ownerName: this.formData.ownerName.trim(),
          gender: this.formData.gender,
          contactPhone: this.formData.contactPhone.trim(),
          propertyId: this.formData.propertyId,
          buildingArea: parseFloat(this.formData.buildingArea),
          propertyCert: this.formData.propertyCert,
          userType: this.formData.userType
        };

        const response = await addMyProperty(submissionData);

        uni.hideLoading();

        // 显示成功提示
        await uni.showToast({
          title: '提交成功,请等待审核',
          icon: 'success',
          duration: 2000
        });

        // 标记为未修改，允许直接返回
        this.hasDataChanged = false;

        // 重置表单
        this.resetForm();

        // 延迟返回上一页
        setTimeout(() => {
          uni.navigateBack({
            delta: 1
          });
        }, 2000);

      } catch (error) {
        uni.hideLoading();
        this.submitting = false;

        console.error('提交产权认证失败:', error);

        uni.showModal({
          title: '提交失败',
          content: msg,
          showCancel: false,
          confirmText: '我知道了'
        });
      }
    },

    // 重置表单
    resetForm() {
      this.formData = {
        ownerName: '',
        gender: '1',
        contactPhone: '',
        communityId: null,
        buildingName: null,
        propertyId: null,
        buildingArea: '',
        propertyCert: '',
        userType: '00'
      };
      this.communityIndex = -1;
      this.buildingIndex = -1;
      this.roomIndex = -1;
      this.buildingList = [];
      this.roomList = [];
    }
  }
}
</script>

<style lang="scss" scoped>
.property-auth-container {
  min-height: 100vh;
  background: #F5F5F5;
  padding-bottom: 180rpx;
}

/* 页面头部 */
.page-header {
  padding: 60rpx 30rpx 40rpx;
  background: #FFFFFF;
  margin-bottom: 20rpx;

  .page-title {
    display: block;
    font-size: 48rpx;
    font-weight: 600;
    color: #262626;
    margin-bottom: 16rpx;
  }

  .page-subtitle {
    display: block;
    font-size: 28rpx;
    color: #8C8C8C;
  }
}

/* 表单区域 */
.form-section {
  .form-group {
    background: #FFFFFF;
    padding: 40rpx 30rpx;
    margin-bottom: 20rpx;

    .group-title {
      display: flex;
      align-items: center;
      margin-bottom: 32rpx;

      .title-icon {
        width: 6rpx;
        height: 32rpx;
        background: linear-gradient(to bottom, #1890FF, #40A9FF);
        border-radius: 3rpx;
        margin-right: 16rpx;
      }

      text {
        font-size: 32rpx;
        font-weight: 600;
        color: #262626;
      }
    }

    .form-item {
      margin-bottom: 32rpx;

      &:last-child {
        margin-bottom: 0;
      }

      .label {
        display: block;
        font-size: 28rpx;
        color: #595959;
        margin-bottom: 16rpx;

        &.required::before {
          content: '* ';
          color: #FF4D4F;
        }
      }

      .input {
        width: 100%;
        height: 88rpx;
        padding: 0 24rpx;
        background: #F5F5F5;
        border-radius: 12rpx;
        font-size: 28rpx;
        color: #262626;
        border: 2rpx solid transparent;
        transition: all 0.3s;
      }

      .input-placeholder {
        color: #BFBFBF;
      }

      .input-wrapper {
        display: flex;
        align-items: center;
        justify-content: space-between;
        height: 88rpx;
        padding: 0 24rpx;
        background: #F5F5F5;
        border-radius: 12rpx;

        .input-text {
          flex: 1;
          font-size: 28rpx;
          color: #262626;

          &.placeholder {
            color: #BFBFBF;
          }
        }
      }

      .input-with-unit {
        display: flex;
        align-items: center;
        gap: 16rpx;

        .flex-input {
          flex: 1;
        }

        .unit-text {
          font-size: 28rpx;
          color: #595959;
        }
      }

      .radio-group {
        display: flex;
        gap: 40rpx;

        .radio-item {
          display: flex;
          align-items: center;
          gap: 12rpx;

          .radio-icon {
            width: 36rpx;
            height: 36rpx;
            border: 2rpx solid #D9D9D9;
            border-radius: 50%;
            display: flex;
            align-items: center;
            justify-content: center;
            transition: all 0.3s;

            &.active {
              border-color: #1890FF;

              .radio-dot {
                width: 20rpx;
                height: 20rpx;
                background: #1890FF;
                border-radius: 50%;
              }
            }
          }

          .radio-text {
            font-size: 28rpx;
            color: #262626;
          }
        }
      }

      &.upload-item {
        .upload-area {
          width: 200rpx;
          height: 200rpx;
          border-radius: 12rpx;
          overflow: hidden;

          .upload-button {
            width: 100%;
            height: 100%;
            background: #F5F5F5;
            border: 2rpx dashed #D9D9D9;
            border-radius: 12rpx;
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;

            .upload-text {
              margin-top: 16rpx;
              font-size: 22rpx;
              color: #BFBFBF;
            }
          }

          .image-preview {
            position: relative;
            width: 100%;
            height: 100%;

            .preview-image {
              width: 100%;
              height: 100%;
            }

            .image-delete {
              position: absolute;
              top: 8rpx;
              right: 8rpx;
              width: 40rpx;
              height: 40rpx;
              background: rgba(0, 0, 0, 0.5);
              border-radius: 50%;
              display: flex;
              align-items: center;
              justify-content: center;
            }
          }
        }

        .help-text {
          margin-top: 16rpx;

          .help-link {
            font-size: 24rpx;
            color: #1890FF;
          }
        }
      }
    }
  }
}

/* 底部按钮 */
.bottom-actions {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  padding: 20rpx 30rpx;
  background: #FFFFFF;
  box-shadow: 0 -2rpx 16rpx rgba(0, 0, 0, 0.06);
  padding-bottom: calc(20rpx + env(safe-area-inset-bottom));

  .submit-button {
    width: 100%;
    height: 88rpx;
    border-radius: 12rpx;
    font-size: 32rpx;
    font-weight: 600;
    border: none;

    &.primary {
      background: linear-gradient(135deg, #1890FF, #40A9FF);
      color: #FFFFFF;
    }
  }
}
</style>
