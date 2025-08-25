<template>
    <view class="visitor-container">
        <form @submit="submitForm">
            <view class="form-section">
                <view class="form-item">
                    <text class="item-label">访客姓名</text>
                    <input v-model="formData.visitorName" placeholder="请输入访客姓名" />
                </view>
                <view class="form-item">
                    <text class="item-label">手机号码</text>
                    <input v-model="formData.visitorPhone" type="number" placeholder="请输入手机号码" />
                </view>
                <view class="form-item">
                    <text class="item-label">身份证号</text>
                    <input v-model="formData.idCard" placeholder="请输入身份证号" />
                </view>
                <view class="form-item">
                    <text class="item-label">来访日期</text>
                    <picker mode="date" :value="formData.visitDate" @change="bindDateChange">
                        <view class="picker">
                            {{formData.visitDate}}
                        </view>
                    </picker>
                </view>
                <view class="form-item">
                    <text class="item-label">车牌号</text>
                    <input v-model="formData.licensePlate" placeholder="请输入车牌号（选填）" />
                </view>
                <view class="form-item">
                    <text class="item-label">来访事由</text>
                    <textarea v-model="formData.reason" placeholder="请输入来访事由" />
                </view>
            </view>
            <button class="submit-btn" form-type="submit">提交登记</button>
        </form>
    </view>
</template>

<script>
import { addVisitor } from '@/api/visitor'
export default {
    data() {
        return {
            formData: {
                visitorName: '',
                visitorPhone: '',
                idCard: '',
                visitDate: new Date().toISOString().slice(0, 10),
                reason: '',
                licensePlate: ''
            }
        }
    },
    methods: {
        bindDateChange(e) {
            this.formData.visitDate = e.target.value
        },
        submitForm() {
            if (!this.formData.visitorName) {
                uni.showToast({ title: '请输入访客姓名', icon: 'none' });
                return;
            }
            if (!this.formData.visitorPhone) {
                uni.showToast({ title: '请输入手机号码', icon: 'none' });
                return;
            }
            if (!this.formData.idCard) {
                uni.showToast({ title: '请输入身份证号', icon: 'none' });
                return;
            }
            if (!this.formData.reason) {
                uni.showToast({ title: '请输入来访事由', icon: 'none' });
                return;
            }

            uni.showLoading({ title: '提交中...' })
            addVisitor(this.formData).then(res => {
                uni.hideLoading();
                uni.showToast({ title: '登记成功', icon: 'success' });
                setTimeout(() => {
                    uni.navigateBack();
                }, 1500);
            }).catch(err => {
                uni.hideLoading();
                uni.showToast({ title: '登记失败', icon: 'none' });
            })
        }
    }
}
</script>

<style lang="scss" scoped>
.visitor-container {
    padding: 20rpx;
}
.form-section {
    background-color: #fff;
    border-radius: 16rpx;
    padding: 0 30rpx;
    margin-bottom: 20rpx;
}
.form-item {
    display: flex;
    align-items: center;
    padding: 30rpx 0;
    border-bottom: 1rpx solid #f0f0f0;
    .item-label {
        width: 180rpx;
    }
    input, textarea {
        flex: 1;
    }
    .picker {
        flex: 1;
    }
}
.submit-btn {
    background-color: #1890ff;
    color: #fff;
    border-radius: 50rpx;
    margin-top: 50rpx;
}
</style>
