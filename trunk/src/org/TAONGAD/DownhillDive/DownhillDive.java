package org.TAONGAD.DownhillDive;

import javax.microedition.khronos.opengles.GL10;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.GradientDrawable.Orientation;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Window;
import android.view.WindowManager;

public class DownhillDive extends Activity{
	static DownhillDive m_instance;
	static public DownhillDive getInstance(){
		return m_instance;
	}
	
	int m_width;
	int m_height;
	boolean m_isLoading = true;
	boolean m_isTextureLoaded = false;
	
	int getWidth(){
		return m_width;
	}
	
	int getHeight(){
		return m_height;
	}
	
	boolean isLoading(){
		return m_isLoading;
	}
	
	boolean isLandscape(){
		return true;
	}
	
	String getLoadingImage(){
		return "loading.png";
	}
	
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);

		m_instance = this;
		m_width = 100;
		m_height = 50;
		m_isLoading = true;
		
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, 
			WindowManager.LayoutParams.FLAG_FULLSCREEN); 
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
		
		init();
		
		RenderView renderView = new RenderView(getApplicationContext());
		GameRenderer renderer = new GameRenderer();
		renderView.setRenderer(renderer);
		setContentView(renderView);
	}
	
	public void init(){
		TextureManager.prepare();
	}
	
	public void doLoading(GL10 gl){
		m_isLoading = true;
		
		TextureManager.loadTextures(gl);
		
		// Wait for load to complete
		while(m_isTextureLoaded == false){
			try{
				Thread.sleep(10);
			}catch(InterruptedException e){
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		m_isLoading = false;
	}
	
	public void textureLoadComplete(){
		m_isTextureLoaded = true;
	}
	
	void drawFrame(GL10 gl){
		// Insert magic here
		gl.glClearColor(1f, 0, 0, 1f);
		gl.glClear(gl.GL_COLOR_BUFFER_BIT | gl.GL_DEPTH_BUFFER_BIT);
	}
	
	void drawGround(GL10 gl, Texture t, Texture tex, int y1, float z1, int y2, float z2, float x, float xmul, float ymul, float texoff){

			float curvdiv = z1/(3.141592654f*0.5f);
			float zpos = 1.0f /z2;
			z1 = 1.0f / z1;


			/* sample 16 times for each scanline */
			y1 <<= 4;
			y2 <<= 4;
			ymul *= 16.0f;

			int BottomY = y2;
			float zadd = (z1 - zpos) / (float)(y2 - y1);
			while(y2 > y1)
			{
				float z = 1.0f / zpos;

				/*
					NOTE: z is the actual depth of this scanline, zpos is 1/z. The most common
					perspective projection formula is used, i.e.

					transformed_x = (centre of screen) - (half width of screen)*(x / z)

					But instead of dividing by z we can multiply by zpos if we like.
				*/
				float centx = (float) (x + xmul*(Math.cos(z/curvdiv) - 1.0f));
				int topy = (int) (y2 + (m_height >> 1)*(ymul*(Math.cos(z/curvdiv) - 1.0f) / z));

				if((topy >> 4) < (BottomY >> 4)) /* question is: are we at least one pixel line above what was last drawn ? */
				{
					//stretch_blit(tex, t, 0, (int)((z+texoff)*4.0f)&(tex.getHeight() - 1), tex.getWidth(), 1, (m_width >> 1) + (m_width >> 1)*(centx-1.0f)*zpos, (topy) >> 4, (m_width >> 1)*(2.0f)*zpos, (BottomY >> 4) -(topy >> 4));
					BottomY = topy;
				}

				zpos += zadd;
		 		y2--;
			}
		}
}