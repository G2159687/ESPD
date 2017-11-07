
package com.github.epd.sprout.actors.mobs;

import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.buffs.Bleeding;
import com.github.epd.sprout.actors.buffs.Buff;
import com.github.epd.sprout.items.food.Meat;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.AlbinoSprite;
import com.watabou.utils.Random;

public class Albino extends Rat {

	{
		name = Messages.get(this, "name");
		spriteClass = AlbinoSprite.class;

		HP = HT = 10 + (Dungeon.depth * Random.NormalIntRange(1, 3));

		loot = new Meat();
		lootChance = 1f;
	}

	@Override
	public void die(Object cause) {
		super.die(cause);
	}

	@Override
	public int attackProc(Char enemy, int damage) {
		if (Random.Int(2) == 0) {
			Buff.affect(enemy, Bleeding.class).set(damage);
		}

		return damage;
	}

	@Override
	public String description() {
		return Messages.get(this, "desc");
	}
}
