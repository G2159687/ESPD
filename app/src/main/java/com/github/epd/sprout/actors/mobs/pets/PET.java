
package com.github.epd.sprout.actors.mobs.pets;

import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.actors.Actor;
import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.blobs.CorruptGas;
import com.github.epd.sprout.actors.blobs.StenchGas;
import com.github.epd.sprout.actors.blobs.ToxicGas;
import com.github.epd.sprout.actors.buffs.Bleeding;
import com.github.epd.sprout.actors.buffs.Poison;
import com.github.epd.sprout.actors.buffs.Roots;
import com.github.epd.sprout.actors.buffs.Vertigo;
import com.github.epd.sprout.actors.hero.Hero;
import com.github.epd.sprout.actors.mobs.Mob;
import com.github.epd.sprout.effects.CellEmitter;
import com.github.epd.sprout.effects.particles.ElmoParticle;
import com.github.epd.sprout.items.Heap;
import com.github.epd.sprout.levels.Level;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.utils.GLog;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.HashSet;

//TODO: 丰富宠物喂食系统，再次削弱宠物

public abstract class PET extends Mob {

	{
		HP = HT = 1;
		EXP = 0;

		hostile = false;
		state = HUNTING;
		ally = true;
	}

	public int level;
	public int kills;
	public int type;
	public int experience;
	public int cooldown;
	public int goaways = 0;
	public boolean callback = false;
	public boolean stay = false;

	private static final String KILLS = "kills";
	private static final String LEVEL = "level";
	private static final String TYPE = "type";
	private static final String EXPERIENCE = "experience";
	private static final String COOLDOWN = "cooldown";
	private static final String GOAWAYS = "goaways";
	private static final String CALLBACK = "callback";
	private static final String STAY = "stay";


	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(KILLS, kills);
		bundle.put(LEVEL, level);
		bundle.put(TYPE, type);
		bundle.put(EXPERIENCE, experience);
		bundle.put(COOLDOWN, cooldown);
		bundle.put(GOAWAYS, goaways);
		bundle.put(CALLBACK, callback);
		bundle.put(STAY, stay);
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		kills = bundle.getInt(KILLS);
		level = bundle.getInt(LEVEL);
		type = bundle.getInt(TYPE);
		experience = bundle.getInt(EXPERIENCE);
		cooldown = bundle.getInt(COOLDOWN);
		goaways = bundle.getInt(GOAWAYS);
		callback = bundle.getBoolean(CALLBACK);
		stay = bundle.getBoolean(STAY);
		adjustStats(level);
	}

	protected void throwItem() {
		Heap heap = Dungeon.level.heaps.get(pos);
		if (heap != null) {
			int n;
			do {
				n = pos + PathFinder.NEIGHBOURS8[Random.Int(8)];
			} while (!Level.passable[n] && !Level.avoid[n]);
			Dungeon.level.drop(heap.pickUp(), n).sprite.drop(pos);
		}
	}

	public void adjustStats(int level) {
	}

	public void spawn(int level) {
		this.level = level;
		adjustStats(level);
	}

	@Override
	protected boolean act() {

		assignPet(this);
		return super.act();
	}


	@Override
	public void damage(int dmg, Object src) {

		if (src instanceof Hero) {
			goaways++;
			GLog.n(Messages.get(PET.class, "warn", name));
		}

		if (goaways > 2) {
			flee();
		}

		super.damage(dmg, src);

	}

	@Override
	public int defenseProc(Char enemy, int damage) {
		//if attacked by something else than current target, and that thing is closer, switch targets
		if (this.enemy == null
				|| (enemy != this.enemy && (Dungeon.level.distance(pos, enemy.pos) < Dungeon.level.distance(pos, this.enemy.pos)))) {
			aggro(enemy);
			target = enemy.pos;
		}
		return damage;
	}

	@Override
	public void die(Object cause) {

		Dungeon.hero.haspet = false;
		Dungeon.hero.petCount++;
		GLog.n(Messages.get(PET.class, "die", name));

		super.die(cause);

	}

	@Override
	public float speed() {

		float speed = super.speed();

		int hasteLevel = Dungeon.petHasteLevel;

		if (hasteLevel > 10) {
			hasteLevel = 10;
		}

		if (hasteLevel != 0)
			speed *= Math.pow(1.2, hasteLevel);

		return speed;
	}

	public void flee() {
		Dungeon.hero.haspet = false;
		GLog.n(Messages.get(PET.class, "flee", name));
		destroy();
		sprite.killAndErase();
		CellEmitter.get(pos).burst(ElmoParticle.FACTORY, 6);
	}

	public void earnExp(int exp) {
		kills++;
		experience += exp;

		if (experience >= (5 + level * 5) && level < 100) {
			level++;
			GLog.p(Messages.get(PET.class, "levelup", name));
			adjustStats(level);
			experience = 0;
		}
	}

	@Override
	protected Char chooseEnemy() {

		if (enemy == null || !enemy.isAlive()) {
			HashSet<Mob> enemies = new HashSet<Mob>();
			for (Mob mob : Dungeon.level.mobs) {
				if (mob.hostile
						&& Level.fieldOfView[mob.pos]
						&& mob.state != mob.PASSIVE) {
					enemies.add(mob);
				}
			}

			//go for closest enemy
			Char closest = null;
			for (Char curr : enemies) {
				if (closest == null
						|| Dungeon.level.distance(pos, curr.pos) < Dungeon.level.distance(pos, closest.pos)) {
					closest = curr;
				}
			}
			return closest;
		}

		return enemy;
	}

	@Override
	protected boolean getCloser(int target) {
		if (enemy != null && !callback) {
			target = enemy.pos;
		} else if (checkNearbyHero()) {
			target =/* wanderLocation() != -1 ? wanderLocation() : */Dungeon.hero.pos;
			callback = false;
		} else if (Dungeon.hero.invisible == 0) {
			target = Dungeon.hero.pos;
		} else {
			target =/* wanderLocation() != -1 ? wanderLocation() : */Dungeon.hero.pos;
		}

		if (stay) {

			return false;

		}

		return super.getCloser(target);
	}

	protected boolean checkNearbyHero() {
		return Dungeon.level.adjacent(pos, Dungeon.hero.pos);
	}

	public int wanderLocation() {
		int newPos = -1;
		ArrayList<Integer> candidates = new ArrayList<Integer>();
		boolean[] passable = Level.passable;

		for (int n : PathFinder.NEIGHBOURS8) {
			int c = pos + n;
			if (passable[c] && Actor.findChar(c) == null) {
				candidates.add(c);
			}
		}

		newPos = candidates.size() > 0 ? Random.element(candidates) : -1;

		return newPos;
	}

	@Override
	public void aggro(Char ch) {
		if (ch != Dungeon.hero)
			enemy = ch;
	}

	@Override
	public void beckon(int cell) {
	}


	private void assignPet(PET pet) {

		Dungeon.hero.petType = pet.type;
		Dungeon.hero.petLevel = pet.level;
		Dungeon.hero.petKills = pet.kills;
		Dungeon.hero.petHP = pet.HP;
		Dungeon.hero.petExperience = pet.experience;
		Dungeon.hero.petCooldown = pet.cooldown;
	}

	abstract public boolean interact();

	private static final HashSet<Class<?>> IMMUNITIES = new HashSet<Class<?>>();

	static {
		IMMUNITIES.add(ToxicGas.class);
		IMMUNITIES.add(Poison.class);
		IMMUNITIES.add(StenchGas.class);
		IMMUNITIES.add(CorruptGas.class);
		IMMUNITIES.add(Bleeding.class);
		IMMUNITIES.add(Vertigo.class);
		IMMUNITIES.add(Roots.class);
	}

	@Override
	public HashSet<Class<?>> immunities() {
		return IMMUNITIES;
	}
}