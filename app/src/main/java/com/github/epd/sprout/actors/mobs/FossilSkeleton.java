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

import com.github.epd.sprout.Assets;
import com.github.epd.sprout.Challenges;
import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.ResultDescriptions;
import com.github.epd.sprout.ShatteredPixelDungeon;
import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.hero.Hero;
import com.github.epd.sprout.items.Generator;
import com.github.epd.sprout.items.Item;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.sprites.FossilSkeletonSprite;
import com.github.epd.sprout.utils.GLog;
import com.github.epd.sprout.utils.Utils;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.PathFinder;
import com.watabou.utils.Random;

public class FossilSkeleton extends Mob {

	private static final String TXT_HERO_KILLED = Messages.get(FossilSkeleton.class, "kill");

	{
		name = Messages.get(this, "name");
		spriteClass = FossilSkeletonSprite.class;

		HP = HT = 30 + (adj(0) * Random.NormalIntRange(3, 7));
		defenseSkill = 9 + adj(1);

		EXP = 5;
		maxLvl = 10;

		loot = Generator.Category.WEAPON;
		lootChance = Dungeon.isChallenged(Challenges.NO_HERBALISM) ? 1f : 0.2f;

		properties.add(Property.UNDEAD);
	}

	@Override
	public int damageRoll() {
		return Random.NormalIntRange(10 + adj(0), 16 + adj(3));
	}

	@Override
	protected float attackDelay() {
		return 1.2f;
	}

	@Override
	public void die(Object cause) {

		super.die(cause);

		boolean heroKilled = false;
		for (int i = 0; i < PathFinder.NEIGHBOURS8.length; i++) {
			Char ch = findChar(pos + PathFinder.NEIGHBOURS8[i]);
			if (ch != null && ch.isAlive()) {
				int damage = Math.max(0,
						Random.NormalIntRange(3, 8) - Random.IntRange(0, ch.dr() / 2));
				ch.damage(damage, this);
				if (ch == Dungeon.hero && !ch.isAlive()) {
					heroKilled = true;
				}
			}
		}

		if (Dungeon.visible[pos]) {
			Sample.INSTANCE.play(Assets.SND_BONES);
		}

		if (heroKilled) {
			Dungeon.fail(Utils.format(ResultDescriptions.MOB,
					Utils.indefinite(name)));
			GLog.n(TXT_HERO_KILLED);
		}
	}

	@Override
	protected Item createLoot() {
		Item loot = Generator.random(Generator.Category.WEAPON);
		for (int i = 0; i < 2; i++) {
			Item l = Generator.random(Generator.Category.WEAPON);
			if (l.level < loot.level) {
				loot = l;
			}
		}
		if (ShatteredPixelDungeon.autocollect()) {
			if (loot.doPickUp(Dungeon.hero))
				GLog.i("\n" + Messages.get(Hero.class, "have", loot.name()));
			else Dungeon.level.drop(loot, Dungeon.hero.pos).sprite.drop();
		} else Dungeon.level.drop(loot, pos).sprite.drop();
		return loot;
	}

	@Override
	public int attackSkill(Char target) {
		return 12 + adj(0);
	}

	@Override
	public int dr() {
		return 5 + adj(0);
	}

	@Override
	public String defenseVerb() {
		return Messages.get(this, "def");
	}

	@Override
	public String description() {
		return Messages.get(this, "desc");
	}
}
