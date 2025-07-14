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
package vip.xiaonuo.dev.feign.provider.dev;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;
import vip.xiaonuo.dev.api.DevMessageApi;
import vip.xiaonuo.dev.feign.DevMessageFeign;

import java.util.List;

/**
 * DevMessageFeign提供者
 *
 * @author dongxiayu
 * @date 2022/11/22 22:50
 */
@Slf4j
@RequiredArgsConstructor
@RestController
public class DevMessageFeignProvider implements DevMessageFeign {

    private final DevMessageApi devMessageApi;

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
        this.devMessageApi.sendMessage(receiverIdList,subject);
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
        this.devMessageApi.sendMessage(receiverIdList,category,subject);
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
        this.devMessageApi.sendMessageWithContent(receiverIdList,subject,content);
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
        this.devMessageApi.sendMessageWithContent(receiverIdList,category,subject,content);
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
        return this.devMessageApi.list(receiverIdList,limit);
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
        return this.devMessageApi.unreadCount(loginId);
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
    public String page(Integer current, Integer size, List<String> receiverIdList, String category) {
        Page<JSONObject> page = this.devMessageApi.page(receiverIdList, category);
        String jsonStr = JSONUtil.toJsonStr(page);
        return jsonStr;
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
        return this.devMessageApi.detail(id);
    }

    /**
     * 站内信全部标记已读
     *
     * @author diantu
     * @date 2023/7/10
     */
    @Override
    public void allMessageMarkRead() {
        this.devMessageApi.allMessageMarkRead();
    }
}
