package vip.xiaonuo.sys.modular.file.enums;

import lombok.Getter;

/**
 * 文件存储位置
 *
 * @author yubaoshan
 * @date 2020/6/7 22:24
 */
@Getter
public enum FileLocationEnum {

    /**
     * 阿里云
     */
    ALIYUN(1),

    /**
     * 腾讯云
     */
    TENCENT(2),

    /**
     * minio服务器
     */
    MINIO(3),

    /**
     * 本地
     */
    LOCAL(4);

    private final Integer code;

    FileLocationEnum(int code) {
        this.code = code;
    }

}
