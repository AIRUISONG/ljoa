package com.jgp.ljoa.expense.service;/*
项目  ljoa
作者  SSF
时间   2018/11/28
*/

import com.jgp.ljoa.expense.model.LjMoneyBorrow;
import com.jgp.sys.ui.Pager;

import java.util.List;

public interface LjMoneyBorrowService {

    //保存
    public LjMoneyBorrow saveLjMoneyBorrow(LjMoneyBorrow ljMoneyBorrow);
    //删除
    public void removeLjMoneyBorrow(String id);
    //全查
    public List<LjMoneyBorrow> queryAllLjMoneyBorrow(Pager pager);
    //单查
    public LjMoneyBorrow queryOneLjMoneyBorrowById(String id);
    //条件查询
    public List<LjMoneyBorrow> queryGroupLjMoneyBorrow(LjMoneyBorrow ljMoneyBorrow,Pager p);
    //条件查询(非)
    public List<LjMoneyBorrow> queryGroupLjMoneyBorrowByStatus(LjMoneyBorrow ljMoneyBorrow,List<String> list,Pager p);


}
