package com.jgp.ljoa.common.controller;

import com.jgp.security.admin.model.AdminUser;
import com.jgp.security.admin.service.AdminUserService;
import com.jgp.sys.controller.JGPController;
import com.jgp.sys.ui.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

/**
 * Created by Administrator on 2018-07-11.
 */
@RestController
@RequestMapping("/ljoa/common/loginApiController")
public class LoginApiController extends JGPController {
    @Autowired
    private AdminUserService adminUserService;


    /*
    * 验证用户名和密码
    * */
    @RequestMapping("checkUserNameAndPassword")
    public Result checkUserNameAndPassword(String username, String password){
        AdminUser adminUser = adminUserService.queryUserByUserName(username);
        if(Objects.isNull(adminUser)){
            return ajaxRe(false);
        }
        Boolean aBoolean = adminUserService.checkPwd(adminUser, password);
        if(aBoolean){
            return ajaxRe(true);
        }else {
            return ajaxRe(false);
        }
    }
}
