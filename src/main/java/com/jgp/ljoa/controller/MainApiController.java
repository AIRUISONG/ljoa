package com.jgp.ljoa.controller;


import com.jgp.security.admin.model.AdminUser;
import com.jgp.security.admin.service.AdminUserService;
import com.jgp.sys.controller.JGPController;
import com.jgp.sys.ui.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * Created by Administrator on 2018/9/29.
 */
/*
作者  SSF
时间   2018/9/29
*/
@RestController
@RequestMapping("/sys/MainApiController")
public class MainApiController extends JGPController {

    @Autowired
    private AdminUserService adminUserService;

    /*
    * 用户名监测
    * */
    @RequestMapping("checkUserNameAndPassword")
    public Result checkUserNameAndPassword(String username, String password){
        AdminUser adminUser = adminUserService.queryUserByUserName(username);
        if(Objects.isNull(adminUser)){
            return ajaxRe(false);
        }
//        Boolean b = adminUserService.checkPwd(adminUser, password);
        Boolean aBoolean = adminUserService.checkPwd(adminUser, password);
        return ajaxRe(aBoolean).addData("flag", aBoolean);
    }
}
