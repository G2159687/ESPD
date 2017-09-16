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
package com.github.epd.sprout.actors.mobs.npcs;

import com.github.epd.sprout.Assets;
import com.github.epd.sprout.Challenges;
import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.Journal;
import com.github.epd.sprout.actors.Actor;
import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.blobs.Blob;
import com.github.epd.sprout.actors.blobs.ToxicGas;
import com.github.epd.sprout.actors.buffs.Buff;
import com.github.epd.sprout.actors.buffs.Roots;
import com.github.epd.sprout.actors.hero.Hero;
import com.github.epd.sprout.actors.mobs.Mob;
import com.github.epd.sprout.effects.CellEmitter;
import com.github.epd.sprout.effects.Speck;
import com.github.epd.sprout.items.Generator;
import com.github.epd.sprout.items.Heap;
import com.github.epd.sprout.items.Item;
import com.github.epd.sprout.items.potions.PotionOfStrength;
import com.github.epd.sprout.items.quest.CorpseDust;
import com.github.epd.sprout.items.wands.Wand;
import com.github.epd.sprout.levels.PrisonLevel;
import com.github.epd.sprout.levels.Room;
import com.github.epd.sprout.levels.Terrain;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.plants.Plant;
import com.github.epd.sprout.scenes.GameScene;
import com.github.epd.sprout.sprites.ItemSpriteSheet;
import com.github.epd.sprout.sprites.WandmakerSprite;
import com.github.epd.sprout.utils.GLog;
import com.github.epd.sprout.utils.Utils;
import com.github.epd.sprout.windows.WndQuest;
import com.github.epd.sprout.windows.WndWandmaker;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;
import com.watabou.utils.Random;

import java.util.ArrayList;

public class Wandmaker extends NPC {

	{
		name = Messages.get(Wandmaker.class, "name");
		spriteClass = WandmakerSprite.class;

		properties.add(Property.IMMOVABLE);
	}

	private static final String TXT_BERRY1 = Messages.get(Wandmaker.class, "berry1");

	private static final String TXT_DUST1 = Messages.get(Wandmaker.class, "dust1");

	private static final String TXT_BERRY2 = Messages.get(Wandmaker.class, "berry2", Dungeon.hero.givenName());

	private static final String TXT_DUST2 = Messages.get(Wandmaker.class, "dust2", Dungeon.hero.givenName());

	@Override
	protected boolean act() {
		throwItem();
		return super.act();
	}

	@Override
	public int defenseSkill(Char enemy) {
		return 1000;
	}

	@Override
	public String defenseVerb() {
		return Messages.get(Tinkerer1.class, "def");
	}

	@Override
	public void damage(int dmg, Object src) {
	}

	@Override
	public void add(Buff buff) {
	}

	@Override
	public boolean reset() {
		return true;
	}

	@Override
	public boolean interact() {

		sprite.turnTo(pos, Dungeon.hero.pos);
		if (Quest.given) {

			Item item = Quest.alternative ? Dungeon.hero.belongings
					.getItem(CorpseDust.class) : Dungeon.hero.belongings
					.getItem(Rotberry.Seed.class);
			if (item != null) {
				GameScene.show(new WndWandmaker(this, item));
			} else {
				tell(Quest.alternative ? TXT_DUST2 : TXT_BERRY2,
						Dungeon.hero.givenName());
			}

		} else {

			Quest.placeItem();

			if (Quest.given)
				tell(Quest.alternative ? TXT_DUST1 : TXT_BERRY1);

			Journal.add(Journal.Feature.WANDMAKER);
		}
		return false;
	}

	private void tell(String format, Object... args) {
		GameScene.show(new WndQuest(this, Utils.format(format, args)));
	}

	@Override
	public String description() {
		return Messages.get(Wandmaker.class, "desc");
	}

	public static class Quest {

		private static boolean spawned;

		private static boolean alternative;

		private static boolean given;

		public static Wand wand1;
		public static Wand wand2;

		public static void reset() {
			spawned = false;

			wand1 = null;
			wand2 = null;
		}

		private static final String NODE = "wandmaker";

		private static final String SPAWNED = "spawned";
		private static final String ALTERNATIVE = "alternative";
		private static final String GIVEN = "given";
		private static final String WAND1 = "wand1";
		private static final String WAND2 = "wand2";

		public static void storeInBundle(Bundle bundle) {

			Bundle node = new Bundle();

			node.put(SPAWNED, spawned);

			if (spawned) {

				node.put(ALTERNATIVE, alternative);

				node.put(GIVEN, given);

				node.put(WAND1, wand1);
				node.put(WAND2, wand2);
			}

			bundle.put(NODE, node);
		}

		public static void restoreFromBundle(Bundle bundle) {

			Bundle node = bundle.getBundle(NODE);

			if (!node.isNull() && (spawned = node.getBoolean(SPAWNED))) {

				alternative = node.getBoolean(ALTERNATIVE);

				given = node.getBoolean(GIVEN);

				wand1 = (Wand) node.get(WAND1);
				wand2 = (Wand) node.get(WAND2);
			} else {
				reset();
			}
		}

		public static void spawn(PrisonLevel level, Room room) {
			if (!spawned && Dungeon.depth == 7) {

				Wandmaker npc = new Wandmaker();
				do {
					npc.pos = room.random();
				} while (level.map[npc.pos] == Terrain.ENTRANCE
						|| level.map[npc.pos] == Terrain.SIGN);
				level.mobs.add(npc);
				Actor.occupyCell(npc);

				spawned = true;


				alternative = Random.Int(2) == 0;


				given = false;

				wand1 = (Wand) Generator.random(Generator.Category.WAND);
				wand1.random().upgrade();
				if (Dungeon.isChallenged(Challenges.NO_SCROLLS)) {
					wand1.upgrade(30);
				}
				do {
					wand2 = (Wand) Generator.random(Generator.Category.WAND);
				} while (wand2.getClass().equals(wand1.getClass()));
				wand2.random().upgrade();
				if (Dungeon.isChallenged(Challenges.NO_SCROLLS)) {
					wand2.upgrade(30);
				}
			}
		}

		public static void placeItem() {
			if (alternative) {

				ArrayList<Heap> candidates = new ArrayList<Heap>();
				for (Heap heap : Dungeon.level.heaps.values()) {
					if (heap.type == Heap.Type.SKELETON) {
						candidates.add(heap);
					}
				}

				if (candidates.size() > 0) {
					Random.element(candidates).drop(new CorpseDust());
					given = true;
				} else {
					int pos = Dungeon.level.randomRespawnCell();
					while (Dungeon.level.heaps.get(pos) != null) {
						pos = Dungeon.level.randomRespawnCell();
					}

					if (pos != -1) {
						Heap heap = Dungeon.level.drop(new CorpseDust(), pos);
						heap.type = Heap.Type.SKELETON;
						heap.sprite.link();
						given = true;
					}
				}

			} else {

				int shrubPos = Dungeon.level.randomRespawnCell();
				while (Dungeon.level.heaps.get(shrubPos) != null) {
					shrubPos = Dungeon.level.randomRespawnCell();
				}

				if (shrubPos != -1) {
					Dungeon.level.plant(new Rotberry.Seed(), shrubPos);
					given = true;
				}

				int shrubPos2 = Dungeon.level.randomRespawnCell();
				while (Dungeon.level.heaps.get(shrubPos2) != null) {
					shrubPos2 = Dungeon.level.randomRespawnCell();
				}

				if (shrubPos2 != -1) {
					Dungeon.level.plant(new Rotberry.Seed(), shrubPos2);
					given = true;
				}

			}
		}

		public static void complete() {
			wand1 = null;
			wand2 = null;

			Journal.remove(Journal.Feature.WANDMAKER);
		}
	}

	public static class Rotberry extends Plant {

		private static final String TXT_DESC = Messages.get(Wandmaker.class, "berrydesc");

		{
			image = 7;
			plantName = Messages.get(Wandmaker.class, "berryname");
		}

		@Override
		public void activate(Char ch) {
			super.activate(ch);

			GameScene.add(Blob.seed(pos, 100, ToxicGas.class));

			Dungeon.level.drop(new Seed(), pos).sprite.drop();

			if (ch != null) {
				Buff.prolong(ch, Roots.class, TICK * 3);
			}
		}

		@Override
		public String desc() {
			return TXT_DESC;
		}

		public static class Seed extends Plant.Seed {
			{
				plantName = Messages.get(Wandmaker.class, "berryname");

				name = Messages.get(Wandmaker.class, "seedname", plantName);
				image = ItemSpriteSheet.SEED_ROTBERRY;

				plantClass = Rotberry.class;
				alchemyClass = PotionOfStrength.class;
			}

			@Override
			public boolean doPickUp(Hero hero) {
				if (super.doPickUp(hero)) {

					if (Dungeon.level != null) {
						for (Mob mob : Dungeon.level.mobs) {
							mob.beckon(Dungeon.hero.pos);
						}

						GLog.w(Messages.get(Wandmaker.class, "pickup"));
						CellEmitter.center(Dungeon.hero.pos).start(
								Speck.factory(Speck.SCREAM), 0.3f, 3);
						Sample.INSTANCE.play(Assets.SND_CHALLENGE);
					}

					return true;
				} else {
					return false;
				}
			}

			@Override
			public String desc() {
				return TXT_DESC;
			}
		}
	}
}
