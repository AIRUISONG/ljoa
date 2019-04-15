package com.jgp.ljoa.marketing.service.impl;

import com.jgp.ljoa.channel.model.LjHouseInfo;
import com.jgp.ljoa.channel.repository.LjHouseInfoRepository;
import com.jgp.ljoa.marketing.model.MarketingChargeInfo;
import com.jgp.ljoa.marketing.model.MarketingSaleInfo;
import com.jgp.ljoa.marketing.repository.MarketingSaleInfoRepository;
import com.jgp.ljoa.marketing.service.MarketingChargeInfoService;
import com.jgp.ljoa.marketing.service.MarketingSaleInfoService;
import com.jgp.sys.query.OrderDirection;
import com.jgp.sys.query.OrderList;
import com.jgp.sys.query.QueryFilterList;
import com.jgp.sys.ui.Pager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created by Administrator on 2018/7/5 0005.
 */
@Service
public class MarketingSaleInfoServiceImpl implements MarketingSaleInfoService{
    @Autowired
    private MarketingSaleInfoRepository marketingSaleInfoRepository;
    @Autowired
    private LjHouseInfoRepository ljHouseInfoRepository;
    @Autowired
    private MarketingChargeInfoService marketingChargeInfoService;


    @Override
    public List<LjHouseInfo> queryLjHouseInfo(LjHouseInfo ljHouseInfo,Pager pager) {
        QueryFilterList queryFilterList = new QueryFilterList();
        if(StringUtils.isNotEmpty(ljHouseInfo.getProjectUuid())){
            queryFilterList.addFilter("projectUuid","eq",ljHouseInfo.getProjectUuid());
        }
        if(StringUtils.isNotEmpty(ljHouseInfo.getRoomType())){
            queryFilterList.addFilter("roomType","eq",ljHouseInfo.getRoomType());
        }
        queryFilterList.addFilter("status","eq","2");
        queryFilterList.addFilter("houseType","eq","2");
        return ljHouseInfoRepository.read(queryFilterList,pager);
    }


    @Override
    public List<MarketingSaleInfo> queryMarketingSaleInfoByHouseUuid(String hid) {
        QueryFilterList queryFilterList = new QueryFilterList();
        queryFilterList.addFilter("houseUuid","eq",hid);
        return marketingSaleInfoRepository.read(queryFilterList);
    }

    @Override
    public MarketingSaleInfo queryMarketingSaleInfoByUuid(String hid) {
        QueryFilterList queryFilterList = new QueryFilterList();
        queryFilterList.addFilter("houseUuid","eq",hid);
        List<MarketingSaleInfo> read = marketingSaleInfoRepository.read(queryFilterList);
        MarketingSaleInfo marketingSaleInfo=new MarketingSaleInfo();
        if(read.size()>0){
            marketingSaleInfo=read.get(0);
        }
        return marketingSaleInfo;
    }

    @Transactional
    @Override
    public MarketingSaleInfo saveMarketingSaleInfo(MarketingSaleInfo marketingSaleInfo) {
        return marketingSaleInfoRepository.createOrUpdate(marketingSaleInfo);
    }

    @Override
    public List<MarketingSaleInfo> queryQueryFilterList(QueryFilterList list) {

        return marketingSaleInfoRepository.read(list);
    }


    @Override
    public MarketingSaleInfo queryOneMarketingSaleInfo(String id) {
        return marketingSaleInfoRepository.read(id);
    }


    @Override
    public List<LjHouseInfo> QueryGeneralManagerQueryMarketingHouseSaleInfo(String projectId, LjHouseInfo ljHouseInfo, Pager pager) {
        QueryFilterList queryFilterList = new QueryFilterList();
        if (org.apache.commons.lang3.StringUtils.isNotEmpty(ljHouseInfo.getRoomType()) && Objects.nonNull(ljHouseInfo.getRoomType())){
            queryFilterList.addFilter("roomType","eq",ljHouseInfo.getRoomType());
        }
        queryFilterList.addFilter("projectUuid","eq",projectId);//项目主键
        queryFilterList.addFilter("status","eq","2");//已售
        queryFilterList.addFilter("houseType","eq","2");//营销
        List<LjHouseInfo> read = ljHouseInfoRepository.read(queryFilterList, pager);
        List<LjHouseInfo> collect = read.stream().filter(houseInfo -> {
            QueryFilterList list = new QueryFilterList();
            list.addFilter("houseUuid", "eq", houseInfo.getId());
            list.addFilter("chargeStatus", "eq", "6");//副总审核通过
            List<MarketingSaleInfo> read1 = marketingSaleInfoRepository.read(list);
            if (Objects.nonNull(read1) && read1.size() > 0) {
                return true;
            } else {
                return false;
            }
        }).collect(Collectors.toList());
        return collect;
    }


    @Override
    public List<LjHouseInfo> queryMarketingDutyManQueryHouseSaleInfo(LjHouseInfo ljHouseInfo, Pager pager) {
//        String sql = "";
//        sql += " status = '1'";
//        sql +=" and house_type = '2'";
        QueryFilterList queryFilterList = new QueryFilterList();
        if (org.apache.commons.lang3.StringUtils.isNotEmpty(ljHouseInfo.getRoomType()) && Objects.nonNull(ljHouseInfo.getRoomType())){
//            sql += " and room_type = '" + ljHouseInfo.getRoomType() + "'";
            queryFilterList.addFilter("roomType","eq",ljHouseInfo.getRoomType());
        }
        if(org.apache.commons.lang3.StringUtils.isNotEmpty(ljHouseInfo.getProjectUuid())){
//            sql += " and project_uuid = '"+ljHouseInfo.getProjectUuid()+"'";
            queryFilterList.addFilter("projectUuid","eq",ljHouseInfo.getProjectUuid());
        }
        if(org.apache.commons.lang3.StringUtils.isNotEmpty(ljHouseInfo.getBuildingNo())){
//            sql += " and building_no like '"+ljHouseInfo.getBuildingNo()+"'";
            queryFilterList.addFilter("buildingNo","like",ljHouseInfo.getBuildingNo());
        }
        if(org.apache.commons.lang3.StringUtils.isNotEmpty(ljHouseInfo.getUnitNo())){
//            sql += " and unit_no like '"+ljHouseInfo.getUnitNo()+"'";
            queryFilterList.addFilter("unitNo","like",ljHouseInfo.getUnitNo());
        }
        if(org.apache.commons.lang3.StringUtils.isNotEmpty(ljHouseInfo.getRoomNo())){
//            sql +=" and room_no like '"+ljHouseInfo.getRoomNo()+"'";
            queryFilterList.addFilter("roomNo","like",ljHouseInfo.getRoomNo());
        }

        queryFilterList.addFilter("status","eq","1");//未售
        queryFilterList.addFilter("houseType","eq","2");//营销
        /*queryFilterList.addFilter("saleMoney","isn");//销售价格
        queryFilterList.addFilter("minMoney","isn");//销售底价
        queryFilterList.addFilter("premium","isn");//溢出金额*/
        OrderList orders = new OrderList();
        orders.addOrder("buildingNo", OrderDirection.ASC);
        orders.addOrder("unitNo", OrderDirection.ASC);
        orders.addOrder("roomNo", OrderDirection.ASC);
/*        sql += " order by building_no DESC,unit_no DESC,room_no DESC";
        String select = "select * from lj_house_info where ";
        String count = "select count(id) from lj_house_info where";
        List<LjHouseInfo> ljHouseInfos = ljHouseInfoRepository.readNativeSql(select + sql, count+sql, pager);*/
        List<LjHouseInfo> read = ljHouseInfoRepository.read(queryFilterList,orders, pager);
        return read;
    }

    @Override
    public List<MarketingSaleInfo> queryAllMarketingSaleInfo() {

        return   marketingSaleInfoRepository.readAll();
    }

    @Transactional(readOnly = true)
    @Override
    public List<LjHouseInfo> queryLjHouseInfoAssistant(LjHouseInfo ljHouseInfo, Pager pager) {
        List<MarketingChargeInfo> marketingChargeInfos = marketingChargeInfoService.queryAllMarketingChargeInfo();
        List<String> collect = marketingChargeInfos.stream().filter(marketingChargeInfo -> {
            return true;
        }).map(marketingChargeInfo -> {
            return marketingChargeInfo.getHouseUuid();
        }).collect(Collectors.toList());
        QueryFilterList queryFilterList = new QueryFilterList();
        List<LjHouseInfo> ljHouseInfos = new ArrayList<>();
        if (collect.size()>0){
            queryFilterList.addFilter("id","in",collect);
            if(StringUtils.isNotEmpty(ljHouseInfo.getProjectUuid())){
                queryFilterList.addFilter("projectUuid","eq",ljHouseInfo.getProjectUuid());
            }
            if(StringUtils.isNotEmpty(ljHouseInfo.getRoomType())){
                queryFilterList.addFilter("roomType","eq",ljHouseInfo.getRoomType());
            }
//            queryFilterList.addFilter("saleMoney","isnn");//销售总监已定价
            ljHouseInfos = ljHouseInfoRepository.read(queryFilterList,pager);
        }
        return ljHouseInfos;
    }

    //已审批
    @Transactional(readOnly = true)
    @Override
    public List<LjHouseInfo> queryLjHouseInfoManager1(LjHouseInfo ljHouseInfo, Pager pager) {
        List<MarketingChargeInfo> marketingChargeInfos = marketingChargeInfoService.queryAllMarketingChargeInfo();
        List<String> collect = marketingChargeInfos.stream().filter(marketingChargeInfo -> {
            if (("2".equals(marketingChargeInfo.getChargeType())&&"4".equals(marketingChargeInfo.getChargeStatus()))
                    ||("5".equals(marketingChargeInfo.getChargeType())&&"4".equals(marketingChargeInfo.getChargeStatus()))){
                return true;
            }else{
                return false;
            }
        }).map(marketingChargeInfo -> {
            return marketingChargeInfo.getHouseUuid();
        }).collect(Collectors.toList());
        QueryFilterList queryFilterList = new QueryFilterList();
        List<LjHouseInfo> ljHouseInfos = new ArrayList<>();
        if (collect.size()>0){
            queryFilterList.addFilter("id","in",collect);
            if(StringUtils.isNotEmpty(ljHouseInfo.getProjectUuid())){
                queryFilterList.addFilter("projectUuid","eq",ljHouseInfo.getProjectUuid());
            }
            if(StringUtils.isNotEmpty(ljHouseInfo.getRoomType())){
                queryFilterList.addFilter("roomType","eq",ljHouseInfo.getRoomType());
            }
            ljHouseInfos = ljHouseInfoRepository.read(queryFilterList,pager);
        }

        return ljHouseInfos;
    }

    //待审核
    @Transactional(readOnly = true)
    @Override
    public List<LjHouseInfo> queryLjHouseInfoManager(LjHouseInfo ljHouseInfo, Pager pager) {
        List<MarketingChargeInfo> marketingChargeInfos = marketingChargeInfoService.queryAllMarketingChargeInfo();
        List<String> collect = marketingChargeInfos.stream().filter(marketingChargeInfo -> {
            if (("2".equals(marketingChargeInfo.getChargeType())&&("0".equals(marketingChargeInfo.getChargeStatus()) ||"11".equals(marketingChargeInfo.getChargeStatus())))
                    ||("5".equals(marketingChargeInfo.getChargeType())&&("0".equals(marketingChargeInfo.getChargeStatus()) ||"11".equals(marketingChargeInfo.getChargeStatus())))
                    ||"3".equals(marketingChargeInfo.getChargeStatus())||"5".equals(marketingChargeInfo.getChargeStatus())||"7".equals(marketingChargeInfo.getChargeStatus())
                    ||"9".equals(marketingChargeInfo.getChargeStatus())||"11".equals(marketingChargeInfo.getChargeStatus())){
                return true;
            }else{
                return false;
            }
        }).map(marketingChargeInfo -> {
            return marketingChargeInfo.getHouseUuid();
        }).collect(Collectors.toList());
        QueryFilterList queryFilterList = new QueryFilterList();
        List<LjHouseInfo> ljHouseInfos = new ArrayList<>();
        if (collect.size()>0){
            queryFilterList.addFilter("id","in",collect);
            if(StringUtils.isNotEmpty(ljHouseInfo.getProjectUuid())){
                queryFilterList.addFilter("projectUuid","eq",ljHouseInfo.getProjectUuid());
            }
            if(StringUtils.isNotEmpty(ljHouseInfo.getRoomType())){
                queryFilterList.addFilter("roomType","eq",ljHouseInfo.getRoomType());
            }
            ljHouseInfos = ljHouseInfoRepository.read(queryFilterList,pager);
        }

        return ljHouseInfos;
    }
}
