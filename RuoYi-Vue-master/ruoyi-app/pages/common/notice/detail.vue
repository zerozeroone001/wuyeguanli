<template>
	<view class="app-container">
		<view class="notice-detail">
			<view class="title">{{notice.noticeTitle}}</view>
			<view class="meta-info">
				<text class="author">发布人：{{notice.createBy}}</text>
				<text class="time">时间：{{parseTime(notice.createTime, '{y}-{m}-{d}')}}</text>
			</view>
			<view class="content">
				<rich-text :nodes="notice.noticeContent"></rich-text>
			</view>
		</view>
	</view>
</template>

<script>
	import { getNotice } from '@/api/notice.js';

	export default {
		data() {
			return {
				notice: {}
			};
		},
		onLoad(options) {
			const noticeId = options.id;
			if(noticeId) {
				this.getDetail(noticeId);
			}
		},
		methods: {
			getDetail(id) {
				getNotice(id).then(response => {
					this.notice = response.data;
				});
			}
		}
	};
</script>

<style lang="scss" scoped>
.notice-detail {
	padding: 20px;
	background-color: #fff;

	.title {
		font-size: 20px;
		font-weight: bold;
		text-align: center;
		margin-bottom: 10px;
	}

	.meta-info {
		font-size: 12px;
		color: #999;
		text-align: center;
		margin-bottom: 20px;

		.author {
			margin-right: 10px;
		}
	}

	.content {
		font-size: 16px;
		line-height: 1.8;
	}
}
</style>