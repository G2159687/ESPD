
package com.github.epd.sprout.actors.blobs;

import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.effects.BlobEmitter;
import com.github.epd.sprout.effects.Speck;
import com.github.epd.sprout.items.Heap;
import com.github.epd.sprout.items.weapon.Weapon;
import com.github.epd.sprout.levels.Level;
import com.watabou.utils.Bundle;

public class Alter extends Blob {

	protected int pos;

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);

		if (volume > 0)
			for (int i = 0; i < cur.length; i++) {
				if (cur[i] > 0) {
					pos = i;
					break;
				}
			}
	}

	@Override
	protected void evolve() {
		volume = off[pos] = cur[pos];
		area.union(pos % Dungeon.level.getWidth(), pos / Dungeon.level.getWidth());
	}

	@Override
	public void seed( Level level, int cell, int amount ) {
		super.seed(level, cell, amount);

		cur[pos] = 0;
		pos = cell;
		volume = cur[pos] = amount;

		area.setEmpty();
		area.union(cell%level.getWidth(), cell/level.getWidth());
	}


	public static void transmute(int cell) {
		Heap heap = Dungeon.level.heaps.get(cell);
		if (heap != null) {

			Weapon result = heap.consecrate();
			if (result != null) {
				Dungeon.level.drop(result, cell).sprite.drop(cell);
			}
		}
	}

	@Override
	public void use(BlobEmitter emitter) {
		super.use(emitter);
		emitter.start(Speck.factory(Speck.LIGHT), 0.4f, 0);
	}
}
