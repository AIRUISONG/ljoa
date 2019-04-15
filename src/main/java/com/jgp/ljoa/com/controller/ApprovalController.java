package com.jgp.ljoa.com.controller;/**
 * Created by Administrator on 2018/7/5.
 */

import com.jgp.ljoa.channel.model.LjHouseSaleInfo;
import com.jgp.ljoa.channel.service.LjHouseSaleInfoService;
import com.jgp.ljoa.com.model.Approval;
import com.jgp.ljoa.com.service.ApprovalService;
import com.jgp.ljoa.expense.model.LjExpense;
import com.jgp.ljoa.expense.service.LjExpenseService;
import com.jgp.ljoa.hr.model.Employee;
import com.jgp.ljoa.hr.service.EmployeeService;
import com.jgp.ljoa.marketing.model.HouseReturn;
import com.jgp.ljoa.marketing.model.MarketingSaleInfo;
import com.jgp.ljoa.marketing.service.HouseReturnService;
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
import java.util.List;
import java.util.Objects;

/**
 * 项目   ljoa
 * 作者   liujinxu
 * 时间   2018/7/5
 */
@Controller
@RequestMapping("/ljoa/com/approvalController")
public class ApprovalController extends JGPController {
    @Autowired
    private ApprovalService approvalService;
    @Autowired
    private LjHouseSaleInfoService ljHouseSaleInfoService;
    @Autowired
    private MarketingSaleInfoService marketingSaleInfoService;
    @Autowired
    private LjExpenseService ljExpenseService;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private AdminUserService adminUserService;
    @Autowired
    private HouseReturnService houseReturnService;

    //跳转到根据审批类型查询审批列表的页面
    @RequestMapping("listGroupLjApprovalByCheckType")
    private String listGroupLjApprovalByCheckType(@PathVariable String checkType, Model model){
        model.addAttribute("checkType", checkType);
        return page("listGroupLjApprovalByCheckType");
    }

    //跳转到根据业务主键查询审批的页面
    @RequestMapping("showLjApprovalByBusiUuid")
    private String showLjApprovalByBusiUuid(String busiUuid, Model model){
        Approval ljApproval = approvalService.queryOneApprovalByBusiUuid(busiUuid);
        if(Objects.nonNull(ljApproval)){
            reForm(model, "fdata", ljApproval);
        }else {
            Approval approval = new Approval();
            reForm(model, "fdata", approval);
        }
        return page("showLjApprovalByBusiUuid");
    }

    //查看审批反馈意见
    @RequestMapping("showApproval/{busiUuid}/{checkContent}/{checkType}")
    private String showApproval(@PathVariable String busiUuid, @PathVariable String checkContent, @PathVariable String checkType, Model model){
    /*    Approval approval = new Approval();
        approval.setBusiUuid(busiUuid);
        approval.setCheckContent(checkContent);
        approval.setCheckType(checkType);
        approval = approvalService.queryOneApproval(approval);*/
        List<Approval> approvals = approvalService.queryApprovalByBusiUuid(busiUuid, null);
        Approval a1=new Approval();
        Approval a2=new Approval();
        Approval a3=new Approval();
        Approval a4=new Approval();
        for(Approval aa:approvals){
            if("1".equals(aa.getCheckType())){//部门
                a1=aa;
            }
            if("2".equals(aa.getCheckType())){//财务
                a2=aa;
            }
            if("3".equals(aa.getCheckType())){//副总
                a3=aa;
            }
            if("4".equals(aa.getCheckType())){//总经理
                a4=aa;
            }
        }
        reForm(model, "fdata1", a1);
        reForm(model, "fdata2", a2);
        reForm(model, "fdata3", a3);
        reForm(model, "fdata4", a4);
        return page("showApproval");
    }

    //查看退房审批反馈意见
    @RequestMapping("showHouseReturnApprovalByHouseUuid/{houseUuid}/{checkContent}/{checkType}")
    private String showHouseReturnApprovalByHouseUuid(@PathVariable String houseUuid, @PathVariable String checkContent, @PathVariable String checkType, Model model){
        HouseReturn houseReturn = houseReturnService.queryOneHouseReturnByHouseId(houseUuid);
        Approval approval = new Approval();
        if(Objects.nonNull(houseReturn)){
            approval.setBusiUuid(houseReturn.getId());
        }
        approval.setCheckContent(checkContent);
        approval.setCheckType(checkType);
        approval = approvalService.queryOneApproval(approval);
        reForm(model, "fdata", approval);
        List<Approval> approvals = approvalService.queryApprovalByBusiUuid(houseReturn.getId(), null);
        Approval a1=new Approval();
        Approval a2=new Approval();
        Approval a3=new Approval();
        Approval a4=new Approval();
        for(Approval aa:approvals){
            if("1".equals(aa.getCheckType())){//部门
                a1=aa;
            }
            if("2".equals(aa.getCheckType())){//财务
                a2=aa;
            }
            if("3".equals(aa.getCheckType())){//副总
                a3=aa;
            }
            if("4".equals(aa.getCheckType())){//总经理
                a4=aa;
            }
        }
        reForm(model, "fdata1", a1);
        reForm(model, "fdata2", a2);
        reForm(model, "fdata3", a3);
        reForm(model, "fdata4", a4);
        return page("showApproval");
    }

    //查看佣金审批反馈意见
    @RequestMapping("showChargeApprovalByHouseUuid/{houseType}/{houseUuid}/{checkContent}/{checkType}")
    private String showChargeApprovalByHouseUuid(@PathVariable String houseType, @PathVariable String houseUuid, @PathVariable String checkContent, @PathVariable String checkType, Model model){
        Approval approval = new Approval();
        approval.setCheckContent(checkContent);
        approval.setCheckType(checkType);
        if("1".equals(houseType)){
            LjHouseSaleInfo ljHouseSaleInfo = ljHouseSaleInfoService.queryOneLjHouseSaleInfoBuUUid(houseUuid);
            approval.setBusiUuid(ljHouseSaleInfo.getId());
        }else if("2".equals(houseType)){
            List<MarketingSaleInfo> marketingSaleInfos = marketingSaleInfoService.queryMarketingSaleInfoByHouseUuid(houseUuid);
            approval.setBusiUuid(marketingSaleInfos.get(0).getId());
        }
        List<Approval> approvals = approvalService.queryApprovalByBusiUuid(approval.getBusiUuid(), null);
        Approval a1=new Approval();
        Approval a2=new Approval();
        Approval a3=new Approval();
        Approval a4=new Approval();
        for(Approval aa:approvals){
            if("1".equals(aa.getCheckType())){//部门
                a1=aa;
            }
            if("2".equals(aa.getCheckType())){//财务
                a2=aa;
            }
            if("3".equals(aa.getCheckType())){//副总
                a3=aa;
            }
            if("4".equals(aa.getCheckType())){//总经理
                a4=aa;
            }
        }
        reForm(model, "fdata1", a1);
        reForm(model, "fdata2", a2);
        reForm(model, "fdata3", a3);
        reForm(model, "fdata4", a4);
      /*  approval = approvalService.queryOneApproval(approval);
        re(model, "fdata", approval);*/
        return page("showApproval");
    }
///////////////////////总经理////////////////

    /*
     * 总经理查询所有已审批房源信息
     * */
    @RequestMapping("listGeneralManagerQueryHouseSaleInfoYES")
    private String listGeneralManagerQueryHouseSaleInfoYES(){
        return page("listGeneralManagerQueryHouseSaleInfoYES");
    }
    /*
    * 总经理查询所有待审批房源信息
    * */
    @RequestMapping("listGeneralManagerQueryHouseSaleInfoNO")
    private String listGeneralManagerQueryHouseSaleInfoNO(){
        return page("listGeneralManagerQueryHouseSaleInfoNO");
    }

    /*
     * 总经理审批房源销售信息
     * */
    @RequestMapping("showGeneralManagerApprovalHouseSaleInfo/{houseId}/{houseType}")
    private String showGeneralManagerApprovalHouseSaleInfo(@PathVariable String houseId, @PathVariable String houseType, Model model){
        model.addAttribute("houseId",houseId);
        model.addAttribute("houseType",houseType);
        Approval approval = new Approval();
        String currentUserId = adminUserService.getCurrentAdmin().getId();
        if("1".equals(houseType)){
            LjHouseSaleInfo ljHouseSaleInfo = ljHouseSaleInfoService.queryOneLjHouseSaleInfoBuUUid(houseId);
            approval = approvalService.queryOneApprovalByUuidAndCheckType(ljHouseSaleInfo.getId(),"4","2");
            if(Objects.isNull(approval)){
                approval = new Approval();
                approval.setCheckType("4");//类型为总经理审核
                approval.setCheckContent("2");//业务为佣金
                approval.setBusiUuid(ljHouseSaleInfo.getId());
                approval.setCheckMan(currentUserId);//审批人
            }
            approval.setCheckTime(LocalDate.now());
            //副总审核通过
            if("4".equals(ljHouseSaleInfo.getChargeStatus())){
                approval.setCheckOption(null);//清空意见
                approval.setCheckResult(null);//置空审核结果
                approval.setCheckMan(currentUserId);//审批人
            }
            model.addAttribute("uuid",ljHouseSaleInfo.getId());
            model.addAttribute("chargeStatus",ljHouseSaleInfo.getChargeStatus());
            reForm(model, "fdata", approval);
            reForm(model, "channelHouseSaleInfo", ljHouseSaleInfo);//渠道房源销售信息
            return page("showGeneralManagerApprovalChannelHouseSaleInfo");
        }else{
            MarketingSaleInfo marketingSaleInfo = marketingSaleInfoService.queryMarketingSaleInfoByHouseUuid(houseId).get(0);
            approval = approvalService.queryOneApprovalByUuidAndCheckType(marketingSaleInfo.getId(),"4","2");
            if(Objects.isNull(approval)){
                approval = new Approval();
                approval.setCheckType("4");//类型为总经理审核
                approval.setCheckContent("2");//业务为佣金
                approval.setBusiUuid(marketingSaleInfo.getId());
                approval.setCheckMan(currentUserId);//审批人
            }
            approval.setCheckTime(LocalDate.now());
            //副总审核通过
            if("10".equals(marketingSaleInfo.getChanneChargeStatus())
                    && "10".equals(marketingSaleInfo.getInsideChargeStatus())){
                approval.setCheckOption(null);//清空意见
                approval.setCheckResult(null);//置空审核结果
                approval.setCheckMan(currentUserId);//审批人
            }
            model.addAttribute("chargeStatus",marketingSaleInfo.getInsideChargeStatus());
            reForm(model, "fdata", approval);
            reForm(model, "marketingHouseSaleInfo", marketingSaleInfo);//营销房源销售信息
            return page("showGeneralManagerApprovalMarketingHouseSaleInfo");
        }
    }

    /*
    * 跳转到总经理查看房源销售信息的页面
    * */
    @RequestMapping("showGeneralManagerLookHouseSaleInfo/{houseUuid}/{houseType}")
    private String showGeneralManagerLookHouseSaleInfo(@PathVariable String houseUuid, @PathVariable String houseType, Model model){
        if("1".equals(houseType)){
            LjHouseSaleInfo ljHouseSaleInfo = ljHouseSaleInfoService.queryOneLjHouseSaleInfoBuUUid(houseUuid);
            reForm(model, "fdata", ljHouseSaleInfo);
            return page("generalManagerLookChannelHouseSaleInfo");
        }else {
            List<MarketingSaleInfo> marketingSaleInfos = marketingSaleInfoService.queryMarketingSaleInfoByHouseUuid(houseUuid);
            MarketingSaleInfo marketingSaleInfo = marketingSaleInfos.get(0);
            //model.addAttribute("",marketingSaleInfo.get)
            reForm(model, "fdata",marketingSaleInfo);
            return page("showGgeneralManagerLookMarketingHouseSaleInfo");
        }
    }



    /*
    * 总经理查询报销已审批页面
    * */
    @RequestMapping("listGeneralManagerQueryExpenseYES")
    private String listGeneralManagerQueryExpenseYES(){
        return page("listGeneralManagerQueryExpenseYES");
    }
    /*
    * 总经理查询报销待审批页面
    * */
    @RequestMapping("listGeneralManagerQueryExpenseNO")
    private String listGeneralManagerQueryExpenseNO(){
        return page("listGeneralManagerQueryExpenseNO");
    }




    /*
    * 总经理审批报销页面
    * */
    @RequestMapping("showGeneralManagerLookExpense/{expenseId}")
    private String showGeneralManagerLookExpense(@PathVariable String expenseId, Model model){
        LjExpense ljExpense = ljExpenseService.queryOneLjExpenseById(expenseId);
        AdminUser adminUser = adminUserService.queryUserById(ljExpense.getExpenseMan());
        Employee employee = employeeService.queryOneEmployeeByAccount(adminUser.getUsername());//员工（报销人）
        Approval approval = approvalService.queryOneApprovalByUuidAndCheckType(ljExpense.getId(),"4","1");
        AdminUser currentAdmin = adminUserService.getCurrentAdmin();
        if(Objects.isNull(approval)){
            approval = new Approval();
            approval.setCheckMan(currentAdmin.getId());//审批人
            approval.setBusiUuid(ljExpense.getId());
            model.addAttribute("flag","1");//可审批
            approval.setCheckResult(null);//置空审核结果
            approval.setCheckOption(null);//置空审核意见
        }else {
            if("5".equals(ljExpense.getStatus())){
                model.addAttribute("flag","1");//可审批
                approval.setCheckResult(null);//置空审核结果
                approval.setCheckOption(null);//置空审核意见
                approval.setCheckMan(currentAdmin.getId());//审批人
            }else {
                model.addAttribute("flag","0");//不可审批
            }
        }
        //初始化审批表
        approval.setCheckTime(LocalDate.now());
        approval.setCheckContent("1");//报销
        approval.setCheckType("4");//总经理审批
        model.addAttribute("uuid",expenseId);
        reForm(model, "LjApprovalfdata", approval);
        reForm(model, "fdata", ljExpense);
        model.addAttribute("expenseManName", employee.getPersonName());
        return page("showGeneralManagerLookExpense");
    }

///////////////////////////////////////////////////////////////////
///////////////////副总经理////////////////////////////////////////
    /*
    * 副总查询报销列表页面
    * */
    //未审批
    @RequestMapping("listDeputyGeneralManagerQueryExpense")
    private String listDeputyGeneralManagerQueryExpense(){
        return page("listDeputyGeneralManagerQueryExpense");
    }
    //已审批
    @RequestMapping("listDeputyGeneralManagerQueryExpense1")
    private String listDeputyGeneralManagerQueryExpense1(){
        return page("listDeputyGeneralManagerQueryExpense1");
    }

    /*
    * 副总审批报销列表页面
    * */
    @RequestMapping("listDeputyGeneralManagerLookExpense/{expenseId}")
    private String listDeputyGeneralManagerLookExpense(@PathVariable String expenseId, Model model){
        LjExpense ljExpense = ljExpenseService.queryOneLjExpenseById(expenseId);
        AdminUser adminUser = adminUserService.queryUserById(ljExpense.getExpenseMan());
        Employee employee = employeeService.queryOneEmployeeByAccount(adminUser.getUsername());//员工（报销人）
        Approval approval = approvalService.queryOneApprovalByUuidAndCheckType(ljExpense.getId(),"3","1");
        AdminUser currentAdmin = adminUserService.getCurrentAdmin();
        if(Objects.isNull(approval)){
            approval = new Approval();
            approval.setBusiUuid(expenseId);
            approval.setCheckMan(currentAdmin.getId());
            approval.setCheckTime(LocalDate.now());
            model.addAttribute("flag","1");//可审批
        }else {
            if("4".equals(ljExpense.getStatus())){
                model.addAttribute("flag","1");//可审批
                approval.setCheckResult(null);//置空审核结果
                approval.setCheckOption(null);//置空审核意见
                approval.setCheckMan(currentAdmin.getId());
            }else {
                model.addAttribute("flag","0");//不可审批
            }
        }
        //初始化审批表
        approval.setCheckTime(LocalDate.now());
        approval.setCheckContent("1");//报销
        approval.setCheckType("3");//副总审批
        model.addAttribute("uuid",expenseId);
        reForm(model, "LjApprovalfdata", approval);
        reForm(model, "fdata", ljExpense);
        model.addAttribute("expenseManName", employee.getPersonName());
        return page("listDeputyGeneralManagerLookExpense");
    }

///////////////////////////////////////////////////////////////////
    @RequestMapping("listApprovalByBusiUuid/{busiId}")
    public String listApprovalByBusiUuid(@PathVariable String busiId,Model model){
        model.addAttribute("busiId",busiId);
        return page("listApprovalByBusiUuid");
    }
}
