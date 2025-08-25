<template>
    <view class="warranty-container">
        <form @submit="submitForm">
            <view class="form-section">
                <view class="form-item">
                    <text class="item-label">设备名称</text>
                    <input v-model="formData.equipmentName" placeholder="请输入设备名称" />
                </view>
                <view class="form-item">
                    <text class="item-label">设备位置</text>
                    <input v-model="formData.equipmentLocation" placeholder="请输入设备位置" />
                </view>
                <view class="form-item">
                    <text class="item-label">问题描述</text>
                    <textarea v-model="formData.description" placeholder="请详细描述问题" />
                </view>
                <view class="form-item">
                    <text class="item-label">您的姓名</text>
                    <input v-model="formData.reporterName" placeholder="请输入您的姓名" />
                </view>
                <view class="form-item">
                    <text class="item-label">联系电话</text>
                    <input v-model="formData.reporterPhone" type="number" placeholder="请输入您的联系电话" />
                </view>
            </view>
            <button class="submit-btn" form-type="submit">提交报修</button>
        </form>
    </view>
</template>

<script>
import { addWarranty } from '@/api/warranty'
export default {
    data() {
        return {
            formData: {
                equipmentName: '',
                equipmentLocation: '',
                description: '',
                reporterName: '',
                reporterPhone: ''
            }
        }
    },
    methods: {
        submitForm() {
            if (!this.formData.equipmentName) {
                uni.showToast({ title: '请输入设备名称', icon: 'none' });
                return;
            }
            if (!this.formData.equipmentLocation) {
                uni.showToast({ title: '请输入设备位置', icon: 'none' });
                return;
            }
            if (!this.formData.description) {
                uni.showToast({ title: '请详细描述问题', icon: 'none' });
                return;
            }
            if (!this.formData.reporterName) {
                uni.showToast({ title: '请输入您的姓名', icon: 'none' });
                return;
            }
            if (!this.formData.reporterPhone) {
                uni.showToast({ title: '请输入您的联系电话', icon: 'none' });
                return;
            }

            uni.showLoading({ title: '提交中...' })
            addWarranty(this.formData).then(res => {
                uni.hideLoading();
                uni.showToast({ title: '报修成功', icon: 'success' });
                setTimeout(() => {
                    uni.navigateBack();
                }, 1500);
            }).catch(err => {
                uni.hideLoading();
                uni.showToast({ title: '报修失败', icon: 'none' });
            })
        }
    }
}
</script>

<style lang="scss" scoped>
.warranty-container {
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
}
.submit-btn {
    background-color: #1890ff;
    color: #fff;
    border-radius: 50rpx;
    margin-top: 50rpx;
}
</style>
