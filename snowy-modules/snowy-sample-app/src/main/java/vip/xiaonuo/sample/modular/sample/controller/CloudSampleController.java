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
