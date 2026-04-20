---
name: full-stack-pagination
description: >-
  Implement full-stack pagination for Vue frontend and Java backend.
  Includes page number/page size conventions, total count handling, boundary
  behavior, backend query pagination, and frontend pagination state + controls.
---

# Full-Stack Pagination Implementation (全栈分页实现)

Use this skill when the user asks to implement pagination end-to-end.

Although examples below use Vue + Java, the same pagination contract should be exposed for other frontend/backend stacks as well.

## 1) Core pagination contract

- `pageNum` is **1-based** (starts from `1`).
- `pageSize` default is `20`.
- Backend response must include:
  - `total` (total number of records)
  - current-page list field (e.g. `list`, `stock_list`)

## 2) Backend rules (Java example)

Apply these in Controller + Service (or Mapper/Repository):

1. Request params:
   - `pageNum` default `1`
   - `pageSize` default `20`
2. Pagination math:
   - `offset = (pageNum - 1) * pageSize`
   - `limit = pageSize`
3. Boundary handling:
   - Query `total` first.
   - If `total == 0`, return empty list immediately.
   - If `pageNum` exceeds max page (`Math.ceil(total / pageSize)`), return empty list but keep `total`.
4. Response shape:
   - Include `total` (Long)
   - Include business list field (e.g. `stock_list`)

## 3) Frontend rules (Vue example)

In page-level state:

- `currentPage = 1`
- `pageSize = 20`
- `total = 0`
- `pageSizeOptions = [10, 20, 50]`

API call requirements:

- Always send `pageNum` (`currentPage`) and `pageSize`.

UI requirements:

- Add a pagination area below the list.
- Support page-size switching (`10/20/50`).
- Support previous/next navigation.
- Disable boundary actions:
  - disable prev when `currentPage <= 1`
  - disable next when `currentPage >= Math.ceil(total / pageSize)`

Reactive behavior:

- Re-fetch data when `currentPage` changes.
- Re-fetch data when `pageSize` changes.
- When `pageSize` changes, reset `currentPage` to `1` before fetching.

## 4) Execution checklist

1. Add pagination request/response DTOs (or extend existing DTOs).
2. Update backend query flow to compute `total` + `offset/limit`.
3. Update frontend API declaration to pass pagination params.
4. Add page state + pagination UI + handlers in the target view/component.
5. Verify boundary scenarios:
   - first page / last page
   - total = 0
   - oversized `pageNum`

## 5) Output quality requirements

- Keep pagination contract consistent across endpoints.
- Avoid breaking existing business fields.
- Keep response backward-compatible when possible.
- Document any new request params and response fields in project docs.

