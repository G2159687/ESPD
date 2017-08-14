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
package com.github.epd.sprout.items.weapon.missiles;

import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.actors.Actor;
import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.buffs.Buff;
import com.github.epd.sprout.actors.buffs.PinCushion;
import com.github.epd.sprout.actors.hero.Hero;
import com.github.epd.sprout.actors.hero.HeroClass;
import com.github.epd.sprout.items.Item;
import com.github.epd.sprout.items.rings.RingOfSharpshooting;
import com.github.epd.sprout.items.weapon.Weapon;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.scenes.GameScene;
import com.github.epd.sprout.windows.WndOptions;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class MissileWeapon extends Weapon {

	private static final String TXT_MISSILES = Messages.get(MissileWeapon.class,"missiles");
	private static final String TXT_YES = Messages.get(MissileWeapon.class,"yes");
	private static final String TXT_NO = Messages.get(MissileWeapon.class,"no");
	private static final String TXT_R_U_SURE = Messages.get(MissileWeapon.class,"sure");

	{
		stackable = true;
		levelKnown = true;
		defaultAction = AC_THROW;
        usesTargeting = true;
	}

	@Override
	public ArrayList<String> actions(Hero hero) {
		ArrayList<String> actions = super.actions(hero);
		if (hero.heroClass != HeroClass.HUNTRESS
				&& hero.heroClass != HeroClass.ROGUE) {
			actions.remove(AC_EQUIP);
			actions.remove(AC_UNEQUIP);
		}
		return actions;
	}

	@Override
	protected void onThrow(int cell) {
		Char enemy = Actor.findChar(cell);
		if (enemy == null || enemy == curUser) {
			if (this instanceof Boomerang || this instanceof JupitersWraith)
				super.onThrow(cell);
			else
				miss(cell);
		} else {
			if (!curUser.shoot(enemy, this)) {
				miss(cell);
			} else if (!(this instanceof Boomerang || this instanceof JupitersWraith)) {
				int bonus = 0;

				for (Buff buff : curUser.buffs(RingOfSharpshooting.Aim.class))
					bonus += ((RingOfSharpshooting.Aim) buff).level;

				if (curUser.heroClass == HeroClass.HUNTRESS
						&& enemy.buff(PinCushion.class) == null)
					bonus += 3;

				if (Random.Float() > Math.pow(0.7, bonus)){
					if (enemy.isAlive())
						Buff.affect(enemy, PinCushion.class).stick(this);
					else
						Dungeon.level.drop( this, enemy.pos).sprite.drop();
				}
			}
		}
	}

	protected void miss(int cell) {
		int bonus = 0;
		for (Buff buff : curUser.buffs(RingOfSharpshooting.Aim.class)) {
			bonus += ((RingOfSharpshooting.Aim) buff).level;
		}

		// degraded ring of sharpshooting will even make missed shots break.
		if (Random.Float() < Math.pow(0.6, -bonus))
			super.onThrow(cell);
	}

	@Override
	public void proc(Char attacker, Char defender, int damage) {

		super.proc(attacker, defender, damage);

		Hero hero = (Hero) attacker;
		if (hero.rangedWeapon == null && stackable) {
			if (quantity == 1) {
				doUnequip(hero, false, false);
			} else {
				detach(null);
			}
		}
	}

	@Override
	public boolean doEquip(final Hero hero) {
		GameScene.show(new WndOptions(TXT_MISSILES, TXT_R_U_SURE, TXT_YES,
				TXT_NO) {
			@Override
			protected void onSelect(int index) {
				if (index == 0) {
					MissileWeapon.super.doEquip(hero);
				}
			}
		});

		return false;
	}

	@Override
	public Item random() {
		return this;
	}

	@Override
	public boolean isUpgradable() {
		return false;
	}

	@Override
	public boolean isIdentified() {
		return true;
	}

	@Override
	public String info() {

		String name = name();
		StringBuilder info = new StringBuilder(desc());

		info.append(Messages.get(this,"avgdmg", MIN, MAX));

		if (Dungeon.hero.belongings.backpack.items.contains(this)) {
			if (STR > Dungeon.hero.STR()) {
				info.append(Messages.get(this,"decreased", name));
			}
			if (STR < Dungeon.hero.STR()
					&& Dungeon.hero.heroClass == HeroClass.HUNTRESS) {
				info.append(Messages.get(this,"increased", name));
			}
		}

		info.append(Messages.get(this,"distance"));

		if (isEquipped(Dungeon.hero)) {
			info.append(Messages.get(this,"ready", name));
		}

		if (enchantment != null){
			info.append("\n\n" + Messages.get(Weapon.class, "enchanted", enchantment.name()));
			info.append(" " + Messages.get(enchantment, "desc"));
		}

		return info.toString();
	}
}
