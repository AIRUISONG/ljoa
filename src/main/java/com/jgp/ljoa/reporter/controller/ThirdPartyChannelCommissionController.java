package com.jgp.ljoa.reporter.controller;


import com.jgp.attachment.model.FileInfo;
import com.jgp.attachment.service.DocService;
import com.jgp.ljoa.channel.model.LjChannelCompany;
import com.jgp.ljoa.channel.model.LjHouseInfo;
import com.jgp.ljoa.channel.model.LjHouseSaleInfo;
import com.jgp.ljoa.channel.model.LjProjectInfo;
import com.jgp.ljoa.channel.service.LjChannelCompanyService;
import com.jgp.ljoa.channel.service.LjHouseInfoService;
import com.jgp.ljoa.channel.service.LjHouseSaleInfoService;
import com.jgp.ljoa.channel.service.LjProjectInfoService;
import com.jgp.ljoa.marketing.model.CustomerInfo;
import com.jgp.ljoa.marketing.model.MarketingChargeInfo;
import com.jgp.ljoa.marketing.service.CustomerInfoService;
import com.jgp.ljoa.marketing.service.MarketingChargeInfoService;
import com.jgp.ljoa.reporter.bean.ThirdPartyChannelCommission;
import com.jgp.ljoa.reporter.common.Num2Rmb;
import com.jgp.reporter.ReporterFormat;
import com.jgp.sys.controller.JGPController;
import com.jgp.sys.model.Attribute;
import com.jgp.sys.service.AttributeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.*;

/**
 * Created by Administrator on 2018/8/14.
 */
/*
作者  SSF
时间   2018/8/14
*/
@Controller
@RequestMapping("/ljoa/reporter/ThirdPartyChannelCommissionController/")
public class ThirdPartyChannelCommissionController extends JGPController {
    @Autowired
    private LjHouseSaleInfoService ljHouseSaleInfoService;
    @Autowired
    private LjHouseInfoService ljHouseInfoService;
    @Autowired
    private LjProjectInfoService ljProjectInfoService;
    @Autowired
    private AttributeService attributeService;
    @Autowired
    private LjChannelCompanyService ljChannelCompanyService;
    @Autowired
    private MarketingChargeInfoService marketingChargeInfoService;
    @Autowired
    private CustomerInfoService customerInfoService;
    @Autowired
    private DocService docService;
//待测
    @RequestMapping("thirdPartyChannelCommission")
    public String thirdPartyChannelCommission(String[] array, Model model) throws IOException {
        Num2Rmb num2Rmb=new Num2Rmb();
        List<LjHouseSaleInfo> ljHouseSaleInfos=new ArrayList<>();//房源销售信息
//        String[] split = array.split(",");

        for(int i=0;i<array.length;i++){
           LjHouseSaleInfo ljHouseSaleInfo = ljHouseSaleInfoService.queryOneLjHouseSaleInfo(array[i]);
           if(Objects.nonNull(ljHouseSaleInfo.getId())){
               ljHouseSaleInfos.add(ljHouseSaleInfo);
           }
       }
        LjChannelCompany ljCh = ljChannelCompanyService.queryOneLjChannelCompany(ljHouseSaleInfos.get(0).getChannelCompany());
        LjHouseInfo ljHouseInfo = ljHouseInfoService.queryOneLjHouseInfoByid(ljHouseSaleInfos.get(0).getHouseUuid());
        //对应项目
        LjProjectInfo ljProjectInfo = ljProjectInfoService.queryOneLjProjectInfoById(ljHouseInfo.getProjectUuid());

        List<ThirdPartyChannelCommission> list=new ArrayList<>();

     for(int j=0;j<ljHouseSaleInfos.size();j++){
         ThirdPartyChannelCommission t=new ThirdPartyChannelCommission();
         t.setProjectName(ljProjectInfo.getProjectName());//项目名
         t.setSubTime(ljHouseSaleInfos.get(j).getBuyTime());//认购时间
         t.setNetSignTime(ljHouseSaleInfos.get(j).getSignTime());//签约时间
         t.setCustomerName(ljHouseSaleInfos.get(j).getCustomerName());//客户姓名
         LjHouseInfo ljHouseInfo1 = ljHouseInfoService.queryOneLjHouseInfoByid(ljHouseSaleInfos.get(j).getHouseUuid());
         t.setRoomNumber(ljHouseInfo1.getBuildingNo()+"楼"+ljHouseInfo1.getUnitNo()+"单元"+ljHouseInfo1.getRoomNo()+"号");//房号
         t.setAcreage(ljHouseInfo1.getRoomArea());//面积
         t.setTotalHouseMoney(ljHouseInfo1.getSaleMoney());//总房款
         List<Attribute> attributes = attributeService.queryAttributesByActiveKey("LJOA.CHANNEL.PAY_TYPE");
         for (Attribute aa:attributes) {
             if(Objects.nonNull(aa.getValue())&&aa.getValue().equals(ljHouseSaleInfos.get(j).getPayType())){
                 ljHouseSaleInfos.get(j).setPayType(aa.getLabel());
             }
         }
         t.setPayMethod(ljHouseSaleInfos.get(j).getPayType());//付款方式
         t.setActualMoney(ljHouseInfo1.getCompanyChargeMoney());//实收金额(公司佣金)
         LjChannelCompany ljChannelCompany = ljChannelCompanyService.queryOneLjChannelCompany(ljHouseSaleInfos.get(j).getChannelCompany());
         t.setSource(ljChannelCompany.getCompanyName());//来源
         t.setCopeWith(ljHouseSaleInfos.get(j).getChannelAfterTaxMoney());//应付佣金金额(税后)
         t.setRealPayment(ljHouseSaleInfos.get(j).getChannelAfterTaxMoney());//实付佣金金额
         t.setRemarks(ljHouseInfo1.getRemark());//备注
         list.add(t);
     }


        Float actualSum=0f;
        Float copeSum=0f;
        Float realSum=0f;
        for (ThirdPartyChannelCommission t:list) {
            actualSum+=t.getActualMoney();
            copeSum+=t.getCopeWith();
            realSum+=t.getRealPayment();
        }
        if(list.size()<5){
            for(int i=0;i<=5-list.size();i++){
                ThirdPartyChannelCommission t=new ThirdPartyChannelCommission();
                list.add(t);
            }

        }
        Map<String,Object> map=new HashMap();
//        map.put("ljList",list);
        map.put("department","邻家渠道");
        List<FileInfo> fileInfos = docService.queryFiles(ljProjectInfo.getId(), "com.jgp.ljoa.channel.model.LjProjectInfo");
        map.put("fileNum",fileInfos.size());//附件数量
        map.put("Year", LocalDate.now().getYear());
        map.put("month", LocalDate.now().getMonthValue());
        map.put("day",LocalDate.now().getDayOfMonth());//日期
        map.put("actualSum",actualSum);
        map.put("copeSum",copeSum);
        map.put("realSum",realSum);
        map.put("corporateName",ljCh.getCompanyName());//收款单位
        map.put("accountNumber1",ljCh.getCompanyAccount());//收款单位账号
        map.put("openBank1",ljCh.getCompanyBank());//开户行

        map.put("RMB",num2Rmb.toHanStr(realSum));
        map.put("RMBNum",realSum);

        map.put("clientName",ljCh.getDeputeName());//委托收款人姓名
        map.put("accountNumber2",ljCh.getDeputeIdentity());//委托收款人账号
        map.put("openBank2",ljCh.getDeputeBank());//委托收款人开户行
//        String fileUrl1 = new PathUtil().getWebInfPath() + "classes/jasper/";
//        map.put("subPath",fileUrl1);

//        String fileUrl = new PathUtil().getWebInfPath() + "classes"+File.separator+"static"+File.separator;
//        File file = new File(File.separator+"images"+File.separator+"ljlogo.jpg"); //图片

        Resource resource = new ClassPathResource("/static/images/ljlogo.jpg");
       InputStream i = resource.getInputStream();
       InputStream in = null;

        try {

            map.put("image", i);  //图片
        } catch (Exception e) {
            e.printStackTrace();
        }

        Resource resource2 = new ClassPathResource("/static/images/ysh.png");
        InputStream i2 = resource2.getInputStream();
        try {

            map.put("backgroup",i2);  //图片
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jasperReporter(model,list,map,"ThirdPartyChannelCommissionReport", ReporterFormat.PDF);

    }

    @RequestMapping("thirdPartyChannelCommissionYX")
    public String thirdPartyChannelCommissionYX(String[] array, Model model) throws IOException {
        Num2Rmb num2Rmb=new Num2Rmb();
        List<MarketingChargeInfo> marketingChargeInfos=new ArrayList<>();//第三方信息

        for(int i=0;i<array.length;i++){
            MarketingChargeInfo marketingChargeInfo = marketingChargeInfoService.queryOneMarketingChargeInfo(array[i]);
            if(Objects.nonNull(marketingChargeInfo.getId())){
                marketingChargeInfos.add(marketingChargeInfo);
            }
        }
        //第三方企业信息
        LjChannelCompany ljCh = ljChannelCompanyService.queryOneLjChannelCompany(marketingChargeInfos.get(0).getChargeTargetUuid());
        LjHouseInfo ljHouseInfo = ljHouseInfoService.queryOneLjHouseInfoByid(marketingChargeInfos.get(0).getHouseUuid());
       //对应项目
        LjProjectInfo ljProjectInfo = ljProjectInfoService.queryOneLjProjectInfoById(ljHouseInfo.getProjectUuid());

        List<ThirdPartyChannelCommission> list=new ArrayList<>();

        for(int j=0;j<marketingChargeInfos.size();j++){
            ThirdPartyChannelCommission t=new ThirdPartyChannelCommission();
            //客户信息
            CustomerInfo customerInfo = customerInfoService.queryCustomerInfoByHouseUuid(marketingChargeInfos.get(j).getHouseUuid());
            t.setProjectName(ljProjectInfo.getProjectName());//项目名
            t.setSubTime(customerInfo.getEarnestDate());//认购时间
            t.setNetSignTime(customerInfo.getTradingDate());//签约时间
            t.setCustomerName(customerInfo.getCustomerName());//客户姓名
            //房源
            LjHouseInfo ljHouseInfo1 = ljHouseInfoService.queryOneLjHouseInfoByid(marketingChargeInfos.get(j).getHouseUuid());
            t.setRoomNumber(ljHouseInfo1.getBuildingNo()+"楼"+ljHouseInfo1.getUnitNo()+"单元"+ljHouseInfo1.getRoomNo()+"号");//房号
            t.setAcreage(ljHouseInfo1.getRoomArea());//面积
            t.setTotalHouseMoney(ljHouseInfo1.getSaleMoney());//总房款
            List<Attribute> attributes = attributeService.queryAttributesByActiveKey("LJ_CUSTOMER_INFO.PATYTYPE");
            for (Attribute aa:attributes) {
                if(Objects.nonNull(aa.getValue())&&aa.getValue().equals(customerInfo.getPatyType())){
                    customerInfo.setPatyType(aa.getLabel());
                }
            }
            t.setPayMethod(customerInfo.getPatyType());//付款方式
            t.setActualMoney(ljHouseInfo1.getCompanyChargeMoney());//实收金额(公司佣金)
            LjChannelCompany ljChannelCompany = ljChannelCompanyService.queryOneLjChannelCompany(marketingChargeInfos.get(j).getChargeTargetUuid());
            t.setSource(ljChannelCompany.getCompanyName());//来源
            t.setCopeWith(marketingChargeInfos.get(j).getChargeMoney());//应付佣金金额(税后)
            t.setRealPayment(marketingChargeInfos.get(j).getChargeMoney());//实付佣金金额
            t.setRemarks(ljHouseInfo1.getRemark());//备注
            list.add(t);
        }
        Float actualSum=0f;
        Float copeSum=0f;
        Float realSum=0f;
        for (ThirdPartyChannelCommission t:list) {
            actualSum+=t.getActualMoney();
            copeSum+=t.getCopeWith();
            realSum+=t.getRealPayment();
        }
        if(list.size()<5){
            for(int i=0;i<=5-list.size();i++){
                ThirdPartyChannelCommission t=new ThirdPartyChannelCommission();
                list.add(t);
            }
        }
        Map<String,Object> map=new HashMap();
//        map.put("ljList",list);
        map.put("department","营销");
        List<FileInfo> fileInfos = docService.queryFiles(ljProjectInfo.getId(), "com.jgp.ljoa.channel.model.LjProjectInfo");
        map.put("fileNum",fileInfos.size());//附件数量
        map.put("Year", LocalDate.now().getYear());
        map.put("month", LocalDate.now().getMonthValue());
        map.put("day",LocalDate.now().getDayOfMonth());//日期
        map.put("actualSum",actualSum);
        map.put("copeSum",copeSum);
        map.put("realSum",realSum);
        map.put("corporateName",ljCh.getCompanyName());//收款单位
        map.put("accountNumber1",ljCh.getCompanyAccount());//收款单位账号
        map.put("openBank1",ljCh.getCompanyBank());//开户行

        map.put("RMB",num2Rmb.toHanStr(realSum));
        map.put("RMBNum",realSum);

        map.put("clientName",ljCh.getDeputeName());//委托收款人姓名
        map.put("accountNumber2",ljCh.getDeputeIdentity());//委托收款人账号
        map.put("openBank2",ljCh.getDeputeBank());//委托收款人开户行
//        String fileUrl1 = new PathUtil().getWebInfPath() + "classes/jasper/";
//        map.put("subPath",fileUrl1);
//        String fileUrl = new PathUtil().getWebInfPath() + "classes"+File.separator+"static"+File.separator;
//        File file = new File(File.separator+"images"+File.separator+"ljlogo.jpg"); //图片
//        InputStream in = null;
//        Resource resource = new ClassPathResource("/static/images/ljlogo.jpg");
//        InputStream i = resource.getInputStream();
        InputStream i =  getClass().getClassLoader().getResourceAsStream("static/images/ljlogo.jpg");
        try {

            map.put("image", i);  //图片
        } catch (Exception e) {
            e.printStackTrace();
        }

//        Resource resource2 = new ClassPathResource("/static/images/ljlogo.jpg");
//        InputStream i2 = resource2.getInputStream();
        InputStream i2 =  getClass().getClassLoader().getResourceAsStream("static/images/ysh.png");
        try {
            map.put("backgroup",i2);  //图片
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jasperReporter(model,list,map,"ThirdPartyChannelCommissionReport", ReporterFormat.PDF);


    }
}
