// 暗盘榜股票数据类型
export interface GreyRankStockItem {
  index?: number;
  stock_code: string;
  stock_name: string;
  market: string;
  main_grey_capital: number;
  main_listed_capital: number;
  change_pct: number;
  zhangdiefu?: number;
}

// 暗盘榜返回数据类型
export interface GreyRankResponse {
  data: {
    total: number;
    stock_list: GreyRankStockItem[];
  }
}
