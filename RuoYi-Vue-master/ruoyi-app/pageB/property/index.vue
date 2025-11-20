
<template>
	<view class="container">
		<!-- 小区统计信息 -->
		<view class="stats-section">
			<view class="stats-card">
				<view class="stat-item">
					<text class="stat-label">已认证人数</text>
					<text class="stat-value">{{ communityStats.certifiedCount || 0 }}</text>
					<text class="stat-unit">人</text>
				</view>
				<view class="stat-divider"></view>
				<view class="stat-item">
					<text class="stat-label">房产面积总和</text>
					<text class="stat-value">{{ communityStats.totalArea || 0 }}</text>
					<text class="stat-unit">㎡</text>
				</view>
			</view>
		</view>

		<!-- 房产列表 -->
		<view class="property-list-section">
			<view class="section-title">我的房产</view>
			<view v-if="propertyList.length > 0">
				<uni-card v-for="(item, index) in propertyList" :key="item.associationId" :title="item.communityName" :sub-title="item.propertyInfo" is-full>
					<text>用户类型：{{ formatUserType(item.userType) }}</text>
				</uni-card>
			</view>
			<view v-else class="empty-container">
				<uni-icons type="home" size="60" color="#D9D9D9" />
				<text class="empty-text">您还没有已绑定的房产</text>
			</view>
		</view>

		<view class="bottom-buttons">
			<button class="button" @click="goToHistory">提交历史</button>
			<button class="button primary" @click="goToAddProperty">新增</button>
		</view>
	</view>
</template>

<script>
	import { listMyProperty } from "@/api/property.js";
	import { mapGetters } from 'vuex';

	export default {
		data() {
			return {
				propertyList: [],
				communityStats: {
					certifiedCount: 0,
					totalArea: 0
				}
			};
		},
		computed: {
			...mapGetters(['ownerProfile'])
		},
		onShow() {
			this.getMyPropertyList();
			this.getCommunityStats();
		},
		methods: {
			getMyPropertyList() {
				// status=1表示查询已通过的
				listMyProperty({ status: '1' }).then(response => {
					const rows = response.rows || [];
					this.propertyList = rows.map(item => ({
						...item,
						communityName: item.communityName || '',
						propertyInfo: this.buildPropertyInfo(item)
					}));
				});
			},
			getCommunityStats() {
				// TODO: 调用后端接口获取小区统计数据
				// 这里暂时使用模拟数据，等后端接口开发完成后替换
				// 获取当前用户绑定的小区ID
				const communityId = uni.getStorageSync('app_current_community_id');

				if (communityId) {
					// 模拟数据
					this.communityStats = {
						certifiedCount: 120,
						totalArea: 15600.5
					};

					/* 实际调用示例:
					getCommunityStats(communityId).then(response => {
						this.communityStats = {
							certifiedCount: response.data.certifiedCount || 0,
							totalArea: response.data.totalArea || 0
						};
					}).catch(error => {
						console.error('获取小区统计信息失败', error);
					});
					*/
				}
			},
			buildPropertyInfo(item) {
				const parts = [item.buildingName, item.unitName, item.roomNumber].filter(Boolean);
				return parts.join('');
			},
			formatUserType(userType) {
				const map = { '00': '业主', '01': '家属', '02': '租户' };
				return map[userType] || '未知';
			},
			goToAddProperty() {
				uni.navigateTo({
					url: '/pageB/property/add'
				});
			},
			goToHistory() {
				uni.navigateTo({
					url: '/pageB/property/history'
				});
			}
		}
	}
</script>

<style lang="scss">
	.container {
		min-height: 100vh;
		background: #F5F5F5;
		padding-bottom: 160rpx; /* 增加底部内边距，防止被按钮遮挡 */
	}

	/* 统计信息区域 */
	.stats-section {
		padding: 30rpx;

		.stats-card {
			display: flex;
			align-items: center;
			background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
			border-radius: 24rpx;
			padding: 40rpx 30rpx;
			box-shadow: 0 8rpx 24rpx rgba(102, 126, 234, 0.3);

			.stat-item {
				flex: 1;
				display: flex;
				flex-direction: column;
				align-items: center;

				.stat-label {
					font-size: 26rpx;
					color: rgba(255, 255, 255, 0.8);
					margin-bottom: 16rpx;
				}

				.stat-value {
					font-size: 56rpx;
					font-weight: 600;
					color: #FFFFFF;
					line-height: 1;
					margin-bottom: 8rpx;
				}

				.stat-unit {
					font-size: 24rpx;
					color: rgba(255, 255, 255, 0.7);
				}
			}

			.stat-divider {
				width: 2rpx;
				height: 80rpx;
				background: rgba(255, 255, 255, 0.3);
			}
		}
	}

	/* 房产列表区域 */
	.property-list-section {
		padding: 0 30rpx;

		.section-title {
			font-size: 32rpx;
			font-weight: 600;
			color: #262626;
			margin-bottom: 24rpx;
		}
	}

	.empty-container {
		display: flex;
		flex-direction: column;
		align-items: center;
		justify-content: center;
		padding: 120rpx 0;
		background: #FFFFFF;
		border-radius: 24rpx;

		.empty-text {
			margin-top: 24rpx;
			font-size: 28rpx;
			color: #999;
		}
	}

	.bottom-buttons {
		position: fixed;
		bottom: 0;
		left: 0;
		right: 0;
		background-color: #fff;
		display: flex;
		padding: 20rpx 30rpx;
		box-shadow: 0 -2px 10px rgba(0, 0, 0, 0.05);
		padding-bottom: constant(safe-area-inset-bottom);
        padding-bottom: env(safe-area-inset-bottom);
		gap: 24rpx;
	}

	.button {
		flex: 1;
		height: 88rpx;
		border-radius: 12rpx;
		font-size: 32rpx;
		font-weight: 600;
		border: none;

		&.primary {
			background: linear-gradient(135deg, #1890FF, #40A9FF);
			color: white;
		}

		&:not(.primary) {
			background-color: #F5F5F5;
			color: #595959;
		}
	}
</style>
