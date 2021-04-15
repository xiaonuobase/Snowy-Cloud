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
package vip.xiaonuo.api.auth.service;

import cn.hutool.core.lang.Dict;
import me.zhyd.oauth.model.AuthUser;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vip.xiaonuo.api.auth.entity.SysUser;
import vip.xiaonuo.api.auth.param.SysUserParam;
import vip.xiaonuo.api.auth.result.SysUserResult;
import vip.xiaonuo.common.consts.FeignConstant;
import vip.xiaonuo.common.pojo.page.PageResult;

import java.util.List;

@RequestMapping("/feign/sysUserServiceApi")
public interface SysUserServiceApi {

    String APP_NAME = FeignConstant.MAIN_APP;

    /**
     * 根据账号获取用户
     *
     * @param account 账号
     * @return 用户
     * @author xuyuxiang
     * @date 2020/3/11 17:51
     */
    @RequestMapping("/getUserByCount")
    SysUser getUserByCount(String account);

    /**
     * 查询系统用户
     *
     * @param sysUserParam 查询参数
     * @return 查询分页结果
     * @author xuyuxiang
     * @date 2020/3/23 9:23
     */
    @RequestMapping("/page")
    PageResult<SysUserResult> page(SysUserParam sysUserParam);

    /**
     * 根据用户账号模糊搜索系统用户列表
     *
     * @param sysUserParam 查询参数
     * @return 增强版hashMap 格式：[{"id:":123, "firstName":"张三"}]
     * @author xuyuxiang
     * @date 2020/4/14 9:21
     */
    @RequestMapping("/list")
    List<Dict> list(SysUserParam sysUserParam);

    /**
     * 增加系统用户
     *
     * @param sysUserParam 添加参数
     * @author xuyuxiang
     * @date 2020/3/23 9:26
     */
    @RequestMapping("/add")
    void add(SysUserParam sysUserParam);

    /**
     * 删除系统用户
     *
     * @param sysUserParam 删除参数
     * @author xuyuxiang
     * @date 2020/3/23 9:26
     */
    @RequestMapping("/delete")
    void delete(SysUserParam sysUserParam);

    /**
     * 编辑系统用户
     *
     * @param sysUserParam 编辑参数
     * @author xuyuxiang
     * @date 2020/3/23 9:26
     */
    @RequestMapping("/edit")
    void edit(SysUserParam sysUserParam);

    /**
     * 查看系统用户
     *
     * @param sysUserParam 查看参数
     * @return 用户结果集
     * @author xuyuxiang
     * @date 2020/3/26 9:52
     */
    @RequestMapping("/detail")
    SysUserResult detail(SysUserParam sysUserParam);

    /**
     * 修改状态
     *
     * @param sysUserParam 修改参数
     * @author xuyuxiang
     * @date 2020/5/25 14:34
     */
    @RequestMapping("/changeStatus")
    void changeStatus(SysUserParam sysUserParam);

    /**
     * 授权角色
     *
     * @param sysUserParam 授权参数
     * @author xuyuxiang
     * @date 2020/3/28 16:54
     */
    @RequestMapping("/grantRole")
    void grantRole(SysUserParam sysUserParam);

    /**
     * 授权数据
     *
     * @param sysUserParam 授权参数
     * @author xuyuxiang
     * @date 2020/3/28 16:54
     */
    @RequestMapping("/grantData")
    void grantData(SysUserParam sysUserParam);

    /**
     * 更新信息
     *
     * @param sysUserParam 更新参数
     * @author xuyuxiang
     * @date 2020/4/1 14:43
     */
    @RequestMapping("/updateInfo")
    void updateInfo(SysUserParam sysUserParam);

    /**
     * 修改密码
     *
     * @param sysUserParam 修改密码参数
     * @author xuyuxiang
     * @date 2020/4/1 14:44
     */
    @RequestMapping("/updatePwd")
    void updatePwd(SysUserParam sysUserParam);

    /**
     * 获取用户的数据范围（组织机构id集合）
     *
     * @param userId 用户id
     * @param orgId  组织机构id
     * @return 数据范围id集合（组织机构id集合）
     * @author xuyuxiang
     * @date 2020/4/5 17:23
     */
    @RequestMapping("/getUserDataScopeIdList")
    List<Long> getUserDataScopeIdList(@RequestParam Long userId, @RequestParam Long orgId);

    /**
     * 根据用户id获取姓名
     *
     * @param userId 用户id
     * @return 用户姓名
     * @author xuyuxiang
     * @date 2020/5/6 15:02
     */
    @RequestMapping("/getNameByUserId")
    String getNameByUserId(Long userId);

    /**
     * 拥有角色
     *
     * @param sysUserParam 查询参数
     * @return 角色id集合
     * @author xuyuxiang
     * @date 2020/5/29 14:10
     */
    @RequestMapping("/ownRole")
    List<Long> ownRole(SysUserParam sysUserParam);

    /**
     * 拥有数据
     *
     * @param sysUserParam 查询参数
     * @return 数据范围id集合
     * @author xuyuxiang
     * @date 2020/5/29 14:10
     */
    @RequestMapping("/ownData")
    List<Long> ownData(SysUserParam sysUserParam);

    /**
     * 重置密码
     *
     * @param sysUserParam 重置参数
     * @author xuyuxiang
     * @date 2020/5/29 14:57
     */
    @RequestMapping("/resetPwd")
    void resetPwd(SysUserParam sysUserParam);

    /**
     * 修改头像
     *
     * @param sysUserParam 修改头像参数
     * @author xuyuxiang
     * @date 2020/6/28 15:21
     */
    @RequestMapping("/updateAvatar")
    void updateAvatar(SysUserParam sysUserParam);

    /**
     * 导出用户
     *
     * @param sysUserParam 导出参数
     * @author xuyuxiang
     * @date 2020/6/30 16:08
     */
    @RequestMapping("/export")
    void export(SysUserParam sysUserParam);

    /**
     * 用户选择器
     *
     * @param sysUserParam 查询参数
     * @return 用户列表集合，格式[{"id":123,"name":"张三"},{"id":456,"name":"李四"}]
     * @author xuyuxiang
     * @date 2020/7/3 13:17
     */
    @RequestMapping("/selector")
    List<Dict> selector(SysUserParam sysUserParam);

    /**
     * 根据用户id获取用户
     *
     * @param userId 用户id
     * @return 用户实体
     * @author xuyuxiang
     * @date 2020/7/29 10:07
     **/
    @RequestMapping("/getUserById")
    SysUser getUserById(Long userId);

    /**
     * 将授权的用户信息保存到用户表
     *
     * @param authUser 授权的用户信息
     * @param sysUser  用户表信息
     * @return void
     * @author xuyuxiang
     * @date 2020/7/29 10:26
     **/
    @RequestMapping("/saveAuthUserToUser")
    void saveAuthUserToUser(@RequestParam AuthUser authUser, @RequestParam SysUser sysUser);

    /**
     * 获取用户id集合
     *
     * @return 用户id集合
     * @author xuyuxiang
     * @date 2020/9/11 17:54
     **/
    @RequestMapping("/getAllUserIdList")
    List<Long> getAllUserIdList();
}
