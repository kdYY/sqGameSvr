package org.sq.gameDemo.svr.game.user.model;

/**
 * 
 * 
 * @author wcyong
 * 
 * @date 2019-07-09
 */
public class User {
    private Integer id;

    private String name;

    private String password;

    /**
     * 实体id
     */
    private Integer entityid;

    private Integer senceid;

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

    public Integer getEntityid() {
        return entityid;
    }

    public void setEntityid(Integer entityid) {
        this.entityid = entityid;
    }

    public Integer getSenceid() {
        return senceid;
    }

    public void setSenceid(Integer senceid) {
        this.senceid = senceid;
    }
}