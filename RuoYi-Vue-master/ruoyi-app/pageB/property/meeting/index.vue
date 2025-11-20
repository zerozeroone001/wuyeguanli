<template>
  <view class="meeting-container">
    <!-- 灏忓尯閫夋嫨 -->
    <view class="community-selector">
      <picker
        class="community-picker"
        mode="selector"
        :range="communityOptions"
        range-key="name"
        :value="selectedCommunityIndex"
        :disabled="communityOptions.length === 0"
        @change="handleCommunityChange"
      >
        <view class="community-picker-inner">
          <uni-icons type="location" size="16" color="#1890FF" />
          <text class="community-name">{{ communityInfo.name }}</text>
          <uni-icons
            v-if="communityOptions.length > 0"
            type="arrowdown"
            size="12"
            color="#1890FF"
            class="community-arrow"
          />
        </view>
      </picker>
    </view>

    <!-- 会议分类 -->
    <view class="meeting-tabs">
      <view 
        class="tab-item" 
        :class="{ active: activeTab === 'ongoing' }"
        @click="switchTab('ongoing')"
      >
        进行中
      </view>
      <view 
        class="tab-item" 
        :class="{ active: activeTab === 'upcoming' }"
        @click="switchTab('upcoming')"
      >
        即将开始
      </view>
      <view 
        class="tab-item" 
        :class="{ active: activeTab === 'finished' }"
        @click="switchTab('finished')"
      >
        已结束
      </view>
    </view>

    <!-- 会议列表 -->
    <view class="meeting-list">
      <view 
        class="meeting-item" 
        v-for="meeting in meetingList" 
        :key="meeting.meetingId"
        @click="viewMeeting(meeting)"
      >
        <view class="meeting-header">
          <text class="meeting-title">{{ meeting.meetingTitle }}</text>
          <view class="meeting-status" :class="statusClassMap[meeting.meetingStatus]">
            {{ statusText(meeting.meetingStatus) }}
          </view>
        </view>
        <view class="meeting-info">
          <view class="info-row">
            <uni-icons type="home" size="16" color="#8C8C8C" />
            <text class="meeting-community">{{ meeting.communityName || '未关联小区' }}</text>
          </view>
          <view class="info-row">
            <uni-icons type="calendar" size="16" color="#8C8C8C" />
            <text class="meeting-time">{{ meeting.meetingTime }}</text>
          </view>
          <view class="info-row">
            <uni-icons type="location" size="16" color="#8C8C8C" />
            <text class="meeting-location">{{ meeting.meetingLocation }}</text>
          </view>
          <view class="info-row">
            <uni-icons type="person" size="16" color="#8C8C8C" />
            <text class="meeting-participants">{{ meeting.actualVoters || 0 }}/{{ meeting.totalVoters || 0 }}人参与</text>
          </view>
        </view>
        <view class="meeting-action">
          <button 
            v-if="meeting.meetingStatus === '1'" 
            class="vote-btn"
            @click.stop="goVote(meeting)"
          >
            参与投票
          </button>
          <button 
            v-else 
            class="detail-btn"
            @click.stop="viewDetail(meeting)"
          >
            查看详情
          </button>
        </view>
      </view>
    </view>

    <!-- 列表为空或加载状态 -->
    <view class="status-view">
      <view class="empty-state" v-if="!loading && meetingList.length === 0">
        <uni-icons type="calendar" size="80" color="#D9D9D9" />
        <text class="empty-text">暂无会议信息</text>
      </view>
      <uni-load-more
        v-if="loading || meetingList.length > 0"
        :status="loadMoreStatus"
      ></uni-load-more>
    </view>

  </view>
</template>

<script>
import config from "@/config";
import { listMyProperty, listCommunity } from "@/api/property.js";
import { listMeeting } from "@/api/property/meeting";
import { mapGetters } from "vuex";

const COMMUNITY_STORAGE_KEY = 'app_current_community_id';
const COMMUNITY_INFO_STORAGE_KEY = 'app_current_community_info';
const TAB_STATUS_MAP = Object.freeze({
  ongoing: '1',
  upcoming: '0',
  finished: '2'
});
const STATUS_TEXT_MAP = Object.freeze({
  '0': '筹备中',
  '1': '进行中',
  '2': '已结束'
});
const STATUS_CLASS_MAP = Object.freeze({
  '0': 'status-upcoming',
  '1': 'status-ongoing',
  '2': 'status-finished'
});
const BASE_COMMUNITY_INFO = (config && config.property && config.property.communityInfo) || {};
const createDefaultCommunityInfo = () => ({ ...BASE_COMMUNITY_INFO });
const createDefaultQueryParams = () => ({
  pageNum: 1,
  pageSize: 10,
  meetingStatus: TAB_STATUS_MAP.ongoing,
  communityId: null
});

export default {
  computed: {
    ...mapGetters(['token']),
    isLoggedIn() {
      return !!this.token;
    },
    currentMeetingStatus() {
      return TAB_STATUS_MAP[this.activeTab] || TAB_STATUS_MAP.ongoing;
    },
    loadMoreStatus() {
      if (this.loading) {
        return 'loading';
      }
      return this.finished ? 'noMore' : 'more';
    }
  },
  data() {
    return {
      activeTab: 'ongoing',
      meetingList: [],
      defaultCommunityInfo: createDefaultCommunityInfo(),
      communityInfo: createDefaultCommunityInfo(),
      statusClassMap: STATUS_CLASS_MAP,
      communityOptions: [],
      selectedCommunityIndex: 0,
      selectedCommunityId: null,
      communityPromptShown: false,
      // 查询参数
      queryParams: createDefaultQueryParams(),
      // 加载状态
      loading: false,
      // 是否已加载全部数据
      finished: false,
      total: 0
    }
  },
  async onLoad() {
    await this.initializeCommunitySelection();
  },
  async onShow() {
    await this.loadCommunityOptions({ reloadOnChange: true, showPrompt: true });
  },
  onHide() {
    this.communityPromptShown = false;
  },
  // 下拉刷新
  onPullDownRefresh() {
    this.resetAndLoad();
    setTimeout(() => {
      uni.stopPullDownRefresh();
    }, 500);
  },
  // 触底加载更多
  onReachBottom() {
    if (!this.selectedCommunityId) {
      return;
    }
    if (!this.finished && !this.loading) {
      this.queryParams.pageNum++;
      this.loadMeetings();
    }
  },
  methods: {
    async initializeCommunitySelection() {
      await this.loadCommunityOptions({ showPrompt: true, reloadOnChange: true, initialLoad: true });
    },
    async loadCommunityOptions(options = {}) {
      const {
        showPrompt = false,
        reloadOnChange = false,
        initialLoad = false
      } = options;
      const previousCommunityId = this.selectedCommunityId;
      if (!this.isLoggedIn) {
        this.communityOptions = [];
        this.resetCommunityInfo();
        if (reloadOnChange || initialLoad) {
          this.clearMeetingData();
        }
        return;
      }
      try {
        const propertyRes = await listMyProperty({ status: '1', pageNum: 1, pageSize: 100 });
        const propertyRows = propertyRes.rows || [];
        const communityIds = Array.from(new Set(propertyRows.map(item => item.communityId).filter(Boolean)));
        if (communityIds.length === 0) {
          this.communityOptions = [];
          this.resetCommunityInfo({ clearStorage: true });
          if (reloadOnChange || initialLoad) {
            this.clearMeetingData();
          }
          if (showPrompt) {
            await this.showEmptyCommunityPrompt();
          }
          return;
        }
        const communityRes = await listCommunity({ pageNum: 1, pageSize: 1000 });
        const communities = (communityRes.rows || []).filter(item => communityIds.includes(item.communityId));
        const optionsList = communities.map(item => ({
          value: item.communityId,
          name: item.communityName,
          address: item.address,
          phone: item.contactPhone,
          raw: item
        }));
        this.communityOptions = optionsList;
        if (optionsList.length > 0) {
          const storedId = uni.getStorageSync(COMMUNITY_STORAGE_KEY);
          let index = optionsList.findIndex(option => option.value === storedId);
          if (index === -1) {
            index = 0;
          }
          this.applyCommunity(optionsList[index], index);
          this.communityPromptShown = false;
        } else {
          this.resetCommunityInfo({ clearStorage: true });
        }
        if ((reloadOnChange || initialLoad) && this.selectedCommunityId) {
          if (this.selectedCommunityId !== previousCommunityId || initialLoad) {
            this.resetAndLoad();
          }
        } else if ((reloadOnChange || initialLoad) && !this.selectedCommunityId) {
          this.clearMeetingData();
        }
      } catch (error) {
        console.error('加载小区信息失败', error);
        this.communityOptions = [];
        this.resetCommunityInfo({ fallbackToStored: true });
        if (reloadOnChange || initialLoad) {
          this.clearMeetingData();
        }
      }
    },
    handleCommunityChange(event) {
      const index = Number(event.detail.value);
      const option = this.communityOptions[index];
      if (option) {
        this.applyCommunity(option, index);
        this.resetAndLoad();
      }
    },
    applyCommunity(option, index = 0) {
      if (option) {
        this.selectedCommunityIndex = index;
        this.selectedCommunityId = option.value;
        this.communityInfo = {
          ...this.defaultCommunityInfo,
          name: option.name || this.defaultCommunityInfo.name,
          address: option.address || this.defaultCommunityInfo.address,
          phone: option.phone || this.defaultCommunityInfo.phone
        };
        uni.setStorageSync(COMMUNITY_STORAGE_KEY, option.value);
        uni.setStorageSync(COMMUNITY_INFO_STORAGE_KEY, this.communityInfo);
        this.communityPromptShown = false;
      } else {
        this.resetCommunityInfo();
      }
    },
    resetCommunityInfo(options = {}) {
      const { fallbackToStored = false, clearStorage = false } = options;
      let info = null;
      if (fallbackToStored) {
        const storedInfo = uni.getStorageSync(COMMUNITY_INFO_STORAGE_KEY);
        if (storedInfo && storedInfo.name) {
          info = storedInfo;
        }
      }
      this.communityInfo = info || { ...this.defaultCommunityInfo };
      this.selectedCommunityId = null;
      this.selectedCommunityIndex = 0;
      this.queryParams.communityId = null;
      if (clearStorage) {
        uni.removeStorageSync(COMMUNITY_STORAGE_KEY);
        uni.removeStorageSync(COMMUNITY_INFO_STORAGE_KEY);
      }
    },
    resetPagination() {
      Object.assign(this.queryParams, createDefaultQueryParams(), {
        meetingStatus: this.currentMeetingStatus
      });
    },
    clearMeetingData() {
      this.resetPagination();
      this.meetingList = [];
      this.total = 0;
      this.finished = true;
      this.loading = false;
    },
    async showEmptyCommunityPrompt() {
      if (this.communityPromptShown) {
        return;
      }
      this.communityPromptShown = true;
      return new Promise(resolve => {
        uni.showModal({
          title: '提示',
          content: '当前尚未绑定任何小区，请先添加房产信息。',
          confirmText: '去添加',
          cancelText: '返回首页',
          success: (res) => {
            if (res.confirm) {
              uni.navigateTo({ url: '/pageB/property/add' });
            } else {
              uni.switchTab({ url: '/pages/index' });
            }
            resolve(res);
          },
          fail: () => resolve()
        });
      });
    },
    // 重置并加载
    resetAndLoad() {
      this.resetPagination();
      this.meetingList = [];
      this.total = 0;
      this.finished = false;
      if (!this.selectedCommunityId) {
        this.finished = true;
        this.loading = false;
        return;
      }
      this.loadMeetings();
    },
    // 加载会议列表
    async loadMeetings() {
      if (this.loading || (!this.selectedCommunityId && this.selectedCommunityId !== 0)) {
        if (!this.selectedCommunityId) {
          this.clearMeetingData();
        }
        return;
      }
      if (this.queryParams.pageNum > 1 && this.finished) {
        return;
      }
      const currentPage = this.queryParams.pageNum;
      const previousPage = currentPage > 1 ? currentPage - 1 : 1;
      const communityId = this.normalizeCommunityId(this.selectedCommunityId);
      const payload = {
        ...this.queryParams,
        pageNum: currentPage,
        meetingStatus: this.currentMeetingStatus,
        communityId: communityId
      };
      this.loading = true;
      try {
        const { rows = [], total = 0 } = await listMeeting(payload);
        const isFirstPage = payload.pageNum === 1;
        this.total = total;
        this.meetingList = isFirstPage ? rows : this.meetingList.concat(rows);
        this.finished = this.meetingList.length >= total || rows.length === 0;
        Object.assign(this.queryParams, {
          pageNum: currentPage,
          meetingStatus: payload.meetingStatus,
          communityId: communityId
        });
      } catch (error) {
        console.error('加载会议列表失败', error);
        this.finished = false;
        this.queryParams.pageNum = previousPage;
      } finally {
        this.loading = false;
      }
    },
    // 切换Tab
    switchTab(tab) {
      if (this.activeTab === tab) {
        return;
      }
      this.activeTab = tab;
      this.resetAndLoad();
    },
    normalizeCommunityId(id) {
      if (id === null || id === undefined) {
        return id;
      }
      const numericId = Number(id);
      return Number.isNaN(numericId) ? id : numericId;
    },
    statusText(status) {
      return STATUS_TEXT_MAP[status] || '未知';
    },
    viewMeeting(meeting) {
      this.navigateToMeeting(meeting);
    },
    
    goVote(meeting) {
      this.navigateToMeeting(meeting);
    },
    
    viewDetail(meeting) {
      this.navigateToMeeting(meeting);
    },
    navigateToMeeting(meeting = {}) {
      if (!meeting.meetingId) {
        return;
      }
      uni.navigateTo({
        url: `/pageB/property/meeting/vote?id=${meeting.meetingId}`
      });
    }
  }
}
</script>

<style lang="scss" scoped>
@import '@/static/scss/global.scss';

page {
  background-color: #FAFBFC;
}

.meeting-container {
  min-height: 100vh;
  background-color: #FAFBFC;
}

.community-selector {
  padding: 20rpx 20rpx 0 20rpx;
}

.community-picker {
  width: 100%;
}

.community-picker-inner {
  display: flex;
  align-items: center;
  background: #FFFFFF;
  border-radius: 20rpx;
  padding: 24rpx 28rpx;
  border: 1rpx solid #E5E5E5;
}

.community-name {
  flex: 1;
  margin: 0 12rpx;
  font-size: 28rpx;
  color: #262626;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.community-arrow {
  margin-left: 8rpx;
}

.meeting-tabs {
  display: flex;
  background: #FFFFFF;
  margin: 20rpx;
  border-radius: 24rpx;
  padding: 10rpx;
  border: 1rpx solid #F0F0F0;
  position: sticky;
  top: 0;
  z-index: 99;
}

.tab-item {
  flex: 1;
  text-align: center;
  padding: 20rpx 0;
  font-size: 28rpx;
  color: #8C8C8C;
  border-radius: 20rpx;
  transition: all 0.3s ease;
  
  &.active {
    background: #1890FF;
    color: #FFFFFF;
    font-weight: 600;
  }
}

.meeting-list {
  padding: 0 20rpx;
}

.meeting-item {
  background: #FFFFFF;
  border-radius: 24rpx;
  padding: 40rpx;
  margin-bottom: 20rpx;
  border: 1rpx solid #F0F0F0;
  
  &:active {
    background: #F8F9FA;
  }
}

.meeting-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
  margin-bottom: 24rpx;
  
  .meeting-title {
    flex: 1;
    font-size: 32rpx;
    font-weight: 600;
    color: #262626;
    line-height: 1.4;
    margin-right: 20rpx;
  }
  
  .meeting-status {
    padding: 8rpx 16rpx;
    border-radius: 16rpx;
    font-size: 22rpx;
    font-weight: 500;
    white-space: nowrap;
    
    &.status-upcoming {
      background: #E6F7FF;
      color: #1890FF;
    }
    
    &.status-ongoing {
      background: #FFF2E8;
      color: #FA8C16;
    }
    
    &.status-finished {
      background: #F6FFED;
      color: #52C41A;
    }
  }
}

.meeting-info {
  margin-bottom: 30rpx;
  
  .info-row {
    display: flex;
    align-items: center;
    margin-bottom: 16rpx;
    
    &:last-child {
      margin-bottom: 0;
    }
    
    text {
      margin-left: 12rpx;
      font-size: 26rpx;
      color: #595959;
    }
  }
}

.meeting-action {
  display: flex;
  justify-content: flex-end;
  
  button {
    margin: 0;
    padding: 16rpx 32rpx;
    border-radius: 20rpx;
    font-size: 26rpx;
    font-weight: 500;
    border: none;
    line-height: 1.5;
    
    &::after {
      border: none;
    }

    &.vote-btn {
      background: #1890FF;
      color: #FFFFFF;
    }
    
    &.detail-btn {
      background: #F0F0F0;
      color: #595959;
    }
    
    &:active {
      opacity: 0.8;
    }
  }
}

.status-view {
  padding-bottom: 40rpx;
}

.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 120rpx 40rpx;
  
  .empty-text {
    margin-top: 30rpx;
    font-size: 28rpx;
    color: #8C8C8C;
  }
}
</style>
