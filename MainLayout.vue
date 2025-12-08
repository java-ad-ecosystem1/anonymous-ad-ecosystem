<template>
  <div class="ads-page">
    <Topbar />

    <div class="container">
      <Sidebar v-model="active" />

      <main class="content">
        <slot :active="active" />
      </main>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import Topbar from './common/Topbar.vue'
import Sidebar from './common/Sidebar.vue'

const active = ref('list')
const setActive = (val) => { active.value = val }
defineExpose({ setActive })
</script>

<style scoped>
.ads-page { min-height: 100vh; display: flex; flex-direction: column; font-family: 'Microsoft YaHei', sans-serif; background: #f3f6fb }
.container { display:flex; flex:1; align-items:flex-start }
.content { flex:1; padding: 24px }
.content > .panel { max-width: 1100px; margin: 0 auto }

/* responsive */
@media (max-width: 900px) {
  .sidebar { display: none }
  .content { padding: 12px }
}
</style>
