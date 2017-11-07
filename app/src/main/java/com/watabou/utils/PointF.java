
package com.watabou.utils;

import android.annotation.SuppressLint;

@SuppressLint("FloatMath")
public class PointF {

	public static final float PI = 3.1415926f;
	public static final float PI2 = PI * 2;
	public static final float G2R = PI / 180;

	public float x;
	public float y;

	public PointF() {
	}

	public PointF(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public PointF(PointF p) {
		this.x = p.x;
		this.y = p.y;
	}

	public PointF(Point p) {
		this.x = p.x;
		this.y = p.y;
	}

	public PointF clone() {
		return new PointF(this);
	}

	public PointF scale(float f) {
		this.x *= f;
		this.y *= f;
		return this;
	}

	public PointF invScale(float f) {
		this.x /= f;
		this.y /= f;
		return this;
	}

	public PointF set(float x, float y) {
		this.x = x;
		this.y = y;
		return this;
	}

	public PointF set(PointF p) {
		this.x = p.x;
		this.y = p.y;
		return this;
	}

	public PointF set(float v) {
		this.x = v;
		this.y = v;
		return this;
	}

	public PointF polar(float a, float l) {
		this.x = l * (float) Math.cos(a);
		this.y = l * (float) Math.sin(a);
		return this;
	}

	public PointF offset(float dx, float dy) {
		x += dx;
		y += dy;
		return this;
	}

	public PointF offset(PointF p) {
		x += p.x;
		y += p.y;
		return this;
	}

	public PointF negate() {
		x = -x;
		y = -y;
		return this;
	}

	public PointF normalize() {
		float l = length();
		x /= l;
		y /= l;
		return this;
	}

	public Point floor() {
		return new Point((int) x, (int) y);
	}

	public float length() {
		return (float) Math.sqrt(x * x + y * y);
	}

	public static PointF sum(PointF a, PointF b) {
		return new PointF(a.x + b.x, a.y + b.y);
	}

	public static PointF diff(PointF a, PointF b) {
		return new PointF(a.x - b.x, a.y - b.y);
	}

	public static PointF inter(PointF a, PointF b, float d) {
		return new PointF(a.x + (b.x - a.x) * d, a.y + (b.y - a.y) * d);
	}

	public static float distance(PointF a, PointF b) {
		float dx = a.x - b.x;
		float dy = a.y - b.y;
		return (float) Math.sqrt(dx * dx + dy * dy);
	}

	public static float angle(PointF start, PointF end) {
		return (float) Math.atan2(end.y - start.y, end.x - start.x);
	}

	@Override
	public String toString() {
		return "" + x + ", " + y;
	}

	@Override
	public boolean equals(Object o) {
		if (super.equals(o))
			return true;
		return o instanceof PointF && (((PointF) o).x == x && ((PointF) o).y == y);
	}
}
