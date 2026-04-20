<template>
  <div class="quotes-container">
    <div class="quotes-board-wrap">
      <div class="quotes-card-wrap">
        <board-card title="榜单" :loading="loading" :is-empty="isEmpty">
          <div class="pagination-nav">
            <button class="pagination-nav__btn" :disabled="isFirstPage" @click="goFirst">首页</button>
            <button class="pagination-nav__btn" :disabled="isFirstPage" @click="goPrev">上一页</button>
            <button
              v-for="item in pageItems"
              :key="`top-${item.key}`"
              class="pagination-nav__page"
              :class="{ 'is-active': item.type === 'page' && item.value === currentPage, 'is-gap': item.type === 'ellipsis' }"
              :disabled="item.type !== 'page' || item.value === currentPage"
              @click="onPageClick(item)"
            >
              {{ item.label }}
            </button>
            <button class="pagination-nav__btn" :disabled="isLastPage" @click="goNext">下一页</button>
            <button class="pagination-nav__btn" :disabled="isLastPage" @click="goLast">末页</button>
          </div>
          <stock-list :data="stocks" @item-click="onStockClick" />
          <div class="pagination-wrap">
            <div class="pagination-summary">
              <span>当前页 / 最大页：{{ currentPage }} / {{ totalPages }}</span>
              <span>总数：{{ total }}</span>
            </div>
            <div class="pagination-size">
              <span class="pagination-size__label">每页</span>
              <button
                v-for="size in pageSizeOptions"
                :key="size"
                class="pagination-size__btn"
                :class="{ 'is-active': pageSize === size }"
                @click="onPageSizeChange(size)"
              >
                {{ size }}
              </button>
            </div>
            <div class="pagination-nav">
              <button class="pagination-nav__btn" :disabled="isFirstPage" @click="goFirst">首页</button>
              <button class="pagination-nav__btn" :disabled="isFirstPage" @click="goPrev">上一页</button>
              <button
                v-for="item in pageItems"
                :key="`bottom-${item.key}`"
                class="pagination-nav__page"
                :class="{ 'is-active': item.type === 'page' && item.value === currentPage, 'is-gap': item.type === 'ellipsis' }"
                :disabled="item.type !== 'page' || item.value === currentPage"
                @click="onPageClick(item)"
              >
                {{ item.label }}
              </button>
              <button class="pagination-nav__btn" :disabled="isLastPage" @click="goNext">下一页</button>
              <button class="pagination-nav__btn" :disabled="isLastPage" @click="goLast">末页</button>
            </div>
          </div>
        </board-card>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, computed, onMounted, getCurrentInstance } from 'vue';
import BoardCard from '@c/common/BoardCard.vue';
import StockList from '@c/featured-quotes/dark-board/StockList.vue';
import { marketApi } from '@/apis/market';
import { jumpToFenShi } from '@/utils/jump';
import type { GreyRankStockItem } from '@/types/market';

const { proxy } = getCurrentInstance() as any;

const stocks = ref<GreyRankStockItem[]>([]);
const loading = ref(true);
const currentPage = ref(1);
const pageSize = ref(20);
const total = ref(0);
const pageSizeOptions = [10, 20, 50];
const isEmpty = computed(() => !loading.value && stocks.value.length === 0);
const totalPages = computed(() => Math.max(1, Math.ceil(total.value / pageSize.value)));
const isFirstPage = computed(() => currentPage.value <= 1);
const isLastPage = computed(() => currentPage.value >= totalPages.value);

type PageItem = { key: string; type: 'page' | 'ellipsis'; value?: number; label: string };
const pageItems = computed<PageItem[]>(() => {
  const maxPage = totalPages.value;
  if (maxPage <= 1) return [{ key: 'p1', type: 'page', value: 1, label: '1' }];

  const current = currentPage.value;
  const start = Math.max(2, current - 3);
  const end = Math.min(maxPage - 1, current + 3);
  const items: PageItem[] = [{ key: 'p1', type: 'page', value: 1, label: '1' }];

  if (start > 2) items.push({ key: 'left-gap', type: 'ellipsis', label: '...' });
  for (let p = start; p <= end; p++) items.push({ key: `p${p}`, type: 'page', value: p, label: String(p) });
  if (end < maxPage - 1) items.push({ key: 'right-gap', type: 'ellipsis', label: '...' });

  items.push({ key: `p${maxPage}`, type: 'page', value: maxPage, label: String(maxPage) });
  return items;
});

const fetchBoard = async () => {
  loading.value = true;
  try {
    const { data } = await marketApi.getDarkPlateBoard('', currentPage.value, pageSize.value);
    total.value = Number(data?.total || 0);
    const maxPage = Math.max(1, Math.ceil(total.value / pageSize.value));
    if (currentPage.value > maxPage) {
      currentPage.value = maxPage;
      const retry = await marketApi.getDarkPlateBoard('', currentPage.value, pageSize.value);
      total.value = Number(retry?.data?.total || 0);
      stocks.value = retry?.data?.stock_list || [];
      return;
    }
    stocks.value = data?.stock_list || [];
  } catch (error) {
    console.error('FeaturedQuotes|获取暗盘榜数据失败', error);
    stocks.value = [];
    total.value = 0;
  } finally {
    loading.value = false;
    proxy.$pageStatus.close();
  }
};

const onStockClick = (stock: GreyRankStockItem) => {
  jumpToFenShi(stock.stock_code, stock.market);
};

const onPageSizeChange = (size: number) => {
  if (pageSize.value === size) return;
  pageSize.value = size;
  currentPage.value = 1;
  fetchBoard();
};

const onPageClick = (item: PageItem) => {
  if (item.type !== 'page' || item.value == null || item.value === currentPage.value) return;
  currentPage.value = item.value;
  fetchBoard();
};

const goFirst = () => {
  if (isFirstPage.value) return;
  currentPage.value = 1;
  fetchBoard();
};

const goLast = () => {
  if (isLastPage.value) return;
  currentPage.value = totalPages.value;
  fetchBoard();
};

const goPrev = () => {
  if (isFirstPage.value) return;
  currentPage.value -= 1;
  fetchBoard();
};

const goNext = () => {
  if (isLastPage.value) return;
  currentPage.value += 1;
  fetchBoard();
};

proxy.$pageStatus.loading({ type: 'spinner' });

onMounted(fetchBoard);
</script>

<style scoped lang="less">
.quotes-container {
  padding-bottom: calc(49px + constant(safe-area-inset-bottom));
  padding-bottom: calc(49px + env(safe-area-inset-bottom));
  .quotes-board-wrap {
    min-height: 100vh;

    .quotes-card-wrap {
      padding-top: 0;
      padding-bottom: 0;
      border-radius: 6px;
      margin: 0 6px 8px 6px !important;
    }
  }
}

.pagination-wrap {
  margin-top: 12px;
  padding: 10px 6px 0;
  border-top: 1px solid rgba(0, 0, 0, 0.06);
}

.pagination-summary {
  display: flex;
  justify-content: space-between;
  margin-bottom: 8px;
  font-size: 12px;
  color: rgba(0, 0, 0, 0.6);
}

.pagination-size {
  display: flex;
  align-items: center;
  margin-bottom: 8px;

  &__label {
    font-size: 12px;
    color: rgba(0, 0, 0, 0.6);
    margin-right: 8px;
  }

  &__btn {
    border: 1px solid rgba(0, 0, 0, 0.15);
    border-radius: 4px;
    background: #fff;
    font-size: 12px;
    color: #333;
    padding: 2px 8px;
    margin-right: 6px;
  }

  .is-active {
    color: #1989fa;
    border-color: #1989fa;
  }
}

.pagination-nav {
  display: flex;
  align-items: center;
  flex-wrap: wrap;
  gap: 6px;
  margin-bottom: 8px;
  padding-bottom: 8px;
  border-bottom: 1px solid rgba(0, 0, 0, 0.06);
}

.pagination-nav__btn,
.pagination-nav__page {
  border: 1px solid rgba(0, 0, 0, 0.15);
  border-radius: 4px;
  background: #fff;
  font-size: 12px;
  color: #333;
  padding: 2px 8px;
}

.pagination-nav__btn[disabled],
.pagination-nav__page[disabled] {
  opacity: 0.45;
}

.pagination-nav__page.is-active {
  color: #1989fa;
  border-color: #1989fa;
}

.pagination-nav__page.is-gap {
  border-style: dashed;
}
</style>
