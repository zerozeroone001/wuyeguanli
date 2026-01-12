<template>
  <div class="estate-container">
    <el-row :gutter="16" class="estate-main">
      <el-col :span="7" class="estate-side">
        <el-card shadow="never" class="full-card">
          <div slot="header" class="side-header">
            <div class="selector-block">
              <div class="selector-label">小区</div>
              <el-select
                v-model="selectedCommunityId"
                filterable
                placeholder="请选择小区"
                size="small"
                class="selector-select"
              >
                <el-option
                  v-for="item in communityOptions"
                  :key="item.communityId"
                  :label="item.communityName"
                  :value="item.communityId"
                />
              </el-select>
            </div>
            <div class="side-actions">
              <el-button type="primary" size="mini" icon="el-icon-plus" @click="handleCommunityAdd">新增</el-button>
              <el-button
                size="mini"
                icon="el-icon-edit"
                :disabled="!selectedCommunity"
                @click="handleCommunityEdit"
              >编辑</el-button>
              <el-dropdown trigger="click" @command="handleCommunityCommand">
                <el-button size="mini" icon="el-icon-more-outline">更多</el-button>
                <el-dropdown-menu slot="dropdown">
                  <el-dropdown-item command="refresh">刷新列表</el-dropdown-item>
                  <el-dropdown-item
                    divided
                    command="remove"
                    :disabled="!selectedCommunity"
                  >删除当前小区</el-dropdown-item>
                </el-dropdown-menu>
              </el-dropdown>
            </div>
          </div>
          <div class="estate-tree-container">
            <el-empty v-if="!buildingTree.length && !treeLoading" description="暂无房源数据，先新增一套房屋试试" />
            <el-skeleton v-else-if="treeLoading" animated :rows="6" />
            <template v-else>
              <el-tree
                ref="buildingTreeRef"
                :data="buildingTree"
                :props="treeProps"
                node-key="id"
                highlight-current
                @node-click="handleTreeNodeClick"
                class="estate-tree"
              >
                <template slot-scope="{ node, data }">
                  <div class="tree-node">
                    <span class="tree-label">
                      <i
                        v-if="data.type === 'building'"
                        class="el-icon-office-building tree-icon"
                      />
                      <i
                        v-else
                        class="el-icon-s-home tree-icon"
                      />
                      {{ node.label }}
                    </span>
                    <el-dropdown
                      v-if="data.type === 'unit'"
                      trigger="click"
                      @command="handleUnitCommand($event, data)"
                    >
                      <i class="el-icon-more tree-more"></i>
                      <el-dropdown-menu slot="dropdown">
                        <el-dropdown-item command="addRoom">新增房屋</el-dropdown-item>
                        <el-dropdown-item command="batchCreate">批量创建</el-dropdown-item>
                      </el-dropdown-menu>
                    </el-dropdown>
                  </div>
                </template>
              </el-tree>
            </template>
          </div>
        </el-card>
      </el-col>
      <el-col :span="17" class="estate-content">
        <el-card shadow="never" class="full-card">
          <div slot="header" class="content-header">
            <div class="content-title">
              <h3>
                {{ selectedBuildingName ? selectedBuildingName : '请选择楼栋' }}
                <span v-if="selectedUnitName"> / {{ selectedUnitName }}</span>
              </h3>
              <p v-if="selectedCommunity" class="content-subtitle">
                当前小区：{{ selectedCommunity.communityName }}
                <span v-if="selectedCommunity.address">（{{ selectedCommunity.address }}）</span>
              </p>
            </div>
            <div class="content-actions">
              <el-button size="mini" @click="sortRooms" icon="el-icon-sort">房屋排序</el-button>
              <el-button size="mini" icon="el-icon-upload2" @click="batchImport">批量导入</el-button>
              <el-button
                size="mini"
                type="primary"
                icon="el-icon-plus"
                :disabled="!selectedBuildingName"
                @click="handlePropertyAdd()"
              >新增房屋</el-button>
            </div>
          </div>
          <div class="estate-grid-wrapper">
            <el-empty v-if="!gridFloors.length && !propertyLoading" description="请选择左侧楼栋/单元后开始添加房屋" />
            <el-skeleton v-else-if="propertyLoading" animated :rows="6" />
            <div v-else class="estate-grid">
              <div class="grid-row grid-header">
                <div class="grid-cell floor-col">楼层</div>
                <div
                  v-for="unit in gridUnits"
                  :key="unit"
                  class="grid-cell unit-col"
                >
                  <span>{{ formatUnit(unit) }}</span>
                  <el-button
                    type="text"
                    icon="el-icon-plus"
                    size="mini"
                    @click="handlePropertyAdd(unit)"
                  />
                </div>
              </div>
              <div
                v-for="floor in gridFloors"
                :key="floor.key"
                class="grid-row"
              >
                <div class="grid-cell floor-col">{{ floor.label }}</div>
                <div
                  v-for="unit in gridUnits"
                  :key="unit + '-' + floor.key"
                  class="grid-cell unit-cell"
                >
                  <div class="room-list">
                    <div
                      v-for="room in getRooms(unit, floor.value)"
                      :key="room.propertyId"
                      class="room-item"
                      :class="{ disabled: room.status === '1' }"
                    >
                      <span class="room-number" @click="handlePropertyEdit(room)">
                        {{ room.roomNumber }}
                      </span>
                      <i
                        class="el-icon-delete room-remove"
                        @click.stop="handlePropertyDelete(room)"
                      />
                    </div>
                    <el-button
                      class="room-add"
                      type="primary"
                      icon="el-icon-plus"
                      circle
                      size="mini"
                      @click="handlePropertyAdd(unit, floor.value)"
                    />
                  </div>
                </div>
              </div>
            </div>
          </div>
        </el-card>
      </el-col>
    </el-row>

    <!-- 小区维护 -->
    <el-dialog :title="communityTitle" :visible.sync="communityOpen" width="620px" append-to-body>
      <el-form ref="communityFormRef" :model="communityForm" :rules="communityRules" label-width="100px">
        <el-form-item label="小区名称" prop="communityName">
          <el-input v-model="communityForm.communityName" placeholder="请输入小区名称" />
        </el-form-item>
        <el-form-item label="省份" prop="province">
          <el-input v-model="communityForm.province" placeholder="请输入省份" />
        </el-form-item>
        <el-form-item label="城市" prop="city">
          <el-input v-model="communityForm.city" placeholder="请输入城市" />
        </el-form-item>
        <el-form-item label="区县" prop="district">
          <el-input v-model="communityForm.district" placeholder="请输入区/县" />
        </el-form-item>
        <el-form-item label="详细地址" prop="address">
          <el-input v-model="communityForm.address" placeholder="请输入详细地址" />
        </el-form-item>
        <el-form-item label="联系人" prop="contactPerson">
          <el-input v-model="communityForm.contactPerson" placeholder="请输入联系人" />
        </el-form-item>
        <el-form-item label="联系电话" prop="contactPhone">
          <el-input v-model="communityForm.contactPhone" placeholder="请输入联系电话" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="communityForm.status">
            <el-radio v-for="dict in dict.type.sys_normal_disable" :key="dict.value" :label="dict.value">
              {{ dict.label }}
            </el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="communityForm.remark" type="textarea" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitCommunityForm">保 存</el-button>
        <el-button @click="cancelCommunity">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 房屋维护 -->
    <el-dialog :title="propertyTitle" :visible.sync="propertyOpen" width="620px" append-to-body>
      <el-form ref="propertyFormRef" :model="propertyForm" :rules="propertyRules" label-width="100px">
        <el-form-item label="所属小区" prop="communityId">
          <el-select v-model="propertyForm.communityId" placeholder="请选择所属小区">
            <el-option
              v-for="item in communityOptions"
              :key="item.communityId"
              :label="item.communityName"
              :value="item.communityId"
            />
          </el-select>
        </el-form-item>
        <el-form-item label="楼栋" prop="buildingName">
          <el-input v-model="propertyForm.buildingName" placeholder="请输入楼栋名称" />
        </el-form-item>
        <el-form-item label="单元" prop="unitName">
          <el-input v-model="propertyForm.unitName" placeholder="请输入单元名称" />
        </el-form-item>
        <el-form-item label="楼层" prop="floor">
          <el-input-number
            v-model="propertyForm.floor"
            :min="0"
            :max="200"
            :step="1"
            controls-position="right"
            style="width: 100%;"
          />
        </el-form-item>
        <el-form-item label="门牌号" prop="roomNumber">
          <el-input v-model="propertyForm.roomNumber" placeholder="请输入门牌号" />
        </el-form-item>
        <el-form-item label="建筑面积(㎡)" prop="area">
          <el-input v-model="propertyForm.area" placeholder="请输入建筑面积" />
        </el-form-item>
        <el-form-item label="状态" prop="status">
          <el-radio-group v-model="propertyForm.status">
            <el-radio v-for="dict in dict.type.sys_normal_disable" :key="dict.value" :label="dict.value">
              {{ dict.label }}
            </el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="备注" prop="remark">
          <el-input v-model="propertyForm.remark" type="textarea" placeholder="请输入备注" />
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitPropertyForm">保 存</el-button>
        <el-button @click="cancelProperty">取 消</el-button>
      </div>
    </el-dialog>

    <!-- 批量导入对话框 -->
    <el-dialog title="批量导入房产信息" :visible.sync="upload.open" width="400px" append-to-body>
      <el-upload
        ref="upload"
        :limit="1"
        accept=".xlsx, .xls"
        :headers="upload.headers"
        :action="upload.url + '?updateSupport=' + upload.updateSupport"
        :disabled="upload.isUploading"
        :on-progress="handleFileUploadProgress"
        :on-success="handleFileSuccess"
        :auto-upload="false"
        drag
      >
        <i class="el-icon-upload"></i>
        <div class="el-upload__text">将文件拖到此处，或<em>点击上传</em></div>
        <div class="el-upload__tip text-center" slot="tip">
          <div class="el-upload__tip" slot="tip">
            <el-checkbox v-model="upload.updateSupport" />是否更新已经存在的房产数据
          </div>
          <span>仅允许导入xls、xlsx格式文件。</span>
          <el-link type="primary" :underline="false" style="font-size: 12px; vertical-align: baseline" @click="importTemplate">下载模板</el-link>
        </div>
      </el-upload>
      <div slot="footer" class="dialog-footer">
        <el-button type="primary" @click="submitFileForm">确 定</el-button>
        <el-button @click="upload.open = false">取 消</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  listCommunity,
  getCommunity,
  addCommunity,
  updateCommunity,
  delCommunity
} from '@/api/system/community'
import {
  listEstateProperty,
  getEstateProperty,
  addEstateProperty,
  updateEstateProperty,
  delEstateProperty
} from '@/api/system/estateProperty'
import { getToken } from '@/utils/auth'

const DEFAULT_UNIT = '默认单元'
const DEFAULT_BUILDING = '未命名楼栋'

export default {
  name: 'EstateManagement',
  dicts: ['sys_normal_disable'],
  data() {
    return {
      communityOptions: [],
      selectedCommunityId: null,
      selectedCommunity: null,
      buildingTree: [],
      treeProps: {
        children: 'children',
        label: 'label'
      },
      treeLoading: false,
      propertyLoading: false,
      propertyList: [],
      selectedBuildingName: null,
      selectedUnitName: null,
      communityOpen: false,
      communityTitle: '',
      communityForm: {},
      communityRules: {
        communityName: [{ required: true, message: '小区名称不能为空', trigger: 'blur' }],
        status: [{ required: true, message: '请选择状态', trigger: 'change' }],
        contactPhone: [
          {
            pattern: /^$|^1[3-9]\d{9}$|^0\d{2,3}-?\d{7,8}$/,
            message: '请输入正确的联系电话',
            trigger: 'blur'
          }
        ]
      },
      propertyOpen: false,
      propertyTitle: '',
      propertyForm: {},
      propertyRules: {
        communityId: [{ required: true, message: '请选择所属小区', trigger: 'change' }],
        buildingName: [{ required: true, message: '楼栋不能为空', trigger: 'blur' }],
        unitName: [{ required: true, message: '单元不能为空', trigger: 'blur' }],
        roomNumber: [{ required: true, message: '门牌号不能为空', trigger: 'blur' }],
        status: [{ required: true, message: '请选择状态', trigger: 'change' }]
      },
      collator: new Intl.Collator('zh-Hans-CN', { numeric: true, sensitivity: 'base' }),
      // 批量导入参数
      upload: {
        // 是否显示弹出层
        open: false,
        // 是否禁用上传
        isUploading: false,
        // 是否更新已经存在的数据
        updateSupport: 0,
        // 设置上传的请求头部
        headers: { Authorization: 'Bearer ' + getToken() },
        // 上传的地址
        url: process.env.VUE_APP_BASE_API + '/system/property/importData'
      }
    }
  },
  computed: {
    gridUnits() {
      if (!this.selectedBuildingName) return []
      const units = new Set()
      this.propertyList
        .filter(item => this.matchBuilding(item, this.selectedBuildingName))
        .forEach(item => {
          units.add(this.normalizeUnit(item.unitName))
        })
      if (this.selectedUnitName) {
        return Array.from(units).filter(unit => unit === this.selectedUnitName)
      }
      return this.naturalSort(Array.from(units))
    },
    gridFloors() {
      const floors = new Set()
      this.filteredProperties().forEach(item => {
        const raw = item.floor
        if (raw === null || raw === undefined || raw === '') {
          floors.add('_blank')
        } else {
          floors.add(Number(raw))
        }
      })
      const result = Array.from(floors).map(value => {
        if (value === '_blank') {
          return { key: 'blank', value: null, label: '未分层' }
        }
        return { key: value, value, label: `${value}层` }
      })
      return result.sort((a, b) => {
        if (a.value === null) return 1
        if (b.value === null) return -1
        return b.value - a.value
      })
    }
  },
  created() {
    this.loadCommunities()
  },
  methods: {
    loadCommunities() {
      const params = { pageNum: 1, pageSize: 999 }
      listCommunity(params).then(response => {
        this.communityOptions = response.rows || []
        if (!this.communityOptions.length) {
          this.selectedCommunityId = null
          this.selectedCommunity = null
          this.buildingTree = []
          this.propertyList = []
          return
        }
        if (!this.selectedCommunityId) {
          this.selectedCommunityId = this.communityOptions[0].communityId
        } else {
          this.handleCommunityChange()
        }
      })
    },
    handleCommunityChange() {
      this.selectedCommunity = this.communityOptions.find(item => item.communityId === this.selectedCommunityId) || null
      this.resetSelection()
      if (!this.selectedCommunityId) {
        this.buildingTree = []
        this.propertyList = []
        return
      }
      this.fetchPropertyList()
    },
    fetchPropertyList() {
      this.propertyLoading = true
      this.treeLoading = true
      const params = {
        communityId: this.selectedCommunityId,
        pageNum: 1,
        pageSize: 5000
      }
      listEstateProperty(params).then(response => {
        this.propertyList = response.rows || []
        this.propertyLoading = false
        this.treeLoading = false
        this.composeTree()
      }).catch(() => {
        this.propertyLoading = false
        this.treeLoading = false
      })
    },
    composeTree() {
      const buildingMap = new Map()
      this.propertyList.forEach(item => {
        const buildingName = this.normalizeBuilding(item.buildingName)
        const unitName = this.normalizeUnit(item.unitName)
        if (!buildingMap.has(buildingName)) {
          buildingMap.set(buildingName, new Map())
        }
        const unitMap = buildingMap.get(buildingName)
        if (!unitMap.has(unitName)) {
          unitMap.set(unitName, [])
        }
        unitMap.get(unitName).push(item)
      })
      const tree = []
      let index = 0
      Array.from(buildingMap.keys()).sort((a, b) => this.compareAlphaNumeric(a, b)).forEach(buildingName => {
        const unitMap = buildingMap.get(buildingName)
        const buildingNode = {
          id: `building-${index}`,
          label: buildingName,
          type: 'building',
          buildingName,
          children: []
        }
        let unitIndex = 0
        Array.from(unitMap.keys()).sort((a, b) => this.compareAlphaNumeric(a, b)).forEach(unitName => {
          buildingNode.children.push({
            id: `unit-${index}-${unitIndex}`,
            label: unitName,
            type: 'unit',
            buildingName,
            unitName,
            count: unitMap.get(unitName).length
          })
          unitIndex += 1
        })
        tree.push(buildingNode)
        index += 1
      })
      this.buildingTree = tree
      this.$nextTick(() => {
        if (tree.length) {
          const firstBuilding = tree[0]
          this.selectedBuildingName = firstBuilding.buildingName
          this.selectedUnitName = null
          this.$refs.buildingTreeRef && this.$refs.buildingTreeRef.setCurrentKey(firstBuilding.id)
        } else {
          this.resetSelection()
        }
      })
    },
    handleTreeNodeClick(node) {
      if (node.type === 'building') {
        this.selectedBuildingName = node.buildingName
        this.selectedUnitName = null
      } else if (node.type === 'unit') {
        this.selectedBuildingName = node.buildingName
        this.selectedUnitName = node.unitName
      }
    },
    handleUnitCommand(command, node) {
      if (command === 'addRoom') {
        this.handlePropertyAdd(node.unitName)
      } else if (command === 'batchCreate') {
        this.$modal.msgWarning('批量创建功能待实现')
      }
    },
    filteredProperties() {
      if (!this.selectedBuildingName) return []
      return this.propertyList.filter(item => {
        if (!this.matchBuilding(item, this.selectedBuildingName)) return false
        if (this.selectedUnitName && this.normalizeUnit(item.unitName) !== this.selectedUnitName) return false
        return true
      })
    },
    getRooms(unitName, floorValue) {
      return this.filteredProperties()
        .filter(item => this.normalizeUnit(item.unitName) === unitName)
        .filter(item => {
          const floor = item.floor === null || item.floor === undefined ? null : Number(item.floor)
          return floor === floorValue
        })
        .sort((a, b) => this.collator.compare(a.roomNumber || '', b.roomNumber || ''))
    },
    formatUnit(unit) {
      return unit || DEFAULT_UNIT
    },
    normalizeUnit(unit) {
      return unit && unit.trim() ? unit : DEFAULT_UNIT
    },
    normalizeBuilding(building) {
      return building && building.trim() ? building : DEFAULT_BUILDING
    },
    matchBuilding(item, buildingName) {
      return this.normalizeBuilding(item.buildingName) === buildingName
    },
    resetSelection() {
      this.selectedBuildingName = null
      this.selectedUnitName = null
    },
    compareAlphaNumeric(a, b) {
      return this.collator.compare(a || '', b || '')
    },
    naturalSort(list) {
      return list.sort((a, b) => this.compareAlphaNumeric(a, b))
    },
    // Community actions
    handleCommunityAdd() {
      this.resetCommunityForm()
      this.communityTitle = '新增小区'
      this.communityOpen = true
    },
    handleCommunityEdit() {
      if (!this.selectedCommunityId) {
        this.$modal.msgWarning('请先选择要编辑的小区')
        return
      }
      this.resetCommunityForm()
      getCommunity(this.selectedCommunityId).then(response => {
        this.communityForm = Object.assign({}, response.data)
        this.communityTitle = '编辑小区'
        this.communityOpen = true
      })
    },
    handleCommunityCommand(command) {
      if (command === 'refresh') {
        this.loadCommunities()
      }
      if (command === 'remove') {
        if (!this.selectedCommunityId) {
          this.$modal.msgWarning('请选择要删除的小区')
          return
        }
        this.$modal.confirm('是否确认删除当前小区？').then(() => {
          return delCommunity(this.selectedCommunityId)
        }).then(() => {
          this.$modal.msgSuccess('删除成功')
          this.selectedCommunityId = null
          this.loadCommunities()
        }).catch(() => {})
      }
    },
    submitCommunityForm() {
      this.$refs.communityFormRef.validate(valid => {
        if (!valid) return
        const request = this.communityForm.communityId ? updateCommunity : addCommunity
        request(this.communityForm).then(() => {
          this.$modal.msgSuccess('保存成功')
          this.communityOpen = false
          this.loadCommunities()
        })
      })
    },
    cancelCommunity() {
      this.communityOpen = false
      this.resetCommunityForm()
    },
    resetCommunityForm() {
      this.communityForm = {
        communityId: null,
        communityName: null,
        province: null,
        city: null,
        district: null,
        address: null,
        contactPerson: null,
        contactPhone: null,
        status: '0',
        remark: null
      }
      this.resetForm('communityFormRef')
    },
    // Property actions
    handlePropertyAdd(unitName, floorValue) {
      if (!this.selectedCommunityId) {
        this.$modal.msgWarning('请先选择小区')
        return
      }
      if (!this.selectedBuildingName) {
        this.$modal.msgWarning('请先选择楼栋')
        return
      }
      this.resetPropertyForm()
      this.propertyTitle = '新增房屋'
      this.propertyForm.communityId = this.selectedCommunityId
      this.propertyForm.buildingName = this.selectedBuildingName === DEFAULT_BUILDING ? '' : this.selectedBuildingName
      this.propertyForm.unitName = unitName && unitName !== DEFAULT_UNIT ? unitName : ''
      if (floorValue !== undefined) {
        this.propertyForm.floor = floorValue
      }
      this.propertyOpen = true
    },
    handlePropertyEdit(row) {
      getEstateProperty(row.propertyId).then(response => {
        this.propertyForm = Object.assign({}, response.data)
        this.propertyTitle = '编辑房屋'
        this.propertyOpen = true
      })
    },
    handlePropertyDelete(row) {
      this.$modal.confirm(`是否确认删除房屋「${row.roomNumber}」？`).then(() => {
        return delEstateProperty(row.propertyId)
      }).then(() => {
        this.$modal.msgSuccess('删除成功')
        this.fetchPropertyList()
      }).catch(() => {})
    },
    submitPropertyForm() {
      this.$refs.propertyFormRef.validate(valid => {
        if (!valid) return
        const request = this.propertyForm.propertyId ? updateEstateProperty : addEstateProperty
        request(this.propertyForm).then(() => {
          this.$modal.msgSuccess('保存成功')
          this.propertyOpen = false
          this.fetchPropertyList()
        })
      })
    },
    cancelProperty() {
      this.propertyOpen = false
      this.resetPropertyForm()
    },
    resetPropertyForm() {
      this.propertyForm = {
        propertyId: null,
        communityId: this.selectedCommunityId,
        buildingName: this.selectedBuildingName === DEFAULT_BUILDING ? '' : this.selectedBuildingName,
        unitName: '',
        floor: null,
        roomNumber: null,
        area: null,
        status: '0',
        remark: null
      }
      this.resetForm('propertyFormRef')
    },
    sortRooms() {
      this.$modal.msgInfo('房屋排序功能暂未实现')
    },
    batchImport() {
      this.upload.open = true
    },
    /** 下载模板操作 */
    importTemplate() {
      this.download('system/property/importTemplate', {}, `property_template_${new Date().getTime()}.xlsx`)
    },
    // 文件上传中处理
    handleFileUploadProgress(event, file, fileList) {
      this.upload.isUploading = true
    },
    // 文件上传成功处理
    handleFileSuccess(response, file, fileList) {
      this.upload.open = false
      this.upload.isUploading = false
      this.$refs.upload.clearFiles()
      this.$alert("<div style='overflow: auto;overflow-x: hidden;max-height: 70vh;padding: 10px 20px 0;'>" + response.msg + "</div>", '导入结果', { dangerouslyUseHTMLString: true })
      this.fetchPropertyList()
    },
    // 提交上传文件
    submitFileForm() {
      this.$refs.upload.submit()
    }
  },
  watch: {
    selectedCommunityId() {
      this.handleCommunityChange()
    }
  }
}
</script>

<style scoped>
.estate-container {
  padding: 0 0 16px;
}

.estate-main {
  min-height: 70vh;
}

.estate-side,
.estate-content {
  display: flex;
}

.full-card {
  width: 100%;
}

.side-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
  gap: 8px;
}

.selector-block {
  display: flex;
  align-items: center;
}

.selector-label {
  margin-right: 8px;
  color: #606266;
}

.selector-select {
  min-width: 180px;
}

.side-actions > * {
  margin-left: 4px;
}

.estate-tree-container {
  height: calc(70vh - 80px);
  overflow: auto;
}

.estate-tree {
  border-right: 1px solid #f0f0f0;
}

.tree-node {
  display: flex;
  align-items: center;
  justify-content: space-between;
  width: 100%;
}

.tree-label {
  display: flex;
  align-items: center;
  gap: 6px;
}

.tree-icon {
  color: #409EFF;
}

.tree-more {
  color: #909399;
  cursor: pointer;
}

.content-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  flex-wrap: wrap;
  gap: 12px;
}

.content-title h3 {
  margin: 0;
  font-size: 18px;
  font-weight: 500;
}

.content-subtitle {
  margin: 4px 0 0;
  color: #909399;
  font-size: 13px;
}

.estate-grid-wrapper {
  min-height: 60vh;
  overflow: auto;
  padding-bottom: 16px;
}

.estate-grid {
  border: 1px solid #ebeef5;
  border-radius: 6px;
  overflow: hidden;
}

.grid-row {
  display: grid;
  grid-template-columns: 140px repeat(auto-fit, minmax(120px, 1fr));
  border-bottom: 1px solid #ebeef5;
}

.grid-row:last-child {
  border-bottom: none;
}

.grid-header {
  background: #f5f7fa;
  font-weight: 500;
}

.grid-cell {
  border-right: 1px solid #ebeef5;
  padding: 12px;
  min-height: 70px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.grid-cell:last-child {
  border-right: none;
}

.floor-col {
  background: #f5f7fa;
  font-weight: 500;
}

.unit-cell {
  justify-content: flex-start;
}

.room-list {
  display: flex;
  flex-wrap: wrap;
  gap: 8px;
}

.room-item {
  background: #ecf5ff;
  color: #409EFF;
  border-radius: 4px;
  padding: 6px 10px;
  display: flex;
  align-items: center;
  gap: 6px;
  cursor: pointer;
  transition: box-shadow 0.2s;
}

.room-item:hover {
  box-shadow: 0 0 6px rgba(64, 158, 255, 0.3);
}

.room-item.disabled {
  background: #f4f4f5;
  color: #909399;
}

.room-number {
  font-weight: 500;
}

.room-remove {
  color: #f56c6c;
  cursor: pointer;
}

.room-add {
  align-self: center;
}

@media (max-width: 1440px) {
  .grid-row {
    grid-template-columns: 120px repeat(auto-fit, minmax(100px, 1fr));
  }
}
</style>
