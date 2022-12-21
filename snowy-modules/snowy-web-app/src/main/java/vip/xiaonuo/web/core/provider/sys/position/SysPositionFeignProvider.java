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
package vip.xiaonuo.web.core.provider.sys.position;

import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vip.xiaonuo.sys.feign.SysPositionFeign;
import vip.xiaonuo.sys.modular.position.provider.SysPositionApiProvider;

/**
 * 职位Feign提供者
 *
 * @author dongxiayu
 * @date 2022/11/23 0:07
 */
@Slf4j
@RequiredArgsConstructor
@RestController
public class SysPositionFeignProvider implements SysPositionFeign {

    private final SysPositionApiProvider sysPositionApiProvider;

    /**
     * 根据id获取名称
     *
     * @param positionId
     * @author dongxiayu
     * @date 2022/8/4 10:13
     */
    @Override
    @RequestMapping("/feign/sys/position/getNameById")
    public String getNameById(@RequestParam(value = "positionId",required = false) String positionId) {
        return sysPositionApiProvider.getNameById(positionId);
    }

    /**
     * 获取职位选择器
     *
     * @param orgId
     * @param searchKey
     * @author dongxiayu
     * @date 2022/7/22 14:47
     */
    @Override
    @RequestMapping("/feign/sys/position/positionSelector")
    public String positionSelector(@RequestParam(value = "orgId",required = false) String orgId, @RequestParam(value = "searchKey",required = false) String searchKey) {
        return JSONUtil.toJsonStr(sysPositionApiProvider.positionSelector(orgId, searchKey));
    }
}