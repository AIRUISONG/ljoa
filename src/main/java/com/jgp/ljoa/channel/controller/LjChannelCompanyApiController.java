package com.jgp.ljoa.channel.controller;

import com.jgp.attachment.utils.DocUtil;
import com.jgp.common.annotation.UIParam;
import com.jgp.common.pojo.LabelValue;
import com.jgp.ljoa.channel.model.LjChannelCompany;
import com.jgp.ljoa.channel.service.LjChannelCompanyService;
import com.jgp.sys.controller.JGPController;
import com.jgp.sys.ui.GridResult;
import com.jgp.sys.ui.Pager;
import com.jgp.sys.ui.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by Administrator on 2018/8/14.
 */
/*
作者  SSF
时间   2018/8/14
*/
@RestController
@RequestMapping("/ljoa/channel/LjChannelCompanyApiController")
public class LjChannelCompanyApiController extends JGPController {
    @Autowired
    private LjChannelCompanyService ljChannelCompanyService;



    @RequestMapping("listAllLjChannelCompany")
    public Result listAllLjChannelCompany(){
        List<LjChannelCompany> ljChannelCompanies = ljChannelCompanyService.queryAllLjChannelCompany();
        List<LabelValue> labelValues = ljChannelCompanies.stream().map(emp -> {
            return new LabelValue(emp.getCompanyName(),emp.getId(),null);
        }).collect(Collectors.toList());
        return ajaxReData("ldata",labelValues);
    }

    @RequestMapping("listLjChannelCompany")
    public Result listLjChannelCompany(Model model, @UIParam("pager")Pager pager){
        List<LjChannelCompany> ljChannelCompanies = ljChannelCompanyService.queryAllLjChannelCompany();
        List<GridResult.Category> categories = new ArrayList<>();
        categories.add(new GridResult.Category("公司名称", "companyName"));
        categories.add(new GridResult.Category("营业执照号码", "licNo"));
        categories.add(new GridResult.Category("委托收款人姓名", "deputeName"));

        return ajaxReGrid("gdata",categories,ljChannelCompanies,pager);
    }

    @RequestMapping("saveLjChannelCompany")
    public Result saveLjChannelCompany(LjChannelCompany ljChannelCompany){
        LjChannelCompany ljChannelCompany1 = ljChannelCompanyService.saveLjChannelCompany(ljChannelCompany);
        return ajaxRe(true);
    }

    @RequestMapping("removeLjChannelCompany/{id}")
    public Result removeLjChannelCompany(@PathVariable String id){
        ljChannelCompanyService.removeLjChannelCompany(id);
        return ajaxRe(true);
    }

    @RequestMapping("importHouse")//批量导入第三方公司信息
    private Result importHouse(String fileId) throws Exception{
        List<String> fileIds = new ArrayList<String>();
        fileIds.add(fileId);
        List<File> fileList = DocUtil.getIOFiles(fileIds);
        for (File file : fileList) {
            ljChannelCompanyService.importData(file);
        }
        return ajaxRe(true);
    }
}
