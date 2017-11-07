
package com.watabou.glwrap;

import android.opengl.GLES20;

public class Framebuffer {

	public static final int COLOR = GLES20.GL_COLOR_ATTACHMENT0;
	public static final int DEPTH = GLES20.GL_DEPTH_ATTACHMENT;
	public static final int STENCIL = GLES20.GL_STENCIL_ATTACHMENT;

	public static final Framebuffer system = new Framebuffer(0);

	private int id;

	public Framebuffer() {
		int[] buffers = new int[1];
		GLES20.glGenBuffers(1, buffers, 0);
		id = buffers[0];
	}

	private Framebuffer(int n) {

	}

	public void bind() {
		GLES20.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, id);
	}

	public void delete() {
		int[] buffers = {id};
		GLES20.glDeleteFramebuffers(1, buffers, 0);
	}

	public void attach(int point, Texture tex) {
		bind();
		GLES20.glFramebufferTexture2D(GLES20.GL_FRAMEBUFFER, point, GLES20.GL_TEXTURE_2D, tex.id, 0);
	}

	public void attach(int point, Renderbuffer buffer) {
		bind();
		GLES20.glFramebufferRenderbuffer(GLES20.GL_RENDERBUFFER, point, GLES20.GL_TEXTURE_2D, buffer.id());
	}

	public boolean status() {
		bind();
		return GLES20.glCheckFramebufferStatus(GLES20.GL_FRAMEBUFFER) == GLES20.GL_FRAMEBUFFER_COMPLETE;
	}
}
