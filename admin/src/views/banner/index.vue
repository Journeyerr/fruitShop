<template>
  <div class="page-container">
    <div class="page-header">
      <h2 class="page-title">轮播图管理</h2>
      <el-button type="primary" @click="handleAdd">
        <el-icon><Plus /></el-icon>
        添加轮播图
      </el-button>
    </div>
    
    <!-- 表格 -->
    <div class="table-container">
      <el-table :data="tableData" style="width: 100%" v-loading="loading">
        <el-table-column type="index" label="序号" width="60" />
        <el-table-column label="图片" min-width="150">
          <template #default="{ row }">
            <el-image
              :src="row.imageUrl"
              fit="cover"
              class="table-image"
              @click="handlePreviewImage(row.imageUrl)"
            />
          </template>
        </el-table-column>
        <el-table-column prop="title" label="标题" min-width="150" />
        <el-table-column label="跳转类型" min-width="100">
          <template #default="{ row }">
            {{ getLinkTypeName(row.linkType) }}
          </template>
        </el-table-column>
        <el-table-column prop="linkParam" label="跳转参数" min-width="120" />
        <el-table-column prop="sort" label="排序" width="80" />
        <el-table-column label="状态" width="100">
          <template #default="{ row }">
            <el-switch
              v-model="row.status"
              :active-value="1"
              :inactive-value="0"
              @change="handleStatusChange(row)"
            />
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150" fixed="right">
          <template #default="{ row }">
            <el-button type="primary" link @click="handleEdit(row)">编辑</el-button>
            <el-button type="danger" link @click="handleDelete(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
    
    <!-- 图片预览遮罩层 -->
    <div v-if="previewVisible" class="image-preview-mask" @click="previewVisible = false">
      <img :src="previewImageUrl" class="image-preview-img" @click.stop />
    </div>

    <!-- 编辑对话框 -->
    <el-dialog
      v-model="dialogVisible"
      :title="dialogTitle"
      width="500px"
      @close="resetForm"
    >
      <el-form ref="formRef" :model="form" :rules="rules" label-width="100px">
        <el-form-item label="标题" prop="title">
          <el-input v-model="form.title" placeholder="请输入标题" />
        </el-form-item>
        <el-form-item label="图片" prop="imageUrl">
          <div class="upload-container">
            <el-upload
              class="image-uploader"
              :show-file-list="false"
              :before-upload="beforeUpload"
              :http-request="handleUpload"
              accept="image/*"
            >
              <el-image 
                v-if="form.imageUrl" 
                :src="form.imageUrl" 
                class="uploaded-image"
                fit="cover"
              />
              <el-icon v-else class="upload-icon"><Plus /></el-icon>
            </el-upload>
            <el-input 
              v-model="form.imageUrl" 
              placeholder="或输入图片URL" 
              class="url-input"
            />
          </div>
        </el-form-item>
        <el-form-item label="跳转类型" prop="linkType">
          <el-select v-model="form.linkType" placeholder="请选择跳转类型">
            <el-option label="商品详情" :value="1" />
            <el-option label="分类" :value="2" />
            <el-option label="外链" :value="3" />
          </el-select>
        </el-form-item>
        <el-form-item label="跳转参数" prop="linkParam">
          <el-input v-model="form.linkParam" placeholder="请输入跳转参数" />
        </el-form-item>
        <el-form-item label="排序" prop="sort">
          <el-input-number v-model="form.sort" :min="0" />
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
import { Plus } from '@element-plus/icons-vue'
import { bannerApi, uploadApi } from '@/api'

const loading = ref(false)
const submitLoading = ref(false)
const uploadLoading = ref(false)
const tableData = ref([])
const dialogVisible = ref(false)
const dialogTitle = ref('添加轮播图')
const formRef = ref(null)

// 图片预览
const previewVisible = ref(false)
const previewImageUrl = ref('')

const form = reactive({
  id: null,
  title: '',
  imageUrl: '',
  linkType: 1,
  linkParam: '',
  sort: 0,
  status: 1
})

const rules = {
  title: [{ required: true, message: '请输入标题', trigger: 'blur' }],
  imageUrl: [{ required: true, message: '请上传图片或输入图片URL', trigger: 'blur' }],
  linkType: [{ required: true, message: '请选择跳转类型', trigger: 'change' }]
}

// 获取跳转类型名称
const getLinkTypeName = (type) => {
  const names = { 1: '商品详情', 2: '分类', 3: '外链' }
  return names[type] || '未知'
}

// 预览图片
const handlePreviewImage = (url) => {
  previewImageUrl.value = url
  previewVisible.value = true
}

// 上传前验证
const beforeUpload = (file) => {
  const isImage = file.type.startsWith('image/')
  const isLt5M = file.size / 1024 / 1024 < 5
  
  if (!isImage) {
    ElMessage.error('只能上传图片文件!')
    return false
  }
  if (!isLt5M) {
    ElMessage.error('图片大小不能超过 5MB!')
    return false
  }
  return true
}

// 处理上传
const handleUpload = async (options) => {
  uploadLoading.value = true
  try {
    const res = await uploadApi.image(options.file)
    form.imageUrl = res.data.url
    ElMessage.success('图片上传成功')
  } catch (error) {
    ElMessage.error('图片上传失败')
  } finally {
    uploadLoading.value = false
  }
}

// 加载数据
const loadData = async () => {
  loading.value = true
  try {
    const res = await bannerApi.page({ page: 1, size: 100 })
    tableData.value = res.data?.records || []
  } catch (error) {
    console.error('加载数据失败:', error)
  } finally {
    loading.value = false
  }
}

// 添加
const handleAdd = () => {
  dialogTitle.value = '添加轮播图'
  dialogVisible.value = true
}

// 编辑
const handleEdit = (row) => {
  dialogTitle.value = '编辑轮播图'
  Object.assign(form, row)
  dialogVisible.value = true
}

// 删除
const handleDelete = (row) => {
  ElMessageBox.confirm('确定要删除该轮播图吗？', '提示', {
    type: 'warning'
  }).then(async () => {
    try {
      await bannerApi.delete(row.id)
      ElMessage.success('删除成功')
      loadData()
    } catch (error) {
      console.error('删除失败:', error)
    }
  })
}

// 状态变更
const handleStatusChange = async (row) => {
  try {
    await bannerApi.updateStatus(row.id, row.status)
    ElMessage.success('状态更新成功')
  } catch (error) {
    row.status = row.status === 1 ? 0 : 1
  }
}

// 提交
const handleSubmit = async () => {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  
  submitLoading.value = true
  try {
    if (form.id) {
      await bannerApi.update(form)
      ElMessage.success('更新成功')
    } else {
      await bannerApi.add(form)
      ElMessage.success('添加成功')
    }
    dialogVisible.value = false
    loadData()
  } catch (error) {
    console.error('提交失败:', error)
  } finally {
    submitLoading.value = false
  }
}

// 重置表单
const resetForm = () => {
  formRef.value?.resetFields()
  Object.assign(form, {
    id: null,
    title: '',
    imageUrl: '',
    linkType: 1,
    linkParam: '',
    sort: 0,
    status: 1
  })
}

onMounted(() => {
  loadData()
})
</script>

<style lang="scss" scoped>
.table-image {
  width: 100px;
  height: 60px;
  border-radius: 4px;
  cursor: pointer;
}

.upload-container {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.image-uploader {
  width: 200px;
  height: 120px;
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
  cursor: pointer;
  overflow: hidden;

  &:hover {
    border-color: #409eff;
  }
}

.uploaded-image {
  width: 200px;
  height: 120px;
}

.upload-icon {
  width: 200px;
  height: 120px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 28px;
  color: #8c939d;
}

.url-input {
  width: 300px;
}
</style>

<style lang="scss">
/* 图片预览遮罩层 */
.image-preview-mask {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background-color: rgba(0, 0, 0, 0.8);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 99999;
  cursor: zoom-out;
}

.image-preview-img {
  max-width: 90vw;
  max-height: 90vh;
  object-fit: contain;
  cursor: default;
}
</style>
