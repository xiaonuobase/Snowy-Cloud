<!--
Copyright [2022] [https://www.xiaonuo.vip]

Snowy采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：

1.请不要删除和修改根目录下的LICENSE文件。
2.请不要删除和修改Snowy源码头部的版权声明。
3.本项目代码可免费商业使用，商业使用请保留源码和相关描述文件的项目出处，作者声明等。
4.分发源码时候，请注明软件出处 https://www.xiaonuo.vip
5.不可二次分发开源参与同类竞品，如有想法可联系团队xiaonuobase@qq.com商议合作。
6.若您的项目无法满足以上几点，需要更多功能代码，获取Snowy商业授权许可，请在官网购买授权，地址为 https://www.xiaonuo.vip
-->
<template>
	<a-config-provider :locale="locale">
		<router-view></router-view>
	</a-config-provider>
</template>

<script setup name="App">
	import i18n from '@/locales'
	import store from '@/store'
	import config from '@/config'
	import configApi from '@/api/dev/configApi'
	import tool from '@/utils/tool'

	store.commit('initTheme')
	const locale = i18n.global.messages[i18n.global.locale].lang
	if (!tool.data.get('SNOWY_SYS_BASE_CONFIG')) {
		let formData = ref(config.SYS_BASE_CONFIG)
		configApi.configSysBaseList().then((data) => {
			if (data) {
				data.forEach((item) => {
					formData.value[item.configKey] = item.configValue
				})
				tool.data.set('SNOWY_SYS_BASE_CONFIG', formData.value)
				store.commit('SET_sysBaseConfig', formData.value)
			}
		})
	}
</script>
