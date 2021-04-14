package vip.xiaonuo.common.pojo.login;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.ObjectUtil;
import vip.xiaonuo.common.consts.CommonConstant;
import vip.xiaonuo.common.pojo.node.LoginMenuTreeNode;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * 登录用户模型
 *
 * @author xuyuxiang
 * @date 2020/3/11 12:21
 */
@Data
@NoArgsConstructor
public class SysLoginUser implements UserDetails, Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    private Long id;

    /**
     * 账号
     */
    private String account;

    /**
     * 昵称
     */
    private String nickName;

    /**
     * 姓名
     */
    private String name;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 生日
     */
    private Date birthday;

    /**
     * 性别(字典 1男 2女)
     */
    private Integer sex;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 手机
     */
    private String phone;

    /**
     * 电话
     */
    private String tel;

    /**
     * 管理员类型（0超级管理员 1非管理员）
     */
    private Integer adminType;

    /**
     * 最后登陆IP
     */
    private String lastLoginIp;

    /**
     * 最后登陆时间
     */
    private String lastLoginTime;

    /**
     * 最后登陆地址
     */
    private String lastLoginAddress;

    /**
     * 最后登陆所用浏览器
     */
    private String lastLoginBrowser;

    /**
     * 最后登陆所用系统
     */
    private String lastLoginOs;

    /**
     * 员工信息
     */
    private LoginEmpInfo loginEmpInfo;

    /**
     * 具备应用信息
     */
    private List<Dict> apps;

    /**
     * 角色信息
     */
    private List<Dict> roles;

    /**
     * 权限信息
     */
    private List<String> permissions;

    /**
     * 登录菜单信息，AntDesign版本菜单
     */
    private List<LoginMenuTreeNode> menus;

    /**
     * 数据范围信息
     */
    private List<Long> dataScopes;

    /**
     * 租户信息
     */
    private Dict tenants;

    /**
     * 角色名称集合
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        ArrayList<SnowyAuthority> grantedAuthorities = CollectionUtil.newArrayList();
        if (ObjectUtil.isNotEmpty(roles)) {
            roles.forEach(dict -> {
                String roleName = dict.getStr(CommonConstant.NAME);
                SnowyAuthority snowyAuthority = new SnowyAuthority(roleName);
                grantedAuthorities.add(snowyAuthority);
            });
        }
        return grantedAuthorities;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return this.account;
    }

    @Override
    public boolean isAccountNonExpired() {
        //能生成loginUser就是jwt解析成功，没锁定
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        //能生成loginUser就是jwt解析成功，没锁定
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        //能生成loginUser就是jwt解析成功，没锁定
        return true;
    }

    @Override
    public boolean isEnabled() {
        //能生成loginUser就是jwt解析成功，没锁定
        return true;
    }
}
