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

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

/**
 * 邮件API接口 Feign
 *
 * @author dongxiayu
 * @date 2022/11/22 22:46
 */
@FeignClient(name= FeignConstant.WEB_APP, contextId = "DevEmailFeign")
public interface DevEmailFeign {

    /**
     * 动态发送TXT邮件（使用系统配置的默认邮件引擎）
     *
     * @param tos 收件人邮箱，逗号拼接
     * @param subject 邮件主题
     * @param content 邮件内容
     * @author xuyuxiang
     * @date 2022/2/7 22:29
     */
    @PostMapping("/feign/dev/email/sendDynamicTxtEmail")
    void sendDynamicTxtEmail(@RequestParam(value = "tos",required = false) String tos,
                             @RequestParam(value = "subject",required = false) String subject,
                             @RequestParam(value = "content",required = false) String content);

    /**
     * 动态发送HTML邮件（使用系统配置的默认邮件引擎）
     *
     * @param tos 收件人邮箱，逗号拼接
     * @param subject 邮件主题
     * @param content 邮件内容
     * @author xuyuxiang
     * @date 2022/2/7 22:29
     */
    @PostMapping("/feign/dev/email/sendDynamicHtmlEmail")
    void sendDynamicHtmlEmail(@RequestParam(value = "tos",required = false) String tos,
                              @RequestParam(value = "subject",required = false) String subject,
                              @RequestParam(value = "content",required = false) String content);

    /* =========本地邮件========= */

    /**
     * 发送纯文本邮件
     *
     * @param tos 收件人邮箱，逗号拼接
     * @param subject 邮件主题
     * @param content 邮件内容
     * @param files 附件列表
     * @author xuyuxiang
     * @date 2022/2/7 22:29
     */
    @PostMapping("/feign/dev/email/sendTextEmailLocal")
    void sendTextEmailLocal(@RequestParam(value = "tos",required = false) String tos,
                            @RequestParam(value = "subject",required = false) String subject,
                            @RequestParam(value = "content",required = false) String content,
                            @RequestParam(value = "files",required = false) List<File> files);

    /**
     * 发送HTML邮件
     *
     * @param tos 收件人邮箱，逗号拼接
     * @param subject 邮件主题
     * @param content 邮件内容
     * @param imageMap – 图片与占位符，占位符格式为cid:$IMAGE_PLACEHOLDER
     * @param files 附件列表
     * @author xuyuxiang
     * @date 2022/2/7 22:29
     */
    @PostMapping("/feign/dev/email/sendHtmlEmailLocal")
    void sendHtmlEmailLocal(@RequestParam(value = "tos",required = false) String tos,
                            @RequestParam(value = "subject",required = false) String subject,
                            @RequestParam(value = "content",required = false) String content,
                            @RequestParam(value = "imageMap",required = false) Map<String, InputStream> imageMap,
                            @RequestParam(value = "files",required = false) List<File> files);

    /* =========阿里云邮件========= */

    /**
     * 发送纯文本邮件（不使用模板，频率限制100 QPS）
     *
     * @param from 管理控制台中配置的发信地址，必传且必须正确
     * @param user 发信人昵称，长度小于15个字符，可不传
     * @param tos 目标地址，多个 email 地址可以用逗号分隔，最多100个地址，必传且必须正确
     * @param subject 邮件主题，必传
     * @param content 邮件 txt 正文，限制28K，必传
     * @author xuyuxiang
     * @date 2022/2/23 14:24
     **/
    @PostMapping("/feign/dev/email/sendTextEmailAliyun")
    void sendTextEmailAliyun(@RequestParam(value = "from",required = false) String from,
                             @RequestParam(value = "user",required = false) String user,
                             @RequestParam(value = "tos",required = false) String tos,
                             @RequestParam(value = "subject",required = false) String subject,
                             @RequestParam(value = "content",required = false) String content);

    /**
     * 发送HTML邮件（不使用模板，频率限制100 QPS）
     *
     * @param from 管理控制台中配置的发信地址，必传且必须正确
     * @param user 发信人昵称，长度小于15个字符，可不传
     * @param tos 目标地址，多个 email 地址可以用逗号分隔，最多100个地址，必传且必须正确
     * @param subject 邮件主题，必传
     * @param content 邮件 html 正文，限制28K，必传
     * @author xuyuxiang
     * @date 2022/2/23 14:24
     **/
    @PostMapping("/feign/dev/email/sendHtmlEmailAliyun")
    void sendHtmlEmailAliyun(@RequestParam(value = "from",required = false) String from,
                             @RequestParam(value = "user",required = false) String user,
                             @RequestParam(value = "tos",required = false) String tos,
                             @RequestParam(value = "subject",required = false) String subject,
                             @RequestParam(value = "content",required = false) String content);

    /**
     * 使用模板发送邮件，国内频率限制是20/min；海外频率限制是10/min。
     *
     * @param from 管理控制台中配置的发信地址，必传且必须正确
     * @param tagName 控制台创建的邮件标签，可不传
     * @param toName 预先创建且上传了收件人的收件人列表名称，必传且必须正确
     * @param templateName 预先创建且通过审核的模板名称，必传且必须正确
     * @author xuyuxiang
     * @date 2022/2/23 14:24
     **/
    @PostMapping("/feign/dev/email/sendEmailWithTemplateAliyun")
    void sendEmailWithTemplateAliyun(@RequestParam(value = "from",required = false) String from,
                                     @RequestParam(value = "tagName",required = false) String tagName,
                                     @RequestParam(value = "toName",required = false) String toName,
                                     @RequestParam(value = "templateName",required = false) String templateName);

    /* =========腾讯云邮件========= */

    /**
     * 发送纯文本邮件（不使用模板，默认接口请求频率限制：20次/秒。）
     *
     * @param from 管理控制台中配置的发信地址，必传且必须正确
     * @param user 发信人昵称，可不传
     * @param tos 目标地址，多个 email 地址可以用逗号分隔，最多50个地址，必传且必须正确，非群发邮件请多次调用API发送
     * @param subject 邮件主题，必传
     * @param content 邮件 txt 正文，必传，注意：腾讯云api目前要求请求包大小不得超过8 MB。
     * @param attachmentList 需要发送附件时，填写附件相关参数，格式:[{"FileName": "xxxx", "Content": "xxx"}]
     *                       支持的格式与说明见：https://cloud.tencent.com/document/api/1288/51053#Attachment
     * @author xuyuxiang
     * @date 2022/2/23 14:24
     **/
    @PostMapping("/feign/dev/email/sendTextEmailTencent")
    void sendTextEmailTencent(@RequestParam(value = "from",required = false) String from,
                              @RequestParam(value = "user",required = false) String user,
                              @RequestParam(value = "tos",required = false) String tos,
                              @RequestParam(value = "subject",required = false) String subject,
                              @RequestParam(value = "content",required = false) String content,
                              @RequestParam(value = "attachmentList",required = false) List<JSONObject> attachmentList);

    /**
     * 发送HTML邮件（不使用模板，默认接口请求频率限制：20次/秒。）
     *
     * @param from 管理控制台中配置的发信地址，必传且必须正确
     * @param user 发信人昵称，可不传
     * @param tos 目标地址，多个 email 地址可以用逗号分隔，最多50个地址，必传且必须正确，非群发邮件请多次调用API发送
     * @param subject 邮件主题，必传
     * @param content 邮件 txt 正文，必传，注意：腾讯云api目前要求请求包大小不得超过8 MB。
     * @param attachmentList 需要发送附件时，填写附件相关参数，格式:[{"FileName": "xxxx", "Content": "xxx"}]
     *                       支持的格式与说明见：https://cloud.tencent.com/document/api/1288/51053#Attachment
     * @author xuyuxiang
     * @date 2022/2/23 14:24
     **/
    @PostMapping("/feign/dev/email/sendHtmlEmailTencent")
    void sendHtmlEmailTencent(@RequestParam(value = "from",required = false) String from,
                              @RequestParam(value = "user",required = false) String user,
                              @RequestParam(value = "tos",required = false) String tos,
                              @RequestParam(value = "subject",required = false) String subject,
                              @RequestParam(value = "content",required = false) String content,
                              @RequestParam(value = "attachmentList",required = false) List<JSONObject> attachmentList);

    /**
     * 使用模板发送邮件，默认接口请求频率限制：20次/秒。
     *
     * @param from 管理控制台中配置的发信地址，必传且必须正确
     * @param user 发信人昵称，可不传
     * @param toId 预先创建且上传了收件人的收件人列表id，必传且必须正确
     * @param templateId 预先创建且通过审核的模板Id，必传且必须正确
     * @param templateParam 预先创建且通过审核的模板的参数json，格式{"name":"张三"}，可不传
     * @param subject 邮件主题，必传
     * @author xuyuxiang
     * @date 2022/2/23 14:24
     **/
    @PostMapping("/feign/dev/email/sendEmailWithTemplateTencent")
    void sendEmailWithTemplateTencent(@RequestParam(value = "from",required = false) String from,
                                      @RequestParam(value = "user",required = false) String user,
                                      @RequestParam(value = "toId",required = false) String toId,
                                      @RequestParam(value = "templateId",required = false) String templateId,
                                      @RequestParam(value = "templateParam",required = false) String templateParam,
                                      @RequestParam(value = "subject",required = false) String subject,
                                      @RequestParam(value = "attachmentList",required = false) List<JSONObject> attachmentList);
}
