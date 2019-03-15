package com.flyz.zwp.spacetime.model;

public class MyCellLocation {
    private String mLocTime;
	private double mOriginLng;
	private double mOriginLat;
	private double mNELng;
	private double mSWLng;
	private double mNELat;
	private double mSWLat;
	private int    mCellX;
	private int    mCellY;
	private String mLocDescribe;
    private int    coFLag;

    public String getmLocTime() {
        return mLocTime;
    }

    public void setmLocTime(String mLocTime) {
        this.mLocTime = mLocTime;
    }

    public String getmLocDescribe() {
        return mLocDescribe;
    }

    public void setmLocDescribe(String mLocDescribe) {
        this.mLocDescribe = mLocDescribe;
    }

    public int getCoFLag() {
        return coFLag;
    }

    public void setCoFLag(int coFLag) {
        this.coFLag = coFLag;
    }

    public double getmOriginLng() {
		return mOriginLng;
	}
	public void setmOriginLng(double mOriginLng) {
		this.mOriginLng = mOriginLng;
	}
	public double getmOriginLat() {
		return mOriginLat;
	}
	public void setmOriginLat(double mOriginLat) {
		this.mOriginLat = mOriginLat;
	}
	public double getmNELng() {
		return mNELng;
	}
	public void setmNELng(double mNELng) {
		this.mNELng = mNELng;
	}
	public double getmSWLng() {
		return mSWLng;
	}
	public void setmSWLng(double mSWLng) {
		this.mSWLng = mSWLng;
	}
	public double getmNELat() {
		return mNELat;
	}
	public void setmNELat(double mNELat) {
		this.mNELat = mNELat;
	}
	public double getmSWLat() {
		return mSWLat;
	}
	public void setmSWLat(double mSWLat) {
		this.mSWLat = mSWLat;
	}
	public int getmCellX() {
		return mCellX;
	}
	public void setmCellX(int mCellX) {
		this.mCellX = mCellX;
	}
	public int getmCellY() {
		return mCellY;
	}
	public void setmCellY(int mCellY) {
		this.mCellY = mCellY;
	}
	
}
