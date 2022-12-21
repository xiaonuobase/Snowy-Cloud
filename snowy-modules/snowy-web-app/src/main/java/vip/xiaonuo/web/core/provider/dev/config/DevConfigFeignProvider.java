/*
 * Copyright [2022] [https://www.xiaonuo.vip]
 *
 * Snowy采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：
 *
 * 1.请不要删除和修改根目录下的LICENSE文件。
 * 2.请不要删除和修改Snowy源码头部的版权声明。
 * 3.本项目代码可免费商业使用，商业使用请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 https://www.xiaonuo.vip
 * 5.不可二次分发开源参与同类竞品，如有想法可联系团队xiaonuobase@qq.com商议合作。
 * 6.若您的项目无法满足以上几点，需要更多功能代码，获取Snowy商业授权许可，请在官网购买授权，地址为 https://www.xiaonuo.vip
 */
package vip.xiaonuo.web.core.provider.dev.config;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vip.xiaonuo.dev.feign.DevConfigFeign;
import vip.xiaonuo.dev.modular.config.provider.DevConfigApiProvider;

/**
 * 配置API Feign接口提供者
 *
 * @author dongxiayu
 * @date 2022/11/22 11:42
 */
@Slf4j
@RequiredArgsConstructor
@RestController
public class DevConfigFeignProvider implements DevConfigFeign {

    private final DevConfigApiProvider devConfigApiProvider;

    /**
     * 根据键获取值
     *
     * @param key
     * @author dongxiayu
     * @date 2022/11/12 11:11
     */
    @Override
    @PostMapping("/feign/dev/config/getValueByKey")
    public String getValueByKey(@RequestParam("key") String key) {
        return devConfigApiProvider.getValueByKey(key);
    }
}