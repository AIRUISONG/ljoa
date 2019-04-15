package com.jgp.ljoa.expense.controller;

import com.jgp.common.annotation.UIParam;
import com.jgp.ljoa.channel.model.LjHouseInfo;
import com.jgp.ljoa.channel.model.LjHouseSaleInfo;
import com.jgp.ljoa.channel.model.LjProjectInfo;
import com.jgp.ljoa.channel.service.LjHouseInfoService;
import com.jgp.ljoa.channel.service.LjHouseSaleInfoService;
import com.jgp.ljoa.com.model.Approval;
import com.jgp.ljoa.com.service.ApprovalService;
import com.jgp.ljoa.marketing.model.MarketingSaleInfo;
import com.jgp.ljoa.marketing.service.MarketingSaleInfoService;
import com.jgp.sys.controller.JGPController;
import com.jgp.sys.query.QueryFilterList;
import com.jgp.sys.ui.GridResult;
import com.jgp.sys.ui.Pager;
import com.jgp.sys.ui.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Administrator on 2018/7/9.
 */
/*
作者  SSF
时间   2018/7/9
*/
@RestController
@RequestMapping("/ljoa/expense/ljExpenseYJSPApiController")
public class LjExpenseYJSPApiController extends JGPController {
    @Autowired
    private LjHouseInfoService ljHouseInfoService;
    @Autowired
    private LjHouseSaleInfoService ljHouseSaleInfoService;//渠道房源销售信息
    @Autowired
    private MarketingSaleInfoService marketingSaleInfoService;//营销房源销售信息表
    @Autowired
    private ApprovalService approvalService;


    @RequestMapping("listGroupLjExpenseYJSPYES")//已售房源中部门审核通过副总审核不通过的（财务已审核的）
    public Result listGroupLjExpenseYJSPYES(@UIParam("pager")Pager pager, LjHouseInfo ljHouseInfo, Model model){
        List<LjHouseInfo> ljHouseInfos = ljHouseInfoService.queryGrouypLjHouseInfoByStatus("2", ljHouseInfo, pager);//已售房源的
        List<LjHouseInfo> collect = ljHouseInfos.stream().filter(l -> {
            if ("1".equals(l.getHouseType())) {
                QueryFilterList list = new QueryFilterList();
                list.addFilter("houseUuid", "eq", l.getId());
                List<LjHouseSaleInfo> ljHouseSaleInfos = ljHouseSaleInfoService.queryQueryFilterList(list);
                if (ljHouseSaleInfos.size() > 0) {
                    if(!"0".equals(ljHouseSaleInfos.get(0).getChargeStatus()) &&
                            !"1".equals(ljHouseSaleInfos.get(0).getChargeStatus())&&
                            !"2".equals(ljHouseSaleInfos.get(0).getChargeStatus())&&
                            !"3".equals(ljHouseSaleInfos.get(0).getChargeStatus())){
                        return true;
                    }
                }
            }
            if ("2".equals(l.getHouseType())) {
                QueryFilterList list2 = new QueryFilterList();
                list2.addFilter("houseUuid", "eq", l.getId());
                List<MarketingSaleInfo> marketingSaleInfos = marketingSaleInfoService.queryQueryFilterList(list2);
                if (marketingSaleInfos.size() > 0) {
                    if(!"0".equals(marketingSaleInfos.get(0).getInsideChargeStatus())
                            && !"1".equals(marketingSaleInfos.get(0).getChanneChargeStatus())
                            && !"2".equals(marketingSaleInfos.get(0).getChanneChargeStatus())
                            && !"3".equals(marketingSaleInfos.get(0).getChanneChargeStatus())
                            && !"4".equals(marketingSaleInfos.get(0).getChanneChargeStatus())
                            && !"5".equals(marketingSaleInfos.get(0).getChanneChargeStatus())){
                        return true;
                    }
                }
            }
            return false;
        }).collect(Collectors.toList());
        List<String> idList = new ArrayList<>();
        for(LjHouseInfo ljHouseInfo1 : collect){
            idList.add(ljHouseInfo1.getId());
        }
        List<LjHouseInfo> ljHouseInfos1 = ljHouseInfoService.queryGrouypLjHouseInfoByGroupStatus(idList, ljHouseInfo, pager);
        List<GridResult.Category> categories = new ArrayList<>();
        categories.add(new GridResult.Category("房源类型", "houseType","LJOA.CHANNEL.HOUSETYPE"));
        categories.add(new GridResult.Category("项目名称", "projectUuid",LjProjectInfo.class,"projectName"));
        categories.add(new GridResult.Category("楼号", "buildingNo"));
        categories.add(new GridResult.Category("单元", "unitNo"));
        categories.add(new GridResult.Category("房号", "roomNo"));
//        categories.add(new GridResult.Category("房主", "ownerName"));
//        categories.add(new GridResult.Category("联系电话", "ownerTel"));
        categories.add(new GridResult.Category("面积(㎡)", "roomArea"));
        categories.add(new GridResult.Category("产品分类", "roomType","LJOA.CHANNEL.ROOM_TYPE"));
//        categories.add(new GridResult.Category("销售价格(元)", "saleMoney"));
        categories.add(new GridResult.Category("销售状态", "status","LJOA.CHANNEL.ROOM_STATUS"));
        categories.add(new GridResult.Category("公司佣金状态", "companyChargeStatus","LJOA.MAIDSTATUS"));
        return ajaxReGrid("gdata", categories, ljHouseInfos1, pager);
    }

    @RequestMapping("listGroupLjExpenseYJSPNO")//已售房源中部门审核通过副总审核不通过的（财务未审核的）
    public Result listGroupLjExpenseYJSPNO(@UIParam("pager")Pager pager,LjHouseInfo ljHouseInfo,Model model){
        ljHouseInfo.setCompanyChargeStatus("1");
        List<LjHouseInfo> ljHouseInfos = ljHouseInfoService.queryGrouypLjHouseInfoByStatus("2", ljHouseInfo, pager);//已售房源的
        List<LjHouseInfo> collect = ljHouseInfos.stream().filter(l -> {
            if ("1".equals(l.getHouseType())) {
                QueryFilterList list = new QueryFilterList();
                list.addFilter("houseUuid", "eq", l.getId());
                List<LjHouseSaleInfo> ljHouseSaleInfos = ljHouseSaleInfoService.queryQueryFilterList(list);
                if (ljHouseSaleInfos.size() > 0) {
                    if("2".equals(ljHouseSaleInfos.get(0).getChargeStatus())){
                        return true;
                    }
                }
            }
            if ("2".equals(l.getHouseType())) {
                QueryFilterList list2 = new QueryFilterList();
                list2.addFilter("houseUuid", "eq", l.getId());
                List<MarketingSaleInfo> marketingSaleInfos = marketingSaleInfoService.queryQueryFilterList(list2);
                if (marketingSaleInfos.size() > 0) {
                    if("4".equals(marketingSaleInfos.get(0).getInsideChargeStatus())){
                        return true;
                    }
                }
            }
            return false;
        }).collect(Collectors.toList());
        List<String> idList = new ArrayList<>();
        for(LjHouseInfo ljHouseInfo1 : collect){
            idList.add(ljHouseInfo1.getId());
        }
        List<LjHouseInfo> ljHouseInfos1 = ljHouseInfoService.queryGrouypLjHouseInfoByGroupStatus(idList, ljHouseInfo, pager);
        List<GridResult.Category> categories = new ArrayList<>();
        categories.add(new GridResult.Category("房源类型", "houseType","LJOA.CHANNEL.HOUSETYPE"));
        categories.add(new GridResult.Category("项目名称", "projectUuid",LjProjectInfo.class,"projectName"));
        categories.add(new GridResult.Category("楼号", "buildingNo"));
        categories.add(new GridResult.Category("单元", "unitNo"));
        categories.add(new GridResult.Category("房号", "roomNo"));
//        categories.add(new GridResult.Category("房主", "ownerName"));
//        categories.add(new GridResult.Category("联系电话", "ownerTel"));
        categories.add(new GridResult.Category("面积(㎡)", "roomArea"));
        categories.add(new GridResult.Category("产品分类", "roomType","LJOA.CHANNEL.ROOM_TYPE"));
//        categories.add(new GridResult.Category("销售价格(元)", "saleMoney"));
        categories.add(new GridResult.Category("销售状态", "status","LJOA.CHANNEL.ROOM_STATUS"));
        categories.add(new GridResult.Category("公司佣金状态", "companyChargeStatus","LJOA.MAIDSTATUS"));
        return ajaxReGrid("gdata", categories, ljHouseInfos1, pager);
    }

    @RequestMapping("listGroupLjExpenseYJSP")//已售房源中部门审核通过副总审核不通过的
    public Result listGroupLjExpenseYJSP(@UIParam("pager")Pager pager,LjHouseInfo ljHouseInfo,Model model){
        List<LjHouseInfo> ljHouseInfos = ljHouseInfoService.queryGrouypLjHouseInfoByStatus("2", ljHouseInfo, pager);//已售房源的
        List<LjHouseInfo> collect = ljHouseInfos.stream().filter(l -> {
            if ("1".equals(l.getHouseType())) {
                QueryFilterList list = new QueryFilterList();
                list.addFilter("houseUuid", "eq", l.getId());
                List<LjHouseSaleInfo> ljHouseSaleInfos = ljHouseSaleInfoService.queryQueryFilterList(list);
                if (ljHouseSaleInfos.size() > 0) {
                    if(!"0".equals(ljHouseSaleInfos.get(0).getChargeStatus())
                            && !"1".equals(ljHouseSaleInfos.get(0).getChargeStatus())&&"1".equals(l.getCompanyChargeStatus())){
                        return true;
                    }
                }
            }
            if ("2".equals(l.getHouseType())) {
                QueryFilterList list2 = new QueryFilterList();
                list2.addFilter("houseUuid", "eq", l.getId());
                List<MarketingSaleInfo> marketingSaleInfos = marketingSaleInfoService.queryQueryFilterList(list2);
                if (marketingSaleInfos.size() > 0) {
                    if(!"0".equals(marketingSaleInfos.get(0).getInsideChargeStatus()) && !"0".equals(marketingSaleInfos.get(0).getChanneChargeStatus())&&"1".equals(l.getCompanyChargeStatus())){
                        return true;
                    }
                }
            }
            return false;
        }).collect(Collectors.toList());
        List<GridResult.Category> categories = new ArrayList<>();
        List<String> idList = new ArrayList<>();
        for(LjHouseInfo ljHouseInfo1 : collect){
            idList.add(ljHouseInfo1.getId());
        }
        List<LjHouseInfo> ljHouseInfos1 = ljHouseInfoService.queryGrouypLjHouseInfoByGroupStatus(idList, ljHouseInfo, pager);
        categories.add(new GridResult.Category("房源类型", "houseType","LJOA.CHANNEL.HOUSETYPE"));
        categories.add(new GridResult.Category("项目名称", "projectUuid",LjProjectInfo.class,"projectName"));
        categories.add(new GridResult.Category("楼号", "buildingNo"));
        categories.add(new GridResult.Category("单元", "unitNo"));
        categories.add(new GridResult.Category("房号", "roomNo"));
//        categories.add(new GridResult.Category("房主", "ownerName"));
//        categories.add(new GridResult.Category("联系电话", "ownerTel"));
        categories.add(new GridResult.Category("面积(㎡)", "roomArea"));
        categories.add(new GridResult.Category("产品分类", "roomType","LJOA.CHANNEL.ROOM_TYPE"));
//        categories.add(new GridResult.Category("销售价格(元)", "saleMoney"));
        categories.add(new GridResult.Category("销售状态", "status","LJOA.CHANNEL.ROOM_STATUS"));
        categories.add(new GridResult.Category("公司佣金状态", "companyChargeStatus","LJOA.MAIDSTATUS"));
        return ajaxReGrid("gdata", categories, ljHouseInfos1, pager);
    }


    @RequestMapping("saveLjApproval")//保存审批
    public Result saveLjApproval(Approval approval){
        Approval approval1 = approvalService.saveApproval(approval);
        return ajaxRe(true);
    }


}
