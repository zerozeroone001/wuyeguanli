
<template>
	<view class="form-container">
		<uni-forms ref="form" :modelValue="formData" :rules="rules">
			<uni-forms-item label="选择小区" required name="communityId">
				<picker @change="handleCommunityChange" :value="communityIndex" :range="communityList" range-key="communityName">
					<view class="uni-input">{{communityList[communityIndex] ? communityList[communityIndex].communityName : '请选择小区'}}</view>
				</picker>
			</uni-forms-item>
			<uni-forms-item label="选择楼栋" required name="buildingName">
				<picker @change="handleBuildingChange" :value="buildingIndex" :range="buildingList" range-key="buildingName" :disabled="!formData.communityId">
					<view class="uni-input">{{buildingList[buildingIndex] ? buildingList[buildingIndex].buildingName : '请先选择小区'}}</view>
				</picker>
			</uni-forms-item>
			<uni-forms-item label="选择房号" required name="propertyId">
				<picker @change="handleRoomChange" :value="roomIndex" :range="roomList" range-key="roomNumber" :disabled="!formData.buildingName">
					<view class="uni-input">{{roomList[roomIndex] ? roomList[roomIndex].roomNumber : '请先选择楼栋'}}</view>
				</picker>
			</uni-forms-item>
			<uni-forms-item label="用户类型" required name="userType">
				<uni-data-checkbox v-model="formData.userType" :localdata="userTypes"></uni-data-checkbox>
			</uni-forms-item>
		</uni-forms>
		<button type="primary" @click="submit">提交审核</button>
	</view>
</template>

<script>
	import { listAllProperty, addMyProperty, listCommunity } from "@/api/property.js";

	export default {
		data() {
			return {
				formData: {
					communityId: null,
					buildingName: null,
					propertyId: null,
					userType: '00'
				},
				communityList: [],
				communityIndex: -1,
				buildingList: [],
				buildingIndex: -1,
				roomList: [],
				roomIndex: -1,
				userTypes: [{ text: '业主', value: '00' }, { text: '家属', value: '01' }, { text: '租户', value: '02' }],
				rules: {
					communityId: { rules: [{ required: true, errorMessage: '请选择小区' }] },
					buildingName: { rules: [{ required: true, errorMessage: '请选择楼栋' }] },
					propertyId: { rules: [{ required: true, errorMessage: '请选择房号' }] },
					userType: { rules: [{ required: true, errorMessage: '请选择用户类型' }] }
				}
			};
		},
		created() {
			this.getCommunities();
		},
		methods: {
			getCommunities() {
				listCommunity().then(response => {
					this.communityList = response.rows;
				});
			},
			handleCommunityChange(e) {
				this.communityIndex = e.detail.value;
				this.formData.communityId = this.communityList[this.communityIndex].communityId;
				// 清空后续选择
				this.resetBuilding();
				this.resetRoom();
				this.getBuildings();
			},
			getBuildings() {
				listAllProperty({ communityId: this.formData.communityId }).then(response => {
					const buildings = {};
					response.rows.forEach(p => {
						if (!buildings[p.buildingName]) {
							buildings[p.buildingName] = { buildingName: p.buildingName };
						}
					});
					this.buildingList = Object.values(buildings);
				});
			},
			handleBuildingChange(e) {
				this.buildingIndex = e.detail.value;
				this.formData.buildingName = this.buildingList[this.buildingIndex].buildingName;
				// 清空后续选择
				this.resetRoom();
				this.getRooms();
			},
			getRooms() {
				listAllProperty({ communityId: this.formData.communityId, buildingName: this.formData.buildingName }).then(response => {
					this.roomList = response.rows;
				});
			},
			handleRoomChange(e) {
				this.roomIndex = e.detail.value;
				this.formData.propertyId = this.roomList[this.roomIndex].propertyId;
			},
			resetBuilding() {
				this.buildingList = [];
				this.buildingIndex = -1;
				this.formData.buildingName = null;
			},
			resetRoom() {
				this.roomList = [];
				this.roomIndex = -1;
				this.formData.propertyId = null;
			},
			submit() {
				this.$refs.form.validate().then(res => {
					const submissionData = {
						propertyId: this.formData.propertyId,
						userType: this.formData.userType
					};
					addMyProperty(submissionData).then(() => {
						this.$modal.msgSuccess("提交成功，请等待审核");
						setTimeout(() => {
							uni.navigateBack();
						}, 1500);
					});
				}).catch(err => {
					console.log('表单错误信息：', err);
				});
			}
		}
	}
</script>

<style>
	.form-container {
		padding: 30rpx;
	}
</style>
