package vip.xiaonuo.common.pojo.page;

import cn.hutool.core.convert.Convert;
import cn.hutool.core.util.PageUtil;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 分页结果集
 *
 * @author xuyuxiang
 * @date 2020/3/30 15:44
 */
@Data
public class PageResult<T> implements Serializable {

    private static final long serialVersionUID = -1L;

    /**
     * 默认分页彩虹展示数量
     */
    public static final int RAINBOW_NUM = 5;

    /**
     * 第几页
     */
    private Integer pageNo = 1;

    /**
     * 每页条数
     */
    private Integer pageSize = 20;

    /**
     * 总页数
     */
    private Integer totalPage = 0;

    /**
     * 总记录数
     */
    private Integer totalRows = 0;

    /**
     * 结果集
     */
    private List<T> rows;

    /**
     * 分页彩虹
     */
    private int[] rainbow;

    public PageResult() {
    }

    /**
     * 将mybatis-plus的page转成自定义的PageResult，扩展了totalPage总页数，和rainBow彩虹条
     *
     * @author xuyuxiang
     * @date 2020/4/8 19:20
     */
    public PageResult(Page<T> page) {
        this.setRows(page.getRecords());
        this.setTotalRows(Convert.toInt(page.getTotal()));
        this.setPageNo(Convert.toInt(page.getCurrent()));
        this.setPageSize(Convert.toInt(page.getSize()));
        this.setTotalPage(PageUtil.totalPage(Convert.toInt(page.getTotal()),
                Convert.toInt(page.getSize())));
        this.setRainbow(PageUtil.rainbow(Convert.toInt(page.getCurrent()),
                Convert.toInt(this.getTotalPage()), RAINBOW_NUM));
    }

    /**
     * 将mybatis-plus的page转成自定义的PageResult，扩展了totalPage总页数，和rainBow彩虹条
     * 可单独设置rows
     *
     * @author xuyuxiang
     * @date 2020/4/14 20:55
     */
    public PageResult(Page<T> page, List<T> t) {
        this.setRows(t);
        this.setTotalRows(Convert.toInt(page.getTotal()));
        this.setPageNo(Convert.toInt(page.getCurrent()));
        this.setPageSize(Convert.toInt(page.getSize()));
        this.setTotalPage(PageUtil.totalPage(Convert.toInt(page.getTotal()),
                Convert.toInt(page.getSize())));
        this.setRainbow(PageUtil.rainbow(Convert.toInt(page.getCurrent()),
                Convert.toInt(this.getTotalPage()), RAINBOW_NUM));
    }
}
