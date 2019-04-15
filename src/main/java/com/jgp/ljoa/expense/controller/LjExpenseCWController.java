package com.jgp.ljoa.expense.controller;

import com.jgp.ljoa.com.model.Approval;
import com.jgp.ljoa.com.service.ApprovalService;
import com.jgp.ljoa.expense.model.LjExpense;
import com.jgp.ljoa.expense.service.LjExpenseService;
import com.jgp.ljoa.hr.model.Employee;
import com.jgp.ljoa.hr.service.EmployeeService;
import com.jgp.ljoa.marketing.model.HouseReturn;
import com.jgp.ljoa.marketing.service.HouseReturnService;
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
 * Created by Administrator on 2018/7/8.
 */
/*
作者  SSF
时间   2018/7/8
*/
@Controller
@RequestMapping("/ljoa/expense/ljExpenseCWController")
public class LjExpenseCWController extends JGPController {
    @Autowired
    private LjExpenseService ljExpenseService;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private ApprovalService approvalService;
    @Autowired
    private AdminUserService adminUserService;
    @Autowired
    private HouseReturnService houseReturnService;

    @RequestMapping("listAllLjExpenseCWYES")//财务审批展示页(已审核的)
    public String listAllLjExpenseCWYES(){

        return page("listAllLjExpenseCWYES");
    }

    @RequestMapping("listAllLjExpenseCWNO")//财务审批展示页(未审核的)
    public String listAllLjExpenseCWNO(){

        return page("listAllLjExpenseCWNO");
    }

    /*
    * 财务查询报销审批列表
    * */
    @RequestMapping("showExamineLjExpenseCW/{id}")
    public String showExamineLjExpenseCW(@PathVariable String id, Model model){
        LjExpense ljExpense = ljExpenseService.queryOneLjExpenseById(id);
        AdminUser adminUser = adminUserService.queryUserById(ljExpense.getExpenseMan());
        Employee employee = employeeService.queryOneEmployeeByAccount(adminUser.getUsername());//员工（报销人）
        Approval approval = approvalService.queryOneApprovalByUuidAndCheckType(ljExpense.getId(),"2","1");//财务审批的对应审批表
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
            if("3".equals(ljExpense.getStatus())){
                model.addAttribute("flag","1");//可审批
                approval.setCheckOption(null);//清空意见
                approval.setCheckResult(null);//清空审批结果
                approval.setCheckMan(currentAdmin.getId());//审核人
            }else {
                model.addAttribute("flag","0");//不可审批
            }
            //副总审核不通过
            /*Approval approval1 = approvalService.queryOneApprovalByUuidAndCheckType(ljExpense.getId(), "3", "1");
            //总监审核通过
            Approval approval2 = approvalService.queryOneApprovalByUuidAndCheckType(ljExpense.getId(), "1", "1");
            if((Objects.nonNull(approval1) && "N".equals(approval1.getCheckResult())) || ("Y".equals(approval2.getCheckResult()))){
                model.addAttribute("flag","1");//可审批
                approval.setCheckOption(null);//清空意见
                approval.setCheckResult(null);//清空审批结果
            }else {
                model.addAttribute("flag","0");//不可审批
            }*/
        }
        model.addAttribute("uuid",id);
        reForm(model,"LjApprovalfdata", approval);
        reForm(model,"fdata",ljExpense);
        model.addAttribute("expenseManName", employee.getPersonName());
        return page("showExamineLjExpenseCW");
    }
    @RequestMapping("showLjExpenseCW/{id}")//详情页
    public String showLjExpenseCW(@PathVariable String id,Model model){
        LjExpense ljExpense = ljExpenseService.queryOneLjExpenseById(id);
        Employee employee = employeeService.queryOneEmployee(ljExpense.getExpenseMan());//员工（报销人）
        reForm(model,"fdata",ljExpense);
        model.addAttribute("expenseManName", employee.getPersonName());
        return page("showLjExpenseCW");
    }

    //跳转财务退房审批已审批
    @RequestMapping("listHouseReturnFinanceYES")
    public String listHouseReturnFinanceYES(){
        return page("listHouseReturnFinanceYES");
    }
    //跳转财务退房审批未审批
    @RequestMapping("listHouseReturnFinanceNO")
    public String listHouseReturnFinanceNO(){
        return page("listHouseReturnFinanceNO");
    }


    //跳转财务退房审批的审批页面
    @RequestMapping("editHouseReturnSaleFinance/{id}")
    public String editHouseReturnSaleFinance(@PathVariable String id,Model model){
        HouseReturn houseReturn = houseReturnService.queryOneHouseReturn(id);
        Approval approval = approvalService.queryOneApprovalByUuidAndCheckType(id, "2", "3");
        String currentUserId = adminUserService.getCurrentAdmin().getId();
        if(Objects.isNull(approval)){
            approval = new Approval();
            approval.setBusiUuid(id);
            approval.setCheckContent("3");
            approval.setCheckType("2");
            approval.setCheckTime(LocalDate.now());
            approval.setCheckMan(currentUserId);
        }else {
            if("3".equals(houseReturn.getApprovalStatus())){
                approval.setCheckOption(null);
                approval.setCheckResult(null);
                approval.setCheckTime(LocalDate.now());
                approval.setCheckMan(currentUserId);
            }
        }
        reForm(model,"afdata",approval);
        reForm(model,"fdata",houseReturn);
        model.addAttribute("houseReturnId",houseReturn.getId());
        model.addAttribute("status",houseReturn.getApprovalStatus());
        return page("editHouseReturnSaleFinance");
    }
}
