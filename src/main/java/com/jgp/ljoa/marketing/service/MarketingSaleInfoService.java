package com.jgp.ljoa.marketing.service;

import com.jgp.ljoa.channel.model.LjHouseInfo;
import com.jgp.ljoa.marketing.model.MarketingSaleInfo;
import com.jgp.sys.query.QueryFilterList;
import com.jgp.sys.ui.Pager;

import java.util.List;

/**
 * Created by Administrator on 2018/7/5 0005.
 */
public interface MarketingSaleInfoService {
    List<LjHouseInfo> queryLjHouseInfo(LjHouseInfo ljHouseInfo,Pager pager);//查询已售房源
    List<MarketingSaleInfo> queryMarketingSaleInfoByHouseUuid(String hid);//根据房源id查找销售信息
    MarketingSaleInfo queryMarketingSaleInfoByUuid(String hid);//根据房源id查找单个销售信息
    MarketingSaleInfo saveMarketingSaleInfo(MarketingSaleInfo marketingSaleInfo);//保存销售信息
    //条件查询
    List<MarketingSaleInfo> queryQueryFilterList(QueryFilterList list);
    /*
    * 根据id查询营销销售信息
    * */
    MarketingSaleInfo queryOneMarketingSaleInfo(String id);

    /*
     * 总经理查询营销房屋销售信息
     * */
    List<LjHouseInfo> QueryGeneralManagerQueryMarketingHouseSaleInfo(String projectId, LjHouseInfo ljHouseInfo, Pager pager);

    /*
    * 营销总监查询可定价房源信息
    * */
    List<LjHouseInfo> queryMarketingDutyManQueryHouseSaleInfo(LjHouseInfo ljHouseInfo, Pager pager);

    List<MarketingSaleInfo> queryAllMarketingSaleInfo();//查询所有

    List<LjHouseInfo> queryLjHouseInfoAssistant(LjHouseInfo ljHouseInfo,Pager pager);//查询助手已填完销售信息的房源
    List<LjHouseInfo> queryLjHouseInfoManager(LjHouseInfo ljHouseInfo,Pager pager);//查询经理已填完销售信息的房源未审批
    List<LjHouseInfo> queryLjHouseInfoManager1(LjHouseInfo ljHouseInfo,Pager pager);//查询经理已填完销售信息的房源已审批
}
