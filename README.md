# ATRS — Airline Ticket Reservation System

航空票务预订系统，基于 **SpringCloud 微服务架构** + **Vue3 前端** + **DeepSeek LLM 智能客服**。

## 技术栈

| 层级 | 技术 |
|------|------|
| 微服务框架 | Spring Boot 3.2 + Spring Cloud 2023 + Spring Cloud Alibaba 2023 |
| 服务注册/配置 | Nacos 2.3 |
| API 网关 | Spring Cloud Gateway |
| 服务调用 | OpenFeign + LoadBalancer |
| 分布式事务 | Seata 1.8 (AT 模式) |
| 熔断降级 | Resilience4j |
| 数据库 | MySQL 8.0，每服务独立数据库 |
| ORM | Spring Data JPA |
| 认证 | JWT（网关统一校验） |
| LLM | DeepSeek API（智能客服） |
| 前端 | Vue 3 + Vite + Element Plus + Pinia |

## 项目结构

```
ATRS/
├── atrs-common/              # 公共模块（实体、DTO、工具、Feign）
├── atrs-gateway/             # API 网关  :8080
├── atrs-user-service/        # 用户服务  :8081
├── atrs-flight-service/      # 航班服务  :8082
├── atrs-order-service/       # 订单服务  :8083  (Seata)
├── atrs-notification-service/# 通知服务  :8084
├── atrs-llm-service/         # LLM客服  :8085
├── atrs-frontend/            # Vue3 前端
└── docker-compose.yml        # 基础设施 (MySQL×4, Nacos, Seata)
```

## 快速开始

### 1. 启动基础设施

```bash
docker-compose up -d
```

### 2. 启动微服务（按顺序）

```bash
# 编译公共模块
cd atrs-common && mvn install -DskipTests && cd ..

# 启动各服务
cd atrs-gateway && mvn spring-boot:run && cd ..
cd atrs-user-service && mvn spring-boot:run && cd ..
cd atrs-flight-service && mvn spring-boot:run && cd ..
cd atrs-order-service && mvn spring-boot:run && cd ..
cd atrs-notification-service && mvn spring-boot:run && cd ..
cd atrs-llm-service && mvn spring-boot:run && cd ..
```

### 3. 启动前端

```bash
cd atrs-frontend
npm install
npm run dev
```

访问 http://localhost:5173

### 4. 默认测试账号

| 账号 | 密码 | 角色 |
|------|------|------|
| admin | admin123 | ADMIN |
| test | 123456 | USER |

### 5. LLM 智能客服

设置环境变量 `DEEPSEEK_API_KEY` 启用 AI 客服：

```bash
export DEEPSEEK_API_KEY=sk-your-key
```

## API 路由

所有请求通过 Gateway (`:8080`) 统一入口：

| 前缀 | 服务 | 说明 |
|------|------|------|
| `/api/user/**` | 用户服务 | 注册、登录、资料 |
| `/api/flight/**` | 航班服务 | 搜索、详情 |
| `/api/order/**` | 订单服务 | 下单、支付、取消 |
| `/api/notification/**` | 通知服务 | 消息通知 |
| `/api/llm/**` | LLM服务 | AI 客服对话 |

## SpringCloud 核心组件应用

- **Nacos**：服务注册与发现 + 配置中心
- **Gateway**：API 网关 + 智能路由 + JWT 认证
- **LoadBalancer**：客户端负载均衡
- **OpenFeign**：声明式服务调用（order → flight 扣减座位）
- **Seata**：分布式事务（下单：订单创建 + 座位扣减）
- **Resilience4j**：熔断降级保护
