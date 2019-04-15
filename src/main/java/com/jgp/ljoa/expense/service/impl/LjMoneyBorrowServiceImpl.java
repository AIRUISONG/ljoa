package com.jgp.ljoa.expense.service.impl;

import com.jgp.ljoa.expense.model.LjMoneyBorrow;
import com.jgp.ljoa.expense.repository.LjMoneyBorrowRepository;
import com.jgp.ljoa.expense.service.LjMoneyBorrowService;
import com.jgp.sys.query.Operator;
import com.jgp.sys.query.OrderDirection;
import com.jgp.sys.query.OrderList;
import com.jgp.sys.query.QueryFilterList;
import com.jgp.sys.ui.Pager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/*
项目  ljoa
作者  SSF
时间   2018/11/28
*/
@Service
public class LjMoneyBorrowServiceImpl implements LjMoneyBorrowService {

    @Autowired
    private LjMoneyBorrowRepository ljMoneyBorrowRepository;

    @Transactional
    @Override
    public LjMoneyBorrow saveLjMoneyBorrow(LjMoneyBorrow ljMoneyBorrow) {
        return ljMoneyBorrowRepository.createOrUpdate(ljMoneyBorrow);
    }

    @Transactional
    @Override
    public void removeLjMoneyBorrow(String id) {
        ljMoneyBorrowRepository.delete(id);
    }

    @Override
    public List<LjMoneyBorrow> queryAllLjMoneyBorrow(Pager pager) {
        return  ljMoneyBorrowRepository.readAll();
    }

    @Override
    public LjMoneyBorrow queryOneLjMoneyBorrowById(String id) {
        return ljMoneyBorrowRepository.read(id);
    }

    @Override
    public List<LjMoneyBorrow> queryGroupLjMoneyBorrow(LjMoneyBorrow ljMoneyBorrow,Pager pager) {
        QueryFilterList queryFilterList = new QueryFilterList();
        if(ljMoneyBorrow!=null){
            if(Objects.nonNull(ljMoneyBorrow.getBorrowMan())){
                queryFilterList.addFilter("borrowMan", Operator.eq,ljMoneyBorrow.getBorrowMan());//借款人
            }
            if(Objects.nonNull(ljMoneyBorrow.getStatus())){
                queryFilterList.addFilter("status", Operator.eq,ljMoneyBorrow.getStatus());//状态
            }
            if(Objects.nonNull(ljMoneyBorrow.getBorrowOrg())){
                queryFilterList.addFilter("borrowOrg", Operator.eq,ljMoneyBorrow.getBorrowOrg());//部门
            }
            if(Objects.nonNull(ljMoneyBorrow.getPayType())){
                queryFilterList.addFilter("payType", Operator.eq,ljMoneyBorrow.getPayType());//借款类型
            }
            if(Objects.nonNull(ljMoneyBorrow.getBorrowDate())){
                queryFilterList.addFilter("borrowDate", Operator.like,ljMoneyBorrow.getBorrowDate());
            }

        }
        OrderList orders=new OrderList();
        orders.addOrder("createDatetime", OrderDirection.DESC);

        return ljMoneyBorrowRepository.read(queryFilterList,orders,pager);
    }

    @Override
    public List<LjMoneyBorrow> queryGroupLjMoneyBorrowByStatus(LjMoneyBorrow ljMoneyBorrow, List<String> list, Pager p) {
        QueryFilterList queryFilterList = new QueryFilterList();
        if(ljMoneyBorrow!=null){
            if(Objects.nonNull(ljMoneyBorrow.getBorrowMan())){
                queryFilterList.addFilter("borrowMan", Operator.eq,ljMoneyBorrow.getBorrowMan());//借款人
            }
            if(Objects.nonNull(ljMoneyBorrow.getBorrowOrg())){
                queryFilterList.addFilter("borrowOrg", Operator.eq,ljMoneyBorrow.getBorrowOrg());//部门
            }
            if(Objects.nonNull(ljMoneyBorrow.getPayType())){
                queryFilterList.addFilter("payType", Operator.eq,ljMoneyBorrow.getPayType());//借款类型
            }
            if(Objects.nonNull(ljMoneyBorrow.getBorrowDate())){
                queryFilterList.addFilter("borrowDate", Operator.like,ljMoneyBorrow.getBorrowDate());
            }
        }
        queryFilterList.addFilter("status", Operator.nin,list);//状态
        OrderList orders=new OrderList();
        orders.addOrder("createDatetime", OrderDirection.DESC);
        return ljMoneyBorrowRepository.read(queryFilterList,orders,p);
    }
}
