package com.jgp.ljoa.expense.controller;

import com.jgp.ljoa.expense.service.LjExpenseService;
import com.jgp.sys.controller.JGPController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/*
项目  ljoa
作者  SSF
时间   2018/11/27
*/
@Controller
@RequestMapping("/ljoa/expense/LjExpenseRSController")
public class LjExpenseRSController extends JGPController {

    @Autowired
    private LjExpenseService ljExpenseService;

    //综合管理经理报销已审批
    @RequestMapping("listGroupLjExpenseByRSYES")
    public String listGroupLjExpenseByRS(){

        return page("listGroupLjExpenseByRSYES");
    }
    //综合管理经理报销未审批
    @RequestMapping("listGroupLjExpenseByRSNO")
    public String listGroupLjExpenseByRSNO(){

        return page("listGroupLjExpenseByRSNO");
    }



}
