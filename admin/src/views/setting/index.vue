<template>
  <div class="page-container">
    <div class="page-header">
      <h2 class="page-title">系统设置</h2>
    </div>
    
    <el-tabs v-model="activeTab">
      <el-tab-pane label="基础设置" name="basic">
        <el-form ref="basicFormRef" :model="basicForm" label-width="120px" class="setting-form">
          <el-form-item label="商城名称">
            <el-input v-model="basicForm.shopName" placeholder="请输入商城名称" />
          </el-form-item>
          <el-form-item label="商城Logo">
            <el-input v-model="basicForm.shopLogo" placeholder="请输入Logo图片URL" />
          </el-form-item>
          <el-form-item label="客服电话">
            <el-input v-model="basicForm.servicePhone" placeholder="请输入客服电话" />
          </el-form-item>
          <el-form-item label="商家电话">
            <el-input v-model="basicForm.merchantPhone" placeholder="请输入商家电话" />
          </el-form-item>
          <el-form-item label="营业时间">
            <el-input v-model="basicForm.businessHours" placeholder="如：08:00-20:00" />
          </el-form-item>
          <el-form-item label="配送范围">
            <el-input-number v-model="basicForm.deliveryRange" :min="0" /> 公里
          </el-form-item>
          <el-form-item label="起送金额">
            <el-input-number v-model="basicForm.minOrderAmount" :min="0" :precision="2" /> 元
          </el-form-item>
          <el-form-item label="配送费">
            <el-input-number v-model="basicForm.deliveryFee" :min="0" :precision="2" /> 元
          </el-form-item>
          <el-form-item label="免配送费金额">
            <el-input-number v-model="basicForm.freeDeliveryAmount" :min="0" :precision="2" /> 元
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="saveBasic" :loading="loading">保存设置</el-button>
          </el-form-item>
        </el-form>
      </el-tab-pane>
      
      <el-tab-pane label="配送设置" name="delivery">
        <el-form ref="deliveryFormRef" :model="deliveryForm" label-width="120px" class="setting-form">
          <el-form-item label="支持自提">
            <el-switch v-model="deliveryForm.supportPickup" />
          </el-form-item>
          <el-form-item label="支持配送">
            <el-switch v-model="deliveryForm.supportDelivery" />
          </el-form-item>
          <el-form-item label="自提地址">
            <el-input v-model="deliveryForm.pickupAddress" type="textarea" :rows="2" placeholder="请输入自提地址" />
          </el-form-item>
          <el-form-item label="配送时间">
            <el-input v-model="deliveryForm.deliveryTime" placeholder="如：每日 08:00-20:00" />
          </el-form-item>
          <el-form-item label="配送说明">
            <el-input v-model="deliveryForm.deliveryNote" type="textarea" :rows="3" placeholder="请输入配送说明" />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="saveDelivery" :loading="loading">保存设置</el-button>
          </el-form-item>
        </el-form>
      </el-tab-pane>
      
      <el-tab-pane label="支付设置" name="payment">
        <el-form ref="paymentFormRef" :model="paymentForm" label-width="120px" class="setting-form">
          <el-form-item label="微信支付">
            <el-switch v-model="paymentForm.wechatPayEnabled" />
          </el-form-item>
          <el-form-item label="AppID">
            <el-input v-model="paymentForm.wechatAppId" placeholder="请输入微信AppID" />
          </el-form-item>
          <el-form-item label="商户号">
            <el-input v-model="paymentForm.wechatMchId" placeholder="请输入商户号" />
          </el-form-item>
          <el-form-item label="支付密钥">
            <el-input v-model="paymentForm.wechatPayKey" type="password" placeholder="请输入支付密钥" show-password />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="savePayment" :loading="loading">保存设置</el-button>
          </el-form-item>
        </el-form>
      </el-tab-pane>
      
      <el-tab-pane label="其他设置" name="other">
        <el-form ref="otherFormRef" :model="otherForm" label-width="120px" class="setting-form">
          <el-form-item label="用户协议">
            <el-input v-model="otherForm.userAgreement" type="textarea" :rows="5" placeholder="请输入用户协议" />
          </el-form-item>
          <el-form-item label="隐私政策">
            <el-input v-model="otherForm.privacyPolicy" type="textarea" :rows="5" placeholder="请输入隐私政策" />
          </el-form-item>
          <el-form-item label="关于我们">
            <el-input v-model="otherForm.aboutUs" type="textarea" :rows="5" placeholder="请输入关于我们" />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="saveOther" :loading="loading">保存设置</el-button>
          </el-form-item>
        </el-form>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { settingApi } from '@/api'

const loading = ref(false)
const activeTab = ref('basic')

const basicForm = reactive({
  shopName: '',
  shopLogo: '',
  servicePhone: '',
  merchantPhone: '',
  businessHours: '',
  deliveryRange: 5,
  minOrderAmount: 20,
  deliveryFee: 5,
  freeDeliveryAmount: 59
})

const deliveryForm = reactive({
  supportPickup: true,
  supportDelivery: true,
  pickupAddress: '',
  deliveryTime: '',
  deliveryNote: ''
})

const paymentForm = reactive({
  wechatPayEnabled: true,
  wechatAppId: '',
  wechatMchId: '',
  wechatPayKey: ''
})

const otherForm = reactive({
  userAgreement: '',
  privacyPolicy: '',
  aboutUs: ''
})

const loadSettings = async () => {
  try {
    const res = await settingApi.get()
    if (res.data) {
      Object.assign(basicForm, res.data.basic || {})
      Object.assign(deliveryForm, res.data.delivery || {})
      Object.assign(paymentForm, res.data.payment || {})
      Object.assign(otherForm, res.data.other || {})
    }
  } catch (error) { console.error(error) }
}

const saveBasic = async () => {
  loading.value = true
  try { await settingApi.update({ type: 'basic', data: basicForm }); ElMessage.success('保存成功') }
  catch (error) { console.error(error) } finally { loading.value = false }
}

const saveDelivery = async () => {
  loading.value = true
  try { await settingApi.update({ type: 'delivery', data: deliveryForm }); ElMessage.success('保存成功') }
  catch (error) { console.error(error) } finally { loading.value = false }
}

const savePayment = async () => {
  loading.value = true
  try { await settingApi.update({ type: 'payment', data: paymentForm }); ElMessage.success('保存成功') }
  catch (error) { console.error(error) } finally { loading.value = false }
}

const saveOther = async () => {
  loading.value = true
  try { await settingApi.update({ type: 'other', data: otherForm }); ElMessage.success('保存成功') }
  catch (error) { console.error(error) } finally { loading.value = false }
}

onMounted(() => loadSettings())
</script>

<style lang="scss" scoped>
.setting-form {
  max-width: 600px;
  padding: 20px;
  background: #fff;
  border-radius: 8px;
}
</style>
