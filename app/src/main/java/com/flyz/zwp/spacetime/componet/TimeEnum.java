package com.flyz.zwp.spacetime.componet;

public class TimeEnum {
	
	/**
	 * 
	 * @author zwp12
	 *
	 */
		public enum TimeOri
		{
			DEFU("",0),
			A_D("",1),//anno Domini
			B_C("公元前",2),//before chris
			AGO("距今",3);//
			private int index = 0;
			private String name;
			TimeOri(String name,int index)
			{
				this.index = index;
				this.name = name;
			}
			public int getIndex()
			{
				return this.index;
			}
			public String getName()
			{
				return this.name;
			}
			
		}
		
		/**
		 * shi jian dan wei
		 * 
		 */
		public enum TimeYearUint
		{


			YEAR("年",0),
			CENTURY("世纪",1),//shi ji
			HUNDRED_YEAR("百年",2),// bai nian
			THOUSAND_YEAR("千年",3),//qian nian
			TEN_THOUSAND_YEAR("万年",4),//wan nian
			MILLON_YEAR("百万年",5),//baiwan nian
			TEN_MILLON_YEAR("千万年",6),//baiwan nian
			HUNDRED_MILLON_YEAR("亿年",7);// yi nian
			
			private int index = 0;
			private String name;
			TimeYearUint(String name,int index)
			{
				this.index = index;
				this.name = name;
			}
			public int getIndex()
			{
				return this.index;
			}
			public String getName()
			{
				return this.name;
			}
			
			
		}
		
		public enum TimeMoreUint
		{
			SECOND("秒",0),
			MINUTE("分",1),
			HOUR("时",2),
			DAY_OF_MONTH("日",3),
			MONTH("月",4),
			YEAR("年",5),
			NIAN_DAI("0年代",6);
			private int index = 0;
			private String name;
			TimeMoreUint(String name,int index)
			{
				this.index = index;
				this.name = name;
			}
			public int getIndex()
			{
				return this.index;
			}
			public String getName()
			{
				return this.name;
			}
			
		}
		
	
	
}
