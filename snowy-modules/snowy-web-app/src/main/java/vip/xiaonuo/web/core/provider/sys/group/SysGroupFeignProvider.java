package vip.xiaonuo.web.core.provider.sys.group;

import cn.hutool.json.JSONObject;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import vip.xiaonuo.dev.modular.config.provider.DevConfigApiProvider;
import vip.xiaonuo.sys.feign.SysGroupFeign;
import vip.xiaonuo.sys.modular.group.provider.SysGroupApiProvider;

import java.util.List;

/**
 * 配置API Feign接口提供者
 *
 * @author dongxiayu
 * @date 2022/11/22 11:42
 */
@Slf4j
@RequiredArgsConstructor
@RestController
public class SysGroupFeignProvider implements SysGroupFeign {

    private final SysGroupApiProvider sysGroupApiProvider;

    /**
     * 根据id获取名称
     *
     * @param userId
     * @author yubaoshan
     * @date 2022/12/25 01:03
     */
    @Override
    @RequestMapping("/feign/sys/group/ownUser")
    public List<String> ownUser(@RequestParam(value = "userId",required = false) String userId) {
        return sysGroupApiProvider.ownUser(userId);
    }

    /**
     * 根据组织id获取部门主管id
     *
     * @param groupId
     * @param userIdList
     * @author yubaoshan
     * @date 2022/12/25 01:03
     */
    @Override
    @RequestMapping("/feign/sys/group/grantUser")
    public void grantUser(@RequestParam(value = "groupId",required = false) String groupId, @RequestParam(value = "userIdList",required = false) List<String> userIdList) {
        sysGroupApiProvider.grantUser(groupId, userIdList);
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
    @RequestMapping("/feign/sys/group/groupSelector")
    public String groupSelector(@RequestParam(value = "searchKey",required = false) String searchKey,
                                @RequestParam(value = "current",required = false) int current,
                                @RequestParam(value = "size",required = false) int size) {
        Page<JSONObject> jsonObjectPage = sysGroupApiProvider.groupSelector(searchKey, current, size);
        String resp = JSON.toJSONString(jsonObjectPage);
        return resp;
    }
}
