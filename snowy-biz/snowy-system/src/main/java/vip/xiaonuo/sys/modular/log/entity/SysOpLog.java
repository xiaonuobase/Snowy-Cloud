package vip.xiaonuo.sys.modular.log.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * 系统操作日志表
 *
 * @author xuyuxiang
 * @date 2020/3/11 11:56
 */
@Data
@TableName("sys_op_log")
public class SysOpLog {

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 名称
     */
    private String name;

    /**
     * 操作类型（见LogAnnotionOpTypeEnum）
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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date opTime;

    /**
     * 操作人
     */
    private String account;
}
