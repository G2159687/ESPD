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
import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.buffs.Buff;
import com.github.epd.sprout.actors.buffs.Drowsy;
import com.github.epd.sprout.actors.mobs.BlueWraith;
import com.github.epd.sprout.actors.mobs.DwarfLich;
import com.github.epd.sprout.actors.mobs.FlyingProtector;
import com.github.epd.sprout.actors.mobs.Golem;
import com.github.epd.sprout.actors.mobs.RedWraith;
import com.github.epd.sprout.actors.mobs.Sentinel;
import com.github.epd.sprout.actors.mobs.ShadowYog;
import com.github.epd.sprout.actors.mobs.Skeleton;
import com.github.epd.sprout.actors.mobs.SpectralRat;
import com.github.epd.sprout.actors.mobs.Statue;
import com.github.epd.sprout.actors.mobs.Wraith;
import com.github.epd.sprout.actors.mobs.Yog;
import com.github.epd.sprout.actors.mobs.npcs.NPC;
import com.github.epd.sprout.effects.Speck;
import com.github.epd.sprout.items.Item;
import com.github.epd.sprout.items.scrolls.ScrollOfTeleportation;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.ItemSpriteSheet;
import com.github.epd.sprout.utils.GLog;
import com.watabou.utils.Random;

public class RiceBall extends MissileWeapon {

	public static final float DURATION = 10f;

	{
		name = Messages.get(this, "name");
		image = ItemSpriteSheet.RICEBALL;

		MIN = 1;
		MAX = 2;

		DLY = 0.25f;

		bones = false;
	}

	public RiceBall() {
		this(1);
	}

	public RiceBall(int number) {
		super();
		quantity = number;
	}

	@Override
	public String desc() {
		return Messages.get(this, "desc");
	}

	@Override
	public void proc(Char attacker, Char defender, int damage) {


		if (defender != null
				&& !(defender instanceof NPC)
				&& !(defender instanceof BlueWraith)
				&& !(defender instanceof Wraith)
				&& !(defender instanceof RedWraith)
				&& !(defender instanceof Sentinel)
				&& !(defender instanceof FlyingProtector)
				&& !(defender instanceof Golem)
				&& !(defender instanceof Skeleton)
				&& !(defender instanceof DwarfLich)
				&& !(defender instanceof Statue)
				&& !(defender instanceof Yog)
				&& !(defender instanceof ShadowYog)
				&& !(defender instanceof SpectralRat)
				) {

			Buff.affect(defender, Drowsy.class);
			defender.sprite.centerEmitter().start(Speck.factory(Speck.NOTE), 0.3f, 5);

			if (defender.HP / defender.HT > 0.01f) {
				int count = 20;
				int pos;
				do {
					pos = Dungeon.level.randomRespawnCell();
					if (count-- <= 0) {
						break;
					}
				} while (pos == -1);

				if (pos == -1) {

					GLog.w(ScrollOfTeleportation.TXT_NO_TELEPORT);

				} else {

					defender.pos = pos;
					defender.sprite.place(defender.pos);
					defender.sprite.visible = Dungeon.visible[pos];
					GLog.i(Messages.get(this, "tele", curUser.name, defender.name));

				}

			} else {

				GLog.i(Messages.get(this, "nothing"));

			}
		}

		super.proc(attacker, defender, damage);
	}

	@Override
	public Item random() {
		quantity = Random.Int(5, 15);
		return this;
	}

	@Override
	public int price() {
		return quantity * 2;
	}
}
