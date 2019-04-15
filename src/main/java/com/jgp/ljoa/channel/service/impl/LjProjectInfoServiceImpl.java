package com.jgp.ljoa.channel.service.impl;

import com.jgp.common.pojo.LabelValue;
import com.jgp.ljoa.channel.model.LjChannelCompany;
import com.jgp.ljoa.channel.model.LjHouseInfo;
import com.jgp.ljoa.channel.model.LjHouseSaleInfo;
import com.jgp.ljoa.channel.model.LjProjectInfo;
import com.jgp.ljoa.channel.repository.LjProjectInfoRepository;
import com.jgp.ljoa.channel.service.LjChannelCompanyService;
import com.jgp.ljoa.channel.service.LjHouseInfoService;
import com.jgp.ljoa.channel.service.LjHouseSaleInfoService;
import com.jgp.ljoa.channel.service.LjProjectInfoService;
import com.jgp.ljoa.common.util.ExcelUtil;
import com.jgp.ljoa.hr.model.Employee;
import com.jgp.ljoa.hr.service.EmployeeService;
import com.jgp.ljoa.marketing.model.CustomerInfo;
import com.jgp.ljoa.marketing.model.MarketingChargeInfo;
import com.jgp.ljoa.marketing.model.MarketingSaleInfo;
import com.jgp.ljoa.marketing.service.CustomerInfoService;
import com.jgp.ljoa.marketing.service.MarketingChargeInfoService;
import com.jgp.ljoa.marketing.service.MarketingSaleInfoService;
import com.jgp.security.admin.model.AdminUser;
import com.jgp.security.admin.service.AdminUserService;
import com.jgp.sys.model.Attribute;
import com.jgp.sys.query.QueryFilterList;
import com.jgp.sys.service.AttributeService;
import com.jgp.sys.ui.Pager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.net.URLEncoder;
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
public class LjProjectInfoServiceImpl implements LjProjectInfoService {

    @Autowired
    private LjProjectInfoRepository ljProjectInfoRepository;
    @Autowired
    private LjHouseInfoService ljHouseInfoService;
    @Autowired
    private LjHouseSaleInfoService ljHouseSaleInfoService;
    @Autowired
    private CustomerInfoService customerInfoService;
    @Autowired
    private MarketingSaleInfoService marketingSaleInfoService;
    @Autowired
    private MarketingChargeInfoService marketingChargeInfoService;
    @Autowired
    private AttributeService attributeService;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private AdminUserService adminUserService;
    @Autowired
    private LjChannelCompanyService ljChannelCompanyService;


    @Override
    public List<LjProjectInfo> queryGroupLjProjectInfoByProjectType(String projectType, Pager pager) {
        QueryFilterList list=new QueryFilterList();
        if(!"3".equals(projectType)){
            list.addFilter("projectType","eq",projectType);
        }
        return ljProjectInfoRepository.read(list,pager );
    }

    @Override
    public List<LjProjectInfo> queryGroupLjProjectInfoByProjectTypeByMan(String projectType, String uuid, Pager pager) {
        String sql ="";
        if(uuid != null){
           sql=" AND (lj_project_info.PRJ_DUTY_MAN = '"+uuid+"' OR " +
                    "lj_project_info.CHIEF_UUID = '"+uuid+"' OR " +
                    "lj_project_info.MANAGER_UUID = '"+uuid+"' OR " +
                    "lj_project_info.CASE_MANAGER = '"+uuid+"')"+" AND DEL_FLAG = 'N'";
        }
        String selectSql = "SELECT " +
                "lj_project_info.ID," +
                "lj_project_info.AREA," +
                "lj_project_info.CASE_MANAGER," +
                "lj_project_info.CHIEF_UUID," +
                "lj_project_info.CITY," +
                "lj_project_info.`DESCRIBE`," +
                "lj_project_info.MANAGER_UUID," +
                "lj_project_info.PROJECT_NAME," +
                "lj_project_info.PROJECT_TYPE," +
                "lj_project_info.PROVINCE," +
                "lj_project_info.PRJ_DUTY_MAN," +
                "lj_project_info.PRJ_PROCESS," +
                "lj_project_info.PROJEC_REGION " +
                "FROM " +
                "lj_project_info" +
                " WHERE " +
                "lj_project_info.PROJECT_TYPE = '"+projectType+"'" ;
        String countSql="SELECT COUNT(id) " +
                " FROM " +
                "lj_project_info" +
        " WHERE  lj_project_info.PROJECT_TYPE = '"+projectType+"'";

        int i =1;
        return ljProjectInfoRepository.readNativeSql(countSql+sql,selectSql+sql,pager);
    }

    @Override
    public LjProjectInfo queryOneLjProjectInfoById(String id) {
        return ljProjectInfoRepository.read(id);
    }
    @Transactional
    @Override
    public LjProjectInfo saveLjProjectInfo(LjProjectInfo l) {
        return  ljProjectInfoRepository.createOrUpdate(l);
    }
    @Transactional
    @Override
    public void removeLjProjectInfo(String id) {
        ljProjectInfoRepository.delete(id);
    }

    @Override
    public List<LjProjectInfo> queryGroupLjProjectInfoByProjectType() {
        return ljProjectInfoRepository.readAll();
    }

    @Override
    public List<LjProjectInfo> queryGroupLjProjectInfoByProjectTypeQD() {
        QueryFilterList queryFilterList = new QueryFilterList();
        queryFilterList.addFilter("projectType","eq","1");
        return ljProjectInfoRepository.read(queryFilterList);
    }

    @Override
    public List<LjProjectInfo> queryGroupLjProjectInfoByProjectTypeYX() {
        QueryFilterList queryFilterList = new QueryFilterList();
        queryFilterList.addFilter("projectType","eq","2");
        return ljProjectInfoRepository.read(queryFilterList);
    }

    @Override
    public LjProjectInfo queryByProjectName(String projectName) {
        QueryFilterList queryFilterList = new QueryFilterList();
        queryFilterList.addFilter("projectName","eq",projectName);
        List<LjProjectInfo> list = ljProjectInfoRepository.read(queryFilterList);
        if (list != null && list.size() > 0) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public List<LjProjectInfo> queryByProjectNameTo(String projectName) {
        QueryFilterList queryFilterList = new QueryFilterList();
        queryFilterList.addFilter("projectName","eq",projectName);
        List<LjProjectInfo> list = ljProjectInfoRepository.read(queryFilterList);
        return list;
    }

    @Override
    public List<LjProjectInfo> queryAllLjProjectInfo() {

        return  ljProjectInfoRepository.readAll();
    }

    @Override
    public List<LjProjectInfo> queryGroupProject(LjProjectInfo ljProjectInfo) {
        QueryFilterList queryFilters = new QueryFilterList();
        if(Objects.nonNull(ljProjectInfo.getProjectType())){
            queryFilters.addFilter("projectType","eq",ljProjectInfo.getProjectType());
        }
        if(Objects.nonNull(ljProjectInfo.getProvince())){
            queryFilters.addFilter("province","eq",ljProjectInfo.getProvince());
        }
        if(Objects.nonNull(ljProjectInfo.getCity())){
            queryFilters.addFilter("city","eq",ljProjectInfo.getCity());
        }
        if(Objects.nonNull(ljProjectInfo.getArea())){
            queryFilters.addFilter("area","eq",ljProjectInfo.getArea());
        }

        return ljProjectInfoRepository.read(queryFilters);
    }

    @Override
    public ResponseEntity<byte[]> exportMarkingInfo(String projecUuid) {
        LjProjectInfo projectInfo = this.queryOneLjProjectInfoById(projecUuid);
        String fileName = projectInfo.getProjectName() + "信息（营销）";

        ExcelUtil excelUtil = new ExcelUtil(fileName);
        excelUtil.setColumnWidth(new int[] { 10, 20, 10, 10, 10, 10, 10, 10, 10, 10, 10, 20, 10, 15, 10, 10,10,20, 10, 10, 10, 10, 10, 10, 10, 10, 10, 20, 10, 15, 10, 10, 10, 10, 10, 10, 10, 20 });
        excelUtil.createHeaderRow();
        excelUtil.addHeaderValue(new String[] { "序号", "项目名称", "客户", "客户身份证号", "客户联系方式", "楼栋号"
                , "产品分类" , "面积（㎡）", "销售状态", "销售价格（元）", "公司佣金结算方式", "公司佣金点位（%）"
                , "公司佣金(元)", "前置赚取（元）", "公司毛利润（元）", "结算金额（元）","手续费", "佣金结算日期", "置业顾问姓名"
                , "置业顾问佣金点位（%）", "置业顾问佣金", "置业顾问结佣状态", "营销主管姓名", "营销主管佣金点位(%)"
                , "营销主管佣金（元）", "营销主管结佣状态", "营销经理姓名", "营销经理佣金点位（%）", "营销经理佣金（元）"
                , "营销经理结佣状态", "营销总监姓名", "营销总监佣金点位(%)", "营销总监佣金（元）", "营销总监结佣状态"
                , "第三方渠道名称", "第三方渠道佣金（元）", "第三方渠道结佣状态", "公司纯利润（元）"});


        List<LjHouseInfo> houseList = ljHouseInfoService.queryGroupLjHouseInfoByUUID(projecUuid);
        List<Attribute> attribute4 = attributeService.queryAttributesByActiveKey("LJ_CUSTOMER_INFO.CHARGESTATUS");
        for (int i = 0; i < houseList.size(); i++) {
            LjHouseInfo ljHouseInfo = houseList.get(i);
            excelUtil.createRow();
            excelUtil.addValue(String.valueOf(i + 1)); // 序号
            excelUtil.addValue(projectInfo.getProjectName()); // 项目名称

            // 查询客户信息
            CustomerInfo customerInfo = customerInfoService.queryCustomerInfoByHouseUuid(ljHouseInfo.getId());
            if (customerInfo != null) {
                excelUtil.addValue(customerInfo.getCustomerName()); // 客户名称
                excelUtil.addValue(customerInfo.getCustomerIdentify()); // 客户身份证号
                excelUtil.addValue(customerInfo.getCustomerTel()); // 客户电话
            } else {
                excelUtil.addValue();
                excelUtil.addValue();
                excelUtil.addValue();
            }

            excelUtil.addValue(ljHouseInfo.getBuildingNo() + "-" + ljHouseInfo.getUnitNo() + "-" + ljHouseInfo.getRoomNo()); //楼栋号
            List<Attribute> attributes = attributeService.queryAttributesByActiveKey("LJOA.CHANNEL.ROOM_TYPE");
            if(ljHouseInfo.getRoomType()!=null){
                for (Attribute aa:attributes) {
                    if(Objects.nonNull(aa.getValue())&&aa.getValue().equals(ljHouseInfo.getRoomType())){
                        excelUtil.addValue(aa.getLabel()); // 产品分类
                    }
                }
            }else{
                excelUtil.addValue(ljHouseInfo.getOtherRoomType()+""); // 产品分类
            }
            excelUtil.addValue(ljHouseInfo.getRoomArea() + ""); // 面积
            List<Attribute> attribute2 = attributeService.queryAttributesByActiveKey("LJOA.CHANNEL.ROOM_STATUS");
            if(ljHouseInfo.getStatus()!=null){
                for (Attribute aa:attribute2) {
                    if(Objects.nonNull(aa.getValue())&&aa.getValue().equals(ljHouseInfo.getStatus())){
                        excelUtil.addValue(aa.getLabel()); // 销售状态
                    }
                }
            }else{
                excelUtil.addValue();
            }

            excelUtil.addValue(ljHouseInfo.getSaleMoney() + ""); // 销售价格
            List<Attribute> attribute3 = attributeService.queryAttributesByActiveKey("LJ_CUSTOMER_INFO.PATYTYPE");
            if(customerInfo!=null&&customerInfo.getPatyType()!=null){
                for (Attribute aa:attribute3) {
                    if(Objects.nonNull(aa.getValue())&&aa.getValue().equals(customerInfo.getPatyType())){
                        excelUtil.addValue(aa.getLabel()); // 公司佣金结算方式
                    }
                }
            }else{
                excelUtil.addValue(); // 公司佣金结算方式
            }
            excelUtil.addValue(ljHouseInfo.getCompanyChargeScale() + ""); // 公司佣金点位（%）
            excelUtil.addValue(ljHouseInfo.getCompanyChargeMoney() + ""); // 公司佣金(元)

            MarketingSaleInfo marketingSaleInfo = marketingSaleInfoService.queryMarketingSaleInfoByUuid(ljHouseInfo.getId());
            if (marketingSaleInfo != null) {
                excelUtil.addValue(marketingSaleInfo.getPrepositionEarn() + ""); // 前置赚取（元）
                excelUtil.addValue(marketingSaleInfo.getGrossProfit() + ""); // 公司毛利润
            } else {
                excelUtil.addValue();
                excelUtil.addValue();
            }

            excelUtil.addValue(ljHouseInfo.getCompanyChargeMoney() + ""); // 结算金额（元）
            excelUtil.addValue(ljHouseInfo.getCompanyChargeMoneyServiceCharge()+ ""); // 手续费（元）
            excelUtil.addValue(ljHouseInfo.getCompanyChargeTime() + ""); // 佣金结算日期

            // 置业顾问佣金信息
            MarketingChargeInfo zygw = marketingChargeInfoService.queryOneMarketingChargeInfoByChargeType("1", ljHouseInfo.getId());
            if (zygw != null) {
                excelUtil.addValue(zygw.getChargeTargetName()); // 置业顾问姓名
                excelUtil.addValue(zygw.getChargeProportion() + ""); // 置业顾问佣金点位（%）
                excelUtil.addValue(zygw.getChargeMoney() + ""); // 置业顾问佣金
                //

                if(zygw.getChargeStatus()!=null){
                    for (Attribute aa:attribute4) {
                        if(Objects.nonNull(aa.getValue())&&aa.getValue().equals(zygw.getChargeStatus())){
                            excelUtil.addValue(aa.getLabel()); // 置业顾问结佣状态
                        }
                    }
                }else{
                    excelUtil.addValue();  // 置业顾问结佣状态
                }
            } else {
                excelUtil.addValue();
                excelUtil.addValue();
                excelUtil.addValue();
                excelUtil.addValue();
            }

            // 营销主管佣金信息
            MarketingChargeInfo yxzg = marketingChargeInfoService.queryOneMarketingChargeInfoByChargeType("2", ljHouseInfo.getId());
            if (yxzg != null) {
                excelUtil.addValue(yxzg.getChargeTargetName()); // 营销主管姓名
                excelUtil.addValue(yxzg.getChargeProportion() + ""); // 营销主管佣金点位(%)
                excelUtil.addValue(yxzg.getChargeMoney() + ""); // 营销主管佣金（元）
                if(yxzg.getChargeStatus()!=null){
                    for (Attribute aa:attribute4) {
                        if(Objects.nonNull(aa.getValue())&&aa.getValue().equals(yxzg.getChargeStatus())){
                            excelUtil.addValue(aa.getLabel()); // 营销主管结佣状态
                        }
                    }
                }else{
                    excelUtil.addValue();  // 营销主管结佣状态
                }
            } else {
                excelUtil.addValue();
                excelUtil.addValue();
                excelUtil.addValue();
                excelUtil.addValue();
            }

            // 营销经理佣金信息
            MarketingChargeInfo yxjl = marketingChargeInfoService.queryOneMarketingChargeInfoByChargeType("3", ljHouseInfo.getId());
            if (yxjl != null) {
                excelUtil.addValue(yxjl.getChargeTargetName()); // 营销经理姓名
                excelUtil.addValue(yxjl.getChargeProportion() + ""); // 营销经理佣金点位（%）
                excelUtil.addValue(yxjl.getChargeMoney() + ""); // 营销经理佣金（元）
                if(yxjl.getChargeStatus()!=null){
                    for (Attribute aa:attribute4) {
                        if(Objects.nonNull(aa.getValue())&&aa.getValue().equals(yxjl.getChargeStatus())){
                            excelUtil.addValue(aa.getLabel()); // 营销主管结佣状态
                        }
                    }
                }else{
                    excelUtil.addValue();  // 营销主管结佣状态
                }
            } else {
                excelUtil.addValue();
                excelUtil.addValue();
                excelUtil.addValue();
                excelUtil.addValue();
            }

            // 营销总监佣金信息
            MarketingChargeInfo yxzj = marketingChargeInfoService.queryOneMarketingChargeInfoByChargeType("4", ljHouseInfo.getId());
            if (yxzj != null) {
                excelUtil.addValue(yxzj.getChargeTargetName()); // 营销总监姓名
                excelUtil.addValue(yxzj.getChargeProportion() + ""); // 营销总监佣金点位(%)
                excelUtil.addValue(yxzj.getChargeMoney() + ""); // 营销总监佣金（元）
                if(yxzj.getChargeStatus()!=null){
                    for (Attribute aa:attribute4) {
                        if(Objects.nonNull(aa.getValue())&&aa.getValue().equals(yxzj.getChargeStatus())){
                            excelUtil.addValue(aa.getLabel()); // 营销总监结佣状态
                        }
                    }
                }else{
                    excelUtil.addValue();   // 营销总监结佣状态
                }

            } else {
                excelUtil.addValue();
                excelUtil.addValue();
                excelUtil.addValue();
                excelUtil.addValue();
            }

            // 渠道佣金信息
            MarketingChargeInfo dsfqd = marketingChargeInfoService.queryOneMarketingChargeInfoByChargeType("5", ljHouseInfo.getId());
            if (dsfqd != null) {
                excelUtil.addValue(dsfqd.getChargeTargetName()); // 第三方渠道名称
                excelUtil.addValue(dsfqd.getChargeMoney() + ""); // 第三方渠道佣金（元）
                if(dsfqd.getChargeStatus()!=null){
                    for (Attribute aa:attribute4) {
                        if(Objects.nonNull(aa.getValue())&&aa.getValue().equals(dsfqd.getChargeStatus())){
                            excelUtil.addValue(aa.getLabel()); // 第三方渠道结佣状态
                        }
                    }
                }else{
                    excelUtil.addValue(); // 第三方渠道结佣状态
                }
//                excelUtil.addValue(dsfqd.getChargeStatus()); // 第三方渠道结佣状态
            } else {
                excelUtil.addValue();
                excelUtil.addValue();
                excelUtil.addValue();
            }

            if (marketingSaleInfo != null&&marketingSaleInfo.getPureProfit()!=null) {
                excelUtil.addValue(marketingSaleInfo.getPureProfit() + ""); // 公司纯利润（元）
            } else {
                excelUtil.addValue();
            }
        }

        byte[] body=null;
        InputStream in=excelUtil.write();
        HttpHeaders headers = new HttpHeaders();
        try {
            body=new byte[in.available()];
            in.read(body);

            headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
            headers.add("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName + ".xls", "UTF-8"));
            headers.add("Pragma", "no-cache");
            headers.add("Expires", "0");
        } catch (Exception e) {
            e.printStackTrace();
        }

        HttpStatus statusCode=HttpStatus.OK;

        ResponseEntity<byte[]> response=new ResponseEntity<byte[]>(body,headers,statusCode);
        return response;
    }

    @Override
    public ResponseEntity<byte[]> exportSoreInfo(String projecUuid) {

        List<Employee> employees = employeeService.queryAllEmployee();
        List<LabelValue> labelValues = employees.stream().map(emp -> {
            AdminUser adminUser = adminUserService.queryUserByUserName(emp.getAccount());
            if(Objects.nonNull(adminUser)){
                return new LabelValue(emp.getPersonName(),adminUser.getId(),null);
            }else {
                return null;
            }
        }).filter(labelValue -> {
            if(Objects.nonNull(labelValue)){
                return true;
            }else {
                return false;
            }
        }).collect(Collectors.toList());//员工label

        List<LjChannelCompany> ljChannelCompanies = ljChannelCompanyService.queryAllLjChannelCompany();
        List<LabelValue> labelValue = ljChannelCompanies.stream().map(emp -> {
            return new LabelValue(emp.getCompanyName(),emp.getId(),null);
        }).collect(Collectors.toList());//第三方渠道公司

        LjProjectInfo projectInfo = this.queryOneLjProjectInfoById(projecUuid);
        String fileName = projectInfo.getProjectName() + "信息（渠道）";
        //新建渠道导出信息
        ExcelUtil excelUtil = new ExcelUtil(fileName);
        //设置列宽
        excelUtil.setColumnWidth(new int[] {10,20,10,20,15,15,10,10,10,10,10,10,10,10,10,20,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10,10});
        //创建表头
        excelUtil.createHeaderRow();
        excelUtil.addHeaderValue(new String[] {"序号","项目名称","客户","客户身份证号","客户联系方式"	,"楼栋号","产品分类"
                        ,"面积（㎡）","销售状态"," 销售价格（元）","结算方式","佣金点位（%）","公司佣金(元)","结算金额（元）","手续费"
                ,"佣金结款日期","案场专员姓名","案场专员佣金（元）","案场经理姓名","案场经理结佣方式","佣金点位（%）",
                "案场经理佣金","渠道经理","渠道经理结佣方式","佣金点位（%）","渠道经理佣金","个人姓名","个人佣金","区域经理姓名"
                ,"区域经理结佣方式","佣金点位(%)","区域经理佣金","渠道总监姓名","渠道总监结佣方式","佣金比例（%）","渠道总监佣金（元）","公司内部结佣状态"
                ,"第三方渠道名称","第三方渠道佣金结佣方式","第三方渠佣金比例（%）","第三方渠道佣金（元）","第三方渠道扣税方式"
                ,"扣税比例（%）","第三方渠道税后佣金（元）","第三方渠道结佣状态","第三方渠道结佣时间","公司利润（元）"});
        List<LjHouseInfo> houseList = ljHouseInfoService.queryGroupLjHouseInfoByUUID(projecUuid);//对应项目房源信息
        for (int i = 0; i < houseList.size(); i++) {
            LjHouseInfo ljHouseInfo = houseList.get(i);
            if(ljHouseInfo!=null){
                excelUtil.createRow();
                excelUtil.addValue(String.valueOf(i + 1)); // 序号
                excelUtil.addValue(projectInfo.getProjectName()); // 项目名称
                LjHouseSaleInfo ljHouseSaleInfo = ljHouseSaleInfoService.queryOneLjHouseSaleInfoBuUUid(ljHouseInfo.getId());
                if (ljHouseSaleInfo != null) {
                    excelUtil.addValue(ljHouseSaleInfo.getCustomerName()); // 客户名称
                    excelUtil.addValue(ljHouseSaleInfo.getIdentityCode()); // 客户身份证号
                    excelUtil.addValue(ljHouseSaleInfo.getCustomerTel()); // 客户电话
                } else {
                    excelUtil.addValue();
                    excelUtil.addValue();
                    excelUtil.addValue();
                }
                excelUtil.addValue(ljHouseInfo.getBuildingNo() + "-" + ljHouseInfo.getUnitNo() + "-" + ljHouseInfo.getRoomNo()); //楼栋号
                List<Attribute> attributes = attributeService.queryAttributesByActiveKey("LJOA.CHANNEL.ROOM_TYPE");
                if(ljHouseInfo.getRoomType()!=null){
                    for (Attribute aa:attributes) {
                        if(Objects.nonNull(aa.getValue())&&aa.getValue().equals(ljHouseInfo.getRoomType())){
                            excelUtil.addValue(aa.getLabel()); // 产品分类
                        }
                    }
                }else{
                    excelUtil.addValue(ljHouseInfo.getOtherRoomType()+""); // 产品分类
                }

                if(ljHouseInfo.getRoomArea()!=null){
                    excelUtil.addValue(ljHouseInfo.getRoomArea() + ""); // 面积
                }else{
                    excelUtil.addValue(); // 面积
                }

                List<Attribute> attribute2 = attributeService.queryAttributesByActiveKey("LJOA.CHANNEL.ROOM_STATUS");
                if(ljHouseInfo.getStatus()!=null){
                    for (Attribute aa:attribute2) {
                        if(Objects.nonNull(aa.getValue())&&aa.getValue().equals(ljHouseInfo.getStatus())){
                            excelUtil.addValue(aa.getLabel()); // 销售状态
                        }
                    }
                }else{
                    excelUtil.addValue();
                }

                if(ljHouseInfo.getSaleMoney()!=null){
                    excelUtil.addValue(ljHouseInfo.getSaleMoney() + ""); // 销售价格
                }else{
                    excelUtil.addValue(); // 销售价格
                }

                //
                List<Attribute> attribute3 = attributeService.queryAttributesByActiveKey("LJOA.CHANNEL.CHARGE_TYPE");
                if(ljHouseInfo.getCompanyChargeType()!=null){
                    for (Attribute aa:attribute3) {
                        if(Objects.nonNull(aa.getValue())&&aa.getValue().equals(ljHouseInfo.getCompanyChargeType())){
                            excelUtil.addValue(aa.getLabel()); // 公司佣金结算方式
                        }
                    }
                }else{
                    excelUtil.addValue(); // 公司佣金结算方式
                }

                if(ljHouseInfo.getCompanyChargeScale()!=null){
                    excelUtil.addValue(ljHouseInfo.getCompanyChargeScale() + ""); // 公司佣金点位（%）
                }else{
                    excelUtil.addValue(); // 公司佣金点位（%）
                }
                if(ljHouseInfo.getCompanyChargeMoney()!=null){
                    excelUtil.addValue(ljHouseInfo.getCompanyChargeMoney() + ""); // 公司佣金(元)
                }else{
                    excelUtil.addValue(); // 公司佣金(元)
                }
               if(ljHouseInfo.getCompanyChargeTime()!=null){
                   excelUtil.addValue(ljHouseInfo.getCompanyChargeMoneyTrue() + "");//结算金额（元）
                   if(ljHouseInfo.getCompanyChargeMoneyServiceCharge()!=null){
                       excelUtil.addValue(ljHouseInfo.getCompanyChargeMoneyServiceCharge()+"");//手续费
                   }else{
                       excelUtil.addValue();
                   }
                   excelUtil.addValue(ljHouseInfo.getCompanyChargeTime() + "");//结算时间
               }else{
                   excelUtil.addValue();//结算金额（元）
                   excelUtil.addValue();//手续费
                   excelUtil.addValue();//佣金结款日期"
               }

               if(ljHouseSaleInfo.getAdviser()!=null){
                   excelUtil.addValue(ljHouseSaleInfo.getAdviser()+"");//案场专员姓名
               }else{
                   excelUtil.addValue();
               }

               if(ljHouseSaleInfo.getAdviserMonty()!=null){
                   excelUtil.addValue(ljHouseSaleInfo.getAdviserMonty()+"");//案场专员佣金
               }else{
                   excelUtil.addValue();
               }

               for(LabelValue l:labelValues){
                   if(l.getValue().equals(ljHouseSaleInfo.getCaseManager())){
                       excelUtil.addValue(l.getLabel());  //案场经理姓名
                   }
               }
                if(ljHouseSaleInfo.getCaseManager()==null){
                    excelUtil.addValue();
                }
                //案场经理结佣方式

                if(ljHouseSaleInfo.getCaseManagerChargeType()!=null){
                    for (Attribute aa:attribute3) {
                        if(Objects.nonNull(aa.getValue())&&aa.getValue().equals(ljHouseSaleInfo.getCaseManagerChargeType())){
                            excelUtil.addValue(aa.getLabel());//案场经理结佣方式
                        }
                    }

                }else{
                    excelUtil.addValue();//案场经理结佣方式
                }
                if(ljHouseSaleInfo.getCaseManagerChargeScale()!=null){
                    excelUtil.addValue(ljHouseSaleInfo.getCaseManagerChargeScale()+"");//佣金点位（%）
                }else{
                    excelUtil.addValue();
                }

                if(ljHouseSaleInfo.getCaseManagerChargeMoney()!=null){
                    excelUtil.addValue(ljHouseSaleInfo.getCaseManagerChargeMoney()+"");//案场经理佣金
                }else{
                    excelUtil.addValue();
                }

                if(ljHouseSaleInfo.getChannelManager()!=null){
                    for(LabelValue ll:labelValues){
                        if(ljHouseSaleInfo.getChannelManager().equals(ll.getValue())){
                            excelUtil.addValue(ll.getLabel());//渠道经理

                        }
                    }

                }else{
                    excelUtil.addValue();
                }

                if(ljHouseSaleInfo.getChannelManagerChargeType()!=null){
                    for (Attribute aa:attribute3) {
                        if(Objects.nonNull(aa.getValue())&&aa.getValue().equals(ljHouseSaleInfo.getChannelManagerChargeType())){
                            excelUtil.addValue(aa.getLabel());  //渠道经理结佣方式
                        }
                    }
                }else{
                    excelUtil.addValue();
                }

                if(ljHouseSaleInfo.getChannelManagerChargeScale()!=null){
                    excelUtil.addValue(ljHouseSaleInfo.getChannelManagerChargeScale()+"");//佣金点位（%）
                }else{
                    excelUtil.addValue();
                }

                if(ljHouseSaleInfo.getChannelManagerChargeMoney()!=null){
                    excelUtil.addValue(ljHouseSaleInfo.getChannelManagerChargeMoney()+""); //渠道经理佣金
                }else{
                    excelUtil.addValue();
                }

                if(ljHouseSaleInfo.getDutyMan()!=null){
                    excelUtil.addValue(ljHouseSaleInfo.getDutyMan());//个人姓名
                }else{
                    excelUtil.addValue();
                }

                if(ljHouseSaleInfo.getDutyManMoney()!=null){
                    excelUtil.addValue(ljHouseSaleInfo.getDutyManMoney()+"");//个人佣金
                }else{
                    excelUtil.addValue();
                }

                if(ljHouseSaleInfo.getAreaManage()!=null){
                    for(LabelValue ll:labelValues){
                        if(ljHouseSaleInfo.getAreaManage().equals(ll.getValue())){
                            excelUtil.addValue(ll.getLabel());//区域经理姓名
                        }
                    }
                }else{
                    excelUtil.addValue();
                }
                if(ljHouseSaleInfo.getAreaManagerChargeType()!=null){
                    for (Attribute aa:attribute3) {
                        if(Objects.nonNull(aa.getValue())&&aa.getValue().equals(ljHouseSaleInfo.getAreaManagerChargeType())){
                            excelUtil.addValue(aa.getLabel());  //区域经理结佣方式",
                        }
                    }
                }else{
                    excelUtil.addValue();
                }


                if(ljHouseSaleInfo.getAreaManagerChargeScale()!=null){
                    excelUtil.addValue(ljHouseSaleInfo.getAreaManagerChargeScale()+"");//佣金点位(%)
                }else{
                    excelUtil.addValue();
                }

                if(ljHouseSaleInfo.getAreaManagerChargeMoney()!=null){
                    excelUtil.addValue(ljHouseSaleInfo.getAreaManagerChargeMoney()+"");  //区域经理佣金
                }else{
                    excelUtil.addValue();
                }

                if(ljHouseSaleInfo.getAreaManage()!=null){
                    for(LabelValue ll:labelValues){
                        if(ljHouseSaleInfo.getChannelChief().equals(ll.getValue())){
                            excelUtil.addValue(ll.getLabel());//渠道总监姓名
                        }
                    }
                }else{
                    excelUtil.addValue();
                }

                if(ljHouseSaleInfo.getChannelChiefChargeType()!=null){
                    for (Attribute aa:attribute3) {
                        if(Objects.nonNull(aa.getValue())&&aa.getValue().equals(ljHouseSaleInfo.getChannelChiefChargeType())){
                            excelUtil.addValue(aa.getLabel());  //渠道总监结佣方式
                        }
                    }
                }else{
                    excelUtil.addValue();
                }

                if(ljHouseSaleInfo.getChannelChiefChargeScale()!=null){
                    excelUtil.addValue(ljHouseSaleInfo.getChannelChiefChargeScale()+"");//佣金点位(%)
                }else{
                    excelUtil.addValue();
                }

                if(ljHouseSaleInfo.getChannelChiefChargeMoney()!=null){
                    excelUtil.addValue(ljHouseSaleInfo.getChannelChiefChargeMoney()+"");//渠道总监佣金（元）
                }else{
                    excelUtil.addValue();
                }

                List<Attribute> attribute4 = attributeService.queryAttributesByActiveKey("LJOA.CHANNEL.COMPANY_CHARGE_STATUS");
                if(ljHouseSaleInfo.getInsideChargeStatus()!=null){
                    for (Attribute aa:attribute4) {
                        if(Objects.nonNull(aa.getValue())&&aa.getValue().equals(ljHouseSaleInfo.getInsideChargeStatus())){
                            excelUtil.addValue(aa.getLabel());  //  公司内部结佣状态
                        }
                    }
                }else{
                    excelUtil.addValue();  //  公司内部结佣状态
                }

                if(ljHouseSaleInfo.getChannelCompany()!=null){
                    for(LabelValue l:labelValue){
                        if(l.getValue().equals(ljHouseSaleInfo.getChannelCompany())){
                            excelUtil.addValue(l.getLabel());  //  第三方渠道名称
                        }
                    }
                }else{
                    excelUtil.addValue();  //  第三方渠道名称
                }

                if(ljHouseSaleInfo.getChannelChargeType()!=null){
                    for (Attribute aa:attribute3) {
                        if(Objects.nonNull(aa.getValue())&&aa.getValue().equals(ljHouseSaleInfo.getChannelChargeType())){
                            excelUtil.addValue(aa.getLabel());  //第三方渠道佣金结佣方式
                        }
                    }
                }else{
                    excelUtil.addValue();
                }


                if(ljHouseSaleInfo.getChannelChargeScale()!=null){
                    excelUtil.addValue(ljHouseSaleInfo.getChannelChargeScale()+"");//第三方渠佣金比例（%）
                }else{
                    excelUtil.addValue();
                }

                if(ljHouseSaleInfo.getChannelChargeMoney()!=null){
                    excelUtil.addValue(ljHouseSaleInfo.getChannelChargeMoney()+"");//第三方渠道佣金（元）
                }else{
                    excelUtil.addValue();
                }

                List<Attribute> attribute5 = attributeService.queryAttributesByActiveKey("LJOA.CHANNEL.TAX_TYPE");
                if(ljHouseSaleInfo.getChannelTaxType()!=null){
                    for (Attribute aa:attribute5) {
                        if(Objects.nonNull(aa.getValue())&&aa.getValue().equals(ljHouseSaleInfo.getChannelTaxType())){
                            excelUtil.addValue(aa.getLabel());  //第三方渠道扣税方式
                        }
                    }
                }else{
                    excelUtil.addValue();
                }

                if(ljHouseSaleInfo.getChannelTaxScale()!=null){
                    excelUtil.addValue(ljHouseSaleInfo.getChannelTaxScale()+"");//扣税比例（%）
                }else{
                    excelUtil.addValue();
                }

                if(ljHouseSaleInfo.getChannelAfterTaxMoney()!=null){
                    excelUtil.addValue(ljHouseSaleInfo.getChannelAfterTaxMoney()+"");//第三方渠道税后佣金（元）
                }else{
                    excelUtil.addValue();
                }

                List<Attribute> attribute6 = attributeService.queryAttributesByActiveKey("LJOA.MAIDSTATUS");
                if(ljHouseSaleInfo.getChannelChargeStatus()!=null){
                    for (Attribute aa:attribute6) {
                        if(Objects.nonNull(aa.getValue())&&aa.getValue().equals(ljHouseSaleInfo.getChannelChargeStatus())){
                            excelUtil.addValue(aa.getLabel());  //第三方渠道结佣状态
                        }
                    }
                }else{
                    excelUtil.addValue();
                }

                if(ljHouseSaleInfo.getChannelChargeSendDate()!=null){
                    excelUtil.addValue(ljHouseSaleInfo.getChannelChargeSendDate()+"");//第三方渠道结佣时间
                }else{
                    excelUtil.addValue();
                }

                if(ljHouseSaleInfo.getCompanyProfit()!=null){
                    excelUtil.addValue(ljHouseSaleInfo.getCompanyProfit()+"");  //公司利润（元）
                }else{
                    excelUtil.addValue();
                }

            }

        }

        byte[] body=null;
        InputStream in=excelUtil.write();
        HttpHeaders headers = new HttpHeaders();
        try {
            body=new byte[in.available()];
            in.read(body);

            headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
            headers.add("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName + ".xls", "UTF-8"));
            headers.add("Pragma", "no-cache");
            headers.add("Expires", "0");
        } catch (Exception e) {
            e.printStackTrace();
        }

        HttpStatus statusCode=HttpStatus.OK;

        ResponseEntity<byte[]> response=new ResponseEntity<byte[]>(body,headers,statusCode);
        return response;
    }
}
