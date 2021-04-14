package vip.xiaonuo.sys.config;

import cn.hutool.core.util.ObjectUtil;
import vip.xiaonuo.common.context.constant.ConstantContextHolder;
import vip.xiaonuo.core.file.FileOperator;
import vip.xiaonuo.core.file.modular.local.LocalFileOperator;
import vip.xiaonuo.core.file.modular.local.prop.LocalFileProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 文件存储的配置
 * <p>
 * 默认激活本地文件存储
 *
 * @author yubaoshan
 * @date 2020/6/6 22:27
 */
@Configuration
public class FileConfig {

    /**
     * 默认文件存储的位置
     */
    public static final String DEFAULT_BUCKET = "defaultBucket";

    /**
     * 本地文件操作客户端
     *
     * @author yubaoshan
     * @date 2020/6/9 21:39
     */
    @Bean
    public FileOperator fileOperator() {
        LocalFileProperties localFileProperties = new LocalFileProperties();
        String fileUploadPathForWindows = ConstantContextHolder.getDefaultFileUploadPathForWindows();
        if (ObjectUtil.isNotEmpty(fileUploadPathForWindows)) {
            localFileProperties.setLocalFileSavePathWin(fileUploadPathForWindows);
        }

        String fileUploadPathForLinux = ConstantContextHolder.getDefaultFileUploadPathForLinux();
        if (ObjectUtil.isNotEmpty(fileUploadPathForLinux)) {
            localFileProperties.setLocalFileSavePathLinux(fileUploadPathForLinux);
        }
        return new LocalFileOperator(localFileProperties);
    }

}
