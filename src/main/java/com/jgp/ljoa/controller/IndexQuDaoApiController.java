package com.jgp.ljoa.controller;

import com.jgp.ljoa.bean.ProjectIncomeAndExpenditure;
import com.jgp.ljoa.bean.TransactionsNum;
import com.jgp.ljoa.channel.model.LjHouseInfo;
import com.jgp.ljoa.channel.model.LjHouseSaleInfo;
import com.jgp.ljoa.channel.model.LjProjectInfo;
import com.jgp.ljoa.channel.service.LjHouseInfoService;
import com.jgp.ljoa.channel.service.LjHouseSaleInfoService;
import com.jgp.ljoa.channel.service.LjProjectInfoService;
import com.jgp.ljoa.expense.model.LjExpense;
import com.jgp.ljoa.expense.service.LjExpenseService;
import com.jgp.sys.controller.JGPController;
import com.jgp.sys.model.Attribute;
import com.jgp.sys.query.QueryFilterList;
import com.jgp.sys.service.AttributeService;
import com.jgp.sys.ui.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

/**
 * Created by Administrator on 2018/8/15 0015.
 */
@RestController
@RequestMapping("/ljoa/indexQuDaoApiController")
public class IndexQuDaoApiController extends JGPController {
    @Autowired
    private LjHouseInfoService ljHouseInfoService;
    @Autowired
    private LjHouseSaleInfoService ljHouseSaleInfoService;
    @Autowired
    private LjProjectInfoService ljProjectInfoService;
    @Autowired
    private AttributeService attributeService;
    @Autowired
    private LjExpenseService ljExpenseService;

    @RequestMapping("indexQuDao")
    private Result indexQuDao(){
        Map<String,Object> map = new HashMap<>();

        //查询已售、未结佣、渠道的房源，计算应收款
        LjHouseInfo ljHouseInfo = new LjHouseInfo();
        ljHouseInfo.setStatus("2");
//        ljHouseInfo.setCompanyChargeStatus("1");//未结佣
        ljHouseInfo.setHouseType("1");//渠道
        List<LjHouseInfo> ljHouseInfos = ljHouseInfoService.queryGroupHouseInfoToCount(ljHouseInfo);
        Float receivables = 0.0f;
        for(LjHouseInfo l:ljHouseInfos){
            if("2".equals(l.getCompanyChargeType())){//定额
                receivables+=l.getCompanyChargeMoney();
            }else if("1".equals(l.getCompanyChargeType())){//比例
                LjHouseSaleInfo ljHouseSaleInfo = ljHouseSaleInfoService.queryOneLjHouseSaleInfoBuUUid(l.getId());
                if(Objects.nonNull(ljHouseSaleInfo.getTotalPrice())){
                    receivables+=l.getCompanyChargeScale()*ljHouseSaleInfo.getTotalPrice()/100;
                }

            }

        }
        map.put("receivables",receivables);
        //查询已售、已结佣、渠道的房源，计算应收款
        LjHouseInfo HouseInfo = new LjHouseInfo();
        HouseInfo.setStatus("2");
        HouseInfo.setCompanyChargeStatus("2");//已结佣
        HouseInfo.setHouseType("1");//渠道
        List<LjHouseInfo> HouseInfos = ljHouseInfoService.queryGroupHouseInfoToCount(HouseInfo);
        Float receivables1 = 0.0f;
        for(LjHouseInfo l:HouseInfos){
            if("2".equals(l.getCompanyChargeType())){//定额
                receivables1+=l.getCompanyChargeMoney();
            }else if("1".equals(l.getCompanyChargeType())){//比例
                LjHouseSaleInfo ljHouseSaleInfo = ljHouseSaleInfoService.queryOneLjHouseSaleInfoBuUUid(l.getId());
                if(Objects.nonNull(ljHouseSaleInfo.getTotalPrice())){
                    receivables1+=l.getCompanyChargeScale()*ljHouseSaleInfo.getTotalPrice()/100;
                }

            }

        }
        map.put("receivables1",receivables1);

        //计算利润
        QueryFilterList queryFilters = new QueryFilterList();
        queryFilters.addFilter("chargeStatus","eq","10");//已结佣
        List<LjHouseSaleInfo> ljHouseSaleInfos = ljHouseSaleInfoService.queryQueryFilterList(queryFilters);
        Float profit = 0.0f;//利润
        for (LjHouseSaleInfo h:ljHouseSaleInfos){
            profit+=h.getCompanyProfit();
        }
        LjExpense ljExpense = new LjExpense();
        ljExpense.setPayStatus("1");//已打款
        ljExpense.setOrgUuid("3bc4148f06684cf6bebcf80bbbf2971c");//渠道整合
        List<LjExpense> ljExpenses = ljExpenseService.queryExpenseApproval(ljExpense, null);
        for(LjExpense l:ljExpenses){
            profit-=l.getMoney();
        }
        map.put("profit",profit);



        //房源成交数量
        //获得所有区
        List<Attribute> attributes = attributeService.queryAttributesByActiveKey("LJOA.CHANNEL.AREA");
        List<TransactionsNum> transactionsNums = new ArrayList<>();
        for (Attribute a: attributes){
            LjProjectInfo ljProjectInfo = new LjProjectInfo();
            ljProjectInfo.setProjectType("1");
            ljProjectInfo.setArea(a.getValue());
            Integer num = 0;
            //获得这个区的渠道项目
            List<LjProjectInfo> ljProjectInfos1 = ljProjectInfoService.queryGroupProject(ljProjectInfo);
            for (LjProjectInfo l:ljProjectInfos1){
                LjHouseInfo ljHouseInfo1 = new LjHouseInfo();
                ljHouseInfo1.setProjectUuid(l.getId());
                ljHouseInfo1.setHouseType("1");
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

        return ajaxReData("mapData",map);
    }

    @RequestMapping("indexQUDAOMoney")
    private Result indexQUDAOMoney(){
        //查找营销中各个项目的收支情况
        List<ProjectIncomeAndExpenditure> pies = new ArrayList<>();
        //查找渠道的所有项目
        List<LjProjectInfo> ljProjectInfos = ljProjectInfoService.queryGroupLjProjectInfoByProjectTypeQD();
        for (LjProjectInfo p:ljProjectInfos){
            Float income = 0.0f;//应收款
            Float receivable=0f;//已收款
            Float expenditure = 0.0f;//支出
            Float profits=0f;//利润

            ProjectIncomeAndExpenditure pie = new ProjectIncomeAndExpenditure();//首页数据转换实体
            pie.setId(p.getId());//项目主键
            pie.setProjectName(p.getProjectName());//项目名
            //查询对应房源
            LjHouseInfo ljHouseInfo1 = new LjHouseInfo();
            ljHouseInfo1.setProjectUuid(p.getId());
            ljHouseInfo1.setHouseType("1");//渠道
            List<LjHouseInfo> ljHouseInfos1 = ljHouseInfoService.queryHouseInfoSold(ljHouseInfo1,null);//对应项目的对应渠道房源
            for (LjHouseInfo h:ljHouseInfos1){
                if("2".equals(h.getStatus())){//已售房源
                    income+=h.getCompanyChargeMoney();//公司佣金金额(应收款)
                }
                if("2".equals(h.getStatus())&&"2".equals(h.getCompanyChargeStatus())){//已结佣房源
                    receivable+=h.getCompanyChargeMoney();//（已收款）
                }
            }

            for (LjHouseInfo h:ljHouseInfos1){
                QueryFilterList queryFilters1 = new QueryFilterList();
                queryFilters1.addFilter("houseUuid","eq",h.getId());
                queryFilters1.addFilter("chargeStatus","eq","10");//佣金状态已结佣的房源销售信息
                List<LjHouseSaleInfo> ljHouseSaleInfos1 = ljHouseSaleInfoService.queryQueryFilterList(queryFilters1);
                for (LjHouseSaleInfo hs:ljHouseSaleInfos1){
                    if(Objects.nonNull(hs.getCompanyProfit())){
                        profits+=hs.getCompanyProfit();//公司利润
                    }


                }
            }
            pie.setIncome(income);//应收款
            pie.setReceivable(receivable);//已收款
            pie.setProfit(profits);//利润
            pie.setExpenditure(receivable-profits);//支出
            pies.add(pie);
        }
        return ajaxReData("ListModel",pies);
    }
}
