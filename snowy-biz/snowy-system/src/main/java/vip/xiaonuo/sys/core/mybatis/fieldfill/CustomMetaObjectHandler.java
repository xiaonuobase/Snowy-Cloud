package vip.xiaonuo.sys.core.mybatis.fieldfill;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.log.Log;
import vip.xiaonuo.common.context.login.LoginContextHolder;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.ReflectionException;

import java.util.Date;

/**
 * 自定义sql字段填充器，自动填充创建修改相关字段
 *
 * @author xuyuxiang
 * @date 2020/3/30 15:21
 */
public class CustomMetaObjectHandler implements MetaObjectHandler {

    private static final Log log = Log.get();

    private static final String CREATE_USER = "createUser";

    private static final String CREATE_TIME = "createTime";

    private static final String UPDATE_USER = "updateUser";

    private static final String UPDATE_TIME = "updateTime";

    @Override
    public void insertFill(MetaObject metaObject) {
        try {
            //为空则设置createUser（BaseEntity)
            Object createUser = metaObject.getValue(CREATE_USER);
            if(ObjectUtil.isNull(createUser)) {
                setFieldValByName(CREATE_USER, this.getUserUniqueId(), metaObject);
            }

            //为空则设置createTime（BaseEntity)
            Object createTime = metaObject.getValue(CREATE_TIME);
            if(ObjectUtil.isNull(createTime)) {
                setFieldValByName(CREATE_TIME, new Date(), metaObject);
            }
        } catch (ReflectionException e) {
            log.warn(">>> CustomMetaObjectHandler处理过程中无相关字段，不做处理");
        }
    }

    @Override
    public void updateFill(MetaObject metaObject) {
        try {
            //设置updateUser（BaseEntity)
            setFieldValByName(UPDATE_USER, this.getUserUniqueId(), metaObject);
            //设置updateTime（BaseEntity)
            setFieldValByName(UPDATE_TIME, new Date(), metaObject);
        } catch (ReflectionException e) {
            log.warn(">>> CustomMetaObjectHandler处理过程中无相关字段，不做处理");
        }
    }

    /**
     * 获取用户唯一id
     */
    private Long getUserUniqueId() {
        try {
            return LoginContextHolder.me().getSysLoginUserId();
        } catch (Exception e) {
            //如果获取不到就返回-1
            return -1L;
        }
    }
}
