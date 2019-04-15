package com.jgp.ljoa.com.controller;

import com.jgp.common.annotation.UIParam;
import com.jgp.ljoa.channel.model.LjHouseInfo;
import com.jgp.ljoa.channel.service.LjHouseInfoService;
import com.jgp.ljoa.marketing.model.HouseReturn;
import com.jgp.ljoa.marketing.service.HouseReturnService;
import com.jgp.sys.controller.JGPController;
import com.jgp.sys.ui.GridResult;
import com.jgp.sys.ui.Pager;
import com.jgp.sys.ui.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/9/25.
 */
/*
作者  SSF
时间   2018/9/25
*/
@RestController
@RequestMapping("/ljoa/com/LjHouseInfoZJLApiController")
public class LjHouseInfoZJLApiController extends JGPController {
    @Autowired
    private LjHouseInfoService service;
    @Autowired
    private HouseReturnService houseReturnService;


    @RequestMapping("listGroupLjHouseInfoQUDAO")//渠道已售房源
    public Result listGroupLjHouseInfoQUDAO( LjHouseInfo ljHouseInfo,@UIParam("pager")Pager pager){
        ljHouseInfo.setStatus("2");//已售
        ljHouseInfo.setHouseType("1");//渠道
        List<LjHouseInfo> ljHouseInfos = service.queryGroupLjHouseInfoINFO(ljHouseInfo, pager);
        List<GridResult.Category> categories = new ArrayList<>();
        categories.add(new GridResult.Category("楼号", "buildingNo"));
        categories.add(new GridResult.Category("单元", "unitNo"));
        categories.add(new GridResult.Category("房号", "roomNo"));
        categories.add(new GridResult.Category("面积(㎡)", "roomArea"));
        categories.add(new GridResult.Category("产品分类", "roomType","LJOA.CHANNEL.ROOM_TYPE"));
        categories.add(new GridResult.Category("其他分类", "otherRoomType"));
        categories.add(new GridResult.Category("房源归属", "houseType","LJOA.CHANNEL.HOUSETYPE"));
        categories.add(new GridResult.Category("销售状态", "status","LJOA.CHANNEL.ROOM_STATUS"));
        categories.add(new GridResult.Category("公司佣金状态", "companyChargeStatus","LJOA.CHANNEL.COMPANY_CHARGE_STATUS"));
        return ajaxReGrid("gdata",categories,ljHouseInfos,pager);
    }

    @RequestMapping("listGroupLjHouseInfoYINGXIAO")//营销已售房源
    public Result listGroupLjHouseInfoYINGXIAO(LjHouseInfo ljHouseInfo,@UIParam("pager")Pager pager){

        ljHouseInfo.setStatus("2");//已售
        ljHouseInfo.setHouseType("2");//营销
        List<LjHouseInfo> ljHouseInfos = service.queryGroupLjHouseInfoINFO(ljHouseInfo, pager);
        List<GridResult.Category> categories = new ArrayList<>();
        categories.add(new GridResult.Category("楼号", "buildingNo"));
        categories.add(new GridResult.Category("单元", "unitNo"));
        categories.add(new GridResult.Category("房号", "roomNo"));
        categories.add(new GridResult.Category("面积(㎡)", "roomArea"));
        categories.add(new GridResult.Category("产品分类", "roomType","LJOA.CHANNEL.ROOM_TYPE"));
        categories.add(new GridResult.Category("销售状态", "status","LJOA.CHANNEL.ROOM_STATUS"));
        return ajaxReGrid("gdata",categories,ljHouseInfos,pager);
    }

    @RequestMapping("listGroupLjHouseInfoQDYJY")//渠道已结佣房源信息
    public Result listGroupLjHouseInfoQDYJY(LjHouseInfo ljHouseInfo,@UIParam("pager")Pager pager){

        ljHouseInfo.setStatus("2");//已售
        ljHouseInfo.setHouseType("1");//渠道
        ljHouseInfo.setCompanyChargeStatus("2");//公司佣金状态已结佣
        List<LjHouseInfo> ljHouseInfos = service.queryGroupLjHouseInfoINFO(ljHouseInfo, pager);
        List<GridResult.Category> categories = new ArrayList<>();
        categories.add(new GridResult.Category("楼号", "buildingNo"));
        categories.add(new GridResult.Category("单元", "unitNo"));
        categories.add(new GridResult.Category("房号", "roomNo"));
        categories.add(new GridResult.Category("面积(㎡)", "roomArea"));
        categories.add(new GridResult.Category("产品分类", "roomType","LJOA.CHANNEL.ROOM_TYPE"));
        categories.add(new GridResult.Category("其他分类", "otherRoomType"));
        categories.add(new GridResult.Category("房源归属", "houseType","LJOA.CHANNEL.HOUSETYPE"));
        categories.add(new GridResult.Category("销售状态", "status","LJOA.CHANNEL.ROOM_STATUS"));
        categories.add(new GridResult.Category("公司佣金状态", "companyChargeStatus","LJOA.CHANNEL.COMPANY_CHARGE_STATUS"));
        return ajaxReGrid("gdata",categories,ljHouseInfos,pager);
    }

    @RequestMapping("listGroupLjHouseInfoYXYJY")//营销已结佣房源
    public Result listGroupLjHouseInfoYXYJY(LjHouseInfo ljHouseInfo,@UIParam("pager")Pager pager){

        ljHouseInfo.setStatus("2");//已售
        ljHouseInfo.setHouseType("2");//营销
        ljHouseInfo.setCompanyChargeStatus("2");//公司佣金状态已结佣
        List<LjHouseInfo> ljHouseInfos = service.queryGroupLjHouseInfoINFO(ljHouseInfo, pager);
        List<GridResult.Category> categories = new ArrayList<>();
        categories.add(new GridResult.Category("楼号", "buildingNo"));
        categories.add(new GridResult.Category("单元", "unitNo"));
        categories.add(new GridResult.Category("房号", "roomNo"));
        categories.add(new GridResult.Category("面积(㎡)", "roomArea"));
        categories.add(new GridResult.Category("产品分类", "roomType","LJOA.CHANNEL.ROOM_TYPE"));
        categories.add(new GridResult.Category("销售状态", "status","LJOA.CHANNEL.ROOM_STATUS"));
        categories.add(new GridResult.Category("公司佣金状态", "companyChargeStatus","LJOA.CHANNEL.COMPANY_CHARGE_STATUS"));
        return ajaxReGrid("gdata",categories,ljHouseInfos,pager);
    }


    @RequestMapping("listGroupHouseReturnYES")//已退款退房信息
    public Result listGroupHouseReturnYES(HouseReturn houseReturn, @UIParam("pager")Pager pager){
        houseReturn.setMoneyStatus("2");//已退款
        List<HouseReturn> houseReturns = houseReturnService.queryGroupHouseReturn(houseReturn, pager);

        List<GridResult.Category> categories = new ArrayList<>();
        categories.add(new GridResult.Category("姓名", "applyName"));
        categories.add(new GridResult.Category("申请填报人", "inputMan"));
        categories.add(new GridResult.Category("退款金额", "returnMoney"));
        categories.add(new GridResult.Category("申请日期", "applyDate"));
        categories.add(new GridResult.Category("审核状态", "approvalStatus","LJ_HOUSE_RETURN.APPROVAL_STATUS"));
        categories.add(new GridResult.Category("退款状态", "moneyStatus","LJ_HOUSE_RETURN.MONEY_STATUS"));
        return ajaxReGrid("gdata", categories, houseReturns, pager);
    }

}
