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
package vip.xiaonuo.common.consts;

/**
 * @author : dongxiayu
 * @classname : AppConstant
 * @description : 应用静态常量
 * @date 2022/10/19 2:47
 */
public class AppConstant {

    /**
     * 团队名称
     */
    public static final String TEAM_NAME = "xiaonuo tech team";

    /**
     * 团队邮箱
     */
    public static final String TEAM_EMAIL = "administrator@xiaonuo.vip";

    /** 应用启动信息常量 **/
    public static final String APP_START_INFO =
            "\n"+
            "==============================================================\n"+
            "\tApp:\t{}\n"+
            "\tState:\tapp is running\n"+
            "\tPID:\t{}\n"+
            "\tDate:\tstarted at {}\n"+
            "\tAuth:\t"+TEAM_NAME+"\n"+
            "\tEmail:\t"+TEAM_EMAIL+"\n"+
            "\tURLs:\thttp://{}:{}{}\n"+
            "==============================================================";

    /**
     * TraceId Key
     */
    public static final String TRACE_ID_STRING = "traceId";

}
