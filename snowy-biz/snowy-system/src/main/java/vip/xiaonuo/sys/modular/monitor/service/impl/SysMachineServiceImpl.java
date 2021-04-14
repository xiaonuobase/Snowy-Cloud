package vip.xiaonuo.sys.modular.monitor.service.impl;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.NumberUtil;
import cn.hutool.system.*;
import vip.xiaonuo.common.consts.SymbolConstant;
import vip.xiaonuo.sys.modular.monitor.result.SysMachineResult;
import vip.xiaonuo.sys.modular.monitor.service.SysMachineService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DecimalFormat;

/**
 * 系统属性监控service接口实现类
 *
 * @author xuyuxiang
 * @date 2020/6/5 14:40
 */
@Service
public class SysMachineServiceImpl implements SysMachineService {

    @Override
    public SysMachineResult query() {
        JvmInfo jvmInfo = SystemUtil.getJvmInfo();
        JavaRuntimeInfo javaRuntimeInfo = SystemUtil.getJavaRuntimeInfo();
        OsInfo osInfo = SystemUtil.getOsInfo();
        HostInfo hostInfo = SystemUtil.getHostInfo();
        RuntimeInfo runtimeInfo = SystemUtil.getRuntimeInfo();
        //系统属性结果集
        SysMachineResult sysMachineResult = new SysMachineResult();

        //系统信息
        SysMachineResult.SysOsInfo sysOsInfo = new SysMachineResult.SysOsInfo();
        sysOsInfo.setOsName(osInfo.getName());
        sysOsInfo.setOsArch(osInfo.getArch());
        sysOsInfo.setOsVersion(osInfo.getVersion());
        sysOsInfo.setOsHostName(hostInfo.getName());
        sysOsInfo.setOsHostAddress(hostInfo.getAddress());
        sysMachineResult.setSysOsInfo(sysOsInfo);

        //Java信息
        SysMachineResult.SysJavaInfo sysJavaInfo = new SysMachineResult.SysJavaInfo();
        sysJavaInfo.setJvmName(jvmInfo.getName());
        sysJavaInfo.setJvmVersion(jvmInfo.getVersion());
        sysJavaInfo.setJvmVendor(jvmInfo.getVendor());
        sysJavaInfo.setJavaName(javaRuntimeInfo.getName());
        sysJavaInfo.setJavaVersion(javaRuntimeInfo.getVersion());
        sysMachineResult.setSysJavaInfo(sysJavaInfo);

        //jvm内存信息
        SysMachineResult.SysJvmMemInfo sysJvmMemInfo = new SysMachineResult.SysJvmMemInfo();
        sysJvmMemInfo.setJvmMaxMemory(FileUtil.readableFileSize(runtimeInfo.getMaxMemory()));
        sysJvmMemInfo.setJvmUsableMemory(FileUtil.readableFileSize(runtimeInfo.getUsableMemory()));
        sysJvmMemInfo.setJvmTotalMemory(FileUtil.readableFileSize(runtimeInfo.getTotalMemory()));
        sysJvmMemInfo.setJvmFreeMemory(FileUtil.readableFileSize(runtimeInfo.getFreeMemory()));
        BigDecimal usedMemory = NumberUtil.sub(new BigDecimal(runtimeInfo.getTotalMemory()), new BigDecimal(runtimeInfo.getFreeMemory()));
        sysJvmMemInfo.setJvmUsedMemory(FileUtil.readableFileSize(usedMemory.longValue()));
        BigDecimal rate = NumberUtil.div(usedMemory, runtimeInfo.getTotalMemory());
        String usedRate = new DecimalFormat("#.00").format(NumberUtil.mul(rate, 100)) + SymbolConstant.PERCENT;
        sysJvmMemInfo.setJvmMemoryUsedRate(usedRate);
        sysMachineResult.setSysJvmMemInfo(sysJvmMemInfo);
        return sysMachineResult;
    }
}
