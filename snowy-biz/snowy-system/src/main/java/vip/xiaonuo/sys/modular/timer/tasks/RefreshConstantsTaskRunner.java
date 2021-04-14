package vip.xiaonuo.sys.modular.timer.tasks;

import cn.hutool.log.Log;
import vip.xiaonuo.common.context.constant.ConstantContext;
import vip.xiaonuo.common.enums.CommonStatusEnum;
import vip.xiaonuo.common.timer.TimerTaskRunner;
import vip.xiaonuo.sys.modular.consts.entity.SysConfig;
import vip.xiaonuo.sys.modular.consts.service.SysConfigService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * 定时器：用来根据sys_config表中的配置，刷新ConstantContextHolder中的缓存
 * <p>
 * 防止由于直接改动数据库，而调用缓存常量时，产生数据不一致问题
 *
 * @author xuyuxiang
 * @date 2020/7/30 16:41
 */
@Component
public class RefreshConstantsTaskRunner implements TimerTaskRunner {

    private static final Log log = Log.get();

    @Resource
    private SysConfigService sysConfigService;

    @Override
    public void action() {

        // 查询库中的所有配置
        LambdaQueryWrapper<SysConfig> sysConfigLambdaQueryWrapper = new LambdaQueryWrapper<>();

        sysConfigLambdaQueryWrapper.eq(SysConfig::getStatus, CommonStatusEnum.ENABLE.getCode());
        sysConfigLambdaQueryWrapper.select(SysConfig::getCode, SysConfig::getValue);

        List<SysConfig> list = sysConfigService.list(sysConfigLambdaQueryWrapper);

        // 所有配置添加到缓存中，覆盖已有配置
        list.forEach(sysConfig -> ConstantContext.putConstant(sysConfig.getCode(), sysConfig.getValue()));

    }

}
