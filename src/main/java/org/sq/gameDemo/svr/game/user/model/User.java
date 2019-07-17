package org.sq.gameDemo.svr.game.user.model;

import org.sq.gameDemo.svr.common.protoUtil.ProtoField;

/**
 * 
 * 
 * @author wcyong
 * 
 * @date 2019-07-17
 */
public class User {
    private Integer id;

    private String name;

    private String password;

    @ProtoField(Ignore = true)
    private String token;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password == null ? null : password.trim();
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token == null ? null : token.trim();
    }
}