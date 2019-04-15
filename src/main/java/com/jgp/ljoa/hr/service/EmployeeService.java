package com.jgp.ljoa.hr.service;

import com.jgp.common.pojo.LabelValue;
import com.jgp.ljoa.hr.model.Employee;
import com.jgp.sys.ui.Pager;

import java.io.File;
import java.util.List;

/**
 * 项目   ljoa
 * 作者   liujinxu
 * 时间   2018/7/5
 */
public interface EmployeeService {



    /*
    * 条件查询员工信息
    * */
    List<Employee> queryGroupEmployee(Employee employee, Pager pager);
    List<Employee> queryGroupEmployeeByEmployeeType(Employee employee, Pager pager);

    /*
    * 查询单个员工
    * */
    Employee queryOneEmployee(String id);

    /*
    * 根据账号查询员工
    * */
    Employee queryOneEmployeeByAccount(String account);

    /*
    * 保存员工信息
    * */
    Employee saveEmployee(Employee employee, String orgId);

    /*
    * 保存员工信息
    * */
    Employee saveEmployee(Employee employee);

    /*
    * 删除单个员工
    * */
    void removeOneEmployee(String id);

    /*
    * 批量删除员工
    * */
    void removeSelectedEmployee(String[] array);
    /*
   * 全查
   * */
    List<Employee> queryAllEmployee();

    /*
    * 根据部门id查询该部门的所有员工，并组装成labelValue
    * */
    List<LabelValue> queryEmployeeByOrgId(String orgId);

    /*
    * 查询当前登陆用户的信息
    * */
    Employee queryCurrentEmployee();

    /*
    * 批量导入员工信息
    * */
    void importData(File file) throws Exception;

}
