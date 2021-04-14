package vip.xiaonuo.sys.modular.menu.entity;

import vip.xiaonuo.common.pojo.base.entity.BaseEntity;
import vip.xiaonuo.common.pojo.base.node.BaseTreeNode;
import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 系统菜单表
 *
 * @author xuyuxiang
 * @date 2020/3/11 11:20
 */
@EqualsAndHashCode(callSuper = true)
@Data
@TableName("sys_menu")
public class SysMenu extends BaseEntity implements BaseTreeNode {

    /**
     * 主键
     */
    @TableId(type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 父id
     */
    private Long pid;

    /**
     * 父ids
     */
    private String pids;

    /**
     * 名称
     */
    private String name;

    /**
     * 编码
     */
    private String code;

    /**
     * 菜单类型（字典 0目录 1菜单 2按钮）
     */
    private Integer type;

    /**
     * 图标
     */
    private String icon;

    /**
     * 路由地址
     */
    private String router;

    /**
     * 组件地址
     */
    private String component;

    /**
     * 权限标识
     */
    private String permission;

    /**
     * 应用分类（应用编码）
     */
    private String application;

    /**
     * 打开方式（字典 0无 1组件 2内链 3外链）
     */
    private Integer openType;

    /**
     * 是否可见（Y-是，N-否）
     */
    private String visible;

    /**
     * 内链地址
     */
    private String link;

    /**
     * 重定向地址
     */
    private String redirect;

    /**
     * 权重（字典 1系统权重 2业务权重）
     */
    private Integer weight;

    /**
     * 排序
     */
    private Integer sort;

    /**
     * 备注
     */
    @TableField(insertStrategy = FieldStrategy.IGNORED, updateStrategy = FieldStrategy.IGNORED)
    private String remark;

    /**
     * 状态（字典 0正常 1停用 2删除）
     */
    private Integer status;

    /**
     * 子节点（表中不存在，用于构造树）
     */
    @TableField(exist = false)
    private List children;

    @Override
    public void setChildren(List children) {
        this.children = children;
    }
}
