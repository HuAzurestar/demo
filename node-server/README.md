# node-server

前端项目配套的 Node.js 本地 mock 服务。仅依赖 Node 内置模块（`http` / `fs`），**无需 `npm install`**。

## 启动

```bash
cd node-server
npm start
```

默认监听 `80` 端口，可通过环境变量覆盖：

```bash
PORT=4000 npm start
```

## 接口

### `GET /indicator/capital/v1/grey_rank`

读取并返回 `data.json` 的内容。修改 `data.json` 后下次请求即可生效，**无需重启**。

- 请求方法：`GET`
- 响应类型：`application/json`
- 已开启 `Access-Control-Allow-Origin: *`，允许跨域
- query string（如 `?_t=xxx`）会被忽略，匹配的是 `pathname`

成功响应：`data.json` 的原始内容。

失败响应：

```json
{ "status_code": -1, "status_msg": "read data.json failed" }
```

## 目录结构

```
node-server/
├── index.js       # 服务入口
├── data.json      # 接口返回的数据源
├── package.json
└── README.md
```

## 添加新接口

在 `index.js` 的 `createServer` 回调中按 `pathname` 增加分支即可。复杂场景建议引入 express/koa 后再扩展。
