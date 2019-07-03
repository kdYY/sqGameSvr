/*
 * 广州丰石科技有限公司拥有本软件版权2019并保留所有权利。
 * Copyright 2019, Guangzhou Rich Stone Data Technologies Company Limited,
 * All rights reserved.
 */

package org.sq.gameDemo.svr.game.user.service;

import org.sq.gameDemo.svr.game.user.dao.UserMapper;
import org.sq.gameDemo.svr.game.user.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.sq.gameDemo.svr.game.user.model.UserExample;

import java.util.List;

/**
 * <b><code>UserService</code></b>
 * <p/>
 * Description
 * <p/>
 * <b>Creation Time:</b> 2019/3/3 22:31.
 *
 * @author 谢德奇
 * @since SpringBootDemo ${PROJECT_VERSION}
 */
@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;


    public User getUser(Integer id){
        UserExample userExample = new UserExample();
        userExample.createCriteria().andEntityidEqualTo(id);
        return userMapper.selectByExample(userExample).get(0);
    }

    public List<User> listUser(){
        return userMapper.selectByExample(new UserExample());
    }

    public void addUser(User user) {
        
    }



}
