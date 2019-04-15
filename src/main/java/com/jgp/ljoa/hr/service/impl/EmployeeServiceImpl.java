package com.jgp.ljoa.hr.service.impl;

import com.jgp.common.pojo.LabelValue;
import com.jgp.ljoa.common.util.ReadExcelUtil;
import com.jgp.ljoa.hr.model.Employee;
import com.jgp.ljoa.hr.model.Relation;
import com.jgp.ljoa.hr.repository.EmployeeRepository;
import com.jgp.ljoa.hr.repository.OrganizationRepository;
import com.jgp.ljoa.hr.service.EmployeeService;
import com.jgp.ljoa.hr.service.RelationService;
import com.jgp.security.admin.model.AdminRole;
import com.jgp.security.admin.model.AdminUser;
import com.jgp.security.admin.repository.AdminRoleRepository;
import com.jgp.security.admin.repository.AdminUserRepository;
import com.jgp.security.admin.service.AdminRoleUserService;
import com.jgp.security.admin.service.AdminUserService;
import com.jgp.sys.model.Attribute;
import com.jgp.sys.query.Operator;
import com.jgp.sys.query.OrderDirection;
import com.jgp.sys.query.OrderList;
import com.jgp.sys.query.QueryFilterList;
import com.jgp.sys.service.AttributeService;
import com.jgp.sys.ui.Pager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 项目   ljoa
 * 作者   liujinxu
 * 时间   2018/7/5
 */
@Service
public class EmployeeServiceImpl implements EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private AdminUserService adminUserService;
    @Autowired
    private RelationService relationService;
    @Autowired
    private AdminRoleUserService adminRoleUserService;
    @Autowired
    private AdminUserRepository adminUserRepository;
    @Autowired
    private OrganizationRepository organizationRepository;
    @Autowired
    private AdminRoleRepository adminRoleRepository;
    @Autowired
    private AttributeService attributeService;



    @Override
    public List<Employee> queryGroupEmployee(Employee employee, Pager pager) {
        QueryFilterList list = new QueryFilterList();
        if(StringUtils.isNotEmpty(employee.getAccount())){
            list.addFilter("account","like", employee.getAccount());
        }
        if(StringUtils.isNotEmpty(employee.getPersonName())){
            list.addFilter("personName","like", employee.getPersonName());
        }
        if(StringUtils.isNotEmpty(employee.getIdentity())){
            list.addFilter("identity","like", employee.getIdentity());
        }
        if(StringUtils.isNotEmpty(employee.getLinkTel())){
            list.addFilter("linkTel","like", employee.getLinkTel());
        }
        if(StringUtils.isNotEmpty(employee.getEmployeeType())){
            list.addFilter("employeeType","eq", employee.getEmployeeType());
        }
        OrderList orders = new OrderList();
        orders.addOrder("workCode", OrderDirection.ASC);
        return employeeRepository.read(list, orders, pager);
    }

    @Override
    public List<Employee> queryGroupEmployeeByEmployeeType(Employee employee, Pager pager) {
        QueryFilterList list = new QueryFilterList();
        if(StringUtils.isNotEmpty(employee.getAccount())){
            list.addFilter("account","like", employee.getAccount());
        }
        if(StringUtils.isNotEmpty(employee.getPersonName())){
            list.addFilter("personName","like", employee.getPersonName());
        }
        if(StringUtils.isNotEmpty(employee.getIdentity())){
            list.addFilter("identity","like", employee.getIdentity());
        }
        if(StringUtils.isNotEmpty(employee.getLinkTel())){
            list.addFilter("linkTel","like", employee.getLinkTel());
        }
       List<String> list1=new ArrayList<>();
        list1.add(employee.getEmployeeType());
            list.addFilter("employeeType", Operator.nin, list1);

        OrderList orders = new OrderList();
        orders.addOrder("workCode", OrderDirection.ASC);
        return employeeRepository.read(list, orders, pager);
    }


    @Override
    public Employee queryOneEmployee(String id) {
        return employeeRepository.read(id);
    }


    @Override
    public Employee queryOneEmployeeByAccount(String account) {
        QueryFilterList list = new QueryFilterList();
        list.addFilter("account","eq",account);
        List<Employee> read = employeeRepository.read(list);
        return read.size() > 0 ? read.get(0) : null;
    }

    @Transactional
    @Override
    public Employee saveEmployee(Employee employee, String orgId) {
        //          去重账号后加数字标示
        Employee em=employee;
        QueryFilterList list = new QueryFilterList();
        list.addFilter("account", Operator.likeL,employee.getAccount());
        List<Employee> read = employeeRepository.read(list);
        List<Employee> collect = read.stream().filter(ee -> {
            String substring = ee.getAccount().substring(0, ee.getAccount().length() - 2);
            if (ee.getAccount().equals(em.getAccount()) || substring.equals(em.getAccount())) {
                return true;
            } else {
                return false;
            }
        }).collect(Collectors.toList());
        if(collect.size()<10&&collect.size()!=0){
            employee.setAccount(employee.getAccount()+"0"+String.valueOf(collect.size()));
        }else if(collect.size()>=10){
            employee.setAccount(employee.getAccount()+String.valueOf(collect.size()));
        }


        AdminUser adminUser = adminUserService.queryUserByUserName(employee.getAccount());
        if(Objects.isNull(adminUser)){
            adminUser = new AdminUser();
            adminUser.setUsername(employee.getAccount());
            adminUser.setPassword("123456");
            AdminUser user = adminUserService.createUser(adminUser);

            employee = employeeRepository.createOrUpdate(employee);
//            if(employee.getId()!=null){
//                System.out.println("保存方法");
//            }else{
//                System.out.println("保存方法失败");
//            }
            Relation ljRelation = new Relation();
            //渠道
            if("2245d9821d8442d5830c49c790c73621".equals(orgId)
                    || "4bd6b48ac1224a3885f47bea09ce254e".equals(orgId)
                    || "8d30f33165aa49ba8206e09b6093baaa".equals(orgId)
                    ||"e236582cae2e40f8b243e124391428d2".equals(orgId)
                    ||"e399286109904779bf0eca404a68208b".equals(orgId)
                    ||"c363305b32bc44a0a01853f500e5f33b".equals(orgId)){
                ljRelation.setMainUuid("3bc4148f06684cf6bebcf80bbbf2971c");//部门Id
                ljRelation.setSubUuid(user.getId());//admin员工id
            }
            //营销
            else if("0e7274539ce74e0fbf1b84d329af343f".equals(orgId)
                    || "38b9aba0ebff436a8c2bd3b6acae7a24".equals(orgId)
                    || "97657831030c46748034a31f2f8fb2cc".equals(orgId)
                    || "9b521b59d7f74050a15585b2fbf8ac9a".equals(orgId)
                    || "dc491f93968f4a24b20d00575d718222".equals(orgId)){
                ljRelation.setMainUuid("d4a1dd4ca37749fc9380f8d61454305c");//部门Id
                ljRelation.setSubUuid(user.getId());//admin员工id
            }
            //财务
            else if("554676a6bdd246b39be21323b935f251".equals(orgId)
                    ||"bc02b8dadc4b4ccd82b3e48208c1c5cf".equals(orgId)) {
                ljRelation.setMainUuid("47e34c25444041d799d04bdfb29a00a4");//部门Id
                ljRelation.setSubUuid(user.getId());//admin员工id
            }
            //综合管理部
            else if("026f1b4b033f440aa0141a131c7873a4".equals(orgId)
                    || "351eac44bb164e8e8ae3e8b029ae3523".equals(orgId)
                    || "53cf3eea8d2d46fcbac12d9ec0e19d55".equals(orgId)
                    || "570ddd327f464e5d9b8f430d443cf79e".equals(orgId)
                    ||"dc84300218db4322809985c19651f67a".equals(orgId)){
                ljRelation.setMainUuid("e92f50d1ce07428faf558b7313a149db");//部门Id
                ljRelation.setSubUuid(user.getId());//admin员工id
            }
            //创意策划部
            else if("66bb46b35b8047d095e8d53534ecda76".equals(orgId)
                    || "7996b5048ea44f8c8ef467ef6025c211".equals(orgId)
                    || "7b1da528fa0142da8969605834e20576".equals(orgId)
                    ||"433311949eee49d2aec25a767d759a67".equals(orgId)){
                ljRelation.setMainUuid("4fce54ec7369496cbaa1cd008ea5688b");//部门Id
                ljRelation.setSubUuid(user.getId());//admin员工id
            }
            //邻家装设计部
            else if("136f7217b59042fda8f64302f5e5a152".equals(orgId)
                    ||"e5ee6885a7c3448199b739b8a2a6328f".equals(orgId)){
                ljRelation.setMainUuid("98260d14ef3a4b098fcf0e46766b8bed");//部门Id
                ljRelation.setSubUuid(user.getId());//admin员工id
            }
            //新房事业部
            else if("6ce6d48e6efa41708dbdd9c4842d30ff".equals(orgId)
                    || "8d98bdfbcf4f4a588f14418edddd0e32".equals(orgId)){
                ljRelation.setMainUuid("fe488cafd8174bd789df302cae699263");//部门Id
                ljRelation.setSubUuid(user.getId());//admin员工id
            }
            //二手房事业部
            else if("8aba24254dee406a864e3c519d8e7aba".equals(orgId)
                    || "0934026b91934790bb008ac67e565fde".equals(orgId)
                    || "ec88ec742b2949fe92d6d269c158660f".equals(orgId)
                    || "9499184e0ecf413fa4c5b50fcb90082f".equals(orgId)
                    || "0366e5ebe88f4be1a296e2e4971c96fe".equals(orgId)){
                ljRelation.setMainUuid("80df0136da4c4582a6a113ff97073b8f");//部门Id
                ljRelation.setSubUuid(user.getId());//admin员工id
            }

            ljRelation.setRelationType("3");//部门与用户
            relationService.saveRelation(ljRelation);
            List<String> adminUserId = new ArrayList<>();
            adminUserId.add(user.getId());
            adminRoleUserService.addRoleUser(orgId, adminUserId);


            return employee;
        }else {
            return null;
        }
    }

    @Transactional
    @Override
    public Employee saveEmployee(Employee employee) {
        //离职人员删除登陆信息
        if("6".equals(employee.getEmployeeType())){
            AdminUser ad = adminUserService.queryUserByUserName(employee.getAccount());
            adminUserRepository.deletePersist(ad);
        }
        return employeeRepository.createOrUpdate(employee);
    }

    @Transactional
    @Override
    public void removeOneEmployee(String id) {
        Employee read = employeeRepository.read(id);
        AdminUser adminUser = adminUserService.queryUserByUserName(read.getAccount());
        if(adminUser != null){
            adminUserRepository.deletePersist(adminUser);
        }
        employeeRepository.delete(id);
    }

    @Transactional
    @Override
    public void removeSelectedEmployee(String[] array) {
        Arrays.stream(array).forEach(s -> {
            this.removeOneEmployee(s);
        });
    }

    @Override
    public List<Employee> queryAllEmployee() {
        List<Employee> employees = employeeRepository.readAll();
        return employees;
    }


    @Override
    public List<LabelValue> queryEmployeeByOrgId(String orgId) {
        Relation relation = new Relation();
        relation.setMainUuid(orgId);
        relation.setRelationType("3");//关系类型为部门与员工
        List<Relation> relations = relationService.queryGroupRelation(relation);
        List<LabelValue> collect = relations.stream().map(relation1 -> {
            AdminUser adminUser = adminUserService.queryUserById(relation1.getSubUuid());
            QueryFilterList list = new QueryFilterList();
            list.addFilter("account", "eq", adminUser.getUsername());
            list.addFilter("employeeType", "nin", "6");
            List<Employee> read = employeeRepository.read(list);
            if (Objects.nonNull(read) && read.size() > 0) {
                return new LabelValue(read.get(0).getPersonName(), adminUser.getId(), null);
            } else {
                return null;
            }
        }).filter(labelValue -> {
            if (Objects.nonNull(labelValue)) {
                return true;
            } else {
                return false;
            }
        }).collect(Collectors.toList());
        return collect;
    }


    @Override
    public Employee queryCurrentEmployee() {
        String username = adminUserService.getCurrentAdmin().getUsername();
        QueryFilterList list = new QueryFilterList();
        list.addFilter("account","eq",username);
        List<Employee> read = employeeRepository.read(list);
        return read.size() > 0 ? read.get(0) : null;
    }

    @Override
    public void importData(File file) throws Exception {

        ReadExcelUtil poi = new ReadExcelUtil();
        int rowsize = poi.importExcel(file);
        String orgid="";
        for (int i = 0; i <= rowsize+1; i++) {

            if (i > 1) {

                Employee employee=new Employee();
                //序号
                if(poi.getStrByNumIndex(0)==null){
                    continue;
                }
                employee.setOrderSn(Integer.valueOf(poi.getStrByNumIndex(0)));
                //账号
                employee.setAccount(poi.getStrByNumIndex(1));
                //工号
                employee.setWorkCode(poi.getStrByNumIndex(2));
                //姓名
                employee.setPersonName(poi.getStrByNumIndex(3));
                //证件号
                employee.setIdentity( poi.getStrByNumIndex(4));
                //部门

                //职位
                List<AdminRole> adminRoles = adminRoleRepository.readAll();
                for(AdminRole a:adminRoles){
                    if((poi.getStrByNumIndex(6)).equals(a.getName())){
                        orgid=a.getId();
                    }
                }

                //职级
                List<Attribute> attributes = attributeService.queryAttributesByActiveKey("LJOA_EMPLOYEE.POSI");
                Boolean k=true;
                for(Attribute a:attributes){
                    if(Objects.nonNull(a.getValue())&&a.getLabel().equals(poi.getStrByNumIndex(7))){
                        k=false;
                        employee.setPosi(a.getValue());
                    }
                }
                if(k){
                    employee.setPosi("6");
                }

                // 性别
                if((poi.getStrByNumIndex(8)).equals("男")){
                    employee.setSex("1");
                }else{
                    employee.setSex("0");
                }

                //生辰

                Timestamp date = poi.getDate(9);
                if(date!=null){
                    LocalDateTime localDateTime = date.toLocalDateTime();
                    employee.setBirthday(localDateTime.toLocalDate());
                }else{
                    String[] signTimeArr = String.valueOf(poi.getStrByNumIndex(9)).split("-");
                    String[] signTimeArr2 = String.valueOf(poi.getStrByNumIndex(9)).split("/");
                    String[] signTimeArr3 = String.valueOf(poi.getStrByNumIndex(9)).split(".");
                    if (signTimeArr.length >= 3) {
                        employee.setBirthday(LocalDate.of(Integer.valueOf(signTimeArr[0]), Integer.valueOf(signTimeArr[1]), Integer.valueOf(signTimeArr[2])));
                    }else if(signTimeArr2.length>=3){
                        employee.setBirthday(LocalDate.of(Integer.valueOf(signTimeArr2[0]), Integer.valueOf(signTimeArr2[1]), Integer.valueOf(signTimeArr2[2])));
                    }else if (signTimeArr3.length >= 3) {
                        employee.setBirthday(LocalDate.of(Integer.valueOf(signTimeArr3[0]), Integer.valueOf(signTimeArr3[1]), Integer.valueOf(signTimeArr3[2])));
                    }else{
                        employee.setBirthday(null);
                    }
                }

               /* */
                //年龄
                employee.setAge(Integer.valueOf(poi.getStrByNumIndex(10)));
                //离职日期
                Timestamp date2 = poi.getDate(11);
                if(date2!=null){
                    LocalDateTime localDateTime2 = date2.toLocalDateTime();
                    employee.setLeaveTime(localDateTime2.toLocalDate());
                }else{
                    String[] signTimeArr = String.valueOf(poi.getStrByNumIndex(11)).split("-");
                    String[] signTimeArr2 = String.valueOf(poi.getStrByNumIndex(11)).split("/");
                    String[] signTimeArr3 = String.valueOf(poi.getStrByNumIndex(11)).split(".");
                    if (signTimeArr.length >= 3) {
                        employee.setLeaveTime(LocalDate.of(Integer.valueOf(signTimeArr[0]), Integer.valueOf(signTimeArr[1]), Integer.valueOf(signTimeArr[2])));
                    }else if(signTimeArr2.length>=3){
                        employee.setLeaveTime(LocalDate.of(Integer.valueOf(signTimeArr2[0]), Integer.valueOf(signTimeArr2[1]), Integer.valueOf(signTimeArr2[2])));
                    }else if (signTimeArr3.length >= 3) {
                        employee.setLeaveTime(LocalDate.of(Integer.valueOf(signTimeArr3[0]), Integer.valueOf(signTimeArr3[1]), Integer.valueOf(signTimeArr3[2])));
                    }else{
                        employee.setLeaveTime(null);
                    }
                }

              /*  */

                //主动离职
                attributes = attributeService.queryAttributesByActiveKey("LJOA_EMPLOYEE.LEAVETYPE");
                for(Attribute a:attributes){
                    if(Objects.nonNull(a.getValue())&&a.getLabel().equals(poi.getStrByNumIndex(12))){
                        employee.setLeaveType(a.getValue());
                    }
                }

                //被动离职
                attributes = attributeService.queryAttributesByActiveKey("LJOA_EMPLOYEE.PASSIVETYPE");
                for(Attribute a:attributes){
                    if(Objects.nonNull(a.getValue())&&a.getLabel().equals(poi.getStrByNumIndex(13))){
                        employee.setPassiveType(a.getValue());
                    }
                }

                //学历
                attributes = attributeService.queryAttributesByActiveKey("LJOA_EMPLOYEE.EDUCATION");
                k = true;
                for(Attribute a:attributes){
                    if(Objects.nonNull(a.getValue())&&a.getLabel().equals(poi.getStrByNumIndex(14))){
                        k=false;
                        employee.setEducation(a.getValue());
                    }
                }
                if(k){
                    employee.setEducation(null);
                }

                //学校
                employee.setSchool(poi.getStrByNumIndex(15));
                //专业
                employee.setSpecialty(poi.getStrByNumIndex(16));
                //联系方式
                employee.setLinkTel(poi.getStrByNumIndex(17));
                //紧急联系人
                employee.setCriticalLinkMan(poi.getStrByNumIndex(18));
                //紧急联系人TEL
                employee.setCriticalLinkTel(poi.getStrByNumIndex(19));
                //民族
                employee.setNation(poi.getStrByNumIndex(20));
                //政治面貌
                attributes = attributeService.queryAttributesByActiveKey("LJOA_EMPLOYEE.POLITICAL");
                k=true;
                for(Attribute a:attributes){
                    if(Objects.nonNull(a.getValue())&&a.getLabel().equals(poi.getStrByNumIndex(21))){
                        k=false;
                        employee.setPolitical(a.getValue());
                    }
                }
                if(k){
                    employee.setPolitical(null);
                }

                //籍贯
                employee.setBirthPlace(poi.getStrByNumIndex(22));
                //户籍地址
                employee.setCensusAddress(poi.getStrByNumIndex(23));
                //现住址
                employee.setAddress(poi.getStrByNumIndex(24));
                //毕业时间
                Timestamp date3 = poi.getDate(25);
                if(date3!=null){
                    LocalDateTime localDateTime3 = date3.toLocalDateTime();
                    employee.setGraduateTime(localDateTime3.toLocalDate());
                }else{
                    String[] signTimeArr = String.valueOf(poi.getStrByNumIndex(25)).split("-");
                    String[] signTimeArr2 = String.valueOf(poi.getStrByNumIndex(25)).split("/");
                    String[] signTimeArr3 = String.valueOf(poi.getStrByNumIndex(25)).split(".");
                    if (signTimeArr.length >= 3) {
                        employee.setGraduateTime(LocalDate.of(Integer.valueOf(signTimeArr[0]), Integer.valueOf(signTimeArr[1]), Integer.valueOf(signTimeArr[2])));
                    }else if(signTimeArr2.length>=3){
                        employee.setGraduateTime(LocalDate.of(Integer.valueOf(signTimeArr2[0]), Integer.valueOf(signTimeArr2[1]), Integer.valueOf(signTimeArr2[2])));
                    }else if (signTimeArr3.length >= 3) {
                        employee.setGraduateTime(LocalDate.of(Integer.valueOf(signTimeArr3[0]), Integer.valueOf(signTimeArr3[1]), Integer.valueOf(signTimeArr3[2])));
                    }else{
                        employee.setGraduateTime(null);
                    }
                }


                //入职时间
                Timestamp date4 = poi.getDate(26);
                if(date4!=null){
                    LocalDateTime localDateTime4 = date4.toLocalDateTime();
                    employee.setInTime(localDateTime4.toLocalDate());
                }else{
                    String[] signTimeArr = String.valueOf(poi.getStrByNumIndex(26)).split("-");
                    String[] signTimeArr2 = String.valueOf(poi.getStrByNumIndex(26)).split("/");
                    String[] signTimeArr3 = String.valueOf(poi.getStrByNumIndex(26)).split(".");
                    if (signTimeArr.length >= 3) {
                        employee.setInTime(LocalDate.of(Integer.valueOf(signTimeArr[0]), Integer.valueOf(signTimeArr[1]), Integer.valueOf(signTimeArr[2])));
                    }else if(signTimeArr2.length>=3){
                        employee.setInTime(LocalDate.of(Integer.valueOf(signTimeArr2[0]), Integer.valueOf(signTimeArr2[1]), Integer.valueOf(signTimeArr2[2])));
                    }else if (signTimeArr3.length >= 3) {
                        employee.setInTime(LocalDate.of(Integer.valueOf(signTimeArr3[0]), Integer.valueOf(signTimeArr3[1]), Integer.valueOf(signTimeArr3[2])));
                    }else{
                        employee.setInTime(null);
                    }
                }



                //员工类型
                attributes = attributeService.queryAttributesByActiveKey("LJOA_EMPLOYEE.EMPLOYEETYPE");
                k=true;
                for(Attribute a:attributes){
                    if(Objects.nonNull(a.getValue())&&a.getLabel().equals(poi.getStrByNumIndex(27))){
                        k=false;
                        employee.setEmployeeType(a.getValue());
                    }
                }
                if(k){
                    employee.setEmployeeType(null);
                }

                //合同签订公司
                employee.setContractCompany(poi.getStrByNumIndex(28));
                //最新合同起始时间
                Timestamp date5 = poi.getDate(29);
                if(date5!=null){
                    LocalDateTime localDateTime5 = date5.toLocalDateTime();
                    employee.setContractBeginTime(localDateTime5.toLocalDate());
                }else{
                    String[] signTimeArr = String.valueOf(poi.getStrByNumIndex(29)).split("-");
                    String[] signTimeArr2 = String.valueOf(poi.getStrByNumIndex(29)).split("/");
                    String[] signTimeArr3 = String.valueOf(poi.getStrByNumIndex(29)).split(".");
                    if (signTimeArr.length >= 3) {
                        employee.setContractBeginTime(LocalDate.of(Integer.valueOf(signTimeArr[0]), Integer.valueOf(signTimeArr[1]), Integer.valueOf(signTimeArr[2])));
                    }else if(signTimeArr2.length>=3){
                        employee.setContractBeginTime(LocalDate.of(Integer.valueOf(signTimeArr2[0]), Integer.valueOf(signTimeArr2[1]), Integer.valueOf(signTimeArr2[2])));
                    }else if (signTimeArr3.length >= 3) {
                        employee.setContractBeginTime(LocalDate.of(Integer.valueOf(signTimeArr3[0]), Integer.valueOf(signTimeArr3[1]), Integer.valueOf(signTimeArr3[2])));
                    }else{
                        employee.setContractBeginTime(null);
                    }
                }

            /*  */
                //最新合同终止时间
                Timestamp date6 = poi.getDate(30);
                if(date6!=null){
                    LocalDateTime localDateTime6 = date6.toLocalDateTime();
                    employee.setContractEndTime(localDateTime6.toLocalDate());
                }else{
                    String[] signTimeArr = String.valueOf(poi.getStrByNumIndex(30)).split("-");
                    String[] signTimeArr2 = String.valueOf(poi.getStrByNumIndex(30)).split("/");
                    String[] signTimeArr3 = String.valueOf(poi.getStrByNumIndex(30)).split(".");
                    if (signTimeArr.length >= 3) {
                        employee.setContractEndTime(LocalDate.of(Integer.valueOf(signTimeArr[0]), Integer.valueOf(signTimeArr[1]), Integer.valueOf(signTimeArr[2])));
                    }else if(signTimeArr2.length>=3){
                        employee.setContractEndTime(LocalDate.of(Integer.valueOf(signTimeArr2[0]), Integer.valueOf(signTimeArr2[1]), Integer.valueOf(signTimeArr2[2])));
                    }else if (signTimeArr3.length >= 3) {
                        employee.setContractEndTime(LocalDate.of(Integer.valueOf(signTimeArr3[0]), Integer.valueOf(signTimeArr3[1]), Integer.valueOf(signTimeArr3[2])));
                    }else{
                        employee.setContractEndTime(null);
                    }
                }


                //档案所在地
                employee.setFileLocation(poi.getStrByNumIndex(31));
                //劳动关系转移身份
                employee.setRelationMove(poi.getStrByNumIndex(32));

                //户籍性质
                attributes = attributeService.queryAttributesByActiveKey("LJOA_EMPLOYEE.CENSUSNATURE");
                k=true;
                for(Attribute a:attributes){
                    if(Objects.nonNull(a.getValue())&&a.getLabel().equals(poi.getStrByNumIndex(33))){
                        k=false;
                        employee.setCensusNature(a.getValue());
                    }
                }
                if(k){
                    employee.setCensusNature(null);
                }

                //是否本地户口
                attributes = attributeService.queryAttributesByActiveKey("LJOA_EMPLOYEE.ISLOCALACCOUNT");
                k=true;
                for(Attribute a:attributes){
                    if(Objects.nonNull(a.getValue())&&a.getLabel().equals(poi.getStrByNumIndex(34))){
                        k=false;
                        employee.setIsLocalAccount(a.getValue());
                    }
                }
                if(k){
                    employee.setIsLocalAccount(null);
                }

                //招聘渠道
                attributes = attributeService.queryAttributesByActiveKey("LJOA_EMPLOYEE.RECRUITTYPE");
                k=true;
                for(Attribute a:attributes){
                    if(Objects.nonNull(a.getValue())&&a.getLabel().equals(poi.getStrByNumIndex(35))){
                        k=false;
                        employee.setRecruitType(a.getValue());
                    }
                }
                if(k){
                    employee.setRecruitType(null);
                }

                Employee employee1 = this.saveEmployee(employee, orgid);
                if(employee1==null){
                    System.out.println("导入失败"+employee.getPersonName()+""+orgid);
                }

            }
            poi.nextRow();
        }
    }
}
