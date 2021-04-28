/*
Copyright [2020] [https://www.xiaonuo.vip]

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

Snowy采用APACHE LICENSE 2.0开源协议，您在使用过程中，需要注意以下几点：

1.请不要删除和修改根目录下的LICENSE文件。
2.请不要删除和修改Snowy源码头部的版权声明。
3.请保留源码和相关描述文件的项目出处，作者声明等。
4.分发源码时候，请注明软件出处 https://gitee.com/xiaonuobase/snowy-cloud
5.在修改包名，模块名称，项目代码等时，请注明软件出处 https://gitee.com/xiaonuobase/snowy-cloud
6.若您的项目无法满足以上几点，可申请商业授权，获取Snowy商业授权许可，请在官网购买授权，地址为 https://www.xiaonuo.vip
 */
package vip.xiaonuo.api.context.service;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vip.xiaonuo.common.consts.FeignConstant;
import vip.xiaonuo.common.pojo.login.SysLoginUser;
import java.util.List;

/**
 * LoginContext上下文服务接口
 *
 * @author dongxiayu
 * @date 2021/3/28 14:57
 */
@RequestMapping("/feign/loginContextServiceApi")
public interface LoginContextServiceApi {

    String APP_NAME = FeignConstant.MAIN_APP;

    /**
     * 获取当前登录用户
     *
     * @return 当前登录用户信息
     * @author dongxiayu
     * @date 2021/3/28 14:57
     */
    @RequestMapping("/getSysLoginUser")
    SysLoginUser getSysLoginUser();

    /**
     * 获取当前登录用户，如未登录，则返回null，不抛异常
     *
     * @return 当前登录用户信息
     * @author dongxiayu
     * @date 2021/3/28 14:57
     */
    @RequestMapping("/getSysLoginUserWithoutException")
    SysLoginUser getSysLoginUserWithoutException();

    /**
     * 获取当前登录用户的id
     *
     * @return 当前登录用户的id
     * @author dongxiayu
     * @date 2021/3/28 14:57
     */
    @RequestMapping("/getSysLoginUserId")
    Long getSysLoginUserId();

    /**
     * 判断用户是否登录
     *
     * @return 是否登录，true是，false否
     * @author dongxiayu
     * @date 2021/3/28 14:57
     */
    @RequestMapping("/hasLogin")
    boolean hasLogin();

    /**
     * 获取当前登录用户的账户
     *
     * @return 当前登陆用户的账户account
     * @author dongxiayu
     * @date 2021/3/28 14:57
     */
    @RequestMapping("/getSysLoginUserAccount")
    String getSysLoginUserAccount();

    /**
     * 判断当前登录用户是否有某资源的访问权限
     *
     * @param requestUri 请求的url
     * @return 是否有访问权限，true是，false否
     * @author dongxiayu
     * @date 2021/3/28 14:57
     */
    @RequestMapping("/hasPermission")
    boolean hasPermission(@RequestParam String requestUri);

    /**
     * 判断当前登录用户是否包含某个角色
     *
     * @param roleCode 角色编码
     * @return 是否包含该角色，true是，false否
     * @author dongxiayu
     * @date 2021/3/28 14:57
     */
    @RequestMapping("/hasRole")
    boolean hasRole(@RequestParam String roleCode);

    /**
     * 判断当前登录用户是否包含任意一个角色
     *
     * @param roleCodes 角色集合，逗号拼接
     * @return 是否包含任一角色，true是，false否
     * @author dongxiayu
     * @date 2021/3/28 14:57
     */
    @RequestMapping("/hasAnyRole")
    boolean hasAnyRole(@RequestParam String roleCodes);

    /**
     * 判断当前登录用户是否是超级管理员
     *
     * @return 当前登录用户是否是超级管理员
     * @author dongxiayu
     * @date 2021/3/28 14:57
     */
    @RequestMapping("/isSuperAdmin")
    boolean isSuperAdmin();

    /**
     * 判断当前登录用户是否包含所有角色
     *
     * @param roleCodes 角色集合，逗号拼接
     * @return 是否包含所有角色，true是，false否
     * @author dongxiayu
     * @date 2021/3/28 14:57
     */
    @RequestMapping("/hasAllRole")
    boolean hasAllRole(@RequestParam String roleCodes);

    /**
     * 获取当前登录用户的数据范围集合（组织机构id集合）
     *
     * @return 数据范围集合（组织机构id集合）
     * @author dongxiayu
     * @date 2021/3/28 14:57
     */
    @RequestMapping("/getLoginUserDataScopeIdList")
    List<Long> getLoginUserDataScopeIdList();

    /**
     * 获取当前登录用户的组织机构id
     *
     * @return 当前登录用户的组织机构id
     * @author dongxiayu
     * @date 2021/3/28 14:57
     */
    @RequestMapping("/getSysLoginUserOrgId")
    Long getSysLoginUserOrgId();

    /**
     * 获取当前登录用户的角色id集合
     *
     * @return 当前登录用户角色id集合
     * @author dongxiayu
     * @date 2021/3/28 14:57
     */
    @RequestMapping("/getLoginUserRoleIds")
    List<String> getLoginUserRoleIds();

    /**
     * 获取最新的用户信息，用于修改之后前端获取
     *
     * @return 最新的用户信息
     * @author dongxiayu
     * @date 2021/3/28 14:57
     **/
    @RequestMapping("/getSysLoginUserUpToDate")
    SysLoginUser getSysLoginUserUpToDate();
}
