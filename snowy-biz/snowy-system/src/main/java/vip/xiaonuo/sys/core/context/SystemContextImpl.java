package vip.xiaonuo.sys.core.context;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.log.Log;
import vip.xiaonuo.api.auth.entity.SysUser;
import vip.xiaonuo.api.auth.service.AuthService;
import vip.xiaonuo.common.context.system.SystemContext;
import vip.xiaonuo.common.pojo.base.validate.UniqueValidateParam;
import vip.xiaonuo.common.pojo.login.SysLoginUser;
import vip.xiaonuo.sys.modular.dict.service.SysDictDataService;
import vip.xiaonuo.sys.modular.role.entity.SysRole;
import vip.xiaonuo.sys.modular.role.param.SysRoleParam;
import vip.xiaonuo.sys.modular.role.service.SysRoleService;
import vip.xiaonuo.api.auth.param.SysUserParam;
import vip.xiaonuo.sys.modular.user.service.SysUserService;
import com.baomidou.mybatisplus.extension.toolkit.SqlRunner;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * 系统相关上下文接口实现类
 *
 * @author xuyuxiang
 * @date 2020/5/6 14:59
 */
@Component
public class SystemContextImpl implements SystemContext {

    Log log = Log.get();

    @Resource
    private AuthService authService;

    @Resource
    private SysUserService sysUserService;

    @Resource
    private SysRoleService sysRoleService;

    @Resource
    private SysDictDataService sysDictDataService;

    @Override
    public String getNameByUserId(Long userId) {
        return sysUserService.getNameByUserId(userId);
    }

    @Override
    public String getNameByRoleId(Long roleId) {
        return sysRoleService.getNameByRoleId(roleId);
    }

    @Override
    public SysLoginUser getLoginUserByToken(String token) {
        return authService.getLoginUserByToken(token);
    }

    @Override
    public List<Dict> listUser(String account) {
        SysUserParam sysUserParam = new SysUserParam();
        if (ObjectUtil.isNotEmpty(account)) {
            sysUserParam.setAccount(account);
        }
        return sysUserService.list(sysUserParam);
    }

    @Override
    public List<Dict> listRole(String name) {
        SysRoleParam sysRoleParam = new SysRoleParam();
        if (ObjectUtil.isNotEmpty(name)) {
            sysRoleParam.setName(name);
        }
        return sysRoleService.list(sysRoleParam);
    }

    @Override
    public boolean isUser(Long userOrRoleId) {
        SysUser sysUser = sysUserService.getById(userOrRoleId);
        return !ObjectUtil.isNull(sysUser);
    }

    @Override
    public boolean isRole(Long userOrRoleId) {
        SysRole sysRole = sysRoleService.getById(userOrRoleId);
        return !ObjectUtil.isNull(sysRole);
    }

    @Override
    public List<String> getDictCodesByDictTypeCode(String... dictTypeCodes) {
        return sysDictDataService.getDictCodesByDictTypeCode(dictTypeCodes);
    }

    @Override
    public boolean tableUniValueFlag(UniqueValidateParam uniqueValidateParam) {
        int resultCount = 0;

        // 参数校验
        paramValidate(uniqueValidateParam);

        // 不排除当前记录，不排除逻辑删除的内容
        if (!uniqueValidateParam.getExcludeCurrentRecord()
                && !uniqueValidateParam.getExcludeLogicDeleteItems()) {
            resultCount = SqlRunner.db().selectCount(
                    "select count(*) from " + uniqueValidateParam.getTableName() + " where " + uniqueValidateParam.getColumnName() + " = {0}",
                    uniqueValidateParam.getValue());
        }

        // 不排除当前记录，排除逻辑删除的内容
        if (!uniqueValidateParam.getExcludeCurrentRecord()
                && uniqueValidateParam.getExcludeLogicDeleteItems()) {
            resultCount = SqlRunner.db().selectCount(
                    "select count(*) from " + uniqueValidateParam.getTableName()
                            + " where " + uniqueValidateParam.getColumnName() + " = {0} "
                            + " and "
                            + "(" + uniqueValidateParam.getLogicDeleteFieldName() + " is null || "
                            + uniqueValidateParam.getLogicDeleteFieldName() + " <> " + uniqueValidateParam.getLogicDeleteValue() + ")",
                    uniqueValidateParam.getValue());
        }

        // 排除当前记录，不排除逻辑删除的内容
        if (uniqueValidateParam.getExcludeCurrentRecord()
                && !uniqueValidateParam.getExcludeLogicDeleteItems()) {

            // id判空
            paramIdValidate(uniqueValidateParam);

            resultCount = SqlRunner.db().selectCount(
                    "select count(*) from " + uniqueValidateParam.getTableName()
                            + " where " + uniqueValidateParam.getColumnName() + " = {0} "
                            + " and id <> {1}",
                    uniqueValidateParam.getValue(), uniqueValidateParam.getId());
        }

        // 排除当前记录，排除逻辑删除的内容
        if (uniqueValidateParam.getExcludeCurrentRecord()
                && uniqueValidateParam.getExcludeLogicDeleteItems()) {

            // id判空
            paramIdValidate(uniqueValidateParam);

            resultCount = SqlRunner.db().selectCount(
                    "select count(*) from " + uniqueValidateParam.getTableName()
                            + " where " + uniqueValidateParam.getColumnName() + " = {0} "
                            + " and id <> {1} "
                            + " and "
                            + "(" + uniqueValidateParam.getLogicDeleteFieldName() + " is null || "
                            + uniqueValidateParam.getLogicDeleteFieldName() + " <> " + uniqueValidateParam.getLogicDeleteValue() + ")",
                    uniqueValidateParam.getValue(), uniqueValidateParam.getId());
        }

        // 如果大于0，代表不是唯一的当前校验的值
        return resultCount <= 0;
    }

    @Override
    public List<Long> getAllUserIdList() {
        return sysUserService.getAllUserIdList();
    }

    /**
     * 几个参数的为空校验
     *
     * @author xuyuxiang
     * @date 2020/8/17 22:00
     */
    private void paramValidate(UniqueValidateParam uniqueValidateParam) {
        if (StrUtil.isBlank(uniqueValidateParam.getTableName())) {
            throw new IllegalArgumentException("当前table字段值唯一性校验失败，tableName为空");
        }
        if (StrUtil.isBlank(uniqueValidateParam.getColumnName())) {
            throw new IllegalArgumentException("当前table字段值唯一性校验失败，columnName为空");
        }
        if (StrUtil.isBlank(uniqueValidateParam.getValue())) {
            throw new IllegalArgumentException("当前table字段值唯一性校验失败，字段值value为空");
        }
    }

    /**
     * id参数的为空校验
     *
     * @author xuyuxiang
     * @date 2020/8/17 22:00
     */
    private void paramIdValidate(UniqueValidateParam uniqueValidateParam) {
        if (uniqueValidateParam.getId() == null) {
            throw new IllegalArgumentException("当前table字段值唯一性校验失败，id为空");
        }
    }

}
