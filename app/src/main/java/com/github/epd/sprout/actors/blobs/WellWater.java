
package com.github.epd.sprout.actors.blobs;

import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.Journal;
import com.github.epd.sprout.Journal.Feature;
import com.github.epd.sprout.actors.hero.Hero;
import com.github.epd.sprout.items.Heap;
import com.github.epd.sprout.items.Item;
import com.github.epd.sprout.levels.Level;
import com.github.epd.sprout.levels.Terrain;
import com.github.epd.sprout.scenes.GameScene;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class WellWater extends Blob {

	public int pos;

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

		if (Dungeon.visible[pos]) {
			if (this instanceof WaterOfAwareness) {
				Journal.add(Feature.WELL_OF_AWARENESS);
			} else if (this instanceof WaterOfHealth) {
				Journal.add(Feature.WELL_OF_HEALTH);
			} else if (this instanceof WaterOfTransmutation) {
				Journal.add(Feature.WELL_OF_TRANSMUTATION);
			}
		}
	}


	protected boolean affect() {

		Heap heap;

		if (pos == Dungeon.hero.pos && affectHero(Dungeon.hero)) {

			volume = off[pos] = cur[pos] = 0;
			return true;

		} else if ((heap = Dungeon.level.heaps.get(pos)) != null) {

			Item oldItem = heap.peek();
			Item newItem = affectItem(oldItem);

			if (newItem != null) {

				if (newItem == oldItem) {

				} else if (oldItem.quantity() > 1) {

					oldItem.quantity(oldItem.quantity() - 1);
					heap.drop(newItem);

				} else {
					heap.replace(oldItem, newItem);
				}

				heap.sprite.link();
				volume = off[pos] = cur[pos] = 0;

				return true;

			} else {

				int newPlace;
				do {
					newPlace = pos + PathFinder.NEIGHBOURS8[Random.Int(8)];
				} while (!Level.passable[newPlace] && !Level.avoid[newPlace]);
				Dungeon.level.drop(heap.pickUp(), newPlace).sprite.drop(pos);

				return false;

			}

		} else {

			return false;

		}
	}

	protected boolean affectHero(Hero hero) {
		return false;
	}

	protected Item affectItem(Item item) {
		return null;
	}

	@Override
	public void seed(Level level, int cell, int amount) {
		if (cur == null) cur = new int[level.getLength()];
		if (off == null) off = new int[cur.length];
		cur[pos] = 0;
		pos = cell;
		volume = cur[pos] = amount;
	}

	public static void affectCell(int cell) {

		Class<?>[] waters = {WaterOfHealth.class, WaterOfAwareness.class,
				WaterOfTransmutation.class, WaterOfUpgradeEating.class};

		for (Class<?> waterClass : waters) {
			WellWater water = (WellWater) Dungeon.level.blobs.get(waterClass);
			if (water != null && water.volume > 0 && water.pos == cell
					&& water.affect()) {

				Level.set(cell, Terrain.EMPTY_WELL);
				GameScene.updateMap(cell);

				return;
			}
		}
	}

	public static boolean affectCellPlant(int cell) {

		boolean transmuted = false;

		Class<?>[] waters = {WaterOfHealth.class, WaterOfAwareness.class,
				WaterOfTransmutation.class, WaterOfUpgradeEating.class};

		for (Class<?> waterClass : waters) {
			WellWater water = (WellWater) Dungeon.level.blobs.get(waterClass);
			if (water != null && water.volume > 0 && water.pos == cell
					&& water.affect()) {

				GameScene.updateMap(cell);
				transmuted = true;
			}
		}
		return transmuted;
	}
}
