/*
 * Copyright [2022] [https://www.xiaonuo.vip]
 *
 * Snowy采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：
 *
 * 1.请不要删除和修改根目录下的LICENSE文件。
 * 2.请不要删除和修改Snowy源码头部的版权声明。
 * 3.本项目代码可免费商业使用，商业使用请保留源码和相关描述文件的项目出处，作者声明等。
 * 4.分发源码时候，请注明软件出处 https://www.xiaonuo.vip
 * 5.不可二次分发开源参与同类竞品，如有想法可联系团队xiaonuobase@qq.com商议合作。
 * 6.若您的项目无法满足以上几点，需要更多功能代码，获取Snowy商业授权许可，请在官网购买授权，地址为 https://www.xiaonuo.vip
 */
package vip.xiaonuo.dev.feign.provider.file;

import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import vip.xiaonuo.dev.api.DevFileApi;
import vip.xiaonuo.dev.feign.DevFileFeign;

import java.util.List;

/**
 * DevFileFeign提供者
 *
 * @author yubaoshan
 * @date 2025/8/6 21:18
 */
@Slf4j
@RequiredArgsConstructor
@RestController
public class DevFileFeignProvider implements DevFileFeign {

    private final DevFileApi devFileApi;

    @Override
    public String uploadDynamicReturnId(MultipartFile file) {
        return devFileApi.uploadDynamicReturnId(file);
    }

    @Override
    public String uploadDynamicReturnUrl(MultipartFile file) {
        return devFileApi.uploadDynamicReturnUrl(file);
    }

    @Override
    public String storageFileWithReturnUrlLocal(MultipartFile file) {
        return devFileApi.storageFileWithReturnUrlLocal(file);
    }

    @Override
    public String storageFileWithReturnIdLocal(MultipartFile file) {
        return devFileApi.storageFileWithReturnIdLocal(file);
    }

    @Override
    public String storageFileWithReturnUrlAliyun(MultipartFile file) {
        return devFileApi.storageFileWithReturnUrlAliyun(file);
    }

    @Override
    public String storageFileWithReturnIdAliyun(MultipartFile file) {
        return devFileApi.storageFileWithReturnIdAliyun(file);
    }

    @Override
    public String storageFileWithReturnUrlTencent(MultipartFile file) {
        return devFileApi.storageFileWithReturnUrlTencent(file);
    }

    @Override
    public String storageFileWithReturnIdTencent(MultipartFile file) {
        return devFileApi.storageFileWithReturnIdTencent(file);
    }

    @Override
    public String storageFileWithReturnUrlMinio(MultipartFile file) {
        return devFileApi.storageFileWithReturnUrlMinio(file);
    }

    @Override
    public String storageFileWithReturnIdMinio(MultipartFile file) {
        return devFileApi.storageFileWithReturnIdMinio(file);
    }

    @Override
    public JSONObject getFileInfoById(String id) {
        return devFileApi.getFileInfoById(id);
    }

    @Override
    public void deleteAbsoluteById(String id) {
        devFileApi.deleteAbsoluteById(id);
    }

    @Override
    public JSONArray getFileListByIds(List<String> ids) {
        return devFileApi.getFileListByIds(ids);
    }

    @Override
    public String storageFileWithReturnUrlOss(MultipartFile file) {
        return devFileApi.storageFileWithReturnUrlOss(file);
    }
}
