package vip.xiaonuo.common.pojo.base.param;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 通用基础参数，相关实体参数校验可继承此类
 *
 * @author xuyuxiang
 * @date 2020/3/10 16:02
 */
@Data
public class BaseParam implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 搜索值
     */
    private String searchValue;

    /**
     * 数据权限
     */
    private List<Long> dataScope;

    /**
     * 开始时间
     */
    private String searchBeginTime;

    /**
     * 结束时间
     */
    private String searchEndTime;

    /**
     * 状态（字典 0正常 1停用 2删除）
     */
    private Integer searchStatus;

    /**
     * 参数校验分组：分页
     */
    public @interface page {
    }

    /**
     * 参数校验分组：列表
     */
    public @interface list {
    }

    /**
     * 参数校验分组：下拉
     */
    public @interface dropDown {
    }

    /**
     * 参数校验分组：增加
     */
    public @interface add {
    }

    /**
     * 参数校验分组：编辑
     */
    public @interface edit {
    }

    /**
     * 参数校验分组：更新信息
     */
    public @interface updateInfo {
    }

    /**
     * 参数校验分组：修改密码
     */
    public @interface updatePwd {
    }

    /**
     * 参数校验分组：重置密码
     */
    public @interface resetPwd {
    }

    /**
     * 参数校验分组：修改头像
     */
    public @interface updateAvatar {
    }

    /**
     * 参数校验分组：删除
     */
    public @interface delete {
    }

    /**
     * 参数校验分组：详情
     */
    public @interface detail {
    }

    /**
     * 参数校验分组：授权角色
     */
    public @interface grantRole {
    }

    /**
     * 参数校验分组：授权菜单
     */
    public @interface grantMenu {
    }

    /**
     * 参数校验分组：授权数据
     */
    public @interface grantData {
    }

    /**
     * 参数校验分组：强退
     */
    public @interface force {
    }

    /**
     * 参数校验分组：停用
     */
    public @interface stop {
    }

    /**
     * 参数校验分组：启用
     */
    public @interface start {
    }

    /**
     * 参数校验分组：部署
     */
    public @interface deploy {
    }

    /**
     * 参数校验分组：挂起
     */
    public @interface suspend {
    }

    /**
     * 参数校验分组：激活
     */
    public @interface active {
    }

    /**
     * 参数校验分组：委托
     */
    public @interface entrust {
    }

    /**
     * 参数校验分组：转办
     */
    public @interface turn {
    }

    /**
     * 参数校验分组：追踪
     */
    public @interface trace {
    }

    /**
     * 参数校验分组：跳转
     */
    public @interface jump {
    }

    /**
     * 参数校验分组：提交
     */
    public @interface submit {
    }

    /**
     * 参数校验分组：退回
     */
    public @interface back {
    }

    /**
     * 参数校验分组：终止
     */
    public @interface end {
    }

    /**
     * 参数校验分组：导出
     */
    public @interface export {
    }

    /**
     * 参数校验分组：映射
     */
    public @interface mapping {
    }

    /**
     * 参数校验分组：切换
     */
    public @interface change {
    }

    /**
     * 参数校验分组：历史审批记录
     */
    public @interface commentHistory {
    }

    /**
     * 参数校验分组：修改状态
     */
    public @interface changeStatus {
    }

    /**
     * 参数校验分组：加签
     */
    public @interface addSign {
    }

    /**
     * 参数校验分组：减签
     */
    public @interface deleteSign {
    }

}
