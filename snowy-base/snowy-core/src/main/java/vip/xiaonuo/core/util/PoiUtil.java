package vip.xiaonuo.core.util;

import cn.afterturn.easypoi.excel.ExcelExportUtil;
import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ExportParams;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.CharsetUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.log.Log;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.Collection;
import java.util.List;

/**
 * 简单导入导出工具类
 *
 * @author xuyuxiang
 * @date 2020/6/30 17:25
 */
public class PoiUtil {

    private static final Log log = Log.get();

    /**
     * 使用流的方式导出excel
     *
     * @param excelName 要导出的文件名称，如Users.xls
     * @param pojoClass Excel实体类
     * @param data      要导出的数据集合
     * @author xuyuxiang
     * @date 2020/7/1 10:00
     */
    public static void exportExcelWithStream(String excelName, Class<?> pojoClass, Collection<?> data) {
        try {
            HttpServletResponse response = HttpServletUtil.getResponse();
            String fileName = URLEncoder.encode(excelName, CharsetUtil.UTF_8);
            response.reset();
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
            response.setContentType("application/octet-stream;charset=UTF-8");
            ServletOutputStream outputStream = response.getOutputStream();
            Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams(), pojoClass, data);
            workbook.write(outputStream);
            outputStream.close();
        } catch (IOException e) {
            log.error(">>> 导出数据异常：{}", e.getMessage());
        }
    }

    /**
     * 使用文件的方式导出excel
     *
     * @param filePath  文件路径，如 d:/demo/demo.xls
     * @param pojoClass Excel实体类
     * @param data      要导出的数据集合
     * @author xuyuxiang
     * @date 2020/7/1 9:58
     */
    public static void exportExcelWithFile(String filePath, Class pojoClass, Collection data) {

        try {
            //先创建父文件夹
            FileUtil.mkParentDirs(filePath);
            File file = FileUtil.file(filePath);
            Workbook workbook = ExcelExportUtil.exportExcel(new ExportParams(), pojoClass, data);
            FileOutputStream fos = new FileOutputStream(file);
            workbook.write(fos);
            fos.close();
        } catch (IOException e) {
            log.error(">>> 导出数据异常：{}", e.getMessage());
        }

    }

    /**
     * 根据文件路径来导入Excel
     *
     * @param filePath   文件路径
     * @param titleRows  表标题的行数
     * @param headerRows 表头行数
     * @param pojoClass  Excel实体类
     * @author xuyuxiang
     * @date 2020/7/1 9:58
     */
    public static <T> List<T> importExcel(String filePath, Integer titleRows, Integer headerRows, Class<T> pojoClass) {
        //判断文件是否存在
        if (ObjectUtil.isEmpty(filePath)) {
            return null;
        }
        ImportParams params = new ImportParams();
        params.setTitleRows(titleRows);
        params.setHeadRows(headerRows);
        List<T> list = null;
        try {
            list = ExcelImportUtil.importExcel(new File(filePath), pojoClass, params);
        } catch (Exception e) {
            log.error(">>> 导入数据异常：{}", e.getMessage());
        }
        return list;
    }

    /**
     * 根据接收的Excel文件来导入Excel,并封装成实体类
     *
     * @param file       上传的文件
     * @param titleRows  表标题的行数
     * @param headerRows 表头行数
     * @param pojoClass  Excel实体类
     * @author xuyuxiang
     * @date 2020/7/1 9:57
     */
    public static <T> List<T> importExcel(MultipartFile file, Integer titleRows, Integer headerRows, Class<T> pojoClass) {
        if (ObjectUtil.isNull(file)) {
            return null;
        }
        ImportParams params = new ImportParams();
        params.setTitleRows(titleRows);
        params.setHeadRows(headerRows);
        List<T> list = null;
        try {
            list = ExcelImportUtil.importExcel(file.getInputStream(), pojoClass, params);
        } catch (Exception e) {
            log.error(">>> 导入数据异常：{}", e.getMessage());
        }
        return list;
    }

}
