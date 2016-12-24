package cn.edu.bupt.qkdswitchercontroll.constant;

import android.R.integer;
import android.graphics.Color;

public class ViewConstant {

//	public static int screenWidth;
//	public static int Line2_kneepoint_x=0;
//	public static int Line2_kneepoint_y=0;
//	public static int Line2_kneepoint2_x=0;
//	public static int Line2_kneepoint2_y=0;
//	
//	public static int Line3_kneepoint_x=0;
//	public static int Line3_kneepoint_y=0;
//	public static int Line3_kneepoint2_x=0;
//	public static int Line3_kneepoint2_y=0;

//	
//	public int getScreenWidth() {
//		return screenWidth;
//	}
//	public static void setScreenWidth(int screenWidth) {
//		ViewConstant.screenWidth = screenWidth;
////		Line2_kneepoint_x=ViewConstant.screenWidth/ViewConstant.WIDTH_DEV*8;
////		Line2_kneepoint2_x=ViewConstant.screenWidth/ViewConstant.WIDTH_DEV*10;
////		
////		Line3_kneepoint_x=ViewConstant.screenWidth/WIDTH_DEV*8;
////		Line3_kneepoint2_x=screenWidth/WIDTH_DEV*10;
////	}
//	public static int getScreenHeight() {
//		return screenHeight;
//	}
//	public static  void setScreenHeight(int screenHeight) {
//		ViewConstant.screenHeight = screenHeight;
////		Line2_kneepoint_y=screenHeight/HEIGHT_DEV*3;
////		Line2_kneepoint2_y=screenHeight/HEIGHT_DEV*7;
////		
////		Line3_kneepoint_y=screenHeight/HEIGHT_DEV*7;
////		Line3_kneepoint2_y=screenHeight/HEIGHT_DEV*3;
//	}
//	public static int  screenHeight;
	public static int WIDTH_DEV=20;
	public static int HEIGHT_DEV=10;
	
	/*
	 * 以下顺序及定义不可变
	 */
	public static final int WSS1_LINE1=0;
	public static final int WSS1_LINE2=1;
	public static final int WSS2_LINE1=2;
	public static final int WSS2_LINE2=3;
	
	public static final int WSS1=4;
	public static final int WSS2=5;
	
	public static final int COUPLER1=6;
	public static final int COUPLER2=7;
	
	
	
	public static int ritht_border=0;  // WSS右边界
	public  static int left_border=0;   // 耦合器左边界
	public static int getRitht_border() {
		return ritht_border;
	}
	public static void setRitht_border(int ritht_border) {
		ViewConstant.ritht_border = ritht_border;
	}
	public static int getLeft_border() {
		return left_border;
	}
	public static void setLeft_border(int left_border) {
		ViewConstant.left_border = left_border;
	}
	
	
}
