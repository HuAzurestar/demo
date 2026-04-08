<template>
  <div class="quotes-container">
    <div class="quotes-board-wrap">
      <div class="quotes-card-wrap">
        <board-card title="榜单" :loading="loading" :is-empty="isEmpty">
          <stock-list :data="stocks" @item-click="onStockClick" />
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
const isEmpty = computed(() => !loading.value && stocks.value.length === 0);

const fetchBoard = async () => {
  try {
    const { data } = await marketApi.getDarkPlateBoard('');
    stocks.value = data?.stock_list || [];
  } catch (error) {
    console.error('FeaturedQuotes|获取暗盘榜数据失败', error);
  } finally {
    loading.value = false;
    proxy.$pageStatus.close();
  }
};

const onStockClick = (stock: GreyRankStockItem) => {
  jumpToFenShi(stock.stock_code, stock.market);
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
</style>
