package com.flyz.zwp.spacetime.model;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Index;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by zwp12 on 2017/2/15.
 */
@Entity
public class ShareBlock {
    @Id(autoincrement = true)
    private Long id;
    @Index(unique = true)
    private String sId;

    private String ceId;
    private Integer sFlag;
    private String sTime;
    private Integer pokerCount;
    private Integer dpokerCount;
    private String uId;
    private String uNickName;

    private String sData1;
    private String sData2;
    private String sData3;

    @Generated(hash = 1453422454)
    public ShareBlock(Long id, String sId, String ceId, Integer sFlag, String sTime,
            Integer pokerCount, Integer dpokerCount, String uId, String uNickName,
            String sData1, String sData2, String sData3) {
        this.id = id;
        this.sId = sId;
        this.ceId = ceId;
        this.sFlag = sFlag;
        this.sTime = sTime;
        this.pokerCount = pokerCount;
        this.dpokerCount = dpokerCount;
        this.uId = uId;
        this.uNickName = uNickName;
        this.sData1 = sData1;
        this.sData2 = sData2;
        this.sData3 = sData3;
    }
    @Generated(hash = 769783091)
    public ShareBlock() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getSId() {
        return this.sId;
    }
    public void setSId(String sId) {
        this.sId = sId;
    }
    public Integer getSFlag() {
        return this.sFlag;
    }
    public void setSFlag(Integer sFlag) {
        this.sFlag = sFlag;
    }
    public String getSTime() {
        return this.sTime;
    }
    public void setSTime(String sTime) {
        this.sTime = sTime;
    }
    public Integer getPokerCount() {
        return this.pokerCount;
    }
    public void setPokerCount(Integer pokerCount) {
        this.pokerCount = pokerCount;
    }
    public Integer getDpokerCount() {
        return this.dpokerCount;
    }
    public void setDpokerCount(Integer dpokerCount) {
        this.dpokerCount = dpokerCount;
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
    public String getSData1() {
        return this.sData1;
    }
    public void setSData1(String sData1) {
        this.sData1 = sData1;
    }
    public String getSData2() {
        return this.sData2;
    }
    public void setSData2(String sData2) {
        this.sData2 = sData2;
    }
    public String getSData3() {
        return this.sData3;
    }
    public void setSData3(String sData3) {
        this.sData3 = sData3;
    }
    public String getCeId() {
        return this.ceId;
    }
    public void setCeId(String ceId) {
        this.ceId = ceId;
    }
    @Override
    public boolean equals(Object obj) {
        if(obj instanceof  ShareBlock){
            ShareBlock b = (ShareBlock) obj;
            return this.sId.equals(b.getSId());
        }
        return super.equals(obj);
    }
}
