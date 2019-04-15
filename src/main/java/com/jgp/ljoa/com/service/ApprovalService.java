package com.jgp.ljoa.com.service;/**
 * Created by Administrator on 2018/7/5.
 */

import com.jgp.ljoa.channel.model.LjHouseInfo;
import com.jgp.ljoa.com.model.Approval;
import com.jgp.sys.ui.Pager;

import java.util.List;

/**
 * 项目   ljoa
 * 作者   liujinxu
 * 时间   2018/7/5
 */
public interface ApprovalService {

    /*
    * 条件查询审批
    * */
    List<Approval> queryGroupApproval(Approval approval, Pager pager);

    /*
    * 条件查询审批
    * */
    Approval queryOneApproval(Approval approval);

    /*
    * 根据业务主键查询审批
    * */
    Approval queryOneApprovalByBusiUuid(String busiUuid);

    /*
    * 保存审批
    * */
    Approval saveApproval(Approval approval);

    /*
    * 根据关联主键和类型查找
    * */
    Approval queryOneApprovalByUuidAndCheckType(String uuid, String checkType,String checkContent);

    /*
    * 根据业务主键查询审批
    * */
    List<Approval> queryApprovalByBusiUuid(String busiUuid, Pager pager);

    /*
    * 总经理查询所有房屋销售信息
    * */
    List<LjHouseInfo> queryGeneralManagerQueryHouseSaleInfo(LjHouseInfo ljHouseInfo, Pager pager);
    /*
    * 单查
    * */
    Approval queryOneApprovalById(String id);


    void removeApproval(List<Approval> h);
}
