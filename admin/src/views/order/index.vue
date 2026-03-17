<template>
  <div class="page-container">
    <div class="page-header">
      <h2 class="page-title">订单管理</h2>
    </div>
    
    <!-- 搜索表单 -->
    <div class="search-form">
      <el-form :inline="true" :model="searchForm">
        <el-form-item label="订单号">
          <el-input v-model="searchForm.orderNo" placeholder="请输入订单号" clearable />
        </el-form-item>
        <el-form-item label="状态">
          <el-select v-model="searchForm.status" placeholder="请选择状态" clearable>
            <el-option label="待付款" :value="0" />
            <el-option label="待配送" :value="1" />
            <el-option label="待收货" :value="2" />
            <el-option label="已完成" :value="3" />
            <el-option label="已取消" :value="4" />
          </el-select>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSearch">搜索</el-button>
          <el-button @click="handleReset">重置</el-button>
        </el-form-item>
      </el-form>
    </div>
    
    <!-- 表格 -->
    <div class="table-container">
      <el-table :data="tableData" style="width: 100%" v-loading="loading">
        <el-table-column label="订单号" min-width="180">
          <template #default="{ row }">
            <el-link type="primary" @click="handleDetail(row)">{{ row.orderNo }}</el-link>
          </template>
        </el-table-column>
        <el-table-column label="收货人" min-width="120">
          <template #default="{ row }">
            <div>{{ row.receiverName }}</div>
            <div class="text-gray">{{ row.receiverPhone }}</div>
          </template>
        </el-table-column>
        <el-table-column label="商品数量" width="100">
          <template #default="{ row }">
            {{ row.goodsCount }}件
          </template>
        </el-table-column>
        <el-table-column label="金额" width="120">
          <template #default="{ row }">
            <span class="price">¥{{ row.payAmount }}</span>
          </template>
        </el-table-column>
        <el-table-column label="配送方式" width="100">
          <template #default="{ row }">
            <el-tag :type="row.deliveryMethod === 'pickup' ? 'success' : 'primary'" size="small">
              {{ row.deliveryMethod === 'pickup' ? '自提' : '配送' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">
              {{ getStatusText(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="下单时间" min-width="160" />
        <el-table-column label="操作" width="200" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleDetail(row)">详情</el-button>
            <el-button 
              v-if="row.status === 1" 
              type="success" 
              link 
              @click="handleDeliver(row)"
            >发货</el-button>
            <el-button 
              v-if="row.status === 0" 
              type="warning" 
              link 
              @click="handleCancel(row)"
            >取消</el-button>
          </template>
        </el-table-column>
      </el-table>
      
      <!-- 分页 -->
      <div class="pagination-container">
        <el-pagination
          v-model:current-page="pagination.page"
          v-model:page-size="pagination.size"
          :page-sizes="[10, 20, 50, 100]"
          :total="pagination.total"
          layout="total, sizes, prev, pager, next, jumper"
          @size-change="loadData"
          @current-change="loadData"
        />
      </div>
    </div>
    
    <!-- 订单详情对话框 -->
    <el-dialog
      v-model="detailVisible"
      title="订单详情"
      width="700px"
    >
      <div v-if="orderDetail" class="order-detail">
        <el-descriptions :column="2" border>
          <el-descriptions-item label="订单号">{{ orderDetail.orderNo }}</el-descriptions-item>
          <el-descriptions-item label="订单状态">
            <el-tag :type="getStatusType(orderDetail.status)">
              {{ getStatusText(orderDetail.status) }}
            </el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="配送方式">
            {{ orderDetail.deliveryMethod === 'pickup' ? '自提' : '配送' }}
          </el-descriptions-item>
          <el-descriptions-item label="支付金额">
            <span class="price">¥{{ orderDetail.payAmount }}</span>
          </el-descriptions-item>
          <el-descriptions-item label="下单时间">{{ orderDetail.createTime }}</el-descriptions-item>
          <el-descriptions-item label="支付时间">{{ orderDetail.payTime || '-' }}</el-descriptions-item>
          <el-descriptions-item label="配送时间">{{ orderDetail.deliveryTime || '-' }}</el-descriptions-item>
          <el-descriptions-item label="完成时间">{{ orderDetail.completeTime || '-' }}</el-descriptions-item>
        </el-descriptions>
        
        <div v-if="orderDetail.deliveryMethod === 'delivery'" class="section">
          <h4>收货信息</h4>
          <el-descriptions :column="1" border>
            <el-descriptions-item label="收货人">{{ orderDetail.receiverName }}</el-descriptions-item>
            <el-descriptions-item label="联系电话">{{ orderDetail.receiverPhone }}</el-descriptions-item>
            <el-descriptions-item label="收货地址">{{ orderDetail.receiverAddress }}</el-descriptions-item>
          </el-descriptions>
        </div>
        
        <div class="section">
          <h4>商品信息</h4>
          <el-table :data="orderDetail.items" size="small">
            <el-table-column label="商品" min-width="200">
              <template #default="{ row }">
                <div class="goods-info">
                  <el-image :src="row.goodsImage" class="goods-image" />
                  <span>{{ row.goodsName }}</span>
                </div>
              </template>
            </el-table-column>
            <el-table-column prop="specification" label="规格" width="100" />
            <el-table-column label="单价" width="100">
              <template #default="{ row }">¥{{ row.price }}</template>
            </el-table-column>
            <el-table-column prop="quantity" label="数量" width="80" />
            <el-table-column label="小计" width="100">
              <template #default="{ row }">¥{{ row.amount }}</template>
            </el-table-column>
          </el-table>
        </div>
        
        <div v-if="orderDetail.remark" class="section">
          <h4>订单备注</h4>
          <p>{{ orderDetail.remark }}</p>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { orderApi } from '@/api'

const route = useRoute()
const loading = ref(false)
const tableData = ref([])
const detailVisible = ref(false)
const orderDetail = ref(null)

const searchForm = reactive({
  orderNo: '',
  status: null
})

const pagination = reactive({
  page: 1,
  size: 10,
  total: 0
})

// 获取状态类型
const getStatusType = (status) => {
  const types = { 0: 'warning', 1: 'primary', 2: 'success', 3: 'info', 4: 'danger' }
  return types[status] || 'info'
}

// 获取状态文本
const getStatusText = (status) => {
  const texts = { 0: '待付款', 1: '待配送', 2: '待收货', 3: '已完成', 4: '已取消' }
  return texts[status] || '未知'
}

// 加载数据
const loadData = async () => {
  loading.value = true
  try {
    const res = await orderApi.page({
      ...searchForm,
      page: pagination.page,
      size: pagination.size
    })
    tableData.value = res.data?.records || []
    pagination.total = res.data?.total || 0
  } catch (error) {
    console.error('加载数据失败:', error)
  } finally {
    loading.value = false
  }
}

// 搜索
const handleSearch = () => {
  pagination.page = 1
  loadData()
}

// 重置
const handleReset = () => {
  Object.assign(searchForm, { orderNo: '', status: null })
  handleSearch()
}

// 查看详情
const handleDetail = async (row) => {
  try {
    const res = await orderApi.detail(row.id)
    orderDetail.value = res.data
    detailVisible.value = true
  } catch (error) {
    console.error('获取详情失败:', error)
  }
}

// 发货
const handleDeliver = (row) => {
  ElMessageBox.confirm('确定要发货吗？', '提示', {
    type: 'warning'
  }).then(async () => {
    try {
      await orderApi.deliver(row.id)
      ElMessage.success('发货成功')
      loadData()
    } catch (error) {
      console.error('发货失败:', error)
    }
  })
}

// 取消订单
const handleCancel = (row) => {
  ElMessageBox.confirm('确定要取消该订单吗？', '提示', {
    type: 'warning'
  }).then(async () => {
    try {
      await orderApi.cancel(row.id)
      ElMessage.success('取消成功')
      loadData()
    } catch (error) {
      console.error('取消失败:', error)
    }
  })
}

onMounted(() => {
  loadData()
  
  // 检查URL参数，如果有订单ID则自动打开详情
  const orderId = route.query.id
  if (orderId) {
    handleDetail({ id: orderId })
  }
})
</script>

<style lang="scss" scoped>
.text-gray {
  color: #909399;
  font-size: 12px;
}

.price {
  color: #f56c6c;
  font-weight: bold;
}

:deep(.el-link) {
  text-decoration: none;
}

// 修复 el-select 输入框文字显示问题
:deep(.el-select) {
  width: 180px;
  
  .el-select__wrapper {
    min-width: 100%;
  }
  
  .el-input__wrapper {
    .el-input__inner {
      color: #606266 !important;
    }
  }
  
  .el-input__inner::placeholder {
    color: #a8abb2 !important;
  }
  
  .el-select__placeholder,
  .el-select__selected-item {
    overflow: visible !important;
    text-overflow: unset !important;
  }
}

.order-detail {
  .section {
    margin-top: 20px;
    
    h4 {
      margin-bottom: 10px;
      color: #303133;
    }
  }
  
  .goods-info {
    display: flex;
    align-items: center;
    gap: 10px;
    
    .goods-image {
      width: 40px;
      height: 40px;
      border-radius: 4px;
    }
  }
}
</style>
