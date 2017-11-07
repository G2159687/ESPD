
package com.github.epd.sprout.actors.mobs;

import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.ShieldedSprite;

public class Shielded extends Brute {

	{
		name = Messages.get(this, "name");
		spriteClass = ShieldedSprite.class;

		defenseSkill = 20 + adj(0);
	}

	@Override
	public int dr() {
		return 20 + adj(0);
	}

	@Override
	public String defenseVerb() {
		return Messages.get(this, "def");
	}

	@Override
	public void die(Object cause) {
		super.die(cause);

	}
}
