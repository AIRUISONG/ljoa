package com.jgp.ljoa.com.controller;

import com.jgp.common.annotation.UIParam;
import com.jgp.ljoa.channel.model.LjHouseInfo;
import com.jgp.ljoa.channel.model.LjHouseSaleInfo;
import com.jgp.ljoa.channel.model.LjProjectInfo;
import com.jgp.ljoa.channel.service.LjHouseInfoService;
import com.jgp.ljoa.channel.service.LjHouseSaleInfoService;
import com.jgp.ljoa.channel.service.LjProjectInfoService;
import com.jgp.ljoa.com.service.ApprovalService;
import com.jgp.ljoa.marketing.service.MarketingSaleInfoService;
import com.jgp.sys.controller.JGPController;
import com.jgp.sys.query.QueryFilterList;
import com.jgp.sys.ui.GridResult;
import com.jgp.sys.ui.Pager;
import com.jgp.sys.ui.Result;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Administrator on 2018/7/10.
 */
/*
作者  SSF
时间   2018/7/10
*/
@RestController
@RequestMapping("/ljoa/com/ljHouseSaleInfoBMYJApiController")
public class  LjHouseSaleInfoBMYJApiController extends JGPController {
    @Autowired
    private LjHouseInfoService ljHouseInfoService;
    @Autowired
    private LjHouseSaleInfoService ljHouseSaleInfoService;//渠道房源销售信息
    @Autowired
    private MarketingSaleInfoService marketingSaleInfoService;//营销房源销售信息表
    @Autowired
    private ApprovalService approvalService;
    @Autowired
    private LjProjectInfoService ljProjectInfoService;


    /*
    * 销售总监佣金审批
    * */
    @RequestMapping("listGroupLjHouseSaleInfoYJSPYX")
    public Result listGroupLjHouseSaleInfoYJSPYX(@UIParam("pager")Pager pager, LjHouseInfo ljHouseInfo, Model model){
        ljHouseInfo.setHouseType("2");//房源类型为营销
        List<LjHouseInfo> ljHouseInfos = ljHouseInfoService.queryGrouypLjHouseInfoByStatus("2", ljHouseInfo, pager);//已售房源的
        List<LjHouseInfo> collect = ljHouseInfos.stream().filter(l -> {
            if ("1".equals(l.getHouseType())) {
                QueryFilterList list = new QueryFilterList();
                list.addFilter("houseUuid", "eq", l.getId());
                List<LjHouseSaleInfo> ljHouseSaleInfos = ljHouseSaleInfoService.queryQueryFilterList(list);
                if (ljHouseSaleInfos.size() > 0) {
                    if("1".equals(ljHouseSaleInfos.get(0).getChargeStatus())||"5".equals(ljHouseSaleInfos.get(0).getChargeStatus()) || "6".equals(ljHouseSaleInfos.get(0).getChargeStatus())){
                        return true;
                    }
                }
            }
            return false;
        }).collect(Collectors.toList());
        List<String> list = new ArrayList<>();
        for(LjHouseInfo l:collect){
            list.add(l.getId());
        }
        List<LjHouseInfo> ljHouseInfos1 = ljHouseInfoService.queryGrouypLjHouseInfoByGroupStatus(list,ljHouseInfo, pager);
        List<LjProjectInfo> ljProjectInfos = ljProjectInfoService.queryAllLjProjectInfo();
        ljHouseInfos1 = ljHouseInfos1.stream().map(ll -> {
            for(LjProjectInfo l : ljProjectInfos){
                if(ll.getProjectUuid().equals(l.getId())){
                    ll.setProjectUuid(l.getProjectName());
                }
            }
            return ll;
        }).collect(Collectors.toList());
        List<GridResult.Category> categories = new ArrayList<>();
        categories.add(new GridResult.Category("项目名称", "projectUuid"));
        categories.add(new GridResult.Category("楼号", "buildingNo"));
        categories.add(new GridResult.Category("单元", "unitNo"));
        categories.add(new GridResult.Category("房号", "roomNo"));
//        categories.add(new GridResult.Category("房主", "ownerName"));
//        categories.add(new GridResult.Category("联系电话", "ownerTel"));
        categories.add(new GridResult.Category("面积(㎡)", "roomArea"));
        categories.add(new GridResult.Category("产品分类", "roomType","LJOA.CHANNEL.ROOM_TYPE"));
        categories.add(new GridResult.Category("销售价格(元)", "saleMoney"));
        categories.add(new GridResult.Category("销售状态", "status","LJOA.CHANNEL.ROOM_STATUS"));
        return ajaxReGrid("gdata", categories, ljHouseInfos1, pager);
    }

    /*
    * 渠道总监佣金审批已审批的
    * */
    @RequestMapping("listGroupLjHouseSaleInfoYJSPQDYES")
    public Result listGroupLjHouseSaleInfoYJSPQDYES(@UIParam("pager")Pager pager, LjHouseInfo ljHouseInfo, Model model){
        ljHouseInfo.setHouseType("1");//房源类型为渠道
        List<LjHouseInfo> ljHouseInfos = ljHouseInfoService.queryGrouypLjHouseInfoByStatus("2", ljHouseInfo, pager);//已售房源的
        List<LjHouseInfo> collect = ljHouseInfos.stream().filter(l -> {
            if ("1".equals(l.getHouseType())) {
                QueryFilterList list = new QueryFilterList();
                list.addFilter("houseUuid", "eq", l.getId());
                List<LjHouseSaleInfo> ljHouseSaleInfos = ljHouseSaleInfoService.queryQueryFilterList(list);
                if (ljHouseSaleInfos.size() > 0) {
                    if(!"0".equals(ljHouseSaleInfos.get(0).getChargeStatus())&&!"1".equals(ljHouseSaleInfos.get(0).getChargeStatus())&& StringUtils.isNotEmpty(ljHouseSaleInfos.get(0).getChargeStatus())){
                        return true;
                    }
                }
            }
            return false;
        }).collect(Collectors.toList());
        List<String> list = new ArrayList<>();
        for(LjHouseInfo l:collect){
            list.add(l.getId());
        }
        List<LjHouseInfo> ljHouseInfos1 = ljHouseInfoService.queryGrouypLjHouseInfoByGroupStatus(list,ljHouseInfo, pager);
        List<LjProjectInfo> ljProjectInfos = ljProjectInfoService.queryAllLjProjectInfo();
        ljHouseInfos1 = ljHouseInfos1.stream().map(ll -> {
            for(LjProjectInfo l : ljProjectInfos){
                if(ll.getProjectUuid().equals(l.getId())){
                    ll.setProjectUuid(l.getProjectName());
                }
            }
            return ll;
        }).collect(Collectors.toList());
        List<GridResult.Category> categories = new ArrayList<>();
        categories.add(new GridResult.Category("项目名称", "projectUuid"));
        categories.add(new GridResult.Category("楼号", "buildingNo"));
        categories.add(new GridResult.Category("单元", "unitNo"));
        categories.add(new GridResult.Category("房号", "roomNo"));
        categories.add(new GridResult.Category("面积(㎡)", "roomArea"));
        categories.add(new GridResult.Category("产品分类", "roomType","LJOA.CHANNEL.ROOM_TYPE"));
        categories.add(new GridResult.Category("销售状态", "status","LJOA.CHANNEL.ROOM_STATUS"));
        categories.add(new GridResult.Category("公司佣金状态", "companyChargeStatus","LJOA.MAIDSTATUS"));
        return ajaxReGrid("gdata", categories, ljHouseInfos1, pager);
    }

    /*
    * 渠道总监佣金审批未审批的
    * */
    @RequestMapping("listGroupLjHouseSaleInfoYJSPQDNO")
    public Result listGroupLjHouseSaleInfoYJSPQDNO(@UIParam("pager")Pager pager, LjHouseInfo ljHouseInfo, Model model){
        ljHouseInfo.setHouseType("1");//房源类型为渠道
        List<LjHouseInfo> ljHouseInfos = ljHouseInfoService.queryGrouypLjHouseInfoByStatus("2", ljHouseInfo, pager);//已售房源的
        List<LjHouseInfo> collect = ljHouseInfos.stream().filter(l -> {
            if ("1".equals(l.getHouseType())) {
                QueryFilterList list = new QueryFilterList();
                list.addFilter("houseUuid", "eq", l.getId());
                List<LjHouseSaleInfo> ljHouseSaleInfos = ljHouseSaleInfoService.queryQueryFilterList(list);
                if (ljHouseSaleInfos.size() > 0) {
                    if("1".equals(ljHouseSaleInfos.get(0).getChargeStatus())){
                        return true;
                    }
                }
            }
            return false;
        }).collect(Collectors.toList());
        List<String> list = new ArrayList<>();
        for(LjHouseInfo l:collect){
            list.add(l.getId());
        }
        List<LjHouseInfo> ljHouseInfos1 = ljHouseInfoService.queryGrouypLjHouseInfoByGroupStatus(list,ljHouseInfo, pager);
        List<LjProjectInfo> ljProjectInfos = ljProjectInfoService.queryAllLjProjectInfo();
        ljHouseInfos1 = ljHouseInfos1.stream().map(ll -> {
            for(LjProjectInfo l : ljProjectInfos){
                if(ll.getProjectUuid().equals(l.getId())){
                    ll.setProjectUuid(l.getProjectName());
                }
            }
            return ll;
        }).collect(Collectors.toList());
        List<GridResult.Category> categories = new ArrayList<>();
        categories.add(new GridResult.Category("项目名称", "projectUuid"));
        categories.add(new GridResult.Category("楼号", "buildingNo"));
        categories.add(new GridResult.Category("单元", "unitNo"));
        categories.add(new GridResult.Category("房号", "roomNo"));
        categories.add(new GridResult.Category("面积(㎡)", "roomArea"));
        categories.add(new GridResult.Category("产品分类", "roomType","LJOA.CHANNEL.ROOM_TYPE"));
        categories.add(new GridResult.Category("销售状态", "status","LJOA.CHANNEL.ROOM_STATUS"));
        categories.add(new GridResult.Category("公司佣金状态", "companyChargeStatus","LJOA.MAIDSTATUS"));
        return ajaxReGrid("gdata", categories, ljHouseInfos1, pager);
    }

}
