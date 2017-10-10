/*
 * Pixel Dungeon
 * Copyright (C) 2012-2015  Oleg Dolya
 *
 * Shattered Pixel Dungeon
 * Copyright (C) 2014-2017 Evan Debenham
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

package com.github.epd.sprout.items.wands;

import com.github.epd.sprout.Assets;
import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.actors.Actor;
import com.github.epd.sprout.actors.Char;
import com.github.epd.sprout.actors.blobs.Blob;
import com.github.epd.sprout.actors.blobs.ConfusionGas;
import com.github.epd.sprout.actors.blobs.CorruptGas;
import com.github.epd.sprout.actors.blobs.ParalyticGas;
import com.github.epd.sprout.actors.blobs.Regrowth;
import com.github.epd.sprout.actors.blobs.ToxicGas;
import com.github.epd.sprout.actors.blobs.VenomGas;
import com.github.epd.sprout.actors.buffs.Buff;
import com.github.epd.sprout.actors.buffs.Burning;
import com.github.epd.sprout.actors.buffs.Frost;
import com.github.epd.sprout.effects.MagicMissile;
import com.github.epd.sprout.effects.Speck;
import com.github.epd.sprout.effects.SpellSprite;
import com.github.epd.sprout.effects.particles.ShadowParticle;
import com.github.epd.sprout.items.Bomb;
import com.github.epd.sprout.items.Generator;
import com.github.epd.sprout.items.scrolls.ScrollOfRecharging;
import com.github.epd.sprout.items.scrolls.ScrollOfTeleportation;
import com.github.epd.sprout.levels.Terrain;
import com.github.epd.sprout.mechanics.Ballistica;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.plants.Plant;
import com.github.epd.sprout.scenes.GameScene;
import com.github.epd.sprout.sprites.ItemSpriteSheet;
import com.github.epd.sprout.utils.GLog;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Callback;
import com.watabou.utils.Random;

public class RandomWand extends Wand {

	//TODO: Add more effects

	{
		name = Messages.get(this, "name");
		image = ItemSpriteSheet.WAND_ADAMANT;
		collisionProperties = Ballistica.PROJECTILE;
	}

	@Override
	protected void onZap(Ballistica bolt) {
		float COMMON_CHANCE = 0.6f;
		float UNCOMMON_CHANCE = 0.3f;
		float RARE_CHANCE = 0.09f;
		float VERY_RARE_CHANCE = 0.01f;
		switch (Random.chances(new float[]{COMMON_CHANCE, UNCOMMON_CHANCE, RARE_CHANCE, VERY_RARE_CHANCE})) {
			case 0:
			default:
				commonEffect(bolt);
				break;
			case 1:
				uncommonEffect(bolt);
				break;
			case 2:
				rareEffect(bolt);
				break;
			case 3:
				veryRareEffect(bolt);
				break;
		}
	}

	@Override
	protected void fx(Ballistica bolt, Callback callback) {
		MagicMissile.whiteLight(curUser.sprite.parent, bolt.sourcePos, bolt.collisionPos, callback);
		Sample.INSTANCE.play(Assets.SND_ZAP);
	}

	@Override
	public String desc() {
		return Messages.get(this, "desc");
	}


	private void commonEffect(Ballistica bolt) {
		switch (Random.Int(4)) {

			//anti-entropy
			case 0:
				Char target = Actor.findChar(bolt.collisionPos);
				switch (Random.Int(2)) {
					case 0:
						if (target != null)
							Buff.affect(target, Burning.class).reignite(target);
						break;
					case 1:
						if (target != null)
							Buff.affect(target, Frost.class, Frost.duration(target) * Random.Float(3f, 5f));
						break;
				}

				break;

			//spawns some regrowth
			case 1:

				int c = Dungeon.level.map[bolt.collisionPos];
				if (c == Terrain.EMPTY ||
						c == Terrain.EMBERS ||
						c == Terrain.EMPTY_DECO ||
						c == Terrain.GRASS ||
						c == Terrain.HIGH_GRASS) {
					GameScene.add(Blob.seed(bolt.collisionPos, 30, Regrowth.class));
				}

				break;

			//random teleportation
			case 2:
				switch (Random.Int(2)) {
					case 0:
						ScrollOfTeleportation.teleportHero(Dungeon.hero);
						break;
					case 1:

						Char ch = Actor.findChar(bolt.collisionPos);
						if (ch != null && !ch.properties().contains(Char.Property.IMMOVABLE)) {
							int count = 10;
							int pos;
							do {
								pos = Dungeon.level.randomRespawnCell();
								if (count-- <= 0) {
									break;
								}
							} while (pos == -1);
							if (pos == -1 || Dungeon.bossLevel()) {
								GLog.w(Messages.get(ScrollOfTeleportation.class, "no_tele"));
							} else {
								ch.pos = pos;
								ch.sprite.place(ch.pos);
								ch.sprite.visible = Dungeon.visible[pos];
							}
						}

						break;
				}
				break;

			//random gas at location
			case 3:

				switch (Random.Int(5)) {
					case 0:
						GameScene.add(Blob.seed(bolt.collisionPos, 800, ConfusionGas.class));
						break;
					case 1:
						GameScene.add(Blob.seed(bolt.collisionPos, 500, ToxicGas.class));
						break;
					case 2:
						GameScene.add(Blob.seed(bolt.collisionPos, 200, ParalyticGas.class));
						break;
					case 3:
						GameScene.add(Blob.seed(bolt.collisionPos, 40 + 20 * level(), VenomGas.class));
						break;
					case 4:
						GameScene.add(Blob.seed(bolt.collisionPos, 40, CorruptGas.class));
				}

				break;
		}

	}

	private static void uncommonEffect(Ballistica bolt) {
		switch (Random.Int(4)) {

			//Random plant
			case 0:

				int pos = bolt.collisionPos;
				//place the plant infront of an enemy so they walk into it.
				if (Actor.findChar(pos) != null && bolt.dist > 1) {
					pos = bolt.path.get(bolt.dist - 1);
				}

				if (pos == Terrain.EMPTY ||
						pos == Terrain.EMBERS ||
						pos == Terrain.EMPTY_DECO ||
						pos == Terrain.GRASS ||
						pos == Terrain.HIGH_GRASS) {
					Dungeon.level.plant((Plant.Seed) Generator.random(Generator.Category.SEED), pos);
				}

				break;


			case 1:
				//Health transfer
				final Char target = Actor.findChar(bolt.collisionPos);
				if (target != null) {

					int damage = Dungeon.hero.lvl * 2;

					Dungeon.hero.HP = Math.min(Dungeon.hero.HT, Dungeon.hero.HP + damage);
					Dungeon.hero.sprite.emitter().burst(Speck.factory(Speck.HEALING), 3);
					target.damage(damage, RandomWand.class);
					target.sprite.emitter().start(ShadowParticle.UP, 0.05f, 10);

				}
				break;

			//Bomb explosion
			case 2:

				new Bomb().explode(bolt.collisionPos);

				break;

			//recharge
			case 3:
				curUser.belongings.charge(true);
				ScrollOfRecharging.charge(Dungeon.hero);
				SpellSprite.show(Dungeon.hero, SpellSprite.CHARGE);
				break;
		}

	}

	private static void rareEffect(Ballistica bolt) {
		switch (Random.Int(4)) {

			//sheep transformation
			case 0:
						/*Char ch = Actor.findChar(bolt.collisionPos);

						if (ch != null && ch != user
								&& !ch.properties().contains(Char.Property.BOSS)
								&& !ch.properties().contains(Char.Property.MINIBOSS)) {
							Sheep sheep = new Sheep();
							sheep.lifespan = 10;
							sheep.pos = ch.pos;
							ch.destroy();
							ch.sprite.killAndErase();
							Dungeon.level.mobs.remove(ch);
							HealthIndicator.instance.target(null);
							GameScene.add(sheep);
							CellEmitter.get(sheep.pos).burst(Speck.factory(Speck.WOOL), 4);
						} else {
							GLog.i(Messages.get(CursedWand.class, "nothing"));
						}
						*/
				break;

			//curses!
			case 1:
				//CursingTrap.curse(user);
				break;


			case 2:
				//inter-level teleportation
				break;


			case 3:
				//summon monsters

				break;
		}
	}

	private static void veryRareEffect(Ballistica bolt) {
		switch (Random.Int(2)) {

			//great forest fire!
			case 0:
				/*for (int i = 0; i < Level.LENGTH; i++) {
					int c = Dungeon.level.map[i];
					if (c == Terrain.EMPTY ||
							c == Terrain.EMBERS ||
							c == Terrain.EMPTY_DECO ||
							c == Terrain.GRASS ||
							c == Terrain.HIGH_GRASS) {
						GameScene.add(Blob.seed(i, 15, Regrowth.class));
					}
				}
				do {
					GameScene.add(Blob.seed(Dungeon.level.randomDestination(), 10, Fire.class));
				} while (Random.Int(5) != 0);
				new Flare(8, 32).color(0xFFFF66, true).show(Dungeon.hero.sprite, 2f);
				Sample.INSTANCE.play(Assets.SND_TELEPORT);
				GLog.p(Messages.get(RandomWand.class, "grass"));
				GLog.w(Messages.get(RandomWand.class, "fire"));*/
				break;

			//superpowered mimic
			case 1:
				/*Mimic mimic = Mimic.spawnAt(bolt.collisionPos, new ArrayList<Item>());
				if (mimic != null) {
					mimic.adjustStats(Dungeon.depth + 10);
					mimic.HP = mimic.HT;
					Item reward;
					do {
						reward = Generator.random(Random.oneOf(Generator.Category.WEAPON, Generator.Category.ARMOR,
								Generator.Category.RING, Generator.Category.WAND));
					} while (reward.level < 2 && !(reward instanceof MissileWeapon));
					Sample.INSTANCE.play(Assets.SND_MIMIC, 1, 1, 0.5f);
					mimic.items.clear();
					mimic.items.add(reward);
				} else {
					GLog.i(Messages.get(RandomWand.class, "nothing"));
				}*/

				break;
		}
	}
}
