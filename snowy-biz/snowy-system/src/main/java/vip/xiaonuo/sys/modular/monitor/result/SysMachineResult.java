package vip.xiaonuo.sys.modular.monitor.result;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 系统属性结果集
 *
 * @author xuyuxiang
 * @date 2020/6/5 15:02
 */
@Data
public class SysMachineResult implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 系统信息
     */
    private SysOsInfo sysOsInfo;

    /**
     * Java信息
     */
    private SysJavaInfo sysJavaInfo;

    /**
     * JVM内存信息
     */
    private SysJvmMemInfo sysJvmMemInfo;

    /**
     * 系统信息内部类
     *
     * @author xuyuxiang
     * @date 2020/6/5 15:19
     */
    @NoArgsConstructor
    @Data
    public static class SysOsInfo {

        /**
         * 系统名称
         */
        private String osName;

        /**
         * 系统架构
         */
        private String osArch;

        /**
         * 系统版本
         */
        private String osVersion;

        /**
         * 主机名称
         */
        private String osHostName;

        /**
         * 主机ip地址
         */
        private String osHostAddress;

    }

    /**
     * JVM信息内部类
     *
     * @author xuyuxiang
     * @date 2020/6/5 15:19
     */
    @NoArgsConstructor
    @Data
    public static class SysJavaInfo {

        /**
         * 虚拟机名称
         */
        private String jvmName;

        /**
         * 虚拟机版本
         */
        private String jvmVersion;

        /**
         * 虚拟机供应商
         */
        private String jvmVendor;

        /**
         * java名称
         */
        private String javaName;

        /**
         * java版本
         */
        private String javaVersion;

    }

    /**
     * JVM内存信息
     *
     * @author xuyuxiang
     * @date 2020/6/5 15:19
     */
    @NoArgsConstructor
    @Data
    public static class SysJvmMemInfo {

        /**
         * 最大内存
         */
        private String jvmMaxMemory;

        /**
         * 可用内存
         */
        private String jvmUsableMemory;

        /**
         * 总内存
         */
        private String jvmTotalMemory;

        /**
         * 已使用内存
         */
        private String jvmUsedMemory;

        /**
         * 空余内存
         */
        private String jvmFreeMemory;

        /**
         * 使用率
         */
        private String jvmMemoryUsedRate;
    }
}
