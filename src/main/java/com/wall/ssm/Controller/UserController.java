package com.wall.ssm.Controller;

import com.wall.ssm.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserController {

    @Autowired
    private IUserService userService;

    @RequestMapping("list")
    public String list(Model model) {
        model.addAttribute("users",userService.queryAll());
        return "user/list";

    }
}
