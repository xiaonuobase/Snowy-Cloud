package vip.xiaonuo.actuator.core.consts;

/**
 * @author : dongxiayu
 * @classname : AppConstant
 * @description : 应用静态常量
 * @date : 2020/7/8 15:52
 */
public class AppConstant {

    /**
     * 团队名称
     */
    public static final String TEAM_NAME = "XIAONUO-TEAM";

    /**
     * 团队邮箱
     */
    public static final String TEAM_EMAIL = "XIAONUO-TEAM@xiaonuo.cn";

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

}
