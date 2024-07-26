const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true,
  // build 경로 설정
  outputDir: "dist",
  devServer: {
    // 프록시 설정
    proxy: {
      '/api': {
        target: 'http://localhost:8080',
        changeOrigin: true,
        pathRewrite: {
          '^/api': '' // Spring 백엔드에서 /api를 사용하지 않는 경우 이 줄을 추가
        }
      }
    },
    configureWebpack: {
      externals: {
        config: JSON.stringify({
          apiUrl: process.env.VUE_APP_API_URL || 'http://localhost:8080/api'
        })
      }
    }
  }
})