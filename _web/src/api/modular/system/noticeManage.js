import { axios } from '@/utils/request'

/**
 * 查询系统通知公告
 *
 * @author yubaoshan
 * @date 2020/6/30 01:56
 */
export function sysNoticePage (parameter) {
  return axios({
    url: '/system/sysNotice/page',
    method: 'get',
    params: parameter
  })
}

/**
 * 添加系统通知公告
 *
 * @author yubaoshan
 * @date 2020/6/30 01:56
 */
export function sysNoticeAdd (parameter) {
  return axios({
    url: '/system/sysNotice/add',
    method: 'post',
    data: parameter
  })
}

/**
 * 编辑系统通知公告
 *
 * @author yubaoshan
 * @date 2020/6/30 01:56
 */
export function sysNoticeEdit (parameter) {
  return axios({
    url: '/system/sysNotice/edit',
    method: 'post',
    data: parameter
  })
}

/**
 * 删除系统通知公告
 *
 * @author yubaoshan
 * @date 2020/6/30 01:56
 */
export function sysNoticeDelete (parameter) {
  return axios({
    url: '/system/sysNotice/delete',
    method: 'post',
    data: parameter
  })
}

/**
 * 通知公告详情
 *
 * @author yubaoshan
 * @date 2020/6/30 01:56
 */
export function sysNoticeDetail (parameter) {
  return axios({
    url: '/system/sysNotice/detail',
    method: 'get',
    params: parameter
  })
}

/**
 * 修改状态
 *
 * @author yubaoshan
 * @date 2020/7/30 02:23
 */
export function sysNoticeChangeStatus (parameter) {
  return axios({
    url: '/system/sysNotice/changeStatus',
    method: 'post',
    data: parameter
  })
}
