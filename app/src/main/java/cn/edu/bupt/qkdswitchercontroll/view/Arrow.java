package cn.edu.bupt.qkdswitchercontroll.view;

import com.topeet.serialtest.serial;

import android.R.bool;
import android.R.integer;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.media.JetPlayer;
import android.util.Log;

import cn.edu.bupt.qkdswitchercontroll.constant.ColorUsed;
import cn.edu.bupt.qkdswitchercontroll.constant.ViewConstant;

public class Arrow {
	private static final String TAG = "Arrow";
	public  float x=0;
	public  float y=0;
	public  Bitmap img;
	
	
	public Arrow(float RightcenterX,float RightcenterY){
		x =RightcenterX;
		y=RightcenterY;
	}
	Bitmap resizedBitmap ;
	int RectWidth=20;
	int RectHight=20;
	public   void drawSelf(Canvas canvas,int flag){
//		flag show quantum channel or classic channel
		Paint paint=new Paint();
		paint.setColor(ColorUsed.RECTCOLOR);
		
		canvas.drawRect(x-RectWidth, y-RectHight, x, y+RectHight, paint);
		paint.setColor(ColorUsed.CIRCLECOLOR);
		if(flag==1){
			//量子信号和同步信号同时传输
			canvas.drawCircle(x-RectWidth/2, y-RectHight/2, Math.min(RectWidth, RectHight)/2-5, paint);
			canvas.drawCircle(x-RectWidth/2, y+RectHight/2, Math.min(RectWidth, RectHight)/2-5, paint);
		}
		else{
			//经典信号
			canvas.drawCircle(x-RectWidth/2, y, Math.min(RectWidth, RectHight)/2, paint);
		}
	}

	public  void getNextPos(int flag) {  
		if (LineUsing.usingLine[0]/10 == 1&&flag==0) {  //control WSS1's the first line
			if (x < ViewConstant.getRitht_border()) {
				this.x += ColorUsed.Speed;
			} else {
				this.x = ViewConstant.getLeft_border();
			}
		}
		if (LineUsing.usingLine[1]/10 == 1&&flag==1) {  //control WSS1's the first line
			if (x < MySurfaceView.Line2_kneepoint_x) {
				x += ColorUsed.Speed;
			} else if (x>MySurfaceView.Line2_kneepoint2_x+5&&x<MySurfaceView.Line2_kneepoint2_x){
				x +=  ColorUsed.Speed;
			}
			else if (x>MySurfaceView.Line2_kneepoint2_x){
				x=ViewConstant.getLeft_border();
				y=MySurfaceView.Line2_kneepoint_y;//ViewConstant.screenHeight / ViewConstant.HEIGHT_DEV * 3;
			}
			else{
				
				double k = ((MySurfaceView.Line2_kneepoint2_y - MySurfaceView.Line2_kneepoint_y) / (MySurfaceView.Line2_kneepoint2_x - MySurfaceView.Line2_kneepoint_x));

				y+=k*(Math.sqrt(Math.pow( ColorUsed.Speed,2)/(1+k*k)));
				x+=Math.sqrt(Math.pow( ColorUsed.Speed,2)/(1+k*k));
				
			}
		}
		if (LineUsing.usingLine[2]/10 == 1 &&flag==2) {  //control WSS1's the first line
			if (x < MySurfaceView.Line3_kneepoint_x) {
				x += ColorUsed.Speed;
			}else if(x>MySurfaceView.Line3_kneepoint2_x+5 && x<ViewConstant.getRitht_border()) {
				x+= ColorUsed.Speed;
			}
			else if(x>ViewConstant.getRitht_border()){
				x = ViewConstant.getLeft_border();
				y=MySurfaceView.Line3_kneepoint_y;
			}else{
				double k = ((MySurfaceView.Line3_kneepoint2_y - MySurfaceView.Line3_kneepoint_y) / (MySurfaceView.Line3_kneepoint2_x - MySurfaceView.Line3_kneepoint_x));
				y+=k*(Math.sqrt(Math.pow( ColorUsed.Speed,2)/(1+k*k)));
				x+=Math.sqrt(Math.pow( ColorUsed.Speed,2)/(1+k*k));
			}
		}
		if (LineUsing.usingLine[3]/10 == 1 && flag==3) {  //control WSS1's the first line
			if (x < ViewConstant.getRitht_border()) {
				x +=  ColorUsed.Speed;
			} else {
				x = ViewConstant.getLeft_border();
			}
		}
    }  
}
