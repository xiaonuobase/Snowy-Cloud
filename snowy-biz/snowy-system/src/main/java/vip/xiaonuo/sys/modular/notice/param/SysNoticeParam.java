package vip.xiaonuo.sys.modular.notice.param;

import vip.xiaonuo.common.pojo.base.param.BaseParam;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 系统通知公告参数
 *
 * @author xuyuxiang
 * @date 2020/6/28 17:19
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class SysNoticeParam extends BaseParam {

    /**
     * 主键
     */
    @NotNull(message = "id不能为空，请检查id参数", groups = {edit.class, delete.class, detail.class, changeStatus.class})
    private Long id;

    /**
     * 标题
     */
    @NotBlank(message = "标题不能为空，请检查title参数", groups = {add.class, edit.class})
    private String title;

    /**
     * 内容
     */
    @NotBlank(message = "内容不能为空，请检查content参数", groups = {add.class, edit.class})
    private String content;

    /**
     * 类型（字典 1通知 2公告）
     */
    @NotNull(message = "类型不能为空，请检查type参数", groups = {add.class, edit.class})
    @Min(value = 1, message = "类型格式错误，请检查type参数", groups = {add.class, edit.class})
    @Max(value = 2, message = "类型格式错误，请检查type参数", groups = {add.class, edit.class})
    private Integer type;

    /**
     * 状态（字典 0草稿 1发布 2撤回 3删除）
     */
    @NotNull(message = "状态不能为空，请检查status参数", groups = {add.class, edit.class, changeStatus.class})
    private Integer status;

    /**
     * 通知到的人
     */
    @NotNull(message = "通知到的人不能为空，请检查noticeUserIdList参数", groups = {add.class, edit.class})
    private List<Long> noticeUserIdList;
}
