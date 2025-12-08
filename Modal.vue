<template>
  <transition name="fade">
    <div v-if="visible" class="modal-backdrop">
      <div class="modal-box">
        <header class="modal-header">
          <h3>{{ title }}</h3>
        </header>
        <div class="modal-body">
          <slot />
        </div>
        <footer class="modal-footer">
          <button class="btn btn-cancel" @click="close">取消</button>
          <button class="btn btn-ok" @click="okClicked" :disabled="props.okLoading">
            <span v-if="props.okLoading" class="spinner" aria-hidden></span>
            <span v-if="props.okLoading">{{ okText }}</span>
            <span v-else>{{ okText }}</span>
          </button>
        </footer>
      </div>
    </div>
  </transition>
</template>

<script setup>
const props = defineProps({
  visible: { type: Boolean, default: false },
  title: { type: String, default: '' },
  okText: { type: String, default: '确定' },
  okLoading: { type: Boolean, default: false }
})
const emit = defineEmits(['update:visible', 'ok'])

const close = () => emit('update:visible', false)
const okClicked = () => {
  if (props.okLoading) return
  emit('ok')
  
  // do not auto-close if parent wants to keep open during async save.
}
</script>

<style scoped>
.modal-backdrop {
  position: fixed; inset: 0; display:flex; align-items:center; justify-content:center; background: rgba(2,6,23,0.45); z-index: 1000;
}
.modal-box { width: 820px; background: #fff; border-radius: 10px; box-shadow: 0 8px 30px rgba(2,6,23,0.2); overflow: hidden }
.modal-header { padding: 16px 20px; border-bottom: 1px solid #eef2f7 }
.modal-header h3 { margin: 0; font-size:16px; color:#0f172a }
.modal-body { padding: 18px 20px; color:#334155 }
.modal-footer { padding: 12px 16px; display:flex; justify-content:flex-end; gap:8px; border-top: 1px solid #f1f5f9 }
.btn { padding:8px 12px; border-radius:8px; cursor:pointer; border:none }
.btn-cancel { background: #f8fafc; color: #334155 }
.btn-ok { background: #2563eb; color:#fff }
.fade-enter-active, .fade-leave-active { transition: all .12s ease }
.fade-enter-from, .fade-leave-to { opacity:0; transform: translateY(-6px) }
.spinner { display:inline-block; width:14px; height:14px; border-radius:50%; border:2px solid rgba(255,255,255,0.4); border-top-color: #fff; margin-right:8px; animation: spin .9s linear infinite }
@keyframes spin { to { transform: rotate(360deg) } }
</style>
