
package com.github.epd.sprout.items.rings;

import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.mobs.Mob;
import com.github.epd.sprout.messages.Messages;

public class RingOfEvasion extends Ring {

	{
		name = Messages.get(this, "name");
	}

	@Override
	protected RingBuff buff() {
		return new Evasion();
	}

	@Override
	public String desc() {
		return isKnown() ? Messages.get(this, "desc")
				: super.desc();
	}

	// yup, the only ring in the game with logic inside of its class
	public class Evasion extends RingBuff {
		public int effectiveLevel;
		private int pos;

		@Override
		public boolean attachTo(Char target) {

			pos = target.pos;
			effectiveLevel = Math.min(0, level);
			return super.attachTo(target);
		}

		@Override
		public boolean act() {

			boolean seen = false;

			for (Mob enemy : Dungeon.level.mobs.toArray(new Mob[0])) {
				if (enemy.focusingHero()) {
					seen = true;
					break;
				}
			}

			if (level < 1) {
				effectiveLevel = level;
			} else if (seen) {
				effectiveLevel = Math.max(effectiveLevel - 1, 0);
			} else {
				effectiveLevel = Math.min(effectiveLevel + 1, level);
			}

			return super.act();
		}
	}
}
