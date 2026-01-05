# 广告精准投放生态系统项目

## 📋 项目概述
这是一个包含四个独立网站的广告精准投放生态系统，实现跨站用户追踪和匿名精准广告投放。

## 👥 团队成员分工

| 成员姓名 | 负责模块 | 主要职责 | 技术任务 |
|---------|---------|---------|---------|
| **杜一帆** | **广告管理网站** | 广告发布与管理平台 | - 广告商登录和管理界面<br>- 广告发布API接口设计<br>- 广告类型管理（文字/图片/视频）<br>- 精准投放逻辑实现 |
| **朱紫月** | **新闻网站** | 新闻内容展示平台 | - 新闻内容展示和分类<br>- 文字/图片广告位管理<br>- 用户浏览行为跟踪<br>- 跨站cookie跟踪实现<br>- 广告展示和点击统计 |
| **韩晴** | **购物网站** | 电商购物平台 | - 商品展示和购物功能<br>- 广告与商品分类关联<br>- 用户购物行为分析<br>- 精准广告匹配算法<br>- 交易和广告数据整合 |
| **刘宇晴** | **视频发布分享网站** | 视频内容平台 | - 视频上传和播放功能<br>- 视频中插播广告实现<br>- 视频内容分类管理<br>- 广告视频流控制<br>- 播放行为数据收集 |


## 🔧 技术栈
- **前端框架**: Vue 3 / React（根据各网站选择）
- **后端技术**: Java + Spring Boot / Node.js
- **数据库**: MySQL / MongoDB
- **服务器**: Tomcat / Nginx
- **开发工具**: IntelliJ IDEA, Git, GitHub
- **版本控制**: Git + GitHub（确保版本统一）
- **部署环境**: 四个独立的服务器

## 📊 开发里程碑

| 阶段 | 时间 | 主要任务 | 负责人 |
|------|------|---------|--------|
| 环境搭建 | 第1周 | 统一开发环境，创建GitHub仓库 | 全体成员 |
| 核心功能开发 | 第2-4周 | 各网站基础功能实现 | 各模块负责人 |
| 广告投放集成 | 第5周 | 跨站跟踪和精准投放逻辑 | 杜一帆 |
| 测试优化 | 第6周 | 功能测试和性能优化 | 全体成员 |
| 部署上线 | 第7周 | 服务器部署和演示准备 | 全体成员 |


# 广告管理系统后端

## 项目概述

广告管理系统是一个基于 Spring Boot 的后端应用程序，用于管理广告的创建、投放、查询等功能。系统支持文本、图片、视频等多种广告类型，并提供用户认证、文件上传、分页查询等核心功能。

## 技术栈

- **后端框架**: Spring Boot 2.x
- **持久层**: MyBatis
- **数据库**: MySQL
- **构建工具**: Maven
- **Java版本**: 8+

## 项目结构

```
com.advertisement_sys
├── common                 # 公共组件
│   ├── PageParam.java     # 分页参数基类
│   ├── PageResult.java    # 分页结果封装
│   ├── Result.java        # 统一响应结果
│   └── ResultCode.java    # 响应状态码枚举
├── config                 # 配置类
│   └── WebMvcConfig.java  # Web MVC配置
├── controller             # 控制器层
│   ├── AdvertisementController.java  # 广告控制器
│   ├── FileUploadController.java     # 文件上传控制器
│   └── UserController.java           # 用户控制器
├── dao                    # 数据访问层
│   ├── AdvertisementMapper.java      # 广告Mapper
│   └── UserMapper.java               # 用户Mapper
├── entity                 # 实体类
│   ├── Advertisement.java            # 广告实体
│   └── User.java                     # 用户实体
├── model                 # 模型类
│   ├── AdvertisementModel.java       # 广告返回模型
│   ├── BeanUtils.java               # Bean转换工具
│   └── LoginResult.java             # 登录结果模型
├── param                 # 参数类
│   ├── AdvertisementParam.java       # 广告参数
│   ├── AdvertisementQueryParam.java  # 广告查询参数
│   └── LoginParam.java              # 登录参数
└── service              # 服务层
    ├── AdvertisementService.java     # 广告服务接口
    └── UserService.java              # 用户服务接口
```

## 核心功能模块

### 1. 广告管理模块
- **广告查询**: 支持分页查询、条件筛选（ID、标题、类型、投放类型等）
- **广告操作**: 新增、更新、删除广告
- **广告投放**: 投放状态管理，查询最新投放广告

### 2. 用户认证模块
- **用户登录**: 用户名密码认证
- **跨域支持**: 配置了跨域资源共享（CORS）

### 3. 文件上传模块
- **文件类型**: 支持图片（jpg, png, gif等）和视频（mp4, avi, mov等）
- **文件大小**: 图片最大10MB，视频最大100MB
- **文件组织**: 按日期和类型自动分类存储

### 4. 通用功能模块
- **统一响应格式**: 使用`Result`类封装所有API响应
- **分页支持**: 标准分页参数和结果封装
- **错误处理**: 统一异常处理和状态码管理

## API接口文档

### 广告管理接口
- `POST /api/advertisement/list` - 分页查询广告
- `GET /api/advertisement/{id}` - 根据ID查询广告
- `POST /api/advertisement/save` - 保存广告（新增/更新）
- `POST /api/advertisement/putAds/{id}` - 投放广告
- `GET /api/advertisement/queryLastAds` - 查询最新广告

### 用户接口
- `POST /api/user/login` - 用户登录

### 文件上传接口
- `POST /api/upload/file` - 上传文件（自动识别图片/视频）

## 实体设计

### 广告实体（Advertisement）
```java
{
  "id": "BigInteger",           // 广告ID
  "title": "String",            // 广告标题
  "advertisementType": "String", // 广告类型（text/video/picture）
  "putType": "String",          // 投放类型（mobile/clothes/photography/computer）
  "putFlag": "Boolean",         // 是否投放
  "content": "String",          // 广告内容（文本内容或文件URL）
  "createTime": "Date"          // 创建时间
}
```

### 用户实体（User）
```java
{
  "id": "BigInteger",           // 用户ID
  "username": "String",         // 用户名
  "password": "String",         // 密码
  "status": "Integer",          // 状态（0-禁用，1-启用）
  "createTime": "Date",         // 创建时间
  "updateTime": "Date"          // 更新时间
}
```

# 广告管理网站前端项目

## 项目概述

这是一个基于 Vue 3 的广告管理网站前端系统，提供广告的创建、查看、投放和管理功能。系统包含用户认证、广告列表展示、文件上传等核心功能，采用现代化的UI设计。

## 技术栈

- **前端框架**: Vue 3 + Composition API
- **路由管理**: Vue Router
- **HTTP客户端**: Axios
- **样式**: 原生CSS + Flexbox/Grid布局
- **构建工具**: Vite 

## 项目结构

```
src/
├── components/
│   ├── AdsList.vue          # 广告列表主组件
│   ├── Login.vue            # 登录页面
│   ├── MainLayout.vue       # 主布局组件
│   ├── MainPage.vue         # 主页面容器
│   ├── common/
│   │   ├── Modal.vue        # 通用模态框组件
│   │   ├── Sidebar.vue      # 侧边栏导航
│   │   └── Topbar.vue       # 顶部导航栏
├── router/
│   └── routes.js           # 路由配置
├── api/
│   └── api.js              # API接口封装
├── utils/
│   ├── http.js             # HTTP客户端封装
│   └── date.js             # 日期格式化工具
├── App.vue                 # 根组件
├── main.js                 # 应用入口
└── style.css              # 全局样式
```

## 功能特性

### 1. 用户认证
- 登录页面：用户名/密码验证
- 路由守卫：保护需要认证的路由
- 本地存储：保持用户会话状态
- 登出功能

### 2. 广告管理
- **广告列表**：
  - 分页显示广告记录
  - 按广告类型、投放类型筛选展示
  - 投放状态切换
  - 表格响应式布局

- **创建广告**：
  - 支持文本、图片、视频三种广告类型
  - 动态表单验证
  - 图片/视频文件上传（带预览）
  - 文件类型和大小验证
  - 投放类型选择（手机、服装、摄影、笔记本）

### 3. 文件上传
- 图片上传：支持常见图片格式，最大10MB
- 视频上传：支持常见视频格式，最大100MB
- 实时预览功能
- 上传进度反馈

### 4. 响应式设计
- 桌面端优化布局
- 移动端适配
- 现代化UI组件
- 优雅的交互动效

## 核心组件说明

### AdsList.vue
主广告管理组件，包含：
- 广告数据获取和分页
- 新建广告表单（根据类型动态切换）
- 文件上传处理
- 投放操作
- 错误处理和用户反馈

### Modal.vue
可复用模态框组件：
- 支持标题、内容插槽
- 确定/取消操作
- 加载状态指示
- 动画过渡效果

### Sidebar.vue
侧边导航栏：
- 可折叠的菜单项
- 路由状态高亮
- 响应式设计（移动端隐藏）

### Topbar.vue
顶部导航栏：
- 显示当前用户
- 登出功能
- 品牌标识

## API接口封装

### HTTP客户端 (http.js)
- 统一错误处理
- 请求/响应拦截器
- FormData自动处理
- 超时和重试配置

### API模块 (api.js)
封装的接口包括：
- `userApi.login()` - 用户登录
- `adsApi.list()` - 获取广告列表
- `adsApi.save()` - 创建/保存广告
- `adsApi.putAds()` - 投放广告
- `uploadApi.upload()` - 文件上传

## 路由配置

### 路由规则
- `/` → 重定向到登录页
- `/login` → 登录页面
- `/main` → 主管理页面（需要认证）

### 路由守卫
- 访问需要认证的页面时检查本地存储
- 已登录用户访问登录页时重定向到主页面


### 功能流程
1. **登录**：输入用户名密码登录系统
2. **查看广告列表**：进入主页面查看所有广告
3. **创建广告**：
   - 点击"新建"按钮
   - 选择广告类型（文本/图片/视频）
   - 填写必要信息
   - 上传对应文件（图片或视频）
   - 保存广告
4. **投放广告**：在列表中对已创建的广告进行投放操作
5. **登出**：点击顶部导航栏的登出按钮
   
# 🛒 购物网站系统（Shop System）

## 🎯 项目简介
本项目是一个基于 **Spring Boot + Vue 3** 的前后端分离电子商务系统，  
实现了商品浏览、搜索筛选、商品详情、购物车管理等核心电商功能。  
系统结构清晰、功能完整、界面现代化，适用于课程设计、期末大作业及中小型电商项目实践。

---

## ✨ 项目功能概览
- 商品分页展示与分类筛选
- 商品名称模糊搜索
- 商品详情页展示
- 购物车管理与价格统计
- 图片上传与静态资源访问
- 响应式布局，适配多端设备

---

## 🏗️ 技术架构

### 后端技术栈
| 技术 | 版本 | 说明 |
|---|---|---|
| Spring Boot | 2.x | 后端核心框架 |
| MyBatis | 3.x | 持久层框架 |
| MySQL | 8.0 | 关系型数据库 |
| Maven | - | 项目依赖管理 |

### 前端技术栈
| 技术 | 版本 | 说明 |
|---|---|---|
| Vue | 3.5+ | 前端框架 |
| Vite | 7.2+ | 构建工具 |
| Vue Router | 4.x | 路由管理 |
| Axios | 1.x | HTTP 请求 |
| CSS3 | - | 页面样式 |

---

## 📁 项目结构

### 后端结构（`com.shop_sys`）
```text
com.shop_sys/
├── common/                # 公共工具类
│   ├── PageParam.java
│   ├── PageResult.java
│   ├── Result.java
│   └── ResultCode.java
├── config/                # 配置类
│   └── WebMvcConfig.java
├── controller/            # 控制层
│   └── ProductController.java
├── service/               # 业务接口
│   └── ProductService.java
├── service/impl/          # 业务实现
│   └── ProductServiceImpl.java
├── dao/                   # 数据访问层
│   └── ProductMapper.java
├── entity/                # 实体类
│   └── Product.java
├── model/                 # 返回模型
│   └── ProductModel.java
├── param/                 # 参数封装
│   └── ProductQueryParam.java
└── ShopSysApplication.java
````markdown
## 📁 前端结构（src）
```text
src/
├── api/                   # 接口封装
│   └── product.js
├── assets/                # 静态资源
│   ├── images/
│   └── styles/
├── components/            # 公共组件
│   ├── ProductCard.vue
│   ├── Header.vue
│   ├── Footer.vue
│   └── Pagination.vue
├── router/
│   └── index.js
├── utils/
│   ├── request.js
│   ├── validator.js
│   └── helper.js
├── views/
│   ├── Home.vue
│   ├── ProductList.vue
│   ├── ProductDetail.vue
│   └── Cart.vue
├── store/
│   └── index.js
├── App.vue
└── main.js
````

---

## 🚀 核心功能模块

### 1️⃣ 商品列表模块（ProductList.vue）

* 商品分页展示（支持自定义页大小）
* 商品分类筛选
* 商品名称模糊搜索
* 响应式布局设计
* 商品卡片展示（图片 / 名称 / 价格）

---

### 2️⃣ 商品详情模块（ProductDetail.vue）

* 商品完整信息展示
* 多图片展示
* 实时库存显示
* 加入购物车功能

---

### 3️⃣ 购物车模块（Cart.vue）

* 商品添加 / 删除 / 数量修改
* 自动计算商品总价
* 库存实时校验
* 一键结算功能

---

### 4️⃣ 公共组件

* **Header**：导航栏 + 搜索功能
* **ProductCard**：商品卡片组件
* **Pagination**：分页组件
* **Footer**：页面底部信息展示

---

## 🔌 API 接口设计

### 商品接口

* `GET /api/product/list`
* `GET /api/product/detail?id=1`

### 统一响应格式

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {}
}
```

---

## 📊 数据库设计

### 商品表（product）

```sql
CREATE TABLE `product` (
  `id` bigint NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  `product_type` varchar(50) NOT NULL COMMENT '商品类型',
  `product_name` varchar(100) DEFAULT NULL COMMENT '商品名称',
  `describe` varchar(255) DEFAULT NULL COMMENT '商品描述',
  `img_url` varchar(255) DEFAULT NULL COMMENT '图片路径',
  `product_price` decimal(10,2) DEFAULT NULL COMMENT '商品价格',
  `product_count` int DEFAULT NULL COMMENT '库存数量',
  `create_time` datetime DEFAULT NULL COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

---

## ⚙️ 配置说明

### 后端配置（application.properties）

```properties
server.port=8083
spring.datasource.url=jdbc:mysql://localhost:3306/shop_db
spring.datasource.username=root
spring.datasource.password=your_password
```

### 前端代理配置（vite.config.js）

```js
server: {
  proxy: {
    '/api': {
      target: 'http://localhost:8083',
      changeOrigin: true,
      rewrite: path => path.replace(/^\/api/, '')
    }
  }
}
```

```
```


# 📹 视频分享网站

## 🎯 项目概述

一个功能完善的视频分享平台，采用前后端分离架构，支持视频浏览、搜索筛选、详情查看等功能，并集成广告投放模块。项目提供流畅的用户体验和现代化的界面设计。

### ✨ 核心特性
- ✅ **完整的视频管理** - 分页浏览、条件筛选、详情查看
- ✅ **响应式设计** - 完美适配桌面端和移动端
- ✅ **广告集成** - 智能广告投放与展示
- ✅ **高性能架构** - Vue 3 + Vite + Spring Boot
- ✅ **统一的API设计** - RESTful API + 统一响应格式
- ✅ **文件管理** - 视频上传、存储与访问

## 🏗️ 技术架构

### 后端技术栈
| 技术 | 版本 | 用途 |
|------|------|------|
| Spring Boot | 2.x | 后端框架 |
| MySQL | 8.0 | 数据库 |
| MyBatis | 3.x | ORM框架 |
| Maven | - | 依赖管理 |

### 前端技术栈
| 技术 | 版本 | 用途 |
|------|------|------|
| Vue 3 | 3.5+ | 前端框架 |
| Vite | 7.2+ | 构建工具 |
| Vue Router | 4.6+ | 路由管理 |
| Axios | 1.13+ | HTTP客户端 |
| CSS3 | - | 样式设计 |

## 📁 项目结构详解

### 前端项目结构 (`src/`)
```
src/
├── api/                          # API接口管理
│   └── api.js                   # 统一的API请求封装
├── assets/                      # 静态资源
│   └── image.png               # 项目图片资源
├── components/                  # 可复用组件
│   ├── AdSidebar.vue           # 广告侧边栏组件
│   └── Header.vue              # 顶部导航组件
├── router/                      # 路由配置
│   └── index.js                # 路由定义与配置
├── utils/                       # 工具函数
│   ├── date.js                 # 日期处理工具
│   ├── http.js                 # HTTP请求工具
│   └── storage.js              # 本地存储工具
├── views/                       # 页面视图
│   ├── VideoDetail.vue         # 视频详情页
│   └── VideoList.vue           # 视频列表页
├── App.vue                      # 根组件
├── main.js                      # 应用入口文件
└── style.css                    # 全局样式文件
```

### 后端项目结构 (`com.video_sys`)
```
com.video_sys/
├── common/                      # 公共基础类
│   ├── PageParam.java          # 分页参数基类
│   ├── PageResult.java         # 分页结果封装
│   ├── Result.java             # 统一响应格式
│   └── ResultCode.java         # 状态码枚举
├── config/                      # 配置类
│   └── WebMvcConfig.java       # Web MVC配置
├── controller/                  # 控制器层
│   └── VideoController.java    # 视频相关API
├── service/                     # 服务层接口
│   └── VideoService.java       # 视频业务接口
├── service/impl/                # 服务层实现
│   └── VideoServiceImpl.java   # 视频业务实现
├── dao/                         # 数据访问层
│   └── VideoMapper.java        # 视频数据操作
├── entity/                      # 实体类
│   └── Video.java              # 视频实体
├── model/                       # 数据模型
│   └── VideoModel.java         # 视频返回模型
├── param/                       # 参数类
│   └── VideoParam.java         # 视频查询参数
└── VideoSysApplication.java     # Spring Boot启动类
```

## 🚀 核心功能模块

### 1. 视频列表模块 (`VideoList.vue`)
- **分页展示** - 支持自定义每页显示数量
- **智能筛选** - 按视频类型、名称、发布者筛选
- **响应式布局** - 自适应不同屏幕尺寸
- **懒加载** - 滚动加载更多内容

### 2. 视频详情模块 (`VideoDetail.vue`)
- **完整信息展示** - 标题、时长、描述、发布时间
- **视频播放器** - 内嵌视频播放功能
- **相关推荐** - 智能推荐相关视频
- **交互功能** - 收藏、分享、评论

### 3. 广告系统 (`AdSidebar.vue`)
- **智能投放** - 根据内容匹配广告
- **多种形式** - 支持图片、视频等广告类型
- **统计分析** - 广告曝光与点击统计
- **定时刷新** - 广告内容轮播展示

### 4. 公共组件
- **Header组件** - 统一的导航栏，支持搜索功能
- **AdSidebar组件** - 灵活的广告展示区域
- **API封装** - 统一的请求拦截与错误处理

## 🔧 配置说明

### 开发环境配置
```javascript
// vite.config.js
export default defineConfig({
  server: {
    host: '0.0.0.0',     // 允许所有IP访问
    port: 5176,          // 前端开发端口
    proxy: {             // API代理配置
      '/api/advertisement': {
        target: 'http://10.100.164.8',  // 广告API服务
        changeOrigin: true,
      },
      '/api': {
        target: 'http://localhost:8084', // 后端API服务
        changeOrigin: true,
        rewrite: path => path.replace(/^\/api/, '')
      }
    }
  }
})
```

### 应用配置文件
```properties
# application.properties
# 服务器配置
server.port=8084
spring.application.name=video-sys

# 数据库配置
spring.datasource.url=jdbc:mysql://localhost:3306/video_db
spring.datasource.username=root
spring.datasource.password=123456

# 文件上传配置
spring.servlet.multipart.max-file-size=200MB
file.upload.path=uploads
video.access.base-url=http://localhost:8084/uploads/videos/
```

## 📊 数据库设计

### 核心表结构
```sql
-- 视频表
CREATE TABLE `video` (
  `id` bigint PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
  `video_type` varchar(50) NOT NULL COMMENT '视频类型',
  `video_name` varchar(500) COMMENT '视频名称',
  `video_publisher` varchar(50) COMMENT '视频发布者',
  `video_url` varchar(255) COMMENT '视频路径',
  `duration` varchar(10) COMMENT '视频时长',
  `video_describe` varchar(255) COMMENT '视频描述',
  `create_time` datetime COMMENT '创建时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
```

## 🌈 设计系统

### 色彩方案
```css
:root {
  --primary-color: #667eea;      /* 主色调 - 蓝色 */
  --primary-dark: #764ba2;       /* 深色主调 - 紫色 */
  --secondary-color: #FF6B6B;    /* 辅助色 - 红色 */
  --text-primary: #2c3e50;       /* 主要文字 */
  --text-secondary: #5f6368;     /* 次要文字 */
  --bg-primary: #f5f7fa;         /* 背景色 */
  --bg-white: #ffffff;           /* 白色背景 */
}
```

### 布局系统
- **栅格系统** - 灵活的内容布局
- **响应式断点** - 768px(平板)、1200px(桌面)
- **间距系统** - 统一的边距和填充
- **圆角系统** - 8px、12px、16px、25px四级圆角

### 动画效果
```css
/* 渐入动画 */
@keyframes fadeIn {
  from { opacity: 0; transform: translateY(20px); }
  to { opacity: 1; transform: translateY(0); }
}

/* 滑动动画 */
@keyframes slideIn {
  from { opacity: 0; transform: translateX(-20px); }
  to { opacity: 1; transform: translateX(0); }
}

/* 缩放动画 */
@keyframes scaleIn {
  from { opacity: 0; transform: scale(0.9); }
  to { opacity: 1; transform: scale(1); }
}
```

## 🔌 API接口设计

### 视频相关接口
```javascript
// API路径前缀: /api/video

// 1. 分页查询视频列表
GET /list?pageNum=1&pageSize=10&videoType=电影&videoName=教程

// 2. 获取视频详情
GET /detail?id=1

// 响应格式
{
  "code": 200,
  "message": "操作成功",
  "data": {
    // 视频数据
  }
}
```

### 状态码定义
| 状态码 | 含义 | 说明 |
|--------|------|------|
| 200 | SUCCESS | 操作成功 |
| 400 | PARAM_ERROR | 参数错误 |
| 401 | UNAUTHORIZED | 未授权 |
| 404 | NOT_FOUND | 资源不存在 |
| 500 | ERROR | 服务器错误 |

## 🚀 启动与部署

### 本地开发
```bash
# 1. 启动后端服务
cd video-sys-backend
mvn spring-boot:run

# 2. 启动前端开发服务器
cd video-web
npm install
npm run dev

# 3. 访问应用
# 前端: http://localhost:5176
# 后端: http://localhost:8084
```

### 生产构建
```bash
# 1. 构建前端
npm run build

# 2. 打包后端
mvn clean package -DskipTests

# 3. 部署后端JAR包
java -jar target/video-sys-*.jar
```

## 📱 移动端适配

项目完全响应式设计，支持：
- 📱 **手机端** - 单列布局，触摸友好
- 📱 **平板端** - 双列布局，优化空间利用
- 🖥️ **桌面端** - 多列布局，充分利用大屏空间

---

**项目亮点**: 
- 🚀 现代化技术栈，开发体验优秀
- 🎨 精美的UI设计和流畅的动画
- 🔧 完善的工程化配置和代码结构
- 📱 全平台响应式支持
- 📊 清晰的数据流和状态管理


# 📰 新闻管理系统 (News Management System)

## 📋 项目概述

新闻管理系统是一个基于Spring Boot + Vue.js的全栈Web应用，主要用于新闻内容的发布、管理和展示。系统采用前后端分离架构，后端提供RESTful API服务，前端使用Vue 3构建现代化的单页面应用。项目实现了新闻的分页查询、详情查看、多媒体广告展示等核心功能，支持多种新闻类型（手机、摄影、服装、电脑等）的分类管理。

## ✨ 核心功能

### 🗞️ 新闻管理功能
- **新闻列表分页查询**：支持按新闻类型、标题、标签、来源等多种条件筛选，实现高效的数据展示
- **新闻详情查看**：展示新闻完整内容，支持HTML富文本渲染，包括图片、排版等多媒体内容
- **图片上传与展示**：支持新闻封面图的上传和动态访问，图片存储在服务器指定目录
- **智能标签分类**：支持多标签分类系统，如"手机|新闻"、"图片|新闻"等，便于内容组织

### 📺 广告管理功能
- **智能广告投放**：根据新闻类型动态显示相关广告内容，提高广告转化率
- **多媒体广告支持**：支持图片、视频、文本三种广告类型的展示和播放控制
- **实时广告查询**：实时获取最新的相关广告内容，确保广告信息的时效性

### 🎯 用户体验特性
- **响应式设计**：全面适配桌面电脑、平板设备和移动手机，提供一致的用户体验
- **现代化交互界面**：采用Vue 3 Composition API构建，界面美观、交互流畅
- **平滑页面导航**：支持路由跳转和浏览状态保持，提升用户浏览体验
- **搜索功能**：提供新闻标题的搜索功能（前端实现），方便用户快速查找内容

## 🏗️ 项目架构设计

### 总体架构概述
本项目采用前后端分离的架构模式。后端使用Spring Boot框架构建RESTful API服务，前端使用Vue 3框架构建现代化的单页面应用。两者通过HTTP/JSON协议进行通信，实现了关注点的完全分离。这种架构设计使得前后端可以独立开发、测试和部署，提高了开发效率和系统的可维护性。

### 后端架构设计
后端采用经典的三层架构设计模式。表现层（Controller层）负责接收HTTP请求、解析参数并返回JSON响应；业务逻辑层（Service层）包含所有的业务规则和逻辑处理；数据访问层（DAO层）负责与数据库进行交互，使用MyBatis作为ORM框架。数据模型层贯穿整个架构，包括实体类、数据传输对象和查询参数对象。

### 前端架构设计
前端基于Vue 3的Composition API构建，采用组件化开发模式。组件层将用户界面拆分为独立、可复用的组件；页面视图层组织各个组件形成完整页面；API服务层封装所有与后端通信的逻辑；工具层提供各种辅助函数。这种分层设计提高了代码的复用性和可维护性。

## 🛠️ 技术栈选型

### 后端技术栈
- **核心框架**：Spring Boot 2.6.13 - 提供快速开发的企业级框架
- **数据库**：MySQL 8.0.33 - 关系型数据库管理系统
- **ORM框架**：MyBatis 2.2.2 + MyBatis-Spring-Boot-Starter - 轻量级持久层框架
- **连接池**：Druid 1.2.16 - 阿里巴巴开源的高性能数据库连接池
- **开发语言**：Java 1.8 - 稳定成熟的企业级编程语言
- **构建工具**：Maven 3.8.1 - 项目构建和依赖管理

### 前端技术栈
- **核心框架**：Vue 3.5.24 + Composition API - 渐进式JavaScript框架
- **构建工具**：Vite 7.2.4 - 下一代前端构建工具
- **路由管理**：Vue Router 4.6.3 - Vue.js官方路由管理器
- **HTTP客户端**：Axios 1.13.2 - 基于Promise的HTTP客户端
- **开发语言**：JavaScript (ES6+) - 现代Web开发语言

### 开发环境
- **开发工具**：IntelliJ IDEA（后端） + VS Code（前端）
- **数据库工具**：MySQL Workbench / Navicat
- **API测试工具**：Postman / Insomnia
- **版本控制系统**：Git + GitHub
- **操作系统**：Windows/Linux/macOS

## 📊 数据库设计

### 主要数据表结构

#### `news` 新闻表（核心数据表）
存储新闻文章的基本信息和内容，包括标题、描述、封面图、内容、来源等字段。支持按类型分类和多标签管理，使用UTF8MB4字符集确保多语言支持。

#### `advertisement` 广告表（根据API推断）
存储广告信息，支持图片、视频、文本三种广告类型。包含投放类型字段（关联新闻类型）、播放控制字段（静音、循环、自动播放）等，实现智能广告投放。

### 数据关系设计
新闻表和广告表通过`news_type`和`put_type`字段建立关联关系，实现基于新闻类型的智能广告匹配。这种设计使得系统能够根据用户当前浏览的新闻类型，动态显示相关的广告内容。

## 🚀 快速开始指南

### 环境要求
- **Java环境**：JDK 1.8 或更高版本
- **Node.js环境**：Node.js 16.x 或更高版本
- **数据库**：MySQL 8.0 或更高版本
- **构建工具**：Maven 3.8+ 和 npm 8+

### 后端部署步骤

1. **项目克隆与准备**
   ```bash
   git clone <repository-url>
   cd news_sys
   ```

2. **数据库初始化**
   ```sql
   -- 创建数据库
   CREATE DATABASE news_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
   
   -- 导入数据表结构
   USE news_db;
   SOURCE news.sql;
   ```

3. **配置文件修改**
   编辑 `src/main/resources/application.properties` 文件，更新数据库连接信息和文件上传路径：
   ```properties
   spring.datasource.url=jdbc:mysql://localhost:3306/news_db
   spring.datasource.username=your_username
   spring.datasource.password=your_password
   file.upload.path=/path/to/your/uploads/folder
   ```

4. **项目构建与运行**
   ```bash
   # 使用Maven构建项目
   mvn clean package
   
   # 运行项目
   java -jar target/news_sys-0.0.1-SNAPSHOT.jar
   
   # 或者使用开发模式运行
   mvn spring-boot:run
   ```

### 前端部署步骤

1. **进入前端目录**
   ```bash
   cd news-web
   ```

2. **安装项目依赖**
   ```bash
   npm install
   ```

3. **环境变量配置**
   创建 `.env` 文件并配置后端API地址：
   ```env
   VITE_API_BASE_URL=http://localhost:8082
   ```

4. **启动开发服务器**
   ```bash
   npm run dev
   ```

5. **构建生产版本**
   ```bash
   npm run build
   # 生成的静态文件位于 dist/ 目录
   ```

6. **访问应用**
   - 开发环境：http://localhost:5173
   - 生产环境：部署到Web服务器（如Nginx）

## 🔧 开发指南

### 项目结构说明

#### 后端项目结构
```
news_sys/                              # 后端Spring Boot项目根目录
├── src/main/java/com/news_sys/        # Java源代码目录
│   ├── common/                        # 公共组件包
│   │   ├── PageParam.java            # 分页参数基类
│   │   ├── PageResult.java           # 分页结果封装类
│   │   ├── Result.java               # 统一响应封装类
│   │   └── ResultCode.java           # 响应状态码枚举
│   ├── config/                       # 配置类包
│   │   └── WebMvcConfig.java         # Web MVC配置类
│   ├── controller/                   # 控制器层
│   │   └── NewsController.java       # 新闻控制器
│   ├── dao/                         # 数据访问层
│   │   └── NewsMapper.java          # MyBatis映射接口
│   ├── entity/                      # 实体类包
│   │   └── News.java                # 新闻实体类
│   ├── model/                       # 模型类包
│   │   └── NewsModel.java           # 新闻数据传输模型
│   ├── param/                       # 参数类包
│   │   └── NewsQueryParam.java      # 新闻查询参数类
│   └── service/                     # 业务逻辑层
│       ├── NewsService.java         # 新闻服务接口
│       └── impl/                    # 服务实现目录
│           └── NewsServiceImpl.java # 新闻服务实现类
└── src/main/resources/              # 资源文件目录
    ├── mapper/                      # MyBatis映射文件目录
    │   └── NewsMapper.xml           # 新闻数据映射文件
    ├── application.properties       # 应用配置文件
    └── news.sql                    # 数据库初始化脚本
```

#### 前端项目结构
```
news-web/                            # 前端Vue项目根目录
├── src/                            # 源代码目录
│   ├── components/                 # 可复用组件目录
│   │   ├── Header.vue             # 页面头部组件
│   │   └── AdSidebar.vue          # 广告侧边栏组件
│   ├── views/                     # 页面视图目录
│   │   ├── NewsList.vue           # 新闻列表页面
│   │   └── NewsDetail.vue         # 新闻详情页面
│   ├── api/                       # API接口目录
│   │   ├── api.js                # API接口封装
│   │   └── http.js               # HTTP客户端配置
│   ├── utils/                     # 工具函数目录
│   │   ├── date.js               # 日期格式化工具
│   │   ├── http.js               # HTTP请求封装
│   │   └── storage.js            # 本地存储管理
│   ├── router/                   # 路由配置目录
│   │   └── index.js             # 路由配置文件
│   ├── assets/                   # 静态资源目录
│   ├── App.vue                   # 根组件
│   └── main.js                   # 应用入口文件
├── package.json                  # 项目配置和依赖管理
├── vite.config.js               # Vite构建配置
└── index.html                   # 应用入口HTML文件
```

### API接口文档

#### 新闻相关接口

**1. 获取新闻列表接口**
- **请求方式**：GET
- **接口地址**：`/api/news/list`
- **请求参数**：
  ```json
  {
    "pageNum": 1,        // 当前页码，从1开始
    "pageSize": 10,      // 每页显示数量
    "newsType": "mobile", // 新闻类型（可选）
    "title": "关键词",    // 标题关键词（可选，模糊查询）
    "label": "手机",      // 标签（可选）
    "source": "CNMO"     // 新闻来源（可选）
  }
  ```
- **响应格式**：
  ```json
  {
    "code": 200,
    "message": "操作成功",
    "data": {
      "list": [
        {
          "id": 1,
          "newsType": "mobile",
          "title": "新闻标题",
          "describe": "新闻描述",
          "imgUrl": "/uploads/images/xxx.png",
          "label": "手机|新闻",
          "content": "新闻内容...",
          "source": "CNMO",
          "createTime": "2025-11-21T09:40:51"
        }
      ],
      "total": 100,
      "pageNum": 1,
      "pageSize": 10,
      "totalPages": 10
    }
  }
  ```

**2. 获取新闻详情接口**
- **请求方式**：GET
- **接口地址**：`/api/news/detail`
- **请求参数**：`id`（新闻ID，必需）
- **响应格式**：返回单个新闻对象的完整信息

#### 广告相关接口

**1. 查询最新广告接口**
- **请求方式**：GET
- **接口地址**：`/api/advertisement/queryLastAds`
- **请求参数**：`putType`（投放类型，对应新闻类型）
- **响应格式**：返回最新的相关广告信息

### 开发规范

#### 命名规范
- **Java类命名**：采用大驼峰命名法，如`NewsController`、`NewsServiceImpl`
- **变量和方法命名**：采用小驼峰命名法，如`getNewsList`、`newsItem`
- **数据库表名和字段名**：采用小写字母加下划线，如`news_table`、`create_time`
- **Vue组件命名**：采用大驼峰命名法，如`NewsList.vue`、`AdSidebar.vue`
- **CSS类名命名**：采用短横线连接，如`news-item`、`detail-header`

#### 代码规范
- **后端代码**：遵循阿里巴巴Java开发手册规范
- **前端代码**：使用ESLint进行代码规范检查
- **API设计**：遵循RESTful设计原则，使用合适的HTTP方法
- **错误处理**：统一使用`Result`类封装响应，包含状态码和消息
- **异步处理**：前端统一使用async/await处理异步操作
- **注释规范**：重要的类、方法、复杂逻辑需要添加注释说明

#### 提交规范
- **提交信息格式**：`<type>(<scope>): <subject>`
- **类型说明**：
  - feat: 新功能
  - fix: 修复bug
  - docs: 文档更新
  - style: 代码格式调整
  - refactor: 代码重构
  - test: 测试相关
  - chore: 构建过程或辅助工具变动

## 📈 开发进度与计划

### 已完成功能模块
- ✅ 后端基础架构搭建和配置
- ✅ 数据库设计和表结构实现
- ✅ 新闻管理核心功能开发
- ✅ 统一响应封装和异常处理
- ✅ 分页查询功能支持
- ✅ 前端Vue3项目初始化和配置
- ✅ 路由系统设计和实现
- ✅ 新闻列表页面开发
- ✅ 新闻详情页面开发
- ✅ 广告组件开发
- ✅ 响应式布局适配

### 当前开发状态
项目已完成基础版本开发，具备新闻展示、分类浏览、详情查看、广告展示等核心功能。前后端已实现完整的数据交互，能够稳定运行。代码结构清晰，便于后续功能扩展和维护。

### 后续开发计划
- 🔄 **用户认证模块**：实现用户注册、登录、权限管理功能
- 🔄 **后台管理系统**：开发新闻发布、编辑、删除的管理后台
- 🔄 **新闻发布功能**：实现新闻内容的在线编辑和发布
- 🔄 **评论互动系统**：添加用户评论、回复、点赞功能
- 🔄 **收藏与分享**：实现新闻收藏、社交媒体分享功能
- 🔄 **搜索功能优化**：后端实现全文搜索，提升搜索准确性和性能
- 🔄 **数据统计报表**：开发访问统计、用户行为分析报表
- 🔄 **性能优化**：缓存策略优化、数据库查询优化、前端加载优化
- 🔄 **移动端适配**：进一步优化移动端用户体验

## 🧪 测试指南

### 单元测试
```bash
# 运行后端单元测试
mvn test

# 运行特定测试类
mvn test -Dtest=NewsServiceTest

# 生成测试报告
mvn surefire-report:report
```

### 集成测试
```bash
# 使用Postman进行API测试
# 导入项目提供的Postman集合文件
# 执行各个接口的测试用例
```

### 前端测试
```bash
# 运行前端单元测试（待配置）
npm run test:unit

# 运行端到端测试（待配置）
npm run test:e2e
```

### API测试示例
```bash
# 测试新闻列表接口
curl -X GET "http://localhost:8082/api/news/list?pageNum=1&pageSize=5"

# 测试新闻详情接口
curl -X GET "http://localhost:8082/api/news/detail?id=1"

# 测试广告查询接口
curl -X GET "http://localhost:8082/api/advertisement/queryLastAds?putType=mobile"
```




