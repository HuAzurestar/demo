<template>
  <div class="stock-list">
    <rank-item
      v-for="(stock, index) in data"
      :key="stock.stock_code"
      :rank="formatRank(stock, index)"
      :title="stock.stock_name"
      @click="$emit('item-click', stock)"
    >
      <template #extra>
        <div class="stock-list__cell">
          <span class="stock-list__label">A盘资金(万)</span>
          <span class="stock-list__value ff-din" v-redGreen="stock.main_grey_capital">
            {{ formatWan(stock.main_grey_capital) }}
          </span>
        </div>
        <div class="stock-list__cell">
          <span class="stock-list__label">B盘资金(万)</span>
          <span class="stock-list__value ff-din" v-redGreen="stock.main_listed_capital">
            {{ formatWan(stock.main_listed_capital) }}
          </span>
        </div>
      </template>
    </rank-item>
  </div>
</template>

<script setup lang="ts">
import { PropType } from 'vue';
import RankItem from '@c/common/RankItem.vue';
import { INTEGER_10000, INTEGER_2 } from '@/utils/constant';
import type { GreyRankStockItem } from '@/types/market';

defineProps({
  data: {
    type: Array as PropType<GreyRankStockItem[]>,
    required: true
  }
});

defineEmits<{ (e: 'item-click', stock: GreyRankStockItem): void }>();

const formatWan = (value: number | null) => {
  if (value === null) return '--';
  const sign = value > 0 ? '+' : '';
  return `${sign}${(value / INTEGER_10000).toFixed(INTEGER_2)}`;
};

const formatRank = (stock: GreyRankStockItem, fallbackIndex: number) => {
  if (typeof stock.index === 'number' && stock.index > 0) {
    return stock.index - 1;
  }
  return fallbackIndex;
};
</script>

<style scoped lang="less">
.stock-list {
  &__cell {
    display: flex;
    flex-direction: column;
    align-items: flex-end;
  }

  &__label {
    font-size: 11px;
    line-height: 14px;
    color: rgba(0, 0, 0, 0.6);
    margin-bottom: 2px;
  }

  &__value {
    font-size: 14px;
    line-height: 16px;
    font-weight: 500;
  }
}
</style>
