import {fileURLToPath, URL} from 'node:url'

import {defineConfig} from 'vite'
import vue from '@vitejs/plugin-vue'

import AutoImport from 'unplugin-auto-import/vite'
import Components from 'unplugin-vue-components/vite'
import {NaiveUiResolver} from 'unplugin-vue-components/resolvers'

import {createRequire} from 'node:module'

const require = createRequire(import.meta.url)
import ckeditor5 from '@ckeditor/vite-plugin-ckeditor5' // 引入 ckeditor5 vite 插件

// https://vitejs.dev/config/
export default defineConfig({
    plugins: [
        vue(),
        AutoImport({
            imports: [
                'vue',
                {
                    'naive-ui': [
                        'useDialog',
                        'useMessage',
                        'useNotification',
                        'useLoadingBar'
                    ]
                }
            ]
        }),
        Components({
            resolvers: [NaiveUiResolver()]
        }),
        ckeditor5({theme: require.resolve('@ckeditor/ckeditor5-theme-lark')}) // ckeditor5 插件，记得安装好主题哦
    ],
    resolve: {
        alias: {
            '@': fileURLToPath(new URL('./src', import.meta.url))
        }
    },
    // 服务
    server: {
        port: 8090,
        // 代理
        proxy: {
            '/note-serve': {
                target: 'http://localhost:8080/', // 代理后台服务器地址
                changeOrigin: true, //允许跨域
                rewrite: path => path.replace(/^\/note-serve/, '') // 将请求地址中的 /note 替换成空
            }
        }
    }
})