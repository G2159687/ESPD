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
package com.github.epd.sprout.actors.mobs;

import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.ResultDescriptions;
import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.buffs.Buff;
import com.github.epd.sprout.actors.buffs.Weakness;
import com.github.epd.sprout.items.Generator;
import com.github.epd.sprout.items.food.Meat;
import com.github.epd.sprout.items.weapon.enchantments.Death;
import com.github.epd.sprout.levels.Level;
import com.github.epd.sprout.mechanics.Ballistica;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.CharSprite;
import com.github.epd.sprout.sprites.WarlockSprite;
import com.github.epd.sprout.utils.GLog;
import com.github.epd.sprout.utils.Utils;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

import java.util.HashSet;

public class Warlock extends Mob implements Callback {

	private static final float TIME_TO_ZAP = 1f;

	private static final String TXT_SHADOWBOLT_KILLED = Messages.get(Warlock.class, "kill");

	{
		name = Messages.get(this, "name");
		spriteClass = WarlockSprite.class;

		HP = HT = 80 + (adj(0) * Random.NormalIntRange(5, 7));
		defenseSkill = 20 + adj(0);

		EXP = 11;
		maxLvl = 21;

		loot = Generator.Category.POTION;
		lootChance = 0.83f;

		lootOther = new Meat();
		lootChanceOther = 0.5f; // by default, see die()

		properties.add(Property.UNDEAD);
	}

	@Override
	public int damageRoll() {
		return Random.NormalIntRange(12, 25 + adj(0));
	}

	@Override
	public int attackSkill(Char target) {
		return 25 + adj(0);
	}

	@Override
	public int dr() {
		return 8 + adj(1);
	}

	@Override
	protected boolean canAttack(Char enemy) {
		return new Ballistica(pos, enemy.pos, Ballistica.MAGIC_BOLT).collisionPos == enemy.pos;
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

		if (hit(this, enemy, true)) {
			if (enemy == Dungeon.hero && Random.Int(2) == 0) {
				Buff.prolong(enemy, Weakness.class, Weakness.duration(enemy));
			}

			int dmg = Random.Int(16, 24 + adj(0));
			enemy.damage(dmg, this);

			if (!enemy.isAlive() && enemy == Dungeon.hero) {
				Dungeon.fail(Utils.format(ResultDescriptions.MOB,
						Utils.indefinite(name)));
				GLog.n(TXT_SHADOWBOLT_KILLED, name);
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
	public String description() {
		return Messages.get(this, "desc");
	}

	private static final HashSet<Class<?>> RESISTANCES = new HashSet<Class<?>>();

	static {
		RESISTANCES.add(Death.class);
	}

	@Override
	public HashSet<Class<?>> resistances() {
		return RESISTANCES;
	}
}
