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

import com.github.epd.sprout.Assets;
import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.ResultDescriptions;
import com.github.epd.sprout.Statistics;
import com.github.epd.sprout.actors.buffs.Buff;
import com.github.epd.sprout.actors.buffs.Cripple;
import com.github.epd.sprout.actors.hero.Hero;
import com.github.epd.sprout.actors.mobs.Mob;
import com.github.epd.sprout.actors.mobs.pets.PET;
import com.github.epd.sprout.items.SanChikarahTranscend;
import com.github.epd.sprout.items.artifacts.DriedRose;
import com.github.epd.sprout.items.artifacts.TimekeepersHourglass;
import com.github.epd.sprout.levels.RegularLevel;
import com.github.epd.sprout.levels.Room;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.scenes.GameScene;
import com.github.epd.sprout.scenes.InterlevelScene;
import com.github.epd.sprout.sprites.MobSprite;
import com.github.epd.sprout.utils.GLog;
import com.github.epd.sprout.windows.WndOptions;
import com.watabou.noosa.Camera;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Random;

public class Chasm {

	private static final String TXT_CHASM = Messages.get(Chasm.class, "chasm");
	private static final String TXT_YES = Messages.get(Chasm.class, "yes");
	private static final String TXT_NO = Messages.get(Chasm.class, "no");
	private static final String TXT_JUMP = Messages.get(Chasm.class, "jump");

	public static boolean jumpConfirmed = false;

	public static void heroJump(final Hero hero) {
		GameScene.show(new WndOptions(TXT_CHASM, TXT_JUMP, TXT_YES, TXT_NO) {
			@Override
			protected void onSelect(int index) {
				if (index == 0) {
					jumpConfirmed = true;
					hero.resume();
				}
			}
		});
	}


	public static void heroFall(int pos) {

		jumpConfirmed = false;

		Sample.INSTANCE.play(Assets.SND_FALLING);

		Buff buff = Dungeon.hero.buff(TimekeepersHourglass.timeFreeze.class);
		if (buff != null)
			buff.detach();

		for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0]))
			if (mob instanceof DriedRose.GhostHero)
				mob.destroy();

		if (Dungeon.depth > 0 && Dungeon.depth < 25){
			if (!Dungeon.level.cleared){
				Statistics.prevfloormoves = 0;
			}
		}

		PET pet = Dungeon.hero.checkpet();
		if (pet != null && Dungeon.depth == 33) {
			Dungeon.hero.petType = pet.type;
			Dungeon.hero.petLevel = pet.level;
			Dungeon.hero.petKills = pet.kills;
			Dungeon.hero.petHP = pet.HP;
			Dungeon.hero.petExperience = pet.experience;
			Dungeon.hero.petCooldown = pet.cooldown;
			pet.destroy();
			Dungeon.hero.petfollow = true;
		} else Dungeon.hero.petfollow = Dungeon.hero.haspet && Dungeon.hero.petfollow;

		SanChikarahTranscend san = Dungeon.hero.belongings.getItem(SanChikarahTranscend.class);
		if (Dungeon.depth == 33 && san != null) {
			san.detachAll(Dungeon.hero.belongings.backpack);
			Dungeon.sanchikarahtranscend = false;
		}

		/*if(Dungeon.depth==33){
		  for (Mob mob : Dungeon.level.mobs) {
			if(mob instanceof PET) {				 
				Dungeon.hero.haspet=false;
				Dungeon.hero.petCount++;
				mob.destroy();				
			}
		  }
		}*/

		if (Dungeon.hero.isAlive()) {
			Dungeon.hero.interrupt();
			InterlevelScene.mode = InterlevelScene.Mode.FALL;
			if (Dungeon.level instanceof RegularLevel) {
				Room room = ((RegularLevel) Dungeon.level).room(pos);
				InterlevelScene.fallIntoPit = room != null
						&& room.type == Room.Type.WEAK_FLOOR;
			} else {
				InterlevelScene.fallIntoPit = false;
			}
			Game.switchScene(InterlevelScene.class);
		} else {
			Dungeon.hero.sprite.visible = false;
		}
	}


	public static void heroLand() {

		Hero hero = Dungeon.hero;

		hero.sprite.burst(hero.sprite.blood(), 10);
		Camera.main.shake(4, 0.2f);

		Buff.prolong(hero, Cripple.class, Cripple.DURATION);


		hero.damage(Random.IntRange(hero.HT / 3, hero.HT / 2), new Hero.Doom() {
			@Override
			public void onDeath() {
				Dungeon.fail(ResultDescriptions.FALL);
				GLog.n(Messages.get(Chasm.class, "ondeath"));
			}
		});
	}

	public static void mobFall(Mob mob) {
		mob.die(null);

		((MobSprite) mob.sprite).fall();
	}
}
