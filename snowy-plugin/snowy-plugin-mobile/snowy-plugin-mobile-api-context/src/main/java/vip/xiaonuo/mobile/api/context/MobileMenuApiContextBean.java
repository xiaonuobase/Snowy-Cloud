package vip.xiaonuo.mobile.api.context;

import cn.hutool.core.lang.tree.Tree;
import cn.hutool.json.JSONObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import vip.xiaonuo.mobile.api.MobileMenuApi;
import vip.xiaonuo.mobile.feign.MobileMenuFeign;

import java.util.List;

/**
 * 系统模块MenuAPI上下文Bean
 *
 * @author xuyuxiang
 * @date 2022/11/22 22:34
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class MobileMenuApiContextBean implements MobileMenuApi {

    final private MobileMenuFeign mobileMenuFeign;

    /**
     * 获取移动端菜单授权树
     *
     * @author xuyuxiang
     * @date 2023/1/31 10:10
     **/
    @Override
    public List<JSONObject> mobileMenuTreeSelector() {
        return this.mobileMenuFeign.mobileMenuTreeSelector();
    }

    /**
     * 获取移动端菜单授权树
     *
     * @param originDataList
     * @author xuyuxiang
     * @date 2023/1/31 10:10
     */
    @Override
    public List<JSONObject> mobileMenuTreeSelector(List<JSONObject> originDataList) {
        return this.mobileMenuFeign.mobileMenuTreeSelector(originDataList);
    }

    /**
     * 获取移动端登录菜单树
     *
     * @param menuIdList
     * @author xuyuxiang
     * @date 2023/1/31 10:29
     */
    @Override
    public List<Tree<String>> loginMobileMenuTree(List<String> menuIdList) {
        return this.mobileMenuFeign.loginMobileMenuTree(menuIdList);
    }

}
