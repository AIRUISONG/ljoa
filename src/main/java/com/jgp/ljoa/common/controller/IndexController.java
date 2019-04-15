package com.jgp.ljoa.common.controller;


import com.jgp.sys.controller.JGPController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Administrator on 2018-07-11.
 */
@Controller
@RequestMapping("/ljoa/common/indexController")
public class IndexController extends JGPController {

    @RequestMapping("index")
    public String index(){
        return page("");
    }
}
