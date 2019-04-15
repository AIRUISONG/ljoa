package com.jgp.ljoa.channel.service.impl;

import com.jgp.ljoa.channel.model.LjHouseSaleInfo;
import com.jgp.ljoa.channel.repository.LjHouseSaleInfoRepository;
import com.jgp.ljoa.channel.service.LjHouseSaleInfoService;
import com.jgp.sys.query.QueryFilterList;
import com.jgp.sys.ui.Pager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Administrator on 2018/7/3.
 */
/*
作者  SSF
时间   2018/7/3
*/
@Service
public class LjHouseSaleInfoServiceImpl implements LjHouseSaleInfoService {
    @Autowired
    private LjHouseSaleInfoRepository repository;


    @Override
    public LjHouseSaleInfo queryOneLjHouseSaleInfoBuUUid(String houseUuid) {
        QueryFilterList list=new QueryFilterList();
        list.addFilter("houseUuid","eq",houseUuid);
        List<LjHouseSaleInfo> read = repository.read(list);
        LjHouseSaleInfo ljHouseSaleInfo=new LjHouseSaleInfo();
        if(read.size()>0){
            ljHouseSaleInfo=read.get(0);
        }
        return ljHouseSaleInfo;
    }

    @Override
    public LjHouseSaleInfo queryOneLjHouseSaleInfo(String id) {
        return repository.read(id);
    }

    @Override
    public List<LjHouseSaleInfo> queryAllLjHouseSaleInfo() {
        return repository.readAll();
    }

    @Transactional
    @Override
    public LjHouseSaleInfo saveLjHouseSaleInfo(LjHouseSaleInfo ljHouseSaleInfo) {
        return repository.createOrUpdate(ljHouseSaleInfo);
    }
    @Transactional
    @Override
    public void removeLjHouseSaleInfo(String id) {
        repository.delete(id);
    }

    @Override
    public List<LjHouseSaleInfo> queryQueryFilterList(QueryFilterList list) {
        return repository.read(list);
    }

    @Override
    public LjHouseSaleInfo queryOneLjHouseSaleInfoByType(String id, LjHouseSaleInfo ljHouseSaleInfo, Pager pager) {
        QueryFilterList list=new QueryFilterList();
        list.addFilter("houseUuid","eq",id);
        if(StringUtils.isNotEmpty(ljHouseSaleInfo.getChannelCompany())){
            list.addFilter("channelCompany","eq",ljHouseSaleInfo.getChannelCompany());
        }
        List<LjHouseSaleInfo> read = repository.read(list, pager);
        LjHouseSaleInfo ljHouseSaleInfo1=new LjHouseSaleInfo();
        if(read.size()>0){
            ljHouseSaleInfo1=read.get(0);
        }
        return ljHouseSaleInfo1;
    }
}
