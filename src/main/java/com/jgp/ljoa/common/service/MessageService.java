package com.jgp.ljoa.common.service;/**
 * Created by Administrator on 2018/7/9.
 */

import com.jgp.ljoa.common.model.Message;
import com.jgp.security.admin.model.AdminRoleUser;
import com.jgp.sys.ui.Pager;

import java.util.List;

/**
 * 项目   ljoa
 * 作者   liujinxu
 * 时间   2018/7/9
 */
public interface MessageService {

    //保存及修改
    Message saveMessage(Message message);

    //删除单个
    void removeOneMessage(String id);

    //批量删除
    void removeSelectedMessage(String[] array);

    //查询单个
    Message queryOneMessage(String id);

    //条件查询
    List<Message> queryGroupMessage(Message message, Pager pager);

    //条件查询(已发送)
    List<Message> queryGroupMessageForSend(Message message, Pager pager);

    //条件查询（未发送）
    List<Message> queryGroupMessageForAccept(Message message, Pager pager);

    //条件查询
    List<Message> queryGroupMessage(Message message);

    //查询角色ID
    String queryAdminRoleIdByRoleName(String roleName);

    //查询此角色的所有adminRoleUser
    List<AdminRoleUser> queryAdminUserByRoleId(String roleId);
}
