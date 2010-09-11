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

	static public DownhillDive getInstance() {
		return m_instance;
	}

	int m_width;
	int m_height;
	boolean m_isLoading = true;
	boolean m_isTextureLoaded = false;
	Texture m_ground;
	Avatar m_avatar;
	
	ByteBuffer m_backBuffer;
	Bitmap m_backBitmap;
	int[] m_texIdx;

	int getWidth() {
		return m_width;
	}

	int getHeight() {
		return m_height;
	}
	
	Avatar getAvatar () {
		return m_avatar;
	}

	boolean isLoading() {
		return m_isLoading;
	}

	boolean isLandscape() {
		return true;
	}

	Texture getGround() {
		return m_ground;
	}

	String getLoadingImage() {
		return "loading.png";
	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		m_instance = this;
		m_isLoading = true;

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

		init();

		//RenderView renderView = new RenderView(getApplicationContext());
		RenderView renderView = new RenderView(getApplicationContext());
		//GameRenderer renderer = new GameRenderer();
		setContentView(renderView);
	}

	public void init() {
		//TextureManager.prepare();
//		TextureAtlas atlas = new TextureAtlas(1024, 1024);

		Display display = getWindowManager().getDefaultDisplay(); 
		m_width = display.getWidth();
		m_height = display.getHeight();
		//m_backBuffer = ByteBuffer.allocateDirect(w * h);
		
		int w = 320;
		int h = 200;
		ByteBuffer byteBuffer = ByteBuffer.allocateDirect(w * h);
		
		m_avatar = new Avatar(this, (SensorManager)getSystemService(Context.SENSOR_SERVICE));
		
	
	
	}

	public void doLoading(GL10 gl) {
		m_isLoading = true;

		TextureManager.loadTextures(gl);

		// Wait for load to complete
		while (m_isTextureLoaded == false) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		m_isLoading = false;
	}

	public void textureLoadComplete() {
		m_isTextureLoaded = true;
	}

	void drawFrame(GL10 gl){
		Canvas canvas = new Canvas(gl);
		Paint mLinePaint = new Paint();
        mLinePaint.setColor(0xFFFFFFFF);
        mLinePaint.setStrokeWidth(5);
        mLinePaint.setStyle(Style.STROKE);
		canvas.drawLine(0, 0, 20, 10, mLinePaint);
		
		// Insert magic here
//		gl.glClearColor(1f, 0, 0, 1f);
//		gl.glClear(gl.GL_COLOR_BUFFER_BIT | gl.GL_DEPTH_BUFFER_BIT);
		
//		m_backBitmap.setPixel(20, 10, 0x00ff00);
//		GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, m_backBitmap, 0);
//		gl.glBindTexture(GL10.GL_TEXTURE_2D, m_texIdx[0]);
//		GL11 g = (GL11)gl;
		
		
//		Bitmap bitmap = BitmapFactory.decodeByteArray(data, offset, length)
//		GLUtils.texImage2D()
//		drawGround(gl, t, tex, y1, z1, y2, z2, x, xmul, ymul, texoff)
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