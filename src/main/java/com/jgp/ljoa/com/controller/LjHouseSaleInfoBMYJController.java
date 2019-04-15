package com.jgp.ljoa.com.controller;

import com.jgp.ljoa.channel.model.LjHouseInfo;
import com.jgp.ljoa.channel.model.LjHouseSaleInfo;
import com.jgp.ljoa.channel.service.LjHouseInfoService;
import com.jgp.ljoa.channel.service.LjHouseSaleInfoService;
import com.jgp.ljoa.com.model.Approval;
import com.jgp.ljoa.com.service.ApprovalService;
import com.jgp.ljoa.marketing.model.MarketingSaleInfo;
import com.jgp.ljoa.marketing.service.MarketingSaleInfoService;
import com.jgp.security.admin.model.AdminUser;
import com.jgp.security.admin.service.AdminUserService;
import com.jgp.sys.controller.JGPController;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Created by Administrator on 2018/7/10.
 */
/*
作者  SSF
时间   2018/7/10
*/
@Controller
@RequestMapping("/ljoa/com/ljHouseSaleInfoBMYJController")
public class LjHouseSaleInfoBMYJController extends JGPController {
    @Autowired
    private LjHouseInfoService ljHouseInfoService;
    @Autowired
    private ApprovalService approvalService;
    @Autowired
    private LjHouseSaleInfoService ljHouseSaleInfoService;//渠道房源销售信息
    @Autowired
    private MarketingSaleInfoService marketingSaleInfoService;//营销房源销售信息表
    @Autowired
    private AdminUserService adminUserService;

    /*
    * 销售总监查询佣金审批列表
    * */
    @RequestMapping("listGroupLjHouseSaleInfoBMYJYX")//跳转展示页
    public String listGroupLjHouseSaleInfoBMYJYX(){
        return page("listGroupLjHouseSaleInfoBMYJYX");
    }

    /*
    * 渠道总监查询佣金审批已审批列表
    * */
    @RequestMapping("listGroupLjHouseSaleInfoBMYJQDYES")//跳转展示页
    public String listGroupLjHouseSaleInfoBMYJQDYES(){
        return page("listGroupLjHouseSaleInfoBMYJQDYES");
    }
    /*
    * 渠道总监查询佣金审批未审批列表
    * */
    @RequestMapping("listGroupLjHouseSaleInfoBMYJQDNO")//跳转展示页
    public String listGroupLjHouseSaleInfoBMYJQDNO(){
        return page("listGroupLjHouseSaleInfoBMYJQDNO");
    }

    @RequestMapping("showExamineLjHouseSaleInfoBMYJQD/{id}")//跳转渠道审批页
    public String  showExamineLjHouseSaleInfoBMYJQD(@PathVariable String id, Model model){
        LjHouseInfo ljHouseInfo = ljHouseInfoService.queryOneLjHouseInfoByid(id);
        LjHouseSaleInfo ljHouseSaleInfo = ljHouseSaleInfoService.queryOneLjHouseSaleInfoBuUUid(ljHouseInfo.getId());
        Approval approval = approvalService.queryOneApprovalByUuidAndCheckType(ljHouseSaleInfo.getId(), "1","2");
        AdminUser currentAdmin = adminUserService.getCurrentAdmin();
        if(Objects.isNull(approval)){
            approval = new Approval();
            approval.setBusiUuid(ljHouseSaleInfo.getId());
            approval.setCheckContent("2");//佣金
            approval.setCheckType("1");//总监审批
            approval.setCheckMan(currentAdmin.getId());//审批人
        }
        approval.setCheckTime(LocalDate.now());
        if(!"2".equals(ljHouseSaleInfo.getChargeStatus()) && !"3".equals(ljHouseSaleInfo.getChargeStatus())){
            approval.setCheckOption(null);//清空意见
            approval.setCheckResult(null);//置空审核结果
            approval.setCheckMan(currentAdmin.getId());//审批人
        }
        model.addAttribute("uuid",ljHouseSaleInfo.getId());
        model.addAttribute("chargeStatus",ljHouseSaleInfo.getChargeStatus());
        reForm(model,"LjApprovalfdata",approval);
        reForm(model,"fdata",ljHouseSaleInfo);
        return page("showExamineLjHouseSaleInfoBMYJQD");
    }

    @RequestMapping("showExamineLjHouseSaleInfoBMYJYX/{id}")//跳转营销审批页
    public String showExamineLjHouseSaleInfoBMYJYX(@PathVariable String id, Model model){
        LjHouseInfo ljHouseInfo = ljHouseInfoService.queryOneLjHouseInfoByid(id);
        MarketingSaleInfo marketingSaleInfo = marketingSaleInfoService.queryMarketingSaleInfoByUuid(ljHouseInfo.getId());
        Approval approval = approvalService.queryOneApprovalByUuidAndCheckType(marketingSaleInfo.getId(), "1","2");
        AdminUser currentAdmin = adminUserService.getCurrentAdmin();
        if(StringUtils.isEmpty(approval.getId())){
            approval.setBusiUuid(marketingSaleInfo.getId());
            approval.setCheckContent("2");
            approval.setCheckType("1");
        }
        approval.setCheckTime(LocalDate.now());
        approval.setCheckOption(null);//清空意见
        approval.setCheckResult(null);//置空审核结果
        approval.setCheckMan(currentAdmin.getId());//审批人
        model.addAttribute("chargeStatus",marketingSaleInfo.getInsideChargeStatus());
        reForm(model,"LjApprovalfdata",approval);
        reForm(model,"fdata",marketingSaleInfo);
        return page("showExamineLjHouseSaleInfoBMYJYX");
    }

}
