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

import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.buffs.Buff;
import com.github.epd.sprout.actors.buffs.Burning;
import com.github.epd.sprout.actors.buffs.Chill;
import com.github.epd.sprout.actors.buffs.Frost;
import com.github.epd.sprout.effects.Speck;
import com.github.epd.sprout.items.food.ChargrilledMeat;
import com.github.epd.sprout.items.potions.PotionOfLiquidFlame;
import com.github.epd.sprout.items.wands.WandOfFirebolt;
import com.github.epd.sprout.items.weapon.enchantments.Fire;
import com.github.epd.sprout.levels.Level;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.ElementalSprite;
import com.watabou.utils.Random;

import java.util.HashSet;

public class Elemental extends Mob {

	{
		name = Messages.get(this, "name");
		spriteClass = ElementalSprite.class;

		HP = HT = 80 + (adj(0) * Random.NormalIntRange(4, 7));
		defenseSkill = 21 + adj(0);

		EXP = 10;
		maxLvl = 20;

		flying = true;

		loot = new PotionOfLiquidFlame();
		lootChance = 0.1f;

		lootOther = new ChargrilledMeat();
		lootChanceOther = 0.5f; // by default, see die()
	}

	@Override
	public int damageRoll() {
		return Random.NormalIntRange(16, 20 + adj(1));
	}

	@Override
	public int attackSkill(Char target) {
		return 25 + adj(1);
	}

	@Override
	public int dr() {
		return 5;
	}

	@Override
	public int attackProc(Char enemy, int damage) {
		if (Random.Int(2) == 0) {
			Buff.affect(enemy, Burning.class).reignite(enemy);
		}

		return damage;
	}

	@Override
	public void add(Buff buff) {
		if (buff instanceof Burning) {
			if (HP < HT) {
				HP++;
				sprite.emitter().burst(Speck.factory(Speck.HEALING), 1);
			}
		} else if (buff instanceof Frost || buff instanceof Chill) {
			if (Level.water[this.pos])
				damage(Random.NormalIntRange(HT / 2, HT), buff);
			else
				damage(Random.NormalIntRange(1, HT * 2 / 3), buff);
		} else {
			super.add(buff);
		}
	}

	@Override
	public String description() {
		return Messages.get(this, "desc");
	}

	private static final HashSet<Class<?>> IMMUNITIES = new HashSet<Class<?>>();

	static {
		IMMUNITIES.add(Burning.class);
		IMMUNITIES.add(Fire.class);
		IMMUNITIES.add(WandOfFirebolt.class);
	}

	@Override
	public HashSet<Class<?>> immunities() {
		return IMMUNITIES;
	}
}
