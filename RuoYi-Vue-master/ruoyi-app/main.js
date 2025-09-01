import Vue from 'vue'
import App from './App'
import '@/common/main.wxss'
import store from './store' // store
import plugins from './plugins' // plugins
import './permission' // permission
import { getDicts } from "@/api/system/dict/data"
import { parseTime } from './utils/ruoyi'

Vue.use(plugins)

Vue.config.productionTip = false
Vue.prototype.$store = store
Vue.prototype.getDicts = getDicts
Vue.prototype.parseTime = parseTime

App.mpType = 'app'

const app = new Vue({
  ...App
})

app.$mount()