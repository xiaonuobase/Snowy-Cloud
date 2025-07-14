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
package vip.xiaonuo.mobile.feign.provider;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;
import vip.xiaonuo.mobile.api.MobileButtonApi;
import vip.xiaonuo.mobile.feign.MobileButtonFeign;

import java.util.List;

/**
 * 移动端管理模块ButtonFeign提供者
 *
 * @author dongxiayu
 * @date 2022/11/22 22:50
 */
@Slf4j
@RequiredArgsConstructor
@RestController
public class MobileButtonFeignProvider implements MobileButtonFeign {

    final private MobileButtonApi mobileButtonApi;

    /**
     * 根据按钮id集合获取按钮码列表
     *
     * @param idList
     * @author 每天一点
     * @date 2023/2/5 13:26
     */
    @Override
    public List<String> listButtonCodeListByIdList(List<String> idList) {
        return this.mobileButtonApi.listButtonCodeListByIdList(idList);
    }
}
