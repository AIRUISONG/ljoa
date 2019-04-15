package com.jgp.ljoa.expense.service;/*
项目  ljoa
作者  SSF
时间   2018/11/28
*/

import com.jgp.ljoa.expense.model.LjMoneyPayRecord;
import com.jgp.sys.ui.Pager;

import java.util.List;

public interface LjMoneyPayRecordService {

    //保存
    public LjMoneyPayRecord saveLjMoneyPayRecord(LjMoneyPayRecord ljMoneyPayRecord);
    //删除
    public void removeLjMoneyPayRecordById(String id);
    //批量删除
    public void removeLjMoneyPayRecordByList(List<LjMoneyPayRecord> ljMoneyPayRecords);
    //全查
    public List<LjMoneyPayRecord> queryAllLjMoneyPayRecord(Pager pager);
    //单查
    public LjMoneyPayRecord queryOneLjMoneyPayRecordById(String id);
    //条件查询
    public List<LjMoneyPayRecord> queryGroupLjMoneyPayRecord(LjMoneyPayRecord ljMoneyPayRecord,Pager pager);
}
