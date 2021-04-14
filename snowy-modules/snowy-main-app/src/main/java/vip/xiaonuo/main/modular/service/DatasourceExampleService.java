package vip.xiaonuo.main.modular.service;

import cn.hutool.core.util.RandomUtil;
import vip.xiaonuo.sys.modular.app.param.SysAppParam;
import vip.xiaonuo.sys.modular.app.service.SysAppService;
import vip.xiaonuo.sys.modular.log.entity.SysVisLog;
import vip.xiaonuo.sys.modular.log.service.SysVisLogService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import javax.annotation.Resource;
import java.util.List;

/**
 * 一个service实现
 *
 * @author yubaoshan
 * @date 2020/4/9 18:11
 */
@Service
public class DatasourceExampleService {

    @Resource
    private SysVisLogService sysVisLogService;

    @Resource
    private SysAppService sysAppService;

    public List<SysVisLog> masterDatasource() {
        return sysVisLogService.list();
    }

    public List<SysVisLog> backupDatasource() {
        return sysVisLogService.list();
    }

    public void datasourceTransactionNone() {

        SysAppParam sysAppParam = new SysAppParam();
        sysAppParam.setName(RandomUtil.randomNumbers(5));
        sysAppParam.setCode(RandomUtil.randomNumbers(5));
        sysAppParam.setActive("N");

        sysAppService.add(sysAppParam);

        //抛异常测试
        int i = 1 / 0;
    }

    //@DataSource(name = "master")
    @Transactional(rollbackFor = Exception.class)
    public void datasourceTransaction() {

        SysAppParam sysAppParam = new SysAppParam();
        sysAppParam.setName(RandomUtil.randomNumbers(5));
        sysAppParam.setCode(RandomUtil.randomNumbers(5));
        sysAppParam.setActive("N");

        sysAppService.add(sysAppParam);

        //抛异常测试
        int i = 1 / 0;
    }

}
