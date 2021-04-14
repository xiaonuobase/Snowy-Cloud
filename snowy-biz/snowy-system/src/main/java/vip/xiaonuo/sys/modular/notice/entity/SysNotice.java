package vip.xiaonuo.sys.modular.notice.entity;

import vip.xiaonuo.common.pojo.base.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 系统通知公告表
 *
 * @author xuyuxiang
 * @date 2020/6/28 17:12
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("sys_notice")
public class SysNotice extends BaseEntity {

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
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
}
