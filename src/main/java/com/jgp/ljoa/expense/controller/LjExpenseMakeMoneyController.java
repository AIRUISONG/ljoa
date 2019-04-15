package com.jgp.ljoa.expense.controller;

import com.jgp.ljoa.com.service.ApprovalService;
import com.jgp.ljoa.expense.model.LjExpense;
import com.jgp.ljoa.expense.service.LjExpenseService;
import com.jgp.ljoa.hr.service.EmployeeService;
import com.jgp.security.admin.service.AdminUserService;
import com.jgp.sys.controller.JGPController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Administrator on 2018/8/1.
 */
/*
作者  SSF
时间   2018/8/1
*/
@Controller
@RequestMapping("/ljoa/expense/ljExpenseMakeMoneyController")
public class LjExpenseMakeMoneyController extends JGPController {
    @Autowired
    private LjExpenseService ljExpenseService;
    @Autowired
    private ApprovalService approvalService;
    @Autowired
    private AdminUserService adminUserService;
    @Autowired
    private EmployeeService employeeService;

    //跳转报销打款信息
    @RequestMapping("listAllLjExpenseMakeMoney")
    public String listAllLjExpenseMakeMoney(){

        return page("listAllLjExpenseMakeMoney");
    }

    //跳转打款
    @RequestMapping("showLjExpenseMakeMoney/{id}")
    public String showLjExpenseMakeMoney(@PathVariable String id,Model model){
        LjExpense ljExpense = ljExpenseService.queryOneLjExpenseById(id);

        model.addAttribute("uuid",id);
        reForm(model,"fdata",ljExpense);
        return page("showLjExpenseMakeMoney");
    }


}
