/**
 *  Copyright [2022] [https://www.xiaonuo.vip]
 *	Snowy采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：
 *	1.请不要删除和修改根目录下的LICENSE文件。
 *	2.请不要删除和修改Snowy源码头部的版权声明。
 *	3.本项目代码可免费商业使用，商业使用请保留源码和相关描述文件的项目出处，作者声明等。
 *	4.分发源码时候，请注明软件出处 https://xiaonuo.vip
 *	5.不可二次分发开源参与同类竞品，如有想法可联系团队xiaonuobase@qq.com商议合作。
 *	6.若您的项目无法满足以上几点，需要更多功能代码，获取Snowy商业授权许可，请在官网购买授权，地址为 https://www.xiaonuo.vip
 */
import * as antdvIcons from '@ant-design/icons-vue'
import config from './config'
import tool from './utils/tool'
import { hasPerm } from './utils/permission/index'
import errorHandler from './utils/errorHandler'
import customIcons from './assets/icons/index.js'
import 'highlight.js/styles/atom-one-dark.css'
import hljsCommon from 'highlight.js/lib/common'
import hljsVuePlugin from '@highlightjs/vue-plugin'
import STable from './components/Table/index.vue'
import Ellipsis from './components/Ellipsis/index.vue'
import DragModal from './components/DragModal/index.vue'

export default {
	install(app) {
		// 挂载全局对象
		app.config.globalProperties.$CONFIG = config
		app.config.globalProperties.$TOOL = tool
		app.config.globalProperties.hasPerm = hasPerm

		// 注册常用组件
		app.component('STable', STable)
		app.component('Ellipsis', Ellipsis)
		app.component('DragModal', DragModal)

		// 统一注册antdv图标
		for (const icon in antdvIcons) {
			app.component(icon, antdvIcons[icon])
		}
		// 统一注册自定义全局图标
		app.use(customIcons)
		// 注册代码高亮组件 （博客：https://blog.csdn.net/weixin_41897680/article/details/124925222）
		// 注意：解决Vue使用highlight.js build打包发布后样式消失问题，原因是webpack在打包的时候没有把未被使用的代码打包进去，因此，在此处引用一下，看似无意义实则有用
        hljsCommon.highlightAuto('<h1>Highlight.js has been registered successfully!</h1>').value
		app.use(hljsVuePlugin)

		// 全局代码错误捕捉
		app.config.errorHandler = errorHandler
	}
}
