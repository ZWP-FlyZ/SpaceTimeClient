package com.flyz.zwp.spacetime.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by zwp12 on 2017/2/15.
 */

@Entity
public class MemFragmentHead implements Cloneable {
    @Id(autoincrement = true)
    private Long id;

    @Index(unique = true)
    private String fId;
    @NotNull
    private String uId;
    @NotNull
    private String fTitle;

    private String cTime;
    private String ftDetail;
    private Double slTime;

    private String fpDetail;
    private Integer fFlag;
    @Generated(hash = 342526112)
    public MemFragmentHead(Long id, String fId, @NotNull String uId,
            @NotNull String fTitle, String cTime, String ftDetail, Double slTime,
            String fpDetail, Integer fFlag) {
        this.id = id;
        this.fId = fId;
        this.uId = uId;
        this.fTitle = fTitle;
        this.cTime = cTime;
        this.ftDetail = ftDetail;
        this.slTime = slTime;
        this.fpDetail = fpDetail;
        this.fFlag = fFlag;
    }
    @Generated(hash = 1647693230)
    public MemFragmentHead() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getFId() {
        return this.fId;
    }
    public void setFId(String fId) {
        this.fId = fId;
    }
    public String getUId() {
        return this.uId;
    }
    public void setUId(String uId) {
        this.uId = uId;
    }
    public String getFTitle() {
        return this.fTitle;
    }
    public void setFTitle(String fTitle) {
        this.fTitle = fTitle;
    }
    public String getCTime() {
        return this.cTime;
    }
    public void setCTime(String cTime) {
        this.cTime = cTime;
    }
    public String getFtDetail() {
        return this.ftDetail;
    }
    public void setFtDetail(String ftDetail) {
        this.ftDetail = ftDetail;
    }
    public Double getSlTime() {
        return this.slTime;
    }
    public void setSlTime(Double slTime) {
        this.slTime = slTime;
    }
    public String getFpDetail() {
        return this.fpDetail;
    }
    public void setFpDetail(String fpDetail) {
        this.fpDetail = fpDetail;
    }
    public Integer getFFlag() {
        return this.fFlag;
    }
    public void setFFlag(Integer fFlag) {
        this.fFlag = fFlag;
    }

    @Override
    public MemFragmentHead clone()   {
        MemFragmentHead head = null;
        try {
            head = (MemFragmentHead)super.clone();
        }catch (CloneNotSupportedException e){
            e.printStackTrace();
        }
        return head;
    }
}
