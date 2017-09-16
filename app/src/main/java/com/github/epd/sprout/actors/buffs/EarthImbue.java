package com.github.epd.sprout.actors.buffs;

import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.effects.CellEmitter;
import com.github.epd.sprout.effects.particles.EarthParticle;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.ui.BuffIndicator;

public class EarthImbue extends FlavourBuff {

	public static final float DURATION = 30f;

	public void proc(Char enemy) {
		Buff.affect(enemy, Roots.class, 2);
		CellEmitter.bottom(enemy.pos).start(EarthParticle.FACTORY, 0.05f, 8);
	}

	@Override
	public int icon() {
		return BuffIndicator.ROOTS;
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
		immunities.add(Paralysis.class);
		immunities.add(Roots.class);
		immunities.add(Slow.class);
	}
}