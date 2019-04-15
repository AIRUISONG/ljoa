package com.jgp.ljoa.hr.controller;

import com.jgp.ljoa.hr.model.Organization;
import com.jgp.ljoa.hr.service.OrganizationService;
import com.jgp.sys.controller.JGPController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 项目   ljoa
 * 作者   liujinxu
 * 时间   2018/7/5
 */
@Controller
@RequestMapping("/ljoa/hr/organizationController")
public class OrganizationController extends JGPController {
    @Autowired
    private OrganizationService organizationService;

    //跳转到公司部门树的页面
    @RequestMapping("listOrganizationTree")
    private String listOrganizationTree(){
        return page("listOrganizationTree");
    }

    //跳转到添加公司部门页面
    @RequestMapping("addOrganization/{upUuid}")
    private String addOrganization(@PathVariable String upUuid, Model model){
        Organization org = new Organization();
        org.setParentId(upUuid);
        reForm(model, "fdata", org);
        return page("addOrganization");
    }

    //跳转到编辑公司部门页面
    @RequestMapping("editOrganization/{id}")
    private String editOrganization(@PathVariable String id, Model model){
        Organization org = organizationService.queryOneOrganization(id);
        reForm(model, "fdata", org);
        return page("editOrganization");
    }

    //跳转到移动部门的页面
    @RequestMapping("moveTo/{id}")
    private String moveTo(@PathVariable String id, Model model){
        model.addAttribute("id",id);
        return page("moveTo");
    }
}
