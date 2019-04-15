package com.jgp.ljoa.com.controller;


import com.jgp.sys.controller.JGPController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Administrator on 2018/9/25.
 */
/*
作者  SSF
时间   2018/9/25
*/
@Controller
@RequestMapping("/ljoa/com/LjHouseInfoZJLController")
public class LjHouseInfoZJLController extends JGPController {


    @RequestMapping("listGroupLjHouseInfo")//应收款数据链接页面
    public String listGroupLjHouseInfo(){

        return page("listGroupLjHouseInfo");
    }

    @RequestMapping("listGroupLjHouseInfoReturn")//回款数据链接页面
    public String listGroupLjHouseInfoReturn(){

        return page("listGroupLjHouseInfoReturn");
    }

    @RequestMapping("listGroupLjHouseInfoYSK")//应收款数据链接页面
    public String listGroupLjHouseInfoYSK(){

        return page("listGroupLjHouseInfoYSK");
    }

    @RequestMapping("listGroupReturnHouseInfo")//退款数据链接
    public String listGroupReturnHouseInfo(){

        return page("listGroupReturnHouseInfo");
    }


}
