<template>
  <div style="width: 100%">
    <!-- 表单标题 -->
    <div class="title">用车申请单</div>
    <!-- 表单容器 -->
    <div class="form-wrapper">
      <a-divider orientation="left">垂直样式</a-divider>
      <a-form :form="form">
        <!-- 第一行 -->
        <a-row :gutter="24">
          <a-col :md="12" :sm="24">
            <a-form-item label="输入框" has-feedback>
              <a-input
                placeholder="请输入"
                v-decorator="['inputVal', {rules: [{required: true, message: '请输入！'}]}]"
              />
            </a-form-item>
          </a-col>
          <a-col :md="12" :sm="24">
            <a-form-item label="可清除" has-feedback>
              <a-input
                allow-clear
                placeholder="请输入"
                v-decorator="['clearVal', {rules: [{required: true, message: '请输入！'}]}]"
              />
            </a-form-item>
          </a-col>
        </a-row>
        <!-- 第二行 -->
        <a-row :gutter="24">
          <a-col :md="24" :sm="24">
            <a-form-item label="文本域" has-feedback>
              <a-textarea
                placeholder="请输入"
                :rows="4"
                v-decorator="['textareaVal', {rules: [{required: true, message: '请输入！'}]}]"
              />
            </a-form-item>
          </a-col>
        </a-row>
      </a-form>

      <a-divider orientation="left">行内样式</a-divider>
      <a-form :form="form">
        <a-row :gutter="24">
          <a-col :md="12" :sm="24">
            <a-form-item label="下拉框" :labelCol="labelCol" :wrapperCol="wrapperCol" has-feedback>
              <a-select
                placeholder="请选择"
                v-decorator="['selectVal', {rules: [{required: true, message: '请选择！'}]}]"
              >
                <a-select-option value="1">选项一</a-select-option>
                <a-select-option value="2">选项二</a-select-option>
                <a-select-option value="3" disabled>选项三</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
          <a-col :md="12" :sm="24">
            <a-form-item label="多选下拉框" :labelCol="labelCol" :wrapperCol="wrapperCol" has-feedback>
              <a-select
                mode="multiple"
                placeholder="请选择"
                v-decorator="['selectMultVal', {rules: [{required: true, message: '请选择！'}]}]"
              >
                <a-select-option value="1">选项一</a-select-option>
                <a-select-option value="2">选项二</a-select-option>
                <a-select-option value="3" disabled>选项三</a-select-option>
                <a-select-option value="4">选项四</a-select-option>
              </a-select>
            </a-form-item>
          </a-col>
        </a-row>

        <a-row :gutter="24">
          <a-col :md="12" :sm="24">
            <a-form-item label="单选按钮（有初始值）" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-radio-group
                name="radioGroup"
                placeholder="请选择"
                v-decorator="['radioVal', {initialValue: '2',rules: [{required: true, message: '请选择！'}]}]"
              >
                <a-radio value="1">A</a-radio>
                <a-radio value="2">B</a-radio>
                <a-radio value="3">C</a-radio>
                <a-radio value="4">D</a-radio>
              </a-radio-group>
            </a-form-item>
          </a-col>
        </a-row>

        <a-row :gutter="24">
          <a-col :md="12" :sm="24">
            <a-form-item label="复选按钮" :labelCol="labelCol" :wrapperCol="wrapperCol">
              <a-checkbox-group
                :options="checkboxOptions"
                placeholder="请选择"
                v-decorator="['checkboxVal', {rules: [{required: true, message: '请选择！'}]}]"
              />
            </a-form-item>
          </a-col>
          <a-col :md="12" :sm="24">
            <a-form-item label="时间选择" :labelCol="labelCol" :wrapperCol="wrapperCol" has-feedback>
              <a-date-picker
                placeholder="请选择"
                v-decorator="['dateVal', {rules: [{required: true, message: '请选择！'}]}]"
              />
            </a-form-item>
          </a-col>
        </a-row>
      </a-form>
    </div>
  </div>
</template>

<script>
import moment from 'moment'
import 'moment/locale/zh-cn'

const checkboxOptions = [
  { label: 'Apple', value: '1' },
  { label: 'Pear', value: '2' },
  { label: 'Orange', value: '3', disabled: true }
]
export default {
  data() {
    return {
      moment,
      checkboxOptions,
      labelCol: {
        xs: { span: 24 },
        sm: { span: 5 } // 灵活调整宽度，保持表单 label 宽度统一， 注意与 wrapperCol 合计24即可
      },
      wrapperCol: {
        xs: { span: 24 },
        sm: { span: 19 }
      },
      form: this.$form.createForm(this, { name: 'carApplyForm' })
    }
  },
  methods: {
    /**
     * 初始化方法
     */
    init(data) {
      if (!data) {
        return
      }
      // console.log('data', data)
      const formData = JSON.parse(data)

      this.form.setFieldsValue(formData)
      // 时间类型处理
      if (formData.dateVal) {
        this.form.setFieldsValue({ dateVal: moment(formData.dateVal) })
      }
    },
    /**
     * 重置方法
     */
    reset() {
      this.form.resetFields()
    },
    /**
     * 存为草稿时获取表单数据
     */
    getDataForDraft() {
      return new Promise((resolve, reject) => {
        const {
          form: { getFieldsValue, getFieldValue }
        } = this
        try {
          const formData = getFieldsValue()
          // 时间类型处理
          formData.dateVal = moment(getFieldValue('dateVal')).format('YYYY-MM-DD')
          resolve(formData)
        } catch (error) {
          reject(error)
        }
      })
    },
    /**
     * 发布时获取表单数据
     */
    getDataForSubmit() {
      return new Promise((resolve, reject) => {
        try {
          this.form.validateFields((err, values) => {
            // 时间类型处理
            values.dateVal = moment(values.dateVal).format('YYYY-MM-DD')

            err ? reject(err) : resolve(values)
          })
        } catch (error) {
          reject(error)
        }
      })
    }
  }
}
</script>

<style lang="less" scoped>
.title {
  text-align: center;
  font-size: 32px;
  font-weight: bolder;
  margin-bottom: 0px;
  padding: 8px 0;
  border: 1px solid #e8e8e8;
  border-bottom: none;
}

.form-wrapper {
  padding: 0 8px;
  border: 1px solid #e8e8e8;
}
</style>
