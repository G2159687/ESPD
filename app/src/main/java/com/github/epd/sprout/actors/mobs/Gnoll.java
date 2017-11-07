
package com.github.epd.sprout.actors.mobs;

import com.github.epd.sprout.Challenges;
import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.items.Generator;
import com.github.epd.sprout.items.Gold;
import com.github.epd.sprout.items.food.Meat;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.GnollSprite;
import com.watabou.utils.Random;

public class Gnoll extends Mob {

	{
		name = Messages.get(this, "name");
		spriteClass = GnollSprite.class;

		HP = HT = 14 + (Dungeon.depth * Random.NormalIntRange(1, 3));
		defenseSkill = 4 + (Math.round((Dungeon.depth) / 2));

		EXP = 2;
		maxLvl = 8;

		if (Dungeon.isChallenged(Challenges.NO_HERBALISM)) {
			loot = Generator.Category.SCROLL;
			lootChance = 0.75f;

			lootOther = Generator.Category.POTION;
			lootChanceOther = 1f;
		} else {

			loot = Gold.class;
			lootChance = 0.5f;

			lootOther = new Meat();
			lootChanceOther = 0.5f; // by default, see die()
		}
	}

	@Override
	public int damageRoll() {
		return Random.NormalIntRange(2, 5 + (Dungeon.depth));
	}

	@Override
	public int attackSkill(Char target) {
		return 11 + (Dungeon.depth);
	}

	@Override
	public int dr() {
		return 2;
	}

	@Override
	public String description() {
		return Messages.get(this, "desc");
	}
}
