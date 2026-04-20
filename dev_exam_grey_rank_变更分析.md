# dev_exam_grey_rank 相对 main 变更分析

本文档用于说明当前 `dev_exam_grey_rank` 分支相对 `main` 的核心差异，以及与题目一相关的落地范围。

## 1. 接口与业务逻辑变更（题目一）

- `java-server` 的 `grey_rank` 接口在 `data.stock_list` 增加了 `ratio_pct` 字段。
- `ratio_pct` 计算规则：`main_listed_capital / turnover * 100`，保留 2 位小数。
- `stock_list` 输出按 `turnover` 降序返回。
- 非法数据（空值、非数值、除数为 0）统一按 `null` 返回，由前端显示 `--`。

涉及代码：
- `java-server/src/main/java/com/myhexin/thsmember/model/dto/StockRankDTO.java`
- `java-server/src/main/java/com/myhexin/thsmember/service/impl/IndicatorServiceImpl.java`
- `java-server/src/test/java/com/myhexin/thsmember/controller/IndicatorControllerTest.java`

## 2. 前端展示变更（配套题目一）

- 暗盘榜 `StockList` 新增「占比(%)」展示列。
- 增加 `ratio_pct` 类型声明与格式化逻辑（非法显示 `--`）。

涉及代码：
- `front/src/components/featured-quotes/dark-board/StockList.vue`
- `front/src/types/market.d.ts`

## 3. Node mock 的处理策略

- 题目一最终口径为 **Java 接口改造**，因此 `node-server/index.js` 中一度加入的占比计算与排序逻辑已回退。
- 当前 `node-server` 恢复为读取并原样返回 `data.json`。

## 4. 运行与联调相关变更

- 文档统一为本地 `80` 端口代理 `/indicator`（前端、根 README、CLAUDE 文档已同步）。
- `java-server/pom.xml` 中移除了 `spring-boot-maven-plugin` 的 `<skip>true</skip>`，以保证可正常运行 Spring Boot 启动流程与可执行打包。

