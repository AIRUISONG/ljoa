package com.jgp.ljoa.marketing.service;

import com.jgp.ljoa.channel.model.LjHouseInfo;
import com.jgp.ljoa.marketing.model.MarketingChargeInfo;
import com.jgp.sys.ui.Pager;

import java.util.List;

/**
 * Created by Administrator on 2018/7/10 0010.
 */
public interface MarketingChargeInfoService {
    List<LjHouseInfo> queryHouseInfoAdviser(LjHouseInfo ljHouseInfo,Pager pager);// 查询已结佣(顾问佣金录入)
    List<MarketingChargeInfo> queryGroupMarketingChargeInfo(String hid,List<String> list,Pager pager);//条件查询佣金
    MarketingChargeInfo saveMarketingChargeInfo(MarketingChargeInfo marketingChargeInfo);//保存佣金信息
    List<MarketingChargeInfo> queryAllMarketingChargeInfo();//查询所有佣金信息
    MarketingChargeInfo queryOneMarketingChargeInfo(String id);//根据id查单个
    void removeMarketingChargeInfo(String id);//删除单个佣金信息
    List<MarketingChargeInfo> queryGroupMarketingChargeInfoByUUid(String uuid);//通过房源主键查询
    MarketingChargeInfo queryOneMarketingChargeInfoByChargeType(String chargeType,String houseUuid);//佣金类型 查询
    MarketingChargeInfo queryMarketingChargeInfoByChargeTarget(String id,MarketingChargeInfo marketingChargeInfo,Pager pager);//渠道对象筛查
}
