
package com.github.epd.sprout.items.teleporter;

import com.github.epd.sprout.Assets;
import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.Statistics;
import com.github.epd.sprout.actors.buffs.Buff;
import com.github.epd.sprout.actors.buffs.Invisibility;
import com.github.epd.sprout.actors.hero.Hero;
import com.github.epd.sprout.actors.mobs.Mob;
import com.github.epd.sprout.actors.mobs.pets.PET;
import com.github.epd.sprout.effects.particles.ElmoParticle;
import com.github.epd.sprout.items.artifacts.DriedRose;
import com.github.epd.sprout.items.artifacts.TimekeepersHourglass;
import com.github.epd.sprout.items.bombs.FishingBomb;
import com.github.epd.sprout.items.bombs.HolyHandGrenade;
import com.github.epd.sprout.items.Item;
import com.github.epd.sprout.items.food.Blackberry;
import com.github.epd.sprout.items.food.Blueberry;
import com.github.epd.sprout.items.food.Cloudberry;
import com.github.epd.sprout.items.food.FullMoonberry;
import com.github.epd.sprout.items.food.GoldenNut;
import com.github.epd.sprout.items.food.Moonberry;
import com.github.epd.sprout.items.keys.IronKey;
import com.github.epd.sprout.items.keys.SkeletonKey;
import com.github.epd.sprout.items.weapon.missiles.ForestDart;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.scenes.GameScene;
import com.github.epd.sprout.scenes.InterlevelScene;
import com.github.epd.sprout.sprites.ItemSpriteSheet;
import com.github.epd.sprout.utils.GLog;
import com.github.epd.sprout.windows.WndBag;
import com.github.epd.sprout.windows.WndOtiluke;
import com.watabou.noosa.Game;
import com.watabou.noosa.audio.Sample;
import com.watabou.utils.Bundle;

import java.util.ArrayList;

public class OtilukesJournal extends Item {

	public final float TIME_TO_USE = 1;

	public static final String AC_RETURN = Messages.get(OtilukesJournal.class, "ac_return");
	public static final String AC_ADD = Messages.get(OtilukesJournal.class, "ac_add");
	public static final String AC_PORT = Messages.get(OtilukesJournal.class, "ac_read");

	protected String inventoryTitle = Messages.get(OtilukesJournal.class, "title");
	protected WndBag.Mode mode = WndBag.Mode.JOURNALPAGES;


	public int returnDepth = -1;
	public int returnPos;

	public boolean[] rooms = new boolean[20];
	public boolean[] firsts = new boolean[20];

	{
		name = Messages.get(this, "name");
		image = ItemSpriteSheet.OTILUKES_JOURNAL;

		unique = true;

	}

	private static final String DEPTH = "depth";
	private static final String POS = "pos";
	private static final String ROOMS = "rooms";
	private static final String FIRSTS = "firsts";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(DEPTH, returnDepth);
		bundle.put(ROOMS, rooms);
		bundle.put(FIRSTS, firsts);
		if (returnDepth != -1) {
			bundle.put(POS, returnPos);
		}
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		returnDepth = bundle.getInt(DEPTH);
		returnPos = bundle.getInt(POS);
		rooms = bundle.getBooleanArray(ROOMS);
		firsts = bundle.getBooleanArray(FIRSTS);
	}

	@Override
	public ArrayList<String> actions(Hero hero) {
		ArrayList<String> actions = super.actions(hero);
		actions.add(AC_ADD);
		if (returnDepth > 0 && (Dungeon.depth < 56 || Dungeon.depth == 68 || Dungeon.depth == 69)
				&& Dungeon.depth > 49 && !hero.petfollow) {
			actions.add(AC_RETURN);
		}
		if (returnDepth > 0 && !hero.petfollow && (Dungeon.depth >= 27 && Dungeon.depth <= 30)) {
			actions.add(AC_RETURN);
		}
		if (returnDepth > 0 && !hero.petfollow && (Dungeon.depth == 25)) {
			actions.add(AC_RETURN);
		}
		if (returnDepth > 0 && !hero.petfollow && (Dungeon.depth >= 36 && Dungeon.depth <= 40)){
			actions.add(AC_RETURN);
		}
		if (Dungeon.depth < 26 && !hero.petfollow && !Dungeon.bossLevel()) {
			actions.add(AC_PORT);
		}

		return actions;
	}

	@Override
	public void execute(Hero hero, String action) {

		if (action == AC_PORT) {

			if (Dungeon.bossLevel()) {
				hero.spend(TIME_TO_USE);
				GLog.w(Messages.get(OtilukesJournal.class, "prevent"));
				return;
			}

			GameScene.show(new WndOtiluke(rooms, this));

		}

		if (action == AC_RETURN) {

			hero.spend(TIME_TO_USE);

			switch (Dungeon.depth){
				case 27: {
					ForestDart dart = Dungeon.hero.belongings.getItem(ForestDart.class);
					if (dart != null) {
						dart.detachAll(Dungeon.hero.belongings.backpack);
					}
					if (Statistics.archersKilled > 49) {
						ForestDart newdart = new ForestDart(30);
						newdart.doPickUp(Dungeon.hero);
					}
					break;
				}

				case 28: {
					HolyHandGrenade bomb = Dungeon.hero.belongings.getItem(HolyHandGrenade.class);
					if (bomb != null) {
						bomb.detachAll(Dungeon.hero.belongings.backpack);
					}
					if (Statistics.skeletonsKilled > 49) {
						HolyHandGrenade newbomb = new HolyHandGrenade(20);
						newbomb.doPickUp(Dungeon.hero);
					}
					if (Statistics.skeletonsKilled > 79 && Dungeon.checkNight()) {
						FullMoonberry berry = new FullMoonberry();
						berry.doPickUp(Dungeon.hero);
					}
					break;
				}

				case 29: {
					FishingBomb bomb = Dungeon.hero.belongings.getItem(FishingBomb.class);
					if (bomb != null) {
						bomb.detachAll(Dungeon.hero.belongings.backpack);
					}
					if (Statistics.albinoPiranhasKilled > 49) {
						Moonberry berry1 = new Moonberry(10);
						berry1.doPickUp(Dungeon.hero);
						Cloudberry berry2 = new Cloudberry(10);
						berry2.doPickUp(Dungeon.hero);
						Blueberry berry3 = new Blueberry(10);
						berry3.doPickUp(Dungeon.hero);
						Blackberry berry4 = new Blackberry(10);
						berry4.doPickUp(Dungeon.hero);

						if (Dungeon.checkNight()) {
							FullMoonberry berry = new FullMoonberry();
							berry.doPickUp(Dungeon.hero);
						}
					}
					break;
				}

				case 30: {
					if (Statistics.goldThievesKilled > 49 && Statistics.skeletonsKilled > 49
							&& Statistics.albinoPiranhasKilled > 49 && Statistics.archersKilled > 49) {
						GoldenNut nut = new GoldenNut();
						nut.doPickUp(Dungeon.hero);
					}
					break;
				}

				case 25: {
					if (Statistics.amuletObtained) {
						rooms[12] = false;
					}
					SkeletonKey key = Dungeon.hero.belongings.getItem(SkeletonKey.class);
					if (key != null) {
						key.detachAll(Dungeon.hero.belongings.backpack);
					}
					break;
				}

				case 40: {
					if (!Dungeon.banditkingkilled){
						GLog.w(Messages.get(AncientCoin.class, "prevent"));
						return;
					} else {
						rooms[13] = false;
					}
					break;
				}

				case 37: {
					if (!Dungeon.skeletonkingkilled){
						GLog.w(Messages.get(Bone.class,"prevent"));
						return;
					} else {
						rooms[14] = false;
					}
					break;
				}

				case 38: {
					if (!Dungeon.crabkingkilled){
						GLog.w(Messages.get(ConchShell.class,"prevent"));
						return;
					} else {
						rooms[15] = false;
					}
					break;
				}

				case 36: {
					if (!Dungeon.tengudenkilled) {
						GLog.w(Messages.get(TenguKey.class, "prevent"));
						return;
					} else {
						rooms[16] = false;
					}
					break;
				}
			}


			Buff buff = Dungeon.hero
					.buff(TimekeepersHourglass.timeFreeze.class);
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

			IronKey key = hero.belongings.getKey(IronKey.class, Dungeon.depth);
			if (key != null) {
				key.detachAll(Dungeon.hero.belongings.backpack);
			}

			updateQuickslot();
			checkPetPort();

			InterlevelScene.mode = InterlevelScene.Mode.RETURN;
			InterlevelScene.returnDepth = returnDepth;
			InterlevelScene.returnPos = returnPos;
			Game.switchScene(InterlevelScene.class);
			returnDepth = -1;
		}

		if (action == AC_ADD) {

			GameScene.selectItem(itemSelector, mode, inventoryTitle);

		} else {

			super.execute(hero, action);

		}
	}

	@Override
	public int price() {
		return 300 * quantity;
	}

	public void reset() {
		returnDepth = -1;
	}

	@Override
	public boolean isUpgradable() {
		return false;
	}

	@Override
	public boolean isIdentified() {
		return true;
	}

	private PET checkpet() {
		for (Mob mob : Dungeon.level.mobs) {
			if (mob instanceof PET) {
				return (PET) mob;
			}
		}
		return null;
	}

	private void checkPetPort() {
		PET pet = checkpet();
		if (pet != null) {
			Dungeon.hero.petType = pet.type;
			Dungeon.hero.petLevel = pet.level;
			Dungeon.hero.petKills = pet.kills;
			Dungeon.hero.petHP = pet.HP;
			Dungeon.hero.petExperience = pet.experience;
			Dungeon.hero.petCooldown = pet.cooldown;
			pet.destroy();
			Dungeon.hero.petfollow = true;
		} else Dungeon.hero.petfollow = Dungeon.hero.haspet && Dungeon.hero.petfollow;

	}

	@Override
	public String info() {
		return Messages.get(OtilukesJournal.class, "desc1");
	}

	protected WndBag.Listener itemSelector = new WndBag.Listener() {
		@Override
		public void onSelect(Item item) {
			if (item != null && item instanceof JournalPage) {
				Hero hero = Dungeon.hero;
				int room = ((JournalPage) item).room;

				hero.sprite.operate(hero.pos);
				hero.busy();
				hero.spend(2f);
				Sample.INSTANCE.play(Assets.SND_BURNING);
				hero.sprite.emitter().burst(ElmoParticle.FACTORY, 12);

				item.detach(hero.belongings.backpack);
				GLog.h(Messages.get(OtilukesJournal.class, "add"));

				rooms[room] = true;
				firsts[room] = true;

			}
		}
	};


}
