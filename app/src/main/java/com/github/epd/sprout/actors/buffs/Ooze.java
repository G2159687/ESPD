
package com.github.epd.sprout.actors.buffs;

import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.ResultDescriptions;
import com.github.epd.sprout.levels.Level;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.ui.BuffIndicator;
import com.github.epd.sprout.utils.GLog;
import com.watabou.utils.Random;

public class Ooze extends Buff {

	@Override
	public int icon() {
		return BuffIndicator.OOZE;
	}

	@Override
	public String toString() {
		return Messages.get(this, "name");
	}

	@Override
	public String desc() {
		return Messages.get(this, "desc");
	}

	@Override
	public boolean act() {
		if (target.isAlive()) {
			if (Dungeon.depth > 4)
				target.damage(Dungeon.depth / 5, this);
			else if (Random.Int(2) == 0)
				target.damage(1, this);
			if (!target.isAlive() && target == Dungeon.hero) {
				Dungeon.fail(ResultDescriptions.OOZE);
				GLog.n(Messages.get(this, "die"), toString());
			}
			spend(TICK);
		}
		if (Level.water[target.pos]) {
			detach();
		}
		return true;
	}
}
