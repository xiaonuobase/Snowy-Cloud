package vip.xiaonuo.main.modular.controller;

import vip.xiaonuo.common.pojo.response.ResponseData;
import vip.xiaonuo.common.pojo.response.SuccessResponseData;
import vip.xiaonuo.main.modular.service.DatasourceExampleService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 一个示例接口
 *
 * @author yubaoshan
 * @date 2020/4/9 18:09
 */
@RestController
@RequestMapping("/example")
public class DatasourceExampleController {

    @Resource
    private DatasourceExampleService datasourceService;

    @GetMapping("/niceDay")
    public ResponseData niceDay() {
        return new SuccessResponseData("nice day");
    }

    @GetMapping("/masterDatasource")
    public ResponseData masterDatasource() {
        return new SuccessResponseData(datasourceService.masterDatasource());
    }

    @GetMapping("/backupDatasource")
    public ResponseData backupDatasource() {
        return new SuccessResponseData(datasourceService.backupDatasource());
    }

    @GetMapping("/datasourceTransactionNone")
    public ResponseData datasourceTransactionNone() {
        datasourceService.datasourceTransactionNone();
        return new SuccessResponseData();
    }

    @GetMapping("/datasourceTransaction")
    public ResponseData datasourceTransaction() {
        datasourceService.datasourceTransaction();
        return new SuccessResponseData();
    }

}
