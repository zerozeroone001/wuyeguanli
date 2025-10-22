<template>
  <view class="notice-list-page">
    <view
      v-for="notice in noticeList"
      :key="notice.noticeId"
      class="notice-card"
      @click="openDetail(notice.noticeId)"
    >
      <view class="card-header">
        <text class="card-title">{{ notice.noticeTitle }}</text>
        <text class="card-date">{{ notice.formattedCreateTime }}</text>
      </view>
      <view class="card-content">
        <text class="card-summary">{{ notice.summary }}</text>
      </view>
      <view class="card-footer">
        <text class="card-more">查看详情</text>
        <uni-icons type="right" size="16" color="#8C8C8C" />
      </view>
    </view>

    <view v-if="!loading && noticeList.length === 0" class="empty-state">
      <uni-icons type="sound" size="48" color="#C5D9FF" />
      <text class="empty-text">暂时没有公告</text>
    </view>

    <uni-load-more
      v-if="noticeList.length > 0"
      :status="loadMoreStatus"
      class="load-more"
    />
  </view>
</template>

<script>
import { listNotice } from '@/api/notice.js'

export default {
  data() {
    return {
      noticeList: [],
      queryParams: {
        pageNum: 1,
        pageSize: 10
      },
      total: 0,
      loading: false,
      loadMoreStatus: 'more'
    }
  },
  onLoad() {
    this.fetchNotices(true)
  },
  onPullDownRefresh() {
    this.fetchNotices(true)
  },
  onReachBottom() {
    if (this.loading || this.noticeList.length >= this.total) {
      this.loadMoreStatus = 'noMore'
      return
    }
    this.queryParams.pageNum += 1
    this.fetchNotices()
  },
  methods: {
    fetchNotices(reset = false) {
      if (this.loading) {
        return Promise.resolve()
      }

      if (reset) {
        this.queryParams.pageNum = 1
        this.noticeList = []
      }

      this.loading = true
      this.loadMoreStatus = 'loading'

      return listNotice(this.queryParams)
        .then(res => {
          const rows = Array.isArray(res?.rows) ? res.rows : []
          this.total = Number(res?.total ?? rows.length ?? 0)
          const normalized = rows.map(item => this.normalizeNotice(item))
          this.noticeList = reset ? normalized : this.noticeList.concat(normalized)
          const noMore = this.noticeList.length >= this.total
          this.loadMoreStatus = noMore ? 'noMore' : 'more'
        })
        .catch(error => {
          console.error('获取公告列表失败', error)
          if (reset) {
            this.noticeList = []
          }
          this.loadMoreStatus = 'more'
          uni.showToast({
            title: '公告加载失败',
            icon: 'none'
          })
        })
        .finally(() => {
          this.loading = false
          uni.stopPullDownRefresh()
        })
    },

    normalizeNotice(notice = {}) {
      const formattedCreateTime = notice.createTime
        ? this.parseTime(notice.createTime, '{y}-{m}-{d}')
        : ''
      return {
        ...notice,
        summary: this.getNoticeSummary(
          notice.noticeContent || notice.noticeContentText || notice.remark || ''
        ),
        formattedCreateTime
      }
    },

    getNoticeSummary(content) {
      const text = this.stripHtml(content)
        .replace(/\s+/g, ' ')
        .trim()
      if (!text) {
        return '暂无公告内容'
      }
      return text.length > 80 ? `${text.slice(0, 80)}...` : text
    },

    stripHtml(content = '') {
      return content
        .replace(/<[^>]*>/g, '')
        .replace(/&nbsp;/gi, ' ')
        .replace(/&amp;/gi, '&')
        .replace(/&lt;/gi, '<')
        .replace(/&gt;/gi, '>')
        .replace(/&quot;/gi, '"')
        .replace(/&#39;/gi, "'")
    },

    openDetail(noticeId) {

      uni.navigateTo({
        url: `/pages/common/notice/detail?id=${noticeId}`,
        success: (res) => {
          if (res && res.eventChannel && typeof res.eventChannel.emit === 'function') {
            res.eventChannel.emit('noticeDetailParams', { noticeId: resolvedId })
          }
        }
      })
    },

    normalizeNoticeId(value) {
      if (value === undefined || value === null) {
        return ''
      }
      const stringValue = String(value).trim()
      if (!stringValue) {
        return ''
      }
      const lower = stringValue.toLowerCase()
      if (lower === 'undefined' || lower === 'null') {
        return ''
      }
      return stringValue
    }
  }
}
</script>

<style lang="scss" scoped>
.notice-list-page {
  min-height: 100vh;
  padding: 24rpx 32rpx 48rpx;
  background: #f5f7fb;
  box-sizing: border-box;
}

.notice-card {
  background: #ffffff;
  border-radius: 16rpx;
  padding: 32rpx 28rpx;
  margin-bottom: 24rpx;
  box-shadow: 0 8rpx 24rpx rgba(24, 144, 255, 0.08);
  display: flex;
  flex-direction: column;
  gap: 16rpx;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  gap: 16rpx;
}

.card-title {
  flex: 1;
  font-size: 32rpx;
  font-weight: 600;
  color: #1f1f1f;
  line-height: 1.4;
}

.card-date {
  font-size: 24rpx;
  color: #8c8c8c;
  flex-shrink: 0;
}

.card-content {
  font-size: 28rpx;
  color: #4a4a4a;
  line-height: 1.6;
}

.card-summary {
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.card-footer {
  display: flex;
  align-items: center;
  justify-content: flex-end;
  gap: 8rpx;
  color: #1890ff;
  font-size: 26rpx;
}

.empty-state {
  margin-top: 120rpx;
  display: flex;
  flex-direction: column;
  align-items: center;
  color: #8c8c8c;
  gap: 24rpx;
}

.empty-text {
  font-size: 28rpx;
}

.load-more {
  margin-top: 8rpx;
}
</style>
