<template>
  <div class="page-container dashboard">
    <div class="page-header">
      <h2 class="page-title">首页概览</h2>
    </div>
    
    <!-- 统计卡片 -->
    <div class="stat-grid">
      <div class="stat-card">
        <div class="stat-icon" style="background: #409eff;">
          <el-icon><Goods /></el-icon>
        </div>
        <div class="stat-info">
          <div class="stat-value">{{ statistics.goodsCount || 0 }}</div>
          <div class="stat-label">商品总数</div>
        </div>
      </div>
      
      <div class="stat-card">
        <div class="stat-icon" style="background: #67c23a;">
          <el-icon><List /></el-icon>
        </div>
        <div class="stat-info">
          <div class="stat-value">{{ statistics.orderCount || 0 }}</div>
          <div class="stat-label">订单总数</div>
        </div>
      </div>
      
      <div class="stat-card">
        <div class="stat-icon" style="background: #e6a23c;">
          <el-icon><User /></el-icon>
        </div>
        <div class="stat-info">
          <div class="stat-value">{{ statistics.memberCount || 0 }}</div>
          <div class="stat-label">会员总数</div>
        </div>
      </div>
      
      <div class="stat-card">
        <div class="stat-icon" style="background: #f56c6c;">
          <el-icon><Money /></el-icon>
        </div>
        <div class="stat-info">
          <div class="stat-value">¥{{ statistics.totalSales || 0 }}</div>
          <div class="stat-label">销售总额</div>
        </div>
      </div>
    </div>
    
    <!-- 待处理订单 -->
    <div class="section-card">
      <div class="section-header">
        <h3>待处理订单</h3>
        <el-button type="primary" link @click="goToOrder">查看全部</el-button>
      </div>
      <el-table :data="pendingOrders" style="width: 100%">
        <el-table-column label="订单编号" min-width="150">
          <template #default="{ row }">
            <el-link type="primary" @click="viewOrderDetail(row)">{{ row.orderNo }}</el-link>
          </template>
        </el-table-column>
        <el-table-column prop="receiverName" label="收货人" min-width="80" />
        <el-table-column prop="payAmount" label="金额" min-width="100">
          <template #default="{ row }">
            ¥{{ row.payAmount }}
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" min-width="80">
          <template #default="{ row }">
            <el-tag :type="getStatusType(row.status)">
              {{ getStatusText(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="下单时间" min-width="160" />
      </el-table>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { statisticsApi, orderApi } from '@/api'

const router = useRouter()

const statistics = ref({
  goodsCount: 0,
  orderCount: 0,
  memberCount: 0,
  totalSales: 0
})

const pendingOrders = ref([])

// 获取统计数据
const loadStatistics = async () => {
  try {
    const res = await statisticsApi.overview()
    if (res.data) {
      statistics.value = res.data
    }
  } catch (error) {
    console.error('获取统计数据失败:', error)
  }
}

// 获取待处理订单
const loadPendingOrders = async () => {
  try {
    const res = await orderApi.page({ page: 1, size: 5, status: 1 })
    if (res.data) {
      pendingOrders.value = res.data.records || []
    }
  } catch (error) {
    console.error('获取待处理订单失败:', error)
  }
}

// 获取状态类型
const getStatusType = (status) => {
  const types = {
    0: 'warning',
    1: 'primary',
    2: 'success',
    3: 'info',
    4: 'danger'
  }
  return types[status] || 'info'
}

// 获取状态文本
const getStatusText = (status) => {
  const texts = {
    0: '待付款',
    1: '待配送',
    2: '待收货',
    3: '已完成',
    4: '已取消'
  }
  return texts[status] || '未知'
}

// 跳转到订单页面
const goToOrder = () => {
  router.push('/order')
}

// 查看订单详情
const viewOrderDetail = (row) => {
  router.push(`/order?id=${row.id}`)
}

onMounted(() => {
  loadStatistics()
  loadPendingOrders()
})
</script>

<style lang="scss" scoped>
.dashboard {
  :deep(.el-link) {
    text-decoration: none;
  }
  
  .stat-grid {
    display: grid;
    grid-template-columns: repeat(4, 1fr);
    gap: 20px;
    margin-bottom: 20px;
    
    @media (max-width: 1024px) {
      grid-template-columns: repeat(2, 1fr);
    }
    
    @media (max-width: 480px) {
      grid-template-columns: 1fr;
    }
  }
  
  .stat-card {
    background: #fff;
    border-radius: 8px;
    padding: 20px;
    display: flex;
    align-items: center;
    
    .stat-icon {
      width: 60px;
      height: 60px;
      border-radius: 8px;
      display: flex;
      align-items: center;
      justify-content: center;
      color: #fff;
      font-size: 28px;
      margin-right: 15px;
      
      @media (max-width: 480px) {
        width: 50px;
        height: 50px;
        font-size: 24px;
      }
    }
    
    .stat-info {
      flex: 1;
      
      .stat-value {
        font-size: 24px;
        font-weight: bold;
        color: #303133;
        
        @media (max-width: 480px) {
          font-size: 20px;
        }
      }
      
      .stat-label {
        font-size: 14px;
        color: #909399;
        margin-top: 5px;
      }
    }
  }
  
  .section-card {
    background: #fff;
    border-radius: 8px;
    padding: 20px;
    margin-bottom: 20px;
    
    .section-header {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 15px;
      
      h3 {
        font-size: 16px;
        color: #303133;
        margin: 0;
      }
    }
  }
}
</style>
