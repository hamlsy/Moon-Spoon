import Vue from 'vue'
import App from './App.vue'
import router from './router'
import './assets/styles/global_nav.css';
import './assets/styles/global_workbook.css';
import './assets/styles/global_btn.css';
import './assets/styles/global_popup.css';
Vue.config.productionTip = false

new Vue({
  render: h => h(App),
  router,
}).$mount('#app')
