<template>
  <view class="owner-change-container">
    <!-- 页面标题 -->
    <view class="page-header">
      <text class="page-title">业主变更</text>
      <text class="page-subtitle">请填写以下信息提交业主变更申请</text>
    </view>

    <!-- 表单区域 -->
    <view class="form-section">
      <!-- 业主信息 -->
      <view class="form-group">
        <view class="group-title">
          <view class="title-icon"></view>
          <text>业主信息</text>
        </view>

        <view class="form-item">
          <text class="label required">业主姓名</text>
          <input
            class="input"
            v-model="formData.ownerName"
            placeholder="请输入业主姓名"
            placeholder-class="input-placeholder"
          />
        </view>

        <view class="form-item">
          <text class="label required">手机号码</text>
          <input
            class="input"
            v-model="formData.ownerPhone"
            placeholder="请输入手机号码"
            placeholder-class="input-placeholder"
            type="number"
            maxlength="11"
          />
        </view>
      </view>

      <!-- 选择房产 -->
      <view class="form-group">
        <view class="group-title">
          <view class="title-icon"></view>
          <text>选择房产</text>
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

        <view class="form-item">
          <text class="label required">选择产权证类型</text>
          <picker @change="handlePropertyTypeChange" :value="propertyTypeIndex" :range="propertyTypeList" range-key="label">
            <view class="input-wrapper">
              <text :class="['input-text', formData.propertyType ? '' : 'placeholder']">
                {{ selectedPropertyType || '房产证' }}
              </text>
              <uni-icons type="right" size="16" color="#BFBFBF" />
            </view>
          </picker>
        </view>

        <view class="form-item upload-item">
          <text class="label required">上传房产证</text>
          <view class="upload-area">
            <view v-if="!formData.propertyCertImage" class="upload-button" @click="choosePropertyCertImage">
              <uni-icons type="camera" size="40" color="#BFBFBF" />
              <text class="upload-text">图片大小不大于3MB</text>
            </view>
            <view v-else class="image-preview" @click="previewPropertyCertImage">
              <image :src="formData.propertyCertImage" mode="aspectFill" class="preview-image" />
              <view class="image-delete" @click.stop="deletePropertyCertImage">
                <uni-icons type="close" size="16" color="#fff" />
              </view>
            </view>
          </view>
          <view class="help-text" @click="showExample">
            <text class="help-link">查看样例</text>
          </view>
        </view>
      </view>
    </view>

    <!-- 底部按钮 -->
    <view class="bottom-actions">
      <button class="submit-button draft" @click="goToHistory">申请记录</button>
      <button class="submit-button primary" @click="submitForm">提交</button>
    </view>
  </view>
</template>

<script>
import { listCommunity, listAllProperty } from '@/api/property.js'

export default {
  data() {
    return {
      formData: {
        ownerName: '',
        ownerPhone: '',
        propertyId: null,
        communityId: null,
        buildingName: null,
        roomNumber: null,
        propertyType: 'property_cert',
        propertyCertImage: ''
      },
      communityList: [],
      communityIndex: -1,
      buildingList: [],
      buildingIndex: -1,
      roomList: [],
      roomIndex: -1,
      propertyTypeList: [
        { label: '房产证', value: 'property_cert' },
        { label: '不动产证', value: 'real_estate_cert' }
      ],
      propertyTypeIndex: 0
    }
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
    },
    selectedPropertyType() {
      return this.propertyTypeList[this.propertyTypeIndex] ? this.propertyTypeList[this.propertyTypeIndex].label : ''
    }
  },
  onLoad() {
    this.loadCommunities()
  },
  methods: {
    // 加载小区列表
    async loadCommunities() {
      try {
        const res = await listCommunity({ pageNum: 1, pageSize: 1000 })
        this.communityList = res.rows || []
        if (this.communityList.length > 0 && this.communityIndex === -1) {
          this.communityIndex = 0
          this.formData.communityId = this.communityList[0].communityId
          await this.loadBuildings(this.communityList[0].communityId)
        }
      } catch (error) {
        console.error('加载小区列表失败', error)
        uni.showToast({
          title: '加载小区列表失败',
          icon: 'none'
        })
      }
    },

    // 加载楼栋列表
    async loadBuildings(communityId) {
      try {
        const res = await listAllProperty({ communityId, pageNum: 1, pageSize: 10000 })
        const buildings = {}
        ;(res.rows || []).forEach(p => {
          if (!buildings[p.buildingName]) {
            buildings[p.buildingName] = { buildingName: p.buildingName }
          }
        })
        this.buildingList = Object.values(buildings)
        if (this.buildingList.length > 0 && this.buildingIndex === -1) {
          this.buildingIndex = 0
          this.formData.buildingName = this.buildingList[0].buildingName
          await this.loadRooms(communityId, this.buildingList[0].buildingName)
        }
      } catch (error) {
        console.error('加载楼栋列表失败', error)
      }
    },

    // 加载房间列表
    async loadRooms(communityId, buildingName) {
      try {
        const res = await listAllProperty({ communityId, buildingName, pageNum: 1, pageSize: 10000 })
        this.roomList = res.rows || []
        if (this.roomList.length > 0 && this.roomIndex === -1) {
          this.roomIndex = 0
          this.formData.propertyId = this.roomList[0].propertyId
          this.formData.roomNumber = this.roomList[0].roomNumber
        }
      } catch (error) {
        console.error('加载房间列表失败', error)
      }
    },

    // 小区选择改变
    handleCommunityChange(e) {
      this.communityIndex = e.detail.value
      const community = this.communityList[this.communityIndex]
      if (community) {
        this.formData.communityId = community.communityId
        // 重置后续选择
        this.buildingIndex = -1
        this.roomIndex = -1
        this.buildingList = []
        this.roomList = []
        this.formData.buildingName = null
        this.formData.propertyId = null
        this.formData.roomNumber = null
        this.loadBuildings(community.communityId)
      }
    },

    // 楼栋选择改变
    handleBuildingChange(e) {
      this.buildingIndex = e.detail.value
      const building = this.buildingList[this.buildingIndex]
      if (building) {
        this.formData.buildingName = building.buildingName
        // 重置房间选择
        this.roomIndex = -1
        this.roomList = []
        this.formData.propertyId = null
        this.formData.roomNumber = null
        this.loadRooms(this.formData.communityId, building.buildingName)
      }
    },

    // 房号选择改变
    handleRoomChange(e) {
      this.roomIndex = e.detail.value
      const room = this.roomList[this.roomIndex]
      if (room) {
        this.formData.propertyId = room.propertyId
        this.formData.roomNumber = room.roomNumber
      }
    },

    // 产权类型选择改变
    handlePropertyTypeChange(e) {
      this.propertyTypeIndex = e.detail.value
      this.formData.propertyType = this.propertyTypeList[this.propertyTypeIndex].value
    },

    // 选择房产证图片
    choosePropertyCertImage() {
      uni.chooseImage({
        count: 1,
        sizeType: ['compressed'],
        sourceType: ['camera', 'album'],
        success: (res) => {
          const tempFilePath = res.tempFilePaths[0]
          // 检查文件大小
          uni.getFileInfo({
            filePath: tempFilePath,
            success: (info) => {
              if (info.size > 3 * 1024 * 1024) {
                uni.showToast({
                  title: '图片大小不能超过3MB',
                  icon: 'none'
                })
                return
              }
              // TODO: 实际项目中应该调用上传接口
              this.formData.propertyCertImage = tempFilePath
              uni.showToast({
                title: '图片选择成功',
                icon: 'success'
              })
            }
          })
        }
      })
    },

    // 预览房产证图片
    previewPropertyCertImage() {
      uni.previewImage({
        urls: [this.formData.propertyCertImage],
        current: 0
      })
    },

    // 删除房产证图片
    deletePropertyCertImage() {
      uni.showModal({
        title: '提示',
        content: '确定要删除这张图片吗?',
        success: (res) => {
          if (res.confirm) {
            this.formData.propertyCertImage = ''
          }
        }
      })
    },

    // 查看样例
    showExample() {
      uni.showModal({
        title: '查看样例',
        content: '房产证样例说明：请上传清晰的房产证照片，确保产权人姓名、房产地址等信息清晰可见。',
        showCancel: false
      })
    },

    // 表单验证
    validateForm() {
      if (!this.formData.ownerName) {
        uni.showToast({
          title: '请输入业主姓名',
          icon: 'none'
        })
        return false
      }

      if (!this.formData.ownerPhone) {
        uni.showToast({
          title: '请输入手机号码',
          icon: 'none'
        })
        return false
      }

      const phoneReg = /^1[3-9]\d{9}$/
      if (!phoneReg.test(this.formData.ownerPhone)) {
        uni.showToast({
          title: '请输入正确的手机号码',
          icon: 'none'
        })
        return false
      }

      if (!this.formData.propertyId) {
        uni.showToast({
          title: '请选择房产',
          icon: 'none'
        })
        return false
      }

      if (!this.formData.propertyCertImage) {
        uni.showToast({
          title: '请上传房产证',
          icon: 'none'
        })
        return false
      }

      return true
    },

    // 跳转到历史记录
    goToHistory() {
      uni.navigateTo({
        url: '/pageB/owner-change/history',
        fail: (err) => {
          console.error('跳转历史记录页面失败:', err);
          uni.showToast({
            title: '页面跳转失败',
            icon: 'none'
          });
        }
      });
    },

    // 提交表单
    submitForm() {
      if (!this.validateForm()) {
        return
      }

      uni.showModal({
        title: '确认提交',
        content: '确定提交业主变更申请吗?',
        success: (res) => {
          if (res.confirm) {
            this.doSubmit()
          }
        }
      })
    },

    // 执行提交
    doSubmit() {
      uni.showLoading({ title: '提交中...' })

      // TODO: 这里需要调用后端接口提交数据
      // 暂时模拟提交成功
      setTimeout(() => {
        uni.hideLoading()
        uni.showToast({
          title: '提交成功',
          icon: 'success'
        })
        setTimeout(() => {
          uni.navigateBack()
        }, 1500)
      }, 1000)
    }
  }
}
</script>

<style lang="scss" scoped>
.owner-change-container {
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
  display: flex;
  gap: 24rpx;
  padding-bottom: calc(20rpx + env(safe-area-inset-bottom));

  .submit-button {
    flex: 1;
    height: 88rpx;
    border-radius: 12rpx;
    font-size: 32rpx;
    font-weight: 600;
    border: none;

    &.draft {
      background: #F5F5F5;
      color: #595959;
    }

    &.primary {
      background: linear-gradient(135deg, #1890FF, #40A9FF);
      color: #FFFFFF;
    }
  }
}
</style>
