package com.jgp.ljoa.common.controller;/**
 * Created by Administrator on 2018/10/11.
 */

import com.jgp.common.annotation.UIParam;
import com.jgp.common.pojo.LabelValue;
import com.jgp.ljoa.common.model.Message;
import com.jgp.ljoa.common.service.MessageService;
import com.jgp.ljoa.hr.model.Employee;
import com.jgp.ljoa.hr.model.Relation;
import com.jgp.ljoa.hr.service.EmployeeService;
import com.jgp.ljoa.hr.service.RelationService;
import com.jgp.security.admin.model.AdminUser;
import com.jgp.security.admin.service.AdminUserService;
import com.jgp.sys.controller.JGPController;
import com.jgp.sys.ui.GridResult;
import com.jgp.sys.ui.Pager;
import com.jgp.sys.ui.Result;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 项目   bank
 * 作者   liujinxu
 * 时间   2018/10/11
 */
@RestController
@RequestMapping("/ljoa/common/messageApiController")
public class MessageApiController extends JGPController {
    @Autowired
    private MessageService messageService;
    @Autowired
    private AdminUserService adminUserService;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private RelationService relationService;

    //添加消息
    @RequestMapping("addMessage")
    public Result addMessage(Message message){
        message.setMsgTime(LocalDateTime.now());
        AdminUser adminUser = adminUserService.queryUserById(message.getAcceptMan());
        Employee employee = employeeService.queryOneEmployeeByAccount(adminUser.getUsername());
        message.setAcceptManName(employee.getPersonName());
        messageService.saveMessage(message);
        return ajaxRe(true);
    }

    //编辑消息
    @RequestMapping("editMessage")
    public Result editMessage(Message message){
        messageService.saveMessage(message);
        return ajaxRe(true);
    }

    //删除单个信息
    @RequestMapping("removeOneMessage")
    public Result removeOneMessage(String id){
        messageService.removeOneMessage(id);
        return ajaxRe(true);
    }

    //批量删除
    @RequestMapping("removeSelectedMessage")
    public Result removeSelectedMessage(String[] array){
        messageService.removeSelectedMessage(array);
        return ajaxRe(true);
    }


    //条件查询已发送的信息
    @RequestMapping("listGroupMessageForSend")
    public Result listGroupMessageForSend(Message message, String beginDate, String endDate, @UIParam("pager")Pager pager){
        AdminUser currentAdmin = adminUserService.getCurrentAdmin();
        message.setSendMan(currentAdmin.getId());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        if(StringUtils.isNotEmpty(beginDate)){
            LocalDate parse = LocalDate.parse(beginDate, formatter);
            LocalDateTime beginDatetime = LocalDateTime.of(parse, LocalTime.of(0, 0, 0));
            message.setCreateDatetime(beginDatetime);
        }
        if(StringUtils.isNotEmpty(endDate)) {
            LocalDate parse1 = LocalDate.parse(endDate, formatter);
            LocalDateTime endDatetime = LocalDateTime.of(parse1, LocalTime.of(23, 59, 59));
            message.setModifyDatetime(endDatetime);
        }
        List<Message> messages = messageService.queryGroupMessage(message, pager);
        List<GridResult.Category> categories = new ArrayList<>();
        categories.add(new GridResult.Category("接收人", "acceptManName"));
        categories.add(new GridResult.Category("标题", "msgTitle"));
        categories.add(new GridResult.Category("发送时间", "msgTime"));
        return ajaxReGrid("gdata", categories, messages, pager);
    }

    //条件查询已接受的信息
    @RequestMapping("listGroupMessageForAccept")
    public Result listGroupMessageForAccept(Message message, String beginDate, String endDate, @UIParam("pager")Pager pager){
        AdminUser currentAdmin = adminUserService.getCurrentAdmin();
        message.setAcceptMan(currentAdmin.getId());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        if(StringUtils.isNotEmpty(beginDate)){
            LocalDate parse = LocalDate.parse(beginDate, formatter);
            LocalDateTime beginDatetime = LocalDateTime.of(parse, LocalTime.of(0, 0, 0));
            message.setCreateDatetime(beginDatetime);
        }
        if(StringUtils.isNotEmpty(endDate)) {
            LocalDate parse1 = LocalDate.parse(endDate, formatter);
            LocalDateTime endDatetime = LocalDateTime.of(parse1, LocalTime.of(23, 59, 59));
            message.setModifyDatetime(endDatetime);
        }
        List<Message> messages = messageService.queryGroupMessageForAccept(message, pager);
        Relation relation = new Relation();
        relation.setRelationType(Relation.RELATION_TYPE_5);
        relation.setMainUuid(currentAdmin.getId());
        messages.stream().forEach(message1 -> {
            relation.setSubUuid(message1.getId());
            List<Relation> relations = relationService.queryGroupRelation(relation);
            if(Objects.nonNull(relations) && relations.size() > 0){
                message1.setIsRead("1");//已读
            }else {
                message1.setIsRead("0");//未读
            }
        });
        List<Message> collect = messages.stream().sorted((o1, o2) -> o1.getIsRead().compareTo(o2.getIsRead())).collect(Collectors.toList());
//        List<Message> messages1 = messageService.queryGroupMessageByIsRead(messages, pager);

        List<GridResult.Category> categories = new ArrayList<>();
        categories.add(new GridResult.Category("发送人", "sendManName"));
        categories.add(new GridResult.Category("标题", "msgTitle"));
        categories.add(new GridResult.Category("发送时间", "msgTime"));
        categories.add(new GridResult.Category("是否已读", "isRead", "MESSAGE. IS_READ"));
        return ajaxReGrid("gdata", categories, collect, pager);
    }

    //查询所有用户(排除当前用户)
    @RequestMapping("queryAllAdminUser")
    public Result queryAllAdminUser(){
        AdminUser currentAdmin = adminUserService.getCurrentAdmin();
        List<Employee> employees = employeeService.queryAllEmployee();
        List<LabelValue> collect = employees.stream().filter(employee -> {
            //排除当前用户
            if (employee.getAccount().equals(currentAdmin.getUsername())) {
                return false;
            } else {
                return true;
            }
        }).map(employee -> {
            AdminUser adminUser = adminUserService.queryUserByUserName(employee.getAccount());
            if(employee.getAccount() != null){
                return new LabelValue(employee.getAccount(), adminUser.getId(), null);
            }else{
                return new LabelValue(employee.getPersonName(), adminUser.getId(), null);
            }

        }).collect(Collectors.toList());
        return ajaxReData("ldata", collect);
    }

    //查询当前用户的未读消息
    @RequestMapping("queryNewMessage")
    public Result queryNewMessage(){
        AdminUser currentAdmin = adminUserService.getCurrentAdmin();
        Message message = new Message();
        message.setAcceptMan(currentAdmin.getId());
        List<Message> messages = messageService.queryGroupMessageForAccept(message, null);
        Relation relation = new Relation();
        relation.setRelationType(Relation.RELATION_TYPE_5);
        relation.setMainUuid(currentAdmin.getId());
        List<Message> collect = messages.stream().filter(message1 -> {
            relation.setSubUuid(message1.getId());
            List<Relation> relations = relationService.queryGroupRelation(relation);
            if (Objects.isNull(relations) || relations.size() == 0) {
                return true;//未读
            } else {
                return false;//已读
            }
        }).collect(Collectors.toList());
        return ajaxRe(true).addData("msgNum", collect != null ? collect.size() : 0);
    }
}
