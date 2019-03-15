package com.flyz.zwp.spacetime.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Transient;

import java.util.List;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by zwp12 on 2017/2/15.
 */
@Entity
public class MemChain {

    @Id(autoincrement = true)
    private Long id;

    @Index(unique = true)
    private String chId;
    @NotNull
    private String uId;

    private String chName;
    private String chDetail;

    private String scTime;
    private Double cslTime;

    private String seTime;
    private Double celTime;

    private Integer chFlag;

    private String chTime;
    private String chContextJSON;

    @Transient
    private List<MemFragmentHead> chContext;

    @Generated(hash = 467903958)
    public MemChain(Long id, String chId, @NotNull String uId, String chName,
            String chDetail, String scTime, Double cslTime, String seTime,
            Double celTime, Integer chFlag, String chTime, String chContextJSON) {
        this.id = id;
        this.chId = chId;
        this.uId = uId;
        this.chName = chName;
        this.chDetail = chDetail;
        this.scTime = scTime;
        this.cslTime = cslTime;
        this.seTime = seTime;
        this.celTime = celTime;
        this.chFlag = chFlag;
        this.chTime = chTime;
        this.chContextJSON = chContextJSON;
    }

    @Generated(hash = 277082755)
    public MemChain() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getChId() {
        return this.chId;
    }

    public void setChId(String chId) {
        this.chId = chId;
    }

    public String getUId() {
        return this.uId;
    }

    public void setUId(String uId) {
        this.uId = uId;
    }

    public String getChName() {
        return this.chName;
    }

    public void setChName(String chName) {
        this.chName = chName;
    }

    public String getChDetail() {
        return this.chDetail;
    }

    public void setChDetail(String chDetail) {
        this.chDetail = chDetail;
    }

    public String getScTime() {
        return this.scTime;
    }

    public void setScTime(String scTime) {
        this.scTime = scTime;
    }

    public Double getCslTime() {
        return this.cslTime;
    }

    public void setCslTime(Double cslTime) {
        this.cslTime = cslTime;
    }

    public String getSeTime() {
        return this.seTime;
    }

    public void setSeTime(String seTime) {
        this.seTime = seTime;
    }

    public Double getCelTime() {
        return this.celTime;
    }

    public void setCelTime(Double celTime) {
        this.celTime = celTime;
    }

    public Integer getChFlag() {
        return this.chFlag;
    }

    public void setChFlag(Integer chFlag) {
        this.chFlag = chFlag;
    }

    public String getChTime() {
        return this.chTime;
    }

    public void setChTime(String chTime) {
        this.chTime = chTime;
    }

    public String getChContextJSON() {
        return this.chContextJSON;
    }

    public void setChContextJSON(String chContextJSON) {
        this.chContextJSON = chContextJSON;
    }

    public List<MemFragmentHead> getChContext() {
        return chContext;
    }

    public void setChContext(List<MemFragmentHead> chContext) {
        this.chContext = chContext;
    }
}
