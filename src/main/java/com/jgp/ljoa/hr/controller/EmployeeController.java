package com.jgp.ljoa.hr.controller;


import com.jgp.ljoa.hr.model.Employee;
import com.jgp.ljoa.hr.model.Relation;
import com.jgp.ljoa.hr.service.EmployeeService;
import com.jgp.ljoa.hr.service.RelationService;
import com.jgp.security.admin.model.AdminUser;
import com.jgp.security.admin.service.AdminUserService;
import com.jgp.sys.controller.JGPController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Objects;

/**
 * 项目   ljoa
 * 作者   liujinxu
 * 时间   2018/7/5
 */
@Controller
@RequestMapping("/ljoa/hr/employeeController")
public class EmployeeController extends JGPController {
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private AdminUserService adminUserService;
    @Autowired
    private RelationService relationService;

    //跳转条件查询离职员工的页面
    @RequestMapping("listGroupEmployee")
    private String listGroupEmployee(){
        return page("listGroupEmployee");
    }

    //跳转条件查询离职员工的页面
    @RequestMapping("listGroupEmployeeNO")
    private String listGroupEmployeeNO(){
        return page("listGroupEmployeeNO");
    }

    //根据账号查询员工
    @RequestMapping("showEmployee")
    private String showEmployee(Model model){
        Employee employee = employeeService.queryCurrentEmployee();
        if(Objects.nonNull(employee)){
            reForm(model, "fdata", employee);
        }
        return page("showEmployeeByAccount");
    }

    //跳转到编辑员工的页面
    @RequestMapping("editEmployee/{id}")
    private String editEmployee(@PathVariable String id, Model model){
        Employee employee = employeeService.queryOneEmployee(id);
//        AdminUser adminUser = adminUserService.queryUserByUserName(employee.getAccount());
//        Relation relation = relationService.queryRelationByAdminUserId(adminUser.getId());
//        model.addAttribute("orgId",relation.getMainUuid());
        reForm(model, "fdata", employee);
        return page("editEmployee");
    }

    //跳转到转职的页面
    @RequestMapping("editEmployeeType/{id}")
    private String editEmployeeType(@PathVariable String id, Model model){
        Employee employee = employeeService.queryOneEmployee(id);
        AdminUser adminUser = adminUserService.queryUserByUserName(employee.getAccount());
        Relation relation = relationService.queryRelationByAdminUserId(adminUser.getId());
        if(relation!=null&&relation.getMainUuid()!=null){
            model.addAttribute("orgId",relation.getMainUuid());
        }

        reForm(model, "fdata", employee);
        return page("editEmployeeType");
    }

    //跳转到添加员工的页面
    @RequestMapping("addEmployee")
    private String addEmployee(Employee employee){
        return page("addEmployee");
    }

    //跳转到员工修改密码的页面
    @RequestMapping("editEmployeePassword")
    private String editEmployeePassword(Model model){
        AdminUser currentAdmin = adminUserService.getCurrentAdmin();
        reForm(model, "fdata", currentAdmin);
        return page("editEmployeePassword");
    }

    //跳转到人事部门修改密码的页面
    @RequestMapping("editEmployeePasswordByRenShi/{userId}")
    private String editEmployeePasswordByRenShi(@PathVariable String userId,  Model model){
        Employee employee = employeeService.queryOneEmployee(userId);
        AdminUser currentAdmin = adminUserService.queryUserByUserName(employee.getAccount());
        reForm(model, "fdata", currentAdmin);
        return page("editEmployeePasswordByRenShi");
    }
}
