package com.flyz.zwp.spacetime.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by zwp12 on 2017/2/15.
 */
@Entity
public class SpaceCell {
    @Id(autoincrement = true)
    private Long id;

    @Index(unique = true)
    private String ceId;
    private Integer cellX;
    private Integer cellY;

    private String ceTime;//kai tuo shi jian
    private String uId;// kai tuo yong hu
    private String uNickName;
    private Integer btCount;
    private Integer mR;
    private Integer mB;
    private Integer mG;

    private String ceDownTime;
    private Double ceDownLTime;
    private String ceUpTime;
    private Double ceUpLTime;

    private Integer nearFlag;

    @Generated(hash = 1668764580)
    public SpaceCell(Long id, String ceId, Integer cellX, Integer cellY,
            String ceTime, String uId, String uNickName, Integer btCount,
            Integer mR, Integer mB, Integer mG, String ceDownTime,
            Double ceDownLTime, String ceUpTime, Double ceUpLTime,
            Integer nearFlag) {
        this.id = id;
        this.ceId = ceId;
        this.cellX = cellX;
        this.cellY = cellY;
        this.ceTime = ceTime;
        this.uId = uId;
        this.uNickName = uNickName;
        this.btCount = btCount;
        this.mR = mR;
        this.mB = mB;
        this.mG = mG;
        this.ceDownTime = ceDownTime;
        this.ceDownLTime = ceDownLTime;
        this.ceUpTime = ceUpTime;
        this.ceUpLTime = ceUpLTime;
        this.nearFlag = nearFlag;
    }

    @Generated(hash = 91802210)
    public SpaceCell() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCeId() {
        return this.ceId;
    }

    public void setCeId(String ceId) {
        this.ceId = ceId;
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

    public String getCeTime() {
        return this.ceTime;
    }

    public void setCeTime(String ceTime) {
        this.ceTime = ceTime;
    }

    public String getUId() {
        return this.uId;
    }

    public void setUId(String uId) {
        this.uId = uId;
    }

    public Integer getBtCount() {
        return this.btCount;
    }

    public void setBtCount(Integer btCount) {
        this.btCount = btCount;
    }

    public Integer getMR() {
        return this.mR;
    }

    public void setMR(Integer mR) {
        this.mR = mR;
    }

    public Integer getMB() {
        return this.mB;
    }

    public void setMB(Integer mB) {
        this.mB = mB;
    }

    public Integer getMG() {
        return this.mG;
    }

    public void setMG(Integer mG) {
        this.mG = mG;
    }

    public String getCeDownTime() {
        return this.ceDownTime;
    }

    public void setCeDownTime(String ceDownTime) {
        this.ceDownTime = ceDownTime;
    }

    public Double getCeDownLTime() {
        return this.ceDownLTime;
    }

    public void setCeDownLTime(Double ceDownLTime) {
        this.ceDownLTime = ceDownLTime;
    }

    public String getCeUpTime() {
        return this.ceUpTime;
    }

    public void setCeUpTime(String ceUpTime) {
        this.ceUpTime = ceUpTime;
    }

    public Double getCeUpLTime() {
        return this.ceUpLTime;
    }

    public void setCeUpLTime(Double ceUpLTime) {
        this.ceUpLTime = ceUpLTime;
    }

    public Integer getNearFlag() {
        return this.nearFlag;
    }

    public void setNearFlag(Integer nearFlag) {
        this.nearFlag = nearFlag;
    }

    public String getUNickName() {
        return this.uNickName;
    }

    public void setUNickName(String uNickName) {
        this.uNickName = uNickName;
    }



}
