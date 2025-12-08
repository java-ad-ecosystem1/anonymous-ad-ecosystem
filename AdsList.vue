<template>
  <section class="ads-list">
    <div class="ads-header">
      <h2>广告列表</h2>
      <div class="actions">
        <button class="new-button" @click="createAds">新建</button>
      </div>
    </div>
    <table class="ads-table">
      <thead>
        <tr>
          <th>序号</th>
          <th>广告名称</th>
          <th>广告类型</th>
          <th>投放类型</th>
          <th>是否投放</th>
          <th>广告内容</th>
          <th>创建时间</th>
          <th>操作</th>
        </tr>
      </thead>
      <tbody>
        <tr v-if="loading">
          <td :colspan="8" class="placeholder">加载中...</td>
        </tr>
        <tr v-else-if="error">
          <td :colspan="8" class="placeholder error">{{ error }}</td>
        </tr>
        <tr v-else-if="ads.length === 0">
          <td :colspan="8" class="placeholder">暂无数据</td>
        </tr>
        <tr v-else v-for="(ad, index) in ads" :key="ad.id">
          <td>{{ index + 1 }}</td>
          <td>{{ ad.title }}</td>
          <td>{{ translate('advertisementType', ad.advertisementType) }}</td>
          <td>{{ translate('putType', ad.putType) }}</td>
          <td>{{ ad.putFlag ? '是':'否' }}</td>
          <td class="content-cell">
            <span v-if="ad.content">{{ ad.content }}</span>
            <span v-else>-</span>
          </td>
          <td>{{ formatDate(ad.createTime,'yyyy-MM-dd HH:mm:ss') }}</td>
          <td>
            <button @click="putAds(ad.id)">投放</button>
          </td>
        </tr>
      </tbody>
    </table>

    <div class="pagination-bar">
      <div class="p-left">
        <button :disabled="pagination.pageNum <= 1 || loading" @click="changePage(pagination.pageNum - 1)">上一页</button>
        <button :disabled="pagination.pageNum >= totalPages || loading" @click="changePage(pagination.pageNum + 1)">下一页</button>
      </div>
      <div class="p-center">第 {{ pagination.pageNum }} / {{ totalPages }} 页，共 {{ pagination.total }} 条</div>
      <div class="p-right">
        <label>每页
          <select v-model.number="pagination.pageSize" @change="onPageSizeChange" :disabled="loading">
            <option :value="5">5</option>
            <option :value="10">10</option>
            <option :value="20">20</option>
            <option :value="50">50</option>
          </select>
        条</label>
      </div>
    </div>

    <Modal v-model:visible="modal.visible" :title="modal.title" :okText="modal.okText" @ok="modal.visible = false">
      <div style="white-space:pre-wrap">{{ modal.message }}</div>
    </Modal>
    <Modal v-model:visible="createModal.visible" title="新建广告" okText="保存" :okLoading="createLoading" @ok="onCreateConfirm">
      <div class="create-form">
        <label class="row">广告名称
          <input v-model="createModal.form.title" type="text" :disabled="createLoading" />
        </label>
        <div class="error-text" v-if="createErrors.title">{{ createErrors.title }}</div>

        <label class="row">广告类型
          <select v-model="createModal.form.advertisementType" :disabled="createLoading">
            <option value="">请选择</option>
            <option v-for="(label,key) in advertisementTypeMap" :key="key" :value="key">{{ label }}</option>
          </select>
        </label>

        <label class="row">投放类型
          <select v-model="createModal.form.putType" :disabled="createLoading">
            <option value="">请选择</option>
            <option v-for="(label,key) in putTypeMap" :key="key" :value="key">{{ label }}</option>
          </select>
        </label>

        <label class="row checkbox-row">是否投放
          <input 
            type="checkbox" 
            v-model="createModalForm.putFlag"
            :disabled="createLoading" 
          />
        </label>

        <!-- 文本类型显示文本输入框 -->
        <label class="row" v-if="createModal.form.advertisementType === 'text'">
          广告内容
          <textarea v-model="createModal.form.content" rows="4" :disabled="createLoading"></textarea>
        </label>

        <!-- 图片类型显示图片上传 -->
        <label class="row" v-if="createModal.form.advertisementType === 'picture'">
          广告内容（图片上传）
          <div class="upload-area">
            <input 
              type="file" 
              ref="imageInput" 
              accept="image/*" 
              @change="handleImageUpload" 
              :disabled="createLoading"
              style="display: none"
            />
            <div class="upload-box" @click="triggerImageUpload" :class="{ disabled: createLoading }">
              <div v-if="!createModal.form.imagePreview && !createLoading" class="upload-placeholder">
                <span>点击上传图片</span>
              </div>
              <div v-if="createLoading && !createModal.form.imagePreview" class="upload-loading">
                <span>上传中...</span>
              </div>
              <div v-if="createModal.form.imagePreview" class="upload-preview">
                <img :src="createModal.form.imagePreview" alt="预览" />
                <button type="button" class="remove-btn" @click.stop="removeImage" :disabled="createLoading">删除</button>
              </div>
            </div>
          </div>
        </label>

        <!-- 视频类型显示视频上传 -->
        <label class="row" v-if="createModal.form.advertisementType === 'video'">
          广告内容（视频上传）
          <div class="upload-area">
            <input 
              type="file" 
              ref="videoInput" 
              accept="video/*" 
              @change="handleVideoUpload" 
              :disabled="createLoading"
              style="display: none"
            />
            <div class="upload-box" @click="triggerVideoUpload" :class="{ disabled: createLoading }">
              <div v-if="!createModal.form.videoPreview && !createLoading" class="upload-placeholder">
                <span>点击上传视频</span>
              </div>
              <div v-if="createLoading && !createModal.form.videoPreview" class="upload-loading">
                <span>上传中...</span>
              </div>
              <div v-if="createModal.form.videoPreview" class="upload-preview">
                <video :src="createModal.form.videoPreview" controls></video>
                <button type="button" class="remove-btn" @click.stop="removeVideo" :disabled="createLoading">删除</button>
              </div>
            </div>
          </div>
        </label>
      </div>
    </Modal>
  </section>
</template>

<script setup>
import { reactive, ref, onMounted, computed, watch } from 'vue'
import { adsApi, uploadApi } from '@/api/api'
import { formatDate } from '@/utils/date'
import Modal from './common/Modal.vue'

const imageInput = ref(null)
const videoInput = ref(null)

const ads = reactive([])
const loading = ref(false)
const error = ref(null)

const pagination = reactive({ pageNum: 1, pageSize: 10, total: 0 })

const totalPages = computed(() => Math.max(1, Math.ceil((pagination.total || 0) / (pagination.pageSize || 1))))

const loadAds = async (pageNum = pagination.pageNum) => {
  loading.value = true
  error.value = null
  try {
    pagination.pageNum = pageNum
    const params = { pageNum: pagination.pageNum, pageSize: pagination.pageSize }
    const res = await adsApi.list(params)

    if(!res || !res.data || res.code !== 200) {
      error.value = (res && res.data && res.data.message) || "获取广告列表失败"
      loading.value = false
      return
    }

    let list = res.data.list || res.data.records || res.data.items || []
    const total = res.data.total ?? res.data.totalCount ?? res.data.count ?? res.data.recordsTotal ?? list.length
    pagination.total = typeof total === 'number' ? total : (Array.isArray(list) ? list.length : 0)
    ads.splice(0, ads.length, ...list)
  } catch (e) {
    error.value = "系统错误"
  } finally {
    loading.value = false
  }
}


const createModalForm = reactive({
  title: '', advertisementType: '', putType: '', putFlag: false, content: '',
  imageFile: null, imagePreview: '', imageUrl: '',
  videoFile: null, videoPreview: '', videoUrl: ''
})
const createModal = reactive({ visible: false, form: createModalForm })
const createLoading = ref(false)
const createErrors = reactive({ title: '' })

// 监听广告类型变化，清除之前的上传内容
watch(() => createModal.form.advertisementType, (newType, oldType) => {
  if (newType !== oldType) {
    // 清除图片相关
    if (oldType === 'picture') {
      removeImage()
    }
    // 清除视频相关
    if (oldType === 'video') {
      removeVideo()
    }
    // 清除文本内容（如果切换到图片或视频类型）
    if (newType === 'picture' || newType === 'video') {
      createModal.form.content = ''
    }
  }
})

const createAds = () => {
  // reset form
  createModal.form.title = ''
  createModal.form.advertisementType = ''
  createModal.form.putType = ''
  createModal.form.putFlag = false
  createModal.form.content = ''
  createModal.form.imageFile = null
  createModal.form.imagePreview = ''
  createModal.form.imageUrl = ''
  createModal.form.videoFile = null
  createModal.form.videoPreview = ''
  createModal.form.videoUrl = ''
  createErrors.title = ''
  createModal.visible = true
}

// 触发图片上传
const triggerImageUpload = () => {
  if (createLoading.value) return
  imageInput.value?.click()
}

// 触发视频上传
const triggerVideoUpload = () => {
  if (createLoading.value) return
  videoInput.value?.click()
}

// 处理图片上传
const handleImageUpload = async (event) => {
  const file = event.target.files?.[0]
  if (!file) return

  // 验证文件类型
  if (!file.type.startsWith('image/')) {
    showErrorModal('上传失败', '请选择图片文件')
    return
  }

  // 验证文件大小（限制10MB）
  if (file.size > 10 * 1024 * 1024) {
    showErrorModal('上传失败', '图片大小不能超过10MB')
    return
  }

  // 创建预览
  const reader = new FileReader()
  reader.onload = (e) => {
    createModal.form.imagePreview = e.target.result
  }
  reader.readAsDataURL(file)
  createModal.form.imageFile = file

  // 上传文件
  createLoading.value = true
  try {
    const res = await uploadApi.upload(file)
    if (res && res.code === 200) {
      // 获取返回的路径，可能是 res.data 或 res.data.path 等
      const path = res.data?.path || res.data?.url || res.data?.fileUrl || res.path || res.url || res.data || ''
      createModal.form.imageUrl = path
      createModal.form.content = path
    } else {
      showErrorModal('上传失败', res?.message || '图片上传失败')
      removeImage()
    }
  } catch (e) {
    showErrorModal('上传失败', '图片上传失败，请重试')
    removeImage()
  } finally {
    createLoading.value = false
  }
}

// 处理视频上传
const handleVideoUpload = async (event) => {
  const file = event.target.files?.[0]
  if (!file) return

  // 验证文件类型
  if (!file.type.startsWith('video/')) {
    showErrorModal('上传失败', '请选择视频文件')
    return
  }

  // 验证文件大小（限制100MB）
  if (file.size > 100 * 1024 * 1024) {
    showErrorModal('上传失败', '视频大小不能超过100MB')
    return
  }

  // 创建预览
  const reader = new FileReader()
  reader.onload = (e) => {
    createModal.form.videoPreview = e.target.result
  }
  reader.readAsDataURL(file)
  createModal.form.videoFile = file

  // 上传文件
  createLoading.value = true
  try {
    const res = await uploadApi.upload(file)
    if (res && res.code === 200) {
      // 获取返回的路径，可能是 res.data 或 res.data.path 等
      const path = res.data?.path || res.data?.url || res.data?.fileUrl || res.path || res.url || res.data || ''
      createModal.form.videoUrl = path
      createModal.form.content = path
    } else {
      showErrorModal('上传失败', res?.message || '视频上传失败')
      removeVideo()
    }
  } catch (e) {
    showErrorModal('上传失败', '视频上传失败，请重试')
    removeVideo()
  } finally {
    createLoading.value = false
  }
}

// 删除图片
const removeImage = () => {
  createModal.form.imageFile = null
  createModal.form.imagePreview = ''
  createModal.form.imageUrl = ''
  createModal.form.content = ''
  if (imageInput.value) {
    imageInput.value.value = ''
  }
}

// 删除视频
const removeVideo = () => {
  createModal.form.videoFile = null
  createModal.form.videoPreview = ''
  createModal.form.videoUrl = ''
  createModal.form.content = ''
  if (videoInput.value) {
    videoInput.value.value = ''
  }
}

const onCreateConfirm = async () => {
  const f = createModalForm
  createErrors.title = ''
  if (!f.title || !f.title.trim()) { createErrors.title = '请输入广告名称'; return }

  // 根据广告类型验证内容
  if (f.advertisementType === 'picture' && !f.imageUrl) {
    showErrorModal('创建失败', '请上传图片')
    return
  }
  if (f.advertisementType === 'video' && !f.videoUrl) {
    showErrorModal('创建失败', '请上传视频')
    return
  }
  if (f.advertisementType === 'text' && !f.content?.trim()) {
    showErrorModal('创建失败', '请输入广告内容')
    return
  }

  createLoading.value = true
  try {
    // 构建提交数据
    const submitData = {
      title: f.title,
      advertisementType: f.advertisementType,
      putType: f.putType,
      putFlag: f.putFlag, // 直接使用 createModalForm.putFlag
      content: f.content || f.imageUrl || f.videoUrl || ''
    }

    const res = await adsApi.save(submitData)
    if(!res || !res.data || res.code !== 200) {
      createModal.visible = false
      showErrorModal('创建失败', res.message || '创建失败')
      return
    }

    createModal.visible = false
    await loadAds()
    showSuccessModal('创建成功', '广告已创建')
  } catch (e) {
    showErrorModal('创建失败','系统错误')
  } finally {
    createLoading.value = false
  }
}


const modal = reactive({ visible: false, title: '', message: '', okText: '确定' })


const showSuccessModal = (title, message) => {
  modal.title = title || '创建成功'
  modal.message = message || ''
  modal.okText = '确定'
  modal.visible = true
}

const showErrorModal = (title, message) => {
  modal.title = title || '操作失败'
  modal.message = message || ''
  modal.okText = '确定'
  modal.visible = true
}

const putAds = async (id) => {
  try {
    console.log(id)
    const res = await adsApi.putAds({ id })
    if(!res || !res.data || res.code !== 200) {
      const text = (res && res.data && res.message) ? res.message : '投放失败'
      showErrorModal('投放失败', text)
      return
    }
    await loadAds()
  } catch (e) {
    showErrorModal('投放失败', '投放失败')
  }
}

onMounted(() => loadAds())

const changePage = (p) => {
  if (p < 1) p = 1
  if (p > totalPages.value) p = totalPages.value
  if (p === pagination.pageNum) return
  loadAds(p)
}

const onPageSizeChange = () => {
  pagination.pageNum = 1
  loadAds(1)
}

defineExpose({ loadAds })

const advertisementTypeMap = {
  text: '文本',
  video: '视频',
  picture: '图片',
}

const putTypeMap = {
  mobile: '手机',
  clothes: '服装',
  photography: '摄影',
  computer: '笔记本',
}

const translate = (type, val) => {
  if (val === null || val === undefined) return ''
  if (type === 'advertisementType') {
    if (advertisementTypeMap[val]) return advertisementTypeMap[val]
    return String(val)
  }
  if (type === 'putType') {
    if (putTypeMap[val]) return putTypeMap[val]
    return String(val)
  }
  return String(val)
}
</script>

<style scoped>
.ads-list {
  padding: 18px;
  background: #fff;
  border-radius: 10px;
  box-shadow: 0 1px 6px rgba(16,24,40,0.04);
}
.ads-list h2 { margin: 0 0 12px; font-size:18px; color:#0f172a }
.ads-header { display:flex; align-items:center; justify-content:space-between }
.actions { display:flex; gap:8px }
.new-button { background: #06b6d4; color: #fff; border:none; padding:8px 12px; border-radius:8px; cursor:pointer }
.new-button:hover { filter:brightness(.95); transform: translateY(-1px) }
.ads-table { width:100%; border-collapse: collapse; margin-top:8px }
.ads-table th, .ads-table td {
  border-bottom: 1px solid #eef2f7;
  padding:12px 10px;
  text-align:left;
}
.ads-table thead th {
  background: #f8fafc;
  font-weight:700;
  color:#334155;
}
.ads-table tbody tr:nth-child(odd) { background: #fff }
.ads-table tbody tr:nth-child(even) { background: #fbfdff }
.ads-table button {
  padding:6px 8px; border-radius:6px; border:none; cursor:pointer; margin-right:6px; font-size:13px
}
.ads-table button:first-of-type { background:#2563eb; color:#fff }
.ads-table button:last-of-type { background:#ef4444; color:#fff }
.placeholder { padding:14px; text-align:center; color:#64748b }
.placeholder.error { color:#b91c1c; background:#fff1f2; border-radius:6px }
.content-cell { 
  max-width: 300px; 
  word-break: break-all;
  font-size: 13px;
  color: #475569;
}

/* create modal form styles */
.create-form { display:block; margin-top:6px }
.create-form .row { display:block; margin-bottom:10px; font-size:14px }
.create-form label { display:block; color:#0f172a }
.create-form input[type='text'], .create-form select, .create-form textarea {
  width:100%; box-sizing:border-box; padding:8px 10px; border:1px solid #e6edf3; border-radius:6px; margin-top:6px; background:#fff
}
.create-form textarea { min-height:90px }
.create-form .checkbox-row { display:flex; align-items:center; gap:8px }
.error-text { color:#b91c1c; font-size:13px; margin: -6px 0 8px }

/* 上传组件样式 */
.upload-area { margin-top: 6px }
.upload-box {
  border: 2px dashed #e6edf3;
  border-radius: 8px;
  padding: 20px;
  text-align: center;
  cursor: pointer;
  transition: all 0.2s;
  background: #f8fafc;
}
.upload-box:hover:not(.disabled) {
  border-color: #06b6d4;
  background: #f0f9ff;
}
.upload-box.disabled {
  opacity: 0.6;
  cursor: not-allowed;
}
.upload-placeholder {
  color: #64748b;
  font-size: 14px;
}
.upload-loading {
  color: #06b6d4;
  font-size: 14px;
}
.upload-preview {
  position: relative;
  display: inline-block;
  max-width: 100%;
}
.upload-preview img {
  max-width: 100%;
  max-height: 300px;
  border-radius: 6px;
  display: block;
}
.upload-preview video {
  max-width: 100%;
  max-height: 300px;
  border-radius: 6px;
  display: block;
}
.remove-btn {
  margin-top: 8px;
  padding: 6px 12px;
  background: #ef4444;
  color: #fff;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  font-size: 13px;
}
.remove-btn:hover:not(:disabled) {
  background: #dc2626;
}
.remove-btn:disabled {
  opacity: 0.6;
  cursor: not-allowed;
}

/* pagination bar: keep everything on a single line */
.pagination-bar {
  display: flex;
  align-items: center;
  justify-content: space-between;
  gap: 12px;
  margin-top: 12px;
  flex-wrap: nowrap;
}
.pagination-bar .p-left,
.pagination-bar .p-center,
.pagination-bar .p-right {
  display: flex;
  align-items: center;
  gap: 8px;
  white-space: nowrap;
}
.pagination-bar .p-center { flex: 1; justify-content: center }
.pagination-bar button { padding:6px 10px; border-radius:6px; border:1px solid #e6edf3; background:#fff; cursor:pointer }
.pagination-bar button:disabled { opacity:0.6; cursor:not-allowed }
.pagination-bar select { padding:6px 8px; border-radius:6px; border:1px solid #e6edf3; background:#fff }

.ads-header .new-button[disabled] { opacity:0.6; cursor:not-allowed }
</style>
