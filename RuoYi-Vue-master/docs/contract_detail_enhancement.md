# 合同详情弹窗功能增强

## 功能概述

为合同管理页面的详情弹窗添加了以下功能：
1. **基本信息展示**：使用 Element UI 的 Descriptions 组件展示合同基本信息
2. **状态流转时间线**：使用 Timeline 组件展示合同从创建到各个阶段的流转记录
3. **文件列表管理**：展示所有已上传的文件，支持预览和下载

## 主要修改

### 1. 模板修改 (Template)

#### 新增合同详情对话框
- 使用 Tab 页签组织内容，包含三个标签页：
  - **基本信息**：展示合同的详细信息
  - **状态流转**：时间线展示合同流转过程
  - **文件列表**：展示所有相关文件

```vue
<el-dialog title="合同详情" :visible.sync="detailDialogOpen" width="900px">
  <el-tabs v-model="detailActiveTab">
    <el-tab-pane label="基本信息" name="basic">...</el-tab-pane>
    <el-tab-pane label="状态流转" name="timeline">...</el-tab-pane>
    <el-tab-pane label="文件列表" name="files">...</el-tab-pane>
  </el-tabs>
</el-dialog>
```

### 2. 数据结构 (Data)

新增以下数据变量：

```javascript
// 详情对话框
detailDialogOpen: false,      // 详情对话框开关
detailActiveTab: "basic",     // 当前激活的标签页
contractDetail: {},           // 合同详情数据
contractFiles: [],            // 文件列表
```

### 3. 方法修改 (Methods)

#### handleViewContract - 查看合同详情
- 获取合同详细信息
- 构建文件列表数组
- 打开详情对话框

支持的文件类型：
- 查验清单 (entryChecklistUrl)
- 物业查验清单 (propertyEntryChecklistUrl)
- 物业月履行清单 (propertyMonthlyChecklistUrl)
- 整改通知单 (rectificationNoticeUrl)
- 整改结果评定通知书 (rectificationResultUrl)
- 年度履行报告 (annualReportUrl)

#### handlePreviewFile - 文件预览
支持的预览类型：
- **图片文件** (jpg, jpeg, png, gif, bmp, webp)：在弹窗中直接显示
- **PDF 文件**：在新窗口中打开
- **其他文件**：提示下载后查看

#### handleDownloadFile - 文件下载
在新窗口中打开文件 URL，浏览器会自动处理下载

### 4. 样式优化 (Style)

添加了以下样式：
- 时间线卡片样式优化
- 图片预览对话框样式
- 确保内容居中和美观

## 使用说明

### 查看合同详情
1. 在合同列表中点击"详情"按钮
2. 弹出详情对话框，默认显示"基本信息"标签页

### 查看状态流转
1. 在详情对话框中切换到"状态流转"标签页
2. 时间线展示合同的各个流转节点和时间

### 查看和下载文件
1. 在详情对话框中切换到"文件列表"标签页
2. 点击"预览"按钮查看文件（支持图片和 PDF）
3. 点击"下载"按钮下载文件

## 技术要点

### Element UI 组件使用
- **el-descriptions**：展示描述列表
- **el-timeline**：展示时间线
- **el-tabs**：标签页切换
- **dict-tag**：字典标签显示

### 文件预览逻辑
```javascript
// 根据文件扩展名判断预览方式
const fileExt = url.substring(url.lastIndexOf('.') + 1).toLowerCase();
if (imageExts.includes(fileExt)) {
  // 图片预览
} else if (pdfExts.includes(fileExt)) {
  // PDF 新窗口打开
} else {
  // 其他文件下载
}
```

### 文件列表构建
遍历合同对象的所有文件 URL 字段，构建统一的文件列表数组：
```javascript
contractFiles: [
  { fileName: "查验清单", fileType: "查验清单", fileUrl: "..." },
  { fileName: "物业查验清单", fileType: "查验清单", fileUrl: "..." },
  ...
]
```

## 注意事项

1. **文件 URL 格式**：确保后端返回的文件 URL 是完整的可访问路径
2. **时间线数据**：目前时间线中部分节点使用 `updateTime`，后续可优化为每个节点独立的时间字段
3. **文件预览限制**：
   - 图片预览依赖浏览器支持
   - PDF 预览需要浏览器内置 PDF 阅读器
   - 其他文件类型需要下载后查看
4. **跨域问题**：如果文件存储在其他域名，需要配置 CORS

## 后续优化建议

1. **时间线优化**：
   - 为每个流转节点添加独立的时间字段
   - 添加操作人信息
   - 支持查看每个节点的详细信息

2. **文件管理优化**：
   - 支持在线编辑（如 Office 文件）
   - 添加文件版本管理
   - 支持批量下载

3. **权限控制**：
   - 根据用户角色控制文件的查看和下载权限
   - 添加操作日志记录

4. **用户体验优化**：
   - 添加文件上传进度显示
   - 支持拖拽上传
   - 添加文件缩略图
