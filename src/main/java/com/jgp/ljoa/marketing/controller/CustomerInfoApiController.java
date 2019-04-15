package com.jgp.ljoa.marketing.controller;

import com.jgp.common.annotation.UIParam;
import com.jgp.ljoa.channel.model.LjHouseInfo;
import com.jgp.ljoa.channel.model.LjProjectInfo;
import com.jgp.ljoa.channel.service.LjHouseInfoService;
import com.jgp.ljoa.hr.service.EmployeeService;
import com.jgp.ljoa.marketing.model.CustomerInfo;
import com.jgp.ljoa.marketing.service.CustomerInfoService;
import com.jgp.security.admin.service.AdminRoleService;
import com.jgp.security.admin.service.AdminUserService;
import com.jgp.sys.controller.JGPController;
import com.jgp.sys.ui.GridResult;
import com.jgp.sys.ui.Pager;
import com.jgp.sys.ui.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/7/4 0004.
 */
@RestController
@RequestMapping("/ljoa/marketing/customerInfoApiController")
public class CustomerInfoApiController extends JGPController {
    @Autowired
    private CustomerInfoService customerInfoService;
    @Autowired
    private LjHouseInfoService ljHouseInfoService;
    @Autowired
    private AdminUserService adminUserService;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private  AdminRoleService adminRoleService;
    //查询未售、营销房源(客户信息录入菜单)
    @RequestMapping("listHouseInfoByStatus/{status}/{id}")
    public Result listHouseInfoByStatus(@UIParam("pager") Pager pager, LjHouseInfo ljHouseInfo, @PathVariable String status, @PathVariable String id) {
        ljHouseInfo.setStatus(status);
        ljHouseInfo.setProjectUuid(id);
        List<LjHouseInfo> ljHouseInfoList = ljHouseInfoService.queryHouseInfoByStatus(ljHouseInfo,pager);
        List<GridResult.Category> categories = new ArrayList<>();
        categories.add(new GridResult.Category("项目名称", "projectUuid",LjProjectInfo.class,"projectName"));
        categories.add(new GridResult.Category("楼号", "buildingNo"));
        categories.add(new GridResult.Category("单元", "unitNo"));
        categories.add(new GridResult.Category("房号", "roomNo"));
        categories.add(new GridResult.Category("面积(㎡)", "roomArea"));
        categories.add(new GridResult.Category("产品分类", "roomType","LJOA.CHANNEL.ROOM_TYPE"));
        categories.add(new GridResult.Category("其他分类", "otherRoomType"));
        categories.add(new GridResult.Category("销售状态", "status","LJOA.CHANNEL.ROOM_STATUS"));
        return ajaxReGrid("gdata", categories, ljHouseInfoList, pager);
    }

    //保存客户信息
    @RequestMapping("saveCustomerInfo/{hid}")
    public Result saveCustomerInfo(@PathVariable String hid, CustomerInfo customerInfo){
        customerInfo.setHouseUuid(hid);
        customerInfo= customerInfoService.saveCustomerInfo(customerInfo);//保存客户信息
        if(customerInfo.getTransactionPrice()!=null){
            LjHouseInfo ljHouseInfo = ljHouseInfoService.queryOneHouseInfo(customerInfo.getHouseUuid());//查询此房源
            ljHouseInfo.setStatus("2");//将此房源改为已售
            ljHouseInfo.setSaleMoney(customerInfo.getTransactionPrice());
            ljHouseInfo.setPremium(customerInfo.getTransactionPrice()-ljHouseInfo.getMinMoney());//成交价格-底价（溢价金额）
            if(ljHouseInfo != null  && ljHouseInfo.getCompanyChargeType()!=null){
                if("1".equals(ljHouseInfo.getCompanyChargeType()) || ljHouseInfo.getPostCommission() < 10 ){ //公司佣金方式（比例）
                    ljHouseInfo.setPostCommission(ljHouseInfo.getCompanyChargeScale());//后置佣金（%）
                    Float moneys=0f;
                    if(ljHouseInfo.getPreCommission() != 0 || ljHouseInfo.getPreCommission()!=null){
                        moneys+=ljHouseInfo.getPreCommission();//前置
                    }
                    if(ljHouseInfo.getPostCommission() != 0 || ljHouseInfo.getPostCommission() != null){
                        moneys+=ljHouseInfo.getPostCommission()*customerInfo.getTransactionPrice();//后置
                    }
                    if(ljHouseInfo.getOtherCommission() != 0 || ljHouseInfo.getOtherCommission() != null ){
                        moneys+=ljHouseInfo.getOtherCommission();//其他
                    }
                    ljHouseInfo.setCompanyChargeMoney(moneys);
                    //公司佣金金额
                }else if("2".equals(ljHouseInfo.getCompanyChargeType()) || ljHouseInfo.getPostCommission() > 10){
                    ljHouseInfo.setPostCommission(ljHouseInfo.getCompanyChargeScale());//后置佣金（%）
                    Float moneys=0f;
                    if(ljHouseInfo.getPreCommission() != 0 || ljHouseInfo.getPreCommission()!=null){
                        moneys+=ljHouseInfo.getPreCommission();//前置
                    }
                    if(ljHouseInfo.getPostCommission() != 0 || ljHouseInfo.getPostCommission() != null){
                        moneys+=ljHouseInfo.getPostCommission();//后置
                    }
                    if(ljHouseInfo.getOtherCommission() != 0 || ljHouseInfo.getOtherCommission() != null ){
                        moneys+=ljHouseInfo.getOtherCommission();//其他
                    }
                    ljHouseInfo.setCompanyChargeMoney(moneys);
                    //公司佣金金额
                }
            }

            ljHouseInfoService.saveLjHouseInfo(ljHouseInfo);//保存房源信息
        }
        return ajaxRe(true);
    }

/*    //根据登录人角色修改操作列
    @RequestMapping("editOperationColumn")
    public Result editOperationColumn(){
        AdminUser currentAdmin = adminUserService.getCurrentAdmin();//当前登录人
        Employee employee = employeeService.queryOneEmployeeByAccount(currentAdmin.getUsername());//员工
        List<AdminRole> adminRoles = adminRoleService.queryRoleByUserId(employee.getId());
        String Column="";
        if(adminRoles.size()  >  0){
            if("销售总监".equals(adminRoles.get(0).getName()) || "销售经理".equals(adminRoles.get(0).getName())){
                Column="A";
            }
        }
        return ajaxRe(true).addData("Column",Column);

    }*/

}
