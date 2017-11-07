
package com.github.epd.sprout.actors.buffs;

import com.github.epd.sprout.actors.blobs.ConfusionGas;
import com.github.epd.sprout.actors.blobs.CorruptGas;
import com.github.epd.sprout.actors.blobs.ParalyticGas;
import com.github.epd.sprout.actors.blobs.StenchGas;
import com.github.epd.sprout.actors.blobs.ToxicGas;
import com.github.epd.sprout.actors.mobs.BrokenRobot;
import com.github.epd.sprout.actors.mobs.Eye;
import com.github.epd.sprout.actors.mobs.Warlock;
import com.github.epd.sprout.actors.mobs.Yog;
import com.github.epd.sprout.levels.traps.LightningTrap;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.ui.BuffIndicator;

public class MagicImmunity extends FlavourBuff {

	public static final float DURATION = 10f;

	@Override
	public int icon() {
		return BuffIndicator.DISPEL;
	}

	@Override
	public String toString() {
		return Messages.get(this, "name");
	}

	@Override
	public String desc() {
		return Messages.get(this, "desc", dispTurns());
	}

	{
		immunities.add(ParalyticGas.class);
		immunities.add(ToxicGas.class);
		immunities.add(ConfusionGas.class);
		immunities.add(StenchGas.class);
		immunities.add(Burning.class);
		immunities.add(ToxicGas.class);
		immunities.add(Poison.class);
		immunities.add(LightningTrap.Electricity.class);
		immunities.add(Warlock.class);
		immunities.add(Eye.class);
		immunities.add(Yog.BurningFist.class);
		immunities.add(BrokenRobot.class);
		immunities.add(CorruptGas.class);


	}
}

