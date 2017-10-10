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
package com.github.epd.sprout.levels.features;

import com.github.epd.sprout.Challenges;
import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.ShatteredPixelDungeon;
import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.buffs.Barkskin;
import com.github.epd.sprout.actors.buffs.Buff;
import com.github.epd.sprout.actors.hero.Hero;
import com.github.epd.sprout.actors.hero.HeroSubClass;
import com.github.epd.sprout.actors.mobs.npcs.Wandmaker;
import com.github.epd.sprout.effects.CellEmitter;
import com.github.epd.sprout.effects.particles.LeafParticle;
import com.github.epd.sprout.items.DewVial;
import com.github.epd.sprout.items.Dewdrop;
import com.github.epd.sprout.items.Generator;
import com.github.epd.sprout.items.Item;
import com.github.epd.sprout.items.RedDewdrop;
import com.github.epd.sprout.items.VioletDewdrop;
import com.github.epd.sprout.items.YellowDewdrop;
import com.github.epd.sprout.items.artifacts.SandalsOfNature;
import com.github.epd.sprout.items.food.Blackberry;
import com.github.epd.sprout.items.food.Blueberry;
import com.github.epd.sprout.items.food.Cloudberry;
import com.github.epd.sprout.items.food.FullMoonberry;
import com.github.epd.sprout.items.food.Moonberry;
import com.github.epd.sprout.levels.Level;
import com.github.epd.sprout.levels.Terrain;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.plants.BlandfruitBush;
import com.github.epd.sprout.plants.Flytrap;
import com.github.epd.sprout.scenes.GameScene;
import com.github.epd.sprout.ui.QuickSlotButton;
import com.github.epd.sprout.utils.GLog;
import com.watabou.utils.Random;

public class HighGrass {

	public static void trample(Level level, int pos, Char ch) {

		int naturalismLevel = 0;
		Level.set(pos, Terrain.GRASS);
		GameScene.updateMap(pos);

		if (ch != null) {
			SandalsOfNature.Naturalism naturalism = ch
					.buff(SandalsOfNature.Naturalism.class);
			if (naturalism != null) {
				if (!naturalism.isCursed()) {
					naturalismLevel = naturalism.level() + 1;
					naturalism.charge();
				} else {
					naturalismLevel = -1;
				}
			}
		}

		createItems(level, pos, naturalismLevel);

		if (ch != null) {
			if (ch.buff(Barkskin.class) != null) {
				createItemsExtra(level, pos);
			}
		}

		QuickSlotButton.refresh();

		int leaves = 4;

		// Barkskin
		if (ch instanceof Hero && ((Hero) ch).subClass == HeroSubClass.WARDEN) {
			Buff.affect(ch, Barkskin.class).level(5);
			leaves = 8;
		}

		CellEmitter.get(pos).burst(LeafParticle.LEVEL_SPECIFIC, leaves);
		if (Dungeon.visible[pos])
		Dungeon.observe();
	}

	public static void createItemsExtra(Level level, int pos) {

		// Seed
		if (Random.Int(10) == 0) {
			Item seed = Generator.random(Generator.Category.SEED);
			dropItems(level, seed, pos);
		}

		// Mushroom
		if (Random.Int(20) == 0) {
			Item mushroom = Generator.random(Generator.Category.MUSHROOM);
			dropItems(level, mushroom, pos);
		}

		if (Random.Int(40) == 0) {
			Item berry = Generator.random(Generator.Category.BERRY);
			dropItems(level, berry, pos);
		}

		if (Random.Int(50) == 0) {
			Item seed2 = Generator.random(Generator.Category.SEEDRICH);
			dropItems(level, seed2, pos);
		}

		if (Random.Int(150) == 0) {
			Item seed3 = new Wandmaker.Rotberry.Seed();
			dropItems(level, seed3, pos);
		}

		if (Random.Int(200) == 0) {
			Item moonberry = new FullMoonberry();
			dropItems(level, moonberry, pos);
		}
	}

	public static void createItems(Level level, int pos, int naturalismLevel) {
		if (naturalismLevel >= 0) {
			// Seed
			if (naturalismLevel < 5 ? (Random.Int(18 - ((int) (naturalismLevel * 3.34))) == 0) : (Random.Int(5) == 0)) {
				Item seed = Generator.random(Generator.Category.SEED);

				if (seed instanceof BlandfruitBush.Seed) {
					if (Random.Int(Dungeon.isChallenged(Challenges.NO_SCROLLS) ? 150 : 15)
							- Dungeon.limitedDrops.blandfruitSeed.count >= 0) {
						dropItems(level, seed, pos);
						Dungeon.limitedDrops.blandfruitSeed.count++;
					}

				} else if (seed instanceof Flytrap.Seed) {
					if (Random.Int(Dungeon.isChallenged(Challenges.NO_SCROLLS) ? 150 : 15)
							- Dungeon.limitedDrops.upgradeEaterSeed.count >= 0) {
						dropItems(level, seed, pos);
						Dungeon.limitedDrops.upgradeEaterSeed.count++;
					}

				} else if (seed instanceof Blackberry || seed instanceof Cloudberry
						|| seed instanceof Blueberry || seed instanceof Moonberry) {

					if (Random.Int(40) - Dungeon.limitedDrops.berries.count >= 0) {
						dropItems(level, seed, pos);
						Dungeon.limitedDrops.berries.count++;
					}
				} else dropItems(level, seed, pos);
			}

			// Mushroom
			if (Dungeon.growLevel(Dungeon.depth) && (naturalismLevel < 9 ? (Random.Int(40 - ((int) (naturalismLevel * 3.34))) == 0) : (Random.Int(10) == 0))) {
				Item mushroom = Generator.random(Generator.Category.MUSHROOM);
				dropItems(level, mushroom, pos);
			}

			// Dew
			if (naturalismLevel < 3 ? (Random.Int(3 - naturalismLevel) == 0) : (Random.Int(1) == 0)) {
				if (naturalismLevel > 20 && (naturalismLevel < 21 ? (Random.Int(30 - naturalismLevel) == 0) : Random.Int(10) == 0)) {
					dropDew(20, 2, new YellowDewdrop(), level, pos, "yellow");
				} else if (naturalismLevel > 30 && (naturalismLevel < 31 ? (Random.Int(50 - naturalismLevel) == 0) : Random.Int(20) == 0)) {
					dropDew(50, 5, new RedDewdrop(), level, pos, "red");
				} else if (naturalismLevel > 40 && (naturalismLevel >= 48 ? (Random.Int(100 - naturalismLevel) == 0) : Random.Int(200) == 0)) {
					dropDew(200, 50, new VioletDewdrop(), level, pos, "violet");
				} else {
					dropDew(10, 1, new Dewdrop(), level, pos, "dew");
				}
			}
		}
	}

	public static void dropItems(Level level, Item item, int pos) {
		if (ShatteredPixelDungeon.autocollect() && item.doPickUp(Dungeon.hero)) {
			GLog.i(Messages.get(Hero.class, "have", item.name()));
		} else {
			level.drop(item, pos).sprite.drop();
		}
	}

	public static void dropDew(int easy, int normal, Item item, Level level, int pos, String color) {
		DewVial vial = Dungeon.hero.belongings.getItem(DewVial.class);
		if (vial != null && ShatteredPixelDungeon.autocollect()) {
			if (!vial.isFull()) {
				vial.volume = vial.volume + (Dungeon.isChallenged(Challenges.SWARM_INTELLIGENCE) ? easy : normal);
				if (vial.isFull()) vial.volume = DewVial.MAX_VOLUME();
				GLog.i(Messages.get(HighGrass.class, color));
			} else {
				level.drop(item, pos).sprite.drop();
			}
		} else level.drop(item, pos).sprite.drop();
	}
}
