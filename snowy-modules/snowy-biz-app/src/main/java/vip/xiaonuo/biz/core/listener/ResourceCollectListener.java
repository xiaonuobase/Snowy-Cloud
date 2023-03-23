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
package vip.xiaonuo.biz.core.listener;

import cn.dev33.satoken.annotation.SaCheckPermission;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.log.Log;
import io.swagger.annotations.ApiOperation;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import vip.xiaonuo.common.cache.CommonCacheOperator;
import vip.xiaonuo.common.consts.CacheConstant;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * 资源搜集器，将项目中所有接口（带@RequestMapping的）都搜集起来
 *  <p>
 *  搜集到的接口会被缓存
 *
 * @author dongxiayu
 * @date 2023/1/29 23:24
 **/
@Component
public class ResourceCollectListener implements CommandLineRunner {

    private static final Log log = Log.get();

    @Resource
    private CommonCacheOperator commonCacheOperator;

    @Override
    public void run(String... args) {

        //1.获取所有后端接口
        List<String> permissionResult = CollectionUtil.newArrayList();
        SpringUtil.getApplicationContext().getBeansOfType(RequestMappingHandlerMapping.class).values()
                .forEach(requestMappingHandlerMapping -> requestMappingHandlerMapping.getHandlerMethods()
                        .forEach((key, value) -> {
                            SaCheckPermission saCheckPermission = value.getMethod().getAnnotation(SaCheckPermission.class);
                            if(ObjectUtil.isNotEmpty(saCheckPermission)) {
                                PatternsRequestCondition patternsCondition = key.getPatternsCondition();
                                if (patternsCondition != null) {
                                    String apiName = "未定义接口名称";
                                    ApiOperation apiOperation = value.getMethod().getAnnotation(ApiOperation.class);
                                    if(ObjectUtil.isNotEmpty(apiOperation)) {
                                        String annotationValue = apiOperation.value();
                                        if(ObjectUtil.isNotEmpty(annotationValue)) {
                                            apiName = annotationValue;
                                        }
                                    }
                                    permissionResult.add(patternsCondition.getPatterns().iterator().next() + StrUtil.BRACKET_START + apiName + StrUtil.BRACKET_END);
                                }
                            }
                        }));

        //2.汇总添加到缓存
        Object permissionResourceObject = commonCacheOperator.get(CacheConstant.PERMISSION_RESOURCE_CACHE_KEY);
        List<String> permissionResource = null;
        if(Objects.isNull(permissionResourceObject)){
            permissionResource = CollUtil.newArrayList();
        }else{
            permissionResource = Convert.toList(String.class,permissionResourceObject);
        }
        if(CollUtil.isNotEmpty(permissionResult)){
            for(String permission : permissionResult){
                if(!permissionResource.contains(permission)){
                    permissionResource.add(permission);
                }
            }

            // 刷新缓存
            commonCacheOperator.put(CacheConstant.PERMISSION_RESOURCE_CACHE_KEY, permissionResource);
        }

        log.info(">>> 缓存资源URL集合完成!资源数量：{}", permissionResult.size());
    }
}
