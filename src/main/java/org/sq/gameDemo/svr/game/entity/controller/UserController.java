/*
 * 广州丰石科技有限公司拥有本软件版权2019并保留所有权利。
 * Copyright 2019, Guangzhou Rich Stone Data Technologies Company Limited,
 * All rights reserved.
 */

package org.sq.gameDemo.svr.game.entity.controller;

import org.springframework.stereotype.Controller;
import org.sq.gameDemo.common.GameOrderMapping;
import org.sq.gameDemo.common.Result;
import org.sq.gameDemo.svr.game.entity.model.User;
import org.sq.gameDemo.svr.game.entity.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

/**
 * <b><code>UserController</code></b>
 * <p/>
 * Description
 * <p/>
 * <b>Creation Time:</b> 2019/3/3 22:34.
 *
 * @author
 * @since SpringBootDemo ${PROJECT_VERSION}
 */
@Controller
public class UserController {
    @Autowired
    private UserService userService;


    @GameOrderMapping("site")
    @GetMapping("listUser")
    public String listUser() {
        return userService.listUser().stream().map(User::toString).collect(Collectors.joining());
    }

}
