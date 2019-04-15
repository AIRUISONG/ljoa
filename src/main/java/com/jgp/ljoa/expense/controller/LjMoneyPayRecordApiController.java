package com.jgp.ljoa.expense.controller;

import com.jgp.common.annotation.UIParam;
import com.jgp.ljoa.expense.model.LjMoneyPayRecord;
import com.jgp.ljoa.expense.service.LjMoneyBorrowService;
import com.jgp.ljoa.expense.service.LjMoneyPayRecordService;
import com.jgp.sys.controller.JGPController;
import com.jgp.sys.ui.GridResult;
import com.jgp.sys.ui.Pager;
import com.jgp.sys.ui.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/*
项目  ljoa
作者  SSF
时间   2018/11/28
*/
@RestController
@RequestMapping("/ljoa/expense/LjMoneyPayRecordApiController")
public class LjMoneyPayRecordApiController extends JGPController {

    @Autowired
    private LjMoneyPayRecordService ljMoneyPayRecordService;
    @Autowired
    private LjMoneyBorrowService ljMoneyBorrowService;

    @RequestMapping("saveLjMoneyPayRecord")//保存
    public Result saveLjMoneyPayRecord(LjMoneyPayRecord ljMoneyPayRecord){
        LjMoneyPayRecord ljMoneyPayRecord1 = ljMoneyPayRecordService.saveLjMoneyPayRecord(ljMoneyPayRecord);
        return ajaxRe(true).addData("id",ljMoneyPayRecord1.getId());
    }

    @RequestMapping("listGroupLjMoneyPayRecordByUUID/{id}")//根据备用金主键查询
    public Result listGroupLjMoneyPayRecordByUUID(LjMoneyPayRecord ljMoneyPayRecord, @PathVariable String id , @UIParam("pager")Pager pager){
        ljMoneyPayRecord.setBorrowUuid(id);
        List<LjMoneyPayRecord> ljMoneyPayRecords = ljMoneyPayRecordService.queryGroupLjMoneyPayRecord(ljMoneyPayRecord, pager);
        List<GridResult.Category> categories = new ArrayList<>();
        categories.add(new GridResult.Category("支出标题", "payItem"));
        categories.add(new GridResult.Category("借款金额", "payMoney"));
        categories.add(new GridResult.Category("支出日期", "payDate"));
        categories.add(new GridResult.Category("支出类型", "payType","LJOA.EXPENSE_TYPE"));
        return ajaxReGrid("gdata", categories, ljMoneyPayRecords, pager);


    }

    @RequestMapping("removeLjMoneyPayRecordById/{id}")//删除
    public Result removeLjMoneyPayRecordById(@PathVariable String id){
      /*  LjMoneyPayRecord ljMoneyPayRecord = ljMoneyPayRecordService.queryOneLjMoneyPayRecordById(id);
        LjMoneyBorrow ljMoneyBorrow = new LjMoneyBorrow();
        if(ljMoneyPayRecord != null){
            ljMoneyBorrow = ljMoneyBorrowService.queryOneLjMoneyBorrowById(ljMoneyPayRecord.getBorrowUuid());
        }
        String statr = "";
        if(ljMoneyBorrow != null){
            statr = ljMoneyBorrow.getStatus();
        }*/
        ljMoneyPayRecordService.removeLjMoneyPayRecordById(id);
        return ajaxRe(true);
    }
}
