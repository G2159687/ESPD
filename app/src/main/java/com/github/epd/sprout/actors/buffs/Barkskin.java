
package com.github.epd.sprout.actors.buffs;

import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.ui.BuffIndicator;
import com.watabou.utils.Bundle;

public class Barkskin extends Buff {

	private int barkleft = 0;

	private static final String BARKLEFT = "barkleft";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(BARKLEFT, barkleft);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		barkleft = bundle.getInt(BARKLEFT);
	}

	@Override
	public boolean act() {
		if (target.isAlive()) {

			spend(TICK);
			if (--barkleft <= 0) {
				detach();
			}

		} else {

			detach();

		}

		return true;
	}

	public int level() {
		return barkleft;
	}

	public void level(int value) {
		if (barkleft < value) {
			barkleft = value;
		}
	}

	@Override
	public int icon() {
		return BuffIndicator.BARKSKIN;
	}

	@Override
	public String toString() {
		return Messages.get(this, "name");
	}

	@Override
	public String desc() {
		return Messages.get(this, "desc", barkleft);
	}
}
