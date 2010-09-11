package org.TAONGAD.DownhillDive;

import java.io.IOException;
import java.nio.ByteBuffer;

import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.hardware.SensorManager;
import android.opengl.GLUtils;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Window;
import android.view.WindowManager;

public class DownhillDive extends Activity {
	static DownhillDive m_instance;
	
	GameLoop m_loop;
	RenderView m_renderView;

	static public DownhillDive getInstance() {
		return m_instance;
	}

	int m_width;
	int m_height;
	Avatar m_avatar;

	int getWidth() {
		return m_width;
	}

	int getHeight() {
		return m_height;
	}
	
	Avatar getAvatar () {
		return m_avatar;
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		m_instance = this;

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

		m_renderView = new RenderView(getApplicationContext());
		
		init();
		setContentView(m_renderView);
	}

	public void init() {
		Display display = getWindowManager().getDefaultDisplay(); 
		m_width = display.getWidth();
		m_height = display.getHeight();
		
		m_avatar = new Avatar(this, (SensorManager)getSystemService(Context.SENSOR_SERVICE));
		
		m_loop = new GameLoop();
		m_loop.start(m_renderView.getHolder());
	}
	
	void drawGround(Canvas canvas, Bitmap groundTex, int y1,
			float z1, float y2, float z2, float x) {

		float zpos = 1.0f / z2;
		z1 = 1.0f / z1;
		final int sideMargin = 40;
		
		
		/* sample 16 times for each scanline */
//		y1 <<= 4;
//		y2 <<= 4;

		int bottomY = (int)y2;
		int topY = y1;
		float zadd = (z1 - zpos) / (float) (y2 - y1);
		while (bottomY > topY) {
			float z = 1.0f / zpos;
			float centx = (float) (x);
			
			Paint mLinePaint = new Paint();

			mLinePaint.setColor(0xFFFFFFFF);
			mLinePaint.setStrokeWidth(1);
			mLinePaint.setStyle(Style.STROKE);

			canvas.drawLine(((m_height - y2) + x) + sideMargin /** (zpos) */
			, bottomY, ((m_width) - (m_height - y2)) - sideMargin /** zpos */
			, bottomY, mLinePaint);

			zpos += zadd;
			y2 -= 1.15;
			bottomY--;
		}
	}

	void drawGround(Canvas canvas, Bitmap groundTex, int y1,
			float z1, int y2, float z2, float x, float xmul, float ymul,
			float texoff) {

		float curvdiv = z1 / (3.141592654f * 0.5f);
		float zpos = 1.0f / z2;
		z1 = 1.0f / z1;

		/* sample 16 times for each scanline */
		y1 <<= 4;
		y2 <<= 4;
		ymul *= 16.0f;

		int BottomY = y2;
		float zadd = (z1 - zpos) / (float) (y2 - y1);
		while (y2 > y1) {
			float z = 1.0f / zpos;

			/*
			 * NOTE: z is the actual depth of this scanline, zpos is 1/z. The
			 * most common perspective projection formula is used, i.e.
			 * 
			 * transformed_x = (centre of screen) - (half width of screen)*(x /
			 * z)
			 * 
			 * But instead of dividing by z we can multiply by zpos if we like.
			 */
			float centx = (float) (x + xmul * (Math.cos(z / curvdiv) - 1.0f));
			int topy = (int) (y2 + (m_height >> 1)
					* (ymul * (Math.cos(z / curvdiv) - 1.0f) / z));

			if ((topy >> 4) < (BottomY >> 4)) /*
											 * question is: are we at least one
											 * pixel line above what was last
											 * drawn ?
											 */
			{
				// stretch_blit(groundTex, backBuffer,
				// 0, (int)((z+texoff)*4.0f) & (groundTex.getHeight() - 1),
				// groundTex.getWidth(), 1,
				// (m_width >> 1) + (m_width >> 1)*(centx-1.0f)*zpos, (topy) >>
				// 4,
				// (m_width >> 1) * (2.0f)*zpos, (BottomY >> 4) - (topy >> 4));
				
				Paint mLinePaint = new Paint();
				mLinePaint.setColor(0xFFFFFFFF);
				mLinePaint.setStrokeWidth(1);
				mLinePaint.setStyle(Style.STROKE);
				
				canvas.drawLine((m_width >> 1) + ((m_width >> 1) * (centx-1.0f) * zpos), (topy) >> 4,
				(m_width >> 1) * (2.0f)*zpos, (BottomY >> 4) - (topy >> 4), mLinePaint);
				BottomY = topy;
			}

			zpos += zadd;
			y2--;
		}
	}
}