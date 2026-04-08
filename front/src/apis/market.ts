import request from './http';
import type { GreyRankResponse } from '../types/market';

export const marketApi = {
  // 获取暗盘榜数据
  async getDarkPlateBoard(_date?: string): Promise<GreyRankResponse> {
    return request.get('/indicator/capital/v1/grey_rank', {
      _t: +new Date(),
    });
  },
};
