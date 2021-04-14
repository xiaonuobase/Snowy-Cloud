package vip.xiaonuo.sys.modular.user.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import vip.xiaonuo.api.auth.entity.SysUser;
import vip.xiaonuo.api.auth.result.SysUserResult;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

/**
 * 系统用户mapper接口
 *
 * @author xuyuxiang
 * @date 2020/3/12 9:48
 */
public interface SysUserMapper extends BaseMapper<SysUser> {

    /**
     * 获取用户分页列表
     *
     * @param page         分页参数
     * @param queryWrapper 查询参数
     * @return 查询分页结果
     * @author xuyuxiang
     * @date 2020/4/7 21:14
     */
    Page<SysUserResult> page(@Param("page") Page page, @Param("ew") QueryWrapper queryWrapper);
}
