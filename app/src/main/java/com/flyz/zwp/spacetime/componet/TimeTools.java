package com.flyz.zwp.spacetime.componet;

import java.text.SimpleDateFormat;
import java.util.Date;

public class TimeTools {
	private final static SimpleDateFormat sdf = 
				new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	public static String getNow(){
		return sdf.format(new Date());
	}
}
