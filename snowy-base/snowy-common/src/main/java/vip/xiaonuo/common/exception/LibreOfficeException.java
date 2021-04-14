package vip.xiaonuo.common.exception;

import lombok.Getter;

/**
 * LibreOffice相关异常
 *
 * @author xuyuxiang
 * @date 2020/7/7 11:08
 */
@Getter
public class LibreOfficeException extends ServiceException {

    private static final int LIBRE_OFFICE_EXP_CODE = 15000;

    private static final String LIBRE_OFFICE_EXP_ERROR_MESSAGE = "LibreOffice初始化异常，请检查LibreOffice是否启动！";

    public LibreOfficeException() {
        super(LIBRE_OFFICE_EXP_CODE, LIBRE_OFFICE_EXP_ERROR_MESSAGE);
    }

}
