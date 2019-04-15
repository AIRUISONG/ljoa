package com.jgp.ljoa.marketing.service.impl;/**
 * Created by Administrator on 2018/7/23.
 */

import com.jgp.ljoa.marketing.model.HouseReturn;
import com.jgp.ljoa.marketing.repository.HouseReturnRepository;
import com.jgp.ljoa.marketing.service.HouseReturnService;
import com.jgp.sys.query.Operator;
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
 * 时间   2018/7/23
 */
@Service
public class HouseReturnServiceImpl implements HouseReturnService{
    @Autowired
    private HouseReturnRepository houseReturnRepository;

    @Transactional
    @Override
    public HouseReturn saveHouseReturn(HouseReturn houseReturn) {
        return houseReturnRepository.createOrUpdate(houseReturn);
    }

    @Override
    public HouseReturn queryOneHouseReturn(String id) {
        return houseReturnRepository.read(id);
    }


    @Override
    public HouseReturn queryOneHouseReturnByHouseId(String houseId) {
        QueryFilterList list = new QueryFilterList();
        list.addFilter("houseUuid","eq",houseId);
        List<HouseReturn> read = houseReturnRepository.read(list);
        return read.size() > 0 ? read.get(0) : null;
    }


    @Override
    public List<HouseReturn> queryGroupHouseReturn(HouseReturn houseReturn, Pager pager) {
        QueryFilterList list = new QueryFilterList();
        if(Objects.nonNull(houseReturn.getHouseUuid()) && StringUtils.isNotEmpty(houseReturn.getHouseUuid())){
            list.addFilter("houseUuid","eq",houseReturn.getHouseUuid());
        }
        if(Objects.nonNull(houseReturn.getApplyName()) && StringUtils.isNotEmpty(houseReturn.getApplyName())){
            list.addFilter("applyName","like",houseReturn.getApplyName());
        }
        if(Objects.nonNull(houseReturn.getIdentityCode()) && StringUtils.isNotEmpty(houseReturn.getIdentityCode())){
            list.addFilter("identityCode","like",houseReturn.getIdentityCode());
        }
        if(Objects.nonNull(houseReturn.getBankCard()) && StringUtils.isNotEmpty(houseReturn.getBankCard())){
            list.addFilter("bankCard","like",houseReturn.getBankCard());
        }
        if(Objects.nonNull(houseReturn.getApprovalStatus()) && StringUtils.isNotEmpty(houseReturn.getApprovalStatus())){
            list.addFilter("approvalStatus","eq",houseReturn.getApprovalStatus());
        }
        if(Objects.nonNull(houseReturn.getMoneyStatus()) && StringUtils.isNotEmpty(houseReturn.getMoneyStatus())){
            list.addFilter("moneyStatus","eq",houseReturn.getMoneyStatus());
        }
        return houseReturnRepository.read(list, pager);
    }

    @Transactional
    @Override
    public void removeOneHouseReturn(String id) {
        houseReturnRepository.delete(id);
    }


    @Override
    public List<HouseReturn> queryGroupHouseReturnInfo(List<String> list, HouseReturn houseReturn, Pager pager) {
        QueryFilterList queryFilters = new QueryFilterList();
        if(Objects.nonNull(houseReturn.getHouseUuid()) && StringUtils.isNotEmpty(houseReturn.getHouseUuid())){
            queryFilters.addFilter("houseUuid","eq",houseReturn.getHouseUuid());
        }
        if(Objects.nonNull(houseReturn.getApplyName()) && StringUtils.isNotEmpty(houseReturn.getApplyName())){
            queryFilters.addFilter("applyName","like",houseReturn.getApplyName());
        }
        if(Objects.nonNull(houseReturn.getIdentityCode()) && StringUtils.isNotEmpty(houseReturn.getIdentityCode())){
            queryFilters.addFilter("identityCode","like",houseReturn.getIdentityCode());
        }
        if(Objects.nonNull(houseReturn.getBankCard()) && StringUtils.isNotEmpty(houseReturn.getBankCard())){
            queryFilters.addFilter("bankCard","like",houseReturn.getBankCard());
        }
        if(Objects.nonNull(houseReturn.getMoneyStatus()) && StringUtils.isNotEmpty(houseReturn.getMoneyStatus())){
            queryFilters.addFilter("moneyStatus","eq",houseReturn.getMoneyStatus());
        }
        queryFilters.addFilter("approvalStatus","in",list);
        return houseReturnRepository.read(queryFilters, pager);
    }

    @Override
    public List<HouseReturn> queryGroupHouseReturnInfoByHouseUuid(List<String> list, HouseReturn houseReturn, Pager pager) {
        QueryFilterList queryFilters = new QueryFilterList();
      /*  if(Objects.nonNull(houseReturn.getHouseUuid()) && StringUtils.isNotEmpty(houseReturn.getHouseUuid())){
            queryFilters.addFilter("houseUuid","eq",houseReturn.getHouseUuid());
        }*/
        if(Objects.nonNull(houseReturn.getApplyName()) && StringUtils.isNotEmpty(houseReturn.getApplyName())){
            queryFilters.addFilter("applyName","like",houseReturn.getApplyName());
        }
        if(Objects.nonNull(houseReturn.getIdentityCode()) && StringUtils.isNotEmpty(houseReturn.getIdentityCode())){
            queryFilters.addFilter("identityCode","like",houseReturn.getIdentityCode());
        }
        if(Objects.nonNull(houseReturn.getBankCard()) && StringUtils.isNotEmpty(houseReturn.getBankCard())){
            queryFilters.addFilter("bankCard","like",houseReturn.getBankCard());
        }
        if(Objects.nonNull(houseReturn.getMoneyStatus()) && StringUtils.isNotEmpty(houseReturn.getMoneyStatus())){
            queryFilters.addFilter("moneyStatus","eq",houseReturn.getMoneyStatus());
        }
        if(Objects.nonNull(houseReturn.getApprovalStatus())){
            queryFilters.addFilter("approvalStatus",Operator.eq,houseReturn.getApprovalStatus());
        }

       if(list != null && list.size() > 0){
           queryFilters.addFilter("houseUuid", Operator.in,list);
       }
        List<HouseReturn> read = houseReturnRepository.read(queryFilters, pager);
        return read;
    }

    @Override
    public List<HouseReturn> queryGroupHouseReturnInfoById(List<String> id, Pager pager) {
        QueryFilterList queryFilters=new QueryFilterList();
        if(id.size() > 0){
            queryFilters.addFilter("id",Operator.in,id);
         }
        return houseReturnRepository.read(queryFilters,pager);
    }
}
