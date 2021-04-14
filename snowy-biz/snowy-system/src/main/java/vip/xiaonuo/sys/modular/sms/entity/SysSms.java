package vip.xiaonuo.sys.modular.sms.entity;


import vip.xiaonuo.common.pojo.base.entity.BaseEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.Date;

/**
 * 系统短信表
 *
 * @author yubaoshan
 * @date 2018/7/5 13:44
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("sys_sms")
public class SysSms extends BaseEntity implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;
    /**
     * 手机号
     */
    private String phoneNumbers;

    /**
     * 短信验证码
     */
    private String validateCode;

    /**
     * 短信模板ID
     */
    private String templateCode;

    /**
     * 回执id，可根据该id查询具体的发送状态
     */
    private String bizId;

    /**
     * 发送状态（字典 0 未发送，1 发送成功，2 发送失败，3 失效）
     */
    private Integer status;

    /**
     * 来源（字典 1 app， 2 pc， 3 其他）
     */
    private Integer source;

    /**
     * 失效时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date invalidTime;
}
