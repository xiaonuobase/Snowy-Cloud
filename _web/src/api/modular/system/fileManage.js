import { axios } from '@/utils/request'

/**
 * 分页查询文件信息表
 *
 * @author yubaoshan
 * @date 2020/6/30 00:20
 */
export function sysFileInfoPage (parameter) {
  return axios({
    url: '/main/sysFileInfo/page',
    method: 'get',
    params: parameter
  })
}

/**
 * 获取全部文件信息表
 *
 * @author yubaoshan
 * @date 2020/6/30 00:20
 */
export function sysFileInfoList (parameter) {
  return axios({
    url: '/main/sysFileInfo/list',
    method: 'get',
    params: parameter
  })
}

/**
 * 上传文件
 *
 * @author yubaoshan
 * @date 2020/6/30 00:20
 */
export function sysFileInfoUpload (parameter) {
  return axios({
    url: '/main/sysFileInfo/upload',
    method: 'post',
    data: parameter
  })
}

/**
 * 下载文件
 *
 * @author yubaoshan
 * @date 2020/6/30 00:20
 */
export function sysFileInfoDownload (parameter) {
  return axios({
    url: '/main/sysFileInfo/download',
    method: 'get',
    params: parameter,
    responseType: 'blob'
  })
}

/**
 * 查看图片
 *
 * @author yubaoshan
 * @date 2020/6/30 00:20
 */
export function sysFileInfoPreview (parameter) {
  return axios({
    url: '/main/sysFileInfo/preview',
    method: 'get',
    params: parameter,
    responseType: 'arraybuffer'
  })
}

/**
 * 查看详情文件信息表
 *
 * @author yubaoshan
 * @date 2020/6/30 00:20
 */
export function sysFileInfoDetail (parameter) {
  return axios({
    url: '/main/sysFileInfo/detail',
    method: 'get',
    params: parameter
  })
}

/**
 * 删除文件信息表
 *
 * @author yubaoshan
 * @date 2020/6/30 00:20
 */
export function sysFileInfoDelete (parameter) {
  return axios({
    url: '/main/sysFileInfo/delete',
    method: 'post',
    data: parameter
  })
}

/**
 * 获取在线文档配置
 *
 * @author yubaoshan
 * @date 2020/6/30 00:20
 */
export function sysFileInfoGetOnlineConfig (parameter) {
  return axios({
    url: '/main/sysFileInfo/getOnlineFileConfig',
    method: 'get',
    params: parameter
  })
}
