<template>
  <div class="app-container vote-results-container">
    <!-- 加载状态 -->
    <el-loading :active.sync="loading" text="加载中..."></el-loading>

    <!-- 顶部标题和操作按钮 -->
    <el-card class="header-card" shadow="hover">
      <div class="header-content">
        <div class="title-section">
          <h2 class="page-title">{{ meetingTitle || '表决结果' }}</h2>
          <span class="meeting-id" v-if="meetingId">会议ID: {{ meetingId }}</span>
        </div>
        <div class="action-buttons">
          <el-button type="primary" size="small" @click="handleRefresh" icon="el-icon-refresh">刷新</el-button>
          <el-button type="success" size="small" @click="showExportDialog = true" icon="el-icon-download">导出</el-button>
        </div>
      </div>
    </el-card>

    <!-- 导出选择对话框 -->
    <el-dialog
      title="选择导出方式"
      :visible.sync="showExportDialog"
      width="500px"
      :close-on-click-modal="false"
    >
      <div class="export-options">
        <el-card class="export-option-card" shadow="hover" @click.native="handleExportExcel">
          <div class="option-content">
            <i class="el-icon-document"></i>
            <div class="option-info">
              <h4>导出投票列表 (Excel)</h4>
              <p>导出小区所有业主的投票明细列表，包含房号、姓名、投票选项等信息</p>
            </div>
          </div>
        </el-card>

        <el-card class="export-option-card" shadow="hover" @click.native="handleExportPdf">
          <div class="option-content">
            <i class="el-icon-tickets"></i>
            <div class="option-info">
              <h4>导出投票统计报告 (PDF)</h4>
              <p>导出专业的会议表决统计报告，包含参与率、各议题同意/反对/弃权统计等</p>
            </div>
          </div>
        </el-card>
      </div>
      <div slot="footer" class="dialog-footer">
        <el-button @click="showExportDialog = false">取消</el-button>
      </div>
    </el-dialog>

    <!-- 议题标签页 -->
    <el-card class="tabs-card" shadow="hover" v-if="voteResults.length > 0">
      <el-tabs v-model="activeTopicId" type="card" @tab-click="onTabChange">
        <el-tab-pane
          v-for="result in voteResults"
          :key="result.topicId"
          :label="result.topicTitle || '议题'"
          :name="String(result.topicId)"
        >
        </el-tab-pane>
      </el-tabs>
    </el-card>

    <!-- 当前议题的表决结果 -->
    <div v-if="currentResult" class="vote-result-content">
      <!-- 表决结果状态 -->
      <el-card class="result-status-card" shadow="hover">
        <div class="status-header">
          <h3>表决结果</h3>
          <div class="status-badge" :class="currentResult.isPassed ? 'passed' : 'rejected'">
            {{ currentResult.isPassed ? '通过' : '未通过' }}
          </div>
        </div>
      </el-card>

      <!-- 统计概览 -->
      <el-card class="statistics-card" shadow="hover">
        <h3 class="card-title">统计概览</h3>
        <div class="statistics-grid">
          <!-- 总人数和总面积 -->
          <div class="stat-item">
            <div class="stat-label">总人数</div>
            <div class="stat-value">{{ currentResult.totalPeople || 0 }}</div>
          </div>
          <div class="stat-item">
            <div class="stat-label">总面积(㎡)</div>
            <div class="stat-value">{{ formatArea(currentResult.totalArea) }}</div>
          </div>
          <div class="stat-item">
            <div class="stat-label">参与人数</div>
            <div class="stat-value">{{ currentResult.participatePeople || 0 }}</div>
          </div>
          <div class="stat-item">
            <div class="stat-label">参与面积(㎡)</div>
            <div class="stat-value">{{ formatArea(currentResult.participateArea) }}</div>
          </div>
          <div class="stat-item">
            <div class="stat-label">参与率</div>
            <div class="stat-value">{{ getRate(currentResult.participatePeople, currentResult.totalPeople) }}%</div>
          </div>
        </div>
      </el-card>

      <!-- 表决结果卡片 -->
      <div class="vote-cards-grid">
        <!-- 同意 -->
        <el-card class="vote-card agree-card" shadow="hover">
          <div class="card-header agree-header">
            <i class="el-icon-success"></i>
            <span>同意</span>
          </div>
          <div class="vote-stats">
            <div class="stat">
              <div class="stat-title">人数</div>
              <div class="stat-number">{{ currentResult.agreePeople || 0 }}</div>
            </div>
            <div class="stat">
              <div class="stat-title">面积(㎡)</div>
              <div class="stat-number">{{ formatArea(currentResult.agreeArea) }}</div>
            </div>
            <div class="stat">
              <div class="stat-title">占比</div>
              <div class="stat-number">{{ getRate(currentResult.agreePeople, currentResult.participatePeople) }}%</div>
            </div>
          </div>
        </el-card>

        <!-- 反对 -->
        <el-card class="vote-card disagree-card" shadow="hover">
          <div class="card-header disagree-header">
            <i class="el-icon-close"></i>
            <span>反对</span>
          </div>
          <div class="vote-stats">
            <div class="stat">
              <div class="stat-title">人数</div>
              <div class="stat-number">{{ currentResult.disagreePeople || 0 }}</div>
            </div>
            <div class="stat">
              <div class="stat-title">面积(㎡)</div>
              <div class="stat-number">{{ formatArea(currentResult.disagreeArea) }}</div>
            </div>
            <div class="stat">
              <div class="stat-title">占比</div>
              <div class="stat-number">{{ getRate(currentResult.disagreePeople, currentResult.participatePeople) }}%</div>
            </div>
          </div>
        </el-card>

        <!-- 弃权 -->
        <el-card class="vote-card abstain-card" shadow="hover">
          <div class="card-header abstain-header">
            <i class="el-icon-minus"></i>
            <span>弃权</span>
          </div>
          <div class="vote-stats">
            <div class="stat">
              <div class="stat-title">人数</div>
              <div class="stat-number">{{ currentResult.abstainPeople || 0 }}</div>
            </div>
            <div class="stat">
              <div class="stat-title">面积(㎡)</div>
              <div class="stat-number">{{ formatArea(currentResult.abstainArea) }}</div>
            </div>
            <div class="stat">
              <div class="stat-title">占比</div>
              <div class="stat-number">{{ getRate(currentResult.abstainPeople, currentResult.participatePeople) }}%</div>
            </div>
          </div>
        </el-card>

        <!-- 未投票 -->
        <el-card class="vote-card unvoted-card" shadow="hover">
          <div class="card-header unvoted-header">
            <i class="el-icon-document"></i>
            <span>未投票</span>
          </div>
          <div class="vote-stats">
            <div class="stat">
              <div class="stat-title">人数</div>
              <div class="stat-number">{{ currentResult.unvotedPeople || 0 }}</div>
            </div>
            <div class="stat">
              <div class="stat-title">面积(㎡)</div>
              <div class="stat-number">{{ formatArea(currentResult.unvotedArea) }}</div>
            </div>
            <div class="stat">
              <div class="stat-title">占比</div>
              <div class="stat-number">{{ getRate(currentResult.unvotedPeople, currentResult.totalPeople) }}%</div>
            </div>
          </div>
        </el-card>
      </div>
    </div>

    <!-- 空状态提示 -->
    <el-empty v-else description="暂无表决结果数据"></el-empty>
  </div>
</template>

<script>
import { listVoteResults, exportVoteListExcel, exportVoteReportPdf } from '@/api/system/voteResults'
import { mapGetters } from 'vuex'

export default {
  name: 'VoteResults',
  computed: {
    ...mapGetters(['currentCommunityId']),
    currentResult() {
      if (!this.activeTopicId || this.voteResults.length === 0) {
        return null
      }
      return this.voteResults.find(item => String(item.topicId) === String(this.activeTopicId))
    }
  },
  data() {
    return {
      loading: false,
      meetingId: null,
      meetingTitle: null,
      activeTopicId: null,
      voteResults: [],
      showExportDialog: false
    }
  },
  created() {
    // 从路由查询参数获取会议信息
    this.meetingId = this.$route.query.meetingId
    this.meetingTitle = this.$route.query.meetingTitle

    if (this.meetingId) {
      this.loadVoteResults()
    }
  },
  methods: {
    /**
     * 加载表决结果数据
     */
    loadVoteResults() {
      this.loading = true
      listVoteResults({
        meetingId: this.meetingId,
        communityId: this.currentCommunityId
      })
        .then(response => {
          if (response && response.rows) {
            this.voteResults = response.rows
            // 设置第一个议题为默认选中
            if (this.voteResults.length > 0) {
              this.activeTopicId = String(this.voteResults[0].topicId)
            }
          } else {
            this.$message.warning('未获取到表决结果数据')
          }
        })
        .catch(error => {
          this.$message.error('加载表决结果失败')
          console.error(error)
        })
        .finally(() => {
          this.loading = false
        })
    },

    /**
     * 处理标签页切换
     */
    onTabChange() {
      // 标签页切换时自动更新currentResult
    },

    /**
     * 刷新数据
     */
    handleRefresh() {
      this.loadVoteResults()
      this.$message.success('数据已刷新')
    },

    /**
     * 导出Excel投票列表
     */
    handleExportExcel() {
      if (!this.meetingId || !this.currentCommunityId) {
        this.$message.warning('缺少必要的会议或小区信息')
        return
      }

      this.$confirm('确认导出投票列表为Excel文件吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'info'
      }).then(() => {
        this.showExportDialog = false
        const loading = this.$loading({
          lock: true,
          text: '正在生成Excel文件，请稍候...',
          spinner: 'el-icon-loading',
          background: 'rgba(0, 0, 0, 0.7)'
        })

        exportVoteListExcel({
          meetingId: this.meetingId,
          communityId: this.currentCommunityId
        }).then(response => {
          this.$message.success('导出成功')
        }).catch(error => {
          console.error('导出失败', error)
          this.$message.error('导出失败: ' + (error.message || '未知错误'))
        }).finally(() => {
          loading.close()
        })
      }).catch(() => {
        // 用户取消
      })
    },

    /**
     * 导出PDF投票统计报告
     */
    handleExportPdf() {
      if (!this.meetingId || !this.currentCommunityId) {
        this.$message.warning('缺少必要的会议或小区信息')
        return
      }

      this.$confirm('确认导出投票统计报告为PDF文件吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'info'
      }).then(() => {
        this.showExportDialog = false
        const loading = this.$loading({
          lock: true,
          text: '正在生成PDF报告，请稍候...',
          spinner: 'el-icon-loading',
          background: 'rgba(0, 0, 0, 0.7)'
        })

        exportVoteReportPdf({
          meetingId: this.meetingId,
          communityId: this.currentCommunityId
        }).then(response => {
          this.$message.success('导出成功')
        }).catch(error => {
          console.error('导出失败', error)
          this.$message.error('导出失败: ' + (error.message || '未知错误'))
        }).finally(() => {
          loading.close()
        })
      }).catch(() => {
        // 用户取消
      })
    },

    /**
     * 格式化面积，保留两位小数
     * @param {number} area - 面积值
     * @return {string} 格式化后的面积
     */
    formatArea(area) {
      if (!area) return '0.00'
      return parseFloat(area).toFixed(2)
    },

    /**
     * 计算百分比，保留两位小数
     * @param {number} part - 部分值
     * @param {number} total - 总值
     * @return {string} 百分比值（不含%符号）
     */
    getRate(part, total) {
      if (!part || !total || total === 0) return '0.00'
      return ((part / total) * 100).toFixed(2)
    }
  }
}
</script>

<style scoped lang="scss">
.vote-results-container {
  padding: 20px;
  background: #f5f7fa;
  min-height: 100vh;

  .header-card {
    margin-bottom: 20px;

    ::v-deep .el-card__body {
      padding: 20px;
    }

    .header-content {
      display: flex;
      justify-content: space-between;
      align-items: center;

      .title-section {
        display: flex;
        flex-direction: column;
        gap: 8px;

        .page-title {
          margin: 0;
          font-size: 24px;
          color: #303133;
          font-weight: 600;
        }

        .meeting-id {
          font-size: 12px;
          color: #909399;
        }
      }

      .action-buttons {
        display: flex;
        gap: 10px;
      }
    }
  }

  .tabs-card {
    margin-bottom: 20px;

    ::v-deep .el-card__body {
      padding: 0;
    }

    ::v-deep .el-tabs {
      .el-tabs__header {
        margin-bottom: 0;
        background: transparent;
      }

      .el-tabs__nav-wrap {
        padding: 20px 20px 0 20px;
      }
    }
  }

  .vote-result-content {
    .result-status-card {
      margin-bottom: 20px;

      ::v-deep .el-card__body {
        padding: 20px;
      }

      .status-header {
        display: flex;
        justify-content: space-between;
        align-items: center;

        h3 {
          margin: 0;
          font-size: 18px;
          color: #303133;
        }

        .status-badge {
          padding: 8px 16px;
          border-radius: 4px;
          font-weight: 600;
          font-size: 14px;

          &.passed {
            background: #f0f9ff;
            color: #67c23a;
            border: 1px solid #c6e2ff;
          }

          &.rejected {
            background: #fef0f0;
            color: #f56c6c;
            border: 1px solid #fde2e2;
          }
        }
      }
    }

    .statistics-card {
      margin-bottom: 20px;

      ::v-deep .el-card__body {
        padding: 20px;
      }

      .card-title {
        margin: 0 0 20px 0;
        font-size: 16px;
        color: #303133;
        font-weight: 600;
      }

      .statistics-grid {
        display: grid;
        grid-template-columns: repeat(auto-fit, minmax(150px, 1fr));
        gap: 15px;

        .stat-item {
          background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
          color: #fff;
          padding: 15px;
          border-radius: 6px;
          text-align: center;

          .stat-label {
            font-size: 12px;
            opacity: 0.9;
            margin-bottom: 8px;
          }

          .stat-value {
            font-size: 24px;
            font-weight: 600;
          }

          &:nth-child(2) {
            background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
          }

          &:nth-child(3) {
            background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
          }

          &:nth-child(4) {
            background: linear-gradient(135deg, #43e97b 0%, #38f9d7 100%);
          }

          &:nth-child(5) {
            background: linear-gradient(135deg, #fa709a 0%, #fee140 100%);
          }
        }
      }
    }

    .vote-cards-grid {
      display: grid;
      grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
      gap: 20px;

      .vote-card {
        ::v-deep .el-card__body {
          padding: 20px;
        }

        .card-header {
          display: flex;
          align-items: center;
          gap: 10px;
          font-size: 16px;
          font-weight: 600;
          margin-bottom: 20px;
          padding-bottom: 15px;
          border-bottom: 2px solid #f0f0f0;

          i {
            font-size: 20px;
          }
        }

        .vote-stats {
          display: flex;
          flex-direction: column;
          gap: 15px;

          .stat {
            text-align: center;

            .stat-title {
              font-size: 12px;
              color: #909399;
              margin-bottom: 5px;
            }

            .stat-number {
              font-size: 22px;
              font-weight: 600;
              color: #303133;
            }
          }
        }

        &.agree-card {
          .card-header {
            color: #67c23a;
            border-bottom-color: #c6e2ff;

            i {
              color: #67c23a;
            }
          }

          .stat-number {
            color: #67c23a;
          }
        }

        &.disagree-card {
          .card-header {
            color: #f56c6c;
            border-bottom-color: #fde2e2;

            i {
              color: #f56c6c;
            }
          }

          .stat-number {
            color: #f56c6c;
          }
        }

        &.abstain-card {
          .card-header {
            color: #e6a23c;
            border-bottom-color: #f5dab1;

            i {
              color: #e6a23c;
            }
          }

          .stat-number {
            color: #e6a23c;
          }
        }

        &.unvoted-card {
          .card-header {
            color: #909399;
            border-bottom-color: #e4e7eb;

            i {
              color: #909399;
            }
          }

          .stat-number {
            color: #909399;
          }
        }
      }
    }
  }
}

::v-deep .el-card {
  border-radius: 6px;
  border: 1px solid #ebeef5;

  .el-card__body {
    padding: 20px;
  }
}

::v-deep .el-empty {
  padding: 60px 20px;
}

::v-deep .el-loading-mask {
  z-index: 2000;
}

.export-options {
  display: flex;
  flex-direction: column;
  gap: 15px;

  .export-option-card {
    cursor: pointer;
    transition: all 0.3s ease;
    border: 2px solid transparent;

    &:hover {
      border-color: #409eff;
      transform: translateY(-2px);
      box-shadow: 0 4px 12px rgba(64, 158, 255, 0.2);
    }

    ::v-deep .el-card__body {
      padding: 20px;
    }

    .option-content {
      display: flex;
      align-items: center;
      gap: 20px;

      i {
        font-size: 48px;
        color: #409eff;
      }

      .option-info {
        flex: 1;

        h4 {
          margin: 0 0 8px 0;
          font-size: 16px;
          font-weight: 600;
          color: #303133;
        }

        p {
          margin: 0;
          font-size: 13px;
          color: #909399;
          line-height: 1.6;
        }
      }
    }
  }
}
</style>
