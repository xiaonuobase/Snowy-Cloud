package com.cn.xiaonuo.main.modular.providor;

import cn.hutool.core.lang.Dict;
import com.cn.xiaonuo.api.auth.entity.SysUser;
import com.cn.xiaonuo.api.auth.param.SysUserParam;
import com.cn.xiaonuo.api.auth.result.SysUserResult;
import com.cn.xiaonuo.api.auth.service.SysUserServiceApi;
import com.cn.xiaonuo.common.pojo.page.PageResult;
import com.cn.xiaonuo.sys.modular.user.service.SysUserService;
import me.zhyd.oauth.model.AuthUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 系统用户 服务API
 *
 * @author dongxiayu
 * @date 2021/3/29 1:02
 */
@RestController
public class SysUserServiceApiProvidor implements SysUserServiceApi {

    @Autowired
    private SysUserService sysUserService;


    /**
     * 根据账号获取用户
     *
     * @param account 账号
     * @return 用户
     * @author xuyuxiang
     * @date 2020/3/11 17:51
     */
    @Override
    public SysUser getUserByCount(String account) {
        return sysUserService.getUserByCount(account);
    }

    /**
     * 查询系统用户
     *
     * @param sysUserParam 查询参数
     * @return 查询分页结果
     * @author xuyuxiang
     * @date 2020/3/23 9:23
     */
    @Override
    public PageResult<SysUserResult> page(SysUserParam sysUserParam) {
        return null;
    }

    /**
     * 根据用户账号模糊搜索系统用户列表
     *
     * @param sysUserParam 查询参数
     * @return 增强版hashMap 格式：[{"id:":123, "firstName":"张三"}]
     * @author xuyuxiang
     * @date 2020/4/14 9:21
     */
    @Override
    public List<Dict> list(SysUserParam sysUserParam) {
        return null;
    }

    /**
     * 增加系统用户
     *
     * @param sysUserParam 添加参数
     * @author xuyuxiang
     * @date 2020/3/23 9:26
     */
    @Override
    public void add(SysUserParam sysUserParam) {
    }

    /**
     * 删除系统用户
     *
     * @param sysUserParam 删除参数
     * @author xuyuxiang
     * @date 2020/3/23 9:26
     */
    @Override
    public void delete(SysUserParam sysUserParam) {
    }

    /**
     * 编辑系统用户
     *
     * @param sysUserParam 编辑参数
     * @author xuyuxiang
     * @date 2020/3/23 9:26
     */
    @Override
    public void edit(SysUserParam sysUserParam) {
    }

    /**
     * 查看系统用户
     *
     * @param sysUserParam 查看参数
     * @return 用户结果集
     * @author xuyuxiang
     * @date 2020/3/26 9:52
     */
    @Override
    public SysUserResult detail(SysUserParam sysUserParam) {
        return null;
    }

    /**
     * 修改状态
     *
     * @param sysUserParam 修改参数
     * @author xuyuxiang
     * @date 2020/5/25 14:34
     */
    @Override
    public void changeStatus(SysUserParam sysUserParam) {
    }

    /**
     * 授权角色
     *
     * @param sysUserParam 授权参数
     * @author xuyuxiang
     * @date 2020/3/28 16:54
     */
    @Override
    public void grantRole(SysUserParam sysUserParam) {
    }

    /**
     * 授权数据
     *
     * @param sysUserParam 授权参数
     * @author xuyuxiang
     * @date 2020/3/28 16:54
     */
    @Override
    public void grantData(SysUserParam sysUserParam) {
    }

    /**
     * 更新信息
     *
     * @param sysUserParam 更新参数
     * @author xuyuxiang
     * @date 2020/4/1 14:43
     */
    @Override
    public void updateInfo(SysUserParam sysUserParam) {
    }

    /**
     * 修改密码
     *
     * @param sysUserParam 修改密码参数
     * @author xuyuxiang
     * @date 2020/4/1 14:44
     */
    @Override
    public void updatePwd(SysUserParam sysUserParam) {
    }

    /**
     * 获取用户的数据范围（组织机构id集合）
     *
     * @param userId 用户id
     * @param orgId  组织机构id
     * @return 数据范围id集合（组织机构id集合）
     * @author xuyuxiang
     * @date 2020/4/5 17:23
     */
    @Override
    public List<Long> getUserDataScopeIdList(Long userId, Long orgId) {
        return null;
    }

    /**
     * 根据用户id获取姓名
     *
     * @param userId 用户id
     * @return 用户姓名
     * @author xuyuxiang
     * @date 2020/5/6 15:02
     */
    @Override
    public String getNameByUserId(Long userId) {
        return sysUserService.getNameByUserId(userId);
    }

    /**
     * 拥有角色
     *
     * @param sysUserParam 查询参数
     * @return 角色id集合
     * @author xuyuxiang
     * @date 2020/5/29 14:10
     */
    @Override
    public List<Long> ownRole(SysUserParam sysUserParam) {
        return sysUserService.ownRole(sysUserParam);
    }

    /**
     * 拥有数据
     *
     * @param sysUserParam 查询参数
     * @return 数据范围id集合
     * @author xuyuxiang
     * @date 2020/5/29 14:10
     */
    @Override
    public List<Long> ownData(SysUserParam sysUserParam) {
        return sysUserService.ownData(sysUserParam);
    }

    /**
     * 重置密码
     *
     * @param sysUserParam 重置参数
     * @author xuyuxiang
     * @date 2020/5/29 14:57
     */
    @Override
    public void resetPwd(SysUserParam sysUserParam) {
    }

    /**
     * 修改头像
     *
     * @param sysUserParam 修改头像参数
     * @author xuyuxiang
     * @date 2020/6/28 15:21
     */
    @Override
    public void updateAvatar(SysUserParam sysUserParam) {
    }

    /**
     * 导出用户
     *
     * @param sysUserParam 导出参数
     * @author xuyuxiang
     * @date 2020/6/30 16:08
     */
    @Override
    public void export(SysUserParam sysUserParam) {
    }

    /**
     * 用户选择器
     *
     * @param sysUserParam 查询参数
     * @return 用户列表集合，格式[{"id":123,"name":"张三"},{"id":456,"name":"李四"}]
     * @author xuyuxiang
     * @date 2020/7/3 13:17
     */
    @Override
    public List<Dict> selector(SysUserParam sysUserParam) {
        return sysUserService.selector(sysUserParam);
    }

    /**
     * 根据用户id获取用户
     *
     * @param userId 用户id
     * @return 用户实体
     * @author xuyuxiang
     * @date 2020/7/29 10:07
     **/
    @Override
    public SysUser getUserById(Long userId) {
        return sysUserService.getUserById(userId);
    }

    /**
     * 将授权的用户信息保存到用户表
     *
     * @param authUser 授权的用户信息
     * @param sysUser  用户表信息
     * @return void
     * @author xuyuxiang
     * @date 2020/7/29 10:26
     **/
    @Override
    public void saveAuthUserToUser(AuthUser authUser, SysUser sysUser) {
    }

    /**
     * 获取用户id集合
     *
     * @return 用户id集合
     * @author xuyuxiang
     * @date 2020/9/11 17:54
     **/
    @Override
    public List<Long> getAllUserIdList() {
        return sysUserService.getAllUserIdList();
    }
}