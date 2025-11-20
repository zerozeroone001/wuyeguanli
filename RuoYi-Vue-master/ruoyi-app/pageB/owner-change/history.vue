<template>
  <view class="history-container">
    <!-- 列表内容 -->
    <view v-if="historyList.length > 0" class="history-list">
      <view
        v-for="(item, index) in historyList"
        :key="item.changeId || index"
        class="history-item"
      >
        <view class="item-header">
          <view class="header-left">
            <text class="community-name">{{ item.communityName || '未知小区' }}</text>
            <text class="property-info">{{ item.propertyInfo }}</text>
          </view>
          <view class="header-right">
            <view :class="['status-badge', 'status-' + item.status]">
              {{ formatStatus(item.status).text }}
            </view>
          </view>
        </view>

        <view class="item-content">
          <view class="content-row">
            <text class="label">业主姓名:</text>
            <text class="value">{{ item.ownerName }}</text>
          </view>
          <view class="content-row">
            <text class="label">手机号码:</text>
            <text class="value">{{ item.ownerPhone }}</text>
          </view>
          <view class="content-row">
            <text class="label">产权类型:</text>
            <text class="value">{{ formatPropertyType(item.propertyType) }}</text>
          </view>
          <view class="content-row">
            <text class="label">提交时间:</text>
            <text class="value">{{ item.createTime }}</text>
          </view>
          <view v-if="item.status === '2' && item.rejectReason" class="content-row reject-reason">
            <text class="label">驳回原因:</text>
            <text class="value error">{{ item.rejectReason }}</text>
          </view>
        </view>

        <view v-if="item.propertyCertImage" class="item-footer">
          <image
            :src="item.propertyCertImage"
            mode="aspectFill"
            class="cert-image"
            @click="previewImage(item.propertyCertImage)"
          />
        </view>
      </view>
    </view>

    <!-- 空状态 -->
    <view v-else class="empty-container">
      <uni-icons type="list" size="80" color="#D9D9D9" />
      <text class="empty-text">您还没有任何申请记录</text>
      <button class="empty-button" @click="goToSubmit">去提交申请</button>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      historyList: []
    };
  },
  onShow() {
    this.getHistoryList();
  },
  methods: {
    // 获取历史记录列表
    getHistoryList() {
      // TODO: 调用实际的业主变更历史记录接口
      // 这里暂时使用模拟数据
      this.getMockHistoryList();
    },

    // 获取模拟数据
    getMockHistoryList() {
      // 模拟从接口获取数据
      setTimeout(() => {
        this.historyList = [
          {
            changeId: 1,
            communityName: '叮咚小区',
            buildingName: '1号楼',
            unitName: '1单元',
            roomNumber: '101',
            ownerName: '张三',
            ownerPhone: '138****1234',
            propertyType: 'property_cert',
            status: '0',
            createTime: '2024-01-15 10:30:00',
            propertyCertImage: 'https://via.placeholder.com/200'
          },
          {
            changeId: 2,
            communityName: '叮咚小区',
            buildingName: '2号楼',
            unitName: '2单元',
            roomNumber: '202',
            ownerName: '李四',
            ownerPhone: '139****5678',
            propertyType: 'real_estate_cert',
            status: '1',
            createTime: '2024-01-10 14:20:00',
            propertyCertImage: 'https://via.placeholder.com/200'
          },
          {
            changeId: 3,
            communityName: '叮咚小区',
            buildingName: '3号楼',
            unitName: '3单元',
            roomNumber: '303',
            ownerName: '王五',
            ownerPhone: '136****9999',
            propertyType: 'property_cert',
            status: '2',
            rejectReason: '房产证图片不清晰,请重新上传',
            createTime: '2024-01-05 16:45:00',
            propertyCertImage: 'https://via.placeholder.com/200'
          }
        ].map(item => ({
          ...item,
          propertyInfo: this.buildPropertyInfo(item)
        }));
      }, 500);
    },

    // 构建房产信息
    buildPropertyInfo(item) {
      const parts = [item.buildingName, item.unitName, item.roomNumber].filter(Boolean);
      return parts.join('');
    },

    // 格式化产权类型
    formatPropertyType(type) {
      const map = {
        'property_cert': '房产证',
        'real_estate_cert': '不动产证'
      };
      return map[type] || '未知';
    },

    // 格式化状态
    formatStatus(status) {
      const map = {
        '0': { text: '待审核', color: '#FF9900' },
        '1': { text: '已通过', color: '#52C41A' },
        '2': { text: '已驳回', color: '#FF4D4F' }
      };
      return map[status] || { text: '未知', color: '#999999' };
    },

    // 预览图片
    previewImage(url) {
      uni.previewImage({
        urls: [url],
        current: 0
      });
    },

    // 跳转到提交页面
    goToSubmit() {
      uni.navigateTo({
        url: '/pageB/owner-change/submit'
      });
    }
  }
}
</script>

<style lang="scss" scoped>
.history-container {
  min-height: 100vh;
  background: #F5F5F5;
  padding: 30rpx;
}

/* 历史记录列表 */
.history-list {
  display: flex;
  flex-direction: column;
  gap: 24rpx;

  .history-item {
    background: #FFFFFF;
    border-radius: 16rpx;
    overflow: hidden;
    box-shadow: 0 2rpx 12rpx rgba(0, 0, 0, 0.04);

    .item-header {
      display: flex;
      align-items: center;
      justify-content: space-between;
      padding: 32rpx;
      border-bottom: 1rpx solid #F0F0F0;

      .header-left {
        flex: 1;
        display: flex;
        flex-direction: column;
        gap: 8rpx;

        .community-name {
          font-size: 32rpx;
          font-weight: 600;
          color: #262626;
        }

        .property-info {
          font-size: 28rpx;
          color: #8C8C8C;
        }
      }

      .header-right {
        .status-badge {
          padding: 8rpx 20rpx;
          border-radius: 24rpx;
          font-size: 24rpx;
          font-weight: 500;

          &.status-0 {
            background: #FFF7E6;
            color: #FF9900;
          }

          &.status-1 {
            background: #F6FFED;
            color: #52C41A;
          }

          &.status-2 {
            background: #FFF1F0;
            color: #FF4D4F;
          }
        }
      }
    }

    .item-content {
      padding: 32rpx;
      display: flex;
      flex-direction: column;
      gap: 20rpx;

      .content-row {
        display: flex;
        align-items: flex-start;
        font-size: 28rpx;

        .label {
          width: 160rpx;
          color: #8C8C8C;
          flex-shrink: 0;
        }

        .value {
          flex: 1;
          color: #262626;

          &.error {
            color: #FF4D4F;
          }
        }

        &.reject-reason {
          background: #FFF1F0;
          padding: 16rpx;
          border-radius: 8rpx;
          margin-top: 8rpx;
        }
      }
    }

    .item-footer {
      padding: 0 32rpx 32rpx;

      .cert-image {
        width: 200rpx;
        height: 200rpx;
        border-radius: 12rpx;
        border: 1rpx solid #E8E8E8;
      }
    }
  }
}

/* 空状态 */
.empty-container {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 120rpx 60rpx;
  gap: 32rpx;

  .empty-text {
    font-size: 28rpx;
    color: #8C8C8C;
  }

  .empty-button {
    width: 280rpx;
    height: 72rpx;
    line-height: 72rpx;
    background: linear-gradient(135deg, #1890FF, #40A9FF);
    color: #FFFFFF;
    border-radius: 36rpx;
    font-size: 28rpx;
    font-weight: 500;
    border: none;
  }
}
</style>
