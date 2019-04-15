package com.jgp.ljoa.com.service.impl;/**
 * Created by Administrator on 2018/7/5.
 */

import com.jgp.ljoa.channel.model.LjHouseInfo;
import com.jgp.ljoa.com.model.Approval;
import com.jgp.ljoa.com.repository.ApprovalRepository;
import com.jgp.ljoa.com.service.ApprovalService;
import com.jgp.sys.query.QueryFilterList;
import com.jgp.sys.ui.Pager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * 项目   ljoa
 * 作者   liujinxu
 * 时间   2018/7/5
 */
@Service
public class ApprovalServiceImpl implements ApprovalService {
    @Autowired
    private ApprovalRepository approvalRepository;


    @Override
    public List<Approval> queryGroupApproval(Approval approval, Pager pager) {
        QueryFilterList list = new QueryFilterList();
        if(Objects.nonNull(approval.getBusiUuid()) && StringUtils.isNotEmpty(approval.getBusiUuid())){
            list.addFilter("busiUuid","eq",approval.getBusiUuid());
        }
        if(Objects.nonNull(approval.getCheckContent()) && StringUtils.isNotEmpty(approval.getCheckContent())){
            list.addFilter("checkContent","eq",approval.getCheckContent());
        }
        if(Objects.nonNull(approval.getCheckType()) && StringUtils.isNotEmpty(approval.getCheckType())){
            list.addFilter("checkType","eq",approval.getCheckType());
        }
        return approvalRepository.read(list,pager);
    }

    @Override
    public Approval queryOneApproval(Approval approval) {
        QueryFilterList list = new QueryFilterList();
        if(Objects.nonNull(approval.getBusiUuid()) && StringUtils.isNotEmpty(approval.getBusiUuid())){
            list.addFilter("busiUuid","eq",approval.getBusiUuid());
        }
        if(Objects.nonNull(approval.getCheckContent()) && StringUtils.isNotEmpty(approval.getCheckContent())){
            list.addFilter("checkContent","eq",approval.getCheckContent());
        }
        if(Objects.nonNull(approval.getCheckType()) && StringUtils.isNotEmpty(approval.getCheckType())){
            list.addFilter("checkType","eq",approval.getCheckType());
        }
        List<Approval> read = approvalRepository.read(list);
        return read.size() > 0 ? read.get(0) : new Approval();
    }


    @Override
    public Approval queryOneApprovalByBusiUuid(String busiUuid) {
        QueryFilterList list = new QueryFilterList();
        list.addFilter("busiUuid","eq",busiUuid);
        List<Approval> read = approvalRepository.read(list);
        return read.size() > 0 ? read.get(0) : null;
    }

    @Transactional
    @Override
    public Approval saveApproval(Approval approval) {
        return approvalRepository.createOrUpdate(approval);
    }


    @Override
    public Approval queryOneApprovalByUuidAndCheckType(String uuid, String checkType,String checkContent) {
        QueryFilterList list=new QueryFilterList();
        list.addFilter("busiUuid","eq",uuid);
        list.addFilter("checkType","eq",checkType);
        list.addFilter("checkContent","eq",checkContent);
        List<Approval> read = approvalRepository.read(list);
        return Objects.nonNull(read) && read.size() > 0 ? read.get(0) : null;
    }


    @Override
    public List<Approval> queryApprovalByBusiUuid(String busiUuid , Pager pager) {
        QueryFilterList queryFilterList = new QueryFilterList();
        queryFilterList.addFilter("busiUuid","eq",busiUuid);
        return approvalRepository.read(queryFilterList,pager);
    }


    @Override
    public List<LjHouseInfo> queryGeneralManagerQueryHouseSaleInfo(LjHouseInfo ljHouseInfo, Pager pager) {

        return null;
    }

    @Override
    public Approval queryOneApprovalById(String id) {
        return approvalRepository.read(id);
    }

    @Override
    public void removeApproval(List<Approval> h) {

        for(Approval aa:h){
            approvalRepository.delete(aa.getId());
        }

    }
}
