/*
 * 广州丰石科技有限公司拥有本软件版权2019并保留所有权利。
 * Copyright 2019, Guangzhou Rich Stone Data Technologies Company Limited,
 * All rights reserved.
 */

package org.sq.gameDemo.svr.service;

import org.sq.gameDemo.svr.dao.UserDao;
import org.sq.gameDemo.svr.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    private UserDao userDao;

    public User getUser(String name,String password){
        return userDao.getUser(name,password);
    }

    public List<User> listUser(){
        return userDao.listUser();
    }

    public void addUser(User user){
        userDao.addUser(user);
    }

    public void updateUser(User user){
        userDao.updateUser(user);
    }

    public void delUser(Integer id){
        userDao.delUser(id);
    }

}
