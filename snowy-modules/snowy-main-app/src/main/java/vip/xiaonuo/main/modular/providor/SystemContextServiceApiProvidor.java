package vip.xiaonuo.main.modular.providor;

import cn.hutool.core.lang.Dict;
import vip.xiaonuo.api.context.service.SystemContextServiceApi;
import vip.xiaonuo.common.context.system.SystemContextHolder;
import vip.xiaonuo.common.pojo.base.validate.UniqueValidateParam;
import vip.xiaonuo.common.pojo.login.SysLoginUser;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author : dongxiayu
 * @classname : SystemContextServiceController
 * @description : SystemContext上下文服务接口
 * @date : 2021/3/28 17:49
 */
@RestController
public class SystemContextServiceApiProvidor implements SystemContextServiceApi {
    /**
     * 根据用户id获取姓名
     *
     * @param userId 用户id
     * @return 用户姓名
     * @author dongxiayu
     * @date 2021/3/28 14:57
     */
    @Override
    public String getNameByUserId(Long userId) {
        return SystemContextHolder.me().getNameByUserId(userId);
    }

    /**
     * 根据角色id获取角色名称
     *
     * @param roleId 角色id
     * @return 角色名称
     * @author dongxiayu
     * @date 2021/3/28 14:57
     */
    @Override
    public String getNameByRoleId(Long roleId) {
        return SystemContextHolder.me().getNameByRoleId(roleId);
    }

    /**
     * 根据token获取登录用户信息
     *
     * @param token token
     * @return 登录用户信息
     * @author dongxiayu
     * @date 2021/3/28 14:57
     */
    @Override
    public SysLoginUser getLoginUserByToken(String token) {
        return SystemContextHolder.me().getLoginUserByToken(token);
    }

    /**
     * 根据用户账号模糊搜索系统用户列表
     *
     * @param account 账号
     * @return 增强版hashMap，格式：[{"id:":123, "firstName":"张三"}]
     * @author dongxiayu
     * @date 2021/3/28 14:57
     */
    @Override
    public List<Dict> listUser(String account) {
        return SystemContextHolder.me().listUser(account);
    }

    /**
     * 根据角色名模糊搜索系统角色列表
     *
     * @param name 角色名
     * @return 增强版hashMap，格式：[{"id:":123, "name":"总经理"}]
     * @author dongxiayu
     * @date 2021/3/28 14:57
     */
    @Override
    public List<Dict> listRole(String name) {
        return SystemContextHolder.me().listUser(name);
    }

    /**
     * 根据id判断是否是用户
     *
     * @param userOrRoleId 用户或角色id
     * @return true是 false否
     * @author dongxiayu
     * @date 2021/3/28 14:57
     */
    @Override
    public boolean isUser(Long userOrRoleId) {
        return SystemContextHolder.me().isUser(userOrRoleId);
    }

    /**
     * 根据id判断是否是角色
     *
     * @param userOrRoleId 用户或角色id
     * @return true是 false否
     * @author dongxiayu
     * @date 2021/3/28 14:57
     */
    @Override
    public boolean isRole(Long userOrRoleId) {
        return SystemContextHolder.me().isRole(userOrRoleId);
    }

    /**
     * 根据字典类型获取字典的code值
     *
     * @param dictTypeCodes 字典类型编码值
     * @return 字典的code值
     * @author dongxiayu
     * @date 2021/3/28 14:57
     */
    @Override
    public List<String> getDictCodesByDictTypeCode(String... dictTypeCodes) {
        return SystemContextHolder.me().getDictCodesByDictTypeCode(dictTypeCodes);
    }

    /**
     * 校验某个表中，某一列是否存在重复的值
     * <p>
     * 一般用于唯一code校验
     *
     * @param uniqueValidateParam 被校验的参数
     * @return true-是唯一的值，false-不是唯一的
     * @author dongxiayu
     * @date 2021/3/28 14:57
     */
    @Override
    public boolean tableUniValueFlag(@RequestParam UniqueValidateParam uniqueValidateParam) {
        return SystemContextHolder.me().tableUniValueFlag(uniqueValidateParam);
    }

    /**
     * 获取系统用户id集合
     *
     * @return 用户id集合
     * @author dongxiayu
     * @date 2021/3/28 14:57
     **/
    @Override
    public List<Long> getAllUserIdList() {
        return SystemContextHolder.me().getAllUserIdList();
    }
}
