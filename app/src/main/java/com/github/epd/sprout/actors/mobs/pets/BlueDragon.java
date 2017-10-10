/*
 * Pixel Dungeon
 * Copyright (C) 2012-2014  Oleg Dolya
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */
package com.github.epd.sprout.actors.mobs.pets;

import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.buffs.Buff;
import com.github.epd.sprout.actors.buffs.Frost;
import com.github.epd.sprout.actors.buffs.MagicalSleep;
import com.github.epd.sprout.actors.buffs.Paralysis;
import com.github.epd.sprout.effects.CellEmitter;
import com.github.epd.sprout.effects.particles.SnowParticle;
import com.github.epd.sprout.levels.Level;
import com.github.epd.sprout.mechanics.Ballistica;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.BlueDragonSprite;
import com.github.epd.sprout.sprites.CharSprite;
import com.github.epd.sprout.utils.GLog;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class BlueDragon extends PET implements Callback {

	{
		name = Messages.get(BlueDragon.class, "name");
		spriteClass = BlueDragonSprite.class;
		flying = true;
		state = HUNTING;
		level = 1;
		type = 7;
		cooldown = 1000;

	}

	private static final float TIME_TO_ZAP = 1f;

	//Frames 1-4 are idle, 5-8 are moving, 9-12 are attack and the last are for death

	//flame on!
	//spits fire
	//feed meat


	@Override
	public int dr() {
		return level * 3;
	}

	protected int regen = 1;
	protected float regenChance = 0.1f;


	@Override
	public void adjustStats(int level) {
		this.level = level;
		HT = (3 + level) * 8;
		defenseSkill = 1 + (level * level) / 2;
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
			cooldown = Math.max(cooldown - (level * level) / 4, 0);
			if (level < 50 && cooldown == 0) {
				GLog.p(Messages.get(BlueDragon.class, "ready1"));
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
			return Level.adjacent(pos, enemy.pos);
		} else {
			return new Ballistica(pos, enemy.pos, Ballistica.MAGIC_BOLT).collisionPos == enemy.pos;
		}
	}

	@Override
	public void flee() {
		((BlueDragonSprite) sprite).hpBar.killAndErase();
		super.flee();
	}

	@Override
	protected boolean doAttack(Char enemy) {

		if (Level.adjacent(pos, enemy.pos)) {

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
			yell(Messages.get(BlueDragon.class, "atk1"));

		if (hit(this, enemy, true)) {

			int dmg = damageRoll() * 2;
			enemy.damage(dmg, this);

			if (enemy.isAlive()) {
				Buff.prolong(enemy, Frost.class, Frost.duration(enemy) * Random.Float(1f, 1.5f));
				CellEmitter.get(enemy.pos).start(SnowParticle.FACTORY, 0.2f, 6);
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

		if (Level.passable[pos]) {

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
		return Messages.get(BlueDragon.class, "desc");
	}


}