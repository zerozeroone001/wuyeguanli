# 业主大会完整导出功能 - 前端更新说明

## 已完成的后端工作

1. 创建了导出Service接口和实现类
   - IMeetingExportService.java
   - MeetingExportServiceImpl.java

2. 创建了PDF生成工具类
   - PdfUtils.java (使用iText 5.5.13.3)

3. 添加了Mapper查询方法
   - SysMeetingVoteMapper.selectVoteListForExport()
   - 对应的XML SQL查询已添加

4. 创建了导出Controller
   - MeetingExportController.java
   - GET /system/meeting/export/voteList - 导出Excel
   - GET /system/meeting/export/voteReport - 导出PDF

5. 更新了前端API
   - voteResults.js 已添加 exportVoteListExcel 和 exportVoteReportPdf 方法

## 需要手动完成的前端更新

请手动更新文件: `ruoyi-ui/src/views/system/voteResults/index.vue`

### 1. 修改导入语句 (第171行)

将:
```javascript
import { listVoteResults } from '@/api/system/voteResults'
```

改为:
```javascript
import { listVoteResults, exportVoteListExcel, exportVoteReportPdf } from '@/api/system/voteResults'
```

### 2. 修改导出按钮部分 (第13-15行)

将:
```vue
<el-button type="primary" size="small" @click="handleRefresh" icon="el-icon-refresh">刷新</el-button>
<el-button type="success" size="small" @click="handleExport" icon="el-icon-download">导出</el-button>
```

改为:
```vue
<el-button type="primary" size="small" @click="handleRefresh" icon="el-icon-refresh">刷新</el-button>
<el-dropdown @command="handleExportCommand" trigger="click">
  <el-button type="success" size="small">
    导出<i class="el-icon-arrow-down el-icon--right"></i>
  </el-button>
  <el-dropdown-menu slot="dropdown">
    <el-dropdown-item command="excel" icon="el-icon-document">导出投票列表(Excel)</el-dropdown-item>
    <el-dropdown-item command="pdf" icon="el-icon-tickets">导出统计报告(PDF)</el-dropdown-item>
  </el-dropdown-menu>
</el-dropdown>
```

### 3. 替换 handleExport 方法 (第248-252行)

将:
```javascript
/**
 * 导出数据
 */
handleExport() {
  this.$message.info('功能开发中...')
},
```

替换为:
```javascript
/**
 * 处理导出命令
 */
handleExportCommand(command) {
  if (!this.meetingId || !this.currentCommunityId) {
    this.$message.warning('缺少必要的参数')
    return
  }

  if (command === 'excel') {
    this.exportToExcel()
  } else if (command === 'pdf') {
    this.exportToPdf()
  }
},

/**
 * 导出投票列表为Excel
 */
exportToExcel() {
  const loading = this.$loading({
    lock: true,
    text: '正在导出Excel，请稍候...',
    spinner: 'el-icon-loading',
    background: 'rgba(0, 0, 0, 0.7)'
  })

  exportVoteListExcel({
    meetingId: this.meetingId,
    communityId: this.currentCommunityId
  })
    .then(response => {
      this.downloadFile(response, this.meetingTitle + '_投票列表.xlsx')
      this.$message.success('Excel导出成功')
    })
    .catch(error => {
      console.error('导出Excel失败', error)
      this.$message.error('导出Excel失败，请重试')
    })
    .finally(() => {
      loading.close()
    })
},

/**
 * 导出统计报告为PDF
 */
exportToPdf() {
  const loading = this.$loading({
    lock: true,
    text: '正在生成PDF报告，请稍候...',
    spinner: 'el-icon-loading',
    background: 'rgba(0, 0, 0, 0.7)'
  })

  exportVoteReportPdf({
    meetingId: this.meetingId,
    communityId: this.currentCommunityId
  })
    .then(response => {
      this.downloadFile(response, this.meetingTitle + '_表决报告.pdf')
      this.$message.success('PDF导出成功')
    })
    .catch(error => {
      console.error('导出PDF失败', error)
      this.$message.error('导出PDF失败，请重试')
    })
    .finally(() => {
      loading.close()
    })
},

/**
 * 下载文件
 */
downloadFile(data, filename) {
  const blob = new Blob([data])
  const url = window.URL.createObjectURL(blob)
  const link = document.createElement('a')
  link.href = url
  link.setAttribute('download', filename)
  document.body.appendChild(link)
  link.click()
  document.body.removeChild(link)
  window.URL.revokeObjectURL(url)
},
```

## 功能说明

### Excel导出功能
- 导出所有业主的投票列表
- 包含业主信息（房号、姓名、手机号、建筑面积）
- 包含投票状态和各议题的投票选项
- 文件名格式: `会议标题_投票列表_时间戳.xlsx`

### PDF导出功能
- 生成专业的PDF报告
- 包含会议基本信息
- 包含各议题的详细统计（人数、面积、占比）
- 包含表决结论（通过/未通过）
- 支持中文字体显示
- 包含页眉页脚和页码
- 文件名格式: `会议标题_表决报告_时间戳.pdf`

## 注意事项

1. 确保后端服务已启动
2. 确保用户有 `system:meeting:export` 权限
3. iText依赖已在 ruoyi-system/pom.xml 中配置
4. PDF生成使用STSong-Light字体，支持中文显示

## 测试建议

1. 测试Excel导出：点击"导出"按钮，选择"导出投票列表(Excel)"
2. 测试PDF导出：点击"导出"按钮，选择"导出统计报告(PDF)"
3. 验证导出文件的内容是否完整正确
4. 测试异常情况（如网络错误、权限不足等）

