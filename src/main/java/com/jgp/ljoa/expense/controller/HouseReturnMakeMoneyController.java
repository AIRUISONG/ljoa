package com.jgp.ljoa.expense.controller;

import com.jgp.ljoa.com.service.ApprovalService;
import com.jgp.ljoa.marketing.model.HouseReturn;
import com.jgp.ljoa.marketing.service.HouseReturnService;
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
@RequestMapping("/ljoa/expense/houseReturnMakeMoneyController")
public class HouseReturnMakeMoneyController extends JGPController {
    @Autowired
    private HouseReturnService houseReturnService;
    @Autowired
    private ApprovalService approvalService;

    //跳转退款打款
    @RequestMapping("listAllHouseReturnMakeMoney")
    public String listAllHouseReturnMakeMoney(){

        return page("listAllHouseReturnMakeMoney");
    }

//退款打款
    @RequestMapping("showHouseReturnMakeMoney/{id}")
    public String showHouseReturnMakeMoney(@PathVariable String id,Model model){
        HouseReturn houseReturn = houseReturnService.queryOneHouseReturn(id);
        model.addAttribute("uuid",houseReturn.getId());
        reForm(model,"fdata",houseReturn);
        return page("showHouseReturnMakeMoney");
    }
}
