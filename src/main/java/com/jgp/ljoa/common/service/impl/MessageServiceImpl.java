package com.jgp.ljoa.common.service.impl;/**
 * Created by Administrator on 2018/10/11.
 */

import com.jgp.ljoa.common.model.Message;
import com.jgp.ljoa.common.repository.MessageRepository;
import com.jgp.ljoa.common.service.MessageService;
import com.jgp.ljoa.hr.model.Relation;
import com.jgp.ljoa.hr.service.RelationService;
import com.jgp.security.admin.model.AdminRole;
import com.jgp.security.admin.model.AdminRoleUser;
import com.jgp.security.admin.repository.AdminRoleRepository;
import com.jgp.security.admin.repository.AdminRoleUserRepository;
import com.jgp.sys.query.Operator;
import com.jgp.sys.query.OrderDirection;
import com.jgp.sys.query.OrderList;
import com.jgp.sys.query.QueryFilterList;
import com.jgp.sys.ui.Pager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 项目   bank
 * 作者   liujinxu
 * 时间   2018/10/11
 */
@Service
public class MessageServiceImpl implements MessageService{
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private RelationService relationService;
    @Autowired
    private AdminRoleRepository adminRoleRepository;
    @Autowired
    private AdminRoleUserRepository adminRoleUserRepository;

    @Transactional
    @Override
    public Message saveMessage(Message message) {
        return messageRepository.createOrUpdate(message);
    }

    @Transactional
    @Override
    public void removeOneMessage(String id) {
        messageRepository.delete(id);
    }

    @Transactional
    @Override
    public void removeSelectedMessage(String[] array) {
        List<String> collect = Arrays.stream(array).collect(Collectors.toList());
        QueryFilterList list = new QueryFilterList();
        list.addFilter("id", Operator.in, collect);
        messageRepository.delete(list);
    }

    @Override
    public Message queryOneMessage(String id) {
        return messageRepository.readPersist(id);
    }

    @Override
    public List<Message> queryGroupMessage(Message message, Pager pager) {
        QueryFilterList list = new QueryFilterList();
        if(Objects.nonNull(message.getSendMan()) && StringUtils.isNotEmpty(message.getSendMan())){
            list.addFilter("sendMan",Operator.eq, message.getSendMan());
        }
        if(Objects.nonNull(message.getAcceptMan()) && StringUtils.isNotEmpty(message.getAcceptMan())){
            list.addFilter("acceptMan",Operator.eq, message.getAcceptMan());
        }
        if(Objects.nonNull(message.getMsgTitle()) && StringUtils.isNotEmpty(message.getMsgTitle())){
            list.addFilter("msgTitle",Operator.like, message.getMsgTitle());
        }
        if(Objects.nonNull(message.getIsRead()) && StringUtils.isNotEmpty(message.getIsRead())){
            list.addFilter("isRead",Operator.eq, message.getIsRead());
        }
        if(Objects.nonNull(message.getCreateDatetime())){
            list.addFilter("msgTime",Operator.ge, message.getCreateDatetime());
        }
        if(Objects.nonNull(message.getModifyDatetime())){
            list.addFilter("msgTime",Operator.le, message.getCreateDatetime());
        }
        OrderList orders = new OrderList();
        orders.addOrder("msgTime", OrderDirection.DESC);
        return messageRepository.read(list, orders, pager);
    }

    @Override
    public List<Message> queryGroupMessageForSend(Message message, Pager pager) {
        QueryFilterList list = new QueryFilterList();
        if(Objects.nonNull(message.getSendMan()) && StringUtils.isNotEmpty(message.getSendMan())){
            list.addFilter("sendMan",Operator.eq, message.getSendMan());
        }
        if(Objects.nonNull(message.getAcceptMan()) && StringUtils.isNotEmpty(message.getAcceptMan())){
            list.addFilter("acceptMan",Operator.eq, message.getAcceptMan());
        }
        if(Objects.nonNull(message.getMsgTitle()) && StringUtils.isNotEmpty(message.getMsgTitle())){
            list.addFilter("msgTitle",Operator.like, message.getMsgTitle());
        }
        if(Objects.nonNull(message.getIsRead()) && StringUtils.isNotEmpty(message.getIsRead())){
            list.addFilter("isRead",Operator.eq, message.getIsRead());
        }
        if(Objects.nonNull(message.getCreateDatetime())){
            list.addFilter("msgTime",Operator.ge, message.getCreateDatetime());
        }
        if(Objects.nonNull(message.getModifyDatetime())){
            list.addFilter("msgTime",Operator.le, message.getModifyDatetime());
        }
        OrderList orders = new OrderList();
        orders.addOrder("msgTime", OrderDirection.DESC);
        return messageRepository.read(list, orders, pager);
    }

    @Override
    public List<Message> queryGroupMessageForAccept(Message message, Pager pager) {
        QueryFilterList list = new QueryFilterList();
        if(Objects.nonNull(message.getSendMan()) && StringUtils.isNotEmpty(message.getSendMan())){
            list.addFilter("sendMan",Operator.eq, message.getSendMan());
        }
        if(Objects.nonNull(message.getAcceptMan()) && StringUtils.isNotEmpty(message.getAcceptMan())){
            list.addFilter("acceptMan",Operator.eq, message.getAcceptMan());
        }
        if(Objects.nonNull(message.getMsgTitle()) && StringUtils.isNotEmpty(message.getMsgTitle())){
            list.addFilter("msgTitle",Operator.like, message.getMsgTitle());
        }
        if(Objects.nonNull(message.getCreateDatetime())){
            list.addFilter("msgTime",Operator.ge, message.getCreateDatetime());
        }
        if(Objects.nonNull(message.getModifyDatetime())){
            list.addFilter("msgTime",Operator.le, message.getModifyDatetime());
        }
        OrderList orders = new OrderList();
//        orders.addOrder("isRead",OrderDirection.ASC);
        orders.addOrder("msgTime", OrderDirection.DESC);
        List<Message> messages = messageRepository.readPersist(list, orders, pager);
        if(Objects.nonNull(message.getIsRead()) && StringUtils.isNotEmpty(message.getIsRead())){
            Relation relation = new Relation();
            relation.setMainUuid(message.getAcceptMan());
            relation.setRelationType(Relation.RELATION_TYPE_5);
            List<Message> collect = messages.stream().filter(message1 -> {
                relation.setSubUuid(message1.getId());
                List<Relation> relations = relationService.queryGroupRelation(relation);
                //未读
                if("0".equals(message.getIsRead())){
                    if (Objects.isNull(relations) || relations.size() == 0) {
                        return true;
                    } else {
                        return false;
                    }
                //已读
                }else{
                    if (Objects.isNull(relations) || relations.size() == 0) {
                        return false;
                    } else {
                        return true;
                    }
                }
            }).map(message1 -> {
                message1.setIsRead(message.getIsRead());
                return message1;
            }).collect(Collectors.toList());
            return collect;
        }
        return messages;
    }

    @Override
    public List<Message> queryGroupMessage(Message message) {
        QueryFilterList list = new QueryFilterList();
        if(Objects.nonNull(message.getSendMan()) && StringUtils.isNotEmpty(message.getSendMan())){
            list.addFilter("sendMan",Operator.eq, message.getSendMan());
        }
        if(Objects.nonNull(message.getAcceptMan()) && StringUtils.isNotEmpty(message.getAcceptMan())){
            list.addFilter("acceptMan",Operator.eq, message.getAcceptMan());
        }
        if(Objects.nonNull(message.getMsgTitle()) && StringUtils.isNotEmpty(message.getMsgTitle())){
            list.addFilter("msgTitle",Operator.like, message.getMsgTitle());
        }
        if(Objects.nonNull(message.getIsRead()) && StringUtils.isNotEmpty(message.getIsRead())){
            list.addFilter("isRead",Operator.eq, message.getIsRead());
        }
        if(Objects.nonNull(message.getCreateDatetime())){
            list.addFilter("msgTime",Operator.ge, message.getCreateDatetime());
        }
        if(Objects.nonNull(message.getModifyDatetime())){
            list.addFilter("msgTime",Operator.le, message.getModifyDatetime());
        }
        return messageRepository.read(list);
    }

    @Override
    public String queryAdminRoleIdByRoleName(String roleName) {
        QueryFilterList list = new QueryFilterList();
        list.addFilter("name", Operator.eq, roleName);
        List<AdminRole> read = adminRoleRepository.read(list);
        //若存在此角色则返回角色ID
        if(Objects.nonNull(read) && read.size() == 1){
            return read.get(0).getId();
        }
        return null;
    }

    @Override
    public List<AdminRoleUser> queryAdminUserByRoleId(String roleId) {
        QueryFilterList list = new QueryFilterList();
        list.addFilter("roleId", Operator.eq, roleId);

        return adminRoleUserRepository.read(list);
    }
}
