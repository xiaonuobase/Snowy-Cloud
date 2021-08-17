import { axios } from '@/utils/request'

/**
 * 发送记录查询
 *
 * @author yubaoshan
 * @date 2020/7/3 22:11
 */
export function smsPage (parameter) {
  return axios({
    url: '/main/sms/page',
    method: 'get',
    params: parameter
  })
}

/**
 * 验证短信验证码
 *
 * @author yubaoshan
 * @date 2020/7/3 22:12
 */
export function sysSendLoginMessage (parameter) {
  return axios({
    url: '/main/sms/sendLoginMessage',
    method: 'post',
    data: parameter
  })
}

/**
 * 验证短信验证码
 *
 * @author yubaoshan
 * @date 2020/7/3 22:12
 */
export function sysValidateMessage (parameter) {
  return axios({
    url: '/main/sms/validateMessage',
    method: 'post',
    data: parameter
  })
}
