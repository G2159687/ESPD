
package com.github.epd.sprout.actors.mobs;

import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.AcidicSprite;
import com.watabou.utils.Random;

public class Acidic extends Scorpio {

	{
		name = Messages.get(this, "name");
		spriteClass = AcidicSprite.class;

		properties.add(Property.UNDEAD);
	}

	@Override
	public int defenseProc(Char enemy, int damage) {

		int dmg = Random.IntRange(0, damage);
		if (dmg > 0) {
			enemy.damage(dmg, this);
		}

		return super.defenseProc(enemy, damage);
	}

	@Override
	public void die(Object cause) {
		super.die(cause);
	}
}
