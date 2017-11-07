
package com.github.epd.sprout.actors.buffs;

import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.ResultDescriptions;
import com.github.epd.sprout.actors.mobs.BanditKing;
import com.github.epd.sprout.actors.mobs.Mob;
import com.github.epd.sprout.effects.Speck;
import com.github.epd.sprout.effects.particles.ShadowParticle;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.ui.BuffIndicator;
import com.github.epd.sprout.utils.GLog;
import com.watabou.utils.Bundle;

public class CountDown extends Buff {

	private int ticks = 0;

	private static final String TICKS = "ticks";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(TICKS, ticks);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		ticks = bundle.getInt(TICKS);
	}


	@Override
	public int icon() {
		return BuffIndicator.COUNTDOWN;
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
			ticks++;
			GLog.w(Messages.get(CountDown.class, "cd", (6 - ticks)));
			if (ticks > 5) {

				GLog.w(Messages.get(CountDown.class, "cdup"));
				target.sprite.emitter().burst(ShadowParticle.CURSE, 6);
				target.damage(Math.round(target.HT / 4), this);
				for (Mob mob : Dungeon.level.mobs) {
					if (mob instanceof BanditKing) {
						mob.sprite.emitter().burst(Speck.factory(Speck.HEALING), 1);
						mob.HP = Math.min(mob.HP + (Math.round(target.HT / 8)), mob.HT);
					}
				}
				GLog.w(Messages.get(CountDown.class, "feed"));
				detach();
			}
		}

		if (!target.isAlive() && target == Dungeon.hero) {
			Dungeon.fail(ResultDescriptions.COUNTDOWN);
			GLog.n(Messages.get(this, "die"), toString());
		}

		spend(TICK);

		return true;
	}
}
