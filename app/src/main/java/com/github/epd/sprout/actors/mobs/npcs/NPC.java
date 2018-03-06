
package com.github.epd.sprout.actors.mobs.npcs;

import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.actors.mobs.Mob;
import com.github.epd.sprout.items.Heap;
import com.github.epd.sprout.levels.Level;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

// TODO: 增加一个允许进行批量转换物品的NPC（生命之书关卡）
public abstract class NPC extends Mob {

	{
		HP = HT = 1;
		EXP = 0;

		hostile = false;
		state = PASSIVE;
	}

	protected void throwItem() {
		Heap heap = Dungeon.level.heaps.get(pos);
		if (heap != null) {
			int n;
			do {
				n = pos + PathFinder.NEIGHBOURS8[Random.Int(8)];
			} while (!Level.passable[n] && !Level.avoid[n]);
			Dungeon.level.drop(heap.pickUp(), n).sprite.drop(pos);
		}
	}

	@Override
	public void beckon(int cell) {
	}

	abstract public boolean interact();
}