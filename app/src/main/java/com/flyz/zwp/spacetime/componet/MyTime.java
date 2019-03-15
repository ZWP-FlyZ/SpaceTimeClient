package com.flyz.zwp.spacetime.componet;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import  com.flyz.zwp.spacetime.componet.TimeEnum.*;


public class MyTime {
	private TimeOri mTimeOri; 
	private TimeYearUint mTimeYearUint;
	private int mTimeFLag = 0x0;
	private int[] mTimeData = null;
	private MyTime myTime;
	private MyTime(){
		mTimeData =  new int[TIME_MORE_UNIT_C];
        Arrays.fill(mTimeData, 0);
        mTimeFLag = 0x0;
        myTime= this;
	}
	private final static String spilt = ":";
	private final static int TIME_MORE_UNIT_C = 7;
	
	public static MyTime build(){
		MyTime time = new MyTime();
        time.setTimeOri(TimeOri.A_D)
                .setTimeYearUint(TimeYearUint.YEAR)
                .setYear(1);
		return time;
	}
	
	public static MyTime buildNowTime(){
		MyTime time = new MyTime();
		Calendar c = Calendar.getInstance();
		time.setTimeOri(TimeOri.A_D);
		time.setTimeYearUint(TimeYearUint.YEAR);
		time.setYear(c.get(Calendar.YEAR));
		time.setMonth(c.get(Calendar.MONTH)+1);
		time.setDayOfMonth(c.get(Calendar.DAY_OF_MONTH));
		time.setHour(c.get(Calendar.HOUR_OF_DAY));
		time.setMinute(c.get(Calendar.MINUTE));
		time.setSecond(c.get(Calendar.SECOND));
		return time;
	}
	

	public void clear(){
        Arrays.fill(mTimeData, 0);
        mTimeFLag = 0x0;
        myTime.setTimeOri(TimeOri.A_D)
                .setTimeYearUint(TimeYearUint.YEAR)
                .setYear(1);

    }
	
	public MyTime setTimeOri(TimeOri ori)
	{
		if(ori == null)
			mTimeOri=TimeOri.A_D;
		else
			mTimeOri = ori;
		
		mTimeFLag = 
				(mTimeFLag)&(~0x0<<2)| mTimeOri.getIndex(); 
		
		return myTime;
	}
	
	
	private void setTimeOri(int oriIndex)
	{
		
			switch(oriIndex)
			{
			case 2:mTimeOri = TimeOri.B_C;break;
			case 3:mTimeOri = TimeOri.AGO;break;
			default:mTimeOri=TimeOri.A_D;
			}
	
		mTimeFLag = 
				(mTimeFLag)&(~0x0<<2)| mTimeOri.getIndex(); 
		
		
	}
	
	public MyTime setTimeYearUint(TimeYearUint timeYearUnit)
	{
		if(timeYearUnit==null)
			mTimeYearUint = TimeYearUint.YEAR;
		else
			mTimeYearUint = timeYearUnit;
		
		mTimeFLag = 
				(mTimeFLag)&(~0x1c)| (mTimeYearUint.getIndex()<<2); 
		return myTime;
	}
	
	private  MyTime setTimeYearUint(int timeYearUnitIndex)
	{
		
		switch(timeYearUnitIndex)
		{
            case 1:mTimeYearUint = TimeYearUint.CENTURY; break;
			case 2:mTimeYearUint = TimeYearUint.HUNDRED_YEAR; break;
			case 3:mTimeYearUint = TimeYearUint.THOUSAND_YEAR; break;
			case 4:mTimeYearUint = TimeYearUint.TEN_THOUSAND_YEAR; break;
			case 5:mTimeYearUint = TimeYearUint.MILLON_YEAR; break;
            case 6:mTimeYearUint = TimeYearUint.TEN_MILLON_YEAR; break;
			case 7:mTimeYearUint = TimeYearUint.HUNDRED_MILLON_YEAR; break;
			default:mTimeYearUint = TimeYearUint.YEAR; 
		}
			
		mTimeFLag = 
				(mTimeFLag)&(~0x1c)| (mTimeYearUint.getIndex()<<2); 
		return myTime;
	}
	
	
	/**
	 * 
	 * @param year 
	 * @return if year >0  yeardata = year
	 * 		    else   yeardata = 1 
	 */
	public MyTime setYear(int year)
	{
		if(year>0)
			{
				mTimeData[TimeMoreUint.YEAR.getIndex()]=year;
				mTimeFLag = mTimeFLag | 1<<(5+TimeMoreUint.YEAR.getIndex());
			}
		else
			mTimeData[TimeMoreUint.YEAR.getIndex()] = 1;
		return myTime;
			
	}
	/**
	 * 
	 * @param month
	 * @return if month <1|| month >12 monthdata = 1 
	 */
	public MyTime setMonth(int month)
	{
		if(month>0&&month<13)
			{
				mTimeData[TimeMoreUint.MONTH.getIndex()] = month;
				mTimeFLag = mTimeFLag | 1<<(5+TimeMoreUint.MONTH.getIndex());
			}
		else
			mTimeData[TimeMoreUint.MONTH.getIndex()] = 1;
		return myTime;
	}
	
	public MyTime setDayOfMonth(int day)
	{
		if(day>0&&day<32)
			{
				mTimeData[TimeMoreUint.DAY_OF_MONTH.getIndex()] = day;
				mTimeFLag = mTimeFLag | 1<<(5+TimeMoreUint.DAY_OF_MONTH.getIndex());
			}
		else
			mTimeData[TimeMoreUint.DAY_OF_MONTH.getIndex()] = 1;
		return myTime;
	}
	public MyTime setHour(int hour)
	{
		if(hour>-1&&hour<24)
		{
				mTimeData[TimeMoreUint.HOUR.getIndex()] = hour;
				mTimeFLag = mTimeFLag | 1<<(5+TimeMoreUint.HOUR.getIndex());
		}
		else
			mTimeData[TimeMoreUint.HOUR.getIndex()] = 0;
		return myTime;
	}
	
	public MyTime setMinute(int min)
	{
		if(min>-1&&min<60)
		{
				mTimeData[TimeMoreUint.MINUTE.getIndex()] = min;
				mTimeFLag = mTimeFLag | 1<<(5+TimeMoreUint.MINUTE.getIndex());
		}
		else
			mTimeData[TimeMoreUint.MINUTE.getIndex()] = 0;
		return myTime;
	}
	public MyTime setSecond(int second)
	{
		if(second>-1&&second<60)
		{
				mTimeData[TimeMoreUint.SECOND.getIndex()] = second;
				mTimeFLag = mTimeFLag | 1<<(5+TimeMoreUint.SECOND.getIndex());
		}
		else
			mTimeData[TimeMoreUint.SECOND.getIndex()] = 0;
		return myTime;
	}
	public MyTime setNianDai(int nianDai)
	{
		if(nianDai>-1&&nianDai<10)
		{
				mTimeData[TimeMoreUint.NIAN_DAI.getIndex()] = nianDai;
				mTimeFLag = mTimeFLag | 1<<(5+TimeMoreUint.NIAN_DAI.getIndex());
		}
		else
			mTimeData[TimeMoreUint.SECOND.getIndex()] = 0;
		return myTime;
	}

	public void setMoreDataByIndex(int index,int value){
        mTimeData[index] = value;
        mTimeFLag = mTimeFLag | 1<<(5+index);
    }
	
	
	
	
	public int getTimeFlag()
	{
		return mTimeFLag;
	}
	
	
	public String getTimeDetail()
	{
		StringBuffer sb = new StringBuffer();
		int tmp=mTimeFLag;
		for(int i=0;i<TIME_MORE_UNIT_C;i++)
		{
			if(((tmp>>(i+5))&1)==1)
			{
				sb.append(spilt);
				sb.append(mTimeData[i]);
			}	
		}
		return sb.toString();
	}
	
	private boolean haveMoreUnit(TimeMoreUint u)
	{
		if(u==null)
			return false;
		else
		{
			boolean t = ((mTimeFLag>>(5+u.getIndex()))&0x1) == 1;
			return t;
		}
	}

    public String toDescribe() {
        StringBuffer sb = new StringBuffer();
        sb.append(mTimeOri.getName());
        sb.append(mTimeData[TimeMoreUint.YEAR.getIndex()]);
        sb.append(mTimeYearUint.getName());
        if(mTimeOri==TimeOri.A_D)
        {
            //nian dai
            if(mTimeYearUint==TimeYearUint.CENTURY&&haveMoreUnit(TimeMoreUint.NIAN_DAI))
                sb.append(" "+mTimeData[TimeMoreUint.NIAN_DAI.getIndex()]
                        +TimeMoreUint.NIAN_DAI.getName());
            // nian
            if(mTimeYearUint==TimeYearUint.YEAR)
            {
                if(haveMoreUnit(TimeMoreUint.MONTH))
                    sb.append(mTimeData[TimeMoreUint.MONTH.getIndex()]
                            +TimeMoreUint.MONTH.getName());

                if(haveMoreUnit(TimeMoreUint.DAY_OF_MONTH))
                    sb.append(mTimeData[TimeMoreUint.DAY_OF_MONTH.getIndex()]
                            +TimeMoreUint.DAY_OF_MONTH.getName());

                if(haveMoreUnit(TimeMoreUint.HOUR)){
                    sb.append("  ");
                    sb.append(mTimeData[TimeMoreUint.HOUR.getIndex()]
                            +TimeMoreUint.HOUR.getName());
                }


                if(haveMoreUnit(TimeMoreUint.MINUTE))
                    sb.append(mTimeData[TimeMoreUint.MINUTE.getIndex()]
                            +TimeMoreUint.MINUTE.getName());

                if(haveMoreUnit(TimeMoreUint.SECOND))
                    sb.append(mTimeData[TimeMoreUint.SECOND.getIndex()]
                            +TimeMoreUint.SECOND.getName());
            }

        }else if(mTimeOri==TimeOri.B_C)//gong yuan qian
        {
            //nian dai
            if(mTimeYearUint==TimeYearUint.CENTURY&&haveMoreUnit(TimeMoreUint.NIAN_DAI))
                sb.append(" "+mTimeData[TimeMoreUint.NIAN_DAI.getIndex()]
                        +TimeMoreUint.NIAN_DAI.getName());
            // nian
            if(mTimeYearUint==TimeYearUint.YEAR)
            {
                if(haveMoreUnit(TimeMoreUint.MONTH))
                    sb.append(mTimeData[TimeMoreUint.MONTH.getIndex()]
                            +TimeMoreUint.MONTH.getName());

                if(haveMoreUnit(TimeMoreUint.DAY_OF_MONTH))
                    sb.append(mTimeData[TimeMoreUint.DAY_OF_MONTH.getIndex()]
                            +TimeMoreUint.DAY_OF_MONTH.getName());
            }


        }else if(mTimeOri==TimeOri.AGO)// jujing
        {


        }

        return sb.toString();
    }

    public static MyTime getMyTimeByDetail(int tFlag,String detail) {
        MyTime time = null;
        if(detail == null ) return time;
        try {
            String[] ss = detail.split(spilt);
            int tmpdata=0,tmpdata2 = 0, i =1;
            time = MyTime.build();
            tmpdata = tFlag&0x3;
            time.setTimeOri(tmpdata);
            tmpdata = (tFlag >>2)&0x7;
            time.setTimeYearUint(tmpdata);

            tmpdata = (tFlag >>5)&0x7f;

            if(((tmpdata>>TimeMoreUint.SECOND.getIndex())&0x1) == 1)
            {
                tmpdata2 = Integer.parseInt(ss[i++]);
                time.setSecond(tmpdata2);
            }
            if(((tmpdata>>TimeMoreUint.MINUTE.getIndex())&0x1) == 1)
            {
                tmpdata2 = Integer.parseInt(ss[i++]);
                time.setMinute(tmpdata2);
            }
            if(((tmpdata>>TimeMoreUint.HOUR.getIndex())&0x1) == 1)
            {
                tmpdata2 = Integer.parseInt(ss[i++]);
                time.setHour(tmpdata2);
            }
            if(((tmpdata>>TimeMoreUint.DAY_OF_MONTH.getIndex())&0x1) == 1)
            {
                tmpdata2 = Integer.parseInt(ss[i++]);
                time.setDayOfMonth(tmpdata2);
            }
            if(((tmpdata>>TimeMoreUint.MONTH.getIndex())&0x1) == 1)
            {
                tmpdata2 = Integer.parseInt(ss[i++]);
                time.setMonth(tmpdata2);
            }
            if(((tmpdata>>TimeMoreUint.YEAR.getIndex())&0x1) == 1)
            {

                tmpdata2 = Integer.parseInt(ss[i++]);
                time.setYear(tmpdata2);
            }
            if(((tmpdata>>TimeMoreUint.NIAN_DAI.getIndex())&0x1) == 1)
            {
                tmpdata2 = Integer.parseInt(ss[i]);
                time.setNianDai(tmpdata2);
            }


        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            time = null;
        }
        return time;
    }

    public double getLogcTime(){
        double tmp = 0.0;
        if(mTimeOri==TimeOri.A_D){

            if(mTimeYearUint==TimeYearUint.CENTURY){
                tmp = (mTimeData[TimeMoreUint.YEAR.getIndex()]-1) * 100.0;
                if(haveMoreUnit(TimeMoreUint.NIAN_DAI))
                    tmp+= mTimeData[TimeMoreUint.NIAN_DAI.getIndex()]*10.0;
            }
            else if(mTimeYearUint==TimeYearUint.YEAR){
                tmp = mTimeData[TimeMoreUint.YEAR.getIndex()] * 1.0;
                if(haveMoreUnit(TimeMoreUint.MONTH))
                    tmp+= mTimeData[TimeMoreUint.MONTH.getIndex()]/Math.pow(10.0, 2.0);
                if(haveMoreUnit(TimeMoreUint.DAY_OF_MONTH))
                    tmp+= mTimeData[TimeMoreUint.DAY_OF_MONTH.getIndex()]/Math.pow(10.0, 4.0);
                if(haveMoreUnit(TimeMoreUint.HOUR))
                    tmp+= mTimeData[TimeMoreUint.HOUR.getIndex()]/Math.pow(10.0, 6.0);
                if(haveMoreUnit(TimeMoreUint.MINUTE))
                    tmp+= mTimeData[TimeMoreUint.MINUTE.getIndex()]/Math.pow(10.0, 8.0);
                if(haveMoreUnit(TimeMoreUint.SECOND))
                    tmp+= mTimeData[TimeMoreUint.SECOND.getIndex()]/Math.pow(10.0, 10.0);
            }
        }//end if A_D
        else if(mTimeOri==TimeOri.B_C){
            if(mTimeYearUint==TimeYearUint.CENTURY){
                tmp = (mTimeData[TimeMoreUint.YEAR.getIndex()]) * 100.0;
                if(haveMoreUnit(TimeMoreUint.NIAN_DAI))
                    tmp+= mTimeData[TimeMoreUint.NIAN_DAI.getIndex()]*10.0;
            }//
            else if(mTimeYearUint==TimeYearUint.YEAR){
                tmp = mTimeData[TimeMoreUint.YEAR.getIndex()] * 1.0;
                if(haveMoreUnit(TimeMoreUint.MONTH))
                    tmp+= mTimeData[TimeMoreUint.MONTH.getIndex()]/Math.pow(10.0, 2.0);
                if(haveMoreUnit(TimeMoreUint.DAY_OF_MONTH))
                    tmp+= mTimeData[TimeMoreUint.DAY_OF_MONTH.getIndex()]/Math.pow(10.0, 4.0);
            }
            else if(mTimeYearUint==TimeYearUint.HUNDRED_YEAR)
                tmp = (mTimeData[TimeMoreUint.YEAR.getIndex()]) * Math.pow(10.0, 2.0);
            else if(mTimeYearUint==TimeYearUint.THOUSAND_YEAR)
                tmp = (mTimeData[TimeMoreUint.YEAR.getIndex()]) * Math.pow(10.0, 3.0);
            else if(mTimeYearUint==TimeYearUint.TEN_THOUSAND_YEAR)
                tmp = (mTimeData[TimeMoreUint.YEAR.getIndex()]) * Math.pow(10.0, 4.0);
            else if(mTimeYearUint==TimeYearUint.MILLON_YEAR)
                tmp = (mTimeData[TimeMoreUint.YEAR.getIndex()]) * Math.pow(10.0, 6.0);
            else if(mTimeYearUint==TimeYearUint.TEN_MILLON_YEAR)
                tmp = (mTimeData[TimeMoreUint.YEAR.getIndex()]) * Math.pow(10.0, 7.0);
            else if(mTimeYearUint==TimeYearUint.HUNDRED_MILLON_YEAR)
                tmp = (mTimeData[TimeMoreUint.YEAR.getIndex()]) * Math.pow(10.0, 8.0);
            tmp = -tmp;

        }//end if B_C
        else if(mTimeOri==TimeOri.AGO){
            double now = ((int)buildNowTime().getLogcTime());

            if(mTimeYearUint==TimeYearUint.CENTURY){
                tmp = now - (mTimeData[TimeMoreUint.YEAR.getIndex()]) * 100.0;
            }//
            else if(mTimeYearUint==TimeYearUint.YEAR){
                tmp = now - mTimeData[TimeMoreUint.YEAR.getIndex()] * 1.0;
            }
            else if(mTimeYearUint==TimeYearUint.HUNDRED_YEAR)
                tmp = now -(mTimeData[TimeMoreUint.YEAR.getIndex()]) * Math.pow(10.0, 2.0);
            else if(mTimeYearUint==TimeYearUint.THOUSAND_YEAR)
                tmp = now -(mTimeData[TimeMoreUint.YEAR.getIndex()]) * Math.pow(10.0, 3.0);
            else if(mTimeYearUint==TimeYearUint.TEN_THOUSAND_YEAR)
                tmp = now -(mTimeData[TimeMoreUint.YEAR.getIndex()]) * Math.pow(10.0, 4.0);
            else if(mTimeYearUint==TimeYearUint.MILLON_YEAR)
                tmp = now -(mTimeData[TimeMoreUint.YEAR.getIndex()]) * Math.pow(10.0, 6.0);
            else if(mTimeYearUint==TimeYearUint.TEN_MILLON_YEAR)
                tmp = now -(mTimeData[TimeMoreUint.YEAR.getIndex()]) * Math.pow(10.0, 7.0);
            else if(mTimeYearUint==TimeYearUint.HUNDRED_MILLON_YEAR)
                tmp = now -(mTimeData[TimeMoreUint.YEAR.getIndex()]) * Math.pow(10.0, 8.0);
            if(tmp<=0) tmp = tmp -1.0;
        }//end if AGO


        DecimalFormat df  = new DecimalFormat("#.0000000000");

        return Double.parseDouble(df.format(tmp));
    }
	
	
}
