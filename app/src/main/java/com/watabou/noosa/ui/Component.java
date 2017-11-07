
package com.watabou.noosa.ui;

import com.watabou.noosa.Group;

public class Component extends Group {

	protected float x;
	protected float y;
	protected float width;
	protected float height;

	public Component() {
		super();
		createChildren();
	}

	public Component setPos(float x, float y) {
		this.x = x;
		this.y = y;
		layout();

		return this;
	}

	public Component setSize(float width, float height) {
		this.width = width;
		this.height = height;
		layout();

		return this;
	}

	public Component setRect(float x, float y, float width, float height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		layout();

		return this;
	}

	public boolean inside(float x, float y) {
		return x >= this.x && y >= this.y && x < this.x + width && y < this.y + height;
	}

	public void fill(Component c) {
		setRect(c.x, c.y, c.width, c.height);
	}

	public float left() {
		return x;
	}

	public float right() {
		return x + width;
	}

	public float centerX() {
		return x + width / 2;
	}

	public float top() {
		return y;
	}

	public float bottom() {
		return y + height;
	}

	public float centerY() {
		return y + height / 2;
	}

	public float width() {
		return width;
	}

	public float height() {
		return height;
	}

	protected void createChildren() {
	}

	protected void layout() {
	}
}
