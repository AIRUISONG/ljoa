package com.jgp.ljoa.com.controller;

import com.jgp.ljoa.com.model.Approval;
import com.jgp.ljoa.com.service.ApprovalService;
import com.jgp.ljoa.marketing.model.HouseReturn;
import com.jgp.ljoa.marketing.service.HouseReturnService;
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
 * Created by Administrator on 2018/7/23.
 */
/*
作者  SSF
时间   2018/7/23
*/
@Controller
@RequestMapping("/ljoa/com/houseReturnZJLController")
public class HouseReturnZJLController extends JGPController {
    @Autowired
    private HouseReturnService houseReturnService;
    @Autowired
    private ApprovalService approvalService;
    @Autowired
    private AdminUserService adminUserService;

    @RequestMapping("listGroupHouseReturnZJLYES")//跳转总经理退房审批已审批
    public String listGroupHouseReturnZJLYES(){

        return page("listGroupHouseReturnZJLYES");
    }

    @RequestMapping("listGroupHouseReturnZJLNO")//跳转总经理退房审批待审批
    public String listGroupHouseReturnZJLNO(){

        return page("listGroupHouseReturnZJLNO");
    }

    @RequestMapping("showExamineHouseReturnZJL/{id}")//总经理退房审批(房源主键id)
    public String showExamineHouseReturnZJL(@PathVariable String id, Model model){
        HouseReturn houseReturn = houseReturnService.queryOneHouseReturn(id);//房源主键id查询
        Approval approval = approvalService.queryOneApprovalByUuidAndCheckType(houseReturn.getId(), "4", "3");//退房的副总审批的对应退房信息的审批表
        String currentUserId = adminUserService.getCurrentAdmin().getId();
        if(Objects.nonNull(approval)){
            if("5".equals(houseReturn.getApprovalStatus())){
                approval.setCheckResult(null);
                approval.setCheckOption(null);
                approval.setCheckTime(LocalDate.now());
                approval.setCheckMan(currentUserId);
            }
            reForm(model,"LjApprovalfdata",approval);
        }else{
            Approval approval1=new Approval();
            approval1.setBusiUuid(id);
            approval1.setCheckType("4");
            approval1.setCheckContent("3");
            approval1.setCheckMan(currentUserId);
            approval1.setCheckTime(LocalDate.now());
            reForm(model,"LjApprovalfdata",approval1);
        }
        model.addAttribute("uuid",houseReturn.getId());
        reForm(model,"fdata",houseReturn);
        return page("showExamineHouseReturnZJL");
    }

}
