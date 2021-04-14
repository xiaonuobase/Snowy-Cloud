package vip.xiaonuo.sys.modular.notice.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

/**
 * 系统通知公告用户表
 *
 * @author xuyuxiang
 * @date 2020/6/29 10:45
 */
@Data
@TableName("sys_notice_user")
public class SysNoticeUser {

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 通知公告id
     */
    private Long noticeId;

    /**
     * 用户id
     */
    private Long userId;

    /**
     * 状态（字典 0未读 1已读）
     */
    private Integer status;

    /**
     * 阅读时间
     */
    private Date readTime;
}
