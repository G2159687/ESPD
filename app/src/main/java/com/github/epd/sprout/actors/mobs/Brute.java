
package com.github.epd.sprout.actors.mobs;

import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.buffs.Terror;
import com.github.epd.sprout.items.Gold;
import com.github.epd.sprout.items.food.Meat;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.BruteSprite;
import com.github.epd.sprout.sprites.CharSprite;
import com.github.epd.sprout.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.HashSet;

public class Brute extends Mob {

	private static final String TXT_ENRAGED = Messages.get(Brute.class, "enrage");

	{
		name = Messages.get(this, "name");
		spriteClass = BruteSprite.class;

		HP = HT = 60 + (adj(0) * Random.NormalIntRange(4, 8));
		defenseSkill = 15 + adj(0);

		EXP = 8;
		maxLvl = 15;

		loot = Gold.class;
		lootChance = 0.5f;

		lootOther = new Meat();
		lootChanceOther = 0.5f; // by default, see die()
	}

	private boolean enraged = false;

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		enraged = HP < HT / 4;
	}

	@Override
	public int damageRoll() {
		return enraged ? Random.NormalIntRange(30 + adj(0), 70 + adj(0)) : Random.NormalIntRange(15 + adj(0), 40 + adj(0));
	}

	@Override
	public int attackSkill(Char target) {
		return 20 + adj(1);
	}

	@Override
	protected float attackDelay() {
		return 1.2f;
	}

	@Override
	public int dr() {
		return 10 + adj(0);
	}

	@Override
	public void damage(int dmg, Object src) {
		super.damage(dmg, src);

		if (isAlive() && !enraged && HP < HT / 4) {
			enraged = true;
			spend(TICK);
			if (Dungeon.visible[pos]) {
				GLog.w(TXT_ENRAGED, name);
				sprite.showStatus(CharSprite.NEGATIVE, Messages.get(Brute.class, "enraged"));
			}
		}
	}

	@Override
	public String description() {
		return Messages.get(this, "desc");
	}

	private static final HashSet<Class<?>> IMMUNITIES = new HashSet<Class<?>>();

	static {
		IMMUNITIES.add(Terror.class);
	}

	@Override
	public HashSet<Class<?>> immunities() {
		return IMMUNITIES;
	}
}
