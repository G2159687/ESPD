
package com.watabou.glwrap;

import android.opengl.GLES20;

import java.nio.FloatBuffer;
import java.util.ArrayList;

public class Vertexbuffer {

	private int id;
	private FloatBuffer vertices;
	private int updateStart, updateEnd;

	private static final ArrayList<Vertexbuffer> buffers = new ArrayList<>();

	public Vertexbuffer(FloatBuffer vertices) {
		synchronized (buffers) {
			int[] ptr = new int[1];
			GLES20.glGenBuffers(1, ptr, 0);
			id = ptr[0];

			this.vertices = vertices;
			buffers.add(this);

			updateStart = 0;
			updateEnd = vertices.limit();
		}
	}

	//For flagging the buffer for a full update without changing anything
	public void updateVertices() {
		updateVertices(vertices);
	}

	//For flagging an update with a full set of new data
	public void updateVertices(FloatBuffer vertices) {
		updateVertices(vertices, 0, vertices.limit());
	}

	//For flagging an update with a subset of data changed
	public void updateVertices(FloatBuffer vertices, int start, int end) {
		this.vertices = vertices;

		if (updateStart == -1)
			updateStart = start;
		else
			updateStart = Math.min(start, updateStart);

		if (updateEnd == -1)
			updateEnd = end;
		else
			updateEnd = Math.max(end, updateEnd);
	}

	public void updateGLData() {
		if (updateStart == -1) return;

		vertices.position(updateStart);
		bind();

		if (updateStart == 0 && updateEnd == vertices.limit()) {
			GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, vertices.limit() * 4, vertices, GLES20.GL_DYNAMIC_DRAW);
		} else {
			GLES20.glBufferSubData(GLES20.GL_ARRAY_BUFFER, updateStart * 4, (updateEnd - updateStart) * 4, vertices);
		}

		release();
		updateStart = updateEnd = -1;
	}

	public void updateGLData2() {
		if (updateStart == -1) return;

		vertices.position(updateStart);
		bind();

		GLES20.glBufferData(GLES20.GL_ARRAY_BUFFER, vertices.limit() * 4, vertices, GLES20.GL_DYNAMIC_DRAW);

		release();
		updateStart = updateEnd = -1;
	}

	public void bind() {
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, id);
	}

	public void release() {
		GLES20.glBindBuffer(GLES20.GL_ARRAY_BUFFER, 0);
	}

	public void delete() {
		synchronized (buffers) {
			GLES20.glDeleteBuffers(1, new int[]{id}, 0);
			buffers.remove(this);
		}
	}

	public static void refreshAllBuffers() {
		synchronized (buffers) {
			for (Vertexbuffer buf : buffers) {
				buf.updateVertices();
				buf.updateGLData2();
			}
		}
	}

}
