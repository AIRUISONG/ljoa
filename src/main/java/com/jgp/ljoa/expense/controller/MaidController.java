package com.jgp.ljoa.expense.controller;/**
 * Created by Administrator on 2018/7/8.
 */

import com.jgp.ljoa.channel.model.LjChannelCompany;
import com.jgp.ljoa.channel.model.LjHouseSaleInfo;
import com.jgp.ljoa.channel.service.LjChannelCompanyService;
import com.jgp.ljoa.channel.service.LjHouseInfoService;
import com.jgp.ljoa.channel.service.LjHouseSaleInfoService;
import com.jgp.ljoa.marketing.model.MarketingChargeInfo;
import com.jgp.ljoa.marketing.model.MarketingSaleInfo;
import com.jgp.ljoa.marketing.service.MarketingChargeInfoService;
import com.jgp.ljoa.marketing.service.MarketingSaleInfoService;
import com.jgp.sys.controller.JGPController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDate;

/**
 * 项目   ljoa
 * 作者   liujinxu
 * 时间   2018/7/8
 */
@Controller
@RequestMapping("/ljoa/expense/maidController")
public class MaidController extends JGPController {
    @Autowired
    private LjHouseInfoService ljHouseInfoService;
    @Autowired
    private LjHouseSaleInfoService ljHouseSaleInfoService;
    @Autowired
    private MarketingSaleInfoService marketingSaleInfoService;
    @Autowired
    private MarketingChargeInfoService marketingChargeInfoService;
    @Autowired
    private LjChannelCompanyService ljChannelCompanyService;

    //跳转到财务查询可结佣的房源信息
    @RequestMapping("listExpenseQueryMaidHouseInfo")
    private String listExpenseQueryMaidHouseInfo(){
        return page("listExpenseQueryMaidHouseInfo");
    }

    //跳转到查询房源销售信息的页面
    @RequestMapping("showExpenseMaidHouseSaleInfo/{houseId}/{houseType}")
    private String showExpenseMaidHouseSaleInfo(@PathVariable String houseId, @PathVariable String houseType, Model model){
        if("1".equals(houseType)){
            LjHouseSaleInfo ljHouseSaleInfo = ljHouseSaleInfoService.queryOneLjHouseSaleInfoBuUUid(houseId);
            if("8".equals(ljHouseSaleInfo.getChargeStatus())){
                model.addAttribute("flag","1");//可修改
            }else if("10".equals(ljHouseSaleInfo.getChargeStatus())){
                model.addAttribute("flag","0");//不可修改
            }
            model.addAttribute("uuid",ljHouseSaleInfo.getId());
            reForm(model, "fdata", ljHouseSaleInfo);
            return page("showExpenseMaidChannelHouseSaleInfo");
        }else {
            MarketingSaleInfo marketingSaleInfo = marketingSaleInfoService.queryMarketingSaleInfoByHouseUuid(houseId).get(0);
            if("8".equals(marketingSaleInfo.getChanneChargeStatus())
                    && "8".equals(marketingSaleInfo.getInsideChargeStatus())){
                model.addAttribute("flag","1");//可修改
            }else if("10".equals(marketingSaleInfo.getChanneChargeStatus())
                    && "10".equals(marketingSaleInfo.getInsideChargeStatus())){
                model.addAttribute("flag","0");//不可修改
            }
            reForm(model, "fdata", marketingSaleInfo);
            return page("showExpenseMaidMarketingHouseSaleInfo");
        }
        //ljHouseSaleInfo.setChargeStatus("10");//已结佣
    }

    /*
    * 个人结佣页面
    * */
    @RequestMapping("showMaidMarketingChargeInfo/{chargeInfoId}")
    public String showMaidMarketingChargeInfo(@PathVariable String chargeInfoId, Model model){
        MarketingChargeInfo marketingChargeInfo = marketingChargeInfoService.queryOneMarketingChargeInfo(chargeInfoId);
        marketingChargeInfo.setChargeSendDate(LocalDate.now());
        marketingChargeInfo.setChargeStatus("12");
        reForm(model, "fdata", marketingChargeInfo);
        return page("showMaidMarketingChargeInfo");
    }

    //跳转渠道第三方公司结佣页
    @RequestMapping("showHouseSaleInfoByChannelCompany/{houseId}")
    public String showHouseSaleInfoByChannelCompany(@PathVariable String houseId, Model model){
        LjHouseSaleInfo ljHouseSaleInfo = ljHouseSaleInfoService.queryOneLjHouseSaleInfo(houseId);
        LjChannelCompany ljChannelCompany = new LjChannelCompany();
        if(ljHouseSaleInfo.getChannelCompany() !=null){
            ljChannelCompany = ljChannelCompanyService.queryOneLjChannelCompany(ljHouseSaleInfo.getChannelCompany());
        }

        if("8".equals(ljHouseSaleInfo.getChargeStatus())){
            model.addAttribute("flag","1");//可修改
        }else if("10".equals(ljHouseSaleInfo.getChargeStatus())){
            model.addAttribute("flag","0");//不可修改
        }
        reForm(model, "fdata", ljHouseSaleInfo);
        reForm(model, "companyData", ljChannelCompany);
        return page("showHouseSaleInfoByChannelCompany");
    }

    //跳转渠道总监结佣页
    @RequestMapping("showHouseSaleInfoByChannelChief/{houseId}")
    public String showHouseSaleInfoByChannelChief(@PathVariable String houseId, Model model){
        LjHouseSaleInfo ljHouseSaleInfo = ljHouseSaleInfoService.queryOneLjHouseSaleInfo(houseId);
        if("8".equals(ljHouseSaleInfo.getChargeStatus())){
            model.addAttribute("flag","1");//可修改
        }else if("10".equals(ljHouseSaleInfo.getChargeStatus())){
            model.addAttribute("flag","0");//不可修改
        }
        reForm(model, "fdata", ljHouseSaleInfo);
        return page("showHouseSaleInfoByChannelChief");
    }

    //跳转渠道总监结佣页
    @RequestMapping("showHouseSaleInfoByAreaManage/{houseId}")
    public String showHouseSaleInfoByAreaManage(@PathVariable String houseId, Model model){
        LjHouseSaleInfo ljHouseSaleInfo = ljHouseSaleInfoService.queryOneLjHouseSaleInfo(houseId);
        if("8".equals(ljHouseSaleInfo.getChargeStatus())){
            model.addAttribute("flag","1");//可修改
        }else if("10".equals(ljHouseSaleInfo.getChargeStatus())){
            model.addAttribute("flag","0");//不可修改
        }
        reForm(model, "fdata", ljHouseSaleInfo);
        return page("showHouseSaleInfoByAreaManage");
    }
}
