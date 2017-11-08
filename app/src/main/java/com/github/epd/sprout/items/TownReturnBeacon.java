
package com.github.epd.sprout.items;

import com.github.epd.sprout.Badges;
import com.github.epd.sprout.Dungeon;
import com.github.epd.sprout.actors.Actor;
import com.github.epd.sprout.actors.buffs.Buff;
import com.github.epd.sprout.actors.buffs.Invisibility;
import com.github.epd.sprout.actors.hero.Hero;
import com.github.epd.sprout.actors.mobs.Mob;
import com.github.epd.sprout.items.artifacts.DriedRose;
import com.github.epd.sprout.items.artifacts.TimekeepersHourglass;
import com.github.epd.sprout.messages.Messages;
import com.github.epd.sprout.scenes.InterlevelScene;
import com.github.epd.sprout.sprites.ItemSpriteSheet;
import com.github.epd.sprout.utils.GLog;
import com.watabou.noosa.Game;
import com.watabou.utils.Bundle;
import com.watabou.utils.PathFinder;

import java.util.ArrayList;

public class TownReturnBeacon extends Item {


	private static final String TXT_INFO = Messages.get(ReturnBeacon.class, "desc");

	public static final float TIME_TO_USE = 1;

	//public static final String AC_SET = "SET";
	public static final String AC_RETURN = Messages.get(TownReturnBeacon.class, "ac_return");
	public static final String AC_RETURNTOWN = Messages.get(TownReturnBeacon.class, "ac_mine");
	public static final String FAIL = Messages.get(TownReturnBeacon.class, "fail");

	private int returnDepth = -1;
	private int returnPos;

	{
		name = Messages.get(this, "dol") + Messages.get(ReturnBeacon.class, "name");
		image = ItemSpriteSheet.BEACON;

		unique = true;
	}

	private static final String DEPTH = "depth";
	private static final String POS = "pos";

	@Override
	public void storeInBundle(Bundle bundle) {
		super.storeInBundle(bundle);
		bundle.put(DEPTH, returnDepth);
		if (returnDepth != -1) {
			bundle.put(POS, returnPos);
		}
	}

	@Override
	public void restoreFromBundle(Bundle bundle) {
		super.restoreFromBundle(bundle);
		returnDepth = bundle.getInt(DEPTH);
		returnPos = bundle.getInt(POS);
	}


	@Override
	public ArrayList<String> actions(Hero hero) {
		ArrayList<String> actions = super.actions(hero);
		if (Dungeon.depth == 55 && returnDepth > 55 && !Badges.checkOtilukeRescued()) {
			actions.add(AC_RETURN);
		}
		if (Dungeon.depth > 55) {
			actions.add(AC_RETURNTOWN);
		}
		return actions;
	}

	@Override
	public void execute(Hero hero, String action) {

		if (action == AC_RETURNTOWN) {

			if (Dungeon.bossLevel() || (Dungeon.level.locked && Dungeon.depth != 66) || hero.petfollow) {
				hero.spend(TIME_TO_USE);
				GLog.w(FAIL);
				return;
			}

		}

		if (action == AC_RETURNTOWN) {

			hero.spend(TIME_TO_USE);

			returnDepth = Dungeon.depth;
			returnPos = hero.pos;

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

			InterlevelScene.mode = InterlevelScene.Mode.RETURN;
			InterlevelScene.returnDepth = 55;
			InterlevelScene.returnPos = 1925;
			Game.switchScene(InterlevelScene.class);

		} else if (action == AC_RETURN) {

			hero.spend(TIME_TO_USE);

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

			InterlevelScene.mode = InterlevelScene.Mode.RETURN;
			InterlevelScene.returnDepth = returnDepth;
			InterlevelScene.returnPos = returnPos;
			Game.switchScene(InterlevelScene.class);

		} else {

			super.execute(hero, action);

		}
	}


	@Override
	public boolean isUpgradable() {
		return false;
	}

	@Override
	public boolean isIdentified() {
		return true;
	}


	@Override
	public String info() {
		return TXT_INFO;
	}
}
