
package com.github.epd.sprout.levels.painters;

import com.github.epd.sprout.levels.Level;
import com.github.epd.sprout.levels.rooms.Room;
import com.watabou.utils.Point;
import com.watabou.utils.Rect;

import java.util.ArrayList;
import java.util.Arrays;

public abstract class Painter {

	//If painters require additional parameters, they should
	// request them in their constructor or other methods

	//Painters take a level and its collection of rooms, and paint all the specific tile values
	public abstract boolean paint(Level level, ArrayList<Room> rooms);

	// Static methods

	public static void set(Level level, int cell, int value) {
		level.map[cell] = value;
	}

	public static void set(Level level, int x, int y, int value) {
		set(level, x + y * level.getWidth(), value);
	}

	public static void set(Level level, Point p, int value) {
		set(level, p.x, p.y, value);
	}

	public static void fill(Level level, int x, int y, int w, int h, int value) {

		int width = level.getWidth();

		int pos = y * width + x;
		for (int i = y; i < y + h; i++, pos += width) {
			Arrays.fill(level.map, pos, pos + w, value);
		}
	}

	public static void fill(Level level, Rect rect, int value) {
		fill(level, rect.left, rect.top, rect.width(), rect.height(), value);
	}

	public static void fill(Level level, Rect rect, int m, int value) {
		fill(level, rect.left + m, rect.top + m, rect.width() - m * 2, rect.height() - m * 2, value);
	}

	public static void fill(Level level, Rect rect, int l, int t, int r, int b,
	                        int value) {
		fill(level, rect.left + l, rect.top + t, rect.width() - (l + r), rect.height() - (t + b), value);
	}

	public static void drawLine( Level level, Point from, Point to, int value){
		float x = from.x;
		float y = from.y;
		float dx = to.x - from.x;
		float dy = to.y - from.y;

		boolean movingbyX = Math.abs(dx) >= Math.abs(dy);
		//normalize
		if (movingbyX){
			dy /= Math.abs(dx);
			dx /= Math.abs(dx);
		} else {
			dx /= Math.abs(dy);
			dy /= Math.abs(dy);
		}

		set(level, Math.round(x), Math.round(y), value);
		while((movingbyX && to.x != x) || (!movingbyX && to.y != y)){
			x += dx;
			y += dy;
			set(level, Math.round(x), Math.round(y), value);
		}
	}

	public static Point drawInside(Level level, Room room, Point from, int n,
	                               int value) {

		Point step = new Point();
		if (from.x == room.left) {
			step.set(+1, 0);
		} else if (from.x == room.right) {
			step.set(-1, 0);
		} else if (from.y == room.top) {
			step.set(0, +1);
		} else if (from.y == room.bottom) {
			step.set(0, -1);
		}

		Point p = new Point(from).offset(step);
		for (int i = 0; i < n; i++) {
			if (value != -1) {
				set(level, p, value);
			}
			p.offset(step);
		}

		return p;
	}
}
