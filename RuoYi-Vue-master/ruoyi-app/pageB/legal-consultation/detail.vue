<template>
  <view class="detail-container">
    <!-- 咨询基本信息 -->
    <view class="consultation-info">
      <view class="info-header">
        <text class="consultation-title">{{ consultationInfo.title }}</text>
        <view class="consultation-status" :class="[consultationInfo.status === 'pending' ? 'status-pending' : consultationInfo.status === 'replied' ? 'status-replied' : consultationInfo.status === 'closed' ? 'status-closed' : 'status-default']">
          {{ getStatusText(consultationInfo.status) }}
        </view>
      </view>
      
      <view class="consultation-meta">
        <view class="meta-item">
          <uni-icons type="list" size="14" color="#8C8C8C" />
          <text>{{ getTypeText(consultationInfo.consultationType) }}</text>
        </view>
        <view class="meta-item">
          <uni-icons type="calendar" size="14" color="#8C8C8C" />
          <text>{{ consultationInfo.createTime }}</text>
        </view>
        <view class="meta-item" v-if="consultationInfo.urgency === '1'">
          <uni-icons type="flag" size="14" color="#FF4D4F" />
          <text>紧急咨询</text>
        </view>
      </view>
    </view>

    <!-- 问题描述 -->
    <view class="problem-section">
      <view class="section-header">
        <uni-icons type="chat" size="20" color="#262626" />
        <text class="section-title">问题描述</text>
      </view>
      <view class="problem-content">
        <text>{{ consultationInfo.content }}</text>
      </view>
      
      <!-- 相关材料 -->
      <view class="materials-section" v-if="consultationInfo.materials && consultationInfo.materials.length > 0">
        <text class="materials-title">相关材料：</text>
        <view class="materials-list">
          <view 
            class="material-item" 
            v-for="material in consultationInfo.materials" 
            :key="material.id"
            @click="viewMaterial(material)"
          >
            <view class="material-preview">
              <image v-if="material.type === 'image'" :src="material.url" class="material-image" />
              <view v-else class="material-doc">
                <uni-icons type="paperplane" size="24" color="#1890FF" />
              </view>
            </view>
            <text class="material-name">{{ material.name }}</text>
          </view>
        </view>
      </view>
    </view>

    <!-- 律师信息 -->
    <view class="lawyer-section" v-if="consultationInfo.lawyerInfo">
      <view class="section-header">
        <uni-icons type="person" size="20" color="#262626" />
        <text class="section-title">负责律师</text>
      </view>
      
      <view class="lawyer-card">
        <image class="lawyer-avatar" :src="consultationInfo.lawyerInfo.avatar" />
        <view class="lawyer-info">
          <text class="lawyer-name">{{ consultationInfo.lawyerInfo.name }}</text>
          <text class="lawyer-title">{{ consultationInfo.lawyerInfo.title }}</text>
          <view class="lawyer-specialties">
            <text 
              class="specialty-tag" 
              v-for="specialty in consultationInfo.lawyerInfo.specialties" 
              :key="specialty"
            >
              {{ specialty }}
            </text>
          </view>
        </view>
        <view class="lawyer-contact">
          <button class="contact-btn" @click="contactLawyer">联系律师</button>
        </view>
      </view>
    </view>

    <!-- 回复记录 -->
    <view class="reply-section">
      <view class="section-header">
        <uni-icons type="chat-filled" size="20" color="#262626" />
        <text class="section-title">律师回复</text>
        <text class="reply-count">{{ replyList.length }}条回复</text>
      </view>
      
      <view class="reply-list" v-if="replyList.length > 0">
        <view 
          class="reply-item" 
          v-for="reply in replyList" 
          :key="reply.replyId"
        >
          <view class="reply-header">
            <view class="replier-info">
              <image class="replier-avatar" :src="reply.lawyerAvatar" />
              <view class="replier-detail">
                <text class="replier-name">{{ reply.lawyerName }}</text>
                <text class="reply-time">{{ reply.replyTime }}</text>
              </view>
            </view>
            <view class="reply-type" :class="[reply.replyType === 'initial' ? 'type-initial' : reply.replyType === 'follow_up' ? 'type-follow-up' : reply.replyType === 'final' ? 'type-final' : 'type-default']">
              {{ getReplyTypeText(reply.replyType) }}
            </view>
          </view>
          
          <view class="reply-content">
            <text>{{ reply.content }}</text>
          </view>
          
          <!-- 回复附件 -->
          <view class="reply-attachments" v-if="reply.attachments && reply.attachments.length > 0">
            <view 
              class="attachment-item" 
              v-for="attachment in reply.attachments" 
              :key="attachment.id"
              @click="viewAttachment(attachment)"
            >
              <uni-icons type="paperplane" size="16" color="#1890FF" />
              <text>{{ attachment.name }}</text>
            </view>
          </view>
          
          <!-- 回复操作 -->
          <view class="reply-actions">
            <view class="action-item" @click="likeReply(reply)">
              <uni-icons 
                type="heart" 
                size="16" 
                :color="reply.isLiked ? '#FF4D4F' : '#8C8C8C'" 
              />
              <text>有用({{ reply.likeCount }})</text>
            </view>
            <view class="action-item" @click="followUpReply(reply)">
              <uni-icons type="chat" size="16" color="#8C8C8C" />
              <text>追问</text>
            </view>
          </view>
        </view>
      </view>
      
      <!-- 无回复状态 -->
      <view class="no-reply" v-else>
        <uni-icons type="chat" size="60" color="#D9D9D9" />
        <text class="no-reply-text">律师正在分析您的问题，请耐心等待</text>
        <text class="no-reply-tip">一般会在{{ getExpectedReplyTime() }}内回复</text>
      </view>
    </view>

    <!-- 追问输入 -->
    <view class="follow-up-section" v-if="consultationInfo.status === 'replied'">
      <view class="section-header">
        <uni-icons type="compose" size="20" color="#262626" />
        <text class="section-title">继续咨询</text>
      </view>
      
      <view class="follow-up-input">
        <textarea 
          class="follow-up-textarea"
          placeholder="如有疑问可继续向律师咨询..."
          v-model="followUpContent"
          maxlength="500"
        />
        <view class="follow-up-actions">
          <text class="char-count">{{ followUpContent.length }}/500</text>
          <button 
            class="send-btn" 
            :disabled="!followUpContent.trim()"
            @click="sendFollowUp"
          >
            发送
          </button>
        </view>
      </view>
    </view>

    <!-- 满意度评价 -->
    <view class="rating-section" v-if="consultationInfo.status === 'replied' && !consultationInfo.hasRated">
      <view class="section-header">
        <uni-icons type="star" size="20" color="#262626" />
        <text class="section-title">服务评价</text>
      </view>
      
      <view class="rating-content">
        <text class="rating-label">请对本次咨询服务进行评价：</text>
        <view class="rating-stars">
          <view 
            class="star-item" 
            v-for="star in 5" 
            :key="star"
            @click="setRating(star)"
          >
            <uni-icons 
              type="star-filled" 
              size="32" 
              :color="star <= rating ? '#FA8C16' : '#D9D9D9'" 
            />
          </view>
        </view>
        <textarea 
          class="rating-comment"
          placeholder="请分享您的使用感受（选填）"
          v-model="ratingComment"
          maxlength="200"
        />
        <button class="rating-submit" @click="submitRating">提交评价</button>
      </view>
    </view>

    <!-- 操作按钮 -->
    <view class="action-buttons" v-if="consultationInfo.status !== 'closed'">
      <button class="action-btn secondary" @click="closeConsultation">
        关闭咨询
      </button>
      <button class="action-btn primary" @click="shareConsultation">
        分享咨询
      </button>
    </view>
  </view>
</template>

<script>
export default {
  data() {
    return {
      consultationId: null,
      consultationInfo: {},
      replyList: [],
      followUpContent: '',
      rating: 0,
      ratingComment: ''
    }
  },
  onLoad(options) {
    this.consultationId = options.id
    this.loadConsultationDetail()
    this.loadReplyList()
  },
  methods: {
    loadConsultationDetail() {
      // 模拟咨询详情数据
      this.consultationInfo = {
        consultationId: 1,
        title: '物业费收费标准是否合理？',
        content: '我们小区的物业费每平米3.5元，感觉偏高，请问这个收费标准是否合理？有什么法律依据吗？我们小区建成于2018年，是高层住宅，有电梯，绿化面积约30%，配有地下停车场和会所。物业公司提供的服务包括保洁、保安、绿化维护等基本服务。但我觉得服务质量一般，收费却比周边小区高出不少。',
        consultationType: 'fee',
        status: 'replied',
        urgency: '2',
        createTime: '2024-01-15 10:30',
        hasRated: false,
        materials: [
          {
            id: 1,
            name: '物业费收费通知.jpg',
            url: 'https://picsum.photos/300/200?random=1',
            type: 'image'
          },
          {
            id: 2,
            name: '服务合同.pdf',
            url: '/static/files/contract.pdf',
            type: 'document'
          }
        ],
        lawyerInfo: {
          id: 1,
          name: '张建华',
          title: '高级合伙人律师',
          avatar: 'https://randomuser.me/api/portraits/men/1.jpg',
          specialties: ['物业纠纷', '合同法', '房地产法'],
          phone: '13800138001'
        }
      }
    },
    
    loadReplyList() {
      // 模拟回复数据
      this.replyList = [
        {
          replyId: 1,
          lawyerName: '张建华',
          lawyerAvatar: 'https://randomuser.me/api/portraits/men/1.jpg',
          replyTime: '2024-01-16 14:20',
          replyType: 'initial',
          content: '您好，根据您描述的情况，我来为您分析一下：\n\n1. 物业费标准的合理性：物业费收费标准应当根据《物业服务收费管理办法》等相关规定，综合考虑物业服务成本、服务质量、小区档次等因素确定。每平米3.5元对于您描述的高层住宅来说，如果服务内容全面，这个标准是在合理范围内的。\n\n2. 法律依据：物业费收费应当遵循"质价相符、公开透明"的原则。物业公司应当公示收费标准、服务内容等信息。\n\n3. 建议：如果您认为收费偏高，可以要求物业公司公示详细的收费构成，也可以通过业主委员会与物业公司协商调整。',
          attachments: [
            {
              id: 1,
              name: '物业服务收费管理办法.pdf',
              url: '/static/files/regulation.pdf'
            }
          ],
          likeCount: 5,
          isLiked: false
        },
        {
          replyId: 2,
          lawyerName: '张建华',
          lawyerAvatar: 'https://randomuser.me/api/portraits/men/1.jpg',
          replyTime: '2024-01-17 09:15',
          replyType: 'follow_up',
          content: '补充说明：如果物业服务确实存在质量问题，业主可以通过以下途径维权：\n\n1. 向物业公司书面投诉，要求改进服务；\n2. 通过业主大会决议更换物业公司；\n3. 向当地房管部门投诉；\n4. 如有必要，可以通过法律途径解决。\n\n需要注意的是，即使对服务不满意，也不能无理由拒交物业费，这可能会影响个人征信。',
          attachments: [],
          likeCount: 3,
          isLiked: true
        }
      ]
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
    
    getReplyTypeClass(type) {
      const classMap = {
        'initial': 'type-initial',
        'follow_up': 'type-follow-up',
        'final': 'type-final'
      }
      return classMap[type] || 'type-default'
    },
    
    getReplyTypeText(type) {
      const textMap = {
        'initial': '初步回复',
        'follow_up': '补充回复',
        'final': '最终回复'
      }
      return textMap[type] || '回复'
    },
    
    getExpectedReplyTime() {
      const urgencyMap = {
        '1': '24小时',
        '2': '3天',
        '3': '7天'
      }
      return urgencyMap[this.consultationInfo.urgency] || '3天'
    },
    
    viewMaterial(material) {
      if (material.type === 'image') {
        uni.previewImage({
          urls: [material.url]
        })
      } else {
        uni.showToast({
          title: '文档预览功能开发中',
          icon: 'none'
        })
      }
    },
    
    viewAttachment(attachment) {
      uni.showToast({
        title: '附件下载功能开发中',
        icon: 'none'
      })
    },
    
    contactLawyer() {
      uni.showActionSheet({
        itemList: ['拨打电话', '发送微信'],
        success: (res) => {
          if (res.tapIndex === 0) {
            uni.makePhoneCall({
              phoneNumber: this.consultationInfo.lawyerInfo.phone
            })
          } else {
            uni.showToast({
              title: '微信联系功能开发中',
              icon: 'none'
            })
          }
        }
      })
    },
    
    likeReply(reply) {
      reply.isLiked = !reply.isLiked
      reply.likeCount += reply.isLiked ? 1 : -1
    },
    
    followUpReply(reply) {
      this.followUpContent = `@${reply.lawyerName} `
    },
    
    async sendFollowUp() {
      if (!this.followUpContent.trim()) return
      
      uni.showLoading({
        title: '发送中...'
      })
      
      try {
        // 模拟发送API调用
        await new Promise(resolve => setTimeout(resolve, 1000))
        
        uni.hideLoading()
        uni.showToast({
          title: '发送成功',
          icon: 'success'
        })
        
        this.followUpContent = ''
      } catch (error) {
        uni.hideLoading()
        uni.showToast({
          title: '发送失败',
          icon: 'none'
        })
      }
    },
    
    setRating(star) {
      this.rating = star
    },
    
    async submitRating() {
      if (this.rating === 0) {
        uni.showToast({
          title: '请选择评分',
          icon: 'none'
        })
        return
      }
      
      uni.showLoading({
        title: '提交中...'
      })
      
      try {
        // 模拟提交评价API调用
        await new Promise(resolve => setTimeout(resolve, 1000))
        
        uni.hideLoading()
        uni.showToast({
          title: '评价成功',
          icon: 'success'
        })
        
        this.consultationInfo.hasRated = true
      } catch (error) {
        uni.hideLoading()
        uni.showToast({
          title: '提交失败',
          icon: 'none'
        })
      }
    },
    
    closeConsultation() {
      uni.showModal({
        title: '确认关闭',
        content: '关闭后将无法继续咨询，确定要关闭吗？',
        success: (res) => {
          if (res.confirm) {
            this.consultationInfo.status = 'closed'
            uni.showToast({
              title: '已关闭咨询',
              icon: 'success'
            })
          }
        }
      })
    },
    
    shareConsultation() {
      uni.showToast({
        title: '分享功能开发中',
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
}

.detail-container {
  min-height: 100vh;
  background-color: #FAFBFC;
  padding-bottom: 40rpx;
}

.consultation-info, .problem-section, .lawyer-section, .reply-section, .follow-up-section, .rating-section {
  background: #FFFFFF;
  margin: 20rpx;
  border-radius: 24rpx;
  padding: 40rpx;
  border: 1rpx solid #F0F0F0;
  
  .section-header {
    display: flex;
    align-items: center;
    justify-content: space-between;
    margin-bottom: 24rpx;
    
    .section-title {
      margin-left: 12rpx;
      font-size: 28rpx;
      font-weight: 600;
      color: #262626;
    }
    
    .reply-count {
      font-size: 22rpx;
      color: #8C8C8C;
    }
  }
}

.consultation-info {
  .info-header {
    display: flex;
    justify-content: space-between;
    align-items: flex-start;
    margin-bottom: 20rpx;
    
    .consultation-title {
      flex: 1;
      font-size: 32rpx;
      font-weight: 600;
      color: #262626;
      line-height: 1.4;
      margin-right: 20rpx;
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
  
  .consultation-meta {
    display: flex;
    align-items: center;
    gap: 24rpx;
    flex-wrap: wrap;
    
    .meta-item {
      display: flex;
      align-items: center;
      
      text {
        margin-left: 8rpx;
        font-size: 22rpx;
        color: #8C8C8C;
      }
    }
  }
}

.problem-content {
  text {
    font-size: 26rpx;
    color: #595959;
    line-height: 1.6;
  }
}

.materials-section {
  margin-top: 30rpx;
  padding-top: 30rpx;
  border-top: 1rpx solid #F0F0F0;
  
  .materials-title {
    display: block;
    font-size: 24rpx;
    color: #8C8C8C;
    margin-bottom: 20rpx;
  }
  
  .materials-list {
    display: flex;
    gap: 20rpx;
    flex-wrap: wrap;
    
    .material-item {
      display: flex;
      flex-direction: column;
      align-items: center;
      
      .material-preview {
        width: 120rpx;
        height: 120rpx;
        border-radius: 16rpx;
        overflow: hidden;
        background: #F8F9FA;
        border: 1rpx solid #F0F0F0;
        margin-bottom: 8rpx;
        
        .material-image {
          width: 100%;
          height: 100%;
          object-fit: cover;
        }
        
        .material-doc {
          display: flex;
          align-items: center;
          justify-content: center;
          height: 100%;
        }
      }
      
      .material-name {
        font-size: 20rpx;
        color: #8C8C8C;
        text-align: center;
        max-width: 120rpx;
        overflow: hidden;
        text-overflow: ellipsis;
        white-space: nowrap;
      }
    }
  }
}

.lawyer-card {
  display: flex;
  align-items: center;
  padding: 30rpx;
  background: #F8F9FA;
  border-radius: 20rpx;
  border: 1rpx solid #F0F0F0;
  
  .lawyer-avatar {
    width: 80rpx;
    height: 80rpx;
    border-radius: 50%;
    margin-right: 20rpx;
  }
  
  .lawyer-info {
    flex: 1;
    
    .lawyer-name {
      display: block;
      font-size: 28rpx;
      font-weight: 600;
      color: #262626;
      margin-bottom: 8rpx;
    }
    
    .lawyer-title {
      display: block;
      font-size: 22rpx;
      color: #8C8C8C;
      margin-bottom: 12rpx;
    }
    
    .lawyer-specialties {
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
  
  .lawyer-contact {
    .contact-btn {
      padding: 12rpx 24rpx;
      background: #1890FF;
      color: #FFFFFF;
      border-radius: 16rpx;
      font-size: 22rpx;
      border: none;
      
      &:active {
        background: #096DD9;
      }
    }
  }
}

.reply-list {
  .reply-item {
    padding: 30rpx 0;
    border-bottom: 1rpx solid #F0F0F0;
    
    &:last-child {
      border-bottom: none;
    }
    
    .reply-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 20rpx;
      
      .replier-info {
        display: flex;
        align-items: center;
        
        .replier-avatar {
          width: 50rpx;
          height: 50rpx;
          border-radius: 50%;
          margin-right: 16rpx;
        }
        
        .replier-detail {
          .replier-name {
            display: block;
            font-size: 24rpx;
            font-weight: 600;
            color: #262626;
            margin-bottom: 4rpx;
          }
          
          .reply-time {
            font-size: 20rpx;
            color: #8C8C8C;
          }
        }
      }
      
      .reply-type {
        padding: 4rpx 12rpx;
        border-radius: 12rpx;
        font-size: 20rpx;
        
        &.type-initial {
          background: #E6F7FF;
          color: #1890FF;
        }
        
        &.type-follow-up {
          background: #F6FFED;
          color: #52C41A;
        }
        
        &.type-final {
          background: #FFF2E8;
          color: #FA8C16;
        }
      }
    }
    
    .reply-content {
      margin-bottom: 20rpx;
      
      text {
        font-size: 26rpx;
        color: #262626;
        line-height: 1.6;
        white-space: pre-line;
      }
    }
    
    .reply-attachments {
      margin-bottom: 20rpx;
      
      .attachment-item {
        display: flex;
        align-items: center;
        padding: 12rpx 16rpx;
        background: #F0F8FF;
        border-radius: 12rpx;
        margin-bottom: 8rpx;
        
        &:last-child {
          margin-bottom: 0;
        }
        
        text {
          margin-left: 8rpx;
          font-size: 22rpx;
          color: #1890FF;
        }
      }
    }
    
    .reply-actions {
      display: flex;
      gap: 32rpx;
      
      .action-item {
        display: flex;
        align-items: center;
        
        text {
          margin-left: 8rpx;
          font-size: 22rpx;
          color: #8C8C8C;
        }
        
        &:active {
          opacity: 0.7;
        }
      }
    }
  }
}

.no-reply {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 80rpx 40rpx;
  
  .no-reply-text {
    margin: 20rpx 0 8rpx;
    font-size: 26rpx;
    color: #8C8C8C;
  }
  
  .no-reply-tip {
    font-size: 22rpx;
    color: #D9D9D9;
  }
}

.follow-up-input {
  .follow-up-textarea {
    width: 100%;
    min-height: 120rpx;
    padding: 20rpx;
    background: #F8F9FA;
    border: 1rpx solid #F0F0F0;
    border-radius: 16rpx;
    font-size: 26rpx;
    color: #262626;
    line-height: 1.6;
    margin-bottom: 16rpx;
  }
  
  .follow-up-actions {
    display: flex;
    justify-content: space-between;
    align-items: center;
    
    .char-count {
      font-size: 22rpx;
      color: #8C8C8C;
    }
    
    .send-btn {
      padding: 12rpx 32rpx;
      background: #1890FF;
      color: #FFFFFF;
      border-radius: 16rpx;
      font-size: 24rpx;
      border: none;
      
      &:disabled {
        background: #D9D9D9;
      }
      
      &:not(:disabled):active {
        background: #096DD9;
      }
    }
  }
}

.rating-content {
  text-align: center;
  
  .rating-label {
    display: block;
    font-size: 26rpx;
    color: #262626;
    margin-bottom: 30rpx;
  }
  
  .rating-stars {
    display: flex;
    justify-content: center;
    gap: 16rpx;
    margin-bottom: 30rpx;
    
    .star-item {
      padding: 8rpx;
    }
  }
  
  .rating-comment {
    width: 100%;
    min-height: 100rpx;
    padding: 20rpx;
    background: #F8F9FA;
    border: 1rpx solid #F0F0F0;
    border-radius: 16rpx;
    font-size: 24rpx;
    color: #262626;
    margin-bottom: 30rpx;
  }
  
  .rating-submit {
    width: 200rpx;
    height: 64rpx;
    background: #1890FF;
    color: #FFFFFF;
    border-radius: 16rpx;
    font-size: 24rpx;
    border: none;
    
    &:active {
      background: #096DD9;
    }
  }
}

.action-buttons {
  display: flex;
  gap: 20rpx;
  padding: 0 20rpx;
  
  .action-btn {
    flex: 1;
    height: 80rpx;
    border-radius: 20rpx;
    font-size: 26rpx;
    font-weight: 500;
    border: none;
    
    &.secondary {
      background: #F8F9FA;
      color: #8C8C8C;
      border: 1rpx solid #F0F0F0;
    }
    
    &.primary {
      background: #1890FF;
      color: #FFFFFF;
    }
    
    &:active {
      opacity: 0.8;
    }
  }
}
</style>
 
 