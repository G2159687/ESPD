
package com.watabou.glwrap;

public class Matrix {

	public static final float G2RAD = 0.01745329251994329576923690768489f;

	public static float[] clone(float[] m) {

		int n = m.length;
		float[] res = new float[n];
		do {
			res[--n] = m[n];
		} while (n > 0);

		return res;
	}

	public static void copy(float[] src, float[] dst) {

		int n = src.length;
		do {
			dst[--n] = src[n];
		} while (n > 0);

	}

	private static float[] identity = new float[]{
			1, 0, 0, 0,
			0, 1, 0, 0,
			0, 0, 1, 0,
			0, 0, 0, 1
	};

	public static void setIdentity(float[] m) {
		System.arraycopy(identity, 0, m, 0, identity.length);
	}

	public static void rotate(float[] m, float a) {
		a *= G2RAD;
		float sin = (float) Math.sin(a);
		float cos = (float) Math.cos(a);
		float m0 = m[0];
		float m1 = m[1];
		float m4 = m[4];
		float m5 = m[5];
		m[0] = m0 * cos + m4 * sin;
		m[1] = m1 * cos + m5 * sin;
		m[4] = -m0 * sin + m4 * cos;
		m[5] = -m1 * sin + m5 * cos;
	}

	public static void skewX(float[] m, float a) {
		double t = Math.tan(a * G2RAD);
		m[4] += -m[0] * t;
		m[5] += -m[1] * t;
	}

	public static void skewY(float[] m, float a) {
		double t = Math.tan(a * G2RAD);
		m[0] += m[4] * t;
		m[1] += m[5] * t;
	}

	public static void scale(float[] m, float x, float y) {
		m[0] *= x;
		m[1] *= x;
		m[2] *= x;
		m[3] *= x;
		m[4] *= y;
		m[5] *= y;
		m[6] *= y;
		m[7] *= y;
		//	android.opengl.Matrix.scaleM( m, 0, x, y, 1 );
	}

	public static void translate(float[] m, float x, float y) {
		m[12] += m[0] * x + m[4] * y;
		m[13] += m[1] * x + m[5] * y;
	}

	public static void multiply(float[] left, float right[], float[] result) {
		android.opengl.Matrix.multiplyMM(result, 0, left, 0, right, 0);
	}
}