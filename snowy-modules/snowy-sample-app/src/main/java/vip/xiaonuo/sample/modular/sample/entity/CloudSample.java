package vip.xiaonuo.sample.modular.sample.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import vip.xiaonuo.common.pojo.base.entity.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 微服务样本表
 *
 * @author dongxiayu
 * @date 2020/12/28 00:25
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("cloud_sample")
public class CloudSample extends BaseEntity {

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 范例名称
     */
    private String sampleName;

}
