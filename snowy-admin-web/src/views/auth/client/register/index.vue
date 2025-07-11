<script setup>
	import { ref, computed } from 'vue'
	import { cloneDeep, isEmpty } from 'lodash-es'
	import { useRouter, useRoute } from 'vue-router'
	import { message } from 'ant-design-vue'
	import { required, rules } from '@/utils/formRules'
	import { globalStore } from '@/store'
	import smCrypto from '@/utils/smCrypto'
	import tool from '@/utils/tool'
	import clientLoginApi from '@/api/auth/client/clientLoginApi'
	import configApi from '@/api/dev/configApi'
	import configData from '@/config'

	const route = useRoute()
	const router = useRouter()
	const isRegister = ref(false)
	const registerFormRef = ref()
	const registerButtonDisable = ref(false)
	const store = globalStore()
	const setSysBaseConfig = store.setSysBaseConfig
	const sysBaseConfig = computed(() => {
		return store.sysBaseConfig
	})
	const registerFormData = ref({
		account: '',
		password: '',
		newPassword: '',
		validCode: '',
		validCodeReqNo: ''
	})
	// 表单校验规则
	const formRules = ref({
		account: [required('请输入账号')],
		password: [
			required('请输入密码'),
			{
				validator: (rule, value) => {
					if (value && registerFormData.value.newPassword && value !== registerFormData.value.newPassword) {
						return Promise.reject('两次输入的密码不一致')
					}
					return Promise.resolve()
				},
				trigger: ['change', 'blur']
			}
		],
		newPassword: [
			required('请再次输入密码'),
			{
				validator: (rule, value) => {
					if (value && registerFormData.value.password && value !== registerFormData.value.password) {
						return Promise.reject('两次输入的密码不一致')
					}
					return Promise.resolve()
				},
				trigger: ['change', 'blur']
			}
		]
	})
	const captchaOpen = ref(configData.SYS_BASE_CONFIG.SNOWY_SYS_DEFAULT_CAPTCHA_OPEN_FLAG_FOR_C)
	const registerOpen = ref('false')
	const validCodeBase64 = ref('')

	onMounted(() => {
		// 获得租户code编码
		if (!isEmpty(route.query.tenCode)) {
			registerFormData.value.tenCode = route.query.tenCode
		}
		registerButtonDisable.value = true
		tool.data.set('SNOWY_TEN_CODE', '')
		// 再去查配置
		getSysConfig()
	})
	const getSysConfig = () => {
		let formData = ref(configData.SYS_BASE_CONFIG)
		configApi
			.configSysBaseList()
			.then((data) => {
				registerButtonDisable.value = false
				if (data) {
					data.forEach((item) => {
						formData.value[item.configKey] = item.configValue
					})
					captchaOpen.value = formData.value.SNOWY_SYS_DEFAULT_CAPTCHA_OPEN_FLAG_FOR_C
					registerOpen.value = formData.value.SNOWY_SYS_DEFAULT_ALLOW_REGISTER_FLAG_FOR_C
					tool.data.set('SNOWY_SYS_BASE_CONFIG', formData.value)
					setSysBaseConfig(formData.value)
					refreshSwitch()
				}
			})
			.catch(() => {})
	}
	// 通过开关加载内容
	const refreshSwitch = () => {
		// 判断是否开启验证码
		if (captchaOpen.value === 'true') {
			// 加载验证码
			registerCaptcha()
			// 加入校验
			formRules.value.validCode = [required('请输入验证码', 'blur'), rules.lettersNum]
		}
	}

	// 点击注册按钮
	const submitRegister = () => {
		formRules.value.validCode = [required('请输入验证码'), rules.lettersNum]
		registerFormRef.value
			.validate()
			.then(() => {
				registerButtonDisable.value = false
				isRegister.value = true
				const loginData = {
					account: registerFormData.value.account,
					// 密码进行SM2加密，传输过程中看到的只有密文，后端存储使用hash
					password: smCrypto.doSm2Encrypt(cloneDeep(registerFormData.value.password)),
					validCode: registerFormData.value.validCode,
					validCodeReqNo: registerFormData.value.validCodeReqNo
				}
				clientLoginApi
					.clientRegister(loginData)
					.then(() => {
						router.replace({
							path: '/front/client/login'
						})
						message.success('注册成功')
					})
					.catch(() => {
						// 异常的时候一般可能就是验证码过期，所以我们将其刷新
						if (captchaOpen.value === 'true') {
							registerCaptcha()
						}
					})
					.finally(() => {
						isRegister.value = false
					})
			})
			.catch(() => {})
	}
	// 获取图形验证码
	const registerCaptcha = () => {
		clientLoginApi.clientGetPicCaptcha().then((data) => {
			validCodeBase64.value = data.validCodeBase64
			registerFormData.value.validCodeReqNo = data.validCodeReqNo
			// 如果有输入的将其清空
			registerFormData.value.validCode = undefined
		})
	}
</script>

<template>
	<div class="login-wrapper">
		<div class="login_main">
			<div class="login-form">
				<a-card>
					<div class="login-header" style="margin-bottom: 20px">
						<h2>用户注册</h2>
					</div>
					<a-form
						ref="registerFormRef"
						:model="registerFormData"
						:rules="formRules"
						class="user-box"
						autocomplete="off"
					>
						<a-form-item name="account">
							<a-input v-model:value="registerFormData.account" placeholder="请输入账号" size="large">
								<template #prefix>
									<user-outlined class="login-icon-gray" />
								</template>
							</a-input>
						</a-form-item>
						<a-form-item name="password">
							<a-input-password v-model:value="registerFormData.password" placeholder="请输入密码" size="large">
								<template #prefix>
									<lock-outlined class="login-icon-gray" />
								</template>
							</a-input-password>
						</a-form-item>
						<a-form-item name="newPassword">
							<a-input-password v-model:value="registerFormData.newPassword" placeholder="请再次输入密码" size="large">
								<template #prefix>
									<lock-outlined class="login-icon-gray" />
								</template>
							</a-input-password>
						</a-form-item>
						<a-form-item name="validCode" v-if="captchaOpen === 'true'">
							<a-row :gutter="8">
								<a-col :span="17">
									<a-input
										v-model:value="registerFormData.validCode"
										placeholder="请输入验证码"
										size="large"
										@keyup.enter="submitRegister"
									>
										<template #prefix>
											<verified-outlined class="login-icon-gray" />
										</template>
									</a-input>
								</a-col>
								<a-col :span="7">
									<img :src="validCodeBase64" class="login-validCode-img" @click="registerCaptcha" />
								</a-col>
							</a-row>
						</a-form-item>
						<a-form-item>
							<a-button
								type="primary"
								class="w-full"
								:loading="isRegister"
								round
								size="large"
								@click="submitRegister"
								:disabled="registerButtonDisable"
							>
								注册
							</a-button>
						</a-form-item>
						<div style="display: flex; justify-content: flex-end">
							<a href="/front/client/login">已有账号？前往登录</a>
						</div>
					</a-form>
				</a-card>
			</div>
		</div>
	</div>
</template>

<style scoped lang="less">
	@import '../login/login';
</style>
