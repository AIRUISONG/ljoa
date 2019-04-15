package com.jgp.ljoa.marketing.service;

import com.jgp.ljoa.channel.model.LjHouseInfo;
import com.jgp.ljoa.marketing.model.CustomerInfo;
import com.jgp.sys.ui.Pager;

import java.util.List;

/**
 * Created by Administrator on 2018/7/4 0004.
 */
public interface CustomerInfoService {

    CustomerInfo saveCustomerInfo(CustomerInfo customerInfo);//保存客户录入信息
    CustomerInfo saveNetSigning(CustomerInfo customerInfo);//保存网签并修改房源为“已售”
    CustomerInfo queryCustomerInfoByHouseUuid(String hid);//根据房源id查找客户信息(一个房源对应一个客户信息)
    List<LjHouseInfo> queryHouseInfoByCustomerInfoSignStatus(LjHouseInfo ljHouseInfo,Pager pager);//查询未网签房源
    List<CustomerInfo> queryCustomerInfoBySignStatus(CustomerInfo customerInfo,Pager pager);//查询未网签客户
    CustomerInfo queryCustomerInfo(String id);//根据客户id查找客户信息

    /*
    * 删除单个
    * */
    void removeOneCustomerInfo(String id);
}
