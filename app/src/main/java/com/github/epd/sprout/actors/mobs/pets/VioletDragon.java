
package com.github.epd.sprout.actors.mobs.pets;

import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.buffs.Buff;
import com.github.epd.sprout.actors.buffs.MagicalSleep;
import com.github.epd.sprout.actors.buffs.Paralysis;
import com.github.epd.sprout.actors.buffs.Poison;
import com.github.epd.sprout.levels.Level;
import com.github.epd.sprout.levels.Terrain;
import com.github.epd.sprout.mechanics.Ballistica;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.CharSprite;
import com.github.epd.sprout.sprites.VioletDragonSprite;
import com.github.epd.sprout.utils.GLog;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

import java.util.HashSet;

public class VioletDragon extends PET implements Callback {

	{
		name = Messages.get(VioletDragon.class, "name");
		spriteClass = VioletDragonSprite.class;
		flying = true;
		state = HUNTING;
		level = 1;
		type = 6;
		cooldown = 1000;

	}

	private static final float TIME_TO_ZAP = 1f;

	//Frames 1-4 are idle, 5-8 are moving, 9-12 are attack and the last are for death

	//flame on!
	//spits fire
	//feed meat

	protected int regen = 1;
	protected float regenChance = 0.2f;

	@Override
	protected float attackDelay() {
		return 0.8f;
	}

	@Override
	public void flee() {
		((VioletDragonSprite) sprite).hpBar.killAndErase();
		super.flee();
	}

	@Override
	public int dr() {
		return level * 3;
	}


	@Override
	public void adjustStats(int level) {
		this.level = level;
		HT = (3 + level) * 8;
		defenseSkill = 5 + (level * level) / 2;
	}


	@Override
	public int attackSkill(Char target) {
		return defenseSkill;
	}

	@Override
	public int damageRoll() {
		return Random.NormalIntRange(HT / 5, HT / 2);
	}

	@Override
	protected boolean act() {

		if (cooldown > 0) {
			cooldown = Math.max(cooldown - (level * level), 0);
			if (level < 50 && cooldown == 0) {
				GLog.p(Messages.get(VioletDragon.class, "ready"));
			}
		}

		if (Random.Float() < regenChance && HP < HT) {
			HP += regen;
		}

		return super.act();
	}


	@Override
	protected boolean canAttack(Char enemy) {
		if (cooldown > 0) {
			return Dungeon.level.adjacent(pos, enemy.pos);
		} else {
			return new Ballistica(pos, enemy.pos, Ballistica.MAGIC_BOLT).collisionPos == enemy.pos;
		}
	}

	@Override
	protected boolean doAttack(Char enemy) {

		if (Dungeon.level.adjacent(pos, enemy.pos)) {

			return super.doAttack(enemy);

		} else {

			boolean visible = Level.fieldOfView[pos]
					|| Level.fieldOfView[enemy.pos];
			if (visible) {
				sprite.zap(enemy.pos);
			} else {
				zap();
			}

			return !visible;
		}
	}


	private void zap() {
		spend(TIME_TO_ZAP);

		cooldown = 1000;
		if (level < 50)
			yell(Messages.get(GreenDragon.class, "atk"));

		if (hit(this, enemy, true)) {

			int dmg = damageRoll() * 2;
			enemy.damage(dmg, this);

			if (enemy.isAlive()) {
				Buff.affect(enemy, Poison.class).set(Poison.durationFactor(enemy) * (level + 1));
			}

		} else {
			enemy.sprite.showStatus(CharSprite.NEUTRAL, enemy.defenseVerb());
		}

	}

	public void onZapComplete() {
		zap();
		next();
	}

	@Override
	public void call() {
		next();
	}


	@Override
	public boolean interact() {

		if (this.buff(MagicalSleep.class) != null) {
			Buff.detach(this, MagicalSleep.class);
		}

		if (state == SLEEPING) {
			state = HUNTING;
		}
		if (buff(Paralysis.class) != null) {
			Buff.detach(this, Paralysis.class);
			GLog.i(Messages.get(bee.class, "shake"), name);
		}

		int curPos = pos;

		if (Level.passable[pos] && Dungeon.level.map[pos] != Terrain.CHASM) {

			moveSprite(pos, Dungeon.hero.pos);
			move(Dungeon.hero.pos);

			Dungeon.hero.sprite.move(Dungeon.hero.pos, curPos);
			Dungeon.hero.move(curPos);

			Dungeon.hero.spend(1 / Dungeon.hero.speed());
			Dungeon.hero.busy();

		}

		return true;
	}


	@Override
	public String description() {
		return Messages.get(VioletDragon.class, "desc");
	}

	private static final HashSet<Class<?>> RESISTANCES = new HashSet<Class<?>>();

	static {
		RESISTANCES.add(Poison.class);
	}

	@Override
	public HashSet<Class<?>> resistances() {
		return RESISTANCES;
	}
}