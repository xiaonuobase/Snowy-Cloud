<template>
	<a-tabs v-model:activeKey="activeKey" tab-position="left">
		<a-tab-pane key="aliPay" tab="支付宝">
			<aliPayForm />
		</a-tab-pane>
		<a-tab-pane key="wxPay" tab="微信">
			<wxPayForm />
		</a-tab-pane>
	</a-tabs>
</template>

<script setup name="payConfig">
	import aliPayForm from './aliPayForm.vue'
	import wxPayForm from './wxPayForm.vue'
	const activeKey = ref('aliPay')
</script>
