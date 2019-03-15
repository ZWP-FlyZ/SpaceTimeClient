package com.flyz.zwp.spacetime.componet;

import com.flyz.zwp.spacetime.model.MyCellLocation;

public class MyConversion {
	public final static double MY_PI   = Math.PI;
	public final static double EARTH_R = 6378137.0;
	public final static double EARTH_HALF_CIR = EARTH_R*MY_PI;
	public final static double MAX_LONGITUDE_IN_DEGREE = 180.0;
	public final static double MAX_LATITUDE_IN_DEGREE = 85.051128;
	public final static double CONVER_ORIGIN_LONGITUDE_IN_DEGREE = 72.0;
	public final static double CELL_DISTANSE = 2000.0;
	
	/**
	 * 
	 * @param degree 角度
	 * @return 弧度
	 */
	public static  double degree2radian(double degree)
	{
		return degree/180.0*MY_PI;
	}
	
	/**
	 * 
	 * @param radian
	 * @return
	 */
	
	public static  double radian2degree(double radian)
	{
		return radian/MY_PI*180.0;
	}
	
	public  static double lng2MctX(double lngInRadian)
	{
		return lngInRadian * EARTH_R;
	}
	
	public static double lat2MctY(double latInRadian) {
		if(latInRadian>=0.0&&latInRadian<0.00001)
			return 0.0;
		//return EARTH_R*Math.log(Math.tan(0.25*MY_PI+0.5*latInRadian));
		return EARTH_R*Math.log(Math.tan(latInRadian)+1.0/Math.cos(latInRadian));
	}
	
	public  static double mctX2Lng(double mctX)
	{
		return mctX / EARTH_R;
	}
	
	public static double mctY2Lat(double mctY) {
		return Math.atan(Math.sinh(mctY/EARTH_R));
	}
	
	public static MyCellLocation latlng2Mct(double lat, double lng) {
		MyCellLocation loc = new MyCellLocation();
		loc.setmOriginLat(lat);
		loc.setmOriginLng(lng);
		
		double tmp = lng2MctX(degree2radian(lng-CONVER_ORIGIN_LONGITUDE_IN_DEGREE));
		loc.setmCellX((int)Math.floor(tmp/CELL_DISTANSE));
		tmp = lat2MctY(degree2radian(lat));
		
		loc.setmCellY((int)Math.floor(tmp/CELL_DISTANSE));
		loc.setmSWLng(
				radian2degree(
						mctX2Lng(loc.getmCellX()*CELL_DISTANSE)
				)+CONVER_ORIGIN_LONGITUDE_IN_DEGREE
		);
		loc.setmSWLat(
				radian2degree(
						mctY2Lat(loc.getmCellY()*CELL_DISTANSE))
				);
		loc.setmNELng(
				radian2degree(
						mctX2Lng(loc.getmCellX()*CELL_DISTANSE+CELL_DISTANSE)
						)+CONVER_ORIGIN_LONGITUDE_IN_DEGREE
				);
		loc.setmNELat(
				radian2degree(
						mctY2Lat(loc.getmCellY()*CELL_DISTANSE+CELL_DISTANSE))
				);	
		return loc;
		
	}
	
}
