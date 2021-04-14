package vip.xiaonuo.common.enums;

import lombok.Getter;

/**
 * 日志注解操作类型枚举
 *
 * @author xuyuxiang
 * @date 2020/3/16 17:45
 */
@Getter
public enum LogAnnotionOpTypeEnum {

    /**
     * 其它
     */
    OTHER,

    /**
     * 增加
     */
    ADD,

    /**
     * 删除
     */
    DELETE,

    /**
     * 编辑
     */
    EDIT,

    /**
     * 更新
     */
    UPDATE,

    /**
     * 查询
     */
    QUERY,

    /**
     * 详情
     */
    DETAIL,

    /**
     * 树
     */
    TREE,

    /**
     * 导入
     */
    IMPORT,

    /**
     * 导出
     */
    EXPORT,

    /**
     * 授权
     */
    GRANT,

    /**
     * 强退
     */
    FORCE,

    /**
     * 清空
     */
    CLEAN,

    /**
     * 修改状态
     */
    CHANGE_STATUS
}
