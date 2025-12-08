<template>
  <MainLayout ref="layoutRef" v-slot="{ active }">
    <div class="panel">
      <AdsList ref="adsListRef" v-if="active === 'list'" @create="onCreate" />
    </div>
  </MainLayout>
</template>


<script setup>
import { reactive, ref } from 'vue'
import MainLayout from './MainLayout.vue'
import AdsList from './AdsList.vue'


const adsListRef = ref(null)
const layoutRef = ref(null)

const onCreate = () => {
  layoutRef.value?.setActive('create')
}
</script>

<style scoped>
.ads-page { min-height: 100vh; display: flex; flex-direction: column; font-family: 'Microsoft YaHei', sans-serif; }
.container { display:flex; flex:1 }
.content { flex:1; padding: 20px }
.ads-table { width:100%; border-collapse: collapse; margin-top:12px }
.ads-table th, .ads-table td { border:1px solid #e6eef8; padding:10px; text-align:left }
.create-form { display:flex; flex-direction:column; gap:12px; max-width:480px }
.create-form label { display:flex; flex-direction:column; font-size:14px }
.create-form input, .create-form select { padding:8px 10px; border:1px solid #dbeafe; border-radius:6px }
.create-form button { width:120px; padding:8px 12px; background:#2563eb; color:#fff; border:none; border-radius:6px; cursor:pointer }
</style>
