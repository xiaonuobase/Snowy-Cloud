package vip.xiaonuo.sample.modular.sample.result;

import lombok.Data;

import java.util.Date;

/**
 * 微服务范例结果
 *
 * @author dongxiayu
 * @date 2020/12/28 00:19
 */
@Data
public class CloudSampleResult {

    /**
     * 主键
     */
    private Long id;

    /**
     * 范例名称
     */
    private String exampleName;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

}
