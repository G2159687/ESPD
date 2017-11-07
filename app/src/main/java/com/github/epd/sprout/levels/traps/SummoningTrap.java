
package com.github.epd.sprout.levels.traps;

import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.actors.Actor;
import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.mobs.Bestiary;
import com.github.epd.sprout.actors.mobs.Mob;
import com.github.epd.sprout.items.Heap;
import com.github.epd.sprout.items.wands.WandOfBlink;
import com.github.epd.sprout.levels.Level;
import com.github.epd.sprout.scenes.GameScene;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class SummoningTrap {

	private static final float DELAY = 2f;

	private static final Mob DUMMY = new Mob() {
	};

	// 0x770088

	public static void trigger(int pos, Char c) {

		if (Dungeon.bossLevel()) {
			return;
		}

		int nMobs = 1;
		if (Random.Int(2) == 0) {
			nMobs++;
			if (Random.Int(2) == 0) {
				nMobs++;
			}
		}

		// It's complicated here, because these traps can be activated in chain

		ArrayList<Integer> candidates = new ArrayList<Integer>();

		for (int i = 0; i < PathFinder.NEIGHBOURS8.length; i++) {
			int p = pos + PathFinder.NEIGHBOURS8[i];
			if (Actor.findChar(p) == null
					&& (Level.passable[p] || Level.avoid[p])) {
				candidates.add(p);
			}
		}

		ArrayList<Integer> respawnPoints = new ArrayList<Integer>();

		while (nMobs > 0 && candidates.size() > 0) {
			int index = Random.index(candidates);

			DUMMY.pos = candidates.get(index);
			respawnPoints.add(candidates.remove(index));
			nMobs--;
		}

		for (Integer point : respawnPoints) {
			Mob mob = Bestiary.mob(Dungeon.depth);
			mob.state = mob.WANDERING;
			GameScene.add(mob, DELAY);
			WandOfBlink.appear(mob, point);
		}

		Heap heap = Dungeon.level.heaps.get(pos);
		if (heap != null) {
			heap.summon();
		}
	}
}
