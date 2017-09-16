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
import com.github.epd.sprout.actors.Actor;
import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.buffs.Buff;
import com.github.epd.sprout.actors.buffs.Burning;
import com.github.epd.sprout.effects.BlobEmitter;
import com.github.epd.sprout.effects.particles.FlameParticle;
import com.github.epd.sprout.items.Heap;
import com.github.epd.sprout.items.journalpages.DragonCave;
import com.github.epd.sprout.items.journalpages.Vault;
import com.github.epd.sprout.items.misc.Spectacles.MagicSight;
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

		boolean shelf = false;

		boolean observe = false;

		for (int i = area.left - 1; i <= area.right; i++) {
			for (int j = area.top - 1; j <= area.bottom; j++) {
				cell = i + j * WIDTH;
				if (cur[cell] > 0) {

					burn(cell);

					fire = cur[cell] - 1;
					if (fire <= 0 && flamable[cell]) {

						if (Dungeon.level.map[cell] == Terrain.BOOKSHELF) {
							shelf = true;
						}

						int oldTile = Dungeon.level.map[cell];
						Level.set(cell, Terrain.EMBERS);


						if (shelf && Random.Float() < .02 && Dungeon.hero.buff(MagicSight.class) != null) {

							if (!Dungeon.limitedDrops.vaultpage.dropped()) {
								Dungeon.level.drop(new Vault(), cell);
								Dungeon.limitedDrops.vaultpage.drop();
							}
						}

						if (shelf && Random.Float() < .02 && Dungeon.hero.buff(MagicSight.class) != null) {

							if (!Dungeon.limitedDrops.dragoncave.dropped()) {
								Dungeon.level.drop(new DragonCave(), cell);
								Dungeon.limitedDrops.dragoncave.drop();
							}
						}

                    /*
                    if (shelf && Random.Float()<.02 && Dungeon.hero.buff(MagicSight.class) != null){

							if (Dungeon.limitedDrops.vaultpage.dropped()) {
								Dungeon.level.drop(new RoyalSpork(), pos);
							}

					}
					*/


						observe = true;
						GameScene.updateMap(cell);
						if (Dungeon.visible[cell]) {
							GameScene.discoverTile(cell, oldTile);
						}
					}

				} else {

					if (flamable[cell]
							&& (cur[cell - 1] > 0 || cur[cell + 1] > 0
							|| cur[cell - WIDTH] > 0 || cur[cell + WIDTH] > 0)) {
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
