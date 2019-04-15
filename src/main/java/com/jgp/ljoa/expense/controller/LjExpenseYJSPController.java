package com.jgp.ljoa.expense.controller;

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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;
import java.util.Objects;

/**
 * Created by Administrator on 2018/7/9.
 */
/*
作者  SSF
时间   2018/7/9
*/
@Controller
@RequestMapping("/ljoa/expense/ljExpenseYJSPController")
public class LjExpenseYJSPController extends JGPController {
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

    @RequestMapping("listGroupLjExpenseYJSPYES")//跳转展示页(财务已审批)
    public String listGroupLjExpenseYJSPYES(){
        return page("listGroupLjExpenseYJSPYES");
    }
    @RequestMapping("listGroupLjExpenseYJSPNO")//跳转展示页(财务未审批)
    public String listGroupLjExpenseYJSPNO(){
        return page("listGroupLjExpenseYJSPNO");
    }

    /*
    * 财务查询渠道佣金审批列表
    * */
    @RequestMapping("showExamineLjExpenseYJSPQD/{id}")
    public String  showExamineLjExpenseYJSPQD(@PathVariable String id, Model model){
        LjHouseInfo ljHouseInfo = ljHouseInfoService.queryOneLjHouseInfoByid(id);
        LjHouseSaleInfo ljHouseSaleInfo = ljHouseSaleInfoService.queryOneLjHouseSaleInfoBuUUid(ljHouseInfo.getId());
        Approval approval = approvalService.queryOneApprovalByUuidAndCheckType(ljHouseSaleInfo.getId(), "2","2");
        AdminUser currentAdmin = adminUserService.getCurrentAdmin();
        if(Objects.isNull(approval)){
            approval = new Approval();
            approval.setBusiUuid(ljHouseSaleInfo.getId());
            approval.setCheckContent("2");//佣金
            approval.setCheckType("2");//财务审批
            approval.setCheckMan(currentAdmin.getId());//审批人
        }
        approval.setCheckTime(LocalDate.now());
        //总监审批通过或副总审核不通过
        if("2".equals(ljHouseSaleInfo.getChargeStatus())
                || "7".equals(ljHouseSaleInfo.getChargeStatus())){
            approval.setCheckOption(null);//清空意见
            approval.setCheckResult(null);//置空审核结果
            approval.setCheckMan(currentAdmin.getId());//审批人
        }
        model.addAttribute("uuid",ljHouseSaleInfo.getId());
        model.addAttribute("chargeStatus",ljHouseSaleInfo.getChargeStatus());//佣金状态
        reForm(model,"LjApprovalfdata",approval);
        reForm(model,"fdata",ljHouseSaleInfo);
        return page("showExamineLjExpenseYJSPQD");
    }

    /*
    * 财务查询营销佣金审批列表
    * */
    @RequestMapping("showExamineLjExpenseYJSPYX/{id}")
    public String showExamineLjExpenseYJSPYX(@PathVariable String id, Model model){
        LjHouseInfo ljHouseInfo = ljHouseInfoService.queryOneLjHouseInfoByid(id);
        MarketingSaleInfo marketingSaleInfo = marketingSaleInfoService.queryMarketingSaleInfoByUuid(ljHouseInfo.getId());
        Approval approval = approvalService.queryOneApprovalByUuidAndCheckType(marketingSaleInfo.getId(), "2","2");
        if(Objects.isNull(approval)){
            approval = new Approval();
            approval.setBusiUuid(marketingSaleInfo.getId());
            approval.setCheckContent("2");//佣金
            approval.setCheckType("2");//财务审批
        }
        approval.setCheckTime(LocalDate.now());
        if(!"10".equals(marketingSaleInfo.getChanneChargeStatus())
                && !"10".equals(marketingSaleInfo.getChanneChargeStatus())
                && !"11".equals(marketingSaleInfo.getInsideChargeStatus())
                && !"11".equals(marketingSaleInfo.getInsideChargeStatus())){
            approval.setCheckOption(null);//清空意见
            approval.setCheckResult(null);//置空审核结果
        }
        model.addAttribute("houseId",ljHouseInfo.getId());
        model.addAttribute("chargeStatus",marketingSaleInfo.getInsideChargeStatus());//佣金状态
        reForm(model,"LjApprovalfdata",approval);
        reForm(model,"fdata",marketingSaleInfo);
        return page("showExamineLjExpenseYJSPYX");
    }

}
