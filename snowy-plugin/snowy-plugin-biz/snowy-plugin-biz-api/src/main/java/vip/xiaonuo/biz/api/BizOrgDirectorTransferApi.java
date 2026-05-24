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
package vip.xiaonuo.biz.api;

import cn.hutool.json.JSONObject;

import java.util.List;

/**
 * 机构主管权限转移Api
 *
 * @author yubaoshan
 * @date 2026/05/17
 **/
public interface BizOrgDirectorTransferApi {

    /**
     * 获取指定用户作为机构主管的机构数量
     *
     * @author yubaoshan
     * @date 2026/05/17
     */
    long getOrgDirectorCount(String userId);

    /**
     * 获取指定用户作为机构主管的机构明细列表
     *
     * @author yubaoshan
     * @date 2026/05/17
     */
    List<JSONObject> getOrgDirectorDetailList(String userId);

    /**
     * 执行机构主管权限转移
     *
     * @param sourceUserId 源用户ID
     * @param targetUserId 目标用户ID（删除模式下可为null）
     * @param transferMode 转移模式：TRANSFER、COPY、DELETE
     * @param transferAll  是否全部转移
     * @param selectedIds  选中的资源ID列表（transferAll为true时可为空）
     * @author yubaoshan
     * @date 2026/05/17
     */
    void executeOrgDirectorTransfer(String sourceUserId, String targetUserId, String transferMode,
                                    boolean transferAll, List<String> selectedIds);
}
