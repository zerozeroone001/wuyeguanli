<template>
  <view class="consultation-container">
    <!-- 服务介绍 -->
    <view class="service-intro">
      <view class="intro-header">
        <uni-icons type="help" size="24" color="#1890FF" />
        <text class="intro-title">法律咨询服务</text>
      </view>
      <text class="intro-desc">专业律师团队为您提供物业相关法律咨询服务，解答疑难问题，维护合法权益</text>
    </view>

    <!-- 快速咨询入口 -->
    <view class="quick-actions">
      <view class="action-item" @click="quickConsult('property')">
        <view class="action-icon property">
          <uni-icons type="home" size="20" color="#1890FF" />
        </view>
        <text class="action-text">物业纠纷</text>
      </view>
      <view class="action-item" @click="quickConsult('contract')">
        <view class="action-icon contract">
          <uni-icons type="paperplane" size="20" color="#52C41A" />
        </view>
        <text class="action-text">合同问题</text>
      </view>
      <view class="action-item" @click="quickConsult('fee')">
        <view class="action-icon fee">
          <uni-icons type="wallet" size="20" color="#FA8C16" />
        </view>
        <text class="action-text">收费争议</text>
      </view>
      <view class="action-item" @click="quickConsult('other')">
        <view class="action-icon other">
          <uni-icons type="help" size="20" color="#722ED1" />
        </view>
        <text class="action-text">其他咨询</text>
      </view>
    </view>

    <!-- 咨询状态筛选 -->
    <view class="status-tabs">
      <view 
        class="tab-item" 
        :class="{ active: activeStatus === 'all' }"
        @click="switchStatus('all')"
      >
        全部咨询
      </view>
      <view 
        class="tab-item" 
        :class="{ active: activeStatus === 'pending' }"
        @click="switchStatus('pending')"
      >
        待回复
      </view>
      <view 
        class="tab-item" 
        :class="{ active: activeStatus === 'replied' }"
        @click="switchStatus('replied')"
      >
        已回复
      </view>
      <view 
        class="tab-item" 
        :class="{ active: activeStatus === 'closed' }"
        @click="switchStatus('closed')"
      >
        已关闭
      </view>
    </view>

    <!-- 我的咨询列表 -->
    <view class="consultation-list">
      <view class="list-header">
        <text class="list-title">我的咨询记录</text>
        <text class="consultation-count">共{{ filteredConsultations.length }}条</text>
      </view>
      
      <view 
        class="consultation-item" 
        v-for="consultation in filteredConsultations" 
        :key="consultation.consultationId"
        @click="viewConsultation(consultation)"
      >
        <view class="consultation-header">
          <view class="consultation-info">
            <text class="consultation-title">{{ consultation.title }}</text>
            <view class="consultation-meta">
              <text class="consultation-type">{{ getTypeText(consultation.consultationType) }}</text>
              <text class="consultation-time">{{ consultation.createTime }}</text>
            </view>
          </view>
          <view class="consultation-status" :class="[consultation.status === 'pending' ? 'status-pending' : consultation.status === 'replied' ? 'status-replied' : consultation.status === 'closed' ? 'status-closed' : 'status-default']">
            {{ getStatusText(consultation.status) }}
          </view>
        </view>
        
        <view class="consultation-content">
          <text class="content-preview">{{ consultation.content }}</text>
        </view>
        
        <view class="consultation-footer">
          <view class="lawyer-info" v-if="consultation.lawyerName">
            <uni-icons type="person" size="14" color="#8C8C8C" />
            <text>负责律师：{{ consultation.lawyerName }}</text>
          </view>
          <view class="reply-info" v-if="consultation.replyCount > 0">
            <uni-icons type="chat" size="14" color="#1890FF" />
            <text>{{ consultation.replyCount }}条回复</text>
          </view>
          <view class="urgency-info" v-if="consultation.urgency === '1'">
            <uni-icons type="flag" size="14" color="#FF4D4F" />
            <text>紧急</text>
          </view>
        </view>
      </view>
    </view>

    <!-- 空状态 -->
    <view class="empty-state" v-if="filteredConsultations.length === 0">
      <uni-icons type="help" size="80" color="#D9D9D9" />
      <text class="empty-text">暂无咨询记录</text>
      <button class="empty-action" @click="addConsultation">立即咨询</button>
    </view>

    <!-- 专家团队 -->
    <view class="expert-team">
      <view class="team-header">
        <uni-icons type="person" size="20" color="#262626" />
        <text class="team-title">专家团队</text>
      </view>
      
      <view class="expert-list">
        <view 
          class="expert-item" 
          v-for="expert in expertTeam" 
          :key="expert.id"
          @click="viewExpert(expert)"
        >
          <image class="expert-avatar" :src="expert.avatar" />
          <view class="expert-info">
            <text class="expert-name">{{ expert.name }}</text>
            <text class="expert-title">{{ expert.title }}</text>
            <view class="expert-specialties">
              <text 
                class="specialty-tag" 
                v-for="specialty in expert.specialties" 
                :key="specialty"
              >
                {{ specialty }}
              </text>
            </view>
          </view>
          <view class="expert-stats">
            <text class="stat-item">{{ expert.consultationCount }}次咨询</text>
            <text class="stat-item">{{ expert.rating }}分</text>
          </view>
        </view>
      </view>
    </view>

    <!-- 常见问题 -->
    <view class="faq-section">
      <view class="faq-header">
        <uni-icons type="help-filled" size="20" color="#262626" />
        <text class="faq-title">常见问题</text>
        <text class="more-link" @click="viewAllFAQ">查看更多 ></text>
      </view>
      
      <view class="faq-list">
        <view 
          class="faq-item" 
          v-for="faq in faqList" 
          :key="faq.id"
          @click="viewFAQ(faq)"
        >
          <view class="faq-question">
            <uni-icons type="help" size="16" color="#1890FF" />
            <text>{{ faq.question }}</text>
          </view>
          <view class="faq-preview">
            <text>{{ faq.answerPreview }}</text>
          </view>
        </view>
      </view>
    </view>

    <!-- 浮动咨询按钮 -->
    <view class="floating-action" @click="addConsultation">
      <uni-icons type="plus" size="24" color="#FFFFFF" />
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      activeStatus: 'all',
      consultationList: [],
      expertTeam: [],
      faqList: []
    }
  },
  computed: {
    filteredConsultations() {
      if (this.activeStatus === 'all') {
        return this.consultationList
      }
      return this.consultationList.filter(item => item.status === this.activeStatus)
    }
  },
  onLoad() {
    this.loadConsultations()
    this.loadExpertTeam()
    this.loadFAQ()
  },
  onPullDownRefresh() {
    this.loadConsultations()
    this.loadExpertTeam()
    this.loadFAQ()
    setTimeout(() => {
      uni.stopPullDownRefresh()
    }, 1000)
  },
  methods: {
    loadConsultations() {
      // 模拟咨询数据
      this.consultationList = [
        {
          consultationId: 1,
          title: '物业费收费标准是否合理？',
          content: '我们小区的物业费每平米3.5元，感觉偏高，请问这个收费标准是否合理？有什么法律依据吗？',
          consultationType: 'fee',
          status: 'replied',
          urgency: '2',
          createTime: '2024-01-15 10:30',
          lawyerName: '张律师',
          replyCount: 2,
          lastReplyTime: '2024-01-16 14:20'
        },
        {
          consultationId: 2,
          title: '业主委员会选举程序问题',
          content: '我们小区业委会选举过程中存在程序不规范的问题，请问应该如何处理？',
          consultationType: 'property',
          status: 'pending',
          urgency: '1',
          createTime: '2024-01-14 16:45',
          lawyerName: '李律师',
          replyCount: 0
        },
        {
          consultationId: 3,
          title: '物业服务合同纠纷',
          content: '物业公司提供的服务与合同约定不符，业主可以要求解除合同吗？',
          consultationType: 'contract',
          status: 'replied',
          urgency: '2',
          createTime: '2024-01-13 09:15',
          lawyerName: '王律师',
          replyCount: 3,
          lastReplyTime: '2024-01-14 11:30'
        },
        {
          consultationId: 4,
          title: '共有部分收益分配问题',
          content: '小区广告位、停车位等共有部分的收益应该如何分配？业主有知情权吗？',
          consultationType: 'property',
          status: 'closed',
          urgency: '2',
          createTime: '2024-01-10 14:20',
          lawyerName: '赵律师',
          replyCount: 1,
          lastReplyTime: '2024-01-11 16:45'
        }
      ]
    },
    
    loadExpertTeam() {
      // 模拟专家团队数据
      this.expertTeam = [
        {
          id: 1,
          name: '张建华',
          title: '高级合伙人律师',
          avatar: 'https://randomuser.me/api/portraits/men/1.jpg',
          specialties: ['物业纠纷', '合同法', '房地产法'],
          consultationCount: 156,
          rating: 4.9,
          experience: '15年执业经验'
        },
        {
          id: 2,
          name: '李美玲',
          title: '资深律师',
          avatar: 'https://randomuser.me/api/portraits/women/2.jpg',
          specialties: ['业主权益', '物业管理', '民事诉讼'],
          consultationCount: 89,
          rating: 4.8,
          experience: '10年执业经验'
        },
        {
          id: 3,
          name: '王志强',
          title: '专业律师',
          avatar: 'https://randomuser.me/api/portraits/men/3.jpg',
          specialties: ['收费争议', '服务合同', '法律顾问'],
          consultationCount: 124,
          rating: 4.7,
          experience: '12年执业经验'
        }
      ]
    },
    
    loadFAQ() {
      // 模拟常见问题数据
      this.faqList = [
        {
          id: 1,
          question: '物业费可以拒交吗？',
          answerPreview: '业主不能无理由拒交物业费，但在特定情况下可以...',
          category: 'fee'
        },
        {
          id: 2,
          question: '业主委员会有哪些职责？',
          answerPreview: '业主委员会是业主大会的执行机构，主要职责包括...',
          category: 'property'
        },
        {
          id: 3,
          question: '如何更换物业公司？',
          answerPreview: '更换物业公司需要经过业主大会决议，程序包括...',
          category: 'contract'
        },
        {
          id: 4,
          question: '物业服务不到位怎么办？',
          answerPreview: '遇到物业服务不到位的情况，业主可以采取以下措施...',
          category: 'property'
        }
      ]
    },
    
    switchStatus(status) {
      this.activeStatus = status
    },
    
    getTypeText(type) {
      const typeMap = {
        'property': '物业纠纷',
        'contract': '合同问题',
        'fee': '收费争议',
        'other': '其他咨询'
      }
      return typeMap[type] || '未知类型'
    },
    
    getStatusClass(status) {
      const classMap = {
        'pending': 'status-pending',
        'replied': 'status-replied',
        'closed': 'status-closed'
      }
      return classMap[status] || 'status-default'
    },
    
    getStatusText(status) {
      const textMap = {
        'pending': '待回复',
        'replied': '已回复',
        'closed': '已关闭'
      }
      return textMap[status] || '未知状态'
    },
    
    quickConsult(type) {
      uni.navigateTo({
        url: `/pages/legal-consultation/add?type=${type}`
      })
    },
    
    addConsultation() {
      uni.navigateTo({
        url: '/pages/legal-consultation/add'
      })
    },
    
    viewConsultation(consultation) {
      uni.navigateTo({
        url: `/pages/legal-consultation/detail?id=${consultation.consultationId}`
      })
    },
    
    viewExpert(expert) {
      uni.showToast({
        title: '专家详情功能开发中',
        icon: 'none'
      })
    },
    
    viewFAQ(faq) {
      uni.showToast({
        title: '常见问题详情功能开发中',
        icon: 'none'
      })
    },
    
    viewAllFAQ() {
      uni.showToast({
        title: 'FAQ页面功能开发中',
        icon: 'none'
      })
    }
  }
}
</script>

<style lang="scss" scoped>
@import '@/static/scss/global.scss';

page {
  background-color: #FAFBFC;
  padding-bottom: 120rpx;
}

.consultation-container {
  min-height: 100vh;
  background-color: #FAFBFC;
}

.service-intro {
  background: linear-gradient(135deg, #1890FF, #40A9FF);
  margin: 20rpx;
  border-radius: 24rpx;
  padding: 40rpx;
  color: #FFFFFF;
  
  .intro-header {
    display: flex;
    align-items: center;
    margin-bottom: 20rpx;
    
    .intro-title {
      margin-left: 12rpx;
      font-size: 32rpx;
      font-weight: 600;
    }
  }
  
  .intro-desc {
    font-size: 26rpx;
    line-height: 1.6;
    opacity: 0.9;
  }
}

.quick-actions {
  display: flex;
  background: #FFFFFF;
  margin: 0 20rpx 20rpx;
  border-radius: 24rpx;
  padding: 30rpx 20rpx;
  border: 1rpx solid #F0F0F0;
  
  .action-item {
    flex: 1;
    display: flex;
    flex-direction: column;
    align-items: center;
    
    &:active {
      opacity: 0.7;
    }
    
    .action-icon {
      width: 80rpx;
      height: 80rpx;
      border-radius: 50%;
      display: flex;
      align-items: center;
      justify-content: center;
      margin-bottom: 16rpx;
      
      &.property {
        background: #E6F7FF;
      }
      
      &.contract {
        background: #F6FFED;
      }
      
      &.fee {
        background: #FFF2E8;
      }
      
      &.other {
        background: #F9F0FF;
      }
    }
    
    .action-text {
      font-size: 24rpx;
      color: #262626;
      font-weight: 500;
    }
  }
}

.status-tabs {
  display: flex;
  background: #FFFFFF;
  margin: 0 20rpx 20rpx;
  border-radius: 24rpx;
  padding: 10rpx;
  border: 1rpx solid #F0F0F0;
  
  .tab-item {
    flex: 1;
    text-align: center;
    padding: 20rpx 0;
    font-size: 24rpx;
    color: #8C8C8C;
    border-radius: 20rpx;
    transition: all 0.3s ease;
    
    &.active {
      background: #1890FF;
      color: #FFFFFF;
      font-weight: 600;
    }
  }
}

.consultation-list {
  padding: 0 20rpx;
  
  .list-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    padding: 0 30rpx 20rpx;
    
    .list-title {
      font-size: 28rpx;
      font-weight: 600;
      color: #262626;
    }
    
    .consultation-count {
      font-size: 24rpx;
      color: #8C8C8C;
    }
  }
  
  .consultation-item {
    background: #FFFFFF;
    border-radius: 24rpx;
    padding: 30rpx;
    margin-bottom: 20rpx;
    border: 1rpx solid #F0F0F0;
    
    &:active {
      background: #F8F9FA;
    }
    
    .consultation-header {
      display: flex;
      justify-content: space-between;
      align-items: flex-start;
      margin-bottom: 20rpx;
      
      .consultation-info {
        flex: 1;
        margin-right: 20rpx;
        
        .consultation-title {
          display: block;
          font-size: 28rpx;
          font-weight: 600;
          color: #262626;
          line-height: 1.4;
          margin-bottom: 12rpx;
        }
        
        .consultation-meta {
          display: flex;
          align-items: center;
          gap: 16rpx;
          
          .consultation-type {
            font-size: 22rpx;
            color: #1890FF;
            padding: 4rpx 12rpx;
            background: #E6F7FF;
            border-radius: 12rpx;
          }
          
          .consultation-time {
            font-size: 22rpx;
            color: #8C8C8C;
          }
        }
      }
      
      .consultation-status {
        padding: 8rpx 16rpx;
        border-radius: 16rpx;
        font-size: 22rpx;
        font-weight: 500;
        white-space: nowrap;
        
        &.status-pending {
          background: #FFF2E8;
          color: #FA8C16;
        }
        
        &.status-replied {
          background: #F6FFED;
          color: #52C41A;
        }
        
        &.status-closed {
          background: #F5F5F5;
          color: #8C8C8C;
        }
      }
    }
    
    .consultation-content {
      margin-bottom: 20rpx;
      
      .content-preview {
        font-size: 26rpx;
        color: #595959;
        line-height: 1.6;
        display: -webkit-box;
        -webkit-line-clamp: 2;
        -webkit-box-orient: vertical;
        overflow: hidden;
      }
    }
    
    .consultation-footer {
      display: flex;
      align-items: center;
      gap: 24rpx;
      
      .lawyer-info, .reply-info, .urgency-info {
        display: flex;
        align-items: center;
        
        text {
          margin-left: 8rpx;
          font-size: 22rpx;
          color: #8C8C8C;
        }
      }
      
      .reply-info text {
        color: #1890FF;
      }
      
      .urgency-info text {
        color: #FF4D4F;
      }
    }
  }
}

.empty-state {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 120rpx 40rpx;
  
  .empty-text {
    margin: 30rpx 0;
    font-size: 28rpx;
    color: #8C8C8C;
  }
  
  .empty-action {
    padding: 16rpx 32rpx;
    background: #1890FF;
    color: #FFFFFF;
    border-radius: 20rpx;
    font-size: 26rpx;
    border: none;
    
    &:active {
      background: #096DD9;
    }
  }
}

.expert-team {
  background: #FFFFFF;
  margin: 0 20rpx 20rpx;
  border-radius: 24rpx;
  padding: 40rpx;
  border: 1rpx solid #F0F0F0;
  
  .team-header {
    display: flex;
    align-items: center;
    margin-bottom: 30rpx;
    
    .team-title {
      margin-left: 12rpx;
      font-size: 28rpx;
      font-weight: 600;
      color: #262626;
    }
  }
  
  .expert-list {
    .expert-item {
      display: flex;
      align-items: center;
      padding: 20rpx 0;
      border-bottom: 1rpx solid #F0F0F0;
      
      &:last-child {
        border-bottom: none;
      }
      
      &:active {
        background: #F8F9FA;
      }
      
      .expert-avatar {
        width: 80rpx;
        height: 80rpx;
        border-radius: 50%;
        margin-right: 20rpx;
      }
      
      .expert-info {
        flex: 1;
        
        .expert-name {
          display: block;
          font-size: 26rpx;
          font-weight: 600;
          color: #262626;
          margin-bottom: 8rpx;
        }
        
        .expert-title {
          display: block;
          font-size: 22rpx;
          color: #8C8C8C;
          margin-bottom: 12rpx;
        }
        
        .expert-specialties {
          display: flex;
          gap: 8rpx;
          flex-wrap: wrap;
          
          .specialty-tag {
            font-size: 20rpx;
            color: #1890FF;
            background: #E6F7FF;
            padding: 4rpx 8rpx;
            border-radius: 8rpx;
          }
        }
      }
      
      .expert-stats {
        display: flex;
        flex-direction: column;
        align-items: flex-end;
        
        .stat-item {
          font-size: 20rpx;
          color: #8C8C8C;
          margin-bottom: 4rpx;
          
          &:last-child {
            margin-bottom: 0;
          }
        }
      }
    }
  }
}

.faq-section {
  background: #FFFFFF;
  margin: 0 20rpx 20rpx;
  border-radius: 24rpx;
  padding: 40rpx;
  border: 1rpx solid #F0F0F0;
  
  .faq-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    margin-bottom: 30rpx;
    
    .faq-title {
      margin-left: 12rpx;
      font-size: 28rpx;
      font-weight: 600;
      color: #262626;
    }
    
    .more-link {
      font-size: 24rpx;
      color: #1890FF;
    }
  }
  
  .faq-list {
    .faq-item {
      padding: 20rpx 0;
      border-bottom: 1rpx solid #F0F0F0;
      
      &:last-child {
        border-bottom: none;
      }
      
      &:active {
        background: #F8F9FA;
      }
      
      .faq-question {
        display: flex;
        align-items: center;
        margin-bottom: 12rpx;
        
        text {
          margin-left: 12rpx;
          font-size: 26rpx;
          font-weight: 500;
          color: #262626;
        }
      }
      
      .faq-preview {
        padding-left: 28rpx;
        
        text {
          font-size: 24rpx;
          color: #8C8C8C;
          line-height: 1.4;
        }
      }
    }
  }
}

.floating-action {
  position: fixed;
  bottom: 40rpx;
  right: 40rpx;
  width: 100rpx;
  height: 100rpx;
  background: #1890FF;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 4rpx 12rpx rgba(24, 144, 255, 0.3);
  z-index: 999;
  
  &:active {
    background: #096DD9;
    transform: scale(0.95);
  }
}
</style>
 
 