<template>
  <view class="my-votes-container">
    <!-- 页面标题 -->
   <!-- <view class="page-header">
      <view class="header-content">
        <view class="header-left" @click="goBack">
          <uni-icons type="left" size="20" color="#333" />
        </view>
        <text class="header-title">我的投票记录</text>
        <view class="header-right"></view>
      </view>
    </view> -->

    <!-- 筛选条件 -->
    <view class="filter-section">
      <view class="filter-tabs">
        <view 
          class="filter-tab" 
          :class="{ active: activeTab === 'all' }"
          @click="switchTab('all')"
        >
          <text>全部</text>
        </view>
        <view 
          class="filter-tab" 
          :class="{ active: activeTab === 'ongoing' }"
          @click="switchTab('ongoing')"
        >
          <text>进行中</text>
        </view>
        <view 
          class="filter-tab" 
          :class="{ active: activeTab === 'finished' }"
          @click="switchTab('finished')"
        >
          <text>已结束</text>
        </view>
      </view>
    </view>

    <!-- 投票记录列表 -->
    <view class="votes-list">
      <view v-if="loading" class="loading-container">
        <uni-load-more status="loading" />
      </view>
      
      <view v-else-if="voteList.length === 0" class="empty-container">
        <image class="empty-icon" src="/static/images/empty-vote.png" mode="aspectFit" />
        <text class="empty-text">暂无投票记录</text>
        <text class="empty-desc">您还没有参与任何投票</text>
      </view>
      
      <view v-else class="vote-items">
        <!-- 调试按钮 -->
      <!--  <view class="debug-section" style="padding: 20rpx; background: #f0f0f0; margin-bottom: 20rpx;">
          <button @click="debugTest" style="margin: 10rpx; padding: 20rpx; background: #1890FF; color: white; border: none; border-radius: 8rpx;">调试测试</button>
          <text style="display: block; margin-top: 10rpx; font-size: 24rpx;">数据长度: {{ voteList.length }}</text>
        </view> -->
        
        <view 
          class="vote-item" 
          v-for="(item, index) in voteList" 
          :key="index"
          @click="viewVoteDetail(item)"
        >
          <view class="vote-header">
            <view class="vote-title-section">
              <text class="vote-title">{{ item.meetingTitle }}</text>
              <view class="vote-status" :class="'status-' + item.status">
                <text class="status-text">{{ getStatusText(item.status) }}</text>
              </view>
            </view>
            <view class="vote-time">
              <text class="time-text">{{ formatTime(item.createTime) }}</text>
            </view>
          </view>
          
          <view class="vote-content">
            <view class="topic-info">
              <text class="topic-title">{{ item.topicTitle }}</text>
              <text class="topic-desc">{{ item.topicDescription }}</text>
            </view>
            
            <view class="my-choice">
              <text class="choice-label">我的选择：</text>
              <text class="choice-value" :class="{
                'choice-agree': item.myChoice && item.myChoice.includes('同意'),
                'choice-disagree': item.myChoice && item.myChoice.includes('反对'),
                'choice-abstain': item.myChoice && item.myChoice.includes('弃权')
              }">
                {{ item.myChoice }}
              </text>
            </view>
          </view>
          
          <view class="vote-footer">
            <view class="vote-stats">
              <text class="stats-text">参与人数：{{ item.voteCount || 0 }}人</text>
            </view>
            <view class="vote-actions">
              <text class="action-text">查看详情</text>
              <uni-icons type="right" size="14" color="#C0C4CC" />
            </view>
          </view>
        </view>
      </view>
    </view>

    <!-- 加载更多 -->
    <view v-if="hasMore && !loading" class="load-more">
      <uni-load-more status="more" @clickLoadMore="loadMore" />
    </view>
  </view>
</template>

<script>
import { getMyVoteRecords } from '@/api/property/meeting'

export default {
  data() {
    return {
      activeTab: 'all',
      voteList: [],
      loading: false,
      hasMore: true,
      queryParams: {
        pageNum: 1,
        pageSize: 10,
        status: null
      }
    }
  },
  
  onLoad() {
    console.log('页面加载，开始检查用户状态')
    // 检查用户是否已登录
    const token = uni.getStorageSync('token')
    console.log('用户token：', token ? '已登录' : '未登录')
    
    // 添加调试按钮
    // this.addDebugButton()
    this.loadVoteRecords()
  },
  
  onPullDownRefresh() {
    this.refreshData()
  },
  
  onReachBottom() {
    if (this.hasMore && !this.loading) {
      this.loadMore()
    }
  },
  
  methods: {
    goBack() {
      uni.navigateBack()
    },
    
    switchTab(tab) {
      this.activeTab = tab
      this.queryParams.status = tab === 'all' ? null : tab
      this.refreshData()
    },
    
    async loadVoteRecords() {
      if (this.loading) return
      
      this.loading = true
      try {
        console.log('开始加载投票记录，参数：', this.queryParams)
        const response = await getMyVoteRecords(this.queryParams)
        console.log('投票记录API响应：', response)
        
        if (response.code === 200) {
          const newData = response.rows || []
          console.log('获取到的数据：', newData)
          console.log('数据长度：', newData.length)
          
          if (this.queryParams.pageNum === 1) {
            this.voteList = newData
          } else {
            this.voteList = [...this.voteList, ...newData]
          }
          
          console.log('更新后的voteList：', this.voteList)
          this.hasMore = newData.length === this.queryParams.pageSize
        } else {
          console.error('API返回错误：', response)
          uni.showToast({
            title: response.msg || '获取数据失败',
            icon: 'none'
          })
        }
      } catch (error) {
        console.error('加载投票记录失败:', error)
        uni.showToast({
          title: '加载失败，请重试',
          icon: 'none'
        })
      } finally {
        this.loading = false
        uni.stopPullDownRefresh()
      }
    },
    
    refreshData() {
      this.queryParams.pageNum = 1
      this.hasMore = true
      this.loadVoteRecords()
    },
    
    loadMore() {
      this.queryParams.pageNum++
      this.loadVoteRecords()
    },
    
    viewVoteDetail(item) {
      // 跳转到投票详情页面
      uni.navigateTo({
        url: `/pages/property/meeting/detail?id=${item.meetingId}&from=my-votes`
      })
    },
    
    getStatusText(status) {
      const statusMap = {
        'ongoing': '进行中',
        'finished': '已结束',
        'pending': '未开始'
      }
      return statusMap[status] || '未知'
    },
    
    getChoiceType(choice) {
      // 根据选择内容返回类型
      if (choice && choice.includes('同意')) {
        return 'agree'
      } else if (choice && choice.includes('反对')) {
        return 'disagree'
      } else if (choice && choice.includes('弃权')) {
        return 'abstain'
      }
      return 'default'
    },
    
    formatTime(time) {
      if (!time) return ''
      const date = new Date(time)
      const now = new Date()
      const diff = now - date
      
      if (diff < 60000) { // 1分钟内
        return '刚刚'
      } else if (diff < 3600000) { // 1小时内
        return Math.floor(diff / 60000) + '分钟前'
      } else if (diff < 86400000) { // 24小时内
        return Math.floor(diff / 3600000) + '小时前'
      } else {
        return date.toLocaleDateString()
      }
    },
    
    // addDebugButton() {
    //   // 添加调试按钮到页面
    //   console.log('添加调试按钮')
    // },
    
  //   async debugTest() {
  //     try {
  //       console.log('=== 开始调试测试 ===')
  //       console.log('1. 检查getMyVoteRecords函数:', typeof getMyVoteRecords)
  //       console.log('2. 检查queryParams:', this.queryParams)
        
  //       // 测试简单的uni.request
  //       console.log('3. 测试uni.request...')
  //       const testResponse = await new Promise((resolve, reject) => {
  //         uni.request({
  //           url: '/meeting/vote/my',
  //           method: 'GET',
  //           data: this.queryParams,
  //           success: (res) => {
  //             console.log('uni.request成功:', res)
  //             resolve(res.data)
  //           },
  //           fail: (err) => {
  //             console.error('uni.request失败:', err)
  //             reject(err)
  //           }
  //         })
  //       })
        
  //       console.log('4. uni.request测试完成:', testResponse)
        
  //       uni.showToast({
  //         title: '调试成功，查看控制台',
  //         icon: 'none',
  //         duration: 3000
  //       })
        
  //     } catch (error) {
  //       console.error('=== 调试测试失败 ===')
  //       console.error('错误类型:', typeof error)
  //       console.error('错误信息:', error.message)
  //       console.error('完整错误:', error)
        
  //       uni.showToast({
  //         title: '调试失败: ' + error.message,
  //         icon: 'none',
  //         duration: 5000
  //       })
  //     }
  //   }
  }
}
</script>

<style lang="scss" scoped>
@import '@/static/scss/global.scss';

.my-votes-container {
  min-height: 100vh;
  background-color: #F8F9FA;
}

.page-header {
  background-color: #FFFFFF;
  border-bottom: 1rpx solid #F0F0F0;
  
  .header-content {
    display: flex;
    align-items: center;
    justify-content: space-between;
    padding: 20rpx 30rpx;
    
    .header-left {
      width: 60rpx;
      height: 60rpx;
      display: flex;
      align-items: center;
      justify-content: center;
    }
    
    .header-title {
      font-size: 32rpx;
      font-weight: 600;
      color: #262626;
    }
    
    .header-right {
      width: 60rpx;
    }
  }
}

.filter-section {
  background-color: #FFFFFF;
  padding: 30rpx;
  border-bottom: 1rpx solid #F0F0F0;
  
  .filter-tabs {
    display: flex;
    background-color: #F8F9FA;
    border-radius: 12rpx;
    padding: 8rpx;
    
    .filter-tab {
      flex: 1;
      text-align: center;
      padding: 16rpx 0;
      border-radius: 8rpx;
      transition: all 0.3s;
      
      text {
        font-size: 26rpx;
        color: #8C8C8C;
        transition: color 0.3s;
      }
      
      &.active {
        background-color: #FFFFFF;
        box-shadow: 0 2rpx 8rpx rgba(0, 0, 0, 0.1);
        
        text {
          color: #1890FF;
          font-weight: 500;
        }
      }
    }
  }
}

.votes-list {
  padding: 30rpx;
  
  .loading-container {
    display: flex;
    justify-content: center;
    padding: 60rpx 0;
  }
  
  .empty-container {
    display: flex;
    flex-direction: column;
    align-items: center;
    padding: 120rpx 0;
    
    .empty-icon {
      width: 200rpx;
      height: 200rpx;
      margin-bottom: 40rpx;
      opacity: 0.6;
    }
    
    .empty-text {
      font-size: 32rpx;
      color: #8C8C8C;
      margin-bottom: 16rpx;
    }
    
    .empty-desc {
      font-size: 24rpx;
      color: #C0C4CC;
    }
  }
  
  .vote-items {
    .vote-item {
      background-color: #FFFFFF;
      border-radius: 20rpx;
      padding: 30rpx;
      margin-bottom: 24rpx;
      border: 1rpx solid #F0F0F0;
      transition: all 0.3s;
      
      &:active {
        transform: scale(0.98);
        background-color: #F8F9FA;
      }
      
      .vote-header {
        display: flex;
        justify-content: space-between;
        align-items: flex-start;
        margin-bottom: 24rpx;
        
        .vote-title-section {
          flex: 1;
          
          .vote-title {
            font-size: 30rpx;
            font-weight: 600;
            color: #262626;
            margin-bottom: 12rpx;
            display: block;
          }
          
          .vote-status {
            display: inline-block;
            padding: 6rpx 16rpx;
            border-radius: 20rpx;
            
            .status-text {
              font-size: 22rpx;
              font-weight: 500;
            }
            
            &.status-ongoing {
              background-color: #E6F7FF;
              
              .status-text {
                color: #1890FF;
              }
            }
            
            &.status-finished {
              background-color: #F6FFED;
              
              .status-text {
                color: #52C41A;
              }
            }
            
            &.status-pending {
              background-color: #FFF7E6;
              
              .status-text {
                color: #FA8C16;
              }
            }
          }
        }
        
        .vote-time {
          .time-text {
            font-size: 22rpx;
            color: #8C8C8C;
          }
        }
      }
      
      .vote-content {
        margin-bottom: 24rpx;
        
        .topic-info {
          margin-bottom: 20rpx;
          
          .topic-title {
            font-size: 28rpx;
            color: #262626;
            font-weight: 500;
            margin-bottom: 8rpx;
            display: block;
          }
          
          .topic-desc {
            font-size: 24rpx;
            color: #8C8C8C;
            line-height: 1.5;
            display: block;
          }
        }
        
        .my-choice {
          display: flex;
          align-items: center;
          
          .choice-label {
            font-size: 24rpx;
            color: #8C8C8C;
            margin-right: 12rpx;
          }
          
          .choice-value {
            font-size: 26rpx;
            font-weight: 500;
            padding: 8rpx 16rpx;
            border-radius: 12rpx;
            
            &.choice-agree {
              background-color: #F6FFED;
              color: #52C41A;
            }
            
            &.choice-disagree {
              background-color: #FFF2F0;
              color: #FF4D4F;
            }
            
            &.choice-abstain {
              background-color: #F8F9FA;
              color: #8C8C8C;
            }
            
            &.choice-default {
              background-color: #F0F0F0;
              color: #595959;
            }
          }
        }
      }
      
      .vote-footer {
        display: flex;
        justify-content: space-between;
        align-items: center;
        
        .vote-stats {
          .stats-text {
            font-size: 22rpx;
            color: #8C8C8C;
          }
        }
        
        .vote-actions {
          display: flex;
          align-items: center;
          
          .action-text {
            font-size: 24rpx;
            color: #1890FF;
            margin-right: 8rpx;
          }
        }
      }
    }
  }
}

.load-more {
  padding: 30rpx;
}
</style>