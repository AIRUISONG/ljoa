package com.jgp.ljoa.channel.controller;

import com.jgp.ljoa.channel.model.LjChannelCompany;
import com.jgp.ljoa.channel.service.LjChannelCompanyService;
import com.jgp.sys.controller.JGPController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Administrator on 2018/8/14.
 */
/*
作者  SSF
时间   2018/8/14
*/
@Controller
@RequestMapping("/ljoa/channel/LjChannelCompanyController")
public class LjChannelCompanyController extends JGPController {
    @Autowired
    private LjChannelCompanyService ljChannelCompanyService;

    @RequestMapping("listAllLjChannelCompany")
    public String listAllLjChannelCompany(){

        return page("listAllLjChannelCompany");
    }
    @RequestMapping("addLjChannelCompany")
    public String addLjChannelCompany(Model model){
        LjChannelCompany ljChannelCompanyj=new LjChannelCompany();

        reForm(model,"fdata",ljChannelCompanyj);
        return page("addLjChannelCompany");
    }

    @RequestMapping("showLjChannelCompany/{id}")
    public String showLjChannelCompany(@PathVariable String id,Model model){
        LjChannelCompany ljChannelCompany = ljChannelCompanyService.queryOneLjChannelCompany(id);
        reForm(model,"fdata",ljChannelCompany);
        return page("showLjChannelCompany");
    }

    @RequestMapping("editLjChannelCompany/{id}")
    public String editLjChannelCompany(@PathVariable String id,Model model){
        LjChannelCompany ljChannelCompany = ljChannelCompanyService.queryOneLjChannelCompany(id);
        reForm(model,"fdata",ljChannelCompany);
        return page("editLjChannelCompany");
    }
}
