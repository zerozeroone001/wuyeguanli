<template>
	<view class="app-container">
		<uni-list v-if="contractList.length > 0">
			<uni-list-item v-for="(contract, index) in contractList" :key="index" :title="contract.contractName" :note="'生效日期: ' + contract.effectiveDate" clickable @click="handleView(contract)">
			</uni-list-item>
		</uni-list>
		<view v-else class="empty-container">
			<text>暂无合同信息</text>
		</view>
	</view>
</template>

<script>
	import { listUserContract } from '@/api/contract.js';

	export default {
		data() {
			return {
				contractList: []
			};
		},
		onLoad() {
			this.getList();
		},
		methods: {
			getList() {
				listUserContract().then(response => {
					this.contractList = response.rows;
				});
			},
			handleView(contract) {
				// uni.navigateTo({ url: '/pages/property/contract/detail?id=' + contract.contractId });
        this.$modal.msg("跳转到合同详情页，该功能待实现");
			}
		}
	};
</script>

<style lang="scss">
.empty-container {
	display: flex;
	justify-content: center;
	align-items: center;
	height: 200px;
	color: #999;
}
</style>