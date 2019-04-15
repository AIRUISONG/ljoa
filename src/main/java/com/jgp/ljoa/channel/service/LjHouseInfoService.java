package com.jgp.ljoa.channel.service;

import com.jgp.ljoa.channel.model.LjHouseInfo;
import com.jgp.sys.ui.Pager;

import java.io.File;
import java.util.List;

/**
 * Created by Administrator on 2018/7/3.
 */
/*
作者  SSF
时间   2018/7/3
*/
//渠道房源信息
public interface LjHouseInfoService {

    //通过项目主键和房源归属查找房源信息
    List<LjHouseInfo> queryGroupLjHouseInfoByUUId(String uuid,LjHouseInfo ljHouseInfo,String houseType,Pager pager);
    List<LjHouseInfo> queryGroupLjHouseInfo(String uuid,LjHouseInfo ljHouseInfo,Pager pager);
    //通过项目主键查
    List<LjHouseInfo> queryGroupLjHouseInfoByUUID(String uuid);
    List<LjHouseInfo> queryGroupLjHouseInfoByUUID(String uuid,LjHouseInfo ljHouseInfo,Pager pager);
    //保存
    LjHouseInfo saveLjHouseInfo(LjHouseInfo ljHouseInfo);
    //单查
    LjHouseInfo queryOneLjHouseInfoByid(String id);
    //删除
    void removeLjHouseInfo(String id);
    void remoueGroupLjHouseInfo(List<LjHouseInfo> ljHouseInfos);
    //全查
    List<LjHouseInfo> queryAllLjHouseInfo(Pager pager);
    //通过状态查询
    List<LjHouseInfo> queryGrouypLjHouseInfoByStatus(String status,LjHouseInfo ljHouseInfo,Pager pager);
    List<LjHouseInfo> queryGrouypLjHouseInfoByGroupStatus(List<String> ljHouseInfos,LjHouseInfo ljHouseInfo,Pager pager);
    //通过房源归属
    List<LjHouseInfo> queryGroupLjHouseInfoByHouseType(String houseType);
    List<LjHouseInfo> queryHouseInfoSold(LjHouseInfo ljHouseInfo,Pager pager);//查找已售房源
    List<LjHouseInfo> queryGroupHouseInfoSold(String uuid,LjHouseInfo ljHouseInfo, String houseType,Pager pager);//查找未售房源
    List<LjHouseInfo> queryGroupHouseInfoSoldByDateTime(String uuid,LjHouseInfo ljHouseInfo, String houseType,Pager pager);
    LjHouseInfo queryOneHouseInfo(String id);//通过id查找房源


    /*
    * 总经理查询所有房源信息
    * */
    List<LjHouseInfo> queryGeneralManagerQueryHouseSaleInfo(LjHouseInfo ljHouseInfo, Pager pager);

    /*
    * 财务查询所有可结佣的房源信息
    * */
    List<LjHouseInfo> queryExpenseMaidHouseInfo(LjHouseInfo ljHouseInfo, Pager pager);

    List<LjHouseInfo> queryHouseInfoByStatus(LjHouseInfo ljHouseInfo,Pager pager);//根据房源状态“未售”查询房源

    List<LjHouseInfo> queryGroupLjHouseInfo(LjHouseInfo ljHouseInfo,Pager pager);// 查询已结佣房源

    List<LjHouseInfo> queryGroupLjHouseInfoINFO(LjHouseInfo ljHouseInfo,Pager pager);// 查询已结佣房源

    /*
    * 以年为单位查询房源信息
    * */
    List<LjHouseInfo> queryGroupHouseInfo(LjHouseInfo ljHouseInfo);

    /*
    * 查询可退房的房源
    * */
    List<LjHouseInfo> queryAllHouseInfoForHouseReturn(LjHouseInfo ljHouseInfo, Pager pager);

    //查询已售、未结佣、营销/渠道的房源，来计算应收款
    List<LjHouseInfo> queryGroupHouseInfoToCount(LjHouseInfo ljHouseInfo);

    /**
     * 导入渠道房源信息
     * @param file
     */
    public void importData(File file) throws Exception;

    /**
     * 导入营销房源信息
     * @param file
     */
    public void importMarketingHouse(File file) throws Exception;
    /**
     * 营销批量定价
     * @param file
     */
    public void importMarketingHouseMoney(File file) throws Exception;

    //根据月份和项目id查找
    List<LjHouseInfo> queryGroupHouseMonth(String projectUuid,Integer month);

    /**
     * 根据房号查询
     * @param projectUuid
     * @param buildingNo
     * @param unitNo
     * @param roomNo
     * @return
     */
    public LjHouseInfo queryByHousNo(String projectUuid, String buildingNo, String unitNo, String roomNo);
}
