package vip.xiaonuo.sys.core.log.factory;

import cn.hutool.core.date.DateTime;
import vip.xiaonuo.common.consts.SymbolConstant;
import vip.xiaonuo.core.util.JoinPointUtil;
import vip.xiaonuo.sys.core.enums.LogSuccessStatusEnum;
import vip.xiaonuo.sys.core.enums.VisLogTypeEnum;
import vip.xiaonuo.sys.modular.log.entity.SysOpLog;
import vip.xiaonuo.sys.modular.log.entity.SysVisLog;
import vip.xiaonuo.common.annotion.BusinessLog;
import org.aspectj.lang.JoinPoint;

import java.util.Arrays;

/**
 * 日志对象创建工厂
 *
 * @author xuyuxiang
 * @date 2020/3/12 14:31
 */
public class LogFactory {

    /**
     * 创建登录日志
     *
     * @author xuyuxiang
     * @date 2020/3/12 16:09
     */
    static void createSysLoginLog(SysVisLog sysVisLog, String account, String successCode, String failMessage) {
        sysVisLog.setName(VisLogTypeEnum.LOGIN.getMessage());
        sysVisLog.setSuccess(successCode);

        sysVisLog.setVisType(VisLogTypeEnum.LOGIN.getCode());
        sysVisLog.setVisTime(DateTime.now());
        sysVisLog.setAccount(account);

        if (LogSuccessStatusEnum.SUCCESS.getCode().equals(successCode)) {
            sysVisLog.setMessage(VisLogTypeEnum.LOGIN.getMessage() + LogSuccessStatusEnum.SUCCESS.getMessage());
        }
        if (LogSuccessStatusEnum.FAIL.getCode().equals(successCode)) {
            sysVisLog.setMessage(VisLogTypeEnum.LOGIN.getMessage() +
                    LogSuccessStatusEnum.FAIL.getMessage() + SymbolConstant.COLON + failMessage);
        }
    }

    /**
     * 创建登出日志
     *
     * @author xuyuxiang
     * @date 2020/3/12 16:09
     */
    static void createSysExitLog(SysVisLog sysVisLog, String account) {
        sysVisLog.setName(VisLogTypeEnum.EXIT.getMessage());
        sysVisLog.setSuccess(LogSuccessStatusEnum.SUCCESS.getCode());
        sysVisLog.setMessage(VisLogTypeEnum.EXIT.getMessage() + LogSuccessStatusEnum.SUCCESS.getMessage());
        sysVisLog.setVisType(VisLogTypeEnum.EXIT.getCode());
        sysVisLog.setVisTime(DateTime.now());
        sysVisLog.setAccount(account);
    }

    /**
     * 创建操作日志
     *
     * @author xuyuxiang
     * @date 2020/3/12 16:09
     */
    static void createSysOperationLog(SysOpLog sysOpLog, String account, BusinessLog businessLog, JoinPoint joinPoint, String result) {
        fillCommonSysOpLog(sysOpLog, account, businessLog, joinPoint);
        sysOpLog.setSuccess(LogSuccessStatusEnum.SUCCESS.getCode());
        sysOpLog.setResult(result);
        sysOpLog.setMessage(LogSuccessStatusEnum.SUCCESS.getMessage());
    }

    /**
     * 创建异常日志
     *
     * @author xuyuxiang
     * @date 2020/3/16 17:18
     */
    static void createSysExceptionLog(SysOpLog sysOpLog, String account, BusinessLog businessLog, JoinPoint joinPoint, Exception exception) {
        fillCommonSysOpLog(sysOpLog, account, businessLog, joinPoint);
        sysOpLog.setSuccess(LogSuccessStatusEnum.FAIL.getCode());
        sysOpLog.setMessage(Arrays.toString(exception.getStackTrace()));
    }

    /**
     * 生成通用操作日志字段
     *
     * @author xuyuxiang
     * @date 2020/3/16 17:20
     */
    private static void fillCommonSysOpLog(SysOpLog sysOpLog, String account, BusinessLog businessLog, JoinPoint joinPoint) {
        String className = joinPoint.getTarget().getClass().getName();

        String methodName = joinPoint.getSignature().getName();

        String param = JoinPointUtil.getArgsJsonString(joinPoint);

        sysOpLog.setName(businessLog.title());
        sysOpLog.setOpType(businessLog.opType().ordinal());
        sysOpLog.setClassName(className);
        sysOpLog.setMethodName(methodName);
        sysOpLog.setParam(param);
        sysOpLog.setOpTime(DateTime.now());
        sysOpLog.setAccount(account);
    }

    /**
     * 构建基础访问日志
     *
     * @author xuyuxiang
     * @date 2020/3/19 14:36
     */
    public static SysVisLog genBaseSysVisLog(String ip, String location, String browser, String os) {
        SysVisLog sysVisLog = new SysVisLog();
        sysVisLog.setIp(ip);
        sysVisLog.setLocation(location);
        sysVisLog.setBrowser(browser);
        sysVisLog.setOs(os);
        return sysVisLog;
    }

    /**
     * 构建基础操作日志
     *
     * @author xuyuxiang
     * @date 2020/3/19 14:36
     */
    public static SysOpLog genBaseSysOpLog(String ip, String location, String browser, String os, String url, String method) {
        SysOpLog sysOpLog = new SysOpLog();
        sysOpLog.setIp(ip);
        sysOpLog.setLocation(location);
        sysOpLog.setBrowser(browser);
        sysOpLog.setOs(os);
        sysOpLog.setUrl(url);
        sysOpLog.setReqMethod(method);
        return sysOpLog;
    }

}
