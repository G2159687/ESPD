
package com.github.epd.sprout.actors.mobs;


import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.items.Generator;
import com.github.epd.sprout.items.scrolls.ScrollOfRegrowth;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.RatBossSprite;
import com.github.epd.sprout.utils.GLog;
import com.watabou.utils.Random;

public class RatBoss extends Rat {


	{
		name = Messages.get(this, "name");
		spriteClass = RatBossSprite.class;

		HP = HT = 12 + Random.NormalIntRange(2, 5);
		defenseSkill = 5 + (Dungeon.depth / 4);

		loot = Generator.Category.BERRY;
		lootChance = 0.5f;

		lootOther = new ScrollOfRegrowth();
		lootChanceOther = 1f;
	}

	private boolean spawnedRats = false;

	@Override
	public int damageRoll() {
		return Random.NormalIntRange(2 + Dungeon.depth / 2, 8 + (Dungeon.depth));
	}

	@Override
	public int attackSkill(Char target) {
		return 11 + Dungeon.depth;
	}

	@Override
	public int dr() {
		return Dungeon.depth / 2;
	}

	@Override
	public void notice() {
		super.notice();
		yell(Messages.get(this, "notice"));
		if (!spawnedRats) {
			Rat.spawnAround(pos);
			GLog.n(Messages.get(this, "spawn"));
			spawnedRats = true;
		}
	}


	@Override
	public String description() {
		return Messages.get(this, "desc");
	}
}
