import axios from 'axios';
import type { AxiosInstance, AxiosRequestConfig, AxiosResponse } from 'axios';

// 创建 axios 实例
const instance: AxiosInstance = axios.create({
  baseURL: import.meta.env.VITE_API_BASE_URL || '',
  timeout: 15000,
  withCredentials: true,
  headers: {
    'Content-Type': 'application/json'
  }
});

// 请求拦截器
instance.interceptors.request.use(
  config => {
    return config;
  },
  error => {
    return Promise.reject(error);
  }
);

// 响应拦截器
instance.interceptors.response.use(
  response => {
    return response;
  },
  error => {
    return Promise.reject(error);
  }
);

class Request {
  /**
   * GET封装
   * @param {string} url 请求地址
   * @param {object} params 请求配置
   * @returns {Promise}
   */
  get(url: string, params: any, config?: AxiosRequestConfig | undefined): Promise<any> {
    return instance.get(url, { params, ...config }).then((response: AxiosResponse<any>) => response.data);
  }

}
const request: Request = new Request();

export default request;
