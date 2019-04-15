package com.jgp.ljoa.channel.service.impl;

import com.jgp.ljoa.channel.model.LjChannelCompany;
import com.jgp.ljoa.channel.model.LjHouseInfo;
import com.jgp.ljoa.channel.model.LjHouseSaleInfo;
import com.jgp.ljoa.channel.model.LjProjectInfo;
import com.jgp.ljoa.channel.repository.LjHouseInfoRepository;
import com.jgp.ljoa.channel.repository.LjHouseSaleInfoRepository;
import com.jgp.ljoa.channel.service.LjChannelCompanyService;
import com.jgp.ljoa.channel.service.LjHouseInfoService;
import com.jgp.ljoa.channel.service.LjHouseSaleInfoService;
import com.jgp.ljoa.channel.service.LjProjectInfoService;
import com.jgp.ljoa.com.service.ApprovalService;
import com.jgp.ljoa.common.util.ReadExcelUtil;
import com.jgp.ljoa.hr.model.Employee;
import com.jgp.ljoa.hr.service.EmployeeService;
import com.jgp.ljoa.marketing.model.CustomerInfo;
import com.jgp.ljoa.marketing.model.MarketingSaleInfo;
import com.jgp.ljoa.marketing.repository.MarketingSaleInfoRepository;
import com.jgp.ljoa.marketing.service.CustomerInfoService;
import com.jgp.ljoa.marketing.service.HouseReturnService;
import com.jgp.security.admin.model.AdminUser;
import com.jgp.security.admin.service.AdminUserService;
import com.jgp.sys.model.Attribute;
import com.jgp.sys.query.Operator;
import com.jgp.sys.query.OrderDirection;
import com.jgp.sys.query.OrderList;
import com.jgp.sys.query.QueryFilterList;
import com.jgp.sys.service.AttributeService;
import com.jgp.sys.ui.Pager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created by Administrator on 2018/7/3.
 */
/*
作者  SSF
时间   2018/7/3
*/
@Service
public class LjHouseInfoServiceImpl implements LjHouseInfoService {
    @Autowired
    private LjHouseInfoRepository ljHouseInfoRepository;
    @Autowired
    private LjHouseSaleInfoRepository ljHouseSaleInfoRepository;
    @Autowired
    private MarketingSaleInfoRepository marketingSaleInfoRepository;
    @Autowired
    private CustomerInfoService customerInfoService;
    @Autowired
    private LjProjectInfoService ljProjectInfoService;
    @Autowired
    private LjHouseSaleInfoService ljHouseSaleInfoService;
    @Autowired
    private AttributeService attributeService;
    @Autowired
    private LjChannelCompanyService ljChannelCompanyService;
    @Autowired
    private HouseReturnService houseReturnService;
    @Autowired
    private ApprovalService approvalService;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private AdminUserService adminuserService;



    @Override
    public List<LjHouseInfo>   queryGroupLjHouseInfoByUUId(String uuid,LjHouseInfo ljHouseInfo, String houseType,Pager pager) {
        QueryFilterList list=new QueryFilterList();
        list.addFilter("projectUuid","eq",uuid);
        list.addFilter("houseType","eq",houseType);
        if(StringUtils.isNotBlank(ljHouseInfo.getRoomType())){
            list.addFilter("roomType","eq",ljHouseInfo.getRoomType());
        }
        if(StringUtils.isNotBlank(ljHouseInfo.getStatus())){
            list.addFilter("status","eq",ljHouseInfo.getStatus());
        }
        /*if(Objects.nonNull(ljHouseInfo.getSaleMoney())){
            list.addFilter("saleMoney","eq",ljHouseInfo.getSaleMoney());
        }*/
        if(StringUtils.isNotEmpty(ljHouseInfo.getBuildingNo())){
            list.addFilter("buildingNo","like",ljHouseInfo.getBuildingNo());
        }
        if(StringUtils.isNotEmpty(ljHouseInfo.getUnitNo())){
            list.addFilter("unitNo","like",ljHouseInfo.getUnitNo());
        }
        if(StringUtils.isNotEmpty(ljHouseInfo.getRoomNo())){
            list.addFilter("roomNo","like",ljHouseInfo.getRoomNo());
        }
        OrderList orders = new OrderList();
//        orders.addOrder("createDatetime", OrderDirection.ASC);
        orders.addOrder("buildingNo", OrderDirection.ASC);
        orders.addOrder("unitNo", OrderDirection.ASC);
        orders.addOrder("roomNo", OrderDirection.ASC);
        return ljHouseInfoRepository.read(list,orders,pager);
    }

    @Override
    public List<LjHouseInfo> queryGroupLjHouseInfo(String uuid, LjHouseInfo ljHouseInfo, Pager pager) {
        QueryFilterList list=new QueryFilterList();
        list.addFilter("projectUuid","eq",uuid);
        if(StringUtils.isNotBlank(ljHouseInfo.getRoomType())){
            list.addFilter("roomType","eq",ljHouseInfo.getRoomType());
        }
        if(StringUtils.isNotBlank(ljHouseInfo.getStatus())){
            list.addFilter("status","eq",ljHouseInfo.getStatus());
        }
        if(StringUtils.isNotEmpty(ljHouseInfo.getBuildingNo())){
            list.addFilter("buildingNo","like",ljHouseInfo.getBuildingNo());
        }
        if(StringUtils.isNotEmpty(ljHouseInfo.getUnitNo())){
            list.addFilter("unitNo","like",ljHouseInfo.getUnitNo());
        }
        if(StringUtils.isNotEmpty(ljHouseInfo.getRoomNo())){
            list.addFilter("roomNo","like",ljHouseInfo.getRoomNo());
        }
        return ljHouseInfoRepository.read(list,pager);
    }

    @Override
    public List<LjHouseInfo> queryGroupLjHouseInfoByUUID(String uuid) {
        QueryFilterList list=new QueryFilterList();
        list.addFilter("projectUuid","eq",uuid);
        return ljHouseInfoRepository.read(list);
    }

    @Override
    public List<LjHouseInfo> queryGroupLjHouseInfoByUUID(String uuid,LjHouseInfo ljHouseInfo, Pager pager) {
        QueryFilterList list=new QueryFilterList();
        list.addFilter("projectUuid","eq",uuid);
        if(StringUtils.isNotEmpty(ljHouseInfo.getCompanyChargeStatus())){
            list.addFilter("companyChargeStatus","eq",ljHouseInfo.getCompanyChargeStatus());
        }
        return ljHouseInfoRepository.read(list,pager);
    }

    @Transactional
    @Override
    public LjHouseInfo saveLjHouseInfo(LjHouseInfo ljHouseInfo) {
        return ljHouseInfoRepository.createOrUpdate(ljHouseInfo);
    }

    @Override
    public LjHouseInfo queryOneLjHouseInfoByid(String id) {
        return ljHouseInfoRepository.read(id);
    }
    @Transactional
    @Override
    public void removeLjHouseInfo(String id) {
        ljHouseInfoRepository.delete(id);
    }
    @Transactional
    @Override
    public void remoueGroupLjHouseInfo(List<LjHouseInfo> ljHouseInfos) {
        ljHouseInfoRepository.deleteInBatch(ljHouseInfos);
    }

    @Override
    public List<LjHouseInfo> queryAllLjHouseInfo(Pager pager) {
        return ljHouseInfoRepository.read(null,pager);
    }


    @Override
    public List<LjHouseInfo> queryGrouypLjHouseInfoByStatus(String status,LjHouseInfo ljHouseInfo,Pager pager) {
        QueryFilterList list=new QueryFilterList();
        list.addFilter("status","eq",status);
        if(StringUtils.isNotBlank(ljHouseInfo.getRoomType())){
            list.addFilter("roomType","eq",ljHouseInfo.getRoomType());
        }
        if(StringUtils.isNotBlank(ljHouseInfo.getProjectUuid())){
            list.addFilter("projectUuid","eq",ljHouseInfo.getProjectUuid());
        }
        if(StringUtils.isNotBlank(ljHouseInfo.getHouseType())){
            list.addFilter("houseType","eq",ljHouseInfo.getHouseType());
        }
        return ljHouseInfoRepository.read(list,pager);
    }

    @Override
    public List<LjHouseInfo> queryGrouypLjHouseInfoByGroupStatus(List<String> ljHouseInfos,LjHouseInfo ljHouseInfo, Pager pager) {
        QueryFilterList queryFilterList=new QueryFilterList();
        if (StringUtils.isNotEmpty(ljHouseInfo.getRoomType()) && Objects.nonNull(ljHouseInfo.getRoomType())){
            queryFilterList.addFilter("roomType","eq",ljHouseInfo.getRoomType());
        }
        if(StringUtils.isNotEmpty(ljHouseInfo.getProjectUuid()) && Objects.nonNull(ljHouseInfo.getProjectUuid())){
            queryFilterList.addFilter("projectUuid","eq",ljHouseInfo.getProjectUuid());
        }
        if(StringUtils.isNotEmpty(ljHouseInfo.getBuildingNo()) && Objects.nonNull(ljHouseInfo.getBuildingNo())){
            queryFilterList.addFilter("buildingNo","like",ljHouseInfo.getBuildingNo());
        }
        if(StringUtils.isNotEmpty(ljHouseInfo.getUnitNo()) && Objects.nonNull(ljHouseInfo.getUnitNo())){
            queryFilterList.addFilter("unitNo","like",ljHouseInfo.getUnitNo());
        }
        if(StringUtils.isNotEmpty(ljHouseInfo.getRoomNo()) && Objects.nonNull(ljHouseInfo.getRoomNo())){
            queryFilterList.addFilter("roomNo","like",ljHouseInfo.getRoomNo());
        }

        if(ljHouseInfos.size() <= 0){
            ljHouseInfos.add("00");
        }
        queryFilterList.addFilter("id",Operator.in,ljHouseInfos);
        return ljHouseInfoRepository.read(queryFilterList,pager);
    }


    @Override
    public List<LjHouseInfo> queryGroupLjHouseInfoByHouseType(String houseType) {
        QueryFilterList list=new QueryFilterList();
        list.addFilter("houseType","eq",houseType);
        return ljHouseInfoRepository.read(list);
    }


    @Override
    public List<LjHouseInfo> queryHouseInfoSold(LjHouseInfo ljHouseInfo, Pager pager) {
        QueryFilterList queryFilterList = new QueryFilterList();
        if (StringUtils.isNotEmpty(ljHouseInfo.getProjectUuid())){
            queryFilterList.addFilter("projectUuid","eq",ljHouseInfo.getProjectUuid());
        }
        if (StringUtils.isNotEmpty(ljHouseInfo.getRoomType())){
            queryFilterList.addFilter("roomType","eq",ljHouseInfo.getRoomType());
        }
        if(Objects.nonNull(ljHouseInfo.getSaleMoney())){
            queryFilterList.addFilter("saleMoney","le",ljHouseInfo.getSaleMoney());
        }
        if(Objects.nonNull(ljHouseInfo.getRoomArea())){
            queryFilterList.addFilter("roomArea","ge",ljHouseInfo.getRoomArea());
        }
        if(Objects.nonNull(ljHouseInfo.getCompanyChargeStatus())){
            queryFilterList.addFilter("companyChargeStatus","eq",ljHouseInfo.getCompanyChargeStatus());
        }
        queryFilterList.addFilter("status","eq","2");//已售
        queryFilterList.addFilter("houseType","eq",ljHouseInfo.getHouseType());//渠道整合/营销
        return ljHouseInfoRepository.read(queryFilterList,pager);

    }

    @Override
    public List<LjHouseInfo> queryGroupHouseInfoSold(String uuid,LjHouseInfo ljHouseInfo, String houseType,Pager pager) {
        QueryFilterList list=new QueryFilterList();
        list.addFilter("projectUuid","eq",uuid);
        list.addFilter("houseType","eq",houseType);
//        list.addFilter("status","eq","1");
        if(StringUtils.isNotBlank(ljHouseInfo.getRoomType())){
            list.addFilter("roomType","eq",ljHouseInfo.getRoomType());
        }
        if(StringUtils.isNotEmpty(ljHouseInfo.getBuildingNo())){
            list.addFilter("buildingNo","like",ljHouseInfo.getBuildingNo());
        }
        if(StringUtils.isNotEmpty(ljHouseInfo.getUnitNo())){
            list.addFilter("unitNo","like",ljHouseInfo.getUnitNo());
        }
        if(StringUtils.isNotEmpty(ljHouseInfo.getRoomNo())){
            list.addFilter("roomNo","like",ljHouseInfo.getRoomNo());
        }
        return ljHouseInfoRepository.read(list,pager);
    }

    @Override
    public List<LjHouseInfo> queryGroupHouseInfoSoldByDateTime(String uuid, LjHouseInfo ljHouseInfo, String houseType, Pager pager) {
        QueryFilterList list=new QueryFilterList();
        list.addFilter("projectUuid","eq",uuid);
        list.addFilter("houseType","eq",houseType);
        if(StringUtils.isNotBlank(ljHouseInfo.getRoomType())){
            list.addFilter("roomType","eq",ljHouseInfo.getRoomType());
        }
        if(StringUtils.isNotEmpty(ljHouseInfo.getBuildingNo())){
            list.addFilter("buildingNo","like",ljHouseInfo.getBuildingNo());
        }
        if(StringUtils.isNotEmpty(ljHouseInfo.getUnitNo())){
            list.addFilter("unitNo","like",ljHouseInfo.getUnitNo());
        }
        if(StringUtils.isNotEmpty(ljHouseInfo.getRoomNo())){
            list.addFilter("roomNo","like",ljHouseInfo.getRoomNo());
        }
        if(StringUtils.isNotEmpty(ljHouseInfo.getStatus())){
            list.addFilter("status","eq",ljHouseInfo.getStatus());
        }
        List<LjHouseInfo> read = ljHouseInfoRepository.read(list, pager);

        List<String> collect = read.stream().map(re -> {
            return re.getId();
        }).collect(Collectors.toList());
        //对应销售信息
        QueryFilterList list1 = new QueryFilterList();
        if(collect.size() <= 0){
            collect.add("0");
        }
        list1.addFilter("houseUuid", Operator.in,collect);
        if(Objects.nonNull(ljHouseInfo.getCreateDatetime())){
            list1.addFilter("buyTime", Operator.ge,ljHouseInfo.getCreateDatetime().toLocalDate());
        }
        if(Objects.nonNull(ljHouseInfo.getModifyDatetime())){
            list1.addFilter("buyTime", Operator.le,ljHouseInfo.getModifyDatetime().toLocalDate());
        }
        List<LjHouseSaleInfo> ljHouseSaleInfos = ljHouseSaleInfoService.queryQueryFilterList(list1);
        List<String> aa = new ArrayList<>();
        for (LjHouseSaleInfo l: ljHouseSaleInfos) {
            aa.add(l.getHouseUuid());
        }
        QueryFilterList list2 = new QueryFilterList();
        if(aa.size() <= 0 ){
            aa.add("0");
        }
        OrderList orders = new OrderList();
//        orders.addOrder("createDatetime", OrderDirection.ASC);
        orders.addOrder("buildingNo", OrderDirection.ASC);
        orders.addOrder("unitNo", OrderDirection.ASC);
        orders.addOrder("roomNo", OrderDirection.ASC);
        list2.addFilter("id",Operator.in ,aa);
        List<LjHouseInfo> read1 = ljHouseInfoRepository.read(list2,orders, pager);
        return read1;
    }


    @Override
    public LjHouseInfo queryOneHouseInfo(String id) {
        return ljHouseInfoRepository.read(id);
    }

    @Override
    public List<LjHouseInfo> queryGeneralManagerQueryHouseSaleInfo(LjHouseInfo ljHouseInfo, Pager pager) {
        QueryFilterList queryFilterList = new QueryFilterList();
        if (StringUtils.isNotEmpty(ljHouseInfo.getRoomType()) && Objects.nonNull(ljHouseInfo.getRoomType())){
            queryFilterList.addFilter("roomType","eq",ljHouseInfo.getRoomType());
        }
        if(StringUtils.isNotEmpty(ljHouseInfo.getProjectUuid())){
            queryFilterList.addFilter("projectUuid","eq",ljHouseInfo.getProjectUuid());
        }
        if(StringUtils.isNotEmpty(ljHouseInfo.getHouseType())){
            queryFilterList.addFilter("houseType","eq",ljHouseInfo.getHouseType());
        }
        if(StringUtils.isNotEmpty(ljHouseInfo.getBuildingNo())){
            queryFilterList.addFilter("buildingNo","like",ljHouseInfo.getBuildingNo());
        }
        if(StringUtils.isNotEmpty(ljHouseInfo.getUnitNo())){
            queryFilterList.addFilter("unitNo","like",ljHouseInfo.getUnitNo());
        }
        if(StringUtils.isNotEmpty(ljHouseInfo.getRoomNo())){
            queryFilterList.addFilter("roomNo","like",ljHouseInfo.getRoomNo());
        }
        queryFilterList.addFilter("status","eq","2");//已售
        List<LjHouseInfo> read = ljHouseInfoRepository.read(queryFilterList, pager);
        List<LjHouseInfo> collect = read.stream().filter(houseInfo -> {
            if("1".equals(houseInfo.getHouseType())){
                QueryFilterList list = new QueryFilterList();
                list.addFilter("houseUuid", "eq", houseInfo.getId());
                //list.addFilter("chargeStatus", "eq", "6");//副总审核通过
                List<LjHouseSaleInfo> read1 = ljHouseSaleInfoRepository.read(list);
                if (Objects.nonNull(read1) && read1.size() > 0) {
                    return true;
                }
            }else if("2".equals(houseInfo.getHouseType())){
                QueryFilterList list = new QueryFilterList();
                list.addFilter("houseUuid", "eq", houseInfo.getId());
                //list.addFilter("chargeStatus", "eq", "6");//副总审核通过
                List<MarketingSaleInfo> read1 = marketingSaleInfoRepository.read(list);
                if (Objects.nonNull(read1) && read1.size() > 0) {
                    return true;
                }
            }
            return false;
        }).collect(Collectors.toList());
        return collect;
    }


    @Override
    public List<LjHouseInfo> queryExpenseMaidHouseInfo(LjHouseInfo ljHouseInfo, Pager pager) {
        QueryFilterList queryFilterList = new QueryFilterList();
        if (StringUtils.isNotEmpty(ljHouseInfo.getRoomType()) && Objects.nonNull(ljHouseInfo.getRoomType())){
            queryFilterList.addFilter("roomType","eq",ljHouseInfo.getRoomType());
        }
        if(StringUtils.isNotEmpty(ljHouseInfo.getProjectUuid())){
            queryFilterList.addFilter("projectUuid","eq",ljHouseInfo.getProjectUuid());
        }
        if(StringUtils.isNotEmpty(ljHouseInfo.getHouseType())){
            queryFilterList.addFilter("houseType","eq",ljHouseInfo.getHouseType());
        }
        if(StringUtils.isNotEmpty(ljHouseInfo.getBuildingNo())){
            queryFilterList.addFilter("buildingNo","like",ljHouseInfo.getBuildingNo());
        }
        if(StringUtils.isNotEmpty(ljHouseInfo.getUnitNo())){
            queryFilterList.addFilter("unitNo","like",ljHouseInfo.getUnitNo());
        }
        if(StringUtils.isNotEmpty(ljHouseInfo.getRoomNo())){
            queryFilterList.addFilter("roomNo","like",ljHouseInfo.getRoomNo());
        }
        queryFilterList.addFilter("status","eq","2");//已售
        queryFilterList.addFilter("companyChargeStatus","eq","2");//已结佣
        List<LjHouseInfo> read = ljHouseInfoRepository.read(queryFilterList, pager);
        List<LjHouseInfo> collect = read.stream().filter(houseInfo -> {
            if("1".equals(houseInfo.getHouseType())){
                QueryFilterList list = new QueryFilterList();
                list.addFilter("houseUuid", "eq", houseInfo.getId());
                //list.addFilter("chargeStatus", "eq", "8");//总经理审核通过
                List<LjHouseSaleInfo> read1 = ljHouseSaleInfoRepository.read(list);
                if (Objects.nonNull(read1) && read1.size() > 0) {
                    return true;
                }
            }else if("2".equals(houseInfo.getHouseType())){
                QueryFilterList list = new QueryFilterList();
                list.addFilter("houseUuid", "eq", houseInfo.getId());
                //list.addFilter("channeChargeStatus", "eq", "8");//渠道佣金总经理审核通过
                //list.addFilter("insideChargeStatus", "eq", "8");//内部佣金总经理审核通过
                List<MarketingSaleInfo> read1 = marketingSaleInfoRepository.read(list);
                if (Objects.nonNull(read1) && read1.size() > 0) {
                    return true;
                }
            }
            return false;
        }).collect(Collectors.toList());
        return collect;
    }


    @Override
    public List<LjHouseInfo> queryHouseInfoByStatus(LjHouseInfo ljHouseInfo,Pager pager) {
        QueryFilterList queryFilterList = new QueryFilterList();
        if(StringUtils.isNotEmpty(ljHouseInfo.getProjectUuid())){
            queryFilterList.addFilter("projectUuid","eq",ljHouseInfo.getProjectUuid());
        }
        if (StringUtils.isNotEmpty(ljHouseInfo.getRoomType())){
            queryFilterList.addFilter("roomType","eq",ljHouseInfo.getRoomType());
        }
        if (StringUtils.isNotEmpty(ljHouseInfo.getStatus())){
            queryFilterList.addFilter("status","eq",ljHouseInfo.getStatus());
        }
        queryFilterList.addFilter("minMoney","isnn");//销售价格不为空
        queryFilterList.addFilter("status","eq",ljHouseInfo.getStatus());//是否销售
        queryFilterList.addFilter("houseType","eq","2");//营销
        OrderList orders = new OrderList();
        orders.addOrder("buildingNo", OrderDirection.ASC);
        orders.addOrder("unitNo", OrderDirection.ASC);
        orders.addOrder("roomNo", OrderDirection.ASC);
        return ljHouseInfoRepository.read(queryFilterList,orders,pager);
    }

    @Override
    public List<LjHouseInfo> queryGroupLjHouseInfo(LjHouseInfo ljHouseInfo,Pager pager) {
        QueryFilterList queryFilterList = new QueryFilterList();
        if(StringUtils.isNotEmpty(ljHouseInfo.getProjectUuid())){
            queryFilterList.addFilter("projectUuid","eq",ljHouseInfo.getProjectUuid());
        }
        if (StringUtils.isNotEmpty(ljHouseInfo.getRoomType())){
            queryFilterList.addFilter("roomType","eq",ljHouseInfo.getRoomType());
        }
        if (StringUtils.isNotEmpty(ljHouseInfo.getCompanyChargeStatus()) && Objects.nonNull(ljHouseInfo.getCompanyChargeStatus())){
            queryFilterList.addFilter("companyChargeStatus","eq",ljHouseInfo.getCompanyChargeMoney());
        }
        //queryFilterList.addFilter("companyChargeStatus","eq","2");
        return ljHouseInfoRepository.read(queryFilterList,pager);
    }

    @Override
    public List<LjHouseInfo> queryGroupLjHouseInfoINFO(LjHouseInfo ljHouseInfo, Pager pager) {
        QueryFilterList queryFilterList = new QueryFilterList();
        if(StringUtils.isNotEmpty(ljHouseInfo.getProjectUuid())){
            queryFilterList.addFilter("projectUuid","eq",ljHouseInfo.getProjectUuid());
        }
        if(StringUtils.isNotEmpty(ljHouseInfo.getHouseType())){
            queryFilterList.addFilter("houseType","eq",ljHouseInfo.getHouseType());
        }
        if(StringUtils.isNotEmpty(ljHouseInfo.getStatus())){
            queryFilterList.addFilter("status","eq",ljHouseInfo.getStatus());
        }
        if(StringUtils.isNotEmpty(ljHouseInfo.getCompanyChargeStatus())){
            queryFilterList.addFilter("companyChargeStatus","eq",ljHouseInfo.getCompanyChargeStatus());
        }
        if(StringUtils.isNotEmpty(ljHouseInfo.getRoomType())){
            queryFilterList.addFilter("roomType","eq",ljHouseInfo.getRoomType());
        }

        return ljHouseInfoRepository.read(queryFilterList,pager);
    }


    @Override
    public List<LjHouseInfo> queryGroupHouseInfo(LjHouseInfo ljHouseInfo) {
        QueryFilterList list = new QueryFilterList();
        if(Objects.nonNull(ljHouseInfo.getHouseType())){
            list.addFilter("houseType","eq",ljHouseInfo.getHouseType());
        }
        if(Objects.nonNull(ljHouseInfo.getCompanyChargeStatus())){
            list.addFilter("companyChargeStatus","eq",ljHouseInfo.getCompanyChargeStatus());
        }
        if(Objects.nonNull(ljHouseInfo.getCompanyChargeTime())){
            //1.1--12.31
            list.addFilter("companyChargeTime","ge", LocalDate.of(ljHouseInfo.getCompanyChargeTime().getYear(),1,1));
            list.addFilter("companyChargeTime","le", LocalDate.of(ljHouseInfo.getCompanyChargeTime().getYear(),12,31));
        }
        return ljHouseInfoRepository.read(list);
    }


    @Override
    public List<LjHouseInfo> queryAllHouseInfoForHouseReturn(LjHouseInfo ljHouseInfo, Pager pager) {
        QueryFilterList queryFilterList = new QueryFilterList();
        if (StringUtils.isNotEmpty(ljHouseInfo.getRoomType())){
            queryFilterList.addFilter("roomType","eq",ljHouseInfo.getRoomType());
        }
        if(StringUtils.isNotEmpty(ljHouseInfo.getProjectUuid())){
            queryFilterList.addFilter("projectUuid","eq",ljHouseInfo.getProjectUuid());
        }
        if(StringUtils.isNotEmpty(ljHouseInfo.getBuildingNo())){
            queryFilterList.addFilter("buildingNo","like",ljHouseInfo.getBuildingNo());
        }
        if(StringUtils.isNotEmpty(ljHouseInfo.getUnitNo())){
            queryFilterList.addFilter("unitNo","like",ljHouseInfo.getUnitNo());
        }
        if(StringUtils.isNotEmpty(ljHouseInfo.getRoomNo())){
            queryFilterList.addFilter("roomNo","like",ljHouseInfo.getRoomNo());
        }
        queryFilterList.addFilter("status","eq","1");//未售
        queryFilterList.addFilter("houseType","eq","2");//营销
        queryFilterList.addFilter("minMoney","isnn");//销售价格不为空（已定价）
        List<LjHouseInfo> read = ljHouseInfoRepository.read(queryFilterList);
        //录入客户信息的房源
        List<LjHouseInfo> collect = read.stream().filter(ljHouseInfo1 -> {
            CustomerInfo customerInfo = new CustomerInfo();
            if(StringUtils.isNotEmpty(ljHouseInfo1.getId())){
                customerInfo = customerInfoService.queryCustomerInfoByHouseUuid(ljHouseInfo1.getId());
            }
            if (Objects.nonNull(customerInfo)) {
                //设置定金金额
                ljHouseInfo1.setSaleMoney(customerInfo.getEarnest());
                return true;
            } else {
                return false;
            }
        }).collect(Collectors.toList());
            List<String> uuid = new ArrayList<>();
        for(int i=0 ; i < collect.size() ; i++){
            if(collect.get(i).getId() != null){
                uuid.add(collect.get(i).getId());
            }
        }
        if(uuid.size() <= 0){
            uuid.add("00");
        }
        List<LjHouseInfo> ljHouseInfos = this.queryGrouypLjHouseInfoByGroupStatus(uuid, ljHouseInfo, pager);
        return ljHouseInfos;
    }

    @Override
    public List<LjHouseInfo> queryGroupHouseInfoToCount(LjHouseInfo ljHouseInfo) {
        QueryFilterList queryFilters = new QueryFilterList();
        if(Objects.nonNull(ljHouseInfo.getProjectUuid())){
            queryFilters.addFilter("projectUuid","eq",ljHouseInfo.getProjectUuid());
        }
        if (Objects.nonNull(ljHouseInfo.getStatus())){
            queryFilters.addFilter("status","eq",ljHouseInfo.getStatus());
        }
        if(Objects.nonNull(ljHouseInfo.getCompanyChargeStatus())){
            queryFilters.addFilter("companyChargeStatus","eq",ljHouseInfo.getCompanyChargeStatus());
        }
        if(Objects.nonNull(ljHouseInfo.getHouseType())){
            queryFilters.addFilter("houseType","eq",ljHouseInfo.getHouseType());
        }
        return ljHouseInfoRepository.read(queryFilters);
    }
//重做
/*    @Transactional
    @Override
    public void importData(File file) {
        List excelList = ReadExcel.readExcel(file);
        System.out.println("list中的数据打印出来");
        for (int i = 0; i <excelList.size(); i++) {
            if (i == 0) {
                continue;
            }
            List list = (List) excelList.get(i);
            // 项目信息
            LjProjectInfo ljProjectInfo = new LjProjectInfo();

            // 房源信息
            LjHouseInfo houseInfo = new LjHouseInfo();
            houseInfo.setHouseType("1");
            houseInfo.setStatus("1");
            houseInfo.setCompanyChargeStatus("1");
            // 房源销售信息
            LjHouseSaleInfo saleInfo = new LjHouseSaleInfo();
            saleInfo.setChargeStatus("0");
            for (int j = 0; j < list.size(); j++) {
                // 成交项目
                if (j == 2) {
                    ljProjectInfo = ljProjectInfoService.queryByProjectName(list.get(j) + "");
                    if (ljProjectInfo == null) {
                        ljProjectInfo = new LjProjectInfo();
                        ljProjectInfo.setProjectType("1");
                        ljProjectInfo.setProjectName(list.get(j) + "");
                    }
                }

                if (ljProjectInfo.getId() == null) {
                    List<Employee> employees = employeeService.queryAllEmployee();
                    for(Employee ee:employees) {

                        // 案场经理
                        if (j == 12) {
                            if(ee.getPersonName()!=null ){
                                if(ee.getPersonName().equals(list.get(j) + "")){
                                    AdminUser adminUser = adminuserService.queryUserByUserName(ee.getAccount());
                                    ljProjectInfo.setCaseManager(adminUser.getId());
                                }
                            }
                        }
                        // 区域经理
                        if (j == 13) {
                            if(ee.getPersonName()!=null ){
                                if(ee.getPersonName().equals(list.get(j) + "")){
                                    AdminUser adminUser = adminuserService.queryUserByUserName(ee.getAccount());
                                    ljProjectInfo.setPrjDutyMan(adminUser.getId());
                                }
                            }


                        }
                        // 总监
                        if (j == 14) {
                            if(ee.getPersonName()!=null ){
                                if(ee.getPersonName().equals(list.get(j) + "")){
                                    AdminUser adminUser = adminuserService.queryUserByUserName(ee.getAccount());
                                    ljProjectInfo.setChiefUuid(adminUser.getId());
                                }
                            }


                        }
                    }

                }

                // 产品类别
                if (j == 3) {
                    //LJOA.CHANNEL.ROOM_TYPE
                    List<Attribute> attributes = attributeService.queryAttributesByActiveKey("LJOA.CHANNEL.ROOM_TYPE");
                    for (Attribute aa:attributes) {
                        if(Objects.nonNull(aa.getValue())&&aa.getLabel().equals(list.get(j) + "")){
                            houseInfo.setRoomType(aa.getValue());
                        }
                    }
                    if(Objects.isNull(houseInfo.getRoomType())){
                        houseInfo.setOtherRoomType(list.get(j) + "");
                    }

                }
                // 房号
                if (j == 4) {
                    String roomNo = list.get(j) + "";
                    String[] roomNoArr = roomNo.split("-");
                    // 楼号
                    if (roomNoArr.length > 0) {
                        houseInfo.setBuildingNo(roomNoArr[0]);
                    }
                    // 单元
                    if (roomNoArr.length > 1) {
                        houseInfo.setUnitNo(roomNoArr[1]);
                    }
                    // 房间号
                    if (roomNoArr.length > 2) {
                        houseInfo.setRoomNo(roomNoArr[2]);
                    }
                }
                if(j==5){
                    houseInfo.setRoomArea(Float.parseFloat(list.get(j) + ""));
                }
                // 客户姓名
                if (j == 0) {
                    saleInfo.setCustomerName(list.get(j) + "");
                }
                // 电话
                if (j == 1) {
                    saleInfo.setCustomerTel(list.get(j) + "");
                }

                // 总价
                if (j == 6) {
                    houseInfo.setSaleMoney(Float.valueOf(list.get(j) + ""));
                    saleInfo.setTotalPrice(Float.valueOf(list.get(j) + ""));
                }
                // 签约日期
                if (j == 7) {
                    String[] signTimeArr = String.valueOf(list.get(j)).split("-");
                    if (signTimeArr.length >= 3) {
                        saleInfo.setSignTime(LocalDate.of(Integer.valueOf(signTimeArr[0]), Integer.valueOf(signTimeArr[1]), Integer.valueOf(signTimeArr[2])));
                    }
                }
                // 渠道
                if (j == 8) {
                    List<LjChannelCompany> ljChannelCompanies = ljChannelCompanyService.queryAllLjChannelCompany();
                for(LjChannelCompany l:ljChannelCompanies){
                    if((list.get(j) + "").equals(l.getCompanyName())){
                        saleInfo.setChannelCompany(l.getId());
                    }else{
                        LjChannelCompany ljChannelCompany=new LjChannelCompany();
                        ljChannelCompany.setCompanyName(list.get(j) + "");
                        LjChannelCompany ljChannelCompany1 = ljChannelCompanyService.saveLjChannelCompany(ljChannelCompany);
                        saleInfo.setChannelCompany(ljChannelCompany1.getId());
                    }
                }


                }
                // 公司业绩
                if (j == 9) {
                    saleInfo.setCompanyProfit(Float.valueOf(list.get(j) + ""));
                }
                // 渠道佣金点位
                if (j == 10) {
                    if(list.get(j)!=null){
                        saleInfo.setChannelChargeType("1");
                    }
//                    houseInfo.setCompanyChargeScale(Float.valueOf(list.get(j) + ""));
                    saleInfo.setChannelChargeScale(Float.valueOf(list.get(j) + ""));
                }
                // 渠道佣金
                if (j == 11) {
                    if(list.get(10)==null&&list.get(j)!=null){
                        saleInfo.setChannelChargeType("2");
                    }
//                    houseInfo.setCompanyChargeMoney(Float.valueOf(list.get(j) + ""));
                    saleInfo.setChannelChargeMoney(Float.valueOf(list.get(j) + ""));
                }

                System.out.print(list.get(j));
            }

            // 保存项目信息
            if (ljProjectInfo.getId() == null) {
                ljProjectInfo= ljProjectInfoService.saveLjProjectInfo(ljProjectInfo);
            }
            // 项目主键
            houseInfo.setProjectUuid(ljProjectInfo.getId());

            // 保存房源信息
            if(saleInfo.getTotalPrice() != null){
                // 房源状态改为已售
//                houseInfo.setStatus("2");
            }
//            List<LjHouseInfo> ljHouseInfos = this.queryAllLjHouseInfo(null);
            LjHouseInfo ll=new LjHouseInfo();
            ll.setHouseType("1");
            ll.setProjectUuid(houseInfo.getProjectUuid());
            List<LjHouseInfo> ljHouseInfos = this.queryGroupLjHouseInfoINFO(ll, null);

            boolean flag=true;
            for (LjHouseInfo l:ljHouseInfos) {
                //楼号 单元 房号相同的
                if(l.getBuildingNo().equals(houseInfo.getBuildingNo())
                        &&l.getUnitNo().equals(houseInfo.getUnitNo())
                        &&l.getRoomNo().equals(houseInfo.getRoomNo())){
                    flag=false;
                }
            }
            if(flag){
                this.saveLjHouseInfo(houseInfo);
            }


            // 保存销售信息
            saleInfo.setHouseUuid(houseInfo.getId());
            saleInfo.setCaseManager(ljProjectInfo.getCaseManager());
            saleInfo.setChannelChief(ljProjectInfo.getChiefUuid());
            saleInfo.setChannelManager(ljProjectInfo.getManagerUuid());
            saleInfo.setAreaManage(ljProjectInfo.getPrjDutyMan());
            ljHouseSaleInfoService.saveLjHouseSaleInfo(saleInfo);
            ReadExcelUtil poi = new ReadExcelUtil();
        }
    }*/
    @Transactional
    @Override
    public void importData(File file) throws Exception {
        ReadExcelUtil poi = new ReadExcelUtil();
        int rowsize = poi.importExcel(file);
        for(int i=0;i<rowsize+1;i++){
            // 项目信息
            LjProjectInfo ljProjectInfo = new LjProjectInfo();
            // 房源信息
            LjHouseInfo houseInfo = new LjHouseInfo();
            houseInfo.setHouseType("1");
            houseInfo.setStatus("1");
            houseInfo.setCompanyChargeStatus("1");
            // 房源销售信息
            LjHouseSaleInfo saleInfo = new LjHouseSaleInfo();
            saleInfo.setChargeStatus("0");
            if(i>0){
                ljProjectInfo = ljProjectInfoService.queryByProjectName(poi.getStrByNumIndex(0)+"");//项目信息
                if (ljProjectInfo == null ) {
                    ljProjectInfo = new LjProjectInfo();
                    ljProjectInfo.setProjectType("1");
                    ljProjectInfo.setProjectName(poi.getStrByNumIndex(0));
                    List<Employee> employees = employeeService.queryAllEmployee();
                    for(Employee ee:employees) {
                        // 案场经理
                        if(poi.getStrByNumIndex(13).equals(ee.getPersonName())){
                            AdminUser adminUser = adminuserService.queryUserByUserName(ee.getAccount());
                            ljProjectInfo.setCaseManager(adminUser.getId());
                            saleInfo.setCaseManager(ljProjectInfo.getCaseManager());

                        }
                        // 区域经理
                        if(poi.getStrByNumIndex(14).equals(ee.getPersonName())){
                            AdminUser adminUser = adminuserService.queryUserByUserName(ee.getAccount());
                            ljProjectInfo.setPrjDutyMan(adminUser.getId());
                            saleInfo.setAreaManage(ljProjectInfo.getPrjDutyMan());
                        }
                        // 总监
                        if(poi.getStrByNumIndex(15).equals(ee.getPersonName())){
                            AdminUser adminUser = adminuserService.queryUserByUserName(ee.getAccount());
                            ljProjectInfo.setChiefUuid(adminUser.getId());
                            saleInfo.setChannelChief(ljProjectInfo.getChiefUuid());
                        }
                    }
                }else{
                    saleInfo.setCaseManager(ljProjectInfo.getCaseManager());
                    saleInfo.setAreaManage(ljProjectInfo.getPrjDutyMan());
                    saleInfo.setChannelChief(ljProjectInfo.getChiefUuid());
                }


                // 产品类别
                List<Attribute> attributes = attributeService.queryAttributesByActiveKey("LJOA.CHANNEL.ROOM_TYPE");
                for (Attribute aa:attributes) {
                    if(Objects.nonNull(aa.getValue())&&aa.getLabel().equals(poi.getStrByNumIndex(3))){
                        houseInfo.setRoomType(aa.getValue());
                    }
                }
                if(Objects.isNull(houseInfo.getRoomType())){
                    houseInfo.setOtherRoomType(poi.getStrByNumIndex(3));
                }
                // 房号
                houseInfo.setBuildingNo(poi.getStrByNumIndex(4));
                houseInfo.setUnitNo(poi.getStrByNumIndex(5));
                houseInfo.setRoomNo(poi.getStrByNumIndex(6));
                //面积
                if(StringUtils.isNotEmpty(poi.getStrByNumIndex(7))){
                    houseInfo.setRoomArea(Float.parseFloat(poi.getStrByNumIndex(7)+""));
                }

                // 客户姓名
                saleInfo.setCustomerName(poi.getStrByNumIndex(1));
                // 电话
                saleInfo.setCustomerTel(poi.getStrByNumIndex(2));
                // 总价
                if(StringUtils.isNotEmpty(poi.getStrByNumIndex(8))){
                    saleInfo.setTotalPrice(Float.valueOf(poi.getStrByNumIndex(8)+""));
                    houseInfo.setSaleMoney(Float.valueOf(poi.getStrByNumIndex(8)+""));
                }
                // 签约日期
                if(Objects.nonNull(poi.getDate(9))){
                    Timestamp date = poi.getDate(9);
                    LocalDateTime localDateTime = date.toLocalDateTime();
                    saleInfo.setSignTime(localDateTime.toLocalDate());
                }

                // 渠道
                List<LjChannelCompany> ljChannelCompanies = ljChannelCompanyService.queryAllLjChannelCompany();
                for(LjChannelCompany l:ljChannelCompanies){
                    if(poi.getStrByNumIndex(10).equals(l.getCompanyName())){
                        saleInfo.setChannelCompany(l.getId());
                    }
                }
                if("定额".equals(poi.getStrByNumIndex(16))&&poi.getStrByNumIndex(18)!=null){
                    //公司佣金金额（业绩）
                    houseInfo.setCompanyChargeType("2");
                    houseInfo.setCompanyChargeMoney(Float.valueOf(poi.getStrByNumIndex(18)));

                }else if(poi.getStrByNumIndex(17)==null||poi.getStrByNumIndex(17)==""){
                    houseInfo.setCompanyChargeScale(null);
                }else{
                    houseInfo.setCompanyChargeType("1");
                    houseInfo.setCompanyChargeScale(Float.valueOf(poi.getStrByNumIndex(17)));
                }
                if(StringUtils.isNotEmpty(poi.getStrByNumIndex(11))){
                    // 渠道佣金点位
                    saleInfo.setChannelChargeType("1");
                    saleInfo.setChannelChargeScale(Float.valueOf(poi.getStrByNumIndex(11)));
                }else if(StringUtils.isNotEmpty(poi.getStrByNumIndex(12))){
                    // 渠道佣金
                    saleInfo.setChannelChargeType("2");
                    saleInfo.setChannelChargeMoney(Float.valueOf(poi.getStrByNumIndex(12)));
                }


                // 保存项目信息
                if (ljProjectInfo.getId() == null) {
                    ljProjectInfo.setProjectType("1");
                    ljProjectInfo = ljProjectInfoService.saveLjProjectInfo(ljProjectInfo);
                }


                if(houseInfo.getBuildingNo()!=null&&houseInfo.getUnitNo()!=null&&houseInfo.getRoomNo()!=null){
                    // 项目主键
                    houseInfo.setProjectUuid(ljProjectInfo.getId());
                    this.saveLjHouseInfo(houseInfo);
                    // 保存销售信息
                    saleInfo.setHouseUuid(houseInfo.getId());
                    ljHouseSaleInfoService.saveLjHouseSaleInfo(saleInfo);
                }


            }
            poi.nextRow();
            }

    }


//poi cell方法 注意循环问题
    @Transactional
    @Override
    public void importMarketingHouse(File file)  throws Exception{
        ReadExcelUtil poi = new ReadExcelUtil();
        int rowsize = poi.importExcel(file);
        for (int i = 0; i <= rowsize+1; i++) {
            if(i>0){
                // 项目信息
                LjProjectInfo ljProjectInfo = new LjProjectInfo();
                // 房源信息
                LjHouseInfo houseInfo = null;
                List<LjProjectInfo> ljProjectInfos = new ArrayList<>();
                if(StringUtils.isNotEmpty(poi.getStrByNumIndex(0))){
                    ljProjectInfos = ljProjectInfoService.queryByProjectNameTo(poi.getStrByNumIndex(0));
                }
//                List<LjProjectInfo> ljProjectInfos = ljProjectInfoService.queryByProjectNameTo(poi.getStrByNumIndex(0));//重名项目集
                if(ljProjectInfos.size() > 1){
                    for(LjProjectInfo ljProjectInfo1 : ljProjectInfos){
                        if("2".equals(ljProjectInfo1.getProjectType())){
                            ljProjectInfo = ljProjectInfo1;
                        }
                    }
                }
                if(ljProjectInfos.size() == 1){
                    ljProjectInfo = ljProjectInfos.get(0);
                }
//                ljProjectInfo = ljProjectInfoService.queryByProjectName(poi.getStrByNumIndex(0));
                if (ljProjectInfo != null&&ljProjectInfo.getId()!=null) {
                    houseInfo = this.queryByHousNo(ljProjectInfo.getId(), poi.getStrByNumIndex(1) , poi.getStrByNumIndex(2) , poi.getStrByNumIndex(3));
                }

                if (houseInfo == null) {
                    houseInfo = new LjHouseInfo();
                    houseInfo.setHouseType("2");
                    houseInfo.setStatus("1");
                    houseInfo.setCompanyChargeStatus("1");
                }

                // 项目名称

//                ljProjectInfo = ljProjectInfoService.queryByProjectName(poi.getStrByNumIndex(0));
                if (ljProjectInfo == null) {
                    ljProjectInfo = new LjProjectInfo();
                    ljProjectInfo.setProjectType("2");
                    ljProjectInfo.setProjectName(poi.getStrByNumIndex(0));
                }


                // 楼号
                if(StringUtils.isNotEmpty(poi.getStrByNumIndex(1))){
                    houseInfo.setBuildingNo(poi.getStrByNumIndex(1));
                }
                // 单元
                if(StringUtils.isNotEmpty(poi.getStrByNumIndex(2))){
                    houseInfo.setUnitNo(poi.getStrByNumIndex(2));
                }
                // 房间号
                if(StringUtils.isNotEmpty(poi.getStrByNumIndex(3))){
                    houseInfo.setRoomNo(poi.getStrByNumIndex(3));
                }
                // 面积
                if(StringUtils.isNotEmpty(poi.getStrByNumIndex(4))){
                    houseInfo.setRoomArea(Float.valueOf(poi.getStrByNumIndex(4)));
                }

                // 产品
                List<Attribute> attributes = attributeService.queryAttributesByActiveKey("LJOA.CHANNEL.ROOM_TYPE");
                for (Attribute aa:attributes) {
                    if(Objects.nonNull(aa.getValue())&&aa.getLabel().equals(poi.getStrByNumIndex(5))){
                        houseInfo.setRoomType(aa.getValue());
                    }
                }
                if(Objects.isNull(houseInfo.getRoomType())){
                    houseInfo.setOtherRoomType(poi.getStrByNumIndex(5));
                }


                // 公司佣金点（比例）
                if(poi.getStrByNumIndex(6)==null||poi.getStrByNumIndex(6)==""){
                    houseInfo.setCompanyChargeScale(null);
                }else{
                    houseInfo.setCompanyChargeScale(Float.valueOf(poi.getStrByNumIndex(6)+""));
                    houseInfo.setPostCommission(houseInfo.getCompanyChargeMoney());
                }
                //  公司佣金（定额）
                if(StringUtils.isNotEmpty(poi.getStrByNumIndex(7))){
                    houseInfo.setCompanyChargeMoney(Float.valueOf(poi.getStrByNumIndex(7)));
                }
                // 销售底价
                if(StringUtils.isNotEmpty(poi.getStrByNumIndex(8))){
                    houseInfo.setMinMoney(Float.valueOf(poi.getStrByNumIndex(8)+""));
                }


                // 保存项目信息
                if (ljProjectInfo.getId() == null && ljProjectInfo.getProjectName()!=null && ljProjectInfo.getProjectName()!="") {
                    ljProjectInfo= ljProjectInfoService.saveLjProjectInfo(ljProjectInfo);
                }
                // 项目主键
                houseInfo.setProjectUuid(ljProjectInfo.getId());
                if(houseInfo.getProjectUuid()!=null){
                    houseInfo = this.saveLjHouseInfo(houseInfo);
                }
            }
            poi.nextRow();
        }
    }

    @Transactional
    @Override
    public void importMarketingHouseMoney(File file) throws Exception {
        ReadExcelUtil poi = new ReadExcelUtil();
        int rowsize = poi.importExcel(file);
        List<LjHouseInfo> ljHouseInfos = this.queryGroupLjHouseInfoByHouseType("2");//房源
        List<LjProjectInfo> ljProjectInfos = ljProjectInfoService.queryAllLjProjectInfo();//项目
        for (int i = 0; i <= rowsize+1; i++) {
            if(i>0){
                LjHouseInfo ljHouseInfo1=new LjHouseInfo();
                 for(LjHouseInfo ljHouseInfo:ljHouseInfos){
                     String projectName="";
                    for(LjProjectInfo ljProjectInfo:ljProjectInfos){
                         if(ljHouseInfo.getProjectUuid().equals(ljProjectInfo.getId())){
                             projectName=ljProjectInfo.getProjectName();
                         }
                     }
                     if(projectName.equals(poi.getStrByNumIndex(0)+"")){
                         ljHouseInfo1 = this.queryByHousNo(ljHouseInfo.getProjectUuid(), poi.getStrByNumIndex(1) , poi.getStrByNumIndex(2) , poi.getStrByNumIndex(3));
                         if(ljHouseInfo1!=null){
                             // 公司佣金点（比例）
                             if(StringUtils.isNotEmpty(poi.getStrByNumIndex(6))){
                                 ljHouseInfo1.setCompanyChargeScale(Float.valueOf(poi.getStrByNumIndex(6)+""));
                             }else{
                                 ljHouseInfo1.setCompanyChargeScale(null);
                             }
                             //  公司佣金（定额）
                             if(StringUtils.isNotEmpty(poi.getStrByNumIndex(7))){
                                 ljHouseInfo1.setCompanyChargeMoney(Float.valueOf(poi.getStrByNumIndex(7)));
                             }else{
                                 ljHouseInfo1.setCompanyChargeMoney(null);
                             }
                             // 销售底价
                             if(StringUtils.isNotEmpty(poi.getStrByNumIndex(8))){
                                 ljHouseInfo1.setMinMoney(Float.valueOf(poi.getStrByNumIndex(8)+""));
                             }else{
                                 ljHouseInfo1.setMinMoney(null);
                             }
                         }

                     }

                 }
                 this.saveLjHouseInfo(ljHouseInfo1);

            }
            poi.nextRow();
        }
    }

    @Override
    public List<LjHouseInfo> queryGroupHouseMonth(String projectUuid, Integer month) {
        QueryFilterList queryFilters = new QueryFilterList();
        queryFilters.addFilter("projectUuid","eq",projectUuid);
        int year = LocalDate.now().getYear();
        if(month==12){
            queryFilters.addFilter("companyChargeTime","ge",LocalDate.of(year, month, 1));
            queryFilters.addFilter("companyChargeTime","le",LocalDate.of(year, month, LocalDate.of(year,month,1).getMonth().maxLength()));
        }else{
            queryFilters.addFilter("companyChargeTime","ge",LocalDate.of(year, month, 1));
            queryFilters.addFilter("companyChargeTime","lt",LocalDate.of(year, month+1, 1));
        }
        return ljHouseInfoRepository.read(queryFilters);
    }

    @Override
    public LjHouseInfo queryByHousNo(String projectUuid, String buildingNo, String unitNo, String roomNo) {
        QueryFilterList queryFilters = new QueryFilterList();
        queryFilters.addFilter("projectUuid","eq",projectUuid);
        queryFilters.addFilter("buildingNo","eq",buildingNo);
        queryFilters.addFilter("unitNo","eq",unitNo);
        queryFilters.addFilter("roomNo","eq",roomNo);
        List<LjHouseInfo> list= ljHouseInfoRepository.read(queryFilters);
        if (list.size() > 0) {
            return list.get(0);
        }
        return null;
    }
}
