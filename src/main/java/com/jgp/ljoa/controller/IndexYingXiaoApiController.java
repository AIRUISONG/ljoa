package com.jgp.ljoa.controller;

import com.jgp.ljoa.bean.ProjectIncomeAndExpenditure;
import com.jgp.ljoa.bean.Stock;
import com.jgp.ljoa.bean.TransactionsNum;
import com.jgp.ljoa.channel.model.LjHouseInfo;
import com.jgp.ljoa.channel.model.LjProjectInfo;
import com.jgp.ljoa.channel.service.LjHouseInfoService;
import com.jgp.ljoa.channel.service.LjProjectInfoService;
import com.jgp.ljoa.expense.model.LjExpense;
import com.jgp.ljoa.expense.service.LjExpenseService;
import com.jgp.ljoa.marketing.model.MarketingSaleInfo;
import com.jgp.ljoa.marketing.service.CustomerInfoService;
import com.jgp.ljoa.marketing.service.MarketingSaleInfoService;
import com.jgp.sys.controller.JGPController;
import com.jgp.sys.model.Attribute;
import com.jgp.sys.query.QueryFilterList;
import com.jgp.sys.service.AttributeService;
import com.jgp.sys.ui.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by Administrator on 2018/8/14 0014.
 */
@RestController
@RequestMapping("/ljoa/indexYingXiaoApiController")
public class IndexYingXiaoApiController extends JGPController {
    @Autowired
    private LjProjectInfoService ljProjectInfoService;
    @Autowired
    private LjHouseInfoService ljHouseInfoService;
    @Autowired
    private CustomerInfoService customerInfoService;
    @Autowired
    private MarketingSaleInfoService marketingSaleInfoService;
    @Autowired
    private LjExpenseService ljExpenseService;
    @Autowired
    private AttributeService attributeService;


    @RequestMapping("indexYingXiao")
    public Result indexYingXiao(){
        Map<String,Object> map = new HashMap<>();
        //查询已售、未结佣、营销的房源，计算应收款
        LjHouseInfo ljHouseInfo = new LjHouseInfo();
        ljHouseInfo.setStatus("2");
//        ljHouseInfo.setCompanyChargeStatus("1");//未结佣
        ljHouseInfo.setHouseType("2");//营销
        List<LjHouseInfo> ljHouseInfos = ljHouseInfoService.queryGroupHouseInfoToCount(ljHouseInfo);
        Float receivables = 0.0f;
        for(LjHouseInfo l:ljHouseInfos){
            if(Objects.nonNull(l.getCompanyChargeMoney())){
                receivables+=l.getCompanyChargeMoney();//公司佣金
            }
            MarketingSaleInfo marketingSaleInfo = marketingSaleInfoService.queryMarketingSaleInfoByUuid(l.getId());
      /*      if(Objects.nonNull(marketingSaleInfo.getPrepositionEarn())){
                receivables+=marketingSaleInfo.getPrepositionEarn();//前置赚取
            }*/

        }
        map.put("receivables",receivables);//应收款

        LjHouseInfo HouseInfo = new LjHouseInfo();
        HouseInfo.setStatus("2");
        HouseInfo.setCompanyChargeStatus("2");//未结佣
        HouseInfo.setHouseType("2");//营销
        List<LjHouseInfo> HouseInfos = ljHouseInfoService.queryGroupHouseInfoToCount(HouseInfo);
        Float receivables1 = 0.0f;
        for(LjHouseInfo l:HouseInfos){
            if(Objects.nonNull(l.getCompanyChargeMoney())){
                receivables1+=l.getCompanyChargeMoney();//公司佣金
            }
            MarketingSaleInfo marketingSaleInfo = marketingSaleInfoService.queryMarketingSaleInfoByUuid(l.getId());
       /*     if(Objects.nonNull(marketingSaleInfo.getPrepositionEarn())){
                receivables1+=marketingSaleInfo.getPrepositionEarn();//前置赚取
            }*/
        }

        map.put("receivables1",receivables1);//已收款

        //计算利润
  /*      QueryFilterList queryFilters = new QueryFilterList();
        queryFilters.addFilter("channeChargeStatus","eq","12");//已结佣*/

        List<MarketingSaleInfo> marketingSaleInfos = marketingSaleInfoService.queryAllMarketingSaleInfo();//所有销售信息
        List<MarketingSaleInfo> collect = marketingSaleInfos.stream().filter(m -> {
            LjHouseInfo ljHouseInfo2 = ljHouseInfoService.queryOneLjHouseInfoByid(m.getHouseUuid());
            if(Objects.nonNull(ljHouseInfo2)){
                if ("2".equals(ljHouseInfo2.getCompanyChargeStatus())) {
                    return true;
                } else {
                    return false;
                }
            }else{
                return false;
            }

        }).collect(Collectors.toList());
        Float profit = 0.0f;//利润
        for (MarketingSaleInfo m:collect){
            profit+=m.getPureProfit();
        }
        LjExpense ljExpense = new LjExpense();
        ljExpense.setPayStatus("1");//已打款
        ljExpense.setOrgUuid("d4a1dd4ca37749fc9380f8d61454305c");//营销
        List<LjExpense> ljExpenses = ljExpenseService.queryExpenseApproval(ljExpense, null);
        for(LjExpense l:ljExpenses){
            profit-=l.getMoney();
        }
        map.put("profit",profit);//利润



        //房源成交数量
        //获得所有区
        List<Attribute> attributes = attributeService.queryAttributesByActiveKey("LJOA.CHANNEL.AREA");
        List<TransactionsNum> transactionsNums = new ArrayList<>();
        for (Attribute a: attributes){
            LjProjectInfo ljProjectInfo = new LjProjectInfo();
            ljProjectInfo.setProjectType("2");
            ljProjectInfo.setArea(a.getValue());
            Integer num = 0;
            List<LjProjectInfo> ljProjectInfos1 = ljProjectInfoService.queryGroupProject(ljProjectInfo);
            for (LjProjectInfo l:ljProjectInfos1){
                LjHouseInfo ljHouseInfo1 = new LjHouseInfo();
                ljHouseInfo1.setProjectUuid(l.getId());
                ljHouseInfo1.setHouseType("2");
                ljHouseInfo1.setStatus("2");
                ljHouseInfo1.setCompanyChargeStatus("2");
                List<LjHouseInfo> ljHouseInfos1 = ljHouseInfoService.queryGroupHouseInfoToCount(ljHouseInfo1);
                num+=ljHouseInfos1.size();
            }
            TransactionsNum transactionsNum = new TransactionsNum();
            transactionsNum.setArea(a.getLabel());
            transactionsNum.setNum(num);
            transactionsNums.add(transactionsNum);

        }
        map.put("transactionsNum",transactionsNums);

        //库存
        List<Stock> stocks = new ArrayList<>();
        List<LjProjectInfo> ljProjectInfos1 = ljProjectInfoService.queryGroupLjProjectInfoByProjectType("2", null);//查找营销的项目
        for(LjProjectInfo l:ljProjectInfos1){
            Stock stock = new Stock();
            stock.setProjectName(l.getProjectName());
            List<LjHouseInfo> ljHouseInfos1 = ljHouseInfoService.queryGroupLjHouseInfoByUUID(l.getId());
            stock.setEntry(ljHouseInfos1.size());
            LjHouseInfo ljHouseInfo1 = new LjHouseInfo();
            ljHouseInfo1.setProjectUuid(l.getId());
            ljHouseInfo1.setHouseType("2");
            ljHouseInfo1.setStatus("2");
            ljHouseInfo1.setCompanyChargeStatus("2");
            List<LjHouseInfo> ljHouseInfos2 = ljHouseInfoService.queryGroupHouseInfoToCount(ljHouseInfo1);
            stock.setMaid(ljHouseInfos2.size());
            stock.setSurplus(ljHouseInfos1.size()-ljHouseInfos2.size());
            stocks.add(stock);

        }
        map.put("stock",stocks);

        return ajaxReData("mapData",map);
    }

    @RequestMapping("indexYINFXIAO")
    public Result indexYINFXIAO(){
        //查找营销中各个项目的收支情况
        List<ProjectIncomeAndExpenditure> pies = new ArrayList<>();
        //查找营销的所有项目
        List<LjProjectInfo> ljProjectInfos = ljProjectInfoService.queryGroupLjProjectInfoByProjectTypeYX();
        for (LjProjectInfo p:ljProjectInfos){
            LjHouseInfo ljHouseInfo1 = new LjHouseInfo();
            ljHouseInfo1.setProjectUuid(p.getId());
            ljHouseInfo1.setHouseType("2");//营销
            List<LjHouseInfo> ljHouseInfos1 = ljHouseInfoService.queryHouseInfoSold(ljHouseInfo1,null);//根据条件查找房源
            ProjectIncomeAndExpenditure pie = new ProjectIncomeAndExpenditure();
            pie.setProjectName(p.getProjectName());//项目名
            pie.setId(p.getId());//项目主键

            Float receivable = 0.0f;//已收款
            Float expenditure = 0.0f;//支出
            Float profit=0f;//利润
            Float income=0f;
            for (LjHouseInfo h:ljHouseInfos1){
                income+=h.getCompanyChargeMoney();//应收款
                QueryFilterList queryFilters1 = new QueryFilterList();
                queryFilters1.addFilter("houseUuid","eq",h.getId());
                queryFilters1.addFilter("channeChargeStatus","eq","12");//渠道佣金状态
                queryFilters1.addFilter("insideChargeStatus","eq","12");//内部人员佣金状态
                List<MarketingSaleInfo> marketingSaleInfo = marketingSaleInfoService.queryQueryFilterList(queryFilters1);
                for (MarketingSaleInfo m:marketingSaleInfo){
                    income+=m.getPrepositionEarn();//前置赚取
                    profit+=m.getPureProfit();
                    receivable+=m.getGrossProfit();
                    expenditure+=(m.getGrossProfit()-m.getPureProfit());
                }
            }
            pie.setReceivable(receivable);//已收款
            pie.setExpenditure(expenditure);//支出
            pie.setProfit(profit);//利润
            pie.setIncome(income);//应收款
            pies.add(pie);
        }
        return ajaxReData("ListModel",pies);
    }
}
