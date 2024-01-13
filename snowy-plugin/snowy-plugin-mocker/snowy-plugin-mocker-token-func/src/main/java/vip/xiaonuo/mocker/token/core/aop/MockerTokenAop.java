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
package vip.xiaonuo.mocker.token.core.aop;

import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import vip.xiaonuo.auth.feign.AuthInnerFeign;
import vip.xiaonuo.common.pojo.CommonResult;
import vip.xiaonuo.common.util.CommonCryptogramUtil;
import vip.xiaonuo.common.util.CommonThreadLocalUtil;
import vip.xiaonuo.mocker.token.annotation.CommonMockerToken;
import vip.xiaonuo.mocker.token.core.consts.MockerTokenConstant;

import java.lang.reflect.Method;
import java.util.Objects;

/**
 * JobTokenMocker aop切面
 *
 * @author dongxiayu
 * @date 2023/11/12 11:47
 */
@Aspect
@Order
@Component
@Slf4j
public class MockerTokenAop {

    @Autowired
    private AuthInnerFeign authInnerFeign;

    /**
     * JobTokenMocker切入点
     *
     * @author dongxiayu
     * @date 2023/11/12 11:47
     */
    @Pointcut("@annotation(vip.xiaonuo.mocker.token.annotation.CommonMockerToken)")
    private void getTokenMockerPointCut() {
    }

    /**
     * JobTokenMocker
     *
     * @author dongxiayu
     * @date 2023/11/12 11:47
     */
    @Before("getTokenMockerPointCut()")
    public void doBefore(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        CommonMockerToken commonMockerToken = method.getAnnotation(CommonMockerToken.class);


        String username = commonMockerToken.username();
        String password = commonMockerToken.password();
        if(StrUtil.isBlank(username) || StrUtil.isBlank(password)){
            return;
        }

        try {
            String passwordEncrypt = CommonCryptogramUtil.doSm2Encrypt(password);
            CommonResult<String> tokenResult = authInnerFeign.doLogin(username,passwordEncrypt);

            if(Objects.nonNull(tokenResult) && tokenResult.getCode()==CommonResult.CODE_SUCCESS){
                // 获取token信息
                String token = tokenResult.getData();

                CommonThreadLocalUtil.set(MockerTokenConstant.MOCKER_TOKEN_Token, token);

            }

        } catch (Exception e){
            // 内部登录发生异常，静默记录日志处理，不参与真实业务
            log.error("vip.xiaonuo.mocker.token.core.aop.MockerTokenAop.doLogin.error",e);
        }

    }

    /**
     * JobTokenMocker
     *
     * @author dongxiayu
     * @date 2023/11/12 11:47
     */
    @After("getTokenMockerPointCut()")
    public void doAfter(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        Method method = methodSignature.getMethod();
        CommonMockerToken commonMockerToken = method.getAnnotation(CommonMockerToken.class);
        String username = commonMockerToken.username();
        String password = commonMockerToken.password();

        String token = CommonThreadLocalUtil.get(MockerTokenConstant.MOCKER_TOKEN_Token);

        if(StrUtil.isBlank(token)){
            return;
        }

        try {
            CommonThreadLocalUtil.remove(MockerTokenConstant.MOCKER_TOKEN_Token);
            authInnerFeign.doLogout(token);
        }catch (Exception e){
            // 内部登录发生异常，静默记录日志处理，不参与真实业务
            log.error("vip.xiaonuo.mocker.token.core.aop.MockerTokenAop.doLogout.error",e);
        }
    }

}
