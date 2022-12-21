<template>
	<a-spin :spinning="loadSpinning">
		<a-form
			ref="formRef"
			:model="formData"
			:rules="formRules"
			layout="vertical"
			:label-col="{ ...layout.labelCol, offset: 0 }"
			:wrapper-col="{ ...layout.wrapperCol, offset: 0 }"
		>
			<a-form-item label="微信支付应用ID：" name="SNOWY_PAY_WX_APP_ID">
				<a-input v-model:value="formData.SNOWY_PAY_WX_APP_ID" placeholder="请输入微信支付应用ID" />
			</a-form-item>
			<a-form-item label="微信支付私钥：" name="SNOWY_PAY_WX_PRIVATE_KEY">
				<a-textarea
					v-model:value="formData.SNOWY_PAY_WX_PRIVATE_KEY"
					placeholder="请输入微信支付私钥"
					:auto-size="{ minRows: 3, maxRows: 5 }"
				/>
			</a-form-item>
			<a-form-item label="支付服务URL：" name="SNOWY_PAY_WX_SERVER_URL">
				<a-input v-model:value="formData.SNOWY_PAY_WX_SERVER_URL" placeholder="请输入支付服务URL" />
			</a-form-item>

			<a-form-item>
				<a-button type="primary" :loading="submitLoading" @click="onSubmit()">保存</a-button>
				<a-button style="margin-left: 10px" @click="() => formRef.resetFields()">重置</a-button>
			</a-form-item>
		</a-form>
	</a-spin>
</template>

<script setup name="wxPayForm">
	import { cloneDeep } from 'lodash-es'
	import { required } from '@/utils/formRules'
	import { message } from 'ant-design-vue'
	import configApi from '@/api/dev/configApi'

	const formRef = ref()
	const formData = ref({})
	const submitLoading = ref(false)
	const loadSpinning = ref(true)

	// 查询此界面的配置项,并转为表单
	const param = {
		category: 'PAY_WX'
	}
	configApi.configList(param).then((data) => {
		loadSpinning.value = false
		if (data) {
			data.forEach((item) => {
				formData.value[item.configKey] = item.configValue
			})
		} else {
			message.warning('表单项不存在，请初始化数据库')
		}
	})

	// 默认要校验的
	const formRules = {
		SNOWY_PAY_WX_APP_ID: [required('请输入微信支付应用ID')],
		SNOWY_PAY_WX_PRIVATE_KEY: [required('请输入微信支付应用ID')],
		SNOWY_PAY_WX_SERVER_URL: [required('请输入支付服务URL')]
	}
	// 验证并提交数据
	const onSubmit = () => {
		formRef.value
			.validate()
			.then(() => {
				submitLoading.value = true
				let submitParam = cloneDeep(formData.value)
				const param = Object.entries(submitParam).map((item) => {
					return {
						configKey: item[0],
						configValue: item[1]
					}
				})
				configApi
					.configEditForm(param)
					.then(() => {})
					.finally(() => {
						submitLoading.value = false
					})
			})
	}
	const layout = {
		labelCol: {
			span: 4
		},
		wrapperCol: {
			span: 12
		}
	}
</script>
