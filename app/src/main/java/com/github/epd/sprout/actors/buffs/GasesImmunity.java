
package com.github.epd.sprout.actors.buffs;

import com.github.epd.sprout.actors.blobs.ConfusionGas;
import com.github.epd.sprout.actors.blobs.ParalyticGas;
import com.github.epd.sprout.actors.blobs.StenchGas;
import com.github.epd.sprout.actors.blobs.ToxicGas;
import com.github.epd.sprout.actors.blobs.VenomGas;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.ui.BuffIndicator;

public class GasesImmunity extends FlavourBuff {

	public static final float DURATION = 15f;

	@Override
	public int icon() {
		return BuffIndicator.IMMUNITY;
	}

	@Override
	public String toString() {
		return Messages.get(this, "name");
	}

	{
		immunities.add(ParalyticGas.class);
		immunities.add(ToxicGas.class);
		immunities.add(ConfusionGas.class);
		immunities.add(StenchGas.class);
		immunities.add(VenomGas.class);
	}

	@Override
	public String desc() {
		return Messages.get(this, "desc", dispTurns());
	}
}
