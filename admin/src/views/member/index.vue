<template>
  <div class="page-container">
    <div class="page-header">
      <h2 class="page-title">会员管理</h2>
    </div>
    
    <div class="search-form">
      <el-form :inline="true" :model="searchForm">
        <el-form-item label="昵称">
          <el-input v-model="searchForm.nickname" placeholder="请输入昵称" clearable />
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="searchForm.phone" placeholder="请输入手机号" clearable />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">搜索</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </div>
    
    <div class="table-container">
      <el-table :data="tableData" style="width: 100%" v-loading="loading">
        <el-table-column label="头像" width="80">
          <template #default="{ row }">
            <el-avatar :src="row.avatar" :size="40" icon="UserFilled" />
          </template>
        </el-table-column>
        <el-table-column prop="nickname" label="昵称" min-width="120" />
        <el-table-column prop="phone" label="手机号" width="120" />
        <el-table-column label="性别" width="80">
          <template #default="{ row }">
            {{ row.gender === 1 ? '男' : row.gender === 2 ? '女' : '未知' }}
          </template>
        </el-table-column>
        <el-table-column prop="level" label="等级" width="80" />
        <el-table-column label="余额" width="100">
          <template #default="{ row }">¥{{ row.balance }}</template>
        </el-table-column>
        <el-table-column prop="points" label="积分" width="80" />
        <el-table-column prop="createTime" label="注册时间" min-width="160" />
        <el-table-column label="操作" width="100" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleDetail(row)">详情</el-button>
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
    
    <el-dialog v-model="detailVisible" title="会员详情" width="600px">
      <el-descriptions v-if="memberDetail" :column="2" border>
        <el-descriptions-item label="头像">
          <el-avatar :src="memberDetail.avatar" :size="50" />
        </el-descriptions-item>
        <el-descriptions-item label="昵称">{{ memberDetail.nickname }}</el-descriptions-item>
        <el-descriptions-item label="手机号">{{ memberDetail.phone || '-' }}</el-descriptions-item>
        <el-descriptions-item label="性别">
          {{ memberDetail.gender === 1 ? '男' : memberDetail.gender === 2 ? '女' : '未知' }}
        </el-descriptions-item>
        <el-descriptions-item label="等级">{{ memberDetail.level }}</el-descriptions-item>
        <el-descriptions-item label="余额">¥{{ memberDetail.balance }}</el-descriptions-item>
        <el-descriptions-item label="积分">{{ memberDetail.points }}</el-descriptions-item>
        <el-descriptions-item label="注册时间">{{ memberDetail.createTime }}</el-descriptions-item>
      </el-descriptions>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { memberApi } from '@/api'

const loading = ref(false)
const tableData = ref([])
const detailVisible = ref(false)
const memberDetail = ref(null)

const searchForm = reactive({ nickname: '', phone: '' })
const pagination = reactive({ page: 1, size: 10, total: 0 })

const loadData = async () => {
  loading.value = true
  try {
    const res = await memberApi.page({ ...searchForm, ...pagination })
    tableData.value = res.data?.records || []
    pagination.total = res.data?.total || 0
  } catch (error) {
    console.error(error)
  } finally {
    loading.value = false
  }
}

const handleSearch = () => { pagination.page = 1; loadData() }
const handleReset = () => { Object.assign(searchForm, { nickname: '', phone: '' }); handleSearch() }

const handleDetail = async (row) => {
  try {
    const res = await memberApi.detail(row.id)
    memberDetail.value = res.data
    detailVisible.value = true
  } catch (error) {
    console.error(error)
  }
}

onMounted(() => loadData())
</script>
