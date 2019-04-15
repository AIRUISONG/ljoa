package com.jgp.ljoa.com.controller;

import com.jgp.ljoa.com.model.Approval;
import com.jgp.ljoa.com.service.ApprovalService;
import com.jgp.ljoa.expense.model.LjExpense;
import com.jgp.ljoa.expense.service.LjExpenseService;
import com.jgp.ljoa.hr.model.Employee;
import com.jgp.ljoa.hr.service.EmployeeService;
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
 * Created by Administrator on 2018/7/7.
 */
/*
作者  SSF
时间   2018/7/7
*/
@Controller
@RequestMapping("/ljoa/com/ljExpenseBMController")
public class LjExpenseBMController extends JGPController {

    @Autowired
    private LjExpenseService ljExpenseService;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private ApprovalService approvalService;
    @Autowired
    private AdminUserService adminUserService;

    /*
    * 营销部门报销审批
    * */
    //待审批
    @RequestMapping("listAllLjExpenseBMYX")
    public String listAllLjExpenseBMYX(){
        return page("listAllLjExpenseBMYX");
    }
    //已审批
    @RequestMapping("listAllLjExpenseBMYX1")
    public String listAllLjExpenseBMYX1(){
        return page("listAllLjExpenseBMYX1");
    }

    /*
    * 渠道部门报销审批已审批的
    * */
    @RequestMapping("listAllLjExpenseBMQDYES")
    public String listAllLjExpenseBMQDYES(){
        return page("listAllLjExpenseBMQDYES");
    }
    /*
    * 渠道部门报销审批未审批的
    * */
    @RequestMapping("listAllLjExpenseBMQDNO")
    public String listAllLjExpenseBMQDNO(){
        return page("listAllLjExpenseBMQDNO");
    }


    /*
     * 新房事业部门报销审批已审批的
    * */
    @RequestMapping("listAllLjExpenseBMXFYES")
    public String listAllLjExpenseBMXFYES(){
        return page("listAllLjExpenseBMXFYES");
    }

    /*
    * 新房事业部门报销审批未审批的
    * */
    @RequestMapping("listAllLjExpenseBMXFNO")
    public String listAllLjExpenseBMXFNO(){
        return page("listAllLjExpenseBMXFNO");
    }

    /*
     * 二手房事业部门报销审批已审批的
    * */
    @RequestMapping("listAllLjExpenseBMERSYES")
    public String listAllLjExpenseBMERSYES(){
        return page("listAllLjExpenseBMERSYES");
    }

    /*
    * 二手房事业部门报销审批未审批的
    * */
    @RequestMapping("listAllLjExpenseBMERSNO")
    public String  listAllLjExpenseBMERSNO(){
        return page("listAllLjExpenseBMERSNO");
    }

    /*
    * 部门负责人审批报销页面
    * */
    @RequestMapping("showExamineLjExpenseBM/{id}")
    public String showExamineLjExpenseBM(@PathVariable String id,Model model){
        LjExpense ljExpense = ljExpenseService.queryOneLjExpenseById(id);
        AdminUser adminUser = adminUserService.queryUserById(ljExpense.getExpenseMan());
        Employee employee = employeeService.queryOneEmployeeByAccount(adminUser.getUsername());//员工（报销人）
        Approval approval = approvalService.queryOneApprovalByUuidAndCheckType(ljExpense.getId(),"1","1");//部门审批的对应审批表
        AdminUser currentAdmin = adminUserService.getCurrentAdmin();
        if(Objects.isNull(approval)){
            approval = new Approval();
            approval.setBusiUuid(id);
            approval.setCheckTime(LocalDate.now());
            approval.setCheckMan(currentAdmin.getId());
            model.addAttribute("flag","1");//可审批
        }else {
            if("2".equals(ljExpense.getStatus())){
                model.addAttribute("flag","1");//可审批
                approval.setCheckMan(currentAdmin.getId());
                approval.setCheckOption(null);//清空意见
                approval.setCheckResult(null);//清空审批结果
            }else {
                model.addAttribute("flag","0");//不可审批
            }
            /*if(!"1".equals(ljExpense.getStatus()) && !"3".equals(ljExpense.getStatus())){
                model.addAttribute("flag","0");//不可审批
            }else if("3".equals(ljExpense.getStatus())){
                //财务审核未通过的
                Approval approval1 = approvalService.queryOneApprovalByUuidAndCheckType(ljExpense.getId(), "2", "1");
                if(Objects.nonNull(approval1) && "N".equals(approval1.getCheckResult())){
                    model.addAttribute("flag","1");//可审批
                    approval.setCheckOption(null);//清空意见
                    approval.setCheckResult(null);//清空审批结果
                }else {
                    model.addAttribute("flag","0");//不可审批
                }
            }else {
                model.addAttribute("flag","1");//可审批
                approval.setCheckOption(null);//清空意见
                approval.setCheckResult(null);//清空审批结果
            }*/
        }
        approval.setCheckContent("1");//报销业务
        approval.setCheckType("1");//总监审批
        reForm(model,"LjApprovalfdata", approval);
        reForm(model,"fdata",ljExpense);
        model.addAttribute("uuid",ljExpense.getId());
        model.addAttribute("expenseManName", employee.getPersonName());
        return page("showExamineLjExpenseBM");
    }


    @RequestMapping("showLjExpenseBM/{id}")//部门详情页
    public String showLjExpenseBM(@PathVariable String id,Model model){
        LjExpense ljExpense = ljExpenseService.queryOneLjExpenseById(id);
        Employee employee = employeeService.queryOneEmployee(ljExpense.getExpenseMan());//员工（报销人）
        reForm(model,"fdata",ljExpense);
        model.addAttribute("expenseManName", employee.getPersonName());
        return page("showLjExpenseBM");
    }

}
