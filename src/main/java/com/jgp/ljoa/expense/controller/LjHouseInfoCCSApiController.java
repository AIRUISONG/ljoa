package com.jgp.ljoa.expense.controller;

import com.jgp.common.annotation.UIParam;
import com.jgp.common.pojo.LabelValue;
import com.jgp.ljoa.channel.model.LjHouseInfo;
import com.jgp.ljoa.channel.model.LjHouseSaleInfo;
import com.jgp.ljoa.channel.model.LjProjectInfo;
import com.jgp.ljoa.channel.service.LjHouseInfoService;
import com.jgp.ljoa.channel.service.LjHouseSaleInfoService;
import com.jgp.ljoa.channel.service.LjProjectInfoService;
import com.jgp.ljoa.marketing.model.MarketingSaleInfo;
import com.jgp.ljoa.marketing.service.MarketingSaleInfoService;
import com.jgp.sys.controller.JGPController;
import com.jgp.sys.query.QueryFilterList;
import com.jgp.sys.ui.GridResult;
import com.jgp.sys.ui.Pager;
import com.jgp.sys.ui.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created by Administrator on 2018/7/9.
 */
/*
作者  SSF
时间   2018/7/9
*/
@RestController
@RequestMapping("/ljoa/expense/ljHouseInfoCCSApiController")//房源信息佣金状态(CompanyChargeStatus)AjaxController
public class LjHouseInfoCCSApiController extends JGPController {

    @Autowired
    private LjHouseInfoService ljHouseInfoService;
    @Autowired
    private LjProjectInfoService ljProjectInfoService;
    @Autowired
    private LjHouseSaleInfoService ljHouseSaleInfoService;
    @Autowired
    private MarketingSaleInfoService marketingSaleInfoService;


    /*
    * 财务修改佣金状态（开发商给邻家结佣 ）
    * */
    @RequestMapping("listGroupLjHouseInfoCCS")
    public Result listGroupLjHouseInfoCCS(@UIParam("pager")Pager pager, LjHouseInfo ljHouseInfo){//已售房源显示
        List<LjHouseInfo> ljHouseInfos = ljHouseInfoService.queryGrouypLjHouseInfoByStatus("2",ljHouseInfo,pager);
        List<LjHouseInfo> collect = ljHouseInfos.stream().filter(l -> {
            if ("1".equals(l.getHouseType())) { //渠道整合的
                QueryFilterList list = new QueryFilterList();
                list.addFilter("houseUuid", "eq", l.getId());
                List<LjHouseSaleInfo> ljHouseSaleInfos = ljHouseSaleInfoService.queryQueryFilterList(list);
                if (ljHouseSaleInfos.size() > 0) {
                    if("8".equals(ljHouseSaleInfos.get(0).getChargeStatus())){//总经理审核通过的
                        return true;
                    }
                }
            }
            if ("2".equals(l.getHouseType())) {
                QueryFilterList list2 = new QueryFilterList();
                list2.addFilter("houseUuid", "eq", l.getId());
                List<MarketingSaleInfo> marketingSaleInfos = marketingSaleInfoService.queryQueryFilterList(list2);
                if (marketingSaleInfos.size() > 0) {
                    if("8".equals(marketingSaleInfos.get(0).getChanneChargeStatus())||"8".equals(marketingSaleInfos.get(0).getInsideChargeStatus())){
                        return true;
                    }
                }
            }
            return false;
        }).collect(Collectors.toList());
        List<GridResult.Category> categories = new ArrayList<>();
        categories.add(new GridResult.Category("项目名称", "projectUuid",LjProjectInfo.class,"projectName"));
        categories.add(new GridResult.Category("楼号", "buildingNo"));
        categories.add(new GridResult.Category("单元", "unitNo"));
        categories.add(new GridResult.Category("房号", "roomNo"));
//        categories.add(new GridResult.Category("房主", "ownerName"));
//        categories.add(new GridResult.Category("联系电话", "ownerTel"));
        categories.add(new GridResult.Category("面积(㎡)", "roomArea"));
        categories.add(new GridResult.Category("产品分类", "roomType","LJOA.CHANNEL.ROOM_TYPE"));
        categories.add(new GridResult.Category("公司佣金状态", "companyChargeStatus","LJOA.MAIDSTATUS"));
        categories.add(new GridResult.Category("销售状态", "status","LJOA.CHANNEL.ROOM_STATUS"));
        return ajaxReGrid("gdata", categories, collect, pager);
    }

    @RequestMapping("editLjHouseInfoCCS")//修改
    public Result editLjHouseInfoCCS(LjHouseInfo ljHouseInfo){
        LjHouseInfo ljHouseInfo1 = ljHouseInfoService.saveLjHouseInfo(ljHouseInfo);
        return ajaxRe(true);
    }
    @RequestMapping("editLjHouseInfo")//修改
    public Result editLjHouseInfo(LjHouseInfo ljHouseInfo){

        if (Objects.nonNull(ljHouseInfo.getCompanyChargeMoneyServiceCharge())&&(ljHouseInfo.getCompanyChargeMoneyTrue()+ljHouseInfo.getCompanyChargeMoneyServiceCharge()<ljHouseInfo.getCompanyChargeMoney())&&"2".equals(ljHouseInfo.getCompanyChargeStatus())){
            return ajaxRe(false);
        }else  if (Objects.isNull(ljHouseInfo.getCompanyChargeMoneyServiceCharge())&&(ljHouseInfo.getCompanyChargeMoneyTrue()<ljHouseInfo.getCompanyChargeMoney())&&"2".equals(ljHouseInfo.getCompanyChargeStatus())){
            return ajaxRe(false);
        }
        else{
            ljHouseInfoService.saveLjHouseInfo(ljHouseInfo);
            return ajaxRe(true);
        }
    }
    @RequestMapping("listProjectName")//所属项目下拉数据源
    public Result listProjectName(){
        List<LjProjectInfo> ljProjectInfos = ljProjectInfoService.queryGroupLjProjectInfoByProjectType();
        List<LabelValue> collect = ljProjectInfos.stream().map(ljProjectInfo -> {
            return new LabelValue(ljProjectInfo.getProjectName(), ljProjectInfo.getId(), null);
        }).collect(Collectors.toList());
        return ajaxReData("ldata",collect);
    }
    @RequestMapping("listProjectNameMarking")//所属营销项目下拉数据源
    public Result listProjectNameMarking(){
        List<LjProjectInfo> ljProjectInfos = ljProjectInfoService.queryGroupLjProjectInfoByProjectTypeYX();
        List<LabelValue> collect = ljProjectInfos.stream().map(ljProjectInfo -> {
            return new LabelValue(ljProjectInfo.getProjectName(), ljProjectInfo.getId(), null);
        }).collect(Collectors.toList());
        return ajaxReData("ldata",collect);
    }
}
