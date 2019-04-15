package com.jgp.ljoa.controller;

import com.jgp.ljoa.channel.model.LjHouseInfo;
import com.jgp.ljoa.channel.model.LjProjectInfo;
import com.jgp.ljoa.channel.service.LjHouseInfoService;
import com.jgp.ljoa.channel.service.LjProjectInfoService;
import com.jgp.ljoa.hr.model.Employee;
import com.jgp.ljoa.hr.service.EmployeeService;
import com.jgp.security.admin.model.AdminRole;
import com.jgp.security.admin.repository.AdminRoleRepository;
import com.jgp.security.admin.service.AdminRoleService;
import com.jgp.security.admin.service.AdminRoleUserService;
import com.jgp.security.admin.service.AdminUserService;
import com.jgp.sys.controller.JGPController;
import com.jgp.sys.model.Attribute;
import com.jgp.sys.service.AttributeService;
import com.jgp.sys.ui.Result;
import org.olap4j.impl.ArrayMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Created by Administrator on 2018/9/12.
 */
/*
作者  SSF
时间   2018/9/12
*/
@RestController
@RequestMapping("/ljoa/indexApiController")
public class IndexApiController extends JGPController {
    @Autowired
    private LjProjectInfoService ljProjectInfoService;
    @Autowired
    private LjHouseInfoService ljHouseInfoService;
    @Autowired
    private AttributeService attributeService;
    @Autowired
    private AdminUserService adminUserService;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private AdminRoleUserService adminRoleUserService;
    @Autowired
    private AdminRoleService adminRoleService;
    @Autowired
    private AdminRoleRepository adminRoleRepository;


    @RequestMapping("listGroupLjProjectInfoIndex")//index页面柱状图数据
    public Result listGroupLjProjectInfoIndex(){

        List<LjProjectInfo> ljProjectInfos = ljProjectInfoService.queryAllLjProjectInfo();//所有项目

        List<ProjectProgress> collect = ljProjectInfos.stream().map(lp -> {
            List<LjHouseInfo> ljHouseInfo = ljHouseInfoService.queryGroupLjHouseInfoByUUID(lp.getId());
            ProjectProgress projectProgress = new ProjectProgress();//项目进度转换实体
            projectProgress.setProjectName(lp.getProjectName());//项目名
            if("1".equals(lp.getProjectType())){
                projectProgress.setProType(1);//项目类别
            }
            if("2".equals(lp.getProjectType())){
                projectProgress.setProType(2);//项目类别
            }

            List<Attribute> attributes = attributeService.queryAttributesByActiveKey("LJOA.TONG.PRJ_PROCESS");
            for (Attribute aa : attributes) {
                if (Objects.nonNull(aa.getValue()) && aa.getValue().equals(lp.getPrjProcess())) {
                    projectProgress.setPrjProcess(aa.getLabel());//项目进度
                }
            }
            int a = 0;
            for (LjHouseInfo l : ljHouseInfo) {
                if ("2".equals(l.getStatus())) {//已售房源
                    a++;
                }
            }
            if(ljHouseInfo.size()==0){
                projectProgress.setProgressRatio(0f);
            }else{
                float v = ((a*1f) / (ljHouseInfo.size()*1f ))* 100F;
                projectProgress.setProgressRatio(v);
            }

            return projectProgress;
        }).collect(Collectors.toList());
        return ajaxReData("ProjectProgress",collect);
    }

    @RequestMapping("MainInfo")//登陆信息判断
    public Result MainInfo(Model model){

        List<AdminRole> adminRoles = adminRoleUserService.queryRoles(adminUserService.getCurrentAdmin().getId());
        String name = adminRoles.get(0).getName();
        String admin="";
        if("财务".equals(name)){
            admin="财务";
        }else if("营销助理".equals(name)){
            admin="营销助理";
        }else if("营销经理".equals(name)){
            admin="营销经理";
        }else if("营销主管".equals(name)){
            admin="营销主管";
        }else if("置业顾问".equals(name)){
            admin="置业顾问";
        }else if("区域经理".equals(name)){
            admin="区域经理";
        }
        return ajaxReData("admin",admin);
    }

    @RequestMapping("indexShowInfo")//index首页部分数据
    public Result indexShowInfo(Model model){
        Map map=new ArrayMap();
        List<Employee> employees = employeeService.queryAllEmployee();
        List<Employee> collect = employees.stream().filter(em -> {
            if (!"6".equals(em.getEmployeeType())) {
                return true;
            } else {
                return false;
            }
        }).collect(Collectors.toList());
        map.put("personnel",collect.size());//员工数量
        LjHouseInfo ljHouseInfo=new LjHouseInfo();
        ljHouseInfo.setStatus("2");
        List<LjHouseInfo> ljHouseInfos = ljHouseInfoService.queryGroupLjHouseInfoINFO(ljHouseInfo, null);
        map.put("room",ljHouseInfos.size());//已售房源数量
        Float money=0f;
        for(int i=0;i<ljHouseInfos.size();i++){
            money+=ljHouseInfos.get(0).getSaleMoney();
        }
         map.put("money",money);//销售额度
        return ajaxReData("dataLine",map);
    }

}
