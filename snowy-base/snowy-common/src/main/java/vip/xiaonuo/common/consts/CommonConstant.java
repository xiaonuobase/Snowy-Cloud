package vip.xiaonuo.common.consts;

/**
 * 通用常量
 *
 * @author xuyuxiang
 * @date 2020/3/11 16:51
 */
public interface CommonConstant {

    /**
     * id
     */
    String ID = "id";

    /**
     * 名称
     */
    String NAME = "name";

    /**
     * 编码
     */
    String CODE = "code";

    /**
     * 值
     */
    String VALUE = "value";

    /**
     * 默认标识状态的字段名称
     */
    String STATUS = "status";

    /**
     * 默认逻辑删除的状态值
     */
    String DEFAULT_LOGIC_DELETE_VALUE = "2";

    /**
     * 用户代理
     */
    String USER_AGENT = "User-Agent";

    /**
     * 请求头token表示
     */
    String AUTHORIZATION = "Authorization";

    /**
     * token名称
     */
    String TOKEN_NAME = "token";

    /**
     * token类型
     */
    String TOKEN_TYPE_BEARER = "Bearer";

    /**
     * 首页提示语
     */
    String INDEX_TIPS = "Welcome To Snowy";

    /**
     * 未知标识
     */
    String UNKNOWN = "Unknown";

    /**
     * 默认包名
     */
    String DEFAULT_PACKAGE_NAME = "vip.xiaonuo";

    /**
     * 默认密码
     */
    String DEFAULT_PASSWORD = "123456";

    /**
     * 默认JWT密码
     */
    String DEFAULT_JWT_PASSWORD = "xiaonuo";

    /**
     * 请求号在header中的唯一标识
     */
    String REQUEST_NO_HEADER_NAME = "Request-No";

    /**
     * 数据库链接URL标识
     */
    String DATABASE_URL_NAME = "DATABASE_URL_NAME";

    /**
     * 数据库链接驱动标识
     */
    String DATABASE_DRIVER_NAME = "DATABASE_DRIVER_NAME";

    /**
     * 数据库用户标识
     */
    String DATABASE_USER_NAME = "DATABASE_USER_NAME";

    /**
     * 团队名称
     */
    String TEAM_NAME = "XIAONUO-TEAM";

    /**
     * 团队邮箱
     */
    String TEAM_EMAIL = "XIAONUO-TEAM@xiaonuo.cn";

    /** 应用启动信息常量 **/
    String APP_START_INFO =
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
     * 点选验证码
     */
    String IMAGE_CODE_TYPE = "clickWord";

    /**
     * undefined未知
     */
    String UNDEFINED = "undefined";

}
