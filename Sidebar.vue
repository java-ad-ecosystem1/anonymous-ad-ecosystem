<template>
  <aside class="sidebar">
    <nav>
      <ul class="menu-root">
        <li class="parent">
          <div class="parent-label" @click="toggleParent">
            <span class="caret" :class="{open: parentOpen}">▸</span>
            <span>广告管理</span>
          </div>
          <ul v-show="parentOpen" class="submenu">
            <li :class="{active: modelValue === 'list'}" @click="$emit('update:modelValue','list')">广告列表</li>
          </ul>
        </li>
      </ul>
    </nav>
  </aside>
</template>

<script setup>
const props = defineProps({
  modelValue: { type: String, default: 'list' }
})

const emit = defineEmits(['update:modelValue'])

import { ref, watch } from 'vue'

const parentOpen = ref(true)

watch(() => props.modelValue, (v) => {
  if (v === 'list' || v === 'create' || v === 'stats') {
    parentOpen.value = true
  }
})

const toggleParent = () => {
  parentOpen.value = !parentOpen.value
}
</script>

<style scoped>
.sidebar {
  width: 220px;
  background: #f8fafc;
  border-right: 1px solid #e6eef8;
  padding: 18px 14px;
  box-shadow: inset -1px 0 0 rgba(14,165,233,0.02);
}
.sidebar ul { list-style:none; padding:0; margin:0 }
.sidebar li { padding: 8px 12px; cursor: pointer; border-radius:8px; color:#0f172a; transition: background .12s ease, color .12s ease }
.sidebar li:hover { background: #f1f5ff }
.sidebar li.active { background:#e6f0ff; color:#1d4ed8; font-weight:600; border-left: 3px solid #2563eb }
.menu-root { padding: 0 }
.parent-label { display:flex; align-items:center; gap:8px; padding: 8px 6px; cursor:pointer; font-weight:600 }
.caret { display:inline-block; transform: rotate(0deg); transition: transform 0.18s cubic-bezier(.2,.8,.2,1) }
.caret.open { transform: rotate(90deg) }
.submenu { padding-left: 8px; margin-top:6px }
.submenu li { padding: 8px 10px; border-radius:6px }
</style>
