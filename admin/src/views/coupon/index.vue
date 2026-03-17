<template>
  <div class="page-container">
    <div class="page-header">
      <h2 class="page-title">优惠券管理</h2>
      <el-button type="primary" @click="handleAdd">
        <el-icon><Plus /></el-icon>添加优惠券
      </el-button>
    </div>
    
    <div class="table-container">
      <el-table :data="tableData" style="width: 100%" v-loading="loading">
        <el-table-column prop="name" label="名称" min-width="150" />
        <el-table-column label="类型" width="100">
          <template #default="{ row }">
            <el-tag :type="getTypeTag(row.type)">{{ getTypeName(row.type) }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column label="优惠内容" min-width="120">
          <template #default="{ row }">
            <span v-if="row.type === 1">满{{ row.minAmount }}减{{ row.amount }}</span>
            <span v-else-if="row.type === 2">{{ row.discount }}折</span>
            <span v-else>减{{ row.amount }}元</span>
          </template>
        </el-table-column>
        <el-table-column label="发放/领取" width="100">
          <template #default="{ row }">{{ row.receivedCount }} / {{ row.totalCount }}</template>
        </el-table-column>
        <el-table-column label="有效期" min-width="200">
          <template #default="{ row }">{{ row.startTime }} 至 {{ row.endTime }}</template>
        </el-table-column>
        <el-table-column label="状态" width="100">
          <template #default="{ row }">
            <el-switch v-model="row.status" :active-value="1" :inactive-value="0" @change="handleStatusChange(row)" />
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleEdit(row)">编辑</el-button>
            <el-button type="danger" link @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
      
      <div class="pagination-container">
        <el-pagination v-model:current-page="pagination.page" v-model:page-size="pagination.size" :total="pagination.total" layout="total, sizes, prev, pager, next" @size-change="loadData" @current-change="loadData" />
      </div>
    </div>
    
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="600px" @close="resetForm">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入优惠券名称" />
        </el-form-item>
        <el-form-item label="类型" prop="type">
          <el-select v-model="form.type" placeholder="请选择类型">
            <el-option label="满减券" :value="1" />
            <el-option label="折扣券" :value="2" />
            <el-option label="无门槛券" :value="3" />
          </el-select>
        </el-form-item>
        <el-form-item v-if="form.type !== 2" label="优惠金额" prop="amount">
          <el-input-number v-model="form.amount" :min="0" :precision="2" />
        </el-form-item>
        <el-form-item v-if="form.type === 2" label="折扣比例" prop="discount">
          <el-input-number v-model="form.discount" :min="0.1" :max="0.99" :precision="2" :step="0.1" />
        </el-form-item>
        <el-form-item v-if="form.type === 1" label="最低消费" prop="minAmount">
          <el-input-number v-model="form.minAmount" :min="0" :precision="2" />
        </el-form-item>
        <el-form-item label="发放总量" prop="totalCount">
          <el-input-number v-model="form.totalCount" :min="1" />
        </el-form-item>
        <el-form-item label="每人限领" prop="limitCount">
          <el-input-number v-model="form.limitCount" :min="1" />
        </el-form-item>
        <el-form-item label="有效期">
          <el-date-picker v-model="dateRange" type="daterange" start-placeholder="开始日期" end-placeholder="结束日期" value-format="YYYY-MM-DD" />
        </el-form-item>
        <el-form-item label="状态">
          <el-radio-group v-model="form.status">
            <el-radio :label="1">启用</el-radio>
            <el-radio :label="0">禁用</el-radio>
          </el-radio-group>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="handleSubmit" :loading="submitLoading">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { couponApi } from '@/api'

const loading = ref(false)
const submitLoading = ref(false)
const tableData = ref([])
const dialogVisible = ref(false)
const dialogTitle = ref('添加优惠券')
const formRef = ref(null)
const dateRange = ref([])

const pagination = reactive({ page: 1, size: 10, total: 0 })
const form = reactive({ id: null, name: '', type: 1, amount: null, discount: null, minAmount: null, totalCount: 100, receivedCount: 0, limitCount: 1, startTime: '', endTime: '', status: 1 })
const rules = { name: [{ required: true, message: '请输入名称', trigger: 'blur' }], type: [{ required: true, message: '请选择类型', trigger: 'change' }] }

const getTypeName = (type) => ({ 1: '满减券', 2: '折扣券', 3: '无门槛券' }[type] || '未知')
const getTypeTag = (type) => ({ 1: 'success', 2: 'warning', 3: 'primary' }[type] || 'info')

const loadData = async () => {
  loading.value = true
  try {
    const res = await couponApi.page({ page: pagination.page, size: pagination.size })
    tableData.value = res.data?.records || []
    pagination.total = res.data?.total || 0
  } catch (error) { console.error(error) } finally { loading.value = false }
}

const handleAdd = () => { dialogTitle.value = '添加优惠券'; dialogVisible.value = true }
const handleEdit = (row) => { dialogTitle.value = '编辑优惠券'; Object.assign(form, row); if (row.startTime && row.endTime) dateRange.value = [row.startTime, row.endTime]; dialogVisible.value = true }

const handleDelete = (row) => {
  ElMessageBox.confirm('确定删除该优惠券吗？', '提示', { type: 'warning' }).then(async () => {
    await couponApi.delete(row.id)
    ElMessage.success('删除成功')
    loadData()
  }).catch(() => {})
}

const handleStatusChange = async (row) => {
  try { await couponApi.updateStatus(row.id, row.status); ElMessage.success('状态更新成功') }
  catch (error) { row.status = row.status === 1 ? 0 : 1 }
}

const handleSubmit = async () => {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  if (dateRange.value?.length === 2) { form.startTime = dateRange.value[0]; form.endTime = dateRange.value[1] }
  submitLoading.value = true
  try {
    if (form.id) { await couponApi.update(form); ElMessage.success('更新成功') }
    else { await couponApi.add(form); ElMessage.success('添加成功') }
    dialogVisible.value = false; loadData()
  } catch (error) { console.error(error) } finally { submitLoading.value = false }
}

const resetForm = () => {
  formRef.value?.resetFields()
  Object.assign(form, { id: null, name: '', type: 1, amount: null, discount: null, minAmount: null, totalCount: 100, receivedCount: 0, limitCount: 1, startTime: '', endTime: '', status: 1 })
  dateRange.value = []
}

onMounted(() => loadData())
</script>
