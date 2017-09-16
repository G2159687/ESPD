/*
 * Pixel Dungeon
 * Copyright (C) 2012-2014  Oleg Dolya
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package com.github.epd.sprout.actors.blobs;

import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.ShatteredPixelDungeon;
import com.github.epd.sprout.actors.Actor;
import com.github.epd.sprout.effects.BlobEmitter;
import com.github.epd.sprout.levels.Level;
import com.watabou.utils.Bundle;
import com.watabou.utils.Rect;

import java.util.Arrays;

public class Blob extends Actor {

	{
		actPriority = 1; //take prioerity over mobs, but not the hero
	}

	public static final int WIDTH = Level.getWidth();
	public static final int HEIGHT = Level.HEIGHT;
	public static final int LENGTH = Level.getLength();

	public int volume = 0;

	public int[] cur;
	protected int[] off;

	public BlobEmitter emitter;

	public Rect area = new Rect();

	protected Blob() {

		cur = new int[LENGTH];
		off = new int[LENGTH];

		volume = 0;
	}

	private static final String CUR = "cur";
	private static final String START = "start";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);

		if (volume > 0) {

			int start;
			for (start = 0; start < LENGTH; start++) {
				if (cur[start] > 0) {
					break;
				}
			}
			int end;
			for (end = LENGTH - 1; end > start; end--) {
				if (cur[end] > 0) {
					break;
				}
			}

			bundle.put(START, start);
			bundle.put(CUR, trim(start, end + 1));

		}
	}

	private int[] trim(int start, int end) {
		int len = end - start;
		int[] copy = new int[len];
		System.arraycopy(cur, start, copy, 0, len);
		return copy;
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {

		super.restoreFromBundle(bundle);

		int[] data = bundle.getIntArray(CUR);
		if (data != null) {
			int start = bundle.getInt(START);
			for (int i = 0; i < data.length; i++) {
				cur[i + start] = data[i];
				volume += data[i];
			}
		}

		if (Level.resizingNeeded) {
			int[] cur = new int[Level.getLength()];
			Arrays.fill(cur, 0);

			int loadedMapSize = Level.loadedMapSize;
			for (int i = 0; i < loadedMapSize; i++) {
				System.arraycopy(this.cur, i * loadedMapSize, cur, i
						* Level.getWidth(), loadedMapSize);
			}

			this.cur = cur;
		}
	}

	@Override
	public boolean act() {

		spend(TICK);

		if (volume > 0) {

			if (area.isEmpty())
				setupArea();

			volume = 0;
			evolve();

			int[] tmp = off;
			off = cur;
			cur = tmp;

		} else {
			area.setEmpty();
		}

		return true;
	}

	public void setupArea() {
		for (int cell = 0; cell < cur.length; cell++) {
			if (cur[cell] != 0) {
				area.union(cell % Level.WIDTH, cell / Level.WIDTH);
			}
		}
	}

	public void use(BlobEmitter emitter) {
		this.emitter = emitter;
	}

	protected void evolve() {

		boolean[] blocking = Level.solid;
		int cell;
		for (int i = area.top - 1; i <= area.bottom; i++) {
			for (int j = area.left - 1; j <= area.right; j++) {
				cell = j + i * WIDTH;
				if (Dungeon.level.insideMap(cell)) {
					if (!blocking[cell]) {

						int count = 1;
						int sum = cur[cell];

						if (j > area.left && !blocking[cell - 1]) {
							sum += cur[cell - 1];
							count++;
						}
						if (j < area.right && !blocking[cell + 1]) {
							sum += cur[cell + 1];
							count++;
						}
						if (i > area.top && !blocking[cell - WIDTH]) {
							sum += cur[cell - WIDTH];
							count++;
						}
						if (i < area.bottom && !blocking[cell + WIDTH]) {
							sum += cur[cell + WIDTH];
							count++;
						}

						int value = sum >= count ? (sum / count) - 1 : 0;
						off[cell] = value;

						if (value > 0) {
							if (i < area.top)
								area.top = i;
							else if (i >= area.bottom)
								area.bottom = i + 1;
							if (j < area.left)
								area.left = j;
							else if (j >= area.right)
								area.right = j + 1;
						}

						volume += value;
					} else {
						off[cell] = 0;
					}
				}
			}
		}
	}

	public void seed(int cell, int amount) {
		cur[cell] += amount;
		volume += amount;

		area.union(cell % WIDTH, cell / WIDTH);
	}

	public void clear(int cell) {
		volume -= cur[cell];
		cur[cell] = 0;
	}

	public String tileDesc() {
		return null;
	}

	@SuppressWarnings("unchecked")
	public static <T extends Blob> T seed(int cell, int amount, Class<T> type) {
		try {

			T gas = (T) Dungeon.level.blobs.get(type);
			if (gas == null) {
				gas = type.newInstance();
				Dungeon.level.blobs.put(type, gas);
			}

			gas.seed(cell, amount);

			return gas;

		} catch (Exception e) {
			ShatteredPixelDungeon.reportException(e);
			return null;
		}
	}

	public static int volumeAt( int cell, Class<? extends Blob> type){
		Blob gas = Dungeon.level.blobs.get( type );
		if (gas == null) {
			return 0;
		} else {
			return gas.cur[cell];
		}
	}
}
