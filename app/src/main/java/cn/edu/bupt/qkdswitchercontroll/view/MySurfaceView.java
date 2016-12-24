package cn.edu.bupt.qkdswitchercontroll.view;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import android.R.integer;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Xfermode;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import cn.edu.bupt.qkdswitchercontroll.constant.ColorUsed;
import cn.edu.bupt.qkdswitchercontroll.constant.ViewConstant;

public class MySurfaceView extends SurfaceView implements
		SurfaceHolder.Callback {
	private static final String TAG = "MySurfaceView";
	int screenWidth = 0;
	int screenHeight = 0;

	public static int Line2_kneepoint_x = 0;
	public static int Line2_kneepoint_y = 0;
	public static int Line2_kneepoint2_x = 0;
	public static int Line2_kneepoint2_y = 0;

	public static int Line3_kneepoint_x = 0;
	public static int Line3_kneepoint_y = 0;
	public static int Line3_kneepoint2_x = 0;
	public static int Line3_kneepoint2_y = 0;

	float WSS1_Bottom = 0;
	float WSS1_Top = 0;

	float WSS2_Bottom = 0;
	float WSS2_Top = 0;

	float WSS1_left = 0;
	float WSS1_right = 0;
	float Coupler_left = 0;
	float Coupler_right = 0;

	float hight_base = 0;
	float width_base = 0;
	SurfaceHolder holder;
	Context context;
	Arrow obj1, obj2, obj3, obj4;

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);// 获取宽度模式
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);// 获取宽度值
		int width = 0;
		if (widthMode == MeasureSpec.EXACTLY) {
			width = widthSize;
		} else {
			width = 200;// 自定义的默认wrap_content值
			if (widthMode == MeasureSpec.AT_MOST) {
				width = Math.min(widthSize, width);
			}

		}
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);// 获取高度模式
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);// 获取高度值
		int height = 0;
		if (heightMode == MeasureSpec.EXACTLY) {
			height = heightSize;
		} else {
			height = 200;// 自定义的默认wrap_content值
			if (heightMode == MeasureSpec.AT_MOST) {
				height = Math.min(heightSize, height);
			}
		}
		this.screenHeight = this.getMeasuredHeight();
		this.screenWidth = this.getMeasuredWidth();
		Log.i(TAG, this.screenHeight + "  " + this.screenWidth);
		ViewConstant.setLeft_border(screenWidth / ViewConstant.WIDTH_DEV * 6);
		ViewConstant.setRitht_border(screenWidth / ViewConstant.WIDTH_DEV * 12);
		LineUsing.usingLine[ViewConstant.WSS1_LINE1] = 1;
		LineUsing.usingLine[ViewConstant.WSS1_LINE2] = 1;

		hight_base = screenHeight / ViewConstant.HEIGHT_DEV;
		width_base = screenWidth / ViewConstant.WIDTH_DEV;

		WSS1_Top = screenHeight / ViewConstant.HEIGHT_DEV;
		WSS2_Top = screenHeight / ViewConstant.HEIGHT_DEV * 6;
		WSS1_Bottom = screenHeight / ViewConstant.HEIGHT_DEV * 4;
		WSS2_Bottom = screenHeight / ViewConstant.HEIGHT_DEV * 9;

		WSS1_left = screenWidth / ViewConstant.WIDTH_DEV * 4;
		WSS1_right = screenWidth / ViewConstant.WIDTH_DEV * 6;

		Coupler_left = screenWidth / ViewConstant.WIDTH_DEV * 12;
		Coupler_right = screenWidth / ViewConstant.WIDTH_DEV * 14;

		Line2_kneepoint_x = screenWidth / ViewConstant.WIDTH_DEV * 8;
		Line2_kneepoint2_x = screenWidth / ViewConstant.WIDTH_DEV * 10;

		Line3_kneepoint_x = screenWidth / ViewConstant.WIDTH_DEV * 8;
		Line3_kneepoint2_x = screenWidth / ViewConstant.WIDTH_DEV * 10;

		Line2_kneepoint_y = screenHeight / ViewConstant.HEIGHT_DEV * 3;
		Line2_kneepoint2_y = screenHeight / ViewConstant.HEIGHT_DEV * 7;

		Line3_kneepoint_y = screenHeight / ViewConstant.HEIGHT_DEV * 7;
		Line3_kneepoint2_y = screenHeight / ViewConstant.HEIGHT_DEV * 3;

		obj1 = new Arrow(WSS1_right, screenHeight / ViewConstant.HEIGHT_DEV * 2);
		obj2 = new Arrow(WSS1_right, screenHeight / ViewConstant.HEIGHT_DEV * 3);
		obj3 = new Arrow(WSS1_right, screenHeight / ViewConstant.HEIGHT_DEV * 7);
		obj4 = new Arrow(WSS1_right, screenHeight / ViewConstant.HEIGHT_DEV * 8);

		holder = this.getHolder();// ��ȡholder
		holder.addCallback(this);
		setMeasuredDimension(width, height);// 最终将测量的值传入该方法完成测量
	}

	public MySurfaceView(Context context, AttributeSet asSet) {
		super(context, asSet);
		// this.holder=holder;
		this.context = context;

	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			float x = event.getX();
			float y = event.getY();
			if (x > WSS1_left && x < WSS1_right && y > WSS1_Top
					&& y < WSS1_Bottom) {
				// WSS1 is pressed
				LineUsing.usingLine[ViewConstant.WSS2_LINE2] = 0;
				LineUsing.usingLine[ViewConstant.WSS2_LINE1] = 0;
				LineUsing.usingLine[ViewConstant.WSS1_LINE2] = 0;
				LineUsing.usingLine[ViewConstant.WSS1_LINE1] = 0;
				Log.i(TAG, "WSS1 is pressed");
			}
			if (x > WSS1_left && x < WSS1_right && y > WSS2_Top
					&& y < WSS2_Bottom) {
				// WSS1 is pressed
				LineUsing.usingLine[ViewConstant.WSS2] = 1;
				LineUsing.usingLine[ViewConstant.WSS2_LINE2] = 11;
				Log.i(TAG, "WSS2 is pressed");
			}
		}
		return super.onTouchEvent(event);
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}

	Thread thread;

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		thread = new Thread(new MyThread());
		thread.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		isRuning = false;
		thread.interrupt();
	}

	public boolean isRuning = true;

	SurfaceHolder surfaceHolder;

	// �ڲ�����ڲ���
	class MyThread implements Runnable {

		public void draw(Canvas canvas, Paint mPaint) {
			if (canvas == null) {
				return;
			}
			mPaint.setColor(ColorUsed.INITColor);
			mPaint.setXfermode(new PorterDuffXfermode(Mode.CLEAR));
			canvas.drawPaint(mPaint);
			mPaint.setXfermode(new PorterDuffXfermode(Mode.SRC));

			mPaint.setStrokeWidth((float) 5.0); // 线宽

			// WSS Rect
			mPaint.setColor(ColorUsed.INITColor);

			if (LineUsing.usingLine[ViewConstant.WSS1] == 1) {
				mPaint.setColor(ColorUsed.usingColor);

			}
			canvas.drawLine(2 * WSS1_left - WSS1_right,
					(WSS1_Top + WSS1_Bottom) / 2, WSS1_left,
					(WSS1_Top + WSS1_Bottom) / 2, mPaint);

			canvas.drawRect(new RectF(WSS1_left, WSS1_Top, WSS1_right,
					WSS1_Bottom), mPaint);

			mPaint.setColor(ColorUsed.INITColor);
			if (LineUsing.usingLine[ViewConstant.WSS2] == 1) {
				mPaint.setColor(ColorUsed.usingColor);
			}
			canvas.drawLine(2 * WSS1_left - WSS1_right,
					(WSS2_Top + WSS2_Bottom) / 2, WSS1_left,
					(WSS2_Top + WSS2_Bottom) / 2, mPaint);

			canvas.drawRect(new RectF(WSS1_left, WSS2_Top, WSS1_right,
					WSS2_Bottom), mPaint);

			// 耦合器
			mPaint.setColor(ColorUsed.INITColor);
			if (LineUsing.usingLine[ViewConstant.COUPLER1] == 1) {
				mPaint.setColor(ColorUsed.usingColor);
			}
			canvas.drawLine(Coupler_right, (WSS1_Top + WSS1_Bottom) / 2, 2
					* Coupler_right - Coupler_left,
					(WSS1_Top + WSS1_Bottom) / 2, mPaint);

			canvas.drawRect(new RectF(Coupler_left, WSS1_Top, Coupler_right,
					WSS1_Bottom), mPaint);

			mPaint.setColor(ColorUsed.INITColor);
			if (LineUsing.usingLine[ViewConstant.COUPLER2] == 1) {
				mPaint.setColor(ColorUsed.usingColor);
			}

			canvas.drawLine(Coupler_right, (WSS2_Top + WSS2_Bottom) / 2, 2
					* Coupler_right - Coupler_left,
					(WSS2_Top + WSS2_Bottom) / 2, mPaint);
			canvas.drawRect(new RectF(Coupler_left, WSS2_Top, Coupler_right,
					WSS2_Bottom), mPaint);

			// WSS1 the first line
			if (LineUsing.usingLine[ViewConstant.WSS1_LINE1] / 10 == 1) {

				obj1.drawSelf(canvas,
						LineUsing.usingLine[ViewConstant.WSS1_LINE1] % 10);
				mPaint.setColor(ColorUsed.usingColor);
				obj1.getNextPos(0);
			}

			canvas.drawLine(WSS1_right, hight_base * 2, Coupler_left,
					hight_base * 2, mPaint);

			// WSS1 the second line
			mPaint.setColor(ColorUsed.INITColor);
			if (LineUsing.usingLine[ViewConstant.WSS1_LINE2] / 10 == 1) {// 取出十位，如果为1则该链路被用，取出个位，如果为1，则作为量子的
				obj2.drawSelf(canvas,
						LineUsing.usingLine[ViewConstant.WSS1_LINE2] % 10);
				obj2.getNextPos(1);
				mPaint.setColor(ColorUsed.usingColor);
			}
			canvas.drawLine(width_base * 6, hight_base * 3, Line2_kneepoint_x,
					Line2_kneepoint_y, mPaint);
			canvas.drawLine(Line2_kneepoint_x, Line2_kneepoint_y,
					Line2_kneepoint2_x, Line2_kneepoint2_y, mPaint);
			canvas.drawLine(width_base * 10, hight_base * 7, width_base * 12,
					hight_base * 7, mPaint);

			mPaint.setColor(ColorUsed.INITColor);
			if (LineUsing.usingLine[ViewConstant.WSS2_LINE1] / 10 == 1) { // 取出十位，如果为1则该链路被用，
				obj3.drawSelf(canvas,
						LineUsing.usingLine[ViewConstant.WSS2_LINE1] % 10);
				obj3.getNextPos(2);
				mPaint.setColor(ColorUsed.usingColor);
			}
			canvas.drawLine(width_base * 6, hight_base * 7, width_base * 8,
					hight_base * 7, mPaint);
			canvas.drawLine(width_base * 8, hight_base * 7, width_base * 10,
					hight_base * 3, mPaint);
			canvas.drawLine(width_base * 10, hight_base * 3, width_base * 12,
					hight_base * 3, mPaint);

			mPaint.setColor(ColorUsed.INITColor);

			if (LineUsing.usingLine[ViewConstant.WSS2_LINE2] / 10 == 1) {
				obj4.drawSelf(canvas,
						LineUsing.usingLine[ViewConstant.WSS2_LINE2] % 10);
				obj4.getNextPos(3);
				mPaint.setColor(ColorUsed.usingColor);
			}
			canvas.drawLine(width_base * 6, hight_base * 8, Coupler_left,
					hight_base * 8, mPaint);

			// 输入输出
		}

		@Override
		public void run() {
			while (isRuning) {
				synchronized (this) {
					if (holder != null) {
						Paint mPaint = new Paint();
						Canvas canvas = holder.lockCanvas();//
						// System.out.println(canvas.toString());
						draw(canvas, mPaint);
						if(canvas!=null)
						holder.unlockCanvasAndPost(canvas);// �����������ύ���õ�ͼ��
					}
				}
			}
		}

	}
}
