
package com.watabou.glwrap;

import android.opengl.GLES20;

public class Renderbuffer {

	public static final int RGBA8 = GLES20.GL_RGBA;    // ?
	public static final int DEPTH16 = GLES20.GL_DEPTH_COMPONENT16;
	public static final int STENCIL8 = GLES20.GL_STENCIL_INDEX8;

	private int id;

	public Renderbuffer() {
		int[] buffers = new int[1];
		GLES20.glGenRenderbuffers(1, buffers, 0);
		id = buffers[0];
	}

	public int id() {
		return id;
	}

	public void bind() {
		GLES20.glBindRenderbuffer(GLES20.GL_RENDERBUFFER, id);
	}

	public void delete() {
		int[] buffers = {id};
		GLES20.glDeleteRenderbuffers(1, buffers, 0);
	}

	public void storage(int format, int width, int height) {
		GLES20.glRenderbufferStorage(GLES20.GL_RENDERBUFFER, format, width, height);
	}
}
