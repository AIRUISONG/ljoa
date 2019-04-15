package com.jgp.ljoa.marketing.service.impl;

import com.jgp.ljoa.channel.model.LjHouseInfo;
import com.jgp.ljoa.channel.service.LjHouseInfoService;
import com.jgp.ljoa.marketing.model.MarketingChargeInfo;
import com.jgp.ljoa.marketing.repository.MarketingChargeInfoRepository;
import com.jgp.ljoa.marketing.service.MarketingChargeInfoService;
import com.jgp.sys.query.QueryFilterList;
import com.jgp.sys.ui.Pager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created by Administrator on 2018/7/10 0010.
 */
@Service
public class MarketingChargeInfoServiceImpl implements MarketingChargeInfoService{
    @Autowired
    private MarketingChargeInfoRepository marketingChargeInfoRepository;
    @Autowired
    private LjHouseInfoService ljHouseInfoService;


    @Override
    public List<LjHouseInfo> queryHouseInfoAdviser(LjHouseInfo ljHouseInfo,Pager pager) {
        ljHouseInfo.setCompanyChargeStatus("2");//公司状态已结佣
        ljHouseInfo.setHouseType("2");//房源归属为营销
        List<LjHouseInfo> ljHouseInfos = ljHouseInfoService.queryGroupLjHouseInfo(ljHouseInfo, pager);
        List<LjHouseInfo> collect = ljHouseInfos.stream().filter(ljHouseInfo1 -> {
            QueryFilterList list = new QueryFilterList();
            list.addFilter("houseUuid", "eq", ljHouseInfo.getId());
            list.addFilter("chargeType", "eq", "1");//佣金类型为置业顾问
            List<MarketingChargeInfo> read = marketingChargeInfoRepository.read(list);
            if (read.size() == 0) {
                return true;
            } else if (read.size() == 1 && "03".equals(read.get(0).getChargeStatus())) {//经理审核不通过
                return true;
            }
            return false;
        }).collect(Collectors.toList());
        return collect;
    }


    @Override
    public List<MarketingChargeInfo> queryGroupMarketingChargeInfo(String hid,List<String> list, Pager pager) {
        QueryFilterList queryFilterList = new QueryFilterList();
        queryFilterList.addFilter("houseUuid","eq",hid);
        if(Objects.nonNull(list)){
            queryFilterList.addFilter("chargeType","in",list);
        }
        return marketingChargeInfoRepository.read(queryFilterList,pager);
    }

    @Transactional
    @Override
    public MarketingChargeInfo saveMarketingChargeInfo(MarketingChargeInfo marketingChargeInfo) {
        return marketingChargeInfoRepository.createOrUpdate(marketingChargeInfo);
    }


    @Override
    public List<MarketingChargeInfo> queryAllMarketingChargeInfo() {
        return marketingChargeInfoRepository.readAll();
    }


    @Override
    public MarketingChargeInfo queryOneMarketingChargeInfo(String id) {
        return marketingChargeInfoRepository.read(id);
    }

    @Transactional
    @Override
    public void removeMarketingChargeInfo(String id) {
        marketingChargeInfoRepository.delete(id);
    }

    @Override
    public List<MarketingChargeInfo> queryGroupMarketingChargeInfoByUUid(String uuid) {
        QueryFilterList queryFilterList = new QueryFilterList();
        queryFilterList.addFilter("houseUuid","eq",uuid);

        return  marketingChargeInfoRepository.read(queryFilterList);
    }

    @Override
    public MarketingChargeInfo queryOneMarketingChargeInfoByChargeType(String chargeType, String houseUuid) {
        QueryFilterList queryFilterList = new QueryFilterList();
        queryFilterList.addFilter("houseUuid","eq",houseUuid);
        queryFilterList.addFilter("chargeType","eq",chargeType);
        List<MarketingChargeInfo> read = marketingChargeInfoRepository.read(queryFilterList);
        MarketingChargeInfo marketingChargeInfo=new MarketingChargeInfo();
        if(read.size()>0){
            marketingChargeInfo=read.get(0);
        }
        return marketingChargeInfo;
    }

    @Override
    public MarketingChargeInfo queryMarketingChargeInfoByChargeTarget(String id, MarketingChargeInfo marketingChargeInfo, Pager pager) {
        QueryFilterList queryFilterList = new QueryFilterList();
        queryFilterList.addFilter("houseUuid","eq",id);
        queryFilterList.addFilter("chargeType","eq","5");
        if(StringUtils.isNotEmpty(marketingChargeInfo.getChargeTargetUuid())){
            queryFilterList.addFilter("chargeTargetUuid","eq",marketingChargeInfo.getChargeTargetUuid());
        }
        List<MarketingChargeInfo> read = marketingChargeInfoRepository.read(queryFilterList, pager);
        MarketingChargeInfo marketingChargeInfo1=new MarketingChargeInfo();
        if(read.size()>0){
            marketingChargeInfo1=read.get(0);
        }
        return marketingChargeInfo1;
    }
}
