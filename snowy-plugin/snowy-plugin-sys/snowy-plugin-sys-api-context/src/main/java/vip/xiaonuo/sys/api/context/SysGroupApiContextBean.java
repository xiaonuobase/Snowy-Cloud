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
package vip.xiaonuo.sys.api.context;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import vip.xiaonuo.sys.api.SysGroupApi;
import vip.xiaonuo.sys.feign.SysGroupFeign;

import java.util.List;

/**
 * 用户组API上下文Bean
 *
 * @author yubaoshan
 * @date 2022/12/25 01:03
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class SysGroupApiContextBean implements SysGroupApi {

    private final SysGroupFeign sysGroupFeign;

    @Override
    public List<String> ownUser(String userId) {
        return sysGroupFeign.ownUser(userId);
    }

    @Override
    public void grantUser(String groupId, List<String> userIdList) {
        sysGroupFeign.grantUser(groupId, userIdList);
    }

    /**
     * 获取用户组选择器
     *
     * @param searchKey
     * @param current
     * @param size
     * @author yubaoshan
     * @date 2025/1/12 02:36
     */
    @Override
    public Page<JSONObject> groupSelector(String searchKey, int current, int size) {
        String feignResp = sysGroupFeign.groupSelector(searchKey,current,size);
        Page<JSONObject> resp = (Page<JSONObject>) JSONUtil.toBean(feignResp,Page.class);
        return resp;
    }
}
