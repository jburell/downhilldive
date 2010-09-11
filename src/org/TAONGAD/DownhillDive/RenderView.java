package org.TAONGAD.DownhillDive;

import android.content.Context;
import android.view.SurfaceHolder;
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

public class RenderView extends SurfaceView implements SurfaceHolder.Callback {

	public static final long FPS_MS = 1000 / 2;

	private final Handler mHandler = new Handler(new MyHandler());

	private final Rect mSrcRect = new Rect();
	private final Rect mDstRect = new Rect();

	private static final int MARGIN = 4;

	private Paint mWinPaint;
	private Paint mLinePaint;

	

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

		setKeepScreenOn(true);
		getHolder().addCallback(this);

		
	}

	// -----------------------------------------

//	@Override
//	protected void onDraw(Canvas canvas) {
//		super.onDraw(canvas);
//
//		
//	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);
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

	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
//		SizeUtil.setScreenSize(width, height);
	}
 
	public void surfaceCreated(SurfaceHolder holder) {
//		if (mStarted) {
//			mGameThread.resume(holder);
//			return;
//		}
//		mStarted = true;
//
//		Rect frame = holder.getSurfaceFrame();
//		SizeUtil.setScreenSize(frame.width(), frame.height());
//
//		mGameThread.start(holder, frame.width(), frame.height());
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
//		mGameThread.pause();
	}
}
