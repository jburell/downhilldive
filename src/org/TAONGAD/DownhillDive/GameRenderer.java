package org.TAONGAD.DownhillDive;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;
import javax.microedition.khronos.opengles.GL11;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.opengl.GLU;
import android.opengl.GLUtils;
import android.opengl.GLSurfaceView.Renderer;
import android.os.Build;
import android.util.Log;

public class GameRenderer implements Renderer{

	public static ByteBuffer backgroundVertex;
	public static ByteBuffer vertexBuffer;

	public void onSurfaceCreated(GL10 gl, EGLConfig config){
		gl.glViewport(0,
			0,
			DownhillDive.getInstance().getWidth(),
			DownhillDive.getInstance().getHeight());
		gl.glHint(GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST);
		gl.glClearColor(0, 0, 0, 1);
		gl.glShadeModel(GL10.GL_FLAT);
		gl.glDisable(GL10.GL_DEPTH_TEST);
		gl.glEnable(GL10.GL_TEXTURE_2D);
		gl.glDisable(GL10.GL_DITHER);
		gl.glDisable(GL10.GL_LIGHTING);
		gl.glClear(GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT);
		gl.glEnableClientState(GL11.GL_VERTEX_ARRAY);
		gl.glEnableClientState(GL10.GL_TEXTURE_COORD_ARRAY);
		GLU.gluOrtho2D(gl,
			0,
			DownhillDive.getInstance().getWidth(),
			DownhillDive.getInstance().getHeight(),
			0);

		if(DownhillDive.getInstance().isLandscape()){
			if(Build.VERSION.SDK_INT == 3){
				backgroundVertex = ByteBuffer.allocate(8 * 4);
			}else{
				backgroundVertex = ByteBuffer.allocateDirect(8 * 4);
			}

			backgroundVertex.order(ByteOrder.nativeOrder());
			backgroundVertex.position(0);
			backgroundVertex.putInt(0);
			backgroundVertex.putInt(0);
			backgroundVertex.putInt(DownhillDive.getInstance().getWidth());
			backgroundVertex.putInt(0);
			backgroundVertex.putInt(0);
			backgroundVertex.putInt(DownhillDive.getInstance().getHeight());
			backgroundVertex.putInt(DownhillDive.getInstance().getWidth());
			backgroundVertex.putInt(DownhillDive.getInstance().getHeight());
			backgroundVertex.position(0);
		}else{
			if(Build.VERSION.SDK_INT == 3){
				backgroundVertex = ByteBuffer.allocate(8 * 4);
			}else{
				backgroundVertex = ByteBuffer.allocateDirect(8 * 4);
			}
			backgroundVertex.order(ByteOrder.nativeOrder());
			backgroundVertex.position(0);
			backgroundVertex.putInt(0);
			backgroundVertex.putInt(0);
			backgroundVertex.putInt(DownhillDive.getInstance().getHeight());
			backgroundVertex.putInt(0);
			backgroundVertex.putInt(0);
			backgroundVertex.putInt(DownhillDive.getInstance().getWidth());
			backgroundVertex.putInt(DownhillDive.getInstance().getHeight());
			backgroundVertex.putInt(DownhillDive.getInstance().getWidth());
			backgroundVertex.position(0);
		}

		if(Build.VERSION.SDK_INT == 3){
			backgroundVertex = ByteBuffer.allocate(8 * 4);
		}else{
			vertexBuffer = ByteBuffer.allocateDirect(8 * 4);
		}

		vertexBuffer.order(ByteOrder.nativeOrder());
		vertexBuffer.position(0);
		vertexBuffer.putFloat(0);
		vertexBuffer.putFloat(0);
		vertexBuffer.putFloat(1);
		vertexBuffer.putFloat(0);
		vertexBuffer.putFloat(0);
		vertexBuffer.putFloat(1);
		vertexBuffer.putFloat(1);
		vertexBuffer.putFloat(1);
		vertexBuffer.position(0);
	}

	public void onSurfaceChanged(GL10 gl, int width, int height){
		gl.glViewport(0, 0, width, height);
		gl.glMatrixMode(GL10.GL_PROJECTION);
		gl.glLoadIdentity();
		gl.glEnable(GL10.GL_BLEND);
		gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		gl.glShadeModel(GL10.GL_FLAT);
		gl.glEnable(GL10.GL_TEXTURE_2D);
		GLU.gluOrtho2D(gl,
			0,
			DownhillDive.getInstance().getWidth(),
			DownhillDive.getInstance().getHeight(),
			0);
		gl.glVertexPointer(2, GL11.GL_FLOAT, 0, vertexBuffer);
	}

	private boolean _hasLoaded = false;
	public void onDrawFrame(final GL10 gl){
		if(DownhillDive.getInstance().isLoading()){
			if(_hasLoaded){
				gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
				gl.glMatrixMode(GL10.GL_MODELVIEW);

				gl.glLoadIdentity();
				gl.glScalef(DownhillDive.getInstance().getWidth(),
					DownhillDive.getInstance().getHeight(),
					0);
				gl.glColor4f(1, 1, 1, 1);
				gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);
				return;
			}
			Bitmap tbmp = null;
			try{
				tbmp = BitmapFactory.decodeStream(DownhillDive.getInstance().getAssets().open(DownhillDive.getInstance().getLoadingImage()));
			}catch(IOException e){
				Log.e("RENDERER", "LOADING SCREEN not found");
				e.printStackTrace();
			}
			Bitmap bmp = null;
			float realWidth, realHeight;
			if(tbmp == null){
				Log.e("RENDERER", "LOADING SCREEN Error!");
				System.exit(0);
				return;
			}else{
				if(tbmp.getWidth() > 512 || tbmp.getHeight() > 512){
					bmp = Bitmap.createBitmap(1024, 1024, Bitmap.Config.ARGB_8888);
				}else{
					bmp = Bitmap.createBitmap(512, 512, Bitmap.Config.ARGB_8888);
				}

				realWidth = tbmp.getWidth();
				realHeight = tbmp.getHeight();
				Canvas canvas = new Canvas(bmp);
				canvas.drawBitmap(tbmp, 0, 0, new Paint());
			}

			int[] tmp_tex = new int[1];
			gl.glGenTextures(1, tmp_tex, 0);
			int tex = tmp_tex[0];
			gl.glBindTexture(GL10.GL_TEXTURE_2D, tex);
			gl.glTexParameterf(GL10.GL_TEXTURE_2D,
				GL10.GL_TEXTURE_MIN_FILTER,
				GL10.GL_NEAREST);
			gl.glTexParameterf(GL10.GL_TEXTURE_2D,
				GL10.GL_TEXTURE_MAG_FILTER,
				GL10.GL_NEAREST);
			gl.glTexParameterf(GL10.GL_TEXTURE_2D,
				GL10.GL_TEXTURE_WRAP_S,
				GL10.GL_CLAMP_TO_EDGE);
			gl.glTexParameterf(GL10.GL_TEXTURE_2D,
				GL10.GL_TEXTURE_WRAP_T,
				GL10.GL_CLAMP_TO_EDGE);
			gl.glTexEnvf(GL10.GL_TEXTURE_ENV,
				GL10.GL_TEXTURE_ENV_MODE,
				GL10.GL_MODULATE);
			GLUtils.texImage2D(GL10.GL_TEXTURE_2D, 0, bmp, 0);

			gl.glClearColor(0.6f, 0.6f, 1, 1);
			gl.glVertexPointer(2, GL11.GL_FLOAT, 0, vertexBuffer);

			ByteBuffer texBuffer;
			if(Build.VERSION.SDK_INT == 3){
				texBuffer = ByteBuffer.allocate(8 * 4);
			}else{
				texBuffer = ByteBuffer.allocateDirect(8 * 4);
			}

			texBuffer.order(ByteOrder.nativeOrder());

			texBuffer.position(0);

			texBuffer.putFloat(0);
			texBuffer.putFloat(0);

			float height = bmp.getHeight();
			float width = bmp.getWidth();

			if(DownhillDive.getInstance().isLandscape()){
				texBuffer.putFloat(realWidth / width);
				texBuffer.putFloat(0);
				texBuffer.putFloat(0);
				texBuffer.putFloat(realHeight / height);
				texBuffer.putFloat(realWidth / width);
				texBuffer.putFloat(realHeight / height);
			}else{
				texBuffer.putFloat(realHeight / height);
				texBuffer.putFloat(0);
				texBuffer.putFloat(0);
				texBuffer.putFloat(realWidth / width);
				texBuffer.putFloat(realHeight / height);
				texBuffer.putFloat(realWidth / width);
			}

			texBuffer.position(0);

			gl.glTexCoordPointer(2, GL10.GL_FLOAT, 0, texBuffer);

			gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
			gl.glMatrixMode(GL10.GL_MODELVIEW);

			gl.glLoadIdentity();
			gl.glScalef(DownhillDive.getInstance().getWidth(),
				DownhillDive.getInstance().getHeight(),
				0);
			gl.glColor4f(1, 1, 1, 1);
			gl.glDrawArrays(GL10.GL_TRIANGLE_STRIP, 0, 4);

			new Thread(new Runnable(){
				public void run(){
					((DownhillDive)DownhillDive.getInstance()).doLoading(gl);
				}
			}).start();
			_hasLoaded = true;
			bmp.recycle();
			bmp = null;
			System.gc();
		}else{
			TextureManager.updateTextures(gl);
			gl.glClear(GL10.GL_COLOR_BUFFER_BIT);
			gl.glMatrixMode(GL10.GL_MODELVIEW);
			DownhillDive.getInstance().drawFrame(gl);
		}
	}

}