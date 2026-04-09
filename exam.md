# 面试题

## 背景

- `java-server` 提供接口：`http://127.0.0.1/indicator/capital/v1/grey_rank`
- 返回数据结构：`data.stock_list` 为股票列表，每条包含 `stock_code`、`stock_name`、`turnover`、`main_grey_capital`、`main_listed_capital` 等字段
- 前端工程位于 `front/`，页面 `views/FeaturedQuotes.vue` 通过 `marketApi.getDarkPlateBoard` 调用该接口并以列表形式渲染

## 题目 1

调用 `http://127.0.0.1/indicator/capital/v1/grey_rank` 接口，修改 `stock_list` 逻辑： 
- 为每条股票数据新增「占比」字段，计算逻辑：
```
占比 = main_listed_capital / turnover * 100
```
- 按 `turnover` **降序**排序。

**要求**
- 「占比」字段数值保留 **2 位小数**
- 在页面列表中**新增一列**渲染该「占比」字段
- 数据为空或非法时显示 `--`
## 题目 2
当前页面是不分页的，请实现分页功能。

**要求**
- 编写一个分页skill完成代码开发。
- 前端页面、后端接口都需要实现分页。
