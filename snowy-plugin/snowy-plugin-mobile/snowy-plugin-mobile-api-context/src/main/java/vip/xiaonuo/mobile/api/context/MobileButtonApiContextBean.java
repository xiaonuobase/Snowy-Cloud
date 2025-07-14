package vip.xiaonuo.mobile.api.context;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import vip.xiaonuo.mobile.api.MobileButtonApi;
import vip.xiaonuo.mobile.feign.MobileButtonFeign;

import java.util.List;

/**
 * 系统模块ButtonAPI上下文Bean
 *
 * @author xuyuxiang
 * @date 2022/11/22 22:34
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class MobileButtonApiContextBean implements MobileButtonApi {

    final private MobileButtonFeign mobileButtonFeign;

    /**
     * 根据按钮id集合获取按钮码列表
     *
     * @param idList
     * @author 每天一点
     * @date 2023/2/5 13:26
     */
    @Override
    public List<String> listButtonCodeListByIdList(List<String> idList) {
        return this.mobileButtonFeign.listButtonCodeListByIdList(idList);
    }
}
