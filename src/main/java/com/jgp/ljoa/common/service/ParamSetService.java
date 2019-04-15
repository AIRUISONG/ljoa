package com.jgp.ljoa.common.service;/**
 * Created by Administrator on 2018/7/9.
 */

import com.jgp.ljoa.common.model.ParamSet;
import com.jgp.sys.ui.Pager;

import java.util.List;

/**
 * 项目   ljoa
 * 作者   liujinxu
 * 时间   2018/7/9
 */
public interface ParamSetService {
    /*
    * 查询所有通用参数设置
    * */
    List<ParamSet> queryAllParamSet(Pager pager);

    /*
    * 查询单个
    * */
    ParamSet queryOneParamSet(String id);

    /*
    * 保存
    * */
    ParamSet saveParamSet(ParamSet paramSet);

    /*
      删除通用参数设置
    * */
    void removeParamSet(String id);

    /*
    * 批量删除通用参数设置
    * */
    void  removeParamSets(String[] array);

    /*
    * 条件查询通用参数设置
    * */
    List<ParamSet> queryGroupParamSet(String paramCode);
}
