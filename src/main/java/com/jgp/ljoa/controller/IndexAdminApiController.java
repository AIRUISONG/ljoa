package com.jgp.ljoa.controller;

import com.jgp.ljoa.bean.Stock;
import com.jgp.ljoa.bean.TransactionsNum;
import com.jgp.ljoa.channel.model.LjHouseInfo;
import com.jgp.ljoa.channel.model.LjHouseSaleInfo;
import com.jgp.ljoa.channel.model.LjProjectInfo;
import com.jgp.ljoa.channel.service.LjHouseInfoService;
import com.jgp.ljoa.channel.service.LjHouseSaleInfoService;
import com.jgp.ljoa.channel.service.LjProjectInfoService;
import com.jgp.ljoa.expense.model.LjExpense;
import com.jgp.ljoa.expense.service.LjExpenseService;
import com.jgp.ljoa.marketing.model.HouseReturn;
import com.jgp.ljoa.marketing.model.MarketingSaleInfo;
import com.jgp.ljoa.marketing.service.HouseReturnService;
import com.jgp.ljoa.marketing.service.MarketingChargeInfoService;
import com.jgp.ljoa.marketing.service.MarketingSaleInfoService;
import com.jgp.sys.controller.JGPController;
import com.jgp.sys.model.Attribute;
import com.jgp.sys.service.AttributeService;
import com.jgp.sys.ui.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * Created by Administrator on 2018/8/16 0016.
 */
@RestController
@RequestMapping("/ljoa/indexAdminApiController")
public class IndexAdminApiController extends JGPController {
    @Autowired
    private LjHouseInfoService ljHouseInfoService;
    @Autowired
    private LjHouseSaleInfoService ljHouseSaleInfoService;
    @Autowired
    private MarketingSaleInfoService marketingSaleInfoService;
    @Autowired
    private MarketingChargeInfoService marketingChargeInfoService;
    @Autowired
    private LjExpenseService ljExpenseService;
    @Autowired
    private LjProjectInfoService ljProjectInfoService;
    @Autowired
    private AttributeService attributeService;
    @Autowired
    private HouseReturnService houseReturnService;

    @RequestMapping("indexAdmin")
    public Result indexAdmin(){
        Map<String,Object> map = new HashMap<>();

        //渠道整合的收入支出
        LjHouseInfo ljHouseInfo = new LjHouseInfo();
        ljHouseInfo.setCompanyChargeStatus("2");//已结佣
        ljHouseInfo.setHouseType("1");//渠道整合
        List<LjHouseInfo> ljHouseInfos = ljHouseInfoService.queryGroupHouseInfoToCount(ljHouseInfo);
        Float incomeQD = 0.0f;//收入=公司佣金
        Float expenditureQD = 0.0f;//支出=第三方渠道佣金+各个岗位的佣金 + 报销（已经结佣）=（公司佣金-纯利润）+报销
        for(LjHouseInfo l:ljHouseInfos){
            LjHouseSaleInfo ljHouseSaleInfo = ljHouseSaleInfoService.queryOneLjHouseSaleInfoBuUUid(l.getId());
            if("10".equals(ljHouseSaleInfo.getChargeStatus())){
                if("2".equals(l.getCompanyChargeType())){//定额
                    expenditureQD += (l.getCompanyChargeMoney()-ljHouseSaleInfo.getCompanyProfit());
                }else{//比例
                    expenditureQD +=(l.getCompanyChargeScale()*ljHouseSaleInfo.getTotalPrice()/100-ljHouseSaleInfo.getCompanyProfit());
                }
            }
            if("2".equals(l.getCompanyChargeType())){//定额
                incomeQD+=l.getCompanyChargeMoney();
            }else{//比例
                incomeQD+=l.getCompanyChargeScale()*ljHouseSaleInfo.getTotalPrice()/100;
            }
        }
        LjExpense ljExpense = new LjExpense();
        ljExpense.setPayStatus("1");//已打款
        ljExpense.setOrgUuid("3bc4148f06684cf6bebcf80bbbf2971c");//渠道整合
        List<LjExpense> ljExpenses = ljExpenseService.queryExpenseApproval(ljExpense, null);
        for(LjExpense l:ljExpenses){
            expenditureQD+=l.getMoney();
        }
        map.put("incomeQD",incomeQD);
        map.put("expenditureQD",expenditureQD);

        //营销的收入和支出
        LjHouseInfo ljHouseInfo1 = new LjHouseInfo();
        ljHouseInfo1.setCompanyChargeStatus("2");//已结佣
        ljHouseInfo1.setHouseType("2");//营销
        List<LjHouseInfo> ljHouseInfos1 = ljHouseInfoService.queryGroupHouseInfoToCount(ljHouseInfo1);
        Float incomeYX = 0.0f;//收入
        Float expenditureYX = 0.0f;//支出
        for(LjHouseInfo l:ljHouseInfos1){
            MarketingSaleInfo marketingSaleInfo = marketingSaleInfoService.queryMarketingSaleInfoByUuid(l.getId());
            //支出=毛利润=公司+前置
            incomeYX+=marketingSaleInfo.getGrossProfit();
            //（各岗位+第三方）=毛利润-纯利润
            if ("12".equals(marketingSaleInfo.getInsideChargeStatus())){
                expenditureYX+=(marketingSaleInfo.getGrossProfit()-marketingSaleInfo.getPureProfit());
            }

        }
        LjExpense ljExpense1 = new LjExpense();
        ljExpense1.setPayStatus("1");//报销
        ljExpense1.setOrgUuid("d4a1dd4ca37749fc9380f8d61454305c");//营销
        List<LjExpense> ljExpenses1 = ljExpenseService.queryExpenseApproval(ljExpense1, null);
        for(LjExpense l:ljExpenses1){
            expenditureYX+=l.getMoney();
        }
        map.put("incomeYX",incomeYX);
        map.put("expenditureYX",expenditureYX);

        //统计
        Float income = 0.0f;//销售额
        Float trueIncome = 0.0f;//应收款
        Float returnMoney=0.0f;//回款额
        Float refund=0.0f;//退款
        LjHouseInfo ljHouseInfo4 = new LjHouseInfo();
        ljHouseInfo4.setStatus("2");
        List<LjHouseInfo> ljHouseInfos4 = ljHouseInfoService.queryGroupHouseInfoToCount(ljHouseInfo4);//查找已售房源，计算销售额
        for(LjHouseInfo l:ljHouseInfos4){
            if(Objects.nonNull(l.getCompanyChargeMoneyTrue())){
                income+=l.getCompanyChargeMoney();
                trueIncome+=l.getCompanyChargeMoneyTrue();
                if("2".equals(l.getCompanyChargeStatus())){
                    returnMoney+=l.getCompanyChargeMoney();
                }

            }
        }
        HouseReturn houseReturn = new HouseReturn();
        houseReturn.setMoneyStatus("2");//查找已结款退款
        List<HouseReturn> houseReturns = houseReturnService.queryGroupHouseReturn(houseReturn, null);
        for(HouseReturn h:houseReturns){
            refund+=h.getActuaLReturnMoney();
        }
        map.put("income",income);//销售额
        map.put("trueIncome",trueIncome);//应收款
        map.put("returnMoney",returnMoney);//回款
        map.put("refund",refund);//退款额

        //房源成交量
        //获得所有区
        List<Attribute> attributes = attributeService.queryAttributesByActiveKey("LJOA.CHANNEL.AREA");
        List<TransactionsNum> transactionsNums = new ArrayList<>();
        Float total=0.0f;//计算总成交量
        for (Attribute a: attributes){
            LjProjectInfo ljProjectInfo = new LjProjectInfo();
            ljProjectInfo.setArea(a.getValue());
            Integer num = 0;
            List<LjProjectInfo> ljProjectInfos1 = ljProjectInfoService.queryGroupProject(ljProjectInfo);
            for (LjProjectInfo l:ljProjectInfos1){
                LjHouseInfo ljHouseInfo2 = new LjHouseInfo();
                ljHouseInfo2.setProjectUuid(l.getId());
                ljHouseInfo2.setCompanyChargeStatus("2");
                List<LjHouseInfo> ljHouseInfos2 = ljHouseInfoService.queryGroupHouseInfoToCount(ljHouseInfo2);
                num+=ljHouseInfos2.size();
            }
            total+=num;
            TransactionsNum transactionsNum = new TransactionsNum();
            transactionsNum.setArea(a.getLabel());
            transactionsNum.setNum(num);
            transactionsNums.add(transactionsNum);
        }
        for (TransactionsNum t:transactionsNums){
            if(total!=0){
                t.setScale(t.getNum()*100/total);
            }else{
                t.setScale(0.0f);
            }

        }
        Collections.sort(transactionsNums);
        map.put("transactionsNum",transactionsNums);

        //库存
        List<Stock> stocks = new ArrayList<>();
        List<LjProjectInfo> ljProjectInfos1 = ljProjectInfoService.queryAllLjProjectInfo();//查找所有项目
        for(LjProjectInfo l:ljProjectInfos1){
            Stock stock = new Stock();
            if(l.getProjectName()!=null){
                stock.setProjectName(l.getProjectName());
                List<LjHouseInfo> ljHouseInfos2 = ljHouseInfoService.queryGroupLjHouseInfoByUUID(l.getId());
                stock.setEntry(ljHouseInfos2.size());
                LjHouseInfo ljHouseInfo2 = new LjHouseInfo();
                ljHouseInfo2.setProjectUuid(l.getId());
                ljHouseInfo2.setCompanyChargeStatus("2");
                List<LjHouseInfo> ljHouseInfos3 = ljHouseInfoService.queryGroupHouseInfoToCount(ljHouseInfo2);
                stock.setMaid(ljHouseInfos3.size());
                stock.setSurplus(ljHouseInfos2.size()-ljHouseInfos3.size());
                stocks.add(stock);
            }

        }
        map.put("stock",stocks);
        return ajaxReData("mapData",map);
    }

    //房源成交数量
    @RequestMapping("numHouse")
    public Result numHouse(LjProjectInfo ljProjectInfo){
        List<Attribute> provinces = attributeService.queryAttributesByActiveKey("LJOA.CHANNEL.PROVINCE");
        for(Attribute a: provinces){
            if (ljProjectInfo.getProvince().equals(a.getLabel())){
                ljProjectInfo.setProvince(a.getValue());
            }
        }
        List<Attribute> cities = attributeService.queryAttributesByActiveKey("LJOA.CHANNEL.CITY");
        for(Attribute a: cities){
            if (ljProjectInfo.getCity().equals(a.getLabel())){
                ljProjectInfo.setCity(a.getValue());
            }
        }
        List<Attribute> area = attributeService.queryAttributesByActiveKey("LJOA.CHANNEL.AREA");
        for(Attribute a: area){
            if (ljProjectInfo.getArea().equals(a.getLabel())){
                ljProjectInfo.setArea(a.getValue());
            }
        }
        Map<String,Object> map = new HashMap<>();
        List<Integer> numYX = new ArrayList<>();//营销每月销售房源统计
        List<Integer> numQD = new ArrayList<>();//渠道每月销售房源统计

        //根据地区名字查询营销的项目--》按照月份查询本年已结佣房子数量
        ljProjectInfo.setProjectType("2");
        List<LjProjectInfo> ljProjectInfos = ljProjectInfoService.queryGroupProject(ljProjectInfo);
        for(int i=1;i<=12;i++){
            int num=0;//本月数量
            for(LjProjectInfo p:ljProjectInfos){
                List<LjHouseInfo> ljHouseInfos = ljHouseInfoService.queryGroupHouseMonth(p.getId(), i);
                num+=ljHouseInfos.size();
            }
            numYX.add(num);
        }
        //根据地区名字查询渠道的项目--》按照月份查询本年已结佣房子数量
        ljProjectInfo.setProjectType("1");
        List<LjProjectInfo> ljProjectInfos1 = ljProjectInfoService.queryGroupProject(ljProjectInfo);
        for(int i=1;i<=12;i++){
            int num=0;//本月数量
            for(LjProjectInfo p:ljProjectInfos1){
                List<LjHouseInfo> ljHouseInfos = ljHouseInfoService.queryGroupHouseMonth(p.getId(), i);
                num+=ljHouseInfos.size();
            }
            numQD.add(num);
        }
        map.put("numYX",numYX);
        map.put("numQD",numQD);
        return ajaxReData("mapData",map);
    }
}
