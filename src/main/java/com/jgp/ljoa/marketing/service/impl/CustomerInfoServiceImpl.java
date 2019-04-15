package com.jgp.ljoa.marketing.service.impl;

import com.jgp.ljoa.channel.model.LjHouseInfo;
import com.jgp.ljoa.channel.repository.LjHouseInfoRepository;
import com.jgp.ljoa.channel.service.LjHouseInfoService;
import com.jgp.ljoa.marketing.model.CustomerInfo;
import com.jgp.ljoa.marketing.repository.CustomerInfoRepository;
import com.jgp.ljoa.marketing.service.CustomerInfoService;
import com.jgp.sys.query.QueryFilterList;
import com.jgp.sys.ui.Pager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Administrator on 2018/7/4 0004.
 */
@Service
public class CustomerInfoServiceImpl implements CustomerInfoService{
    @Autowired
    private CustomerInfoRepository customerInfoRepository;
    @Autowired
    private LjHouseInfoRepository ljHouseInfoRepository;
    @Autowired
    private LjHouseInfoService ljHouseInfoService;

    @Transactional
    @Override
    public CustomerInfo saveCustomerInfo(CustomerInfo customerInfo) {
        return customerInfoRepository.createOrUpdate(customerInfo);
    }

    @Transactional
    @Override
    public CustomerInfo saveNetSigning(CustomerInfo customerInfo) {
        LjHouseInfo ljHouseInfo = ljHouseInfoService.queryOneHouseInfo(customerInfo.getHouseUuid());
        ljHouseInfo.setStatus("2");
        ljHouseInfoService.saveLjHouseInfo(ljHouseInfo);
        return customerInfoRepository.createOrUpdate(customerInfo);
    }

    @Override
    public CustomerInfo queryCustomerInfoByHouseUuid(String hid) {
        QueryFilterList queryFilterList = new QueryFilterList();
        queryFilterList.addFilter("houseUuid","eq",hid);
        List<CustomerInfo> customerInfos = customerInfoRepository.read(queryFilterList);
        return customerInfos.size() > 0 ? customerInfos.get(0) : null;
    }

    @Override
    public List<LjHouseInfo> queryHouseInfoByCustomerInfoSignStatus(LjHouseInfo ljHouseInfo,Pager pager) {
        List<String> earnestStatus = new ArrayList();
        earnestStatus.add("1");
        earnestStatus.add("2");
        QueryFilterList queryFilterList = new QueryFilterList();
        queryFilterList.addFilter("signStatus","eq","1");
        queryFilterList.addFilter("earnestStatus","in",earnestStatus);
//        queryFilterList.addFilter("houseType","eq","2");
        List<CustomerInfo> customerInfos = customerInfoRepository.read(queryFilterList);
        List<LjHouseInfo> ljHouseInfos = new ArrayList<>();
        if (customerInfos.size()>0){
            List<String> collect = customerInfos.stream().map(customerInfo -> {
                return customerInfo.getHouseUuid();
            }).collect(Collectors.toList());
            QueryFilterList list = new QueryFilterList();
            if(StringUtils.isNotEmpty(ljHouseInfo.getProjectUuid())){
                list.addFilter("projectUuid","eq",ljHouseInfo.getProjectUuid());
            }
            list.addFilter("id","in",collect);
            ljHouseInfos = ljHouseInfoRepository.read(list, pager);
        }
        return ljHouseInfos;
    }

    @Override
    public List<CustomerInfo> queryCustomerInfoBySignStatus(CustomerInfo customerInfo,Pager pager) {
        List<String> earnestStatus = new ArrayList();
        earnestStatus.add("1");
        earnestStatus.add("2");
        QueryFilterList queryFilterList = new QueryFilterList();
        if(StringUtils.isNotEmpty(customerInfo.getEarnestStatus())){
            queryFilterList.addFilter("earnestStatus","eq",customerInfo.getEarnestStatus());
        }
        if (StringUtils.isNotEmpty(customerInfo.getEarnestDirection())){
            queryFilterList.addFilter("earnestDirection","eq",customerInfo.getEarnestDirection());
        }
        queryFilterList.addFilter("signStatus","eq","1");
        queryFilterList.addFilter("earnestStatus","in",earnestStatus);
        return customerInfoRepository.read(queryFilterList,pager);
    }

    @Override
    public CustomerInfo queryCustomerInfo(String id) {
        return customerInfoRepository.read(id);
    }

    @Transactional
    @Override
    public void removeOneCustomerInfo(String id) {
        customerInfoRepository.delete(id);
    }
}
