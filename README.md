<!--
 * @Author: zhengbooleen zhengbooleen@gmail.com
 * @Date: 2026-04-08 21:53:44
 * @LastEditors: zhengbooleen zhengbooleen@gmail.com
 * @LastEditTime: 2026-04-08 21:55:25
 * @FilePath: \20260410-vant\README.md
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
-->
# 20260410

 Vue 2.7 + Vite 移动端 H5，配套一个 Node.js mock 服务。

## 目录结构

```
.
├── front/         # 前端工程（Vue 2.7 + Vite + TS）
└── node-server/   # Node.js mock 服务，提供本地接口
```

## 子项目

- [front](./front/README.md) — 前端工程
- [node-server](./node-server/README.md) — 本地 mock 服务

## 快速开始

```bash
# 1. 启动 mock 服务（默认 80 端口）
cd node-server
npm start

# 2. 启动前端（默认 8080 端口）
cd front
pnpm install
pnpm dev
```

本地开发时，`pnpm dev` 下 Vite 将请求路径 `/indicator` 代理到 `http://127.0.0.1:80`。在该端口启动 **`node-server`（mock）** 或 **`java-server`** 均可；二者接口路径一致，**同一时间只需运行其中一个**。在 Windows 上监听 80 端口可能需要**以管理员身份**运行终端，若无法绑定可改用环境变量指定其它端口（并同步修改 `java-server` 的 `application.yml` 与 `front/vite.config.ts` 中的代理地址）。

若使用 Java 后端联调，可在 `java-server` 目录执行：

```bash
mvn spring-boot:run
```
