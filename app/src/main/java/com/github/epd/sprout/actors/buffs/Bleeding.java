
package com.github.epd.sprout.actors.buffs;

import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.ResultDescriptions;
import com.github.epd.sprout.effects.Splash;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.ui.BuffIndicator;
import com.github.epd.sprout.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.PointF;
import com.watabou.utils.Random;

public class Bleeding extends Buff {

	protected int level;

	private static final String LEVEL = "level";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(LEVEL, level);

	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		level = bundle.getInt(LEVEL);
	}

	public void set(int level) {
		this.level = level;
	}

	@Override
	public int icon() {
		return BuffIndicator.BLEEDING;
	}

	@Override
	public String toString() {
		return Messages.get(this, "name");
	}

	@Override
	public boolean act() {
		if (target.isAlive()) {

			if ((level = Random.Int(level / 2, level)) > 0) {

				target.damage(level, this);
				if (target.sprite.visible) {
					Splash.at(target.sprite.center(), -PointF.PI / 2,
							PointF.PI / 6, target.sprite.blood(),
							Math.min(10 * level / target.HT, 10));
				}

				if (target == Dungeon.hero && !target.isAlive()) {
					Dungeon.fail(ResultDescriptions.BLEEDING);
					GLog.n(Messages.get(this, "die"));
				}

				spend(TICK);
			} else {
				detach();
			}

		} else {

			detach();

		}

		return true;
	}

	@Override
	public String desc() {
		return Messages.get(this, "desc", level);
	}
}
