package vip.xiaonuo.common.pojo.node;

import vip.xiaonuo.common.pojo.base.node.BaseTreeNode;
import lombok.Data;

import java.util.List;

/**
 * antd通用前端树节点
 *
 * @author yubaoshan
 * @date 2020/6/9 12:42
 */
@Data
public class AntdBaseTreeNode implements BaseTreeNode {

    /**
     * 主键
     */
    private Long id;

    /**
     * 父id
     */
    private Long parentId;

    /**
     * 名称
     */
    private String title;

    /**
     * 值
     */
    private String value;

    /**
     * 排序，越小优先级越高
     */
    private Integer weight;

    /**
     * 子节点
     */
    private List children;

    /**
     * 父id别名
     */
    @Override
    public Long getPid() {
        return this.parentId;
    }

    /**
     * 子节点
     */
    @Override
    public void setChildren(List children) {
        this.children = children;
    }

}
