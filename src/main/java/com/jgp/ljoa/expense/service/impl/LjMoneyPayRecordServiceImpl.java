package com.jgp.ljoa.expense.service.impl;

import com.jgp.ljoa.expense.model.LjMoneyPayRecord;
import com.jgp.ljoa.expense.repository.LjMoneyPayRecordRepository;
import com.jgp.ljoa.expense.service.LjMoneyPayRecordService;
import com.jgp.sys.query.Operator;
import com.jgp.sys.query.OrderDirection;
import com.jgp.sys.query.OrderList;
import com.jgp.sys.query.QueryFilterList;
import com.jgp.sys.ui.Pager;
import org.apache.commons.lang3.StringUtils;
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
public class LjMoneyPayRecordServiceImpl implements LjMoneyPayRecordService {

    @Autowired
    private LjMoneyPayRecordRepository ljMoneyPayRecordRepository;

    @Transactional
    @Override
    public LjMoneyPayRecord saveLjMoneyPayRecord(LjMoneyPayRecord ljMoneyPayRecord) {
        return ljMoneyPayRecordRepository.createOrUpdate(ljMoneyPayRecord);
    }

    @Transactional
    @Override
    public void removeLjMoneyPayRecordById(String id) {
        ljMoneyPayRecordRepository.delete(id);
    }
    @Transactional
    @Override
    public void removeLjMoneyPayRecordByList(List<LjMoneyPayRecord> ljMoneyPayRecords) {
        ljMoneyPayRecordRepository.deleteInBatch(ljMoneyPayRecords);
    }

    @Override
    public List<LjMoneyPayRecord> queryAllLjMoneyPayRecord(Pager pager) {
        return ljMoneyPayRecordRepository.readAll();
    }

    @Override
    public LjMoneyPayRecord queryOneLjMoneyPayRecordById(String id) {
        return ljMoneyPayRecordRepository.read(id);
    }

    @Override
    public List<LjMoneyPayRecord> queryGroupLjMoneyPayRecord(LjMoneyPayRecord ljMoneyPayRecord, Pager pager) {
        QueryFilterList queryFilterList = new QueryFilterList();
        if(ljMoneyPayRecord!=null){
            if(Objects.nonNull(ljMoneyPayRecord.getPayMan())){//支出人
                queryFilterList.addFilter("payMan", Operator.eq,ljMoneyPayRecord.getPayMan());
            }
            if(Objects.nonNull(ljMoneyPayRecord.getPayOrg())){//支出部门
                queryFilterList.addFilter("payOrg", Operator.eq,ljMoneyPayRecord.getPayOrg());
            }
            if(StringUtils.isNotEmpty(ljMoneyPayRecord.getPayType())){//支出类型
                queryFilterList.addFilter("payType", Operator.eq,ljMoneyPayRecord.getPayType());
            }
            if(StringUtils.isNotEmpty(ljMoneyPayRecord.getBorrowUuid())){//备用金
                queryFilterList.addFilter("borrowUuid", Operator.eq,ljMoneyPayRecord.getBorrowUuid());
            }

        }
        OrderList orders=new OrderList();
        orders.addOrder("createDatetime", OrderDirection.DESC);
        return ljMoneyPayRecordRepository.read(queryFilterList,orders,pager);
    }
}
