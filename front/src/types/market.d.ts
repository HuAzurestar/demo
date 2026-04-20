// 暗盘榜股票数据类型
export interface GreyRankStockItem {
  stock_code: string;
  stock_name: string;
  market: string;
  main_grey_capital: number;
  main_listed_capital: number;
  turnover?: number;
  /** main_listed_capital / turnover * 100，两位小数；非法时前端展示 -- */
  ratio_pct?: number | null;
  change_pct: number;
  zhangdiefu?: number;
}

// 暗盘榜返回数据类型
export interface GreyRankResponse {
  data: {
    stock_list: GreyRankStockItem[];
  }
}
