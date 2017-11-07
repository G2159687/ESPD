
package com.github.epd.sprout.actors.mobs;

import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.Statistics;
import com.github.epd.sprout.actors.Actor;
import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.buffs.Amok;
import com.github.epd.sprout.actors.buffs.Poison;
import com.github.epd.sprout.levels.Level;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.BeeSprite;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.HashSet;

public class Bee extends Mob {

	{
		name = Messages.get(this, "name");
		spriteClass = BeeSprite.class;

		viewDistance = 4;

		flying = true;
		state = WANDERING;
	}

	private int level;

	// -1 refers to a pot that has gone missing.
	private int potPos;
	// -1 for no owner
	private int potHolder;

	private static final String LEVEL = "level";
	private static final String POTPOS = "potpos";
	private static final String POTHOLDER = "potholder";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(LEVEL, level);
		bundle.put(POTPOS, potPos);
		bundle.put(POTHOLDER, potHolder);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		spawn(bundle.getInt(LEVEL));
		potPos = bundle.getInt(POTPOS);
		potHolder = bundle.getInt(POTHOLDER);
	}

	public void spawn(int level) {
		this.level = Math.min(level, Statistics.deepestFloor);

		HT = (2 + level) * 4;
		defenseSkill = 9 + level;
	}

	public void setPotInfo(int potPos, Char potHolder) {
		this.potPos = potPos;
		if (potHolder == null)
			this.potHolder = -1;
		else
			this.potHolder = potHolder.id();
	}

	@Override
	public int attackSkill(Char target) {
		return defenseSkill;
	}

	@Override
	public int damageRoll() {
		return Random.NormalIntRange(HT / 10, HT / 4);
	}

	@Override
	public int attackProc(Char enemy, int damage) {
		if (enemy instanceof Mob) {
			((Mob) enemy).aggro(this);
		}
		return damage;
	}

	@Override
	protected Char chooseEnemy() {
		// if the pot is no longer present, target the hero
		if (potHolder == -1 && potPos == -1)
			return Dungeon.hero;

			// if something is holding the pot, target that
		else if (Actor.findById(potHolder) != null)
			return (Char) Actor.findById(potHolder);

			// if the pot is on the ground
		else {

			// if already targeting something, and that thing is still alive and
			// near the pot, keeping targeting it.
			if (enemy != null && enemy.isAlive()
					&& Level.fieldOfView[enemy.pos] && enemy.invisible == 0
					&& Dungeon.level.distance(enemy.pos, potPos) <= 3)
				return enemy;

			// find all mobs near the pot
			HashSet<Char> enemies = new HashSet<Char>();
			for (Mob mob : Dungeon.level.mobs)
				if (!(mob instanceof Bee)
						&& Dungeon.level.distance(mob.pos, potPos) <= 3
						&& (mob.hostile || mob.ally))
					enemies.add(mob);

			// pick one, if there are none, check if the hero is near the pot,
			// go for them, otherwise go for nothing.
			if (enemies.size() > 0)
				return Random.element(enemies);
			else
				return (Dungeon.level.distance(Dungeon.hero.pos, potPos) <= 3) ? Dungeon.hero
						: null;
		}
	}

	@Override
	protected boolean getCloser(int target) {
		if (enemy != null && Actor.findById(potHolder) == enemy) {
			target = enemy.pos;
		} else if (potPos != -1
				&& (state == WANDERING || Dungeon.level.distance(target, potPos) > 3))
			this.target = target = potPos;
		return super.getCloser(target);
	}

	@Override
	public String description() {
		return Messages.get(this, "desc");
	}

	private static final HashSet<Class<?>> IMMUNITIES = new HashSet<Class<?>>();

	static {
		IMMUNITIES.add(Poison.class);
		IMMUNITIES.add(Amok.class);
	}

	@Override
	public HashSet<Class<?>> immunities() {
		return IMMUNITIES;
	}
}