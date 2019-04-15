package com.jgp.ljoa.hr.controller;

import com.jgp.attachment.utils.DocUtil;
import com.jgp.common.annotation.UIParam;
import com.jgp.common.pojo.LabelValue;
import com.jgp.ljoa.hr.model.Employee;
import com.jgp.ljoa.hr.service.EmployeeService;
import com.jgp.security.admin.model.AdminUser;
import com.jgp.security.admin.service.AdminUserService;
import com.jgp.sys.controller.JGPController;
import com.jgp.sys.ui.GridResult;
import com.jgp.sys.ui.Pager;
import com.jgp.sys.ui.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 项目   ljoa
 * 作者   liujinxu
 * 时间   2018/7/5
 */
@RestController
@RequestMapping("/ljoa/hr/employeeApiController")
public class EmployeeApiController extends JGPController {
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private AdminUserService adminUserService;


    /*
    * 条件查询员工列表
    * */
    @RequestMapping("listGroupEmployee")
    private Result listGroupEmployee(Employee employee, @UIParam("pager")Pager pager){
        employee.setEmployeeType("6");
        List<Employee> employees = employeeService.queryGroupEmployeeByEmployeeType(employee, pager);

        List<GridResult.Category> categories = new ArrayList<>();
        categories.add(new GridResult.Category("账号", "account"));
        categories.add(new GridResult.Category("工号", "workCode"));
        categories.add(new GridResult.Category("姓名", "personName"));
        categories.add(new GridResult.Category("身份证号", "identity"));
        categories.add(new GridResult.Category("联系方式", "linkTel"));
        categories.add(new GridResult.Category("性别", "sex","PERSON.SEX"));
        categories.add(new GridResult.Category("出生日期", "birthday"));
        categories.add(new GridResult.Category("地址", "address"));
        return ajaxReGrid("gdata", categories, employees, pager);
    }

    /*
    * 条件查询员工列表
    * */
    @RequestMapping("listGroupEmployeeNO")
    private Result listGroupEmployeeNO(Employee employee, @UIParam("pager")Pager pager){
        employee.setEmployeeType("6");
        List<Employee> employees = employeeService.queryGroupEmployee(employee, pager);
        //离职员工

        List<GridResult.Category> categories = new ArrayList<>();
        categories.add(new GridResult.Category("账号", "account"));
        categories.add(new GridResult.Category("工号", "workCode"));
        categories.add(new GridResult.Category("姓名", "personName"));
        categories.add(new GridResult.Category("身份证号", "identity"));
        categories.add(new GridResult.Category("联系方式", "linkTel"));
        categories.add(new GridResult.Category("性别", "sex","PERSON.SEX"));
        categories.add(new GridResult.Category("出生日期", "birthday"));
        categories.add(new GridResult.Category("地址", "address"));
        return ajaxReGrid("gdata", categories, employees, pager);
    }

    /*
    * 添加员工
    * */
    @RequestMapping("addEmployee")
    private Result addEmployee(Employee employee, String orgId){
        AdminUser adminUser = adminUserService.queryUserByUserName(employee.getAccount());
        if(Objects.isNull(adminUser)){
            employee.setEmployeeType("1");
            employeeService.saveEmployee(employee, orgId);
            return ajaxRe(true);
        }else{
            return ajaxRe(false);
        }
    }

    /*
    * 编辑员工
    * */
    @RequestMapping("editEmployee")
    private Result editEmployee(Employee employee){

        employeeService.saveEmployee(employee);
        return ajaxRe(true);
    }

    /*
    * 删除单个员工
    * */
    @RequestMapping("removeOneEmployee")
    private Result removeOneEmployee(String id){
        employeeService.removeOneEmployee(id);
        return ajaxRe(true);
    }

    /*
    * 批量删除员工
    * */
    @RequestMapping("removeSelectedEmployee")
    private Result removeSelectedEmployee(String[] array){
        employeeService.removeSelectedEmployee(array);
        return ajaxRe(true);
    }


    /*
    * 根据部门id查询该部门的所有员工
    * */
    @RequestMapping("listEmployeeByOrgId")
    private Result listEmployeeByOrgId(String orgId){
        List<LabelValue> labelValues = employeeService.queryEmployeeByOrgId(orgId);
        return ajaxReData("ldata",labelValues);
    }

    //员工做下拉
    @RequestMapping("listAllEmployee")
    private Result listAllEmployee(){

        List<Employee> employees = employeeService.queryAllEmployee();
        List<Employee> collect = employees.stream().filter(em -> {
            if (!"6".equals(em.getEmployeeType())&&em.getPersonName()!=null) {
                return true;
            } else {
                return false;
            }
        }).collect(Collectors.toList());

        List<LabelValue> labelValues = collect.stream().map(emp -> {
            AdminUser adminUser = adminUserService.queryUserByUserName(emp.getAccount());
            if(Objects.nonNull(adminUser)){
                return new LabelValue(emp.getPersonName(),adminUser.getId(),null);
            }else {
                return null;
            }
        }).filter(labelValue -> {
            if(Objects.nonNull(labelValue)){
                return true;
            }else {
                return false;
            }
        }).collect(Collectors.toList());
/*        for(LabelValue l:labelValues){
            System.out.println(l.getLabel()+":ID:"+l.getValue());
        }*/

        return ajaxReData("ldata",labelValues);
    }

    //员工修改密码
    @RequestMapping("editEmployeePassword")
    private Result editEmployeePassword(String userId, String pwd, String newPwd){
        AdminUser adminUser = adminUserService.queryUserById(userId);
        Boolean aBoolean = adminUserService.checkPwd(adminUser, pwd);
        if(aBoolean){
            adminUserService.resetPwd(adminUser, newPwd);
            return ajaxRe(true);
        }else {
            return ajaxRe(false);
        }

    }

    //人事部门修改密码
    @RequestMapping("editEmployeePasswordByRenShi")
    private Result editEmployeePasswordByRenShi(String userId, String newPwd){
        AdminUser adminUser = adminUserService.queryUserById(userId);
        try{
            adminUserService.resetPwd(adminUser, newPwd);
            return ajaxRe(true);
        }catch(Exception e) {
            return ajaxRe(false);
        }
    }

    @RequestMapping("importHouse")//批量导入
    private Result importHouse(String fileId) throws Exception {
        List<String> fileIds = new ArrayList<String>();
        fileIds.add(fileId);
        List<File> fileList = DocUtil.getIOFiles(fileIds);
        for (File file : fileList) {
           employeeService.importData(file);
        }
        return ajaxRe(true);
    }
}
