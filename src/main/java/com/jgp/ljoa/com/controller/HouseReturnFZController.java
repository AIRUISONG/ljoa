package com.jgp.ljoa.com.controller;

import com.jgp.ljoa.channel.model.LjHouseInfo;
import com.jgp.ljoa.channel.service.LjHouseInfoService;
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
@RequestMapping("/ljoa/com/houseReturnFZController")
public class HouseReturnFZController extends JGPController {
    @Autowired
    private HouseReturnService houseReturnService;
    @Autowired
    private ApprovalService approvalService;
    @Autowired
    private LjHouseInfoService ljHouseInfoService;
    @Autowired
    private AdminUserService adminUserService;

    //跳转副总退房展示（待审批）
    @RequestMapping("listGroupHouseReturnFZ")
    public String listGroupHouseReturnFZ(){

        return page("listGroupHouseReturnFZ");
    }
    //跳转副总退房展示（已审批）
    @RequestMapping("listGroupHouseReturnFZ1")
    public String listGroupHouseReturnFZ1(){

        return page("listGroupHouseReturnFZ1");
    }

    @RequestMapping("editExamineHouseReturnFZ/{id}")//副总退房审批(房源主键id)
    public String editExamineHouseReturnFZ(@PathVariable String id,Model model){
        HouseReturn houseReturn = houseReturnService.queryOneHouseReturn(id);//房源主键id查询
        Approval approval = approvalService.queryOneApprovalByUuidAndCheckType(houseReturn.getId(), "4", "3");//退房的副总审批的对应退房信息的审批表
        String id1 = adminUserService.getCurrentAdmin().getId();
        if(Objects.nonNull(approval)){
            if("4".equals(houseReturn.getApprovalStatus())){
                approval.setCheckResult(null);
                approval.setCheckOption(null);
                approval.setCheckTime(LocalDate.now());
                approval.setCheckMan(id1);//审批人
            }
            reForm(model,"LjApprovalfdata",approval);
        }else{

            Approval approval1=new Approval();
            approval1.setBusiUuid(id);
            approval1.setCheckType("3");
            approval1.setCheckContent("3");
            approval1.setCheckTime(LocalDate.now());
            approval1.setCheckMan(id1);//审批人
            reForm(model,"LjApprovalfdata",approval1);
        }
        model.addAttribute("uuid",houseReturn.getId());
        model.addAttribute("houseReturnId",houseReturn.getId());
        model.addAttribute("status",houseReturn.getApprovalStatus());
        reForm(model,"fdata",houseReturn);
        return page("editExamineHouseReturnFZ");
    }
    @RequestMapping("showLjHouseInfoHouseReturn/{id}")//对应房源详情(退房主键id)
    public String showLjHouseInfoHouseReturn(@PathVariable String id,Model model){
        HouseReturn houseReturn = houseReturnService.queryOneHouseReturn(id);
        LjHouseInfo ljHouseInfo = ljHouseInfoService.queryOneLjHouseInfoByid(houseReturn.getHouseUuid());
        model.addAttribute("uuid",ljHouseInfo.getId());
        reForm(model,"fdata",ljHouseInfo);
        return page("showLjHouseInfoHouseReturn");
    }
}
