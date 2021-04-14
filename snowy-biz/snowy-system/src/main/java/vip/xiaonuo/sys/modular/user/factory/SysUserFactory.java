package vip.xiaonuo.sys.modular.user.factory;

import cn.hutool.core.util.ObjectUtil;
import vip.xiaonuo.api.auth.entity.SysUser;
import vip.xiaonuo.common.context.constant.ConstantContextHolder;
import vip.xiaonuo.common.enums.CommonStatusEnum;
import vip.xiaonuo.sys.core.enums.AdminTypeEnum;
import vip.xiaonuo.sys.core.enums.SexEnum;
import org.springframework.security.crypto.bcrypt.BCrypt;

/**
 * 填充用户附加信息工厂
 *
 * @author xuyuxiang
 * @date 2020/3/23 16:40
 */
public class SysUserFactory {

    /**
     * 管理员类型（1超级管理员 2非管理员）
     * 新增普通用户时填充相关信息
     *
     * @author xuyuxiang
     * @date 2020/3/23 16:41
     */
    public static void fillAddCommonUserInfo(SysUser sysUser) {
        fillBaseUserInfo(sysUser);
        sysUser.setAdminType(AdminTypeEnum.NONE.getCode());
    }

    /**
     * 填充用户基本字段
     *
     * @author xuyuxiang
     * @date 2020/3/23 16:50
     */
    public static void fillBaseUserInfo(SysUser sysUser) {
        //密码为空则设置密码
        if (ObjectUtil.isEmpty(sysUser.getPassword())) {
            //没有密码则设置默认密码
            String password = ConstantContextHolder.getDefaultPassWord();
            //设置密码为Md5加密后的密码
            sysUser.setPassword(BCrypt.hashpw(password, BCrypt.gensalt()));
        }

        if (ObjectUtil.isEmpty(sysUser.getAvatar())) {
            sysUser.setAvatar(null);
        }

        if (ObjectUtil.isEmpty(sysUser.getSex())) {
            sysUser.setSex(SexEnum.NONE.getCode());
        }

        sysUser.setStatus(CommonStatusEnum.ENABLE.getCode());
    }
}
