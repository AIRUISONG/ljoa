package com.jgp.ljoa.expense.controller;

import com.jgp.ljoa.expense.model.LjMoneyBorrow;
import com.jgp.ljoa.expense.model.LjMoneyPayRecord;
import com.jgp.ljoa.expense.service.LjMoneyBorrowService;
import com.jgp.ljoa.expense.service.LjMoneyPayRecordService;
import com.jgp.sys.controller.JGPController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/*
项目  ljoa
作者  SSF
时间   2018/11/28
*/
@Controller
@RequestMapping("/ljoa/expense/LjMoneyPayRecordController")
public class LjMoneyPayRecordController extends JGPController {
    @Autowired
    private LjMoneyPayRecordService ljMoneyPayRecordService;
    @Autowired
    private LjMoneyBorrowService ljMoneyBorrowService;

    //添加页面
    @RequestMapping("addLjMoneyPayRecord/{id}")
    public String addLjMoneyPayRecord(@PathVariable String id,Model model){
        LjMoneyBorrow ljMoneyBorrow = ljMoneyBorrowService.queryOneLjMoneyBorrowById(id);
        LjMoneyPayRecord ljMoneyPayRecord=new LjMoneyPayRecord();
        ljMoneyPayRecord.setPayMan(ljMoneyBorrow.getBorrowMan());
        ljMoneyPayRecord.setPayOrg(ljMoneyBorrow.getBorrowOrg());
        ljMoneyPayRecord.setBorrowUuid(id);
        reForm(model,"fdata",ljMoneyPayRecord);
        return page("addLjMoneyPayRecord");
    }
    //修改页面
    @RequestMapping("editLjMoneyPayRecord/{id}")
    public String editLjMoneyPayRecord(@PathVariable String id, Model model){
        LjMoneyPayRecord ljMoneyPayRecord = ljMoneyPayRecordService.queryOneLjMoneyPayRecordById(id);
        if(ljMoneyPayRecord!=null){
            model.addAttribute("id",ljMoneyPayRecord.getId());
            reForm(model,"fdata",ljMoneyPayRecord);
        }

        return page("editLjMoneyPayRecord");
    }
    //详情页
    @RequestMapping("showLjMoneyPayRecord/{id}")
    public String showLjMoneyPayRecord(@PathVariable String id, Model model){
        LjMoneyPayRecord ljMoneyPayRecord = ljMoneyPayRecordService.queryOneLjMoneyPayRecordById(id);
        if(ljMoneyPayRecord!=null){
            model.addAttribute("id",ljMoneyPayRecord.getId());
            reForm(model,"fdata",ljMoneyPayRecord);
        }

        return page("showLjMoneyPayRecord");
    }

    //Add备用金主键列表
    @RequestMapping("listGroupLjMoneyPayRecordByUUID/{id}")
    public String listGroupLjMoneyPayRecordByUUID(@PathVariable String id,Model model){
        LjMoneyBorrow ljMoneyBorrow = ljMoneyBorrowService.queryOneLjMoneyBorrowById(id);
        model.addAttribute("id",id);
        if(ljMoneyBorrow != null){
            model.addAttribute("status",ljMoneyBorrow.getStatus());
        }
        reForm(model,"fdataLB",ljMoneyBorrow);
        return page("listGroupLjMoneyPayRecordByUUID");
    }

    //根据备用金主键列表
    @RequestMapping("listGroupLjMoneyPayRecordByUUIDAT/{id}")
    public String listGroupLjMoneyPayRecordByUUIDAT(@PathVariable String id,Model model){
        LjMoneyBorrow ljMoneyBorrow = ljMoneyBorrowService.queryOneLjMoneyBorrowById(id);
        model.addAttribute("id",id);
        reForm(model,"fdataLB",ljMoneyBorrow);
        return page("listGroupLjMoneyPayRecordByUUIDAT");
    }


}
