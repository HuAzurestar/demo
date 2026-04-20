<!--
 * @Author: zhengbooleen zhengbooleen@gmail.com
 * @Date: 2026-04-08 21:53:44
 * @LastEditors: zhengbooleen zhengbooleen@gmail.com
 * @LastEditTime: 2026-04-08 21:55:25
 * @FilePath: \20260410-vant\README.md
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
-->
# 20260410

 Vue 2.7 + Vite 移动端 H5，配套 Node.js mock 与 Java 服务端实现。

## 目录结构

```
.
├── front/         # 前端工程（Vue 2.7 + Vite + TS）
├── node-server/   # Node.js mock 服务，提供本地接口
└── java-server/   # Java 服务端实现（Spring Boot）
```

## 子项目

- [front](./front/README.md) — 前端工程
- [node-server](./node-server/README.md) — 本地 mock 服务

## 快速开始

```bash
# 1. 启动后端服务（二选一，默认 80 端口）

# 1A. Node mock
cd node-server && npm start

# 1B. Java server
cd java-server && mvn spring-boot:run

# 2. 启动前端（默认 8080 端口）
cd front
pnpm install
pnpm dev
```

前端通过 Vite 代理 `/indicator` 到 `http://127.0.0.1:80`。联调时仅需启动一个后端（Node 或 Java），避免端口冲突。
