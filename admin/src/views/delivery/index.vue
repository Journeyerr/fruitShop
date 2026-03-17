<template>
  <div class="page-container">
    <div class="page-header">
      <h2 class="page-title">配送地址管理</h2>
      <el-button type="primary" @click="handleAdd">
        <el-icon><Plus /></el-icon>
        添加配送范围
      </el-button>
    </div>
    
    <div class="table-container">
      <el-table :data="tableData" style="width: 100%" v-loading="loading">
        <el-table-column type="index" label="序号" width="60" />
        <el-table-column prop="name" label="配送区域名称" min-width="150" />
        <el-table-column prop="range" label="配送范围(km)" width="120" />
        <el-table-column prop="fee" label="配送费" width="100">
          <template #default="{ row }">¥{{ row.fee }}</template>
        </el-table-column>
        <el-table-column prop="minAmount" label="起送价" width="100">
          <template #default="{ row }">¥{{ row.minAmount }}</template>
        </el-table-column>
        <el-table-column prop="freeAmount" label="免配送费金额" width="120">
          <template #default="{ row }">¥{{ row.freeAmount || '-' }}</template>
        </el-table-column>
        <el-table-column label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'">
              {{ row.status === 1 ? '启用' : '禁用' }}
            </el-tag>
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
        <el-pagination
          v-model:current-page="pagination.page"
          v-model:page-size="pagination.size"
          :total="pagination.total"
          layout="total, sizes, prev, pager, next"
          @size-change="loadData"
          @current-change="loadData"
        />
      </div>
    </div>
    
    <el-dialog v-model="dialogVisible" :title="dialogTitle" width="500px" @close="resetForm">
      <el-form ref="formRef" :model="form" :rules="rules" label-width="120px">
        <el-form-item label="区域名称" prop="name">
          <el-input v-model="form.name" placeholder="请输入配送区域名称" />
        </el-form-item>
        <el-form-item label="配送范围" prop="range">
          <el-input-number v-model="form.range" :min="0" :precision="1" /> km
        </el-form-item>
        <el-form-item label="配送费" prop="fee">
          <el-input-number v-model="form.fee" :min="0" :precision="2" /> 元
        </el-form-item>
        <el-form-item label="起送价" prop="minAmount">
          <el-input-number v-model="form.minAmount" :min="0" :precision="2" /> 元
        </el-form-item>
        <el-form-item label="免配送费金额">
          <el-input-number v-model="form.freeAmount" :min="0" :precision="2" /> 元
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
import { deliveryApi } from '@/api'

const loading = ref(false)
const submitLoading = ref(false)
const tableData = ref([])
const dialogVisible = ref(false)
const dialogTitle = ref('添加配送范围')
const formRef = ref(null)

const pagination = reactive({ page: 1, size: 10, total: 0 })
const form = reactive({ id: null, name: '', range: 3, fee: 5, minAmount: 20, freeAmount: 59, status: 1 })
const rules = { name: [{ required: true, message: '请输入区域名称', trigger: 'blur' }] }

const loadData = async () => {
  loading.value = true
  try {
    const res = await deliveryApi.page({ page: pagination.page, size: pagination.size })
    tableData.value = res.data?.records || []
    pagination.total = res.data?.total || 0
  } catch (error) { console.error(error) } finally { loading.value = false }
}

const handleAdd = () => { dialogTitle.value = '添加配送范围'; dialogVisible.value = true }
const handleEdit = (row) => { dialogTitle.value = '编辑配送范围'; Object.assign(form, row); dialogVisible.value = true }

const handleDelete = (row) => {
  ElMessageBox.confirm('确定删除该配送范围吗？', '提示', { type: 'warning' }).then(async () => {
    await deliveryApi.delete(row.id)
    ElMessage.success('删除成功')
    loadData()
  }).catch(() => {})
}

const handleSubmit = async () => {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  submitLoading.value = true
  try {
    if (form.id) { await deliveryApi.update(form); ElMessage.success('更新成功') }
    else { await deliveryApi.add(form); ElMessage.success('添加成功') }
    dialogVisible.value = false; loadData()
  } catch (error) { console.error(error) } finally { submitLoading.value = false }
}

const resetForm = () => {
  formRef.value?.resetFields()
  Object.assign(form, { id: null, name: '', range: 3, fee: 5, minAmount: 20, freeAmount: 59, status: 1 })
}

onMounted(() => loadData())
</script>
