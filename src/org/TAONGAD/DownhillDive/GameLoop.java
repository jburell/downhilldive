package org.TAONGAD.DownhillDive;

import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.SurfaceHolder;

public class GameLoop implements Runnable {
	SurfaceHolder m_surfHolder;
	Thread m_thread;
	
	Bitmap m_groundTex;
	Bitmap m_bmpAv[];
	
	public GameLoop() {
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
		while (true) {
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

		DownhillDive.getInstance().drawGround(canvas, m_groundTex,
				DownhillDive.getInstance().getHeight() >> 1, 20,
				DownhillDive.getInstance().getHeight(), 1, 0f);

		canvas.drawBitmap(m_bmpAv[DownhillDive.getInstance().getAvatar()
				.getLeanCurrent().ordinal()], new Rect(0, 0, 128, 128),
				new Rect(10, 10, 128, 128), new Paint());
	}
}
