package com.jgp.ljoa.marketing.controller;

import com.jgp.ljoa.channel.model.LjHouseInfo;
import com.jgp.ljoa.channel.model.LjProjectInfo;
import com.jgp.ljoa.channel.service.LjHouseInfoService;
import com.jgp.ljoa.channel.service.LjProjectInfoService;
import com.jgp.ljoa.hr.model.Employee;
import com.jgp.ljoa.hr.model.Relation;
import com.jgp.ljoa.hr.service.EmployeeService;
import com.jgp.ljoa.hr.service.RelationService;
import com.jgp.ljoa.marketing.model.MarketingChargeInfo;
import com.jgp.ljoa.marketing.model.MarketingSaleInfo;
import com.jgp.ljoa.marketing.service.MarketingChargeInfoService;
import com.jgp.ljoa.marketing.service.MarketingSaleInfoService;
import com.jgp.security.admin.model.AdminUser;
import com.jgp.security.admin.service.AdminUserService;
import com.jgp.sys.controller.JGPController;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Objects;

/**
 * Created by Administrator on 2018/7/10 0010.
 */
@Controller
@RequestMapping("/ljoa/marketing/marketingChargeInfoController")
public class MarketingChargeInfoController extends JGPController {
    @Autowired
    private MarketingChargeInfoService marketingChargeInfoService;
    @Autowired
    private MarketingSaleInfoService marketingSaleInfoService;
    @Autowired
    private LjHouseInfoService ljHouseInfoService;
    @Autowired
    private LjProjectInfoService ljProjectInfoService;
    @Autowired
    private RelationService relationService;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private AdminUserService adminUserService;


    @RequestMapping("listMarketingChargeInfoCounselorMan/{hid}")
    public String listMarketingChargeInfoCounselorMan(@PathVariable String hid, Model model){
//        List<String> list = new ArrayList<>();
//        list.add("5");
//        List<MarketingChargeInfo> marketingChargeInfos = marketingChargeInfoService.queryGroupMarketingChargeInfo(hid, list, null);
//        List<MarketingChargeInfo> marketingChargeInfos1 = marketingChargeInfoService.queryGroupMarketingChargeInfo(hid, null, null);
//        if(marketingChargeInfos1.size()>0){//有数据
//            if(marketingChargeInfos.size()>0){//有渠道信息
//                model.addAttribute("way","0");//渠道
//            }else{
//                model.addAttribute("way","1");//置业顾问
//            }
//        }else{
//            model.addAttribute("way","2");//都没有填写
//        }
        MarketingSaleInfo marketingSaleInfo = marketingSaleInfoService.queryMarketingSaleInfoByUuid(hid);//根据房源id查找销售信息
        model.addAttribute("insideChargeStatus",marketingSaleInfo.getInsideChargeStatus());//用来判断是否结佣
        model.addAttribute("hid",hid);
        return page("listMarketingChargeInfoCounselorMan");
    }
    //添加置业顾问佣金
    @RequestMapping("addMarketingChargeInfoCounselorMen/{hid}")
    public String addMarketingChargeInfoCounselorMen(@PathVariable String hid, Model model){
        LjHouseInfo ljHouseInfo = ljHouseInfoService.queryOneLjHouseInfoByid(hid);
        List<Relation> relations = relationService.queryGroupRelation(ljHouseInfo.getProjectUuid(), "4", null);
        Relation relation=new Relation();
        if(relations.size() > 0){
            relation = relations.get(0);
        }
        String adminId = "";
        if(relation != null){
            Employee employee = employeeService.queryOneEmployee(relation.getSubUuid());
            if(employee != null){
                AdminUser adminUser = adminUserService.queryUserByUserName(employee.getAccount());
                if(Objects.nonNull(adminUser) && StringUtils.isNotEmpty(adminUser.getId())){
                    adminId = adminUser.getId();
                }

            }

        }
        MarketingChargeInfo marketingChargeInfo = new MarketingChargeInfo();
        marketingChargeInfo.setHouseUuid(hid);
        marketingChargeInfo.setFrontHouse(90f);
        marketingChargeInfo.setAfterHouse(10f);
        marketingChargeInfo.setChargeType("1");//置业顾问
        marketingChargeInfo.setChargeStatus("0");//未上报
        if(StringUtils.isNotEmpty(adminId)){
            marketingChargeInfo.setChargeTargetUuid(adminId);
        }
        reForm(model,"fdata",marketingChargeInfo);
        return page("addMarketingChargeInfoCounselorMen");
    }
    //添加渠道佣金addMarketingChargeInfoChannel
    @RequestMapping("addMarketingChargeInfoChannel/{hid}")
    public String addMarketingChargeInfoChannel(@PathVariable String hid, Model model){
        MarketingChargeInfo marketingChargeInfo = new MarketingChargeInfo();
        marketingChargeInfo.setHouseUuid(hid);
        marketingChargeInfo.setChargeType("5");//置业顾问
        marketingChargeInfo.setChargeStatus("0");//未上报
        reForm(model,"fdata",marketingChargeInfo);
        return page("addMarketingChargeInfoChannel");
    }
    //修改
    @RequestMapping("editMarketingChargeInfo/{id}")
    public String editMarketingChargeInfo(@PathVariable String id, Model model){
        MarketingChargeInfo marketingChargeInfo = marketingChargeInfoService.queryOneMarketingChargeInfo(id);
        model.addAttribute("chargeType",marketingChargeInfo.getChargeType());
        reForm(model,"fdata",marketingChargeInfo);
        return page("editMarketingChargeInfo");
    }

    //经理填写佣金跳转页面
    @RequestMapping("listMarketingChargeInfoManager/{hid}")
    public String listMarketingChargeInfoManager(@PathVariable String hid, Model model){
//        List<String> list = new ArrayList<>();
//        list.add("5");
//        List<MarketingChargeInfo> marketingChargeInfos = marketingChargeInfoService.queryGroupMarketingChargeInfo(hid, list, null);
//        List<MarketingChargeInfo> marketingChargeInfos1 = marketingChargeInfoService.queryGroupMarketingChargeInfo(hid, null, null);
//        if(marketingChargeInfos1.size()>0){//有数据
//            if(marketingChargeInfos.size()>0){//有渠道信息
//                model.addAttribute("way","0");//渠道
//            }else{
//                model.addAttribute("way","1");//置业顾问
//            }
//        }else{
//            model.addAttribute("way","2");//都没有填写
//        }
        MarketingSaleInfo marketingSaleInfo = marketingSaleInfoService.queryMarketingSaleInfoByUuid(hid);//根据房源id查找销售信息
        model.addAttribute("insideChargeStatus",marketingSaleInfo.getInsideChargeStatus());
        model.addAttribute("hid",hid);
        return page("listMarketingChargeInfoManager");
    }
    //添加主管佣金
    @RequestMapping("addMarketingChargeInfoManager/{hid}")
    public String addMarketingChargeInfoManager(@PathVariable String hid, Model model){
        MarketingChargeInfo marketingChargeInfo = new MarketingChargeInfo();
        marketingChargeInfo.setHouseUuid(hid);
        marketingChargeInfo.setChargeType("2");//主管
        marketingChargeInfo.setChargeStatus("0");//未上报
        reForm(model,"fdata",marketingChargeInfo);
        return page("addMarketingChargeInfoDirector");
    }

    //销售总监填写佣金跳转页面
    @RequestMapping("listMarketingChargeInfo/{hid}")
    public String listMarketingChargeInfo(@PathVariable String hid, Model model){
//        List<String> list = new ArrayList<>();
//        list.add("5");
//        List<MarketingChargeInfo> marketingChargeInfos = marketingChargeInfoService.queryGroupMarketingChargeInfo(hid, list, null);
//        List<MarketingChargeInfo> marketingChargeInfos1 = marketingChargeInfoService.queryGroupMarketingChargeInfo(hid, null, null);
//        if(marketingChargeInfos1.size()>0){//有数据
//            if(marketingChargeInfos.size()>0){//有渠道信息
//                model.addAttribute("way","0");//渠道
//            }else{
//                model.addAttribute("way","1");//置业顾问
//            }
//        }else{
//            model.addAttribute("way","2");//都没有填写
//        }
        MarketingSaleInfo marketingSaleInfo = marketingSaleInfoService.queryMarketingSaleInfoByUuid(hid);//根据房源id查找销售信息
        model.addAttribute("insideChargeStatus",marketingSaleInfo.getInsideChargeStatus());
        model.addAttribute("hid",hid);
        return page("listMarketingChargeInfo");
    }

    //添加销售经理佣金
    @RequestMapping("addMarketingChargeInfoSaleManager/{hid}")
    public String addMarketingChargeInfoSaleManager(@PathVariable String hid, Model model){
        LjHouseInfo ljHouseInfo = ljHouseInfoService.queryOneLjHouseInfoByid(hid);
        LjProjectInfo ljProjectInfo = new LjProjectInfo();
        if(ljHouseInfo != null){
            ljProjectInfo  = ljProjectInfoService.queryOneLjProjectInfoById(ljHouseInfo.getProjectUuid());
        }

        MarketingChargeInfo marketingChargeInfo = new MarketingChargeInfo();
        marketingChargeInfo.setHouseUuid(hid);
        marketingChargeInfo.setChargeStatus("0");//未上报
        marketingChargeInfo.setChargeType("3");//销售经理
        if(ljProjectInfo != null){
            marketingChargeInfo.setChargeTargetUuid(ljProjectInfo.getManagerUuid());
        }
        reForm(model,"fdata",marketingChargeInfo);
        return page("addMarketingChargeInfoSaleManager");
    }
    //添加销售总监佣金
    @RequestMapping("addMarketingChargeInfoSaleDirector/{hid}")
    public String addMarketingChargeInfo(@PathVariable String hid, Model model){
        LjHouseInfo ljHouseInfo = ljHouseInfoService.queryOneLjHouseInfoByid(hid);
        LjProjectInfo ljProjectInfo = new LjProjectInfo();
        if(ljHouseInfo != null){
            ljProjectInfo  = ljProjectInfoService.queryOneLjProjectInfoById(ljHouseInfo.getProjectUuid());
        }
        MarketingChargeInfo marketingChargeInfo = new MarketingChargeInfo();
        marketingChargeInfo.setHouseUuid(hid);
        marketingChargeInfo.setChargeStatus("0");//未上报
        marketingChargeInfo.setChargeType("4");//销售总监
        if(ljProjectInfo != null){
            marketingChargeInfo.setChargeTargetUuid(ljProjectInfo.getChiefUuid());
        }
        reForm(model,"fdata",marketingChargeInfo);
        return page("addMarketingChargeInfoSaleDirector");
    }

}
