package com.jgp.ljoa.com.controller;

import com.jgp.ljoa.channel.model.LjHouseInfo;
import com.jgp.ljoa.channel.model.LjHouseSaleInfo;
import com.jgp.ljoa.channel.service.LjHouseInfoService;
import com.jgp.ljoa.channel.service.LjHouseSaleInfoService;
import com.jgp.ljoa.com.model.Approval;
import com.jgp.ljoa.com.service.ApprovalService;
import com.jgp.ljoa.hr.service.EmployeeService;
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
 * Created by Administrator on 2018/7/10.
 */
/*
作者  SSF
时间   2018/7/10
*/
@Controller
@RequestMapping("/ljoa/com/ljHouseSaleInfoFZYJController")
public class LjHouseSaleInfoFZYJController extends JGPController {
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
    @Autowired
    private EmployeeService employeeService;


    //副总佣金审批（待审批）
    @RequestMapping("listGroupLjHouseSaleInfoFZYJ")
    public String listGroupLjHouseSaleInfoFZYJ(){

        return page("listGroupLjHouseSaleInfoFZYJ");
    }
    //副总佣金审批（已审批）
    @RequestMapping("listGroupLjHouseSaleInfoFZYJ1")
    public String listGroupLjHouseSaleInfoFZYJ1(){

        return page("listGroupLjHouseSaleInfoFZYJ1");
    }

    /*
    * 副总查询渠道佣金审批列表
    * */
    @RequestMapping("showExamineLjHouseSaleInfoFZYJQD/{id}")
    public String  showExamineLjHouseSaleInfoFZYJQD(@PathVariable String id, Model model){
        LjHouseInfo ljHouseInfo = ljHouseInfoService.queryOneLjHouseInfoByid(id);
        LjHouseSaleInfo ljHouseSaleInfo = ljHouseSaleInfoService.queryOneLjHouseSaleInfoBuUUid(ljHouseInfo.getId());
        Approval approval = approvalService.queryOneApprovalByUuidAndCheckType(ljHouseSaleInfo.getId(), "3","2");
        AdminUser currentAdmin = adminUserService.getCurrentAdmin();
        if(Objects.isNull(approval)){
            approval = new Approval();
            approval.setBusiUuid(ljHouseSaleInfo.getId());
            approval.setCheckContent("2");//佣金
            approval.setCheckType("3");//副总审批
            approval.setCheckMan(currentAdmin.getId());
        }
        approval.setCheckTime(LocalDate.now());
        //财务审批通过或总经理审批不通过
        if("4".equals(ljHouseSaleInfo.getChargeStatus())
                || "9".equals(ljHouseSaleInfo.getChargeStatus())){
            approval.setCheckOption(null);//清空意见
            approval.setCheckResult(null);//置空审核结果
            approval.setCheckMan(currentAdmin.getId());
        }
        model.addAttribute("uuid",ljHouseSaleInfo.getId());
        model.addAttribute("chargeStatus",ljHouseSaleInfo.getChargeStatus());//佣金状态
        reForm(model,"LjApprovalfdata",approval);
        reForm(model,"fdata",ljHouseSaleInfo);
        return page("showExamineLjHouseSaleInfoFZYJQD");
    }

    /*
    * 副总查询营销佣金审批列表
    * */
    @RequestMapping("showExamineLjHouseSaleInfoFZYJYX/{id}")
    public String showExamineLjHouseSaleInfoFZYJYX(@PathVariable String id, Model model){
        LjHouseInfo ljHouseInfo = ljHouseInfoService.queryOneLjHouseInfoByid(id);
        MarketingSaleInfo marketingSaleInfo = marketingSaleInfoService.queryMarketingSaleInfoByUuid(ljHouseInfo.getId());
        Approval approval = approvalService.queryOneApprovalByUuidAndCheckType(marketingSaleInfo.getId(), "3","2");
        AdminUser currentAdmin = adminUserService.getCurrentAdmin();
        if(Objects.isNull(approval)){
            approval = new Approval();
            approval.setBusiUuid(marketingSaleInfo.getId());
            approval.setCheckContent("2");//佣金
            approval.setCheckType("3");//副总审批
            approval.setCheckMan(currentAdmin.getId());
        }
        approval.setCheckTime(LocalDate.now());
        //财务审批通过或总经理审批不通过
        if(("9".equals(marketingSaleInfo.getChanneChargeStatus())
                && "9".equals(marketingSaleInfo.getInsideChargeStatus()))
                || ("10".equals(marketingSaleInfo.getChanneChargeStatus())
                && "10".equals(marketingSaleInfo.getInsideChargeStatus()))){
            approval.setCheckOption(null);//清空意见
            approval.setCheckResult(null);//置空审核结果
            approval.setCheckMan(currentAdmin.getId());
        }
        model.addAttribute("houseId",ljHouseInfo.getId());
        model.addAttribute("chargeStatus",marketingSaleInfo.getInsideChargeStatus());//佣金状态
        reForm(model,"LjApprovalfdata",approval);
        reForm(model,"fdata",marketingSaleInfo);
        return page("showExamineLjHouseSaleInfoFZYJYX");
    }

}
