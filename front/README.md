<!--
 * @Author: zhengbooleen zhengbooleen@gmail.com
 * @Date: 2026-04-08 21:54:24
 * @LastEditors: zhengbooleen zhengbooleen@gmail.com
 * @LastEditTime: 2026-04-08 21:55:02
 * @FilePath: \20260410-vant\front\README.md
 * @Description: 这是默认设置,请设置`customMade`, 打开koroFileHeader查看配置 进行设置: https://github.com/OBKoro1/koro1FileHeader/wiki/%E9%85%8D%E7%BD%AE
-->
# front 

前端工程，Vue 2.7 + Vite 4 + TypeScript 移动端 H5。

## 技术栈

- Vue 2.7（Composition API + `<script setup>`，通过 `unplugin-auto-import`）
- Vite 4
- TypeScript
- Vue Router 3
- Axios
- Less + postcss-px-to-viewport（375 设计稿，自动 px → vw）
- Vant（按需引入）

## 目录结构

```
src/
├── apis/           # API 封装（axios 实例 + 业务接口）
├── assets/         # 静态资源、样式
├── components/
│   ├── common/     # 通用组件（BoardCard、RankItem、PageStatus）
│   └── featured-quotes/  # 精选行情业务组件
├── directives/     # 自定义指令（v-fontResize、v-redGreen）
├── router/         # 路由配置
├── types/          # TS 类型定义
├── utils/          # 工具函数（tools、jump、constant 等）
└── views/          # 页面
```

## 环境要求

- Node.js >= 14
- 推荐 pnpm

## 安装与启动

```bash
pnpm install        # 内部源在 miniapp.10jqka.com.cn
pnpm dev            # 开发：vite --mode dev，127.0.0.1:8080
pnpm testing        # 测试包
pnpm release        # 生产包
pnpm lint           # eslint --fix
```

> 没有配置测试运行器。

## 接口与代理

接口走统一的 `@/apis/http`（axios 实例），错误统一上报。本地开发时，vite 把 `/indicator` 代理到本机后端服务：

```ts
// vite.config.ts（节选）
proxy: {
  '/indicator': {
    target: 'http://127.0.0.1:80',
    changeOrigin: true,
    secure: false
  }
}
```

启动前端前请先启动 `node-server` 或 `java-server`（二选一，默认 80 端口），详见根目录 README。

当前 `FeaturedQuotes` 列表支持分页联动：
- 请求参数：`pageNum`（1-based）、`pageSize`
- 响应字段：`total`、`stock_list`
- 页码导航：顶部/底部共享 `首页 | 上一页 | 页码窗口 | 下一页 | 末页`
- 页码窗口规则：固定包含首页与末页，当前页展示 `前 3 页 + 当前页 + 后 3 页`，中间以省略号连接
- 底部额外展示：`当前页/最大页`、`总数`、`每页大小` 切换（10/20/50）

## 设计约定

- **组件设计**：通用组件保持纯展示（如 `RankItem` 通过 `@click` 抛事件，不感知业务跳转）；数据获取与跳转放在页面层（`views/`）；卡片容器统一用 `BoardCard`。
- **CSS 单位**：以 375 宽设计稿用 `px` 书写，由 postcss 自动转 `vw`。需要保持像素级精确（如 1px 边框）的元素加 `no-vw` 类。
- **自动导入**：`vue` / `vue-router` 的 API 和 `@atom/atom-ui` 组件无需手动 import，类型在 `src/types/auto-imports.d.ts`、`src/types/components.d.ts`。
- **路径别名**：`@` → `src`，`@c` → `src/components`，`_t` → `src/utils/tools.ts`。
- **接口加载**：接口的状态字段不统一，`http.ts` 依次探测 `status_code` / `errorcode` / `errorCode` / `code`，新增接口时不要假设单一字段。

## 主题

只支持亮色模式（`@/assets/style/theme/light.css`）。