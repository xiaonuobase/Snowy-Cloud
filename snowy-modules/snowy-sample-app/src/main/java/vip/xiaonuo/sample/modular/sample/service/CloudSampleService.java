package vip.xiaonuo.sample.modular.sample.service;

import com.baomidou.mybatisplus.extension.service.IService;
import vip.xiaonuo.sample.modular.sample.entity.CloudSample;
import vip.xiaonuo.sample.modular.sample.param.CloudSampleParam;

import java.util.List;

/**
 * 微服务范例service接口
 *
 * @author dongxiayu
 * @date 2020/12/28 00:49
 */
public interface CloudSampleService extends IService<CloudSample> {

    /**
     * 根据范例名称获取范例
     *
     * @param sampleName 范例名称
     * @return 用户
     * @author dongxiayu
     * @date 2020/12/28 00:51
     */
    CloudSample getBySampleName(String sampleName);

    /**
     * 查询微服务范例
     *
     * @param cloudSampleParam 查询参数
     * @return 查询列表结果
     * @author dongxiayu
     * @date 2020/12/28 00:51
     */
    List<CloudSample> list(CloudSampleParam cloudSampleParam);

}
