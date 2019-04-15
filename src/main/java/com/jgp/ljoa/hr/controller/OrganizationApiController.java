package com.jgp.ljoa.hr.controller;/**
 * Created by Administrator on 2018/7/4.
 */

import com.jgp.common.pojo.LabelValue;
import com.jgp.common.pojo.TreeBean;
import com.jgp.ljoa.hr.model.Organization;
import com.jgp.ljoa.hr.service.OrganizationService;
import com.jgp.security.admin.model.AdminRole;
import com.jgp.security.admin.service.AdminRoleService;
import com.jgp.sys.controller.JGPController;
import com.jgp.sys.service.MenuService;
import com.jgp.sys.ui.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 项目   ljoa
 * 作者   liujinxu
 * 时间   2018/7/5
 */
@RestController
@RequestMapping("/ljoa/hr/organizationApiController")
public class OrganizationApiController extends JGPController {
    @Autowired
    private OrganizationService organizationService;
    @Autowired
    private MenuService menuService;
    @Autowired
    private AdminRoleService adminRoleService;

    /*
    * 查询组织机构树
    * */
    @RequestMapping("listTreeOrganization")
    private List<TreeBean> treeMenu() {
        return this.organizationService.queryOrganizationTree("-1", false);
    }

    /*
    * 添加部门
    * */
    @RequestMapping("addOrganization")
    private Result addOrganization(Organization org){
        organizationService.saveOrganization(org);
        return ajaxRe(true);
    }

    /*
    * 编辑部门信息
    * */
    @RequestMapping("editOrganization")
    private Result editOrganization(Organization org){
        organizationService.saveOrganization(org);
        return ajaxRe(true);
    }

    /*
    * 删除单个部门
    * */
    @RequestMapping("removeOneOrganization")
    private Result removeOneOrganization(String id){
        organizationService.removeOneOrganization(id);
        return ajaxRe(true);
    }

    /*
    * 移动部门
    * */
    @RequestMapping("moveTo")
    private Result moveTo(String id, String toId){
        organizationService.moveTo(id, toId);
        return ajaxRe(true);
    }

    /*
     * 查询公司所有部门
     * */
    @RequestMapping("listAllOrg")
    private Result listAllOrg(Model model){
        List<Organization> organizations = organizationService.queryAllOrg();
        List<LabelValue> collect = organizations.stream().map(ljOrganization -> {
            return new LabelValue(ljOrganization.getOrgName(), ljOrganization.getId(), null);
        }).collect(Collectors.toList());
        return ajaxReData("ldata",collect);
    }

    /*
    * 查询所有角色
    * */
    @RequestMapping("listAllRole")
    private Result listAllRole(){
        List<AdminRole> adminRoles = adminRoleService.queryRoles(new AdminRole(), null);
        List<LabelValue> collect = adminRoles.stream().filter(adminRole -> {
            //非管理员的角色
            if ("40eccc3deeed44ff806308d9978287e6".equals(adminRole.getId())) {
                return false;
            } else {
                return true;
            }
        }).sorted(new Comparator<AdminRole>() {
            @Override
            public int compare(AdminRole o1, AdminRole o2) {
                if(Integer.valueOf(o1.getSort()) > Integer.valueOf(o2.getSort())){
                    return 1;
                }else if(Integer.valueOf(o1.getSort()) < Integer.valueOf(o2.getSort())){
                    return -1;
                }else {
                    return 0;
                }
            }
        }).map(adminRole -> {
            return new LabelValue(adminRole.getName(), adminRole.getId(), null);
        }).collect(Collectors.toList());
        return ajaxReData("ldata",collect);
    }
}
