<template>
  <div class="page">
    <div class="panel">
      <section class="form">
        <h2>账号登录</h2>
        <form @submit.prevent="handleSubmit">
          <label>
            <span>用户名</span>
            <input
              v-model.trim="form.username"
              type="text"
              placeholder="请输入用户名"
              autocomplete="username"
              required
            />
            <small v-if="errors.username">{{ errors.username }}</small>
          </label>

          <label>
            <span>密码</span>
            <div class="password-field">
              <input
                v-model="form.password"
                :type="showPassword ? 'text' : 'password'"
                placeholder="请输入密码"
                autocomplete="current-password"
                required
              />
              <button type="button" class="link" @click="togglePassword">
                {{ showPassword ? '隐藏' : '显示' }}
              </button>
            </div>
            <small v-if="errors.password">{{ errors.password }}</small>
          </label>

          <button class="primary" type="submit" :disabled="isSubmitting || !canSubmit">
            {{ isSubmitting ? '登录中...' : '登录' }}
          </button>
        </form>

        <p v-if="message" class="toast">{{ message }}</p>
      </section>
    </div>
  </div>
</template>

<script setup>
import { computed, reactive, ref } from 'vue'
import { useRouter } from 'vue-router'
import { userApi } from '@/api/api'

const form = reactive({
  username: '',
  password: '',
})

const errors = reactive({
  username: '',
  password: '',
})

const showPassword = ref(false)
const isSubmitting = ref(false)
const message = ref('')
const canSubmit = computed(() => {
  return (
    form.username.trim().length >= 3 &&
    form.password.trim().length >= 6 &&
    !isSubmitting.value
  )
})

const validate = () => {
  errors.username = ''
  errors.password = ''

  if (!form.username) {
    errors.username = '请输入用户名'
  } else if (form.username.length < 3) {
    errors.username = '用户名不少于 3 个字符'
  }
  if (!form.password) {
    errors.password = '请输入密码'
  } else if (form.password.length < 6) {
    errors.password = '密码长度不少于 6 位'
  }

  return !errors.username && !errors.password
}

const router = useRouter()

const handleSubmit = async () => {
  if (!validate() || isSubmitting.value) return

  isSubmitting.value = true
  message.value = ''

  try {
    const res = await userApi.login({
      username: form.username,
      password: form.password,
    })

    if (res && res.code === 500) {
      message.value = res.message
      return
    }

    message.value = '登录成功'
    // 保存用户名并短暂提示后跳转到广告管理页面
    localStorage.setItem('username', form.username)
    await new Promise((r) => setTimeout(r, 300))
    router.replace({ name: 'Main' })
  } catch (err) {
    message.value = err?.message || '登录失败'
  } finally {
    isSubmitting.value = false
  }
}

const togglePassword = () => {
  showPassword.value = !showPassword.value
}
</script>

<style scoped>
:global(body) {
  margin: 0;
  font-family: 'Inter', 'PingFang SC', 'Microsoft YaHei', sans-serif;
  background: linear-gradient(135deg, #0f172a, #1d4ed8);
  color: #0f172a;
}

.page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 32px 16px;
}

.panel {
  width: min(960px, 100%);
  background: rgba(255, 255, 255, 0.9);
  border-radius: 32px;
  padding: 48px;
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(320px, 1fr));
  gap: 40px;
  box-shadow: 0 25px 80px rgba(15, 23, 42, 0.2);
  backdrop-filter: blur(12px);
}

.branding {
  color: #0f172a;
}

.brand-badge {
  display: inline-flex;
  align-items: center;
  padding: 6px 14px;
  border-radius: 999px;
  background: rgba(37, 99, 235, 0.1);
  color: #2563eb;
  font-weight: 600;
  margin-bottom: 16px;
}

h1 {
  margin: 0;
  font-size: 40px;
  line-height: 1.2;
}

.subtitle {
  margin: 16px 0 24px;
  color: #475569;
}

ul {
  padding-left: 18px;
  color: #334155;
  line-height: 1.7;
  margin: 0;
}

.form {
  background: #ffffff;
  border-radius: 24px;
  padding: 32px;
  box-shadow: 0 20px 40px rgba(37, 99, 235, 0.15);
}

.form h2 {
  margin-top: 0;
  margin-bottom: 24px;
}

form {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

label {
  display: flex;
  flex-direction: column;
  gap: 8px;
  font-size: 14px;
  color: #475569;
}

input {
  border: 1px solid #cbd5f5;
  border-radius: 12px;
  padding: 12px 14px;
  font-size: 16px;
  transition: border-color 0.2s, box-shadow 0.2s;
}

input:focus {
  outline: none;
  border-color: #2563eb;
  box-shadow: 0 0 0 3px rgba(37, 99, 235, 0.15);
}

.password-field {
  display: flex;
  align-items: center;
  border: 1px solid #cbd5f5;
  border-radius: 12px;
  padding-right: 4px;
  transition: border-color 0.2s, box-shadow 0.2s;
}

.password-field:focus-within {
  border-color: #2563eb;
  box-shadow: 0 0 0 3px rgba(37, 99, 235, 0.15);
}

.password-field input {
  flex: 1;
  border: none;
  border-radius: 12px;
}

.actions {
  display: flex;
  justify-content: space-between;
  align-items: center;
  font-size: 14px;
}

.remember {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  color: #0f172a;
  cursor: pointer;
}

.link {
  background: none;
  border: none;
  color: #2563eb;
  font-size: 14px;
  padding: 4px 8px;
  cursor: pointer;
}

.link:hover {
  text-decoration: underline;
}

.primary {
  background: linear-gradient(120deg, #2563eb, #a855f7);
  border: none;
  color: #ffffff;
  font-size: 16px;
  font-weight: 600;
  padding: 14px;
  border-radius: 16px;
  cursor: pointer;
  transition: transform 0.2s, box-shadow 0.2s;
}

.primary:disabled {
  opacity: 0.7;
  cursor: not-allowed;
}

.primary:not(:disabled):hover {
  transform: translateY(-1px);
  box-shadow: 0 12px 24px rgba(37, 99, 235, 0.3);
}

.hint {
  text-align: center;
  font-size: 14px;
  color: #475569;
  margin: 8px 0 0;
}

.toast {
  margin-top: 16px;
  padding: 12px 16px;
  background: rgba(34, 197, 94, 0.12);
  color: #15803d;
  border-radius: 12px;
  font-size: 14px;
}

small {
  color: #dc2626;
}

@media (max-width: 768px) {
  .panel {
    padding: 32px 24px;
  }

  .form {
    padding: 24px;
  }
}
</style>

