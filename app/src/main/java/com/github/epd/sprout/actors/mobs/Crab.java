
package com.github.epd.sprout.actors.mobs;

import com.github.epd.sprout.Challenges;
import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.items.food.MysteryMeat;
import com.github.epd.sprout.items.scrolls.ScrollOfMagicalInfusion;
import com.github.epd.sprout.items.scrolls.ScrollOfUpgrade;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.CrabSprite;
import com.watabou.utils.Random;

public class Crab extends Mob {

	{
		name = Messages.get(this, "name");
		spriteClass = CrabSprite.class;

		HP = HT = 18 + (adj(0) * Random.NormalIntRange(1, 3));
		defenseSkill = 5 + adj(1);

		EXP = 3;
		maxLvl = 9;

		if (Dungeon.moreLoots) {
			loot = new ScrollOfUpgrade();
			lootChance = 0.5f;

			lootOther = new ScrollOfMagicalInfusion();
			lootChanceOther = 1f;

		} else {

			loot = new MysteryMeat();
			lootChance = 0.5f;
		}
	}

	@Override
	public int damageRoll() {
		return Random.NormalIntRange(3, 6 + adj(0));
	}

	@Override
	public int attackSkill(Char target) {
		return 12 + adj(0);
	}

	@Override
	public int dr() {
		return 4;
	}

	@Override
	public String defenseVerb() {
		return Messages.get(this, "def");
	}

	@Override
	public String description() {
		return Messages.get(this, "desc");
	}
}
