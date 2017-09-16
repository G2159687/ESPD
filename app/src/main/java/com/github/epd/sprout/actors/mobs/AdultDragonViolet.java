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
import com.github.epd.sprout.actors.blobs.ToxicGas;
import com.github.epd.sprout.actors.buffs.Buff;
import com.github.epd.sprout.actors.buffs.Poison;
import com.github.epd.sprout.items.scrolls.ScrollOfPsionicBlast;
import com.github.epd.sprout.items.weapon.enchantments.Death;
import com.github.epd.sprout.levels.Level;
import com.github.epd.sprout.mechanics.Ballistica;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.AdultDragonVioletSprite;
import com.github.epd.sprout.sprites.CharSprite;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

import java.util.HashSet;

public class AdultDragonViolet extends Mob implements Callback {

	private static final int JUMP_DELAY = 5;
	private static final float TIME_TO_ZAP = 1f;


	{
		name = Messages.get(this, "name");
		spriteClass = AdultDragonVioletSprite.class;
		baseSpeed = 2f;

		HP = HT = 8000;
		EXP = 20;
		defenseSkill = 75;
	}

	private int timeToJump = JUMP_DELAY;


	@Override
	public int damageRoll() {
		return Random.NormalIntRange(150, 300);
	}

	@Override
	public int attackSkill(Char target) {
		return 99;
	}

	@Override
	public int dr() {
		return 75;
	}


	@Override
	public void die(Object cause) {

		super.die(cause);

		yell(Messages.get(AdultDragonViolet.class, "die"));

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


		yell(Messages.get(AdultDragonViolet.class, "atk"));

		if (hit(this, enemy, true)) {

			int dmg = damageRoll() * 2;
			enemy.damage(dmg, this);

			Buff.affect(enemy, Poison.class).set(Poison.durationFactor(enemy));

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
		RESISTANCES.add(ToxicGas.class);
		RESISTANCES.add(Poison.class);
		RESISTANCES.add(Death.class);
		RESISTANCES.add(ScrollOfPsionicBlast.class);
	}

	@Override
	public HashSet<Class<?>> resistances() {
		return RESISTANCES;
	}
}
