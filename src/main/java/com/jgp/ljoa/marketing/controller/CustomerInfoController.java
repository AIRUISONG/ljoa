package com.jgp.ljoa.marketing.controller;

import com.jgp.ljoa.channel.model.LjHouseInfo;
import com.jgp.ljoa.channel.service.LjHouseInfoService;
import com.jgp.ljoa.marketing.model.CustomerInfo;
import com.jgp.ljoa.marketing.service.CustomerInfoService;
import com.jgp.sys.controller.JGPController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Objects;

/**
 * Created by Administrator on 2018/7/4 0004.
 */
@Controller
@RequestMapping("/ljoa/marketing/customerInfoController")
public class CustomerInfoController extends JGPController {
    @Autowired
    private CustomerInfoService customerInfoService;
    @Autowired
    private LjHouseInfoService ljHouseInfoService;

    //跳转客户信息录入页面（未售房源）
    @RequestMapping("listHouseInfoByStatus")
    public String listHouseInfoByStatus(Model model){
        model.addAttribute("status","1");
        return page("listHouseInfoByStatusInfo");
    }
    //跳转客户信息录入页面（已售房源）
    @RequestMapping("listHouseInfoByStatusSold")
    public String listHouseInfoByStatusSold(Model model){
        model.addAttribute("status","2");
        return page("listHouseInfoByStatusInfo");
    }
    //跳转客户信息录入页面（前后置佣金）
    @RequestMapping("listHouseInfoByMoney")
    public String listHouseInfoByMoney(Model model){
        model.addAttribute("status","2");
        return page("listHouseInfoByMoney");
    }
    //跳转填写客户信息页面
    @RequestMapping("addCustomerInfo/{hid}")
    public String addCustomerInfo(@PathVariable String hid , Model model){
        CustomerInfo customerInfo = customerInfoService.queryCustomerInfoByHouseUuid(hid);
        LjHouseInfo ljHouseInfo = ljHouseInfoService.queryOneHouseInfo(hid);
        if (!Objects.nonNull(customerInfo)){
            customerInfo= new CustomerInfo();
            customerInfo.setHouseUuid(hid);
        }
        model.addAttribute("hid",hid);
        model.addAttribute("status",ljHouseInfo.getStatus());
        reForm(model,"fdata",customerInfo);
        return page("addCustomerInfo");
    }
    //跳转未网签房源
//    @RequestMapping("queryHouseInfoByCustomerInfoSignStatus")
//    public String queryHouseInfoByCustomerInfoSignStatus(Model model){
//        return page("queryHouseInfoByCustomerInfoSignStatus");
//    }
    //延期处理
//    @RequestMapping("addorEditDeferredHandling/{hid}")
//    public String addorEditDeferredHandling(@PathVariable String hid, Model model){
//        CustomerInfo customerInfo = customerInfoService.queryCustomerInfoByHouseUuid(hid);
//        if ("".equals(customerInfo.getHouseUuid())){
//            customerInfo.setHouseUuid(hid);
//        }
//        re(model,"fdata",customerInfo);
//        return page("addorEditDeferredHandling");
//    }
    //办理网签页面
//    @RequestMapping("addorEditNetSigning/{hid}")
//    public String addorEditNetSigning(@PathVariable String hid, Model model){
//        CustomerInfo customerInfo = customerInfoService.queryCustomerInfoByHouseUuid(hid);
//        if ("".equals(customerInfo.getHouseUuid())){
//            customerInfo.setHouseUuid(hid);
//        }
//        re(model,"fdata",customerInfo);
//        return page("addorEditNetSigning");
//    }

    //跳转办理退定金客户
    @RequestMapping("listCustomerInfoBySignStatus")
    public String listCustomerInfoBySignStatus(Model model){
        return page("listCustomerInfoBySignStatus");
    }

    //退定金处理
    @RequestMapping("addorEditCustomerInfoBySignStatus/{id}")
    public String addorEditCustomerInfoBySignStatus(@PathVariable String id, Model model){
        CustomerInfo customerInfo = customerInfoService.queryCustomerInfo(id);
        reForm(model,"fdata",customerInfo);
        return page("addorEditCustomerInfoBySignStatus");
    }

    //跳转填写客户信息页面
    @RequestMapping("showCustomerInfo/{hid}")
    public String showCustomerInfo(@PathVariable String hid, Model model){
        CustomerInfo customerInfo = customerInfoService.queryCustomerInfoByHouseUuid(hid);
        reForm(model,"fdata",customerInfo);
        return page("showCustomerInfo");
    }
}
