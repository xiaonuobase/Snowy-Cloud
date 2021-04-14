package vip.xiaonuo.sys.modular.file.result;

import lombok.Data;

/**
 * 文件信息结果集
 *
 * @author yubaoshan
 * @date 2020/6/7 22:15
 */
@Data
public class SysFileInfoResult {

    /**
     * 主键id
     */
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

    /**
     * 文件的字节
     */
    private byte[] fileBytes;

}
