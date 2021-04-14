package vip.xiaonuo.sys.modular.log.param;

import vip.xiaonuo.common.pojo.base.param.BaseParam;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 操作日志参数
 *
 * @author xuyuxiang
 * @date 2020/3/26 9:16
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SysOpLogParam extends BaseParam {

    /**
     * 主键
     */
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 操作类型（0其他 1增加 2删除 3编辑 ...见BasePram的参数校验类型）
     */
    private Integer opType;

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
     * 请求地址
     */
    private String url;

    /**
     * 类名称
     */
    private String className;

    /**
     * 方法名称
     */
    private String methodName;

    /**
     * 请求方式（GET POST PUT DELETE)
     */
    private String reqMethod;

    /**
     * 请求参数
     */
    private String param;

    /**
     * 返回结果
     */
    private String result;

    /**
     * 操作时间
     */
    private Date opTime;

    /**
     * 操作人
     */
    private String account;
}
