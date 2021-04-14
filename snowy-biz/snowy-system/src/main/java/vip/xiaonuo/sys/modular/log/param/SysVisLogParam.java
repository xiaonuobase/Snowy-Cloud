package vip.xiaonuo.sys.modular.log.param;

import vip.xiaonuo.common.pojo.base.param.BaseParam;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 访问日志参数
 *
 * @author xuyuxiang
 * @date 2020/3/26 9:15
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SysVisLogParam extends BaseParam {

    /**
     * 主键
     */
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 是否执行成功（Y-是，N-否）
     */
    private String success;

    /**
     * 具体消息
     */
    private String message;

    /**
     * ip
     */
    private String ip;

    /**
     * 地址
     */
    private String location;

    /**
     * 浏览器
     */
    private String browser;

    /**
     * 操作系统
     */
    private String os;

    /**
     * 访问类型（字典 1登入 2登出）
     */
    private Integer visType;

    /**
     * 访问时间
     */
    private Date visTime;

    /**
     * 访问人
     */
    private String account;
}
