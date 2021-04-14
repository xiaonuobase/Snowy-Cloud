package vip.xiaonuo.sample.modular.sample.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import vip.xiaonuo.sample.modular.sample.entity.CloudSample;
import vip.xiaonuo.sample.modular.sample.mapper.CloudSampleMapper;
import vip.xiaonuo.sample.modular.sample.param.CloudSampleParam;
import vip.xiaonuo.sample.modular.sample.service.CloudSampleService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 微服务范例service接口实现类
 *
 * @author dongxiayu
 * @date 2020/12/28 00:49
 */
@Service
public class CloudSampleServiceImpl extends ServiceImpl<CloudSampleMapper, CloudSample> implements CloudSampleService {

    /**
     * 根据范例名称获取范例
     *
     * @param sampleName 范例名称
     * @return 用户
     * @author dongxiayu
     * @date 2020/12/28 00:51
     */
    @Override
    public CloudSample getBySampleName(String sampleName){
        if(StrUtil.isBlank(sampleName)) {
            return null;
        }
        LambdaQueryWrapper<CloudSample> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.like(CloudSample::getSampleName,sampleName);
        return this.getOne(queryWrapper);
    }

    /**
     * 查询微服务范例
     *
     * @param cloudSampleParam 查询参数
     * @return 查询分页结果
     * @author dongxiayu
     * @date 2020/12/28 00:51
     */
    @Override
    public List<CloudSample> list(CloudSampleParam cloudSampleParam){
        if(cloudSampleParam==null){
            return null;
        }
        LambdaQueryWrapper<CloudSample> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(cloudSampleParam.getId()!=null, CloudSample::getId, cloudSampleParam.getId().longValue());
        queryWrapper.like(StrUtil.isNotBlank(cloudSampleParam.getSampleName()), CloudSample::getSampleName, cloudSampleParam.getSampleName());
        return this.list(queryWrapper);
    }

}
