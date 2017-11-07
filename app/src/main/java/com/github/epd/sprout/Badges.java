
package com.github.epd.sprout;

import android.content.Context;

import com.watabou.gltextures.TextureCache;
import com.watabou.noosa.Game;
import com.watabou.utils.Bundle;
import com.watabou.utils.Callback;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashSet;

public class Badges {

	public enum Badge {
		MASTERY_WARRIOR, MASTERY_MAGE, MASTERY_ROGUE, MASTERY_HUNTRESS, SUPPORTER, SUPPORTER2, OTILUKE;

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
	private static HashSet<Badge> local = new HashSet<>();

	private static boolean saveNeeded = false;

	public static Callback loadingListener = null;

	public static void reset() {
		local.clear();
		loadGlobal();
	}

	private static final String BADGES_FILE = "badges.dat";
	private static final String BADGES = "badges";

	private static HashSet<Badge> restore(Bundle bundle) {
		HashSet<Badge> badges = new HashSet<>();

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
				global = new HashSet<>();
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
				File file = new File(TextureCache.context.getExternalFilesDir(null), BADGES_FILE);
				Bundle.writeext(bundle, file);
				output.close();
				saveNeeded = false;
			} catch (IOException e) {

			}
		}
	}

	public static void validateOtilukeRescued() {
		if (!local.contains(Badge.OTILUKE)) {
			Badge badge = Badge.OTILUKE;
			local.add(badge);
			displayBadge(badge);
		}
	}

	public static boolean checkOtilukeRescued() {
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

	/*public static void validateSupporter() {
		loadGlobal();
		global.add(Badge.SUPPORTER);
		saveNeeded = true;
	}

	public static void validateSupporter2() {
		loadGlobal();
		global.add(Badge.SUPPORTER2);
		saveNeeded = true;
	}
*/
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
}
