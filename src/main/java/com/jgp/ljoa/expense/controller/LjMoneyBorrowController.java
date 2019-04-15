package com.jgp.ljoa.expense.controller;

import com.jgp.ljoa.com.model.Approval;
import com.jgp.ljoa.com.service.ApprovalService;
import com.jgp.ljoa.expense.model.LjMoneyBorrow;
import com.jgp.ljoa.expense.service.LjMoneyBorrowService;
import com.jgp.ljoa.hr.model.Employee;
import com.jgp.ljoa.hr.model.Relation;
import com.jgp.ljoa.hr.service.EmployeeService;
import com.jgp.ljoa.hr.service.RelationService;
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

/*
项目  ljoa
作者  SSF
时间   2018/11/28
*/
@Controller
@RequestMapping("/ljoa/expense/LjMoneyBorrowController")
public class LjMoneyBorrowController extends JGPController {

    @Autowired
    private LjMoneyBorrowService ljMoneyBorrowService;
    @Autowired
    private AdminUserService adminUserService;
    @Autowired
    private RelationService relationService;
    @Autowired
    private ApprovalService approvalService;
    @Autowired
    private EmployeeService employeeService;


    @RequestMapping("addLjMoneyBorrow")//添加页面
    public String addLjMoneyBorrow(Model model){
        AdminUser currentAdmin = adminUserService.getCurrentAdmin();//员工（报销人）
        Relation relation=relationService.queryOrgByEmployeeId(currentAdmin.getId());//关系表
        LjMoneyBorrow ljMoneyBorrow=new LjMoneyBorrow();
        ljMoneyBorrow.setBorrowMan(currentAdmin.getId());
        ljMoneyBorrow.setStatus("0");
        ljMoneyBorrow.setBorrowOrg(relation.getMainUuid());//部门id
        ljMoneyBorrow.setBorrowDate(LocalDate.now());
        reForm(model,"fdata",ljMoneyBorrow);
        return page("addLjMoneyBorrow");
    }
    @RequestMapping("editLjMoneyBorrow/{id}")//修改页面
    public String editLjMoneyBorrow(@PathVariable String id,Model model){
        LjMoneyBorrow ljMoneyBorrow = ljMoneyBorrowService.queryOneLjMoneyBorrowById(id);
        if(ljMoneyBorrow!=null){
            model.addAttribute("id",ljMoneyBorrow.getId());
            reForm(model,"fdata",ljMoneyBorrow);
            if("0".equals(ljMoneyBorrow.getStatus())||"8".equals(ljMoneyBorrow.getStatus())
                    ||"9".equals(ljMoneyBorrow.getStatus())||"10".equals(ljMoneyBorrow.getStatus())
                    ||"11".equals(ljMoneyBorrow.getStatus())){
                model.addAttribute("flag","1");//可修改
            }else {
                model.addAttribute("flag","0");//不可修改
            }
        }


        return page("editLjMoneyBorrow");
    }
    @RequestMapping("showljMoneyBorrow/{id}")//详情页面
    public String showljMoneyBorrow(@PathVariable String id,Model model){
        LjMoneyBorrow ljMoneyBorrow = ljMoneyBorrowService.queryOneLjMoneyBorrowById(id);
        if(ljMoneyBorrow!=null){
            model.addAttribute("id",ljMoneyBorrow.getId());
            reForm(model,"fdata",ljMoneyBorrow);
        }
        return page("showljMoneyBorrow");
    }
    @RequestMapping("listLjMoneyBorrowByAdminUser")//根据登陆人信息查询备用金list
    public String listLjMoneyBorrowByAdminUser(){

        return page("listLjMoneyBorrowByAdminUser");

    }


    @RequestMapping("listLjMoneyBorrowByOrgUuidYES")//部门主管已审核
    public String listLjMoneyBorrowByOrgUuidYES(){

        return page("listLjMoneyBorrowByOrgUuidYES");
    }
    @RequestMapping("listLjMoneyBorrowByOrgUuidNO")//部门主管未审核
    public String listLjMoneyBorrowByOrgUuidNO(){

        return page("listLjMoneyBorrowByOrgUuidNO");
    }

    @RequestMapping("updateLjMoneyBorrowByBorrowOrgDirector/{id}")//部门主管审批页
    public String updateLjMoneyBorrowByBorrowOrgDirector(@PathVariable String id,Model model){
        LjMoneyBorrow ljMoneyBorrow = ljMoneyBorrowService.queryOneLjMoneyBorrowById(id);
        AdminUser adminUser = adminUserService.queryUserById(ljMoneyBorrow.getBorrowMan());
        Approval approval = approvalService.queryOneApprovalByUuidAndCheckType(ljMoneyBorrow.getId(),"1","4");//部门审批的对应审批表
        AdminUser currentAdmin = adminUserService.getCurrentAdmin();
        if(Objects.isNull(approval)){
            approval = new Approval();
            approval.setBusiUuid(id);
            approval.setCheckTime(LocalDate.now());
            approval.setCheckMan(currentAdmin.getId());
            model.addAttribute("flag","1");//可审批
        }else {
            if("2".equals(ljMoneyBorrow.getStatus())){
                model.addAttribute("flag","1");//可审批
                approval.setCheckMan(currentAdmin.getId());
                approval.setCheckOption(null);//清空意见
                approval.setCheckResult(null);//清空审批结果
            }else {
                model.addAttribute("flag","0");//不可审批
            }
        }
        approval.setCheckContent("4");//备用金业务
        approval.setCheckType("1");//总监审批
        reForm(model,"LjApprovalfdata", approval);
        reForm(model,"fdata",ljMoneyBorrow);
        model.addAttribute("id",ljMoneyBorrow.getId());
        return page("updateLjMoneyBorrowByBorrowOrgDirector");
    }



    @RequestMapping("listLjMoneyBorrowByFinanceYES")//财务已审核
    public String listLjMoneyBorrowByFinanceYES(){

        return page("listLjMoneyBorrowByFinanceYES");
    }
    @RequestMapping("listLjMoneyBorrowByFinanceNO")//财务未审核
    public String listLjMoneyBorrowByFinanceNO(){

        return page("listLjMoneyBorrowByFinanceNO");
    }
    @RequestMapping("updateLjMoneyBorrowByFinanceDirector/{id}")//财务审批页
    public String updateLjMoneyBorrowByFinanceDirector(@PathVariable String id,Model model){
        LjMoneyBorrow ljMoneyBorrow = ljMoneyBorrowService.queryOneLjMoneyBorrowById(id);
        AdminUser adminUser = adminUserService.queryUserById(ljMoneyBorrow.getBorrowMan());
        Employee employee = employeeService.queryOneEmployeeByAccount(adminUser.getUsername());//员工（报销人）
        Approval approval = approvalService.queryOneApprovalByUuidAndCheckType(ljMoneyBorrow.getId(),"2","4");//财务审批的对应审批表
        AdminUser currentAdmin = adminUserService.getCurrentAdmin();
        if(Objects.isNull(approval)){
            approval = new Approval();
            approval.setBusiUuid(id);
            approval.setCheckMan(currentAdmin.getId());//审核人
            approval.setCheckContent("1");//报销
            approval.setCheckType("2");//财务审批
            approval.setCheckTime(LocalDate.now());
            model.addAttribute("flag","1");//可审批
        }else {
            if("3".equals(ljMoneyBorrow.getStatus())){
                model.addAttribute("flag","1");//可审批
                approval.setCheckOption(null);//清空意见
                approval.setCheckResult(null);//清空审批结果
                approval.setCheckMan(currentAdmin.getId());//审核人
            }else {
                model.addAttribute("flag","0");//不可审批
            }
        }
        approval.setCheckContent("4");//备用金业务
        approval.setCheckType("2");//caiwu审批
        model.addAttribute("id",id);
        reForm(model,"LjApprovalfdata", approval);
        reForm(model,"fdata",ljMoneyBorrow);
        model.addAttribute("expenseManName", employee.getPersonName());

        return page("updateLjMoneyBorrowByFinanceDirector");
    }


    @RequestMapping("listLjMoneyBorrowByFZYES")//副总经理已审核
    public String listLjMoneyBorrowByFZYES(){
        return page("listLjMoneyBorrowByFZYES");
    }
    @RequestMapping("listLjMoneyBorrowByFZNO")//副总经理未审核
    public String listLjMoneyBorrowByFZNO(){
        return page("listLjMoneyBorrowByFZNO");
    }
    @RequestMapping("updateLjMoneyBorrowByFZDirector/{id}")//副总经理审批页
    public String updateLjMoneyBorrowByFZDirector(@PathVariable String id,Model model){
        LjMoneyBorrow ljMoneyBorrow = ljMoneyBorrowService.queryOneLjMoneyBorrowById(id);
        AdminUser adminUser = adminUserService.queryUserById(ljMoneyBorrow.getBorrowMan());
        Employee employee = employeeService.queryOneEmployeeByAccount(adminUser.getUsername());//员工（报销人）
        Approval approval = approvalService.queryOneApprovalByUuidAndCheckType(ljMoneyBorrow.getId(),"3","4");
        AdminUser currentAdmin = adminUserService.getCurrentAdmin();
        if(Objects.isNull(approval)){
            approval = new Approval();
            approval.setBusiUuid(id);
            approval.setCheckMan(currentAdmin.getId());
            approval.setCheckTime(LocalDate.now());
            model.addAttribute("flag","1");//可审批
        }else {
            if("4".equals(ljMoneyBorrow.getStatus())){
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
        approval.setCheckContent("4");//备用金业务
        approval.setCheckType("3");//caiwu审批
        model.addAttribute("uuid",id);
        reForm(model, "LjApprovalfdata", approval);
        reForm(model, "fdata", ljMoneyBorrow);
        return page("updateLjMoneyBorrowByFZDirector");
    }


    @RequestMapping("listLjMoneyBorrowByZYES")//总经理已审核
    public String listLjMoneyBorrowByZYES(){
        return page("listLjMoneyBorrowByZYES");
    }
    @RequestMapping("listLjMoneyBorrowByZNO")//总经理未审核
    public String listLjMoneyBorrowByZNO(){
        return page("listLjMoneyBorrowByZNO");
    }
    @RequestMapping("updateLjMoneyBorrowByZDirector/{id}")//总经理审批页
    public String updateLjMoneyBorrowByZDirector(@PathVariable String id,Model model){
        LjMoneyBorrow ljMoneyBorrow = ljMoneyBorrowService.queryOneLjMoneyBorrowById(id);
        AdminUser adminUser = adminUserService.queryUserById(ljMoneyBorrow.getBorrowMan());
        Employee employee = employeeService.queryOneEmployeeByAccount(adminUser.getUsername());//员工（报销人）
        Approval approval = approvalService.queryOneApprovalByUuidAndCheckType(ljMoneyBorrow.getId(),"4","4");
        AdminUser currentAdmin = adminUserService.getCurrentAdmin();
        if(Objects.isNull(approval)){
            approval = new Approval();
            approval.setCheckMan(currentAdmin.getId());//审批人
            approval.setBusiUuid(ljMoneyBorrow.getId());
            model.addAttribute("flag","1");//可审批
            approval.setCheckResult(null);//置空审核结果
            approval.setCheckOption(null);//置空审核意见
        }else {
            if("5".equals(ljMoneyBorrow.getStatus())){
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
        approval.setCheckContent("4");//备用金业务
        approval.setCheckType("4");//caiwu审批
        model.addAttribute("id",id);
        reForm(model, "LjApprovalfdata", approval);
        reForm(model, "fdata", ljMoneyBorrow);

        return page("updateLjMoneyBorrowByZDirector");
    }


    @RequestMapping("listLjMoneyBorrowMakeMoney")//打款展示
    public String listLjMoneyBorrowMakeMoney(){

        return page("listLjMoneyBorrowMakeMoney");
    }
    @RequestMapping("updateLjMoneyBorrowMakeMoney/{id}")//打款界面
    public String updateLjMoneyBorrowMakeMoney(@PathVariable String id ,Model model){
        LjMoneyBorrow ljMoneyBorrow = ljMoneyBorrowService.queryOneLjMoneyBorrowById(id);
        model.addAttribute("id",ljMoneyBorrow.getId());
        reForm(model,"fdata",ljMoneyBorrow);
        return page("updateLjMoneyBorrowMakeMoney");
    }


}
