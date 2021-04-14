package vip.xiaonuo.sys.modular.dict.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.ObjectUtil;
import vip.xiaonuo.common.enums.CommonStatusEnum;
import vip.xiaonuo.common.exception.ServiceException;
import vip.xiaonuo.common.exception.enums.StatusExceptionEnum;
import vip.xiaonuo.core.factory.PageFactory;
import vip.xiaonuo.core.factory.TreeBuildFactory;
import vip.xiaonuo.common.pojo.page.PageResult;
import vip.xiaonuo.sys.modular.dict.entity.SysDictData;
import vip.xiaonuo.sys.modular.dict.entity.SysDictType;
import vip.xiaonuo.sys.modular.dict.enums.SysDictTypeExceptionEnum;
import vip.xiaonuo.sys.modular.dict.mapper.SysDictTypeMapper;
import vip.xiaonuo.sys.modular.dict.param.SysDictTypeParam;
import vip.xiaonuo.sys.modular.dict.result.SysDictTreeNode;
import vip.xiaonuo.sys.modular.dict.service.SysDictDataService;
import vip.xiaonuo.sys.modular.dict.service.SysDictTypeService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 系统字典类型service接口实现类
 *
 * @author xuyuxiang，xuyuxiang
 * @date 2020/3/13 16:11
 */
@Service
public class SysDictTypeServiceImpl extends ServiceImpl<SysDictTypeMapper, SysDictType> implements SysDictTypeService {

    @Resource
    private SysDictDataService sysDictDataService;

    @Override
    public PageResult<SysDictType> page(SysDictTypeParam sysDictTypeParam) {

        //构造条件
        LambdaQueryWrapper<SysDictType> queryWrapper = new LambdaQueryWrapper<>();

        if (ObjectUtil.isNotNull(sysDictTypeParam)) {
            //根据字典类型名称模糊查询
            if (ObjectUtil.isNotEmpty(sysDictTypeParam.getName())) {
                queryWrapper.like(SysDictType::getName, sysDictTypeParam.getName());
            }

            //根据字典类型编码模糊查询
            if (ObjectUtil.isNotEmpty(sysDictTypeParam.getCode())) {
                queryWrapper.like(SysDictType::getCode, sysDictTypeParam.getCode());
            }
        }

        //查询未删除的
        queryWrapper.ne(SysDictType::getStatus, CommonStatusEnum.DELETED.getCode());
        //根据排序升序排列，序号越小越在前
        queryWrapper.orderByAsc(SysDictType::getSort);
        //查询分页结果
        return new PageResult<>(this.page(PageFactory.defaultPage(), queryWrapper));
    }

    @Override
    public List<SysDictType> list(SysDictTypeParam sysDictTypeParam) {

        //构造条件
        LambdaQueryWrapper<SysDictType> queryWrapper = new LambdaQueryWrapper<>();

        //查询未删除的
        queryWrapper.ne(SysDictType::getStatus, CommonStatusEnum.DELETED.getCode());

        return this.list(queryWrapper);
    }

    @Override
    public List<Dict> dropDown(SysDictTypeParam sysDictTypeParam) {
        LambdaQueryWrapper<SysDictType> queryWrapper = new LambdaQueryWrapper<SysDictType>()
                .eq(SysDictType::getCode, sysDictTypeParam.getCode());

        SysDictType sysDictType = this.getOne(queryWrapper);
        if (ObjectUtil.isNull(sysDictType)) {
            throw new ServiceException(SysDictTypeExceptionEnum.DICT_TYPE_NOT_EXIST);
        }
        return sysDictDataService.getDictDataListByDictTypeId(sysDictType.getId());
    }

    @Override
    public void add(SysDictTypeParam sysDictTypeParam) {

        //校验参数，检查是否存在重复的编码或者名称，不排除当前添加的这条记录
        checkParam(sysDictTypeParam, false);

        //将dto转为实体
        SysDictType sysDictType = new SysDictType();
        BeanUtil.copyProperties(sysDictTypeParam, sysDictType);

        //设置状态为启用
        sysDictType.setStatus(CommonStatusEnum.ENABLE.getCode());

        this.save(sysDictType);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void delete(SysDictTypeParam sysDictTypeParam) {

        //根据id查询实体
        SysDictType sysDictType = this.querySysDictType(sysDictTypeParam);

        //逻辑删除，修改状态
        sysDictType.setStatus(CommonStatusEnum.DELETED.getCode());

        //更新实体
        this.updateById(sysDictType);

        //级联删除字典值
        sysDictDataService.deleteByTypeId(sysDictType.getId());
    }

    @Override
    public void edit(SysDictTypeParam sysDictTypeParam) {

        //根据id查询实体
        SysDictType sysDictType = this.querySysDictType(sysDictTypeParam);

        //校验参数，检查是否存在重复的编码或者名称，排除当前编辑的这条记录
        checkParam(sysDictTypeParam, true);

        //请求参数转化为实体
        BeanUtil.copyProperties(sysDictTypeParam, sysDictType);

        //不能修改状态，用修改状态接口修改状态
        sysDictType.setStatus(null);

        this.updateById(sysDictType);
    }

    @Override
    public SysDictType detail(SysDictTypeParam sysDictTypeParam) {
        return this.querySysDictType(sysDictTypeParam);
    }

    @Override
    public void changeStatus(SysDictTypeParam sysDictTypeParam) {
        Long id = sysDictTypeParam.getId();
        Integer status = sysDictTypeParam.getStatus();

        //校验状态在不在枚举值里
        CommonStatusEnum.validateStatus(status);

        //更新枚举，更新只能更新未删除状态的
        LambdaUpdateWrapper<SysDictType> updateWrapper = new LambdaUpdateWrapper<>();
        updateWrapper.eq(SysDictType::getId, id)
                .and(i -> i.ne(SysDictType::getStatus, CommonStatusEnum.DELETED.getCode()))
                .set(SysDictType::getStatus, status);
        boolean update = this.update(updateWrapper);
        if (!update) {
            throw new ServiceException(StatusExceptionEnum.UPDATE_STATUS_ERROR);
        }
    }

    @Override
    public List<SysDictTreeNode> tree() {
        List<SysDictTreeNode> resultList = CollectionUtil.newArrayList();
        LambdaQueryWrapper<SysDictType> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.ne(SysDictType::getStatus, CommonStatusEnum.DELETED.getCode());
        this.list(queryWrapper).forEach(sysDictType -> {
            SysDictTreeNode sysDictTreeNode = new SysDictTreeNode();
            BeanUtil.copyProperties(sysDictType, sysDictTreeNode);
            sysDictTreeNode.setPid(0L);
            resultList.add(sysDictTreeNode);
        });
        sysDictDataService.list(new LambdaQueryWrapper<SysDictData>().ne(SysDictData::getStatus, CommonStatusEnum.DELETED.getCode()))
                .forEach(sysDictData -> {
                    SysDictTreeNode sysDictTreeNode = new SysDictTreeNode();
                    sysDictTreeNode.setId(sysDictData.getId());
                    sysDictTreeNode.setPid(sysDictData.getTypeId());
                    sysDictTreeNode.setCode(sysDictData.getCode());
                    sysDictTreeNode.setName(sysDictData.getValue());
                    resultList.add(sysDictTreeNode);
                });
        return new TreeBuildFactory<SysDictTreeNode>().doTreeBuild(resultList);
    }

    /**
     * 校验参数，检查是否存在重复的编码或者名称
     *
     * @author xuyuxiang，xuyuxiang
     * @date 2020/3/25 21:23
     */
    private void checkParam(SysDictTypeParam sysDictTypeParam, boolean isExcludeSelf) {
        Long id = sysDictTypeParam.getId();
        String name = sysDictTypeParam.getName();
        String code = sysDictTypeParam.getCode();

        //构建带name和code的查询条件
        LambdaQueryWrapper<SysDictType> queryWrapperByName = new LambdaQueryWrapper<>();
        queryWrapperByName.eq(SysDictType::getName, name)
                .ne(SysDictType::getStatus, CommonStatusEnum.DELETED.getCode());

        LambdaQueryWrapper<SysDictType> queryWrapperByCode = new LambdaQueryWrapper<>();
        queryWrapperByCode.eq(SysDictType::getCode, code)
                .ne(SysDictType::getStatus, CommonStatusEnum.DELETED.getCode());

        //如果排除自己，则增加查询条件主键id不等于本条id
        if (isExcludeSelf) {
            queryWrapperByName.ne(SysDictType::getId, id);
            queryWrapperByCode.ne(SysDictType::getId, id);
        }

        //查询重复记录的数量
        int countByName = this.count(queryWrapperByName);
        int countByCode = this.count(queryWrapperByCode);

        //如果存在重复的记录，抛出异常，直接返回前端
        if (countByName >= 1) {
            throw new ServiceException(SysDictTypeExceptionEnum.DICT_TYPE_NAME_REPEAT);
        }
        if (countByCode >= 1) {
            throw new ServiceException(SysDictTypeExceptionEnum.DICT_TYPE_CODE_REPEAT);
        }
    }

    /**
     * 获取系统字典类型
     *
     * @author xuyuxiang，xuyuxiang
     * @date 2020/3/31 20:38
     */
    private SysDictType querySysDictType(SysDictTypeParam sysDictTypeParam) {
        SysDictType sysDictType = this.getById(sysDictTypeParam.getId());
        if (ObjectUtil.isNull(sysDictType)) {
            throw new ServiceException(SysDictTypeExceptionEnum.DICT_TYPE_NOT_EXIST);
        }
        return sysDictType;
    }
}
