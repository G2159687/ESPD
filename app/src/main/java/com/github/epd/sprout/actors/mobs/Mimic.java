/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015 Oleg Dolya
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

import com.github.epd.sprout.Assets;
import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.actors.Actor;
import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.effects.CellEmitter;
import com.github.epd.sprout.effects.Pushing;
import com.github.epd.sprout.effects.Speck;
import com.github.epd.sprout.items.Generator;
import com.github.epd.sprout.items.Gold;
import com.github.epd.sprout.items.Item;
import com.github.epd.sprout.items.scrolls.ScrollOfPsionicBlast;
import com.github.epd.sprout.levels.Level;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.scenes.GameScene;
import com.github.epd.sprout.sprites.MimicSprite;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

public class Mimic extends Mob {

	private int level;

	{
		name = Messages.get(this,"name");
		spriteClass = MimicSprite.class;
	}

	public ArrayList<Item> items;

	private static final String LEVEL = "level";
	private static final String ITEMS = "items";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(ITEMS, items);
		bundle.put(LEVEL, level);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void restoreFromBundle(Bundle bundle) {
		items = new ArrayList<Item>(
				(Collection<Item>) ((Collection<?>) bundle.getCollection(ITEMS)));
		adjustStats(bundle.getInt(LEVEL));
		super.restoreFromBundle(bundle);
	}

	@Override
	public int damageRoll() {
		return Random.NormalIntRange(HT / 10, HT / 4);
	}

	@Override
	public int attackSkill(Char target) {
		return 9 + level;
	}

	@Override
	public int attackProc(Char enemy, int damage) {
		return super.attackProc(enemy, damage);
	}

	public void adjustStats(int level) {
		this.level = level;

		HT = (3 + level) * 5;
		EXP = 2 + 2 * (level - 1) / 5;
		defenseSkill = attackSkill(null) / 2;

		enemySeen = true;
	}

	@Override
	public void die(Object cause) {

		super.die(cause);

		if (items != null) {
			for (Item item : items) {
				Dungeon.level.drop(item, pos).sprite.drop();
			}
		}
	}

	@Override
	public boolean reset() {
		state = WANDERING;
		return true;
	}

	@Override
	public String description() {
		return Messages.get(this,"desc");
	}

	public static Mimic spawnAt(int pos, List<Item> items) {
		Char ch = Actor.findChar(pos);
		if (ch != null) {
			ArrayList<Integer> candidates = new ArrayList<Integer>();
			for (int n : PathFinder.NEIGHBOURS8) {
				int cell = pos + n;
				if ((Level.passable[cell] || Level.avoid[cell])
						&& Actor.findChar(cell) == null) {
					candidates.add(cell);
				}
			}
			if (candidates.size() > 0) {
				int newPos = Random.element(candidates);
				Actor.addDelayed(new Pushing(ch, ch.pos, newPos), -1);

				ch.pos = newPos;

				if (ch instanceof Mob) {
					Dungeon.level.mobPress((Mob) ch);
				} else {
					Dungeon.level.press(newPos, ch);
				}
			} else {
				return null;
			}
		}

		Mimic m = new Mimic();
		m.items = new ArrayList<Item>(items);
		m.adjustStats(Dungeon.depth);
		m.HP = m.HT;
		m.pos = pos;
		m.state = m.HUNTING;
		GameScene.add(m, 1);

		m.sprite.turnTo(pos, Dungeon.hero.pos);

		if (Dungeon.visible[m.pos]) {
			CellEmitter.get(pos).burst(Speck.factory(Speck.STAR), 10);
			Sample.INSTANCE.play(Assets.SND_MIMIC);
		}


		//generate an extra reward for killing the mimic
		switch(Random.Int(6)){
			case 0: case 1:
				m.items.add(new Gold().random()); break;
			case 2:
				m.items.add(Generator.randomArmor().identify()); break;
			case 3:
				m.items.add(Generator.randomWeapon().identify()); break;
			case 4:
				m.items.add(Generator.random(Generator.Category.RING).identify()); break;
			case 5:
				m.items.add(Generator.random(Generator.Category.WAND).identify()); break;
		}

		return m;
	}

	private static final HashSet<Class<?>> IMMUNITIES = new HashSet<Class<?>>();
	static {
		IMMUNITIES.add(ScrollOfPsionicBlast.class);
	}

	@Override
	public HashSet<Class<?>> immunities() {
		return IMMUNITIES;
	}
}
