
package com.github.epd.sprout.items.rings;

import com.github.epd.sprout.actors.blobs.ToxicGas;
import com.github.epd.sprout.actors.buffs.Burning;
import com.github.epd.sprout.actors.buffs.Poison;
import com.github.epd.sprout.actors.mobs.Eye;
import com.github.epd.sprout.actors.mobs.Warlock;
import com.github.epd.sprout.actors.mobs.Yog;
import com.github.epd.sprout.levels.traps.LightningTrap;
import com.github.epd.sprout.messages.Messages;
import com.watabou.utils.Random;

import java.util.HashSet;

public class RingOfElements extends Ring {

	{
		name = Messages.get(this, "name");
	}

	@Override
	protected RingBuff buff() {
		return new Resistance();
	}

	@Override
	public String desc() {
		return isKnown() ? Messages.get(this, "desc")
				: super.desc();
	}

	private static final HashSet<Class<?>> EMPTY = new HashSet<Class<?>>();
	private static final HashSet<Class<?>> FULL;

	static {
		FULL = new HashSet<Class<?>>();
		FULL.add(Burning.class);
		FULL.add(ToxicGas.class);
		FULL.add(Poison.class);
		FULL.add(LightningTrap.Electricity.class);
		FULL.add(Warlock.class);
		FULL.add(Eye.class);
		FULL.add(Yog.BurningFist.class);
	}

	public class Resistance extends RingBuff {

		public HashSet<Class<?>> resistances() {
			if (Random.Int(level + 2) >= 2) {
				return FULL;
			} else {
				return EMPTY;
			}
		}

		public float durationFactor() {
			return level < 0 ? 1 : (1 + 0.5f * level) / (1 + level);
		}
	}
}
