package org.TAONGAD.DownhillDive;

import javax.microedition.khronos.opengles.GL10;

public class TextureOptions{

	public int magFilter, minFilter, texEnv;

	public TextureOptions(){
		magFilter = GL10.GL_NEAREST;
		minFilter = GL10.GL_NEAREST;
		texEnv = GL10.GL_MODULATE;
	}

	public TextureOptions(int minFilter, int magFilter, int texEnv){
		this.minFilter = minFilter;
		this.magFilter = magFilter;
		this.texEnv = texEnv;
	}

	public TextureOptions(int minFilter, int magFilter){
		this.minFilter = minFilter;
		this.magFilter = magFilter;
		texEnv = GL10.GL_MODULATE;
	}

}
