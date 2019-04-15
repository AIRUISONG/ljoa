package com.jgp.ljoa.channel.service.impl;

import com.jgp.ljoa.channel.model.LjChannelCompany;
import com.jgp.ljoa.channel.repository.LjChannelCompanyRepository;
import com.jgp.ljoa.channel.service.LjChannelCompanyService;
import com.jgp.ljoa.common.util.ReadExcelUtil;
import com.jgp.sys.query.Operator;
import com.jgp.sys.query.QueryFilterList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.List;
import java.util.Objects;

/**
 * Created by Administrator on 2018/8/14.
 */
/*
作者  SSF
时间   2018/8/14
*/
@Service
public class LjChannelCompanyServiceImpl implements LjChannelCompanyService {
    @Autowired
    private LjChannelCompanyRepository ljChannelCompanyRepository;


    @Override
    public List<LjChannelCompany> queryAllLjChannelCompany() {
        return ljChannelCompanyRepository.readAll();
    }

    @Override
    public LjChannelCompany queryOneLjChannelCompany(String id) {

        return ljChannelCompanyRepository.read(id);
    }

    @Override
    public LjChannelCompany queryOneLjChannelCompanyByName(String Name) {
        QueryFilterList queryFilters = new QueryFilterList();
        if(Name != null){
            queryFilters.addFilter("companyName", Operator.eq ,Name);
        }
        List<LjChannelCompany> read = ljChannelCompanyRepository.read(queryFilters);
        LjChannelCompany ljChannelCompany = new LjChannelCompany();
        if(read.size() > 0){
            ljChannelCompany = read.get(0);
        }
        return ljChannelCompany;
    }

    @Transactional
    @Override
    public LjChannelCompany saveLjChannelCompany(LjChannelCompany ljChannelCompany) {
        return  ljChannelCompanyRepository.createOrUpdate(ljChannelCompany);
    }

    @Transactional
    @Override
    public void removeLjChannelCompany(String id) {
        ljChannelCompanyRepository.delete(id);
    }

    @Transactional
    @Override
    public void importData(File file) {
        ReadExcelUtil poi = new ReadExcelUtil();
        int rowsize = poi.importExcel(file);
        for (int i = 0; i <= rowsize; i++) {
            if(i > 0){
                LjChannelCompany ljChannelCompany=new LjChannelCompany();//第三方

                if(poi.getStrByNumIndex(0) != null || poi.getStrByNumIndex(0) != ""){
                    ljChannelCompany  = this.queryOneLjChannelCompanyByName(poi.getStrByNumIndex(0));
                    if(Objects.isNull(ljChannelCompany.getId())){
                        ljChannelCompany.setCompanyName(poi.getStrByNumIndex(0));//公司名称
                    }
                }
                if(poi.getStrByNumIndex(1) != null || poi.getStrByNumIndex(1) != ""){
                    ljChannelCompany.setCompanyAccount(poi.getStrByNumIndex(1));//公司账号
                }
                if(poi.getStrByNumIndex(2) != null || poi.getStrByNumIndex(2) != ""){
                    ljChannelCompany.setAddress(poi.getStrByNumIndex(2));//地址
                }
                if(poi.getStrByNumIndex(3) != null || poi.getStrByNumIndex(3) != ""){
                    ljChannelCompany.setLicNo(poi.getStrByNumIndex(3));//执照号码
                }
                if(poi.getStrByNumIndex(4) != null || poi.getStrByNumIndex(4) != ""){
                    ljChannelCompany.setCompanyBank(poi.getStrByNumIndex(4));//开户行
                }
                if(poi.getStrByNumIndex(5) != null || poi.getStrByNumIndex(5) != ""){
                    ljChannelCompany.setDeputeName(poi.getStrByNumIndex(5));//委托人姓名
                }
                if(poi.getStrByNumIndex(6) != null || poi.getStrByNumIndex(6) != ""){
                    ljChannelCompany.setDeputeAccount(poi.getStrByNumIndex(6));//委托人账号
                }
                if(poi.getStrByNumIndex(7) != null || poi.getStrByNumIndex(7) != ""){
                    ljChannelCompany.setDeputeIdentity(poi.getStrByNumIndex(7));//委托收款人身份证号
                }
                if(poi.getStrByNumIndex(8) != null || poi.getStrByNumIndex(8) != ""){
                    ljChannelCompany.setDeputeBank(poi.getStrByNumIndex(8));//委托人开户行
                }
                if(ljChannelCompany.getId()==null){
                    this.saveLjChannelCompany(ljChannelCompany);
                }
            }
            poi.nextRow();
        }
    }
}
