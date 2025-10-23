<template>
  <div class="poll-designer">
    <div class="designer-header">
      <el-page-header @back="handleBack" content="问卷表单设计" />
      <div class="header-actions">
        <span class="poll-meta" v-if="pollInfo">
          <i class="el-icon-document"></i>
          <span class="poll-title">{{ pollInfo.title }}</span>
        </span>
        <el-button icon="el-icon-mobile-phone" @click="openPreview('mobile')">手机预览</el-button>
        <el-button icon="el-icon-monitor" @click="openPreview('desktop')">电脑预览</el-button>
        <el-button type="primary" icon="el-icon-upload" :loading="saving" @click="handleSave">保存模板</el-button>
      </div>
    </div>

    <div class="designer-body" v-loading="loading">
      <div class="palette">
        <h3 class="panel-title">组件库</h3>
        <div class="palette-section" v-for="section in componentCatalog" :key="section.name">
          <div class="palette-title">{{ section.name }}</div>
          <div class="palette-items">
            <el-button
              v-for="item in section.items"
              :key="item.key"
              size="mini"
              type="primary"
              plain
              @click="addComponent(item)"
            >{{ item.label }}</el-button>
          </div>
        </div>
      </div>

      <div class="builder-area">
        <h3 class="panel-title">表单画布</h3>
        <draggable v-if="fields.length" v-model="fields" handle=".drag-handle" :animation="200">
          <transition-group name="fade" tag="div">
            <div
              class="field-card"
              :class="{ active: field.id === activeFieldId }"
              v-for="(field, index) in fields"
              :key="field.id"
              @click="selectField(field)"
            >
              <div class="field-card-header">
                <i class="el-icon-rank drag-handle"></i>
                <span class="field-name">{{ field.label }}</span>
                <div class="field-ops">
                  <el-tag size="mini">{{ componentTypeLabel(field.type) }}</el-tag>
                  <el-button type="text" icon="el-icon-delete" @click.stop="removeField(index)" />
                </div>
              </div>
            </div>
          </transition-group>
        </draggable>
        <el-empty v-else description="点击左侧组件添加字段" />
      </div>

      <div class="property-panel">
        <h3 class="panel-title">属性配置</h3>
        <el-scrollbar class="property-scroll">
          <div v-if="activeField">
            <el-form :model="activeField" label-width="90px" size="small">
              <el-form-item label="字段标识">
                <el-input v-model="activeField.field" disabled />
              </el-form-item>
              <el-form-item label="显示名称">
                <el-input v-model="activeField.label" />
              </el-form-item>
              <el-form-item v-if="supportsPlaceholder(activeField.type)" label="占位提示">
                <el-input v-model="activeField.placeholder" />
              </el-form-item>
              <el-form-item label="是否必填">
                <el-switch v-model="activeField.required" />
              </el-form-item>
              <el-form-item v-if="activeField.type === 'select'" label="允许多选">
                <el-switch v-model="activeField.multiple" />
              </el-form-item>
              <div v-if="hasOptions(activeField.type)" class="options-editor">
                <div class="options-header">
                  <span>选项设置</span>
                  <el-button type="text" icon="el-icon-plus" @click="addOption(activeField)">新增选项</el-button>
                </div>
                <div v-for="(option, idx) in activeField.options" :key="option.id" class="option-item">
                  <el-input v-model="option.label" size="mini" />
                  <el-button type="text" icon="el-icon-delete" @click="removeOption(activeField, idx)" />
                </div>
              </div>
            </el-form>
          </div>
          <div v-else>
            <el-form :model="appearanceConfig" label-width="100px" size="small">
              <el-form-item label="显示标题">
                <el-switch v-model="appearanceConfig.showTitle" />
              </el-form-item>
              <el-form-item v-if="appearanceConfig.showTitle" label="标题内容">
                <el-input v-model="appearanceConfig.title" />
              </el-form-item>
              <el-form-item label="显示描述">
                <el-switch v-model="appearanceConfig.showDescription" />
              </el-form-item>
              <el-form-item v-if="appearanceConfig.showDescription" label="描述文本">
                <el-input v-model="appearanceConfig.description" type="textarea" />
              </el-form-item>
              <el-form-item label="显示序号">
                <el-switch v-model="appearanceConfig.showSerialNumber" />
              </el-form-item>
              <el-form-item label="按钮文本">
                <el-input v-model="appearanceConfig.submitButtonText" />
              </el-form-item>
              <el-form-item label="按钮颜色">
                <el-color-picker v-model="appearanceConfig.submitButtonColor" />
              </el-form-item>
            </el-form>
          </div>
        </el-scrollbar>
      </div>
    </div>

    <el-dialog :visible.sync="previewVisible" :title="previewDevice === 'mobile' ? '手机预览' : '电脑预览'" width="480px">
      <div :class="['preview-wrapper', previewDevice === 'mobile' ? 'preview-mobile' : 'preview-desktop']">
        <div class="preview-header" v-if="appearanceConfig.showTitle || appearanceConfig.showDescription">
          <h3 v-if="appearanceConfig.showTitle">{{ appearanceConfig.title || (pollInfo && pollInfo.title) || '在线问卷' }}</h3>
          <p v-if="appearanceConfig.showDescription">{{ appearanceConfig.description || (pollInfo && pollInfo.description) || '请填写以下内容' }}</p>
        </div>
        <div class="preview-body">
          <div v-for="(field, index) in fields" :key="field.id" class="preview-field">
            <label class="preview-label">
              <span v-if="appearanceConfig.showSerialNumber" class="serial">{{ index + 1 }}.</span>
              {{ field.label }}
              <span v-if="field.required" class="required">*</span>
            </label>
            <div class="preview-control">
              <span class="placeholder">{{ previewPlaceholder(field) }}</span>
            </div>
          </div>
        </div>
        <el-button type="primary" class="preview-submit" :style="{ backgroundColor: appearanceConfig.submitButtonColor || '#409EFF' }">
          {{ appearanceConfig.submitButtonText || '提交' }}
        </el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import draggable from 'vuedraggable'
import { getFormTemplate, updateFormTemplate } from '@/api/system/formTemplate'
import { getPoll } from '@/api/system/poll'

const COMPONENT_CATALOG = [
  {
    name: '输入组件',
    items: [
      { key: 'input', type: 'input', label: '单行文本', placeholder: '请输入内容' },
      { key: 'textarea', type: 'textarea', label: '多行文本', placeholder: '请输入内容' },
      { key: 'number', type: 'number', label: '数字输入', placeholder: '请输入数字' }
    ]
  },
  {
    name: '选择组件',
    items: [
      { key: 'select', type: 'select', label: '下拉选择', placeholder: '请选择', options: [
        { label: '选项一', value: '1' },
        { label: '选项二', value: '2' }
      ] },
      { key: 'radio', type: 'radio', label: '单选组', options: [
        { label: '选项一', value: '1' },
        { label: '选项二', value: '2' }
      ] },
      { key: 'checkbox', type: 'checkbox', label: '多选组', options: [
        { label: '选项一', value: '1' },
        { label: '选项二', value: '2' }
      ] }
    ]
  },
  {
    name: '日期时间',
    items: [
      { key: 'date', type: 'date', label: '日期选择', placeholder: '请选择日期' },
      { key: 'daterange', type: 'daterange', label: '日期范围', placeholder: '请选择日期范围' },
      { key: 'time', type: 'time', label: '时间选择', placeholder: '请选择时间' }
    ]
  },
  {
    name: '评分组件',
    items: [
      { key: 'rate', type: 'rate', label: '评分控件', placeholder: '' }
    ]
  }
]

export default {
  name: 'PollDesigner',
  components: { draggable },
  data() {
    return {
      pollId: null,
      formId: null,
      pollInfo: null,
      loading: false,
      saving: false,
      previewVisible: false,
      previewDevice: 'mobile',
      componentCatalog: COMPONENT_CATALOG,
      fields: [],
      activeFieldId: null,
      nextFieldId: 1,
      appearanceConfig: {
        showTitle: true,
        title: '',
        showDescription: true,
        description: '',
        showSerialNumber: false,
        submitButtonText: '提交',
        submitButtonColor: '#1890FF'
      }
    }
  },
  computed: {
    activeField() {
      return this.fields.find(item => item.id === this.activeFieldId) || null
    }
  },
  created() {
    const { pollId, formId } = this.$route.query
    this.pollId = pollId ? Number(pollId) : null
    this.formId = formId ? Number(formId) : null
    this.initialize()
  },
  methods: {
    async initialize() {
      if (this.pollId) {
        try {
          const res = await getPoll(this.pollId)
          this.pollInfo = res?.data || null
          if (this.pollInfo) {
            if (!this.appearanceConfig.title) {
              this.appearanceConfig.title = this.pollInfo.title || ''
            }
            if (!this.appearanceConfig.description) {
              this.appearanceConfig.description = this.pollInfo.description || ''
            }
          }
        } catch (error) {
          this.pollInfo = null
        }
      }
      if (this.formId) {
        this.loading = true
        try {
          const res = await getFormTemplate(this.formId)
          const data = res?.data || res
          if (data) {
            if (data.formSchema) {
              const schema = JSON.parse(data.formSchema)
              if (Array.isArray(schema)) {
                const restored = []
                schema.forEach(item => {
                  const id = item.id || this.nextFieldId++
                  restored.push({
                    id,
                    field: item.field || `field_${id}`,
                    type: item.type,
                    label: item.label,
                    placeholder: item.placeholder || '',
                    required: !!item.required,
                    multiple: !!item.multiple,
                    options: (item.options || []).map((opt, idx) => ({
                      id: opt.id || `${id}_${idx}_${Date.now()}`,
                      label: opt.label,
                      value: opt.value
                    }))
                  })
                  if (id >= this.nextFieldId) {
                    this.nextFieldId = id + 1
                  }
                })
                this.fields = restored
              }
            }
            if (data.appearanceConfig) {
              try {
                const appearance = JSON.parse(data.appearanceConfig)
                this.appearanceConfig = Object.assign(this.appearanceConfig, appearance)
              } catch (e) {}
            }
          }
        } catch (error) {
          this.$message.error('加载表单模板失败，请稍后重试')
        } finally {
          this.loading = false
        }
      }
      if (!this.fields.length) {
        this.addComponent(this.componentCatalog[0].items[0])
      } else {
        this.activeFieldId = this.fields[0].id
      }
    },
    handleBack() {
      this.$router.back()
    },
    addComponent(component) {
      const id = this.nextFieldId++
      const field = {
        id,
        field: `field_${id}`,
        type: component.type,
        label: component.label,
        placeholder: component.placeholder || '',
        required: false,
        multiple: !!component.multiple,
        options: (component.options || []).map((opt, idx) => ({
          id: `${id}_${idx}_${Date.now()}`,
          label: opt.label,
          value: opt.value
        }))
      }
      this.fields.push(field)
      this.selectField(field)
    },
    selectField(field) {
      this.activeFieldId = field.id
    },
    removeField(index) {
      const removed = this.fields.splice(index, 1)
      if (removed.length && removed[0].id === this.activeFieldId) {
        this.activeFieldId = this.fields.length ? this.fields[0].id : null
      }
    },
    componentTypeLabel(type) {
      const map = {
        input: '单行文本',
        textarea: '多行文本',
        number: '数字输入',
        select: '下拉选择',
        radio: '单选组',
        checkbox: '多选组',
        date: '日期选择',
        daterange: '日期范围',
        time: '时间选择',
        rate: '评分控件'
      }
      return map[type] || type
    },
    supportsPlaceholder(type) {
      return ['input', 'textarea', 'number', 'select', 'date', 'daterange', 'time'].includes(type)
    },
    hasOptions(type) {
      return ['select', 'radio', 'checkbox'].includes(type)
    },
    addOption(field) {
      if (!field.options) {
        this.$set(field, 'options', [])
      }
      const idx = field.options.length + 1
      field.options.push({
        id: `${field.id}_${idx}_${Date.now()}`,
        label: `选项${idx}`,
        value: String(idx)
      })
    },
    removeOption(field, idx) {
      field.options.splice(idx, 1)
    },
    previewPlaceholder(field) {
      if (field.placeholder) {
        return field.placeholder
      }
      switch (field.type) {
        case 'input':
        case 'textarea':
          return '请输入内容'
        case 'number':
          return '请输入数字'
        case 'select':
        case 'radio':
        case 'checkbox':
          return '请选择'
        case 'date':
        case 'daterange':
          return '请选择日期'
        case 'time':
          return '请选择时间'
        case 'rate':
          return '请选择评分'
        default:
          return '请填写'
      }
    },
    openPreview(device) {
      if (!this.fields.length) {
        this.$message.warning('请先添加表单字段')
        return
      }
      this.previewDevice = device
      this.previewVisible = true
    },
    buildMobileSchema(fields) {
      return fields.map(field => ({
        id: field.id,
        field: field.field,
        label: field.label,
        type: field.type,
        required: !!field.required,
        placeholder: field.placeholder || '',
        options: field.options || [],
        multiple: !!field.multiple
      }))
    },
    async handleSave() {
      if (!this.formId) {
        this.$message.error('表单模板编号缺失')
        return
      }
      if (!this.fields.length) {
        this.$message.warning('请至少添加一个字段')
        return
      }
      this.saving = true
      try {
        const payload = {
          formId: this.formId,
          formSchema: JSON.stringify(this.fields),
          formConfig: JSON.stringify({ labelWidth: 90 }),
          appearanceConfig: JSON.stringify(this.appearanceConfig),
          mobileSchema: JSON.stringify(this.buildMobileSchema(this.fields))
        }
        await updateFormTemplate(payload)
        this.$message.success('模板保存成功')
      } catch (error) {
        this.$message.error('保存失败，请稍后重试')
      } finally {
        this.saving = false
      }
    }
  }
}
</script>

<style scoped>
.poll-designer {
  display: flex;
  flex-direction: column;
  height: 100%;
  background: #f5f7fa;
}

.designer-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 12px 16px;
  background: #fff;
  border-bottom: 1px solid #ebeef5;
}

.header-actions {
  display: flex;
  align-items: center;
  gap: 12px;
}

.poll-meta {
  display: flex;
  align-items: center;
  color: #606266;
  margin-right: 12px;
}

.poll-title {
  margin-left: 4px;
  font-weight: 500;
}

.designer-body {
  display: grid;
  grid-template-columns: 260px 1fr 320px;
  gap: 16px;
  padding: 16px;
  flex: 1;
  overflow: hidden;
}

.panel-title {
  margin: 0 0 12px;
  font-size: 14px;
  font-weight: 600;
  color: #303133;
}

.palette, .builder-area, .property-panel {
  background: #fff;
  border-radius: 6px;
  padding: 16px;
  box-shadow: 0 1px 3px rgba(0,0,0,0.08);
  display: flex;
  flex-direction: column;
}

.palette-section + .palette-section {
  margin-top: 16px;
}

.palette-title {
  font-size: 13px;
  color: #909399;
  margin-bottom: 8px;
}

.palette-items {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.field-card {
  border: 1px solid #ebeef5;
  border-radius: 6px;
  padding: 12px;
  margin-bottom: 12px;
  cursor: pointer;
  transition: all 0.2s ease;
}

.field-card.active {
  border-color: #409EFF;
  box-shadow: 0 0 0 2px rgba(64,158,255,0.15);
}

.field-card-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.field-name {
  font-weight: 500;
}

.field-ops {
  display: flex;
  align-items: center;
  gap: 6px;
}

.property-panel {
  overflow: hidden;
}

.property-scroll {
  max-height: calc(100vh - 200px);
}

.options-editor {
  margin-top: 10px;
}

.options-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 6px;
}

.option-item {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 6px;
}

.preview-wrapper {
  width: 100%;
  background: #f5f5f5;
  border-radius: 12px;
  padding: 16px;
}

.preview-mobile {
  max-width: 360px;
  margin: 0 auto;
}

.preview-desktop {
  max-width: 720px;
  margin: 0 auto;
}

.preview-header {
  margin-bottom: 12px;
  text-align: center;
}

.preview-body {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.preview-field {
  background: #fff;
  border: 1px solid #ebeef5;
  border-radius: 6px;
  padding: 12px;
}

.preview-label {
  display: block;
  font-size: 13px;
  margin-bottom: 6px;
  color: #606266;
}

.preview-label .serial {
  margin-right: 4px;
  color: #909399;
}

.preview-label .required {
  color: #f56c6c;
  margin-left: 4px;
}

.preview-control {
  background: #f5f7fa;
  border-radius: 4px;
  padding: 8px 10px;
  color: #c0c4cc;
  font-size: 13px;
}

.preview-submit {
  width: 100%;
  margin-top: 16px;
}

.fade-enter-active, .fade-leave-active {
  transition: opacity .2s ease;
}

.fade-enter, .fade-leave-to {
  opacity: 0;
}
</style>
