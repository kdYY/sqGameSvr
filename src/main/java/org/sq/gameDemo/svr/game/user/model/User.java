package org.sq.gameDemo.svr.game.user.model;

/**
 * 
 * 
 * @author wcyong
 * 
 * @date 2019-07-10
 */
public class User {
    private Integer id;

    private String name;

    private String password;

    /**
     * 实体id
     */
    private Integer typeId;

    private Integer senceId;

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

    public Integer getTypeId() {
        return typeId;
    }

    public void setTypeId(Integer typeId) {
        this.typeId = typeId;
    }

    public Integer getSenceId() {
        return senceId;
    }

    public void setSenceId(Integer senceId) {
        this.senceId = senceId;
    }
}