package com.jgp.ljoa.common.service.impl;

import com.jgp.ljoa.common.model.LjNoticeInfo;
import com.jgp.ljoa.common.repository.LjNoticeInfoRepository;
import com.jgp.ljoa.common.service.LjNoticeInfoService;
import com.jgp.sys.query.OrderDirection;
import com.jgp.sys.query.OrderList;
import com.jgp.sys.query.QueryFilterList;
import com.jgp.sys.ui.Pager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Administrator on 2018/9/11.
 */
/*
作者  SSF
时间   2018/9/11
*/
@Service
public class LjNoticeInfoServiceImpl implements LjNoticeInfoService {

    @Autowired
    private LjNoticeInfoRepository ljNoticeInfoRepository;

    @Override
    public LjNoticeInfo saveLjNoticeInfo(LjNoticeInfo ljNoticeInfo) {
        return ljNoticeInfoRepository.createOrUpdate(ljNoticeInfo);
    }

    @Override
    public void removeOneLjNoticeInfoById(String id) {
        ljNoticeInfoRepository.delete(id);
    }

    @Transactional(readOnly = true)
    @Override
    public List<LjNoticeInfo> queryGroupLjNoticeInfo(LjNoticeInfo ljNoticeInfo,Pager pager) {
        QueryFilterList queryFilters=new QueryFilterList();
        if(StringUtils.isNotEmpty(ljNoticeInfo.getNoticeType())){
            queryFilters.addFilter("noticeType","eq",ljNoticeInfo.getNoticeType());//信息类别
        }
        if(StringUtils.isNotEmpty(ljNoticeInfo.getBoticeStatus())){
            queryFilters.addFilter("boticeStatus","eq",ljNoticeInfo.getBoticeStatus());//信息状态
        }

        OrderList orders=new OrderList();
        orders.addOrder("createDatetime", OrderDirection.DESC);
        return ljNoticeInfoRepository.read(queryFilters,orders,pager);
    }

    @Transactional
    @Override
    public LjNoticeInfo queryOneLjNoticeInfo(String id) {
        return ljNoticeInfoRepository.read(id);
    }

    @Override
    public List<LjNoticeInfo> queryGroupLjNoticeInfoByType(LjNoticeInfo ljNoticeInfo, List<String> type, Pager pager) {
        QueryFilterList queryFilters=new QueryFilterList();
        if(StringUtils.isNotEmpty(ljNoticeInfo.getNoticeType())){
            queryFilters.addFilter("noticeType","nin",type);//信息类别
        }
        if(StringUtils.isNotEmpty(ljNoticeInfo.getBoticeStatus())){
            queryFilters.addFilter("boticeStatus","eq",ljNoticeInfo.getBoticeStatus());//信息状态
        }
        OrderList orders=new OrderList();
        orders.addOrder("createDatetime", OrderDirection.DESC);
        return ljNoticeInfoRepository.read(queryFilters,orders,pager);
    }
}
