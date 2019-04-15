package com.jgp.ljoa.common.controller;/**
 * Created by Administrator on 2018/10/11.
 */

import com.jgp.ljoa.common.model.Message;
import com.jgp.ljoa.common.service.MessageService;
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

import java.util.List;
import java.util.Objects;

/**
 * 项目   bank
 * 作者   liujinxu
 * 时间   2018/10/11
 */
@Controller
@RequestMapping("/ljoa/common/messageController")
public class MessageController extends JGPController {
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
    public String addMessage(Model model){
        AdminUser currentAdmin = adminUserService.getCurrentAdmin();
        Employee employee = employeeService.queryOneEmployeeByAccount(currentAdmin.getUsername());
        Message message = new Message();
        message.setSendMan(currentAdmin.getId());
        message.setIsRead("0");
        message.setSendManName(employee.getPersonName());
        reForm(model, "fdata", message);
        return page("addMessage");
    }

    //编辑消息
    @RequestMapping("editMessage/{id}")
    public String editMessage(@PathVariable String id, Model model){
        Message message = messageService.queryOneMessage(id);
        reForm(model, "fdata", message);
        return page("editMessage");
    }

    //条件查询已发送
    @RequestMapping("listGroupMessageForSend")
    public String listGroupMessageForSend(Model model){
        return page("listGroupMessageForSend");
    }

    //条件查询已接受
    @RequestMapping("listGroupMessageForAccept")
    public String listGroupMessageForAccept(Model model){
        return page("listGroupMessageForAccept");
    }

    //接收人查看信息
    @RequestMapping("acceptManLookMessage/{msgId}")
    public String acceptManLookMessage(@PathVariable String msgId, Model model){
        AdminUser currentAdmin = adminUserService.getCurrentAdmin();
        Relation relation = new Relation();
        relation.setMainUuid(currentAdmin.getId());
        relation.setSubUuid(msgId);
        relation.setRelationType("5");//用户是否阅读此信息
        List<Relation> relations = relationService.queryGroupRelation(relation);
        //若当前用户未查看，则是能成关联数据
        if(Objects.isNull(relations) || relations.size() == 0){
            relationService.saveRelation(relation);
        }
        Message message = messageService.queryOneMessage(msgId);
        reForm(model, "fdata", message);
        return page("showMessage");
    }
}
