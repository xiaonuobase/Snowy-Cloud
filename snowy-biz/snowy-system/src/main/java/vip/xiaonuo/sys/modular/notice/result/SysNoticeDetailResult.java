package vip.xiaonuo.sys.modular.notice.result;

import cn.hutool.core.lang.Dict;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 系统通知公告详情结果集
 *
 * @author xuyuxiang
 * @date 2020/6/29 11:46
 */
@Data
public class SysNoticeDetailResult {

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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date publicTime;

    /**
     * 撤回时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date cancelTime;

    /**
     * 状态（字典 0草稿 1发布 2撤回 3删除）
     */
    private Integer status;

    /**
     * 通知到的用户id集合
     */
    private List<Long> noticeUserIdList;

    /**
     * 通知到的用户阅读信息集合
     */
    private List<Dict> noticeUserReadInfoList;
}
