package vip.xiaonuo.sys.modular.monitor.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjectUtil;
import vip.xiaonuo.cache.UserCache;
import vip.xiaonuo.common.context.constant.ConstantContextHolder;
import vip.xiaonuo.common.context.login.LoginContextHolder;
import vip.xiaonuo.common.exception.DemoException;
import vip.xiaonuo.core.factory.PageFactory;
import vip.xiaonuo.common.pojo.login.SysLoginUser;
import vip.xiaonuo.common.pojo.page.PageResult;
import vip.xiaonuo.core.util.PageUtil;
import vip.xiaonuo.sys.core.log.LogManager;
import vip.xiaonuo.sys.modular.monitor.param.SysOnlineUserParam;
import vip.xiaonuo.sys.modular.monitor.result.SysOnlineUserResult;
import vip.xiaonuo.sys.modular.monitor.service.SysOnlineUserService;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 系统组织机构service接口实现类
 *
 * @author xuyuxiang
 * @date 2020/4/7 17:06
 */
@Service
public class SysOnlineUserServiceImpl implements SysOnlineUserService {

    @Resource
    private UserCache userCache;

    @Override
    public PageResult<SysOnlineUserResult> list(SysOnlineUserParam sysOnlineUserParam) {
        List<SysOnlineUserResult> tempList = CollectionUtil.newArrayList();
        // 获取缓存中的所有用户
        Map<String, SysLoginUser> allKeyValues = userCache.getAllKeyValues();
        for (Map.Entry<String, SysLoginUser> sysLoginUserEntry : allKeyValues.entrySet()) {
            SysOnlineUserResult sysOnlineUserResult = new SysOnlineUserResult();
            sysOnlineUserResult.setSessionId(sysLoginUserEntry.getKey());
            BeanUtil.copyProperties(sysLoginUserEntry.getValue(), sysOnlineUserResult);
            tempList.add(sysOnlineUserResult);
        }
        List<SysOnlineUserResult> listAll = tempList.stream()
                .sorted(Comparator.comparing(SysOnlineUserResult::getLastLoginTime, Comparator.reverseOrder()))
                .collect(Collectors.toList());
        Page<SysOnlineUserResult> page = PageFactory.defaultPage();
        page.setTotal(tempList.size());
        List<SysOnlineUserResult> resultList = PageUtil.page(page, listAll);
        return new PageResult<>(page, resultList);
    }

    @Override
    public void forceExist(SysOnlineUserParam sysOnlineUserParam) {
        Boolean demoEnvFlag = ConstantContextHolder.getDemoEnvFlag();
        if (demoEnvFlag) {
            throw new DemoException();
        }

        //获取缓存的key
        String redisLoginUserKey = sysOnlineUserParam.getSessionId();

        //获取缓存的用户
        SysLoginUser sysLoginUser = userCache.get(redisLoginUserKey);

        //如果缓存的用户存在，清除会话，否则表示该会话信息已失效，不执行任何操作
        if (ObjectUtil.isNotNull(sysLoginUser)) {

            //清除登录会话
            userCache.remove(redisLoginUserKey);

            //获取登录用户的账户信息
            String account = LoginContextHolder.me().getSysLoginUserAccount();

            //创建退出登录日志
            LogManager.me().executeExitLog(account);
        }
    }
}
