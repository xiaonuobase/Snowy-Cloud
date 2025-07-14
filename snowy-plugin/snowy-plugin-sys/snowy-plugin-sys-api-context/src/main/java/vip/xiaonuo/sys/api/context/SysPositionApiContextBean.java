package vip.xiaonuo.sys.api.context;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import vip.xiaonuo.sys.api.SysPositionApi;
import vip.xiaonuo.sys.feign.SysPositionFeign;

/**
 * 系统模块PositionAPI上下文Bean
 *
 * @author dongxiayu
 * @date 2022/11/22 22:34
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class SysPositionApiContextBean implements SysPositionApi {

    final private SysPositionFeign sysPositionFeign;

    /**
     * 根据id获取名称
     *
     * @param positionId
     * @author xuyuxiang
     * @date 2022/8/4 10:13
     */
    @Override
    public String getNameById(String positionId) {
        return this.sysPositionFeign.getNameById(positionId);
    }

    /**
     * 获取职位选择器
     *
     * @param orgId
     * @param searchKey
     * @param current
     * @param size
     * @author xuyuxiang
     * @date 2022/7/22 14:47
     */
    @Override
    public Page<JSONObject> positionSelector(String orgId, String searchKey, Integer current, Integer size) {
        String feignResp = this.sysPositionFeign.positionSelector(current, size, orgId, searchKey);
        Page<JSONObject> resp = (Page<JSONObject>) JSONUtil.toBean(feignResp,Page.class);
        return resp;
    }
}
