package vip.xiaonuo.main.modular.consumer;

import vip.xiaonuo.common.consts.FeignConstant;
import vip.xiaonuo.common.pojo.response.ResponseData;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * CloudSampleRestApiClient Feign Client
 *
 * @author dongxiayu
 * @date 2020/12/29 12:06
 */
@Component
@FeignClient(name = FeignConstant.SAMPLE_APP)
public interface CloudSampleRestApiConsumer {

    @RequestMapping("/sample/config/test")
    String configTest();

    @RequestMapping("/sample/getBySampleName")
    ResponseData getBySampleName(@RequestParam("sampleName") String sampleName);

}
