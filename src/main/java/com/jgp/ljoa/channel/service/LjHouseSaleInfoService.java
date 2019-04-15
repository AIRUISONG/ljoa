package com.jgp.ljoa.channel.service;

import com.jgp.ljoa.channel.model.LjHouseSaleInfo;
import com.jgp.sys.query.QueryFilterList;
import com.jgp.sys.ui.Pager;

import java.util.List;

/**
 * Created by Administrator on 2018/7/3.
 */
/*
作者  SSF
时间   2018/7/3
*/
//房源销售信息
public interface LjHouseSaleInfoService {
    //通过房源查
    LjHouseSaleInfo  queryOneLjHouseSaleInfoBuUUid(String houseUuid);
    //单查
    LjHouseSaleInfo queryOneLjHouseSaleInfo(String id);
    //全查
    List<LjHouseSaleInfo> queryAllLjHouseSaleInfo();
    //保存
    LjHouseSaleInfo saveLjHouseSaleInfo(LjHouseSaleInfo ljHouseSaleInfo);
    //删除
    void removeLjHouseSaleInfo(String id);
    //条件查询
    List<LjHouseSaleInfo> queryQueryFilterList(QueryFilterList list);
    //条件ID查询
    LjHouseSaleInfo queryOneLjHouseSaleInfoByType(String id,LjHouseSaleInfo ljHouseSaleInfo,Pager pager);


}
