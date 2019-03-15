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

@Entity(indexes ={@Index(value = "fId",unique = true)})
public class MemFragmentBody {

    @Id(autoincrement = true)
    private Long id;
    @NotNull
    private String fId ;        //Fragment id

    @NotNull
    private String uId;         //user id

    private String cTime;       //create time;
    private Integer tFLag;      //time flag
    private String sTime;       //start time;
    private String eTime;       //end time;
    private Double slTime;      //
    private Double elTime;      //

    private String ftDetail;//时间描述

    private Double fLatitude;   //relation Latitude
    private Double fLongitude;  //relation Longitude
    private String fpDetail;    //relation location detail
    private Integer coFlag;     //cell
    private Integer cellX;      //cellX
    private Integer cellY;      //cellY

    @NotNull
    private String fTitle;      // title of the memory fragment
    private String bodyItemsJSON ;
    @Transient
    private List<BodyItem> bodyItemList;
    @Generated(hash = 1194922980)
    public MemFragmentBody(Long id, @NotNull String fId, @NotNull String uId,
            String cTime, Integer tFLag, String sTime, String eTime, Double slTime,
            Double elTime, String ftDetail, Double fLatitude, Double fLongitude,
            String fpDetail, Integer coFlag, Integer cellX, Integer cellY,
            @NotNull String fTitle, String bodyItemsJSON) {
        this.id = id;
        this.fId = fId;
        this.uId = uId;
        this.cTime = cTime;
        this.tFLag = tFLag;
        this.sTime = sTime;
        this.eTime = eTime;
        this.slTime = slTime;
        this.elTime = elTime;
        this.ftDetail = ftDetail;
        this.fLatitude = fLatitude;
        this.fLongitude = fLongitude;
        this.fpDetail = fpDetail;
        this.coFlag = coFlag;
        this.cellX = cellX;
        this.cellY = cellY;
        this.fTitle = fTitle;
        this.bodyItemsJSON = bodyItemsJSON;
    }
    @Generated(hash = 1369365161)
    public MemFragmentBody() {
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
    public String getCTime() {
        return this.cTime;
    }
    public void setCTime(String cTime) {
        this.cTime = cTime;
    }
    public Integer getTFLag() {
        return this.tFLag;
    }
    public void setTFLag(Integer tFLag) {
        this.tFLag = tFLag;
    }
    public String getSTime() {
        return this.sTime;
    }
    public void setSTime(String sTime) {
        this.sTime = sTime;
    }
    public String getETime() {
        return this.eTime;
    }
    public void setETime(String eTime) {
        this.eTime = eTime;
    }
    public Double getSlTime() {
        return this.slTime;
    }
    public void setSlTime(Double slTime) {
        this.slTime = slTime;
    }
    public Double getElTime() {
        return this.elTime;
    }
    public void setElTime(Double elTime) {
        this.elTime = elTime;
    }
    public String getFtDetail() {
        return this.ftDetail;
    }
    public void setFtDetail(String ftDetail) {
        this.ftDetail = ftDetail;
    }
    public Double getFLatitude() {
        return this.fLatitude;
    }
    public void setFLatitude(Double fLatitude) {
        this.fLatitude = fLatitude;
    }
    public Double getFLongitude() {
        return this.fLongitude;
    }
    public void setFLongitude(Double fLongitude) {
        this.fLongitude = fLongitude;
    }
    public String getFpDetail() {
        return this.fpDetail;
    }
    public void setFpDetail(String fpDetail) {
        this.fpDetail = fpDetail;
    }
    public Integer getCoFlag() {
        return this.coFlag;
    }
    public void setCoFlag(Integer coFlag) {
        this.coFlag = coFlag;
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
    public String getFTitle() {
        return this.fTitle;
    }
    public void setFTitle(String fTitle) {
        this.fTitle = fTitle;
    }
    public String getBodyItemsJSON() {
        return this.bodyItemsJSON;
    }
    public void setBodyItemsJSON(String bodyItemsJSON) {
        this.bodyItemsJSON = bodyItemsJSON;
    }

    public List<BodyItem> getBodyItemList() {
        return bodyItemList;
    }

    public void setBodyItemList(List<BodyItem> bodyItemList) {
        this.bodyItemList = bodyItemList;
    }
}
