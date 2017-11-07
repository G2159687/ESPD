
package com.github.epd.sprout.actors.buffs;

import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.ui.BuffIndicator;
import com.watabou.utils.Bundle;

public class Terror extends FlavourBuff {

	public static final float DURATION = 10f;

	public int object = 0;

	private static final String OBJECT = "object";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(OBJECT, object);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		object = bundle.getInt(OBJECT);
	}

	@Override
	public int icon() {
		return BuffIndicator.TERROR;
	}

	@Override
	public String toString() {
		return Messages.get(this, "name");
	}

	@Override
	public String desc() {
		return Messages.get(this, "desc", dispTurns());
	}

	public static void recover(Char target) {
		Terror terror = target.buff(Terror.class);
		if (terror != null && terror.cooldown() < DURATION) {
			target.remove(terror);
		}
	}
}
