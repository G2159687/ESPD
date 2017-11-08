
package com.github.epd.sprout.levels.features;

import com.github.epd.sprout.Assets;
import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.ResultDescriptions;
import com.github.epd.sprout.Statistics;
import com.github.epd.sprout.actors.buffs.Buff;
import com.github.epd.sprout.actors.buffs.Cripple;
import com.github.epd.sprout.actors.buffs.Invisibility;
import com.github.epd.sprout.actors.hero.Hero;
import com.github.epd.sprout.actors.mobs.Mob;
import com.github.epd.sprout.items.consumables.Ankh;
import com.github.epd.sprout.items.quest.SanChikarahTranscend;
import com.github.epd.sprout.items.artifacts.DriedRose;
import com.github.epd.sprout.items.artifacts.TimekeepersHourglass;
import com.github.epd.sprout.levels.RegularLevel;
import com.github.epd.sprout.levels.rooms.Room;
import com.github.epd.sprout.levels.rooms.special.WeakFloorRoom;
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
	private static final String TXT_JUMPDANGER = Messages.get(Chasm.class, "jumpdanger");

	public static boolean jumpConfirmed = false;

	public static void heroJump(final Hero hero) {
		if (Dungeon.depth != 33) {
			GameScene.show(new WndOptions(TXT_CHASM, TXT_JUMP, TXT_YES, TXT_NO) {
				@Override
				protected void onSelect(int index) {
					if (index == 0) {
						jumpConfirmed = true;
						hero.resume();
					}
				}
			});
		} else {
			GameScene.show(new WndOptions(TXT_CHASM, TXT_JUMPDANGER, TXT_YES, TXT_NO) {
				@Override
				protected void onSelect(int index) {
					if (index == 0) {
						jumpConfirmed = true;
						hero.resume();
					}
				}
			});
		}
	}


	public static void heroFall(int pos) {

		jumpConfirmed = false;

		Sample.INSTANCE.play(Assets.SND_FALLING);

		Buff buff = Dungeon.hero.buff(TimekeepersHourglass.timeFreeze.class);
		if (buff != null)
			buff.detach();

		Buff buffinv = Dungeon.hero.buff(Invisibility.class);
		if (buffinv != null)
			buffinv.detach();
		Invisibility.dispel();
		Dungeon.hero.invisible = 0;

		for (Mob mob : Dungeon.level.mobs.toArray(new Mob[0]))
			if (mob instanceof DriedRose.GhostHero)
				mob.destroy();

		if (Dungeon.depth > 0 && Dungeon.depth < 25){
			if (!Dungeon.level.cleared){
				Statistics.prevfloormoves = 0;
			}
		}

		Ankh ankh = Dungeon.hero.belongings.getItem(Ankh.class);
		if (Dungeon.depth == 33) {
			if (ankh != null)
				ankh.detachAll(Dungeon.hero.belongings.backpack);
			Dungeon.hero.HP = 1;
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
						&& room instanceof WeakFloorRoom;
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
