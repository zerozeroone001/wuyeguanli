
<template>
	<view class="container">
		<view v-if="historyList.length > 0">
			<uni-card v-for="(item, index) in historyList" :key="item.associationId" :title="item.communityName" :sub-title="item.propertyInfo" :extra="formatStatus(item.status).text" :class="'status-' + item.status">
				<text>用户类型：{{ formatUserType(item.userType) }}</text>
			</uni-card>
		</view>
		<view v-else class="empty-container">
			<text>您还没有任何提交记录</text>
		</view>
	</view>
</template>

<script>
	import { listMyProperty } from "@/api/property.js";
	import { getPropertyDetails } from "@/api/property.js";

	export default {
		data() {
			return {
				historyList: []
			};
		},
		onShow() {
			this.getHistoryList();
		},
		methods: {
			getHistoryList() {
				// 不带任何查询参数，获取当前用户的所有记录
				listMyProperty().then(response => {
					const userProperties = response.rows;
					const promises = userProperties.map(up => getPropertyDetails(up.propertyId));
					
					Promise.all(promises).then(detailsResponses => {
						this.historyList = userProperties.map((up, index) => {
							const details = detailsResponses[index].data;
							return {
								...up,
								communityName: details.communityName,
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
			formatStatus(status) {
				const map = {
					'0': { text: '待审核', color: '#ff9900' },
					'1': { text: '已通过', color: '#18bc37' },
					'2': { text: '已驳回', color: '#e43d33' }
				};
				return map[status] || { text: '未知', color: '#999' };
			}
		}
	}
</script>

<style lang="scss">
	.empty-container {
		text-align: center;
		margin-top: 100rpx;
		color: #999;
	}

	// 根据状态给卡片添加不同颜色的边框
	.status-0 .uni-card__header-extra-text {
		color: #ff9900 !important;
	}
	.status-1 .uni-card__header-extra-text {
		color: #18bc37 !important;
	}
	.status-2 .uni-card__header-extra-text {
		color: #e43d33 !important;
	}
</style>
