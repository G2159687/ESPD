
package com.github.epd.sprout.actors.buffs;

import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.ui.BuffIndicator;
import com.watabou.utils.Bundle;

public class BerryRegeneration extends Buff {

	private int regenleft = 0;

	private static final String REGENLEFT = "regenleft";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(REGENLEFT, regenleft);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		regenleft = bundle.getInt(REGENLEFT);
	}

	public int level() {
		return regenleft;
	}

	public void level(int value) {
		if (regenleft < value) {
			regenleft = value;
		}
	}

	@Override
	public int icon() {
		return BuffIndicator.REGEN;
	}

	@Override
	public String toString() {
		return Messages.get(this, "name");
	}

	@Override
	public String desc() {
		return Messages.get(this, "desc", regenleft);
	}

	@Override
	public boolean act() {
		if (target.isAlive()) {

			if (target.HP < target.HT) {
				target.HP += Math.min(1 + Math.round(regenleft / 25), (target.HT - target.HP));
			}

			spend(TICK);
			if (--regenleft <= 0) {
				detach();
			}

		} else {

			detach();

		}

		return true;
	}

}
