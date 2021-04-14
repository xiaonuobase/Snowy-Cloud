package vip.xiaonuo.common.enums;

import lombok.Getter;

/**
 * 不同数据库类型的枚举
 * <p>
 * 用于标识mapping.xml中不同数据库的标识
 *
 * @author yubaoshan
 * @date 2020/6/20 21:08
 */
@Getter
public enum DbIdEnum {

    /**
     * mysql
     */
    MYSQL("mysql", "mysql"),

    /**
     * pgsql
     */
    PG_SQL("pgsql", "postgresql"),

    /**
     * oracle
     */
    ORACLE("oracle", "oracle"),

    /**
     * mssql
     */
    MS_SQL("mssql", "sqlserver");

    private final String code;

    private final String name;

    DbIdEnum(String code, String name) {
        this.code = code;
        this.name = name;
    }

}
