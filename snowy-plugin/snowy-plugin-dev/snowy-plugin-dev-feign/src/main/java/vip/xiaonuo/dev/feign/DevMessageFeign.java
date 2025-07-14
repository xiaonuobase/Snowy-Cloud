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
package vip.xiaonuo.dev.feign;

import cn.hutool.json.JSONObject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vip.xiaonuo.common.consts.FeignConstant;

import java.util.List;

/**
 * 站内信API接口 Feign
 *
 * @author dongxiayu
 * @date 2022/11/22 22:46
 */
@FeignClient(name= FeignConstant.WEB_APP, contextId = "DevMessageFeign")
public interface DevMessageFeign {

    /**
     * 发送站内信，默认：分类系统通知
     *
     * @param receiverIdList 接收的用户id集合
     * @param subject 主题
     * @author xuyuxiang
     * @date 2022/6/22 17:35
     **/
    @PostMapping("/feign/dev/message/sendMessageWithTwoParam")
    void sendMessage(@RequestParam(value = "receiverIdList",required = false) List<String> receiverIdList,
                     @RequestParam(value = "subject",required = false) String subject);

    /**
     * 发送站内信指定分类
     *
     * @param receiverIdList 接收的用户id集合
     * @param subject 主题
     * @author xuyuxiang
     * @date 2022/6/22 17:35
     **/
    @PostMapping("/feign/dev/message/sendMessageWithThreeParam")
    void sendMessage(@RequestParam(value = "receiverIdList",required = false) List<String> receiverIdList,
                     @RequestParam(value = "category",required = false) String category,
                     @RequestParam(value = "subject",required = false) String subject);

    /**
     * 发送站内信带内容，默认：分类系统通知
     *
     * @param receiverIdList 接收的用户id集合
     * @param subject 主题
     * @param content 站内信内容
     * @author xuyuxiang
     * @date 2022/6/22 17:35
     **/
    @PostMapping("/feign/dev/message/sendMessageWithContent")
    void sendMessageWithContent(@RequestParam(value = "receiverIdList",required = false) List<String> receiverIdList,
                                @RequestParam(value = "subject",required = false) String subject,
                                @RequestParam(value = "content",required = false) String content);

    /**
     * 发送站内信带内容，指定分类
     *
     * @param receiverIdList 接收的用户id集合
     * @param subject 主题
     * @param content 站内信内容
     * @author xuyuxiang
     * @date 2022/6/22 17:35
     **/
    @PostMapping("/feign/dev/message/sendMessageWithContentWithFourParam")
    void sendMessageWithContent(@RequestParam(value = "receiverIdList",required = false) List<String> receiverIdList,
                                @RequestParam(value = "category",required = false) String category,
                                @RequestParam(value = "subject",required = false) String subject,
                                @RequestParam(value = "content",required = false) String content);

    /**
     * 获取未读站内信列表
     *
     * @author xuyuxiang
     * @date 2022/9/2 11:48
     */
    @PostMapping("/feign/dev/message/list")
    List<JSONObject> list(@RequestParam(value = "receiverIdList",required = false) List<String> receiverIdList,
                          @RequestParam(value = "limit",required = false) Integer limit);

    /**
     * 获取未读站内信数量
     *
     * @author diantu
     * @date 2023/7/10
     */
    @PostMapping("/feign/dev/message/unreadCount")
    Long unreadCount(@RequestParam(value = "loginId",required = false) String loginId);

    /**
     * 获取站内信分页
     *
     * @author xuyuxiang
     * @date 2022/9/2 11:48
     */
    @PostMapping("/feign/dev/message/page")
    String page(@RequestParam(value = "current",required = false) Integer current,
                @RequestParam(value = "size",required = false) Integer size,
                @RequestParam(value = "receiverIdList",required = false) List<String> receiverIdList,
                @RequestParam(value = "category",required = false) String category);

    /**
     * 获取站内信详情
     *
     * @param id 站内信id
     * @author xuyuxiang
     * @date 2022/4/24 20:08
     */
    @PostMapping("/feign/dev/message/detail")
    JSONObject detail(@RequestParam(value = "id",required = false) String id);

    /**
     * 站内信全部标记已读
     *
     * @author diantu
     * @date 2023/7/10
     */
    @PostMapping("/feign/dev/message/allMessageMarkRead")
    void allMessageMarkRead();
}
