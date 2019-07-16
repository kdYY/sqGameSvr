/*
 * 广州丰石科技有限公司拥有本软件版权2019并保留所有权利。
 * Copyright 2019, Guangzhou Rich Stone Data Technologies Company Limited,
 * All rights reserved.
 */

package org.sq.gameDemo.svr.game.user.service;

import org.springframework.transaction.annotation.Transactional;
import org.sq.gameDemo.common.proto.UserProto;
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
 * @since SpringBootDemo ${PROJECT_VERSION}
 */
@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;


    public User getUser(Integer id){
        return userMapper.selectByPrimaryKey(id);
    }

    public User getUser(UserProto.User user){
        UserExample userExample = new UserExample();
        userExample.createCriteria().andNameEqualTo(user.getName()).andPasswordEqualTo(user.getPassword());
        List<User> users = userMapper.selectByExample(userExample);
        if(users.size() != 0) {
            return users.get(0);
        }
        return null;
    }

    public List<User> listUser(){
        return userMapper.selectByExample(new UserExample());
    }

    @Transactional
    public int addUser(User user) throws Exception{
        return userMapper.insertSelective(user);
    }

    public User getUserName(String name) {
        UserExample example = new UserExample();
        example.createCriteria().andNameEqualTo(name);
        List<User> userList = userMapper.selectByExample(example);
        if(userList.size() != 0) {
            return userList.get(0);
        }
        return null;
    }

    public boolean userNameExist(String name) {
        if(getUserName(name) != null) {
            return true;
        }
        return false;
    }


}
