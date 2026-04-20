import request from './http';
import type { GreyRankResponse } from '../types/market';

export const marketApi = {
  // 获取暗盘榜数据
  async getDarkPlateBoard(_date?: string, pageNum = 1, pageSize = 20): Promise<GreyRankResponse> {
    return request.get('/indicator/capital/v1/grey_rank', {
      pageNum,
      pageSize,
      _t: +new Date(),
    });
  },
};
