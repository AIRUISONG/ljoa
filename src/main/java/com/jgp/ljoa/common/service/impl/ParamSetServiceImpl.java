package com.jgp.ljoa.common.service.impl;/**
 * Created by Administrator on 2018/7/9.
 */

import com.jgp.ljoa.common.model.ParamSet;
import com.jgp.ljoa.common.repository.ParamSetRepository;
import com.jgp.ljoa.common.service.ParamSetService;
import com.jgp.sys.query.Operator;
import com.jgp.sys.query.QueryFilterList;
import com.jgp.sys.ui.Pager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

/**
 * 项目   ljoa
 * 作者   liujinxu
 * 时间   2018/7/9
 */
@Service
public class ParamSetServiceImpl implements ParamSetService{
    @Autowired
    private ParamSetRepository paramSetRepository;


    @Override
    public List<ParamSet> queryAllParamSet(Pager pager) {


        return paramSetRepository.read(new QueryFilterList(),pager);
    }


    @Override
    public ParamSet queryOneParamSet(String id) {
        return paramSetRepository.read(id);
    }

    @Transactional
    @Override
    public ParamSet saveParamSet(ParamSet paramSet) {
        return paramSetRepository.createOrUpdate(paramSet);
    }

    @Transactional
    @Override
    public void removeParamSet(String id) {
        paramSetRepository.delete(id);
    }

    @Transactional
    @Override
    public void removeParamSets(String[] array) {
        Arrays.stream(array).forEach(s -> {
            this.removeParamSet(s);
        });
    }

    @Override
    public List<ParamSet> queryGroupParamSet(String paramCode) {
        QueryFilterList list = new QueryFilterList();
        list.addFilter("paramCode", Operator.eq, paramCode);
        return paramSetRepository.read(list);
    }
}
