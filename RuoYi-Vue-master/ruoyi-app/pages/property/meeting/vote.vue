<template>
	<view class="container">
		<view class="meeting-info">
			<uni-card :title="meeting.title" :sub-title="meeting.meetingTime" :extra="'状态: ' + meeting.status">
				<view classs="content">
					<text>{{ meeting.content }}</text>
				</view>
			</uni-card>
		</view>

		<view class="topic-list">
			<uni-card v-for="(topic, index) in topics" :key="topic.id" :title="'议题' + (index + 1) + ': ' + topic.title">
				<view class="topic-content">
					<text>{{ topic.content }}</text>
				</view>
				<view class="vote-section">
					<view class="vote-buttons">
						<button v-for="option in voteOptions" :key="option.value"
							@click="handleVote(topic, option)"
							:class="['vote-button', topic.voteResult === option.value ? 'selected' : '']"
							:type="topic.voteResult === option.value ? 'primary' : 'default'">
							{{ option.text }}
						</button>
					</view>
				</view>
			</uni-card>
		</view>
		<view v-if="!topics.length" class="empty-text">
			暂无议题
		</view>
	</view>
</template>

<script>
	import { getMeeting, addMeetingVote } from "@/api/property/meeting";

	export default {
		data() {
			return {
				meeting: {},
				topics: [],
				voteOptions: [
					{ text: '同意', value: 2 },
					{ text: '反对', value: 3 },
					{ text: '弃权', value: 4 }
				]
			};
		},
		onLoad(options) {
			const meetingId = options.id;
			this.getMeeting(meetingId);
		},
		methods: {
			getMeeting(meetingId) {
				getMeeting(meetingId).then(response => {
					this.meeting = response.data;
					this.topics = response.data.topics.map(topic => {
						// voteResult: 1=未投票, 2=同意, 3=反对, 4=弃权
						if (!topic.voteResult) {
							topic.voteResult = 1; 
						}
						return topic;
					});
				});
			},
			handleVote(topic, option) {
				if (topic.voteResult === option.value) {
					// 如果点击的是当前已选中的选项，则不执行任何操作
					return;
				}
				
				uni.showModal({
					title: '提示',
					content: `您确定要投"${option.text}"票吗？`,
					success: (res) => {
						if (res.confirm) {
							// 乐观更新UI
							const originalVote = topic.voteResult;
							topic.voteResult = option.value;

							// 准备要提交的数据
							const voteData = {
								id: topic.id,
								meetingId: topic.meetingId,
								title: topic.title,
								content: topic.content,
								voteResult: option.value
							};
							
							submitVote([voteData]).then(response => {
								this.$modal.msgSuccess("投票成功");
								// 可以在此刷新数据以获取最新状态, 但乐观更新已处理UI, 通常不需要
								// this.getMeeting(this.meeting.id); 
							}).catch(() => {
								// 如果API调用失败，则回滚UI
								topic.voteResult = originalVote;
								this.$modal.msgError("投票失败，请重试");
							});
						}
					}
				});
			}
		}
	};
</script>

<style lang="scss" scoped>
	.container {
		padding: 10px;
		background-color: #f5f5f5;
	}

	.meeting-info {
		margin-bottom: 10px;
	}

	.topic-list {
		.uni-card {
			margin-bottom: 10px;
		}
	}
	
	.topic-content {
		margin-bottom: 15px;
		color: #666;
		font-size: 14px;
	}

	.vote-section {
		margin-top: 10px;
	}

	.vote-buttons {
		display: flex;
		justify-content: space-around;
		width: 100%;
	}

	.vote-button {
		flex: 1;
		margin: 0 5px;
		font-size: 14px;
	}
	
	.vote-button[type="default"] {
		background-color: #f8f8f8;
		color: #333;
	}
	
	.empty-text {
		text-align: center;
		margin-top: 20px;
		color: #999;
	}
</style>
