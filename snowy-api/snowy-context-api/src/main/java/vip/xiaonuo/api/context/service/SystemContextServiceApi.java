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

import cn.hutool.core.lang.Dict;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import vip.xiaonuo.common.consts.FeignConstant;
import vip.xiaonuo.common.pojo.base.validate.UniqueValidateParam;
import vip.xiaonuo.common.pojo.login.SysLoginUser;

import java.util.List;

/**
 * SystemContext上下文服务接口
 *
 * @author dongxiayu
 * @date 2021/3/28 14:57
 */
@RequestMapping("/feign/systemContextServiceApi")
public interface SystemContextServiceApi {

    String APP_NAME = FeignConstant.MAIN_APP;

    /**
     * 根据用户id获取姓名
     *
     * @param userId 用户id
     * @return 用户姓名
     * @author dongxiayu
     * @date 2021/3/28 14:57
     */
    @RequestMapping("/getNameByUserId")
    String getNameByUserId(@RequestParam(value = "userId") Long userId);

    /**
     * 根据角色id获取角色名称
     *
     * @param roleId 角色id
     * @return 角色名称
     * @author dongxiayu
     * @date 2021/3/28 14:57
     */
    @RequestMapping("/getNameByRoleId")
    String getNameByRoleId(@RequestParam(value = "roleId") Long roleId);

    /**
     * 根据token获取登录用户信息
     *
     * @param token token
     * @return 登录用户信息
     * @author dongxiayu
     * @date 2021/3/28 14:57
     */
    @RequestMapping("/getLoginUserByToken")
    SysLoginUser getLoginUserByToken(@RequestParam(value = "token") String token);

    /**
     * 根据用户账号模糊搜索系统用户列表
     *
     * @param account 账号
     * @return 增强版hashMap，格式：[{"id:":123, "firstName":"张三"}]
     * @author dongxiayu
     * @date 2021/3/28 14:57
     */
    @RequestMapping("/listUser")
    List<Dict> listUser(@RequestParam(required = false, value = "account") String account);

    /**
     * 根据角色名模糊搜索系统角色列表
     *
     * @param name 角色名
     * @return 增强版hashMap，格式：[{"id:":123, "name":"总经理"}]
     * @author dongxiayu
     * @date 2021/3/28 14:57
     */
    @RequestMapping("/listRole")
    List<Dict> listRole(@RequestParam(required = false, value = "name") String name);

    /**
     * 根据id判断是否是用户
     *
     * @param userOrRoleId 用户或角色id
     * @return true是 false否
     * @author dongxiayu
     * @date 2021/3/28 14:57
     */
    @RequestMapping("/isUser")
    boolean isUser(@RequestParam(value = "userOrRoleId") Long userOrRoleId);

    /**
     * 根据id判断是否是角色
     *
     * @param userOrRoleId 用户或角色id
     * @return true是 false否
     * @author dongxiayu
     * @date 2021/3/28 14:57
     */
    @RequestMapping("/isRole")
    boolean isRole(@RequestParam(value = "userOrRoleId") Long userOrRoleId);

    /**
     * 根据字典类型获取字典的code值
     *
     * @param dictTypeCodes 字典类型编码值
     * @return 字典的code值
     * @author dongxiayu
     * @date 2021/3/28 14:57
     */
    @RequestMapping("/getDictCodesByDictTypeCode")
    List<String> getDictCodesByDictTypeCode(@RequestParam(value = "dictTypeCodes") String... dictTypeCodes);

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
    @RequestMapping(name = "/tableUniValueFlag",consumes = "application/json")
    boolean tableUniValueFlag(@RequestParam(value = "uniqueValidateParam") UniqueValidateParam uniqueValidateParam);

    /**
     * 获取系统用户id集合
     *
     * @return 用户id集合
     * @author dongxiayu
     * @date 2021/3/28 14:57
     **/
    @RequestMapping("/getAllUserIdList")
    List<Long> getAllUserIdList();


}
