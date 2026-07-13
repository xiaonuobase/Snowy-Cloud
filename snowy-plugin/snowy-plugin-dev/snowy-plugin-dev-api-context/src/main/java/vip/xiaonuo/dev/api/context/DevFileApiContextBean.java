package vip.xiaonuo.dev.api.context;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import vip.xiaonuo.dev.api.DevFileApi;
import vip.xiaonuo.dev.feign.DevFileFeign;

import java.util.List;

/**
 * 文件API上下文Bean
 *
 * @author yubaoshan
 * @date 2025/8/6 21:18
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class DevFileApiContextBean implements DevFileApi {

    final private DevFileFeign devFileFeign;

    @Override
    public String uploadDynamicReturnId(MultipartFile file) {
        return this.devFileFeign.uploadDynamicReturnId(file);
    }

    @Override
    public String uploadDynamicReturnUrl(MultipartFile file) {
        return this.devFileFeign.uploadDynamicReturnUrl(file);
    }

    @Override
    public String storageFileWithReturnUrlLocal(MultipartFile file) {
        return this.devFileFeign.storageFileWithReturnUrlLocal(file);
    }

    @Override
    public String storageFileWithReturnIdLocal(MultipartFile file) {
        return this.devFileFeign.storageFileWithReturnIdLocal(file);
    }

    @Override
    public String storageFileWithReturnUrlAliyun(MultipartFile file) {
        return this.devFileFeign.storageFileWithReturnUrlAliyun(file);
    }

    @Override
    public String storageFileWithReturnIdAliyun(MultipartFile file) {
        return this.devFileFeign.storageFileWithReturnIdAliyun(file);
    }

    @Override
    public String storageFileWithReturnUrlTencent(MultipartFile file) {
        return this.devFileFeign.storageFileWithReturnUrlTencent(file);
    }

    @Override
    public String storageFileWithReturnIdTencent(MultipartFile file) {
        return this.devFileFeign.storageFileWithReturnIdTencent(file);
    }

    @Override
    public String storageFileWithReturnUrlMinio(MultipartFile file) {
        return this.devFileFeign.storageFileWithReturnUrlMinio(file);
    }

    @Override
    public String storageFileWithReturnIdMinio(MultipartFile file) {
        return this.devFileFeign.storageFileWithReturnIdMinio(file);
    }

    @Override
    public String storageFileWithReturnUrlFtp(MultipartFile file) {
        return "";
    }

    @Override
    public String storageFileWithReturnIdFtp(MultipartFile file) {
        return "";
    }

    @Override
    public JSONObject getFileInfoById(String id) {
        return this.devFileFeign.getFileInfoById(id);
    }

    @Override
    public void deleteAbsoluteById(String id) {
        this.devFileFeign.deleteAbsoluteById(id);
    }

    @Override
    public JSONArray getFileListByIds(List<String> ids) {
        return this.devFileFeign.getFileListByIds(ids);
    }

    @Override
    public String storageFileWithReturnUrlOss(MultipartFile file) {
        return this.devFileFeign.storageFileWithReturnUrlOss(file);
    }
}
