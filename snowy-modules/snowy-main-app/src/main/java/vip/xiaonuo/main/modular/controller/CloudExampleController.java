package vip.xiaonuo.main.modular.controller;

import vip.xiaonuo.common.pojo.response.ResponseData;
import vip.xiaonuo.common.pojo.response.SuccessResponseData;
import vip.xiaonuo.main.modular.consumer.CloudSampleRestApiConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * sprindcloud-config示例接口
 *
 * @author dongxiayu
 * @date 2020/12/28 19:09
 */
@RestController
@RequestMapping("/oauth")
public class CloudExampleController {

    @Value("${snowy.main.config.test}")
    private String configTest;

    @Autowired
    private CloudSampleRestApiConsumer cloudSampleRestApiConsumer;

    @RequestMapping("/config/test")
    public ResponseData configTest() {
        return new SuccessResponseData(configTest);
    }

    @RequestMapping("/sample/config/test")
    public ResponseData sampleConfigTest() {
        return ResponseData.success(cloudSampleRestApiConsumer.configTest());
    }

    @RequestMapping("/sample/getBySampleName")
    public ResponseData getBySampleName(String sampleName){
        return ResponseData.success(cloudSampleRestApiConsumer.getBySampleName(sampleName));
    }

}
