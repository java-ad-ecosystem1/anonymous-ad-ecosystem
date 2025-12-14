import { defineConfig, loadEnv } from 'vite'
import vue from '@vitejs/plugin-vue'
import { resolve } from 'path'

// https://vite.dev/config/
export default defineConfig({
    plugins: [vue()],
    server: {
      host: '0.0.0.0', // 允许从任何IP地址访问
      port: 5176,
      proxy: {
        '/api/advertisement': {
          target: 'http://192.168.1.103:8081',
          changeOrigin: true,
          secure: false,
        },
        '/api': {
          target: 'http://localhost:8084',
          changeOrigin: true,
          pathRewrite: {
            '^/api': ''
          }
        }
      }
    },
    resolve: {
      alias: {
        '@': resolve(__dirname, 'src') // 路径别名
      },
      extensions: ['.js', '.json', '.ts', '.vue'] // 使用路径别名时想要省略的后缀名，可以自己 增减
    },
  })
