
package com.github.epd.sprout.actors.mobs;

import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.buffs.Buff;
import com.github.epd.sprout.actors.buffs.Paralysis;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.SeniorSprite;
import com.watabou.utils.Random;

public class Senior extends Monk {

	{
		name = Messages.get(this, "name");
		spriteClass = SeniorSprite.class;
	}

	@Override
	public int damageRoll() {
		return Random.NormalIntRange(32, 56 + adj(0));
	}

	@Override
	public int attackProc(Char enemy, int damage) {
		if (Random.Int(10) == 0) {
			Buff.prolong(enemy, Paralysis.class, 1.1f);
		}
		return super.attackProc(enemy, damage);
	}

	@Override
	public void die(Object cause) {
		super.die(cause);

	}
}
