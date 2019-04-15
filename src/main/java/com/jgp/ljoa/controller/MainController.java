package com.jgp.ljoa.controller;

import com.jgp.ljoa.hr.model.Employee;
import com.jgp.ljoa.hr.service.EmployeeService;
import com.jgp.security.admin.model.AdminRole;
import com.jgp.security.admin.model.AdminUser;
import com.jgp.security.admin.service.AdminRoleService;
import com.jgp.security.admin.service.AdminRoleUserService;
import com.jgp.security.admin.service.AdminUserService;
import com.jgp.sys.controller.JGPController;
import com.jgp.sys.service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.Objects;

/**
 * 项目   ljoa
 * 作者   loufei
 * 时间   2018/6/28
 */
@Controller
@RequestMapping("/sys")
public class MainController extends JGPController {
    @Autowired
    private ConfigService configService;
    @Autowired
    private AdminUserService adminUserService;
    @Autowired
    private AdminRoleService adminRoleService;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private AdminRoleUserService adminRoleUserService;


    @RequestMapping("index")
    public String index(Model model){

        List<AdminRole> adminRoles = adminRoleUserService.queryRoles(adminUserService.getCurrentAdmin().getId());
        String name ="";
        if(adminRoles.size()==0){
            name="0";
        }else{
            name= adminRoles.get(0).getName();
        }

        model.addAttribute("globalConfig", configService.globalConfig());
        AdminUser currentAdmin = adminUserService.getCurrentAdmin();
        Employee employee = employeeService.queryOneEmployeeByAccount(currentAdmin.getUsername());
        if(Objects.isNull(employee)){
            model.addAttribute("personName","邻家员工");
        }else{
            model.addAttribute("personName",employee.getPersonName());
        }
        if("销售总监".equals(name)){
            return "indexyingxiao";
        }else if("渠道总监".equals(name)){
            return "indexqudao";
        }else if("总经理".equals(name)||"副总经理".equals(name)){
            return "indexAdmin";
        }else{
            return "index";
        }
    }
}
