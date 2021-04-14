package vip.xiaonuo.sys.modular.notice.mapper;

import vip.xiaonuo.sys.modular.notice.entity.SysNotice;
import vip.xiaonuo.sys.modular.notice.result.SysNoticeReceiveResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

/**
 * 系统通知公告mapper接口
 *
 * @author xuyuxiang
 * @date 2020/6/28 17:18
 */
public interface SysNoticeMapper extends BaseMapper<SysNotice> {

    /**
     * 查询当前登陆用户已收公告
     *
     * @param page         分页参数
     * @param queryWrapper 查询参数
     * @return 查询分页结果
     * @author xuyuxiang
     * @date 2020/6/29 14:32
     */
    Page<SysNoticeReceiveResult> page(@Param("page") Page page, @Param("ew") QueryWrapper queryWrapper);
}
