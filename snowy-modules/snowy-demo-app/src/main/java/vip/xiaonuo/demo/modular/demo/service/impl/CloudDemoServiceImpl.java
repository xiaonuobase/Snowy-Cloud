/*
Copyright [2020] [https://www.xiaonuo.vip]

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

Snowy采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：

1.请不要删除和修改根目录下的LICENSE文件。
2.请不要删除和修改Snowy源码头部的版权声明。
3.请保留源码和相关描述文件的项目出处，作者声明等。
4.分发源码时候，请注明软件出处 https://gitee.com/xiaonuobase/snowy-cloud
5.在修改包名，模块名称，项目代码等时，请注明软件出处 https://gitee.com/xiaonuobase/snowy-cloud
6.若您的项目无法满足以上几点，可申请商业授权，获取Snowy商业授权许可，请在官网购买授权，地址为 https://www.xiaonuo.vip
 */
package vip.xiaonuo.demo.modular.demo.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import vip.xiaonuo.demo.modular.demo.entity.CloudDemo;
import vip.xiaonuo.demo.modular.demo.mapper.CloudDemoMapper;
import vip.xiaonuo.demo.modular.demo.param.CloudDemoParam;
import vip.xiaonuo.demo.modular.demo.service.CloudDemoService;

import java.util.List;

/**
 * 微服务demo service接口实现类
 *
 * @author dongxiayu
 * @date 2020/12/28 00:49
 */
@Service
public class CloudDemoServiceImpl extends ServiceImpl<CloudDemoMapper, CloudDemo> implements CloudDemoService {

    /**
     * 根据demo名称获取demo
     *
     * @param demoName demo名称
     * @return 用户
     * @author dongxiayu
     * @date 2020/12/28 00:51
     */
    @Override
    public CloudDemo getByDemoName(String demoName){
        if(StrUtil.isBlank(demoName)) {
            return null;
        }
        LambdaQueryWrapper<CloudDemo> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.like(CloudDemo::getDemoName,demoName);
        return this.getOne(queryWrapper);
    }

    /**
     * 查询微服务demo
     *
     * @param cloudDemoParam 查询参数
     * @return 查询分页结果
     * @author dongxiayu
     * @date 2020/12/28 00:51
     */
    @Override
    public List<CloudDemo> list(CloudDemoParam cloudDemoParam){
        if(cloudDemoParam==null){
            return null;
        }
        LambdaQueryWrapper<CloudDemo> queryWrapper = Wrappers.lambdaQuery();
        queryWrapper.eq(cloudDemoParam.getId()!=null, CloudDemo::getId, cloudDemoParam.getId().longValue());
        queryWrapper.like(StrUtil.isNotBlank(cloudDemoParam.getDemoName()), CloudDemo::getDemoName, cloudDemoParam.getDemoName());
        return this.list(queryWrapper);
    }

}
