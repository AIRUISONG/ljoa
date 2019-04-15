package com.jgp.ljoa.expense.controller;

import com.jgp.sys.controller.JGPController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Administrator on 2018/10/25.
 */
/*
作者  SSF
时间   2018/10/25
*/
//设计报销
@Controller
@RequestMapping("/ljoa/expense/LjExpenseDesignController")
public class LjExpenseDesignController extends JGPController {

    //设计总监报销未审核界面
    @RequestMapping("listGroupLjExpenseDesignNo")
    public String listGroupLjExpenseDesignNo(){

        return page("listGroupLjExpenseDesignNo");
    }
    //设计总监报销已审核界面
    @RequestMapping("listGroupLjExpenseDesignYES")
    public String listGroupLjExpenseDesignYES(){

        return page("listGroupLjExpenseDesignYES");
    }

    //创意总监报销未审核界面
    @RequestMapping("listGroupLjExpenseOriginalityNO")
    public String listGroupLjExpenseOriginalityNO(){

        return page("listGroupLjExpenseOriginalityNO");
    }

    //创意总监报销已审核审核界面
    @RequestMapping("listGroupLjExpenseOriginalityYES")
    public String listGroupLjExpenseOriginalityYES(){

        return page("listGroupLjExpenseOriginalityYES");
    }

}
