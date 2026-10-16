package vip.xiaonuo.dev.api.context;

import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import feign.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import vip.xiaonuo.common.exception.CommonException;
import vip.xiaonuo.dev.api.DevFileApi;
import vip.xiaonuo.dev.feign.DevFileFeign;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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
        return this.devFileFeign.storageFileWithReturnUrlFtp(file);
    }

    @Override
    public String storageFileWithReturnIdFtp(MultipartFile file) {
        return this.devFileFeign.storageFileWithReturnIdFtp(file);
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

    @Override
    public byte[] getFileBytes(String engine, String bucket, String fileKey) {
        return this.devFileFeign.getFileBytes(engine, bucket, fileKey);
    }

    @Override
    public String storageFileWithReturnUrlRustfs(MultipartFile file) {
        return this.devFileFeign.storageFileWithReturnUrlRustfs(file);
    }

    @Override
    public String storageFileWithReturnIdRustfs(MultipartFile file) {
        return this.devFileFeign.storageFileWithReturnIdRustfs(file);
    }

    @Override
    public Long getFileSize(String engine, String bucket, String fileKey) {
        return this.devFileFeign.getFileSize(engine, bucket, fileKey);
    }

    @Override
    public void writeFileToStream(String engine, String bucket, String fileKey, OutputStream outputStream) {
        // 原始响应不经错误解码器，需自行判断状态并关闭响应
        try (Response response = this.devFileFeign.writeFileToStream(engine, bucket, fileKey)) {
            if (response.status() != HttpStatus.OK.value()) {
                String body = ObjectUtil.isNull(response.body()) ? null : IoUtil.readUtf8(response.body().asInputStream());
                String msg = JSONUtil.isTypeJSONObject(body) ? JSONUtil.parseObj(body).getStr("msg") : body;
                throw new CommonException("读取远程文件流失败：{}", ObjectUtil.defaultIfBlank(msg, String.valueOf(response.status())));
            }
            if (ObjectUtil.isNotNull(response.body())) {
                IoUtil.copy(response.body().asInputStream(), outputStream);
            }
        } catch (IOException e) {
            throw new CommonException("读取远程文件流失败：{}", e.getMessage());
        }
    }

    @Override
    public String registerExistingFileReturnId(String engine, String bucket, String objectKey, String fileName, Long sizeBytes) {
        return this.devFileFeign.registerExistingFileReturnId(engine, bucket, objectKey, fileName, sizeBytes);
    }

    @Override
    public String storageStreamReturnId(String engine, InputStream inputStream, String fileName, Long size) {
        // Feign请求体需整体发送，跨服务时会把流读入内存，读取后关闭流与本地实现口径一致
        byte[] content = ObjectUtil.isNull(inputStream) ? null : IoUtil.readBytes(inputStream, true);
        return this.devFileFeign.storageStreamReturnId(engine, fileName, size, content);
    }

    @Override
    public void generateThumbnail(String fileId) {
        this.devFileFeign.generateThumbnail(fileId);
    }

    @Override
    public String registerSharedFileReturnId(String sourceFileId, String targetEngine, String fileName, Long sizeBytes) {
        return this.devFileFeign.registerSharedFileReturnId(sourceFileId, targetEngine, fileName, sizeBytes);
    }
}
