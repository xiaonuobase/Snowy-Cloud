package vip.xiaonuo.sys.modular.file.param;

import vip.xiaonuo.common.pojo.base.param.BaseParam;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.NotNull;

/**
 * <p>
 * 文件信息表
 * </p>
 *
 * @author yubaoshan
 * @date 2020/6/7 22:15
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SysFileInfoParam extends BaseParam {

    /**
     * 主键id
     */
    @NotNull(message = "id不能为空，请检查id参数", groups = {delete.class, detail.class})
    private Long id;

    /**
     * 文件存储位置（1:阿里云，2:腾讯云，3:minio，4:本地）
     */
    private Integer fileLocation;

    /**
     * 文件仓库
     */
    private String fileBucket;

    /**
     * 文件名称（上传时候的文件名）
     */
    private String fileOriginName;

    /**
     * 文件后缀
     */
    private String fileSuffix;

    /**
     * 文件大小kb
     */
    private Long fileSizeKb;

    /**
     * 存储到bucket的名称（文件唯一标识id）
     */
    private String fileObjectName;

    /**
     * 存储路径
     */
    private String filePath;

}
