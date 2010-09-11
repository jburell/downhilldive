package org.TAONGAD.DownhillDive;

import android.content.Context;
import android.view.SurfaceView;
import android.view.View;

import java.io.IOException;
import java.util.Random;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory.Options;
import android.graphics.Paint.Style;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.os.Handler.Callback;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

//-----------------------------------------------

public class RenderView extends View {

	public static final long FPS_MS = 1000 / 2;

	private final Handler mHandler = new Handler(new MyHandler());

	private final Rect mSrcRect = new Rect();
	private final Rect mDstRect = new Rect();

	private static final int MARGIN = 4;

	private Paint mWinPaint;
	private Paint mLinePaint;

	Bitmap m_groundTex;
	Bitmap m_bmpAv[];

	public RenderView(Context context/* , AttributeSet attrs */) {
		super(context/* , attrs */);
		requestFocus();

		mLinePaint = new Paint();
		mLinePaint.setColor(0xFFFFFFFF);
		mLinePaint.setStrokeWidth(5);
		mLinePaint.setStyle(Style.STROKE);

		mWinPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
		mWinPaint.setColor(0xFFFF0000);
		mWinPaint.setStrokeWidth(10);
		mWinPaint.setStyle(Style.STROKE);

		try {
			m_groundTex = BitmapFactory.decodeStream(DownhillDive.getInstance()
					.getAssets().open(
							DownhillDive.getInstance().getLoadingImage()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		m_bmpAv = new Bitmap[Avatar.Leaning.values().length];
		try {
			m_bmpAv[Avatar.Leaning.NONE.ordinal()] = BitmapFactory.decodeStream(DownhillDive.getInstance()
					.getAssets().open(DownhillDive.getInstance().getAvatar().getLeanPic(Avatar.Leaning.NONE)));
		} catch (IOException e) {
			Log.e("RENDERER", "LOADING " + DownhillDive.getInstance().getAvatar().getLeanPic(Avatar.Leaning.NONE) + " failed!");
			e.printStackTrace();
		}
		
		try {
			m_bmpAv[Avatar.Leaning.ALOT.ordinal()] = BitmapFactory.decodeStream(DownhillDive.getInstance()
					.getAssets().open(DownhillDive.getInstance().getAvatar().getLeanPic(Avatar.Leaning.ALOT)));
		} catch (IOException e) {
			Log.e("RENDERER", "LOADING " + DownhillDive.getInstance().getAvatar().getLeanPic(Avatar.Leaning.ALOT) + " failed!");
			e.printStackTrace();
		}
		
		try {
			m_bmpAv[Avatar.Leaning.SLIGHTLY.ordinal()] = BitmapFactory.decodeStream(DownhillDive.getInstance()
					.getAssets().open(DownhillDive.getInstance().getAvatar().getLeanPic(Avatar.Leaning.SLIGHTLY)));
		} catch (IOException e) {
			Log.e("RENDERER", "LOADING " + DownhillDive.getInstance().getAvatar().getLeanPic(Avatar.Leaning.SLIGHTLY) + " failed!");
			e.printStackTrace();
		}
	}

	// -----------------------------------------

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		
		DownhillDive.getInstance().getAvatar().Update();
		
//		DownhillDive.getInstance().drawGround(canvas, m_groundTex,
//				DownhillDive.getInstance().getHeight() >> 1, 20,
//				DownhillDive.getInstance().getHeight(), 1, 0, 0, 0, 0);
		
		DownhillDive.getInstance().drawGround(canvas, m_groundTex,
				DownhillDive.getInstance().getHeight() >> 1, 20,
				DownhillDive.getInstance().getHeight(), 1, 0f);
	
	
		canvas.drawBitmap(m_bmpAv[DownhillDive.getInstance().getAvatar().getLeanCurrent().ordinal()], new Rect(0,0,128,128), new Rect(10,10,128,128), new Paint());
	}

//	@Override
//	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//		super(widthMeasureSpec, heightMeasureSpec);
//		// Keep the view squared
////		int w = MeasureSpec.getSize(widthMeasureSpec);
////		int h = MeasureSpec.getSize(heightMeasureSpec);
////		int d = w == 0 ? h : h == 0 ? w : w < h ? w : h;
////		setMeasuredDimension(d, d);
//	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);

//		int sx = (w - 2 * MARGIN) / 3;
//		int sy = (h - 2 * MARGIN) / 3;
//
//		int size = sx < sy ? sx : sy;
//
//		mDstRect.set(MARGIN, MARGIN, size - MARGIN, size - MARGIN);
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		int action = event.getAction();

		if (action == MotionEvent.ACTION_DOWN) {
			return true;

		} else if (action == MotionEvent.ACTION_UP) {
			int x = (int) event.getX();
			int y = (int) event.getY();

			return true;
		}

		return false;
	}

	@Override
	protected Parcelable onSaveInstanceState() {
		Bundle b = new Bundle();

		Parcelable s = super.onSaveInstanceState();

		return b;
	}

	@Override
	protected void onRestoreInstanceState(Parcelable state) {

		if (!(state instanceof Bundle)) {
			// Not supposed to happen.
			super.onRestoreInstanceState(state);
			return;
		}

		Bundle b = (Bundle) state;
		Parcelable superState = b.getParcelable("gv_super_state");

		super.onRestoreInstanceState(superState);
	}

	// -----

	private class MyHandler implements Callback {
		public boolean handleMessage(Message msg) {

			return false;
		}
	}
}
