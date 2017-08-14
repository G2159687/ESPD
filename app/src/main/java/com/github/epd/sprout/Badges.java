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
package com.github.epd.sprout;

import android.content.Context;

import com.watabou.noosa.Game;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

public class Badges {

	public enum Badge {
		BOSS_SLAIN_1_WARRIOR, BOSS_SLAIN_1_MAGE, BOSS_SLAIN_1_ROGUE, BOSS_SLAIN_1_HUNTRESS, BOSS_SLAIN_1("1st boss slain", 12), BOSS_SLAIN_2("2nd boss slain", 13), BOSS_SLAIN_3(
				"3rd boss slain", 14), BOSS_SLAIN_4("4th boss slain", 15), BOSS_SLAIN_1_ALL_CLASSES(
				"1st boss slain by Warrior, Mage, Rogue & Huntress", 32, true), BOSS_SLAIN_3_GLADIATOR, BOSS_SLAIN_3_BERSERKER, BOSS_SLAIN_3_WARLOCK, BOSS_SLAIN_3_BATTLEMAGE, BOSS_SLAIN_3_FREERUNNER, BOSS_SLAIN_3_ASSASSIN, BOSS_SLAIN_3_SNIPER, BOSS_SLAIN_3_WARDEN, BOSS_SLAIN_3_ALL_SUBCLASSES(
				"3rd boss slain by Gladiator, Berserker, Warlock, Battlemage, "
						+ "Freerunner, Assassin, Sniper & Warden", 33, true),  MASTERY_WARRIOR, MASTERY_MAGE, MASTERY_ROGUE, MASTERY_HUNTRESS, ITEM_LEVEL_1(
				"Item of level 3 acquired", 48), VICTORY_WARRIOR, VICTORY_MAGE, VICTORY_ROGUE, VICTORY_HUNTRESS, VICTORY(
				"Amulet of Yendor obtained", 22), VICTORY_ALL_CLASSES(
				"Amulet of Yendor obtained by Warrior, Mage, Rogue & Huntress",
				36, true), MASTERY_COMBO("7-hit combo", 56), NO_MONSTERS_SLAIN(
				"Level completed without killing any monsters", 28), CHAMPION(
				"Challenge won", 39, true), SUPPORTER(
				"Thanks for your support!", 31, true),
				SUPPORTER2("Thanks for your support!", 31, true),
				 ORB("Orb of Zot obtained!", 68),
				 OTILUKE("Otiluke rescued!",65)
				 ;

		public boolean meta;

		public String description;
		public int image;

		Badge(String description, int image) {
			this(description, image, false);
		}

		Badge(String description, int image, boolean meta) {
			this.description = description;
			this.image = image;
			this.meta = meta;
		}

		Badge() {
			this("", -1);
		}
	}

	private static HashSet<Badge> global;
	private static HashSet<Badge> local = new HashSet<Badges.Badge>();

	private static boolean saveNeeded = false;

	public static Callback loadingListener = null;

	public static void reset() {
		local.clear();
		loadGlobal();
	}

	private static final String BADGES_FILE = "badges.dat";
	private static final String BADGES = "badges";

	private static HashSet<Badge> restore(Bundle bundle) {
		HashSet<Badge> badges = new HashSet<Badge>();

		String[] names = bundle.getStringArray(BADGES);
		for (int i = 0; i < names.length; i++) {
			try {
				badges.add(Badge.valueOf(names[i]));
			} catch (Exception e) {
			}
		}

		return badges;
	}

	private static void store(Bundle bundle, HashSet<Badge> badges) {
		int count = 0;
		String names[] = new String[badges.size()];

		for (Badge badge : badges) {
			names[count++] = badge.toString();
		}
		bundle.put(BADGES, names);
	}

	public static void loadLocal(Bundle bundle) {
		local = restore(bundle);
	}

	public static void saveLocal(Bundle bundle) {
		store(bundle, local);
	}

	public static void loadGlobal() {
		if (global == null) {
			try {
				InputStream input = Game.instance.openFileInput(BADGES_FILE);
				Bundle bundle = Bundle.read(input);
				input.close();

				global = restore(bundle);

			} catch (Exception e) {
				global = new HashSet<Badge>();
			}
		}
	}

	public static void saveGlobal() {
		if (saveNeeded) {

			Bundle bundle = new Bundle();
			store(bundle, global);

			try {
				OutputStream output = Game.instance.openFileOutput(BADGES_FILE,
						Context.MODE_PRIVATE);
				Bundle.write(bundle, output);
				output.close();
				saveNeeded = false;
			} catch (IOException e) {

			}
		}
	}

	public static void validateBossSlain() {
		Badge badge = null;
		switch (Dungeon.depth) {
		case 5:
			badge = Badge.BOSS_SLAIN_1;
			break;
		case 10:
			badge = Badge.BOSS_SLAIN_2;
			break;
		case 15:
			badge = Badge.BOSS_SLAIN_3;
			break;
		case 20:
			badge = Badge.BOSS_SLAIN_4;
			break;
		}

		if (badge != null) {
			local.add(badge);
			displayBadge(badge);

			if (badge == Badge.BOSS_SLAIN_1) {
				switch (Dungeon.hero.heroClass) {
				case WARRIOR:
					badge = Badge.BOSS_SLAIN_1_WARRIOR;
					break;
				case MAGE:
					badge = Badge.BOSS_SLAIN_1_MAGE;
					break;
				case ROGUE:
					badge = Badge.BOSS_SLAIN_1_ROGUE;
					break;
				case HUNTRESS:
					badge = Badge.BOSS_SLAIN_1_HUNTRESS;
					break;
				}
				local.add(badge);
				if (!global.contains(badge)) {
					global.add(badge);
					saveNeeded = true;
				}

				if (global.contains(Badge.BOSS_SLAIN_1_WARRIOR)
						&& global.contains(Badge.BOSS_SLAIN_1_MAGE)
						&& global.contains(Badge.BOSS_SLAIN_1_ROGUE)
						&& global.contains(Badge.BOSS_SLAIN_1_HUNTRESS)) {

					badge = Badge.BOSS_SLAIN_1_ALL_CLASSES;
					if (!global.contains(badge)) {
						displayBadge(badge);
						global.add(badge);
						saveNeeded = true;
					}
				}
			} else if (badge == Badge.BOSS_SLAIN_3) {
				switch (Dungeon.hero.subClass) {
				case GLADIATOR:
					badge = Badge.BOSS_SLAIN_3_GLADIATOR;
					break;
				case BERSERKER:
					badge = Badge.BOSS_SLAIN_3_BERSERKER;
					break;
				case WARLOCK:
					badge = Badge.BOSS_SLAIN_3_WARLOCK;
					break;
				case BATTLEMAGE:
					badge = Badge.BOSS_SLAIN_3_BATTLEMAGE;
					break;
				case FREERUNNER:
					badge = Badge.BOSS_SLAIN_3_FREERUNNER;
					break;
				case ASSASSIN:
					badge = Badge.BOSS_SLAIN_3_ASSASSIN;
					break;
				case SNIPER:
					badge = Badge.BOSS_SLAIN_3_SNIPER;
					break;
				case WARDEN:
					badge = Badge.BOSS_SLAIN_3_WARDEN;
					break;
				default:
					return;
				}
				local.add(badge);
				if (!global.contains(badge)) {
					global.add(badge);
					saveNeeded = true;
				}

				if (global.contains(Badge.BOSS_SLAIN_3_GLADIATOR)
						&& global.contains(Badge.BOSS_SLAIN_3_BERSERKER)
						&& global.contains(Badge.BOSS_SLAIN_3_WARLOCK)
						&& global.contains(Badge.BOSS_SLAIN_3_BATTLEMAGE)
						&& global.contains(Badge.BOSS_SLAIN_3_FREERUNNER)
						&& global.contains(Badge.BOSS_SLAIN_3_ASSASSIN)
						&& global.contains(Badge.BOSS_SLAIN_3_SNIPER)
						&& global.contains(Badge.BOSS_SLAIN_3_WARDEN)) {

					badge = Badge.BOSS_SLAIN_3_ALL_SUBCLASSES;
					if (!global.contains(badge)) {
						displayBadge(badge);
						global.add(badge);
						saveNeeded = true;
					}
				}
			}
		}
	}
	
	public static void validateOrbObtained()  {
		    Badge badge = Badge.ORB;
	    	local.add(badge);
			displayBadge(badge);
			if (!global.contains(Badge.ORB)) {
				global.add(badge);		
			}
		}
	
	public static void validateOtilukeRescued()  {
		if (!local.contains(Badge.OTILUKE)) {
			Badge badge = Badge.OTILUKE;
			local.add(badge);
			displayBadge(badge);
		}
	}

   public static boolean checkOtilukeRescued()  {
	   return local.contains(Badge.OTILUKE); 
   }


	public static void validateMastery() {

		Badge badge = null;
		switch (Dungeon.hero.heroClass) {
		case WARRIOR:
			badge = Badge.MASTERY_WARRIOR;
			break;
		case MAGE:
			badge = Badge.MASTERY_MAGE;
			break;
		case ROGUE:
			badge = Badge.MASTERY_ROGUE;
			break;
		case HUNTRESS:
			badge = Badge.MASTERY_HUNTRESS;
			break;
		}

		if (!global.contains(badge)) {
			global.add(badge);
			saveNeeded = true;
		}
	}

	public static void validateVictory() {

		Badge badge = Badge.VICTORY;
		displayBadge(badge);

		switch (Dungeon.hero.heroClass) {
		case WARRIOR:
			badge = Badge.VICTORY_WARRIOR;
			break;
		case MAGE:
			badge = Badge.VICTORY_MAGE;
			break;
		case ROGUE:
			badge = Badge.VICTORY_ROGUE;
			break;
		case HUNTRESS:
			badge = Badge.VICTORY_HUNTRESS;
			break;
		}
		local.add(badge);
		if (!global.contains(badge)) {
			global.add(badge);
			saveNeeded = true;
		}

		if (global.contains(Badge.VICTORY_WARRIOR)
				&& global.contains(Badge.VICTORY_MAGE)
				&& global.contains(Badge.VICTORY_ROGUE)
				&& global.contains(Badge.VICTORY_HUNTRESS)) {

			badge = Badge.VICTORY_ALL_CLASSES;
			displayBadge(badge);
		}
	}

	public static void validateNoKilling() {
		if (!local.contains(Badge.NO_MONSTERS_SLAIN)
				&& Statistics.completedWithNoKilling) {
			Badge badge = Badge.NO_MONSTERS_SLAIN;
			local.add(badge);
			displayBadge(badge);
		}
	}

	public static void validateSupporter() {
		loadGlobal();
		global.add(Badge.SUPPORTER);
		saveNeeded = true;
	}

	public static void validateSupporter2() {
		loadGlobal();
		global.add(Badge.SUPPORTER2);
		saveNeeded = true;
	}

	public static void validateChampion() {
		displayBadge(Badge.CHAMPION);
	}

	private static void displayBadge(Badge badge) {

		if (badge == null) {
			return;
		}

		if (global.contains(badge)) {
		} else {
			global.add(badge);
			saveNeeded = true;
		}
	}

	public static boolean isUnlocked(Badge badge) {
		return global.contains(badge);
	}

	public static void disown(Badge badge) {
		loadGlobal();
		global.remove(badge);
		saveNeeded = true;
	}

	public static List<Badge> filtered(boolean global) {

		HashSet<Badge> filtered = new HashSet<Badge>(global ? Badges.global
				: Badges.local);

		if (!global) {
			Iterator<Badge> iterator = filtered.iterator();
			while (iterator.hasNext()) {
				Badge badge = iterator.next();
				if (badge.meta) {
					iterator.remove();
				}
			}
		}

		leaveBest(filtered, Badge.BOSS_SLAIN_1, Badge.BOSS_SLAIN_2,
				Badge.BOSS_SLAIN_3, Badge.BOSS_SLAIN_4);
		leaveBest(filtered, Badge.BOSS_SLAIN_1_ALL_CLASSES,
				Badge.BOSS_SLAIN_3_ALL_SUBCLASSES);
		leaveBest(filtered, Badge.VICTORY, Badge.VICTORY_ALL_CLASSES);


		ArrayList<Badge> list = new ArrayList<Badge>(filtered);
		Collections.sort(list);

		return list;
	}

	private static void leaveBest(HashSet<Badge> list, Badge... badges) {
		for (int i = badges.length - 1; i > 0; i--) {
			if (list.contains(badges[i])) {
				for (int j = 0; j < i; j++) {
					list.remove(badges[j]);
				}
				break;
			}
		}
	}
}
