package org.TAONGAD.DownhillDive;

import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.SurfaceHolder;
import android.widget.SlidingDrawer;

public class GameLoop implements Runnable {
	SurfaceHolder m_surfHolder;
	Thread m_thread;
	
	Bitmap m_groundTex;
	Bitmap m_bmpAv[];
	
	float m_bgFrame = 0;
	
	Bitmap m_groundAnim[];
	Bitmap m_horizon;
	
	public GameLoop() {
		m_groundAnim = new Bitmap[3];
		try {
			m_groundAnim[0] = BitmapFactory.decodeStream(DownhillDive.getInstance().getAssets().open("ski_lvl_frame0.png"));
			m_groundAnim[1] = BitmapFactory.decodeStream(DownhillDive.getInstance().getAssets().open("ski_lvl_frame1.png"));
			m_groundAnim[2] = BitmapFactory.decodeStream(DownhillDive.getInstance().getAssets().open("ski_lvl_frame2.png"));
			m_horizon = BitmapFactory.decodeStream(DownhillDive.getInstance().getAssets().open("horizon.png"));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		
		
		m_bmpAv = new Bitmap[Avatar.Leaning.values().length];
		try {
			m_bmpAv[Avatar.Leaning.NONE.ordinal()] = BitmapFactory
					.decodeStream(DownhillDive.getInstance().getAssets().open(
							DownhillDive.getInstance().getAvatar().getLeanPic(
									Avatar.Leaning.NONE)));
		} catch (IOException e) {
			Log.e("RENDERER", "LOADING "
					+ DownhillDive.getInstance().getAvatar().getLeanPic(
							Avatar.Leaning.NONE) + " failed!");
			e.printStackTrace();
		}

		try {
			m_bmpAv[Avatar.Leaning.ALOT.ordinal()] = BitmapFactory
					.decodeStream(DownhillDive.getInstance().getAssets().open(
							DownhillDive.getInstance().getAvatar().getLeanPic(
									Avatar.Leaning.ALOT)));
		} catch (IOException e) {
			Log.e("RENDERER", "LOADING "
					+ DownhillDive.getInstance().getAvatar().getLeanPic(
							Avatar.Leaning.ALOT) + " failed!");
			e.printStackTrace();
		}

		try {
			m_bmpAv[Avatar.Leaning.SLIGHTLY.ordinal()] = BitmapFactory
					.decodeStream(DownhillDive.getInstance().getAssets().open(
							DownhillDive.getInstance().getAvatar().getLeanPic(
									Avatar.Leaning.SLIGHTLY)));
		} catch (IOException e) {
			Log.e("RENDERER", "LOADING "
					+ DownhillDive.getInstance().getAvatar().getLeanPic(
							Avatar.Leaning.SLIGHTLY) + " failed!");
			e.printStackTrace();
		}
	}

	public void start(SurfaceHolder surfaceHolder) {
		m_surfHolder = surfaceHolder;
		if (m_thread == null) {
			m_thread = new Thread(this, "GameLoop");
			m_thread.start();
		} else {
			m_thread.resume();
		}
	}

	public void run() {
		float tmp_target = 1000.0f / 30.0f; // 30FPS
		long tmp_last = System.currentTimeMillis();
		
		while (true) {
			long tmp_now = System.currentTimeMillis();
			if (tmp_target < tmp_now - tmp_last) {
				tmp_last = tmp_now;
				
				Canvas canvas = m_surfHolder.lockCanvas();
				if (canvas != null) {
					try {
						updateInput();
	
						// updateSound();
						updateGraphics(canvas);
					} finally {
						m_surfHolder.unlockCanvasAndPost(canvas);
					}
				}
			}
			else {
				try {
					Thread.sleep(tmp_now - tmp_last);
				} catch (InterruptedException e) {
					Log.e("Crash!!!!!!!!!!!!!!1", "Failed to sleep");
					e.printStackTrace();
				}
			}
		}
	}

	void updateSound(){
		
	}
	
	void updateInput(){
		
	}
	
	void updateGraphics(Canvas canvas){
		DownhillDive.getInstance().getAvatar().Update();		
		
		// DownhillDive.getInstance().drawGround(canvas, m_groundTex,
		// DownhillDive.getInstance().getHeight() >> 1, 20,
		// DownhillDive.getInstance().getHeight(), 1, 0, 0, 0, 0);
		
		canvas.drawColor(Color.BLACK);
		
		canvas.drawBitmap(m_horizon, new Rect(0, 0, 480, 160), new Rect(0, 0, DownhillDive.getInstance().getWidth(), DownhillDive.getInstance().getHeight() >> 1), new Paint());
		canvas.drawBitmap(m_groundAnim[(int) (m_bgFrame % 3)], new Rect(0, 0, 480, 160), new Rect(0, DownhillDive.getInstance().getHeight() >> 1, DownhillDive.getInstance().getWidth(), DownhillDive.getInstance().getHeight()), new Paint());
		m_bgFrame += 0.6;
//		DownhillDive.getInstance().drawGround(canvas, m_groundTex,
//				DownhillDive.getInstance().getHeight() >> 1, 20,
//				DownhillDive.getInstance().getHeight(), 1, 0f);

		canvas.drawBitmap(m_bmpAv[DownhillDive.getInstance().getAvatar()
				.getLeanCurrent().ordinal()], new Rect(0, 0, 64, 64),
				new Rect(10, 10, 256, 256), new Paint());
	}
}
