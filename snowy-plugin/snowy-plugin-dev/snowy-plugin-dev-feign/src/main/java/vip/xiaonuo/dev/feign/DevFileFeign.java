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
package vip.xiaonuo.dev.feign;

import cn.hutool.json.JSONObject;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import vip.xiaonuo.common.consts.FeignConstant;

/**
 * 文件API接口 Feign
 *
 * @author dongxiayu
 * @date 2022/11/22 22:46
 */
@FeignClient(name= FeignConstant.WEB_APP, contextId = "DevFileFeign")
public interface DevFileFeign {

    /**
     * 动态上传文件返回id（使用系统配置的默认文件引擎）
     *
     * @author xuyuxiang
     * @date 2021/10/13 14:01
     **/
    @PostMapping("/feign/dev/file/uploadDynamicReturnId")
    String uploadDynamicReturnId(@RequestParam(value = "file",required = false) MultipartFile file);

    /**
     * 动态上传文件返回url（使用系统配置的默认文件引擎）
     *
     * @author xuyuxiang
     * @date 2021/10/13 14:01
     **/
    @PostMapping("/feign/dev/file/uploadDynamicReturnUrl")
    String uploadDynamicReturnUrl(@RequestParam(value = "file",required = false) MultipartFile file);

    /* =========本地文件========= */

    /**
     * 上传文件返回Url
     *
     * @param file 文件
     * @author xuyuxiang
     * @date 2022/6/22 17:44
     **/
    @PostMapping("/feign/dev/file/storageFileWithReturnUrlLocal")
    String storageFileWithReturnUrlLocal(@RequestParam(value = "file",required = false) MultipartFile file);

    /**
     * 上传文件返回Id
     *
     * @param file 文件
     * @author xuyuxiang
     * @date 2022/6/22 17:44
     **/
    @PostMapping("/feign/dev/file/storageFileWithReturnUrlLocal")
    String storageFileWithReturnIdLocal(@RequestParam(value = "file",required = false) MultipartFile file);

    /* =========阿里云文件========= */

    /**
     * 上传文件返回Url
     *
     * @param file 文件
     * @author xuyuxiang
     * @date 2022/6/22 17:44
     **/
    @PostMapping("/feign/dev/file/storageFileWithReturnUrlAliyun")
    String storageFileWithReturnUrlAliyun(@RequestParam(value = "file",required = false) MultipartFile file);

    /**
     * 上传文件返回Id
     *
     * @param file 文件
     * @author xuyuxiang
     * @date 2022/6/22 17:44
     **/
    @PostMapping("/feign/dev/file/storageFileWithReturnIdAliyun")
    String storageFileWithReturnIdAliyun(@RequestParam(value = "file",required = false) MultipartFile file);

    /* =========腾讯云件========= */

    /**
     * 上传文件返回Url
     *
     * @param file 文件
     * @author xuyuxiang
     * @date 2022/6/22 17:44
     **/
    @PostMapping("/feign/dev/file/storageFileWithReturnUrlTencent")
    String storageFileWithReturnUrlTencent(@RequestParam(value = "file",required = false) MultipartFile file);

    /**
     * 上传文件返回Id
     *
     * @param file 文件
     * @author xuyuxiang
     * @date 2022/6/22 17:44
     **/
    @PostMapping("/feign/dev/file/storageFileWithReturnIdTencent")
    String storageFileWithReturnIdTencent(@RequestParam(value = "file",required = false) MultipartFile file);

    /* =========MINIO件========= */

    /**
     * 上传文件返回Url
     *
     * @param file 文件
     * @author xuyuxiang
     * @date 2022/6/22 17:44
     **/
    @PostMapping("/feign/dev/file/storageFileWithReturnUrlMinio")
    String storageFileWithReturnUrlMinio(@RequestParam(value = "file",required = false) MultipartFile file);

    /**
     * 上传文件返回Id
     *
     * @param file 文件
     * @author xuyuxiang
     * @date 2022/6/22 17:44
     **/
    @PostMapping("/feign/dev/file/storageFileWithReturnIdMinio")
    String storageFileWithReturnIdMinio(@RequestParam(value = "file",required = false) MultipartFile file);

    /**
     * 通过文件id查询文件详情
     *
     * @author chengchuanyao
     * @date 2024/7/26 16:10
     */
    @PostMapping("/feign/dev/file/getFileInfoById")
    JSONObject getFileInfoById(@RequestParam(value = "id",required = false) String id);


    /**
     * 根据文件id物理删除文件
     *
     * @author xuyuxiang
     * @date 2022/8/4 10:36
     **/
    @PostMapping("/feign/dev/file/deleteAbsoluteById")
    void deleteAbsoluteById(@RequestParam(value = "id",required = false) String id);
}
