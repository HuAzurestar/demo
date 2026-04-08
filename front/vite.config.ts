import { splitVendorChunkPlugin, UserConfigExport, ConfigEnv, loadEnv, Plugin } from 'vite';
import vue from '@vitejs/plugin-vue2';
import legacy from '@vitejs/plugin-legacy';
import Components from 'unplugin-vue-components/vite';
import AutoImport from 'unplugin-auto-import/vite';
import { VantResolver } from 'unplugin-vue-components/resolvers';
import { createHtmlPlugin } from 'vite-plugin-html';
import { resolve } from 'path';
import pxtovw from 'postcss-px-to-viewport';
import autoprefixer from 'autoprefixer';

// px转vw
const loder_pxtovw = pxtovw({
  viewportWidth: 375,
  viewportUnit: 'vw',
  selectorBlackList: [
    'no-vw'
  ]
});

// css自动添加前缀
const loader_autoprefixer = autoprefixer({
  overrideBrowserslist: ['Android 4.1', 'iOS 7.1'],
  grid: true
});

// 创建自定义插件处理 crossorigin
function crossoriginPlugin(): Plugin {
  return {
    name: 'vite:crossorigin',
    transformIndexHtml(html) {
      return html.replace(
        /(<link|<script)([^>]*?)(href|src)="([^"]+)"([^>]*?)>/g,
        (match, tag, beforeAttr, sourceAttr, sourceValue, afterAttr) => {
          const attrs = beforeAttr + afterAttr;
          if (attrs.includes('crossorigin')) {
            return match;
          }
          return `${tag}${beforeAttr} crossorigin="anonymous" ${sourceAttr}="${sourceValue}"${afterAttr}>`;
        }
      );
    }
  };
}

export default ({ mode }: ConfigEnv): UserConfigExport => {
  const env = loadEnv(mode, process.cwd());

  return {
    base: env.VITE_APP_BASE_URL,
    plugins: [
      vue(),
      splitVendorChunkPlugin(),
      crossoriginPlugin(),
      legacy({
        targets: ['defaults', 'Android > 44', 'Safari > 8', 'iOS > 8'],
        additionalLegacyPolyfills: ['regenerator-runtime/runtime']
      }),
      createHtmlPlugin({
        inject: {
          data: {
            ...env
          }
        }
      }),
      AutoImport({
        // 不用手动导入ref,reactive,onmounted
        imports: ['vue', 'vue-router'],
        // 不用手动导入组件的方法
        dts: 'src/types/auto-imports.d.ts'
      }),
      Components({
        // 不用手动导入组件
        resolvers: [VantResolver()],
        dts: 'src/types/components.d.ts'
      })
    ],
    // 全局导入common.less
    css: {
      preprocessorOptions: {
        less: {
          additionalData: '@import "@/assets/style/common.less";'
        }
      },
      postcss: {
        plugins: [loder_pxtovw, loader_autoprefixer]
      }
    },
    server: {
      port: 8080,
      open: true,
      cors: true,
      host: '127.0.0.1',
      allowedHosts: [
        'dev.10jqka.com.cn'
      ],
      // 设置代理，根据我们项目实际情况配置
      proxy: {
        '/indicator': {
          target: 'http://127.0.0.1:80',
          changeOrigin: true,
          secure: false
        }
      }
    },
    build: {
      target: 'es2015',
      chunkSizeWarningLimit: 2000,
      sourcemap: env.VITE_APP_CURRENTMODE !== 'release' || 'hidden',
    },
    // 路径简称
    resolve: {
      alias: {
        '@': resolve(__dirname, 'src'),
        '@c': resolve(__dirname, 'src/components'),
        _t: resolve(__dirname, 'src/utils/tools.ts')
      }
    }
  };
};
