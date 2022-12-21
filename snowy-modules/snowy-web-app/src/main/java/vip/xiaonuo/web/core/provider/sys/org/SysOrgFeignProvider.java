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
package vip.xiaonuo.web.core.provider.sys.org;

import cn.hutool.json.JSONUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vip.xiaonuo.sys.feign.SysOrgFeign;
import vip.xiaonuo.sys.modular.org.provider.SysOrgApiProvider;

/**
 * 组织Feign提供者
 *
 * @author dongxiayu
 * @date 2022/11/22 23:57
 */
@Slf4j
@RequiredArgsConstructor
@RestController
public class SysOrgFeignProvider implements SysOrgFeign {

    private final SysOrgApiProvider sysOrgApiProvider;

    /**
     * 根据id获取名称
     *
     * @param orgId
     * @author dongxiayu
     * @date 2022/8/4 10:12
     */
    @Override
    @RequestMapping("/feign/sys/org/getNameById")
    public String getNameById(@RequestParam(value = "orgId",required = false) String orgId) {
        return sysOrgApiProvider.getNameById(orgId);
    }

    /**
     * 根据组织id获取部门主管id
     *
     * @param orgId
     * @author dongxiayu
     * @date 2022/6/6 14:50
     */
    @Override
    @RequestMapping("/feign/sys/org/getSupervisorIdByOrgId")
    public String getSupervisorIdByOrgId(@RequestParam(value = "orgId",required = false) String orgId) {
        return sysOrgApiProvider.getSupervisorIdByOrgId(orgId);
    }

    /**
     * 获取组织树选择器
     *
     * @author dongxiayu
     * @date 2022/7/22 14:46
     **/
    @Override
    @RequestMapping("/feign/sys/org/orgTreeSelector")
    public String orgTreeSelector() {
        return JSONUtil.toJsonStr(sysOrgApiProvider.orgTreeSelector());
    }

    /**
     * 获取组织列表选择器
     *
     * @param parentId
     * @author dongxiayu
     * @date 2022/7/22 14:45
     */
    @Override
    @RequestMapping("/feign/sys/org/orgListSelector")
    public String orgListSelector(@RequestParam(value = "parentId",required = false) String parentId) {
        return JSONUtil.toJsonStr(sysOrgApiProvider.orgListSelector(parentId));
    }
}