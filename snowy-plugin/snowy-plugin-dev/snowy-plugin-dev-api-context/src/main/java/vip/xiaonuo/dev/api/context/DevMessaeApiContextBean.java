package vip.xiaonuo.dev.api.context;

import cn.hutool.core.convert.Convert;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import vip.xiaonuo.common.page.CommonPageRequest;
import vip.xiaonuo.dev.api.DevMessageApi;
import vip.xiaonuo.dev.feign.DevMessageFeign;

import java.util.List;

/**
 * DevMessageApi上下文Bean
 *
 * @author xuyuxiang
 * @date 2022/11/22 22:34
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class DevMessaeApiContextBean implements DevMessageApi {

    final private DevMessageFeign devMessageFeign;

    /**
     * 发送站内信，默认：分类系统通知
     *
     * @param receiverIdList 接收的用户id集合
     * @param subject        主题
     * @author xuyuxiang
     * @date 2022/6/22 17:35
     **/
    @Override
    public void sendMessage(List<String> receiverIdList, String subject) {
        this.devMessageFeign.sendMessage(receiverIdList,subject);
    }

    /**
     * 发送站内信指定分类
     *
     * @param receiverIdList 接收的用户id集合
     * @param category
     * @param subject        主题
     * @author xuyuxiang
     * @date 2022/6/22 17:35
     **/
    @Override
    public void sendMessage(List<String> receiverIdList, String category, String subject) {
        this.devMessageFeign.sendMessage(receiverIdList,category,subject);
    }

    /**
     * 发送站内信带内容，默认：分类系统通知
     *
     * @param receiverIdList 接收的用户id集合
     * @param subject        主题
     * @param content        站内信内容
     * @author xuyuxiang
     * @date 2022/6/22 17:35
     **/
    @Override
    public void sendMessageWithContent(List<String> receiverIdList, String subject, String content) {
        this.devMessageFeign.sendMessageWithContent(receiverIdList,subject,content);
    }

    /**
     * 发送站内信带内容，指定分类
     *
     * @param receiverIdList 接收的用户id集合
     * @param category
     * @param subject        主题
     * @param content        站内信内容
     * @author xuyuxiang
     * @date 2022/6/22 17:35
     **/
    @Override
    public void sendMessageWithContent(List<String> receiverIdList, String category, String subject, String content) {
        this.devMessageFeign.sendMessageWithContent(receiverIdList,category,subject,content);
    }

    /**
     * 获取未读站内信列表
     *
     * @param receiverIdList
     * @param limit
     * @author xuyuxiang
     * @date 2022/9/2 11:48
     */
    @Override
    public List<JSONObject> list(List<String> receiverIdList, Integer limit) {
        return this.devMessageFeign.list(receiverIdList,limit);
    }

    /**
     * 获取未读站内信数量
     *
     * @param loginId
     * @author diantu
     * @date 2023/7/10
     */
    @Override
    public Long unreadCount(String loginId) {
        return this.devMessageFeign.unreadCount(loginId);
    }

    /**
     * 获取站内信分页
     *
     * @param receiverIdList
     * @param category
     * @author xuyuxiang
     * @date 2022/9/2 11:48
     */
    @Override
    public Page<JSONObject> page(List<String> receiverIdList, String category) {
        long current = CommonPageRequest.defaultPage().getCurrent();
        long size = CommonPageRequest.defaultPage().getSize();
        String feignResp = this.devMessageFeign.page(Convert.toInt(current), Convert.toInt(size), receiverIdList, category);
        Page<JSONObject> resp = (Page<JSONObject>) JSONUtil.toBean(feignResp,Page.class);
        return resp;
    }

    /**
     * 获取站内信详情
     *
     * @param id 站内信id
     * @author xuyuxiang
     * @date 2022/4/24 20:08
     */
    @Override
    public JSONObject detail(String id) {
        return this.devMessageFeign.detail(id);
    }

    /**
     * 站内信全部标记已读
     *
     * @author diantu
     * @date 2023/7/10
     */
    @Override
    public void allMessageMarkRead() {
        this.devMessageFeign.allMessageMarkRead();
    }
}
