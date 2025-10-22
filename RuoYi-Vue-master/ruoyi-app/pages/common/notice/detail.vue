<template>
  <view class="app-container">
    <view class="notice-detail">
      <view class="title">{{ notice.noticeTitle || '公告详情' }}</view>
      <view class="meta-info">
        <text class="author">发布人：{{ notice.createBy || '系统公告' }}</text>
        <text class="time">时间：{{ formattedCreateTime || '--' }}</text>
      </view>
      <view class="content">
        <rich-text :nodes="notice.noticeContent || '<p>暂无公告内容</p>'"></rich-text>
      </view>
    </view>
  </view>
</template>

<script>
import { getNotice } from '@/api/notice.js'

export default {
  data() {
    return {
      noticeId: '',
      notice: {},
      loading: false,
      openerEventChannel: null,
      eventChannelHandler: null,
      eventChannelBound: false,
      missingNoticeTimer: null,
      missingNoticeHandled: false
    }
  },
  computed: {
    formattedCreateTime() {
      if (!this.notice || !this.notice.createTime) {
        return ''
      }
      return this.parseTime(this.notice.createTime, '{y}-{m}-{d}')
    }
  },
  onLoad(options = {}) {
    this.bindEventChannel()
    console.log(options)
    const noticeId = this.resolveNoticeId(options)
    if (noticeId) {
      this.noticeId = noticeId
      this.fetchDetail()
    } else {
      this.deferMissingNoticeWarning()
    }
  },
  onUnload() {
    this.clearMissingNoticeTimer()
    if (this.openerEventChannel && this.eventChannelHandler && typeof this.openerEventChannel.off === 'function') {
      this.openerEventChannel.off('noticeDetailParams', this.eventChannelHandler)
    }
    this.openerEventChannel = null
    this.eventChannelHandler = null
    this.eventChannelBound = false
  },
  methods: {
    resolveNoticeId(options = {}) {
      const candidates = [
        options.id,
        options.noticeId,
        options.noticeID,
        options.noticeid
      ]
      const target = candidates.find(
        value => value !== undefined && value !== null && value !== ''
      )
      if (target === undefined) {
        return ''
      }
      const trimmed = String(target).trim()
      if (!trimmed) {
        return ''
      }
      const lower = trimmed.toLowerCase()
      if (lower === 'undefined' || lower === 'null') {
        return ''
      }
      return trimmed
    },

    handleMissingNotice() {
      if (this.noticeId || this.missingNoticeHandled) {
        return
      }
      this.missingNoticeHandled = true
      uni.showToast({
        title: '缺少公告编号',
        icon: 'none'
      })
      setTimeout(() => {
        const pages = getCurrentPages()
        if (pages.length > 1) {
          uni.navigateBack({ delta: 1 })
        } else {
          uni.switchTab({ url: '/pages/index' })
        }
      }, 1500)
    },

    fetchDetail() {
      if (!this.noticeId || this.loading) {
        return
      }
      this.loading = true
      getNotice(this.noticeId)
        .then(response => {
          const data = response?.data
          if (!data || Object.keys(data).length === 0) {
            throw new Error('empty notice')
          }
          this.notice = data
        })
        .catch(error => {
          console.error('获取公告详情失败', error)
          uni.showToast({
            title: '公告不存在或已删除',
            icon: 'none'
          })
          this.notice = {
            noticeTitle: '公告不存在',
            createBy: '',
            createTime: '',
            noticeContent: '<p>该公告可能已被删除或不存在。</p>'
          }
        })
        .finally(() => {
          this.loading = false
        })
    },

    bindEventChannel() {
      if (this.eventChannelBound || !this.getOpenerEventChannel) {
        return
      }
      const channel = this.getOpenerEventChannel()
      if (channel && typeof channel.on === 'function') {
        this.openerEventChannel = channel
        const handler = (payload = {}) => {
          const resolvedId = this.resolveNoticeId(payload)
          if (resolvedId) {
            if (!this.noticeId || this.noticeId !== resolvedId) {
              this.noticeId = resolvedId
              this.fetchDetail()
            }
            this.clearMissingNoticeTimer()
          } else {
            this.deferMissingNoticeWarning()
          }
        }
        channel.on('noticeDetailParams', handler)
        this.eventChannelHandler = handler
        this.eventChannelBound = true
      }
    },

    deferMissingNoticeWarning() {
      if (this.noticeId || this.missingNoticeHandled) {
        return
      }
      this.clearMissingNoticeTimer()
      this.missingNoticeTimer = setTimeout(() => {
        if (!this.noticeId && !this.missingNoticeHandled) {
          this.handleMissingNotice()
        }
      }, 200)
    },

    clearMissingNoticeTimer() {
      if (this.missingNoticeTimer) {
        clearTimeout(this.missingNoticeTimer)
        this.missingNoticeTimer = null
      }
    }
  }
}
</script>

<style lang="scss" scoped>
.notice-detail {
  padding: 32rpx;
  background-color: #ffffff;

  .title {
    font-size: 34rpx;
    font-weight: 600;
    text-align: center;
    margin-bottom: 24rpx;
    color: #1f1f1f;
    line-height: 1.4;
  }

  .meta-info {
    font-size: 24rpx;
    color: #8c8c8c;
    text-align: center;
    margin-bottom: 40rpx;
    display: flex;
    justify-content: center;
    gap: 24rpx;

    .author,
    .time {
      display: inline-flex;
      align-items: center;
    }
  }

  .content {
    font-size: 30rpx;
    line-height: 1.8;
    color: #333333;
    word-break: break-word;
  }
}
</style>
