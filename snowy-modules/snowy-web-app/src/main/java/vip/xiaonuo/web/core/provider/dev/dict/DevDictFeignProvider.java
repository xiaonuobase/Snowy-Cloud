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
package vip.xiaonuo.web.core.provider.dev.dict;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vip.xiaonuo.dev.feign.DevDictFeign;
import vip.xiaonuo.dev.modular.dict.provider.DevDictApiProvider;

/**
 * 字典翻译API Feign接口提供者
 *
 * @author yubaoshan
 * @date 2025/3/24 02:39
 */
@Slf4j
@RequiredArgsConstructor
@RestController
public class DevDictFeignProvider implements DevDictFeign {

    private final DevDictApiProvider devDictApiProvider;

    /**
     * 通过位置获得轮播图列表
     *
     * @author yubaoshan
     * @date  2024/07/13 00:31
     **/
    @Override
    @RequestMapping("/feign/dev/dict/getDictLabel")
    public String getDictLabel(@RequestParam("typeCode") String typeCode, @RequestParam("value") String value) {
        return devDictApiProvider.getDictLabel(typeCode, value);
    }
}
