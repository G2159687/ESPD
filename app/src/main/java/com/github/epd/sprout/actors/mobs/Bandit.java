
package com.github.epd.sprout.actors.mobs;

import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.actors.buffs.Blindness;
import com.github.epd.sprout.actors.buffs.Buff;
import com.github.epd.sprout.actors.buffs.Cripple;
import com.github.epd.sprout.actors.buffs.Poison;
import com.github.epd.sprout.actors.hero.Hero;
import com.github.epd.sprout.items.Item;
import com.github.epd.sprout.items.artifacts.MasterThievesArmband;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.BanditSprite;
import com.watabou.utils.Random;

public class Bandit extends Thief {

	public Item item;

	{
		name = Messages.get(this, "name");
		spriteClass = BanditSprite.class;
	}

	@Override
	protected boolean steal(Hero hero) {
		if (super.steal(hero)) {

			Buff.prolong(hero, Blindness.class, Random.Int(5, 12));
			Buff.affect(hero, Poison.class).set(
					Random.Int(5, 7) * Poison.durationFactor(enemy));
			Buff.prolong(hero, Cripple.class, Cripple.DURATION);
			Dungeon.observe();

			return true;
		} else {
			return false;
		}
	}

	@Override
	public void die(Object cause) {
		super.die(cause);

		if (!Dungeon.limitedDrops.armband.dropped() && Random.Float() < 0.1f){
			Dungeon.limitedDrops.armband.drop();
			Item.autocollect(new MasterThievesArmband().identify(), pos);
		}

	}
}
