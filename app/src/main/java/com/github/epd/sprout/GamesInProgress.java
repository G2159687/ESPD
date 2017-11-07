
package com.github.epd.sprout;

import com.github.epd.sprout.actors.hero.HeroClass;
import com.watabou.utils.Bundle;

import java.util.HashMap;

public class GamesInProgress {

	private static HashMap<HeroClass, Info> state = new HashMap<HeroClass, Info>();

	public static Info check(HeroClass cl) {

		if (state.containsKey(cl)) {

			return state.get(cl);

		} else {

			Info info;
			try {

				Bundle bundle = Dungeon.gameBundle(Dungeon.gameFile(cl));
				info = new Info();
				Dungeon.preview(info, bundle);

			} catch (Exception e) {
				info = null;
			}

			state.put(cl, info);
			return info;

		}
	}

	public static void set(HeroClass cl, int depth, int level,
	                       boolean challenges) {
		Info info = new Info();
		info.depth = depth;
		info.level = level;
		info.challenges = challenges;
		state.put(cl, info);
	}

	public static void setUnknown(HeroClass cl) {
		state.remove(cl);
	}

	public static void delete(HeroClass cl) {
		state.put(cl, null);
	}

	public static class Info {
		public int depth;
		public int level;
		public boolean challenges;
	}
}
