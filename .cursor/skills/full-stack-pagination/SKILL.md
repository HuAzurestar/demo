---
name: full-stack-pagination
description: >-
  Implement full-stack pagination for Vue frontend and Java backend.
  Includes page number/page size conventions, total count handling, boundary
  behavior, backend query pagination, record index field, frontend rich pager
  controls, and comprehensive Java tests.
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
- Backend page item should include an `index` field (1-based absolute index in full dataset).

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
   - Include per-item `index` field
5. `index` rule:
   - For each item in current page:
     - `index = offset + localPosition + 1`
     - `localPosition` starts from `0`
6. GET verification after implementation:
   - `total` must be present and correct.
   - Returned list length must never exceed `pageSize`.
   - If requested page is **not** the last page, returned length must equal `pageSize`.

## 3) Frontend rules (Vue example)

In page-level state:

- `currentPage = 1`
- `pageSize = 20`
- `total = 0`
- `pageSizeOptions = [10, 20, 50]`

API call requirements:

- Always send `pageNum` (`currentPage`) and `pageSize`.

UI requirements:

- **Pagination Layout & Placement:**
  - Render pagination controls in both the **top** and **bottom** areas of the list.
  - **Shared Navigation Bar (Top & Bottom):** Both areas must render the identical navigation flow: `First | Previous | Number Window | Next | Last`.
  - **Bottom-Only Status Row:** Only the bottom area should include an additional row showing: `Current Page / Max Page`, `Total Items`, and the `Page-size selector`.

- **Shared Navigation Bar Rules (Top & Bottom):**
  - Example target layout: `First | Prev | 1 ... 3 4 5 6(current) 7 8 9 ... 79 | Next | Last`
  - Both bars must share identical page data, state, and click behaviors.
  - Disable boundary actions:
    - Disable `first` and `prev` buttons when `currentPage <= 1`.
    - Disable `next` and `last` buttons when `currentPage >= maxPage`.

- **Number-Window Navigation Rules:**
  - Always include page `1` and `maxPage` as anchors (when `maxPage > 1`).
  - Show a centered window around `currentPage`: `[currentPage-3, currentPage+3]`.
  - The page-number UI must render numbers on **both sides** of the current page whenever available. Minimum expectation in middle pages: at least 3 numbers before and 3 numbers after the current page.
  - Clamp the dynamic window strictly into `[2, maxPage-1]`.
  - Render a left ellipsis (`...`) if the window start is `> 2`.
  - Render a right ellipsis (`...`) if the window end is `< maxPage - 1`.
  - Example patterns:
    - Head area: `1(current) 2 3 4 ... 79`
    - Middle area: `1 ... 3 4 5 6(current) 7 8 9 ... 79`
    - Tail area: `1 ... 76 77 78 79(current)`

- **Bottom-Only Status Row Rules:**
  - Support page-size switching with explicitly defined options (e.g., `10`, `20`, `50`), visually indicating the currently selected size.
  - Display the `currentPage / maxPage` ratio.
  - Display the total count of items.

Reactive behavior:

- Re-fetch data when `currentPage` changes.
- Re-fetch data when `pageSize` changes.
- When `pageSize` changes, reset `currentPage` to `1` before fetching.

## 4) Execution checklist

1. Add pagination request/response DTOs (or extend existing DTOs).
2. Update backend query flow to compute `total` + `offset/limit` and inject `index`.
3. Run GET checks for `total`, page size upper bound, and non-last-page exact-size behavior.
4. Update frontend API declaration to pass pagination params.
5. Add page state + pagination UI + handlers in the target view/component.
6. Verify boundary scenarios:
   - first page / last page
   - total = 0
   - oversized `pageNum`
   - page number window rendering (`...`) correctness
   - first/last button behavior

## 5) Java test requirements (mandatory)

Add/extend tests to cover all pagination behavior:

1. Basic pagination:
   - default request returns `total` and list length `<= pageSize`.
2. Exact page size for non-last pages:
   - when `pageNum` is not last page, list length must equal `pageSize`.
3. Last page behavior:
   - list length can be `< pageSize`.
4. Oversized page:
   - empty list with correct `total`.
5. Empty dataset:
   - `total = 0`, empty list.
6. `index` correctness:
   - verify first item index on page 1 and page N.
   - verify monotonic increment within one page.
7. Navigation-related contract:
   - ensure max page math (`ceil(total/pageSize)`) is consistent with returned data.

## 6) Output quality requirements

- Keep pagination contract consistent across endpoints.
- Avoid breaking existing business fields.
- Keep response backward-compatible when possible.
- Document any new request params and response fields in project docs.

