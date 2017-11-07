
package com.github.epd.sprout.levels.traps;

import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.buffs.Bleeding;
import com.github.epd.sprout.actors.buffs.Buff;
import com.github.epd.sprout.actors.buffs.Cripple;
import com.github.epd.sprout.effects.Wound;
import com.watabou.utils.Random;

public class GrippingTrap {

	public static void trigger(int pos, Char c) {

		if (c != null) {
			int damage = Math.max(0,
					(Dungeon.depth + 3) - Random.IntRange(0, c.dr() / 2));
			Buff.affect(c, Bleeding.class).set(damage);
			Buff.prolong(c, Cripple.class, Cripple.DURATION);
			Wound.hit(c);
		} else {
			Wound.hit(pos);
		}

	}
}
