package vip.xiaonuo.sys.modular.notice.result;

import lombok.Data;

import java.util.Date;

/**
 * 已收系统通知公告结果集
 *
 * @author xuyuxiang
 * @date 2020/6/29 12:20
 */
@Data
public class SysNoticeReceiveResult {

    /**
     * 主键
     */
    private Long id;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 类型（字典 1通知 2公告）
     */
    private Integer type;

    /**
     * 发布人id
     */
    private Long publicUserId;

    /**
     * 发布人姓名
     */
    private String publicUserName;

    /**
     * 发布机构id
     */
    private Long publicOrgId;

    /**
     * 发布机构名称
     */
    private String publicOrgName;

    /**
     * 发布时间
     */
    private Date publicTime;

    /**
     * 撤回时间
     */
    private Date cancelTime;

    /**
     * 状态（字典 0草稿 1发布 2撤回 3删除）
     */
    private Integer status;

    /**
     * 阅读状态（字典 0未读 1已读）
     */
    private Integer readStatus;

    /**
     * 阅读时间
     */
    private Date readTime;
}
