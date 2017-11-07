
package com.github.epd.sprout.actors.mobs;

import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.buffs.Amok;
import com.github.epd.sprout.actors.buffs.Terror;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.GreyOniSprite;
import com.watabou.utils.Random;

import java.util.HashSet;

public class GreyOni extends Mob {

	{
		name = Messages.get(this, "name");
		spriteClass = GreyOniSprite.class;
		state = SLEEPING;

		HP = HT = 500;
		defenseSkill = 35;

		EXP = 22;

		properties.add(Property.UNDEAD);
		properties.add(Property.EVIL);
	}

	@Override
	public int damageRoll() {
		return Random.NormalIntRange(55, 115);
	}

	@Override
	public int attackSkill(Char target) {
		return 55;
	}

	@Override
	protected float attackDelay() {
		return 1.5f;
	}

	@Override
	public int dr() {
		return 32;
	}

	@Override
	public String defenseVerb() {
		return Messages.get(this, "def");
	}

	@Override
	public String description() {
		return Messages.get(this, "desc");
	}

	private static final HashSet<Class<?>> RESISTANCES = new HashSet<Class<?>>();

	static {
	}

	@Override
	public HashSet<Class<?>> resistances() {
		return RESISTANCES;
	}

	private static final HashSet<Class<?>> IMMUNITIES = new HashSet<Class<?>>();

	static {
		IMMUNITIES.add(Amok.class);
		IMMUNITIES.add(Terror.class);
	}

	@Override
	public HashSet<Class<?>> immunities() {
		return IMMUNITIES;
	}
}
