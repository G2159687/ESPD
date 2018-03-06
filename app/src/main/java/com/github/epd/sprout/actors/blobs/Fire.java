
package com.github.epd.sprout.actors.blobs;

import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.actors.Actor;
import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.buffs.Buff;
import com.github.epd.sprout.actors.buffs.Burning;
import com.github.epd.sprout.effects.BlobEmitter;
import com.github.epd.sprout.effects.particles.FlameParticle;
import com.github.epd.sprout.items.Heap;
import com.github.epd.sprout.items.misc.Spectacles.MagicSight;
import com.github.epd.sprout.items.teleporter.DragonCave;
import com.github.epd.sprout.items.teleporter.Vault;
import com.github.epd.sprout.levels.Level;
import com.github.epd.sprout.levels.Terrain;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.scenes.GameScene;
import com.watabou.utils.Random;

public class Fire extends Blob {

	@Override
	protected void evolve() {

		boolean[] flamable = Level.flamable;

		int cell;
		int fire;

		boolean observe = false;

		for (int i = area.left - 1; i <= area.right; i++) {
			for (int j = area.top - 1; j <= area.bottom; j++) {
				cell = i + j * Dungeon.level.getWidth();
				if (cur[cell] > 0) {

					burn(cell);

					fire = cur[cell] - 1;
					if (fire <= 0 && flamable[cell]) {

						int oldTile = Dungeon.level.map[cell];
						Level.set(cell, Terrain.EMBERS);

						observe = true;
						GameScene.updateMap(cell);
						if (Dungeon.visible[cell]) {
							GameScene.discoverTile(cell, oldTile);
						}
					}

				} else {

					if (flamable[cell]
							&& (cur[cell - 1] > 0 || cur[cell + 1] > 0
							|| cur[cell - Dungeon.level.getWidth()] > 0 || cur[cell + Dungeon.level.getWidth()] > 0)) {
						fire = 4;
						burn(cell);
						area.union(i, j);
					} else {
						fire = 0;
					}

				}

				volume += (off[cell] = fire);

			}

			if (observe) {
				Dungeon.observe();
			}
		}
	}

	private void burn(int pos) {
		Char ch = Actor.findChar(pos);
		if (ch != null) {
			Buff.affect(ch, Burning.class).reignite(ch);
		}

		Heap heap = Dungeon.level.heaps.get(pos);
		if (heap != null) {
			heap.burn();
		}
	}

	@Override
	public void use(BlobEmitter emitter) {
		super.use(emitter);
		emitter.start(FlameParticle.FACTORY, 0.03f, 0);
	}

	@Override
	public String tileDesc() {
		return Messages.get(this, "desc");
	}
}
