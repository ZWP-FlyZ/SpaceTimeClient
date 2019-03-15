package com.flyz.zwp.spacetime;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by zwp12 on 2017/2/13.
 */
@Entity(indexes = {@Index(value = "uId",unique = true)},nameInDb = "USER")
public class User {

    @Id(autoincrement = true)
    private Long id;

    @NotNull
    private String uId;

    private String uNickName;
    private String uPassword;
    private Integer uAge;


    @Generated(hash = 1949321551)
    public User(Long id, @NotNull String uId, String uNickName, String uPassword,
            Integer uAge) {
        this.id = id;
        this.uId = uId;
        this.uNickName = uNickName;
        this.uPassword = uPassword;
        this.uAge = uAge;
    }

    @Generated(hash = 586692638)
    public User() {
    }


    public Integer getuAge() {
        return uAge;
    }

    public void setuAge(Integer uAge) {
        this.uAge = uAge;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getuNickName() {
        return uNickName;
    }

    public void setuNickName(String uNickName) {
        this.uNickName = uNickName;
    }

    public String getuPassword() {
        return uPassword;
    }

    public void setuPassword(String uPassword) {
        this.uPassword = uPassword;
    }

    public String getUId() {
        return this.uId;
    }

    public void setUId(String uId) {
        this.uId = uId;
    }

    public String getUNickName() {
        return this.uNickName;
    }

    public void setUNickName(String uNickName) {
        this.uNickName = uNickName;
    }

    public String getUPassword() {
        return this.uPassword;
    }

    public void setUPassword(String uPassword) {
        this.uPassword = uPassword;
    }

    public Integer getUAge() {
        return this.uAge;
    }

    public void setUAge(Integer uAge) {
        this.uAge = uAge;
    }
}
