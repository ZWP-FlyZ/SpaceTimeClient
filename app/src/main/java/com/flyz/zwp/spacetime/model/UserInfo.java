package com.flyz.zwp.spacetime.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by zwp12 on 2017/2/16.
 */

@Entity
public class UserInfo {

    @Id
    private Long id;
    @Index(unique = true)
    private String uId;
    private String uNickName;
    private String uPassword;
    private Integer uSex;
    private String uBri;
    private String uSign;

    private Integer cellX;
    private Integer cellY;

    private String crTime;
    private String crDesc;
    private Integer campFlag;

    private String lRecJson ;
    private String gRecJson;
    @Generated(hash = 780337683)
    public UserInfo(Long id, String uId, String uNickName, String uPassword,
            Integer uSex, String uBri, String uSign, Integer cellX, Integer cellY,
            String crTime, String crDesc, Integer campFlag, String lRecJson,
            String gRecJson) {
        this.id = id;
        this.uId = uId;
        this.uNickName = uNickName;
        this.uPassword = uPassword;
        this.uSex = uSex;
        this.uBri = uBri;
        this.uSign = uSign;
        this.cellX = cellX;
        this.cellY = cellY;
        this.crTime = crTime;
        this.crDesc = crDesc;
        this.campFlag = campFlag;
        this.lRecJson = lRecJson;
        this.gRecJson = gRecJson;
    }
    @Generated(hash = 1279772520)
    public UserInfo() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
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
    public Integer getUSex() {
        return this.uSex;
    }
    public void setUSex(Integer uSex) {
        this.uSex = uSex;
    }
    public String getUBri() {
        return this.uBri;
    }
    public void setUBri(String uBri) {
        this.uBri = uBri;
    }
    public String getUSign() {
        return this.uSign;
    }
    public void setUSign(String uSign) {
        this.uSign = uSign;
    }
    public Integer getCellX() {
        return this.cellX;
    }
    public void setCellX(Integer cellX) {
        this.cellX = cellX;
    }
    public Integer getCellY() {
        return this.cellY;
    }
    public void setCellY(Integer cellY) {
        this.cellY = cellY;
    }
    public Integer getCampFlag() {
        return this.campFlag;
    }
    public void setCampFlag(Integer campFlag) {
        this.campFlag = campFlag;
    }
    public String getLRecJson() {
        return this.lRecJson;
    }
    public void setLRecJson(String lRecJson) {
        this.lRecJson = lRecJson;
    }
    public String getGRecJson() {
        return this.gRecJson;
    }
    public void setGRecJson(String gRecJson) {
        this.gRecJson = gRecJson;
    }
    public String getCrTime() {
        return this.crTime;
    }
    public void setCrTime(String crTime) {
        this.crTime = crTime;
    }
    public String getCrDesc() {
        return this.crDesc;
    }
    public void setCrDesc(String crDesc) {
        this.crDesc = crDesc;
    }

}
