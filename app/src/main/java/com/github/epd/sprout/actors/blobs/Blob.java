
package com.github.epd.sprout.actors.blobs;

import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.ShatteredPixelDungeon;
import com.github.epd.sprout.actors.Actor;
import com.github.epd.sprout.effects.BlobEmitter;
import com.github.epd.sprout.levels.Level;
import com.watabou.utils.Bundle;
import com.watabou.utils.Rect;

public class Blob extends Actor {

	{
		actPriority = 1; //take prioerity over mobs, but not the hero
	}

	public int volume = 0;

	public int[] cur;
	protected int[] off;

	public BlobEmitter emitter;

	public Rect area = new Rect();

	private static final String CUR = "cur";
	private static final String START = "start";
	private static final String LENGTH	= "length";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);

		if (volume > 0) {

			int start;
			for (start = 0; start < Dungeon.level.getLength(); start++) {
				if (cur[start] > 0) {
					break;
				}
			}
			int end;
			for (end = Dungeon.level.getLength() - 1; end > start; end--) {
				if (cur[end] > 0) {
					break;
				}
			}

			bundle.put(START, start);
			bundle.put( LENGTH, cur.length );
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
	public void restoreFromBundle( Bundle bundle ) {

		super.restoreFromBundle( bundle );

		if (bundle.contains( CUR )) {

			if (bundle.contains(LENGTH)) {
				cur = new int[bundle.getInt(LENGTH)];
			} else {
				//compatability with pre-0.4.2
				cur = new int[1024];
			}
			off = new int[cur.length];

			int[] data = bundle.getIntArray(CUR);
			int start = bundle.getInt(START);
			for (int i = 0; i < data.length; i++) {
				cur[i + start] = data[i];
				volume += data[i];
			}

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
				area.union(cell % Dungeon.level.getWidth(), cell / Dungeon.level.getWidth());
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
				cell = j + i * Dungeon.level.getWidth();
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
						if (i > area.top && !blocking[cell - Dungeon.level.getWidth()]) {
							sum += cur[cell - Dungeon.level.getWidth()];
							count++;
						}
						if (i < area.bottom && !blocking[cell + Dungeon.level.getWidth()]) {
							sum += cur[cell + Dungeon.level.getWidth()];
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

	public void seed( Level level, int cell, int amount ) {
		if (cur == null) cur = new int[level.getLength()];
		if (off == null) off = new int[cur.length];

		cur[cell] += amount;
		volume += amount;

		area.union(cell%level.getWidth(), cell/level.getWidth());
	}

	public void clear( int cell ) {
		if (volume == 0) return;
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

			gas.seed(Dungeon.level, cell, amount);

			return gas;

		} catch (Exception e) {
			ShatteredPixelDungeon.reportException(e);
			return null;
		}
	}

	public static int volumeAt( int cell, Class<? extends Blob> type){
		Blob gas = Dungeon.level.blobs.get( type );
		if (gas == null || gas.volume == 0) {
			return 0;
		} else {
			return gas.cur[cell];
		}
	}
}
