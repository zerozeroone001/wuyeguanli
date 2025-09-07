
<template>
	<view class="container">
		<view v-if="propertyList.length > 0">
			<uni-card v-for="(item, index) in propertyList" :key="item.associationId" :title="item.communityName" :sub-title="item.propertyInfo" is-full>
				<text>用户类型：{{ formatUserType(item.userType) }}</text>
			</uni-card>
		</view>
		<view v-else class="empty-container">
			<text>您还没有已绑定的房产</text>
		</view>

		<view class="bottom-buttons">
			<button class="button" @click="goToHistory">提交历史</button>
			<button class="button primary" @click="goToAddProperty">新增</button>
		</view>
	</view>
</template>

<script>
	import { listMyProperty } from "@/api/property.js";
	import { getPropertyDetails } from "@/api/property.js";

	export default {
		data() {
			return {
				propertyList: []
			};
		},
		onShow() {
			this.getMyPropertyList();
		},
		methods: {
			getMyPropertyList() {
				// status=1表示查询已通过的
				listMyProperty({ status: '1' }).then(response => {
					const userProperties = response.rows;
					const promises = userProperties.map(up => getPropertyDetails(up.propertyId));
					
					Promise.all(promises).then(detailsResponses => {
						this.propertyList = userProperties.map((up, index) => {
							const details = detailsResponses[index].data;
							return {
								...up,
								communityName: details.communityName, // 从房产详情中获取小区名
								propertyInfo: `${details.buildingName}${details.unitName}${details.roomNumber}`
							};
						});
					});
				});
			},
			formatUserType(userType) {
				const map = { '00': '业主', '01': '家属', '02': '租户' };
				return map[userType] || '未知';
			},
			goToAddProperty() {
				uni.navigateTo({
					url: '/pages/property/add'
				});
			},
			goToHistory() {
				uni.navigateTo({
					url: '/pages/property/history'
				});
			}
		}
	}
</script>

<style lang="scss">
	.container {
		padding-bottom: 140rpx; /* 增加底部内边距，防止被按钮遮挡 */
	}
	.empty-container {
		text-align: center;
		margin-top: 100rpx;
		color: #999;
	}
	.bottom-buttons {
		position: fixed;
		bottom: 0;
		left: 0;
		right: 0;
		background-color: #fff;
		display: flex;
		padding: 20rpx;
		box-shadow: 0 -2px 10px rgba(0, 0, 0, 0.05);
		padding-bottom: constant(safe-area-inset-bottom);
        padding-bottom: env(safe-area-inset-bottom);
	}
	.button {
		flex: 1;
		margin: 0 10rpx;
		&.primary {
			background-color: #3c9cff;
			color: white;
		}
	}
</style>
