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
package vip.xiaonuo.sample.modular.sample.controller;

import vip.xiaonuo.common.pojo.response.ResponseData;
import vip.xiaonuo.sample.modular.sample.param.CloudSampleParam;
import vip.xiaonuo.sample.modular.sample.service.CloudSampleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * sprindcloud-config示例接口
 *
 * @author dongxiayu
 * @date 2020/12/28 19:09
 */
@RestController
@RequestMapping("/sample")
public class CloudSampleController {

    @Value("${snowy.sample.config.test}")
    private String configTest;

    @Autowired
    private CloudSampleService cloudSampleService;

    @RequestMapping("/config/test")
    public ResponseData configTest() {
        return ResponseData.success(configTest);
    }

    @RequestMapping("/getBySampleName")
    public ResponseData getBySampleName(String sampleName){
        return ResponseData.success(cloudSampleService.getBySampleName(sampleName));
    }

    @RequestMapping("/list")
    public ResponseData list(@RequestBody CloudSampleParam cloudSampleParam){
        return ResponseData.success(cloudSampleService.list(cloudSampleParam));
    }

}
