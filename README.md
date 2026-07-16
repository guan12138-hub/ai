# AI 智能食材管理系统

> 基于 SpringBoot + Vue3 + MySQL + 微信小程序的 AI 智能食材管理平台  
> 完全匹配实训要求：Web 全套 AI 功能、Git 协作、适配 Coze 答辩

---

## 📋 技术栈

| 层级 | 技术 | 说明 |
|------|------|------|
| **后端** | SpringBoot 3.2 + MyBatis-Plus | RESTful API，ORM 操作 MySQL |
| **权限** | Sa-Token | 登录认证、角色区分（管理员/商户） |
| **Web 前端** | Vue3 + Vite + Element Plus | 响应式管理后台，ECharts 可视化 |
| **小程序** | UniApp (Vue3) | 一套代码多端运行 |
| **数据库** | MySQL 8.0 | 9 张核心表 |
| **AI 接口** | Coze / DeepSeek / 火山引擎 | 对话、文案生成、文生图 |
| **文件** | Apache POI | Excel 上传解析 |
| **CI/CD** | GitHub Actions | 自动构建 |

---

## 🚀 快速启动

### 1️⃣ 数据库初始化

```sql
-- 执行 sql/init.sql 完成建库建表
mysql -u root -p < sql/init.sql
```

### 2️⃣ 启动后端

```bash
cd food-backend
# 修改 application.yml 中的数据库连接和 AI API Key
mvn clean install -DskipTests
mvn spring-boot:run
# 后端运行在 http://localhost:8080
```

### 3️⃣ 启动 Web 前端

```bash
cd food-web
npm install
npm run dev
# 前端运行在 http://localhost:3000
```

### 4️⃣ 启动微信小程序

使用 HBuilderX 打开 `food-miniapp` 文件夹：
- 运行 → 运行到小程序模拟器 → 微信开发者工具

---

## 🗄️ 数据库表结构

| 表名 | 说明 |
|------|------|
| `user` | 用户表（管理员/商户） |
| `food_stock` | 食材库存表（名称、数量、保质期、预警阈值） |
| `purchase_order` | 采购订单表 |
| `purchase_order_item` | 采购订单明细表 |
| `supplier` | 供应商表 |
| `food_loss_data` | 食材损耗表 |
| `ai_chat_record` | AI 对话记录表 |
| `ai_generate_resource` | AI 生成资源表 |
| `ai_analysis_report` | AI 分析报表表 |

---

## 🤖 四大 AI 功能详解

### 1️⃣ AI 问答客服（Coze Bot 核心）

- 在 Coze 平台创建「食材管理机器人」，上传知识库
- 配置 `application.yml` 中的 Coze API 参数
- 前端输入问题 → 后端调用 Coze API → 返回答案并入库
- 支持多轮上下文记忆

### 2️⃣ AI 文章生成

- **菜谱生成**：根据食材清单创作菜谱
- **采购报表**：基于库存预警数据生成采购计划
- **损耗报告**：分析损耗原因并给出建议

### 3️⃣ AI 绘图

- 文生图 API（火山引擎/通义万相）
- 生成菜品海报、菜单示意图
- 图片 URL 存入数据库，前端直接渲染

### 4️⃣ AI 数据分析

- **损耗趋势**：按月统计损耗，AI 分析趋势
- **成本分析**：库存成本分布，优化采购策略
- **智能预警**：低库存 + 临期食材优先级建议
- 分析结果配合 ECharts 可视化展示

---

## 🔧 配置文件（application.yml）

```yaml
ai:
  coze:
    api-url: https://api.coze.cn/open_api/v2/chat
    bot-id: YOUR_BOT_ID
    api-key: YOUR_COZE_API_KEY
  volc:
    api-key: YOUR_VOLC_API_KEY
    image-api-url: https://api.volcengine.com/image/generate
  deepseek:
    api-url: https://api.deepseek.com/v1/chat/completions
    api-key: YOUR_DEEPSEEK_API_KEY
```

---

## 📁 项目结构

```
food_control/
├── food-backend/          # Java SpringBoot 后端
│   ├── pom.xml
│   └── src/main/java/com/food/
│       ├── controller/     # 接口层（7个Controller）
│       ├── service/        # 业务逻辑层
│       ├── service/ai/     # AI 核心服务
│       ├── mapper/         # MyBatis-Plus Mapper
│       ├── entity/         # 数据实体
│       ├── dto/            # 数据传输对象
│       ├── config/         # 配置类
│       └── util/           # 工具类
├── food-web/              # Vue3 管理后台
│   ├── src/views/         # 9个页面视图
│   ├── src/api/           # API 封装
│   ├── src/store/         # Pinia 状态
│   └── src/router/        # 路由配置
├── food-miniapp/          # 微信小程序（UniApp）
│   ├── src/pages/         # 7个小程序页面
│   └── src/api/           # API 封装
├── sql/                   # 数据库初始化脚本
└── .github/workflows/     # CI 配置
```

---

## 🌟 评优亮点

1. **Coze 机器人答辩** - 核心 AI 问答功能，完整对接 Coze API
2. **ECharts 可视化** - 库存分类饼图 + 损耗趋势分析
3. **Element Plus 美观 UI** - 渐变背景、卡片式布局、响应式设计
4. **多端覆盖** - Web 管理后台 + 微信小程序，同一后端
5. **Git 协作** - 分支开发、PR 流程、CI 自动构建
6. **Excel 上传** - POI 解析 Excel 进行 AI 分析
