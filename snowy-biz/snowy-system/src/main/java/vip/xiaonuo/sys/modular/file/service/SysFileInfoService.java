package vip.xiaonuo.sys.modular.file.service;

import vip.xiaonuo.common.pojo.page.PageResult;
import vip.xiaonuo.sys.modular.file.entity.SysFileInfo;
import vip.xiaonuo.sys.modular.file.param.SysFileInfoParam;
import vip.xiaonuo.sys.modular.file.result.SysFileInfoResult;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * 文件信息表 服务类
 *
 * @author yubaoshan
 * @date 2020/6/7 22:15
 */
public interface SysFileInfoService extends IService<SysFileInfo> {

    /**
     * 分页查询文件信息表
     *
     * @param sysFileInfoParam 查询参数
     * @return 查询分页结果
     * @author yubaoshan
     * @date 2020/6/7 22:15
     */
    PageResult<SysFileInfo> page(SysFileInfoParam sysFileInfoParam);

    /**
     * 查询所有文件信息表
     *
     * @param sysFileInfoParam 查询参数
     * @return 文件信息列表
     * @author yubaoshan
     * @date 2020/6/7 22:15
     */
    List<SysFileInfo> list(SysFileInfoParam sysFileInfoParam);

    /**
     * 添加文件信息表
     *
     * @param sysFileInfoParam 添加参数
     * @author yubaoshan
     * @date 2020/6/7 22:15
     */
    void add(SysFileInfoParam sysFileInfoParam);

    /**
     * 删除文件信息表
     *
     * @param sysFileInfoParam 删除参数
     * @author yubaoshan
     * @date 2020/6/7 22:15
     */
    void delete(SysFileInfoParam sysFileInfoParam);

    /**
     * 编辑文件信息表
     *
     * @param sysFileInfoParam 编辑参数
     * @author yubaoshan
     * @date 2020/6/7 22:15
     */
    void edit(SysFileInfoParam sysFileInfoParam);

    /**
     * 查看详情文件信息表
     *
     * @param sysFileInfoParam 查看参数
     * @return 文件信息
     * @author yubaoshan
     * @date 2020/6/7 22:15
     */
    SysFileInfo detail(SysFileInfoParam sysFileInfoParam);

    /**
     * 上传文件，返回文件的唯一标识
     *
     * @param file 要上传的文件
     * @return 文件id
     * @author yubaoshan
     * @date 2020/6/9 21:21
     */
    Long uploadFile(MultipartFile file);

    /**
     * 获取文件信息结果集
     *
     * @param fileId 文件id
     * @return 文件信息结果集
     * @author yubaoshan
     * @date 2020/6/9 21:56
     */
    SysFileInfoResult getFileInfoResult(Long fileId);

    /**
     * 判断文件是否存在
     *
     * @param field 文件id
     * @author xuyuxiang
     * @date 2020/6/28 15:55
     */
    void assertFile(Long field);

    /**
     * 文件预览
     *
     * @param sysFileInfoParam 文件预览参数
     * @param response         响应结果
     * @author xuyuxiang
     * @date 2020/7/7 11:23
     */
    void preview(SysFileInfoParam sysFileInfoParam, HttpServletResponse response);

    /**
     * 文件下载
     *
     * @param sysFileInfoParam 文件下载参数
     * @param response         响应结果
     * @author xuyuxiang
     * @date 2020/7/7 12:09
     */
    void download(SysFileInfoParam sysFileInfoParam, HttpServletResponse response);
}
